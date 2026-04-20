package com.sia.service;

import com.sia.dto.UserCreateDTO;
import com.sia.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserCreateDTO dto);

    UserDTO getUserById(Integer id);

    List<UserDTO> getAllUsers();

    UserDTO getUserByUsername(String username);

    void deleteUser(Integer id);

    UserDTO updateUser(Integer id, UserCreateDTO dto);
}
