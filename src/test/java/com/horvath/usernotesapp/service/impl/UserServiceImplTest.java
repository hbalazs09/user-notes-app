package com.horvath.usernotesapp.service.impl;

import com.horvath.usernotesapp.api.dto.UserDto;
import com.horvath.usernotesapp.domain.Note;
import com.horvath.usernotesapp.domain.User;
import com.horvath.usernotesapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
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
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Alice");
        user.setDeleted(false);
        user.setNotes(List.of());
    }

    @Test
    void createUser_shouldCreateUserSuccessfully() {
        UserDto dto = new UserDto();
        dto.setName("Alice");

        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = userService.createUser(dto);

        assertEquals("Alice", result.getName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserById_shouldReturnNonDeletedUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertEquals("Alice", result.getName());
    }

    @Test
    void getUserById_shouldThrowIfUserNotFound() {
        user.setDeleted(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void deleteUser_shouldSoftDeleteUserAndNotes() {
        Note note = new Note();
        note.setDeleted(false);
        user.setNotes(List.of(note));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        assertTrue(user.isDeleted());
        assertTrue(note.isDeleted());
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_shouldUpdateName() {
        UserDto updatedDto = new UserDto();
        updatedDto.setName("Bob");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User updated = userService.updateUser(1L, updatedDto);

        assertEquals("Bob", updated.getName());
    }

    @Test
    void updateUser_shouldThrowIfUserNotFound() {
        user.setDeleted(true);
        UserDto dto = new UserDto();
        dto.setName("Someone");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(1L, dto));
    }
}
