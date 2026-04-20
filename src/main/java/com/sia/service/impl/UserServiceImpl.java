package com.sia.service.impl;

import com.sia.exception.ConflictException;
import com.sia.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.sia.dto.UserCreateDTO;
import com.sia.dto.UserDTO;
import com.sia.entity.User;
import com.sia.mapper.UserMapper;
import com.sia.repository.UserRepository;
import com.sia.service.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDTO createUser(UserCreateDTO dto) {
        log.info("creating user with email: {}", dto.getEmail());

        if(userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("email already exists");
        }

        if(userRepository.existsByUsername(dto.getUsername())) {
            throw new ConflictException("username already exists");
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        User saved = userRepository.save(user);

        log.info("user created with id: {}", saved.getId());

        return userMapper.toDTO(saved);
    }

    @Override
    public UserDTO getUserById(Integer id) {
        log.info("fetching user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("user not found with id: {}", id);
                    return new NotFoundException("User was not found");
                });
        return userMapper.toDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        log.info("fetching all users");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        log.info("deleting user with id: {}", id);

        if(!userRepository.existsById(id)) {
            log.warn("attempt to delete non-existing user: {}", id);
            throw new NotFoundException("user was not found");
        }

        userRepository.deleteById(id);

        log.info("user deleted with id: {}", id);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Integer id, UserCreateDTO dto) {
        log.info("updating user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("user not found with id: {}", id);
                    return new NotFoundException("user was not found");
                });

        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User updated = userRepository.save(user);

        log.info("user updated with id: {}", id);

        return userMapper.toDTO(updated);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->  {
                    log.warn("user not found with username: {}", username);
                    return new NotFoundException("user was not found");
                });
        return userMapper.toDTO(user);
    }
}
