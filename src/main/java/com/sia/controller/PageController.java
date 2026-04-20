package com.sia.controller;

import com.sia.dto.AdDTO;
import com.sia.dto.FavoriteDTO;
import com.sia.dto.MessageDTO;
import com.sia.dto.UserCreateDTO;
import com.sia.dto.UserDTO;
import com.sia.entity.AdStatus;
import com.sia.service.AdService;
import com.sia.service.CategoryService;
import com.sia.service.FavoriteService;
import com.sia.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MVC-контроллер для обработки web-запросов и отображения страниц прилоения.
 * отвечает за авторизацию, регистрацию, работу с объявлениями,
 * избранными и сообщениями ерез html-представление.
 */
@Controller
@RequiredArgsConstructor
public class PageController {

    private final AdService adService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final FavoriteService favoriteService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserCreateDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserCreateDTO userDto,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.createUser(userDto);
        } catch (Exception e) {
            bindingResult.reject("registerError", e.getMessage());
            return "register";
        }

        return "redirect:/login";
    }

    @GetMapping("/ads")
    public String adsPage(@RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "8") int size,
                          @RequestParam(required = false) Integer categoryId,
                          Authentication authentication,
                          Model model) {

        Page<AdDTO> ads;

        if (categoryId != null) {
            ads = adService.getAdsByCategory(categoryId, PageRequest.of(page, size));
        } else {
            ads = adService.getAllAds(PageRequest.of(page, size));
        }

        model.addAttribute("adsPage", ads);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("currentUserId",
                isRealAuthenticated(authentication) ? getCurrentUserId(authentication) : null);
        return "ads";
    }

    @GetMapping("/ads/create")
    public String createAdPage(Model model) {
        AdDTO ad = new AdDTO();
        ad.setStatus(AdStatus.ACTIVE);

        model.addAttribute("ad", ad);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("statuses", AdStatus.values());
        model.addAttribute("pageTitle", "Создать объявление");
        model.addAttribute("formAction", "/ads/create");
        model.addAttribute("submitLabel", "Опубликовать");
        return "ad-create";
    }

    @PostMapping("/ads/create")
    public String createAd(@Valid @ModelAttribute("ad") AdDTO adDto,
                           BindingResult bindingResult,
                           Authentication authentication,
                           Model model) {
        if (bindingResult.hasErrors()) {
            populateAdForm(model, "Создать объявление", "/ads/create", "Опубликовать");
            return "ad-create";
        }

        adDto.setUserId(getCurrentUserId(authentication));
        if (adDto.getStatus() == null) {
            adDto.setStatus(AdStatus.ACTIVE);
        }

        adService.createAd(adDto);
        return "redirect:/ads";
    }

    @GetMapping("/ads/{id}")
    public String adDetails(@PathVariable Integer id,
                            @RequestParam(required = false) Integer chatUserId,
                            Authentication authentication,
                            Model model) {

        Integer currentUserId = isRealAuthenticated(authentication) ? getCurrentUserId(authentication) : null;

        AdDTO ad = adService.getAdById(id);
        if (currentUserId == null || !currentUserId.equals(ad.getUserId())) {
            ad = adService.incrementViews(id);
        }

        boolean isOwner = currentUserId != null && currentUserId.equals(ad.getUserId());

        model.addAttribute("ad", ad);
        model.addAttribute("currentUserId", currentUserId);
        model.addAttribute("isOwner", isOwner);

        if (currentUserId != null) {
            if (!isOwner) {
                List<MessageDTO> messages = adService.getChatForAdAndUsers(id, currentUserId, ad.getUserId());
                model.addAttribute("messages", messages);
                model.addAttribute("chatReceiverId", ad.getUserId());
            } else {
                List<MessageDTO> allMessages = adService.getMessagesByAdId(id);

                List<Integer> dialogUserIds = allMessages.stream()
                        .flatMap(m -> java.util.stream.Stream.of(m.getSenderId(),
                                m.getReceiverId()))
                        .filter(userId -> !userId.equals(currentUserId))
                        .distinct()
                        .toList();

                model.addAttribute("dialogUserIds", dialogUserIds);

                if (chatUserId != null) {
                    List<MessageDTO> messages = adService.getChatForAdAndUsers(id, currentUserId, chatUserId);
                    model.addAttribute("messages", messages);
                    model.addAttribute("chatReceiverId", chatUserId);
                    model.addAttribute("selectedChatUserId", chatUserId);
                }
            }
        }

        return "ad-details";
    }

    @GetMapping("/ads/{id}/edit")
    public String editAdPage(@PathVariable Integer id,
                             Authentication authentication,
                             Model model) {
        AdDTO ad = adService.getAdById(id);
        ensureOwner(authentication, ad);

        model.addAttribute("ad", ad);
        populateAdForm(model, "Редактировать объявление",
                "/ads/" + id + "/edit", "Сохранить");
        return "ad-create";
    }

    @PostMapping("/ads/{id}/edit")
    public String editAd(@PathVariable Integer id,
                         @Valid @ModelAttribute("ad") AdDTO adDto,
                         BindingResult bindingResult,
                         Authentication authentication,
                         Model model) {
        AdDTO existingAd = adService.getAdById(id);
        ensureOwner(authentication, existingAd);

        if (bindingResult.hasErrors()) {
            populateAdForm(model, "Редактировать объявление",
                    "/ads/" + id + "/edit", "Сохранить");
            return "ad-create";
        }

        adDto.setUserId(existingAd.getUserId());
        if (adDto.getStatus() == null) {
            adDto.setStatus(existingAd.getStatus());
        }

        adService.updateAd(id, adDto);
        return "redirect:/ads/" + id;
    }

    @PostMapping("/ads/{id}/delete")
    public String deleteAd(@PathVariable Integer id,
                           Authentication authentication) {
        AdDTO ad = adService.getAdById(id);
        ensureOwner(authentication, ad);
        adService.deleteAd(id);
        return "redirect:/ads";
    }

    @PostMapping("/favorites/{adId}/add")
    public String addFavorite(@PathVariable Integer adId,
                              Authentication authentication) {
        FavoriteDTO favoriteDTO = FavoriteDTO.builder()
                .userId(getCurrentUserId(authentication))
                .adId(adId)
                .build();

        favoriteService.addToFavorite(favoriteDTO);
        return "redirect:/ads/" + adId;
    }

    @GetMapping("/favorites")
    public String favoritesPage(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "8") int size,
                                Authentication authentication,
                                Model model) {
        Integer currentUserId = getCurrentUserId(authentication);
        Page<FavoriteDTO> favorites = favoriteService.getUserFavorites(currentUserId, PageRequest.of(page, size));

        model.addAttribute("favoritesPage", favorites);
        return "favorites";
    }

    @PostMapping("/favorites/{adId}/remove")
    public String removeFavorite(@PathVariable Integer adId,
                                 Authentication authentication) {
        favoriteService.removeFromFavorites(getCurrentUserId(authentication), adId);
        return "redirect:/favorites";
    }

    @GetMapping("/chat/{adId}")
    public String openChat(@PathVariable Integer adId,
                           Authentication authentication,
                           Model model) {
        Integer currentUserId = getCurrentUserId(authentication);
        AdDTO ad = adService.getAdById(adId);
        Integer ownerId = ad.getUserId();

        if (currentUserId.equals(ownerId)) {
            throw new IllegalArgumentException("Нельзя писать самому себе");
        }

        List<MessageDTO> messages = adService.getChatForAdAndUsers(adId, currentUserId, ownerId);

        model.addAttribute("messages", messages);
        model.addAttribute("adId", adId);
        model.addAttribute("firstUserId", currentUserId);
        model.addAttribute("secondUserId", ownerId);

        return "chat";
    }

    @PostMapping("/chat/send")
    public String sendMessage(@RequestParam Integer adId,
                              @RequestParam String text,
                              @RequestParam Integer receiverId,
                              Authentication authentication) {
        Integer senderId = getCurrentUserId(authentication);
        AdDTO ad = adService.getAdById(adId);
        Integer ownerId = ad.getUserId();

        Integer actualReceiverId = receiverId;

        if(actualReceiverId == null) {
            actualReceiverId = ownerId;
        }
        if (senderId.equals(actualReceiverId)) {
            throw new IllegalArgumentException("Нельзя писать самому себе");
        }

        adService.sendMessage(adId, senderId, receiverId, text);

        if(senderId.equals(ownerId)) {
            return "redirect:/ads/" + adId + "?chatUserId=" + actualReceiverId;
        }
        return "redirect:/ads/" + adId;
    }

    private void populateAdForm(Model model, String pageTitle, String formAction, String submitLabel) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("statuses", AdStatus.values());
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("formAction", formAction);
        model.addAttribute("submitLabel", submitLabel);
    }

    private Integer getCurrentUserId(Authentication authentication) {
        UserDTO currentUser = userService.getUserByUsername(authentication.getName());
        return currentUser.getId();
    }

    private boolean isRealAuthenticated(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName());
    }

    private void ensureOwner(Authentication authentication, AdDTO ad) {
        Integer currentUserId = getCurrentUserId(authentication);
        if (!currentUserId.equals(ad.getUserId())) {
            throw new IllegalArgumentException("you can edit only your own ad");
        }
    }
}