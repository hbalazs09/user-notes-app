package com.horvath.usernotesapp.service;

import com.horvath.usernotesapp.api.dto.UserDto;
import com.horvath.usernotesapp.domain.User;
import jakarta.persistence.EntityNotFoundException;

/**
 * UserService responsible for managing user-related operations.
 * Handles creation, retrieval, soft-deletion, and updating of users.
 */
public interface UserService {

    /**
     * Creates and persists a new user entity.
     *
     * @param userDto DTO containing user data
     * @return the created User entity
     */
    User createUser(UserDto userDto);

    /**
     * Retrieves a user by ID if they are not marked as deleted.
     *
     * @param id user ID
     * @return the found User entity
     * @throws EntityNotFoundException if the user is not found or deleted
     */
    User getUserById(Long id);

    /**
     * Marks a user and all their notes as deleted (soft delete).
     *
     * @param id user ID
     * @throws EntityNotFoundException if the user is not found
     */
    void deleteUser(Long id);

    /**
     * Updates an existing user.
     *
     * @param userId         ID of the user to update
     * @param updatedUserDto DTO containing the new values
     * @return the updated User entity
     * @throws EntityNotFoundException if the user is not found or deleted
     */
    User updateUser(Long userId, UserDto updatedUserDto);
}
