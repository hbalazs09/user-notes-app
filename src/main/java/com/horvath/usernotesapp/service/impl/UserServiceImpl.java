package com.horvath.usernotesapp.service.impl;

import com.horvath.usernotesapp.api.dto.UserDto;
import com.horvath.usernotesapp.domain.User;
import com.horvath.usernotesapp.repository.UserRepository;
import com.horvath.usernotesapp.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static jakarta.transaction.Transactional.TxType.REQUIRED;

/**
 * Implementation of UserService responsible for managing user-related operations.
 * Handles creation, retrieval, soft-deletion, and updating of users.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(REQUIRED)
    public User createUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        return userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .filter(user -> !user.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(REQUIRED)
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).filter(u -> !u.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setDeleted(true);
        user.getNotes().forEach(note -> note.setDeleted(true));
        userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(REQUIRED)
    public User updateUser(Long userId, UserDto updatedUserDto) {
        User user = userRepository.findById(userId).filter(u -> !u.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setName(updatedUserDto.getName());
        return userRepository.save(user);
    }
}
