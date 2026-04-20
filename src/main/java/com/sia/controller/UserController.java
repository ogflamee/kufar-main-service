package com.sia.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.sia.dto.UserCreateDTO;
import com.sia.dto.UserDTO;
import com.sia.service.UserService;

import java.util.List;

/**
 * REST-контроллер для работы с пользователями.
 * предоставляет endpoints для создания, получения,
 * обновления и удаления поьзователей.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDTO createUser(@Valid @RequestBody UserCreateDTO dto) {
        return userService.createUser(dto);
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable @Positive Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable @Positive Integer id) {
        userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable @Positive Integer id,
                              @Valid @RequestBody UserCreateDTO dto) {
        return userService.updateUser(id, dto);
    }
}
