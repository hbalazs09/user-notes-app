package com.horvath.usernotesapp.service.impl;

import com.horvath.usernotesapp.api.dto.NoteDto;
import com.horvath.usernotesapp.domain.Note;
import com.horvath.usernotesapp.domain.User;
import com.horvath.usernotesapp.repository.NoteRepository;
import com.horvath.usernotesapp.repository.UserRepository;
import com.horvath.usernotesapp.service.UserService;
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
public class NoteServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private NoteServiceImpl noteService;

    private User user;
    private Note note;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");

        note = new Note();
        note.setId(1L);
        note.setText("Initial Text");
        note.setUser(user);
        note.setDeleted(false);
        user.setNotes(List.of(note));
    }

    @Test
    void createNoteForUser_shouldCreateNoteSuccessfully() {
        NoteDto dto = new NoteDto();
        dto.setText("New Note");
        dto.setCompleted(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(noteRepository.save(any(Note.class))).thenAnswer(i -> i.getArguments()[0]);

        Note created = noteService.createNoteForUser(1L, dto);

        assertEquals("New Note", created.getText());
        assertEquals(user, created.getUser());
    }

    @Test
    void getNotesByUser_shouldReturnNonDeletedNotes() {
        when(userService.getUserById(1L)).thenReturn(user);
        List<Note> result = noteService.getNotesByUser(1L);

        assertEquals(1, result.size());
        assertEquals("Initial Text", result.getFirst().getText());
    }

    @Test
    void deleteNote_shouldSetDeletedToTrue() {
        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));

        noteService.deleteNote(1L);

        assertTrue(note.isDeleted());
        verify(noteRepository).save(note);
    }

    @Test
    void updateNote_shouldUpdateTextAndCompletedFields() {
        NoteDto dto = new NoteDto();
        dto.setText("Updated Text");
        dto.setCompleted(true);

        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));
        when(noteRepository.save(any(Note.class))).thenAnswer(i -> i.getArguments()[0]);

        Note updated = noteService.updateNote(1L, dto);

        assertEquals("Updated Text", updated.getText());
        assertTrue(updated.isCompleted());
    }

    @Test
    void createNoteForUser_shouldThrowIfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        NoteDto dto = new NoteDto();
        dto.setText("Text");

        assertThrows(EntityNotFoundException.class, () -> noteService.createNoteForUser(1L, dto));
    }
}
