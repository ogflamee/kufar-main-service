package com.sia.impl.service;

import com.sia.dto.UserCreateDTO;
import com.sia.dto.UserDTO;
import com.sia.entity.User;
import com.sia.mapper.UserMapper;
import com.sia.repository.UserRepository;
import com.sia.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_success() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("test@gmail.com");
        dto.setUsername("test");
        dto.setPassword("123456");

        User user = new User();
        User saved = new User();
        saved.setId(1);

        UserDTO response = new UserDTO();
        response.setId(1);

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(dto.getUsername())).thenReturn(false);

        when(userMapper.toEntity(dto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(saved);
        when(userMapper.toDTO(saved)).thenReturn(response);

        UserDTO result = userService.createUser(dto);

        assertNotNull(result);
        assertEquals(1, result.getId());

        verify(userRepository).save(user);
    }

    @Test
    void createUser_emailExists() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("test@gmail.com");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, ()-> userService.createUser(dto));
    }

    @Test
    void createUser_usernameExists() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("test@gmail.com");
        dto.setUsername("test");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(dto.getUsername())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, ()-> userService.createUser(dto));
    }

    @Test
    void getUserById_success() {
        Integer id = 1;

        User user = new User();
        user.setId(id);

        UserDTO dto = new UserDTO();
        dto.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(dto);

        UserDTO result = userService.getUserById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getUserById_notFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, ()-> userService.getUserById(1));
    }

    @Test
    void getAllUsers_success() {
        User user = new User();
        UserDTO dto = new UserDTO();

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDTO(user)).thenReturn(dto);

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(1, result.size());
    }

    @Test
    void updateUser_success() {
        Integer id = 1;

        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("new@gmail.com");
        dto.setUsername("new");
        dto.setPassword("1234");

        User user = new User();
        user.setId(id);

        User updated = new User();
        updated.setId(id);

        UserDTO response = new UserDTO();
        response.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(updated);
        when(userMapper.toDTO(updated)).thenReturn(response);

        UserDTO result = userService.updateUser(id, dto);

        assertNotNull(result);
        assertEquals(id,result.getId());
    }

    @Test
    void updateUser_notFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, ()-> userService.updateUser(1, new UserCreateDTO()));
    }
    @Test
    void deleteUser_success(){
        when(userRepository.existsById(1)).thenReturn(true);

        userService.deleteUser(1);

        verify(userRepository).deleteById(1);
    }

    @Test
    void deleteUser_notFound() {
        when(userRepository.existsById(1)).thenReturn(false);

        assertThrows(RuntimeException.class, ()-> userService.deleteUser(1));
    }
}