package com.horvath.usernotesapp.service.impl;

import com.horvath.usernotesapp.api.dto.NoteDto;
import com.horvath.usernotesapp.domain.Note;
import com.horvath.usernotesapp.domain.User;
import com.horvath.usernotesapp.repository.NoteRepository;
import com.horvath.usernotesapp.repository.UserRepository;
import com.horvath.usernotesapp.service.NoteService;
import com.horvath.usernotesapp.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static jakarta.transaction.Transactional.TxType.REQUIRED;

/**
 * Implementation of NoteService responsible for managing note-related operations.
 * Handles creation, retrieval, soft-deletion, and updating of notes for a user.
 */
@Service
public class NoteServiceImpl implements NoteService {

    private final UserRepository userRepository;
    private final NoteRepository noteRepository;
    private final UserService userService;

    @Autowired
    public NoteServiceImpl(UserRepository userRepository, NoteRepository noteRepository, UserService userService) {
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
        this.userService = userService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(REQUIRED)
    public Note createNoteForUser(Long userId, NoteDto noteDto) {
        User user = userRepository.findById(userId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Note note = new Note();
        note.setUser(user);
        note.setText(noteDto.getText());
        note.setCompleted(noteDto.getCompleted());
        return noteRepository.save(note);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Note> getNotesByUser(Long userId) {
        return userService.getUserById(userId).getNotes().stream().filter(note -> !note.isDeleted()).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(REQUIRED)
    public void deleteNote(Long noteId) {
        Note note = noteRepository.findById(noteId).filter(n -> !n.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("Note not found"));
        note.setDeleted(true);
        noteRepository.save(note);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(REQUIRED)
    public Note updateNote(Long noteId, NoteDto updatedNoteDto) {
        Note note = noteRepository.findById(noteId).filter(n -> !n.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("Note not found"));
        note.setText(updatedNoteDto.getText());
        note.setCompleted(updatedNoteDto.getCompleted());
        return noteRepository.save(note);
    }
}
