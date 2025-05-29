package com.horvath.usernotesapp.service;

import com.horvath.usernotesapp.api.dto.NoteDto;
import com.horvath.usernotesapp.domain.Note;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

/**
 * NoteService responsible for managing note-related operations.
 * Handles creation, retrieval, soft-deletion, and updating of notes for a user.
 */
public interface NoteService {

    /**
     * Creates a new note associated with the specified user.
     *
     * @param userId  ID of the user
     * @param noteDto Data transfer object containing note details
     * @return the persisted Note entity
     * @throws EntityNotFoundException if the user is not found or deleted
     */
    Note createNoteForUser(Long userId, NoteDto noteDto);

    /**
     * Retrieves all non-deleted notes for a given user.
     *
     * @param userId ID of the user
     * @return list of non-deleted notes
     */
    List<Note> getNotesByUser(Long userId);

    /**
     * Marks a note as deleted (soft delete).
     *
     * @param noteId ID of the note to delete
     * @throws EntityNotFoundException if the note does not exist
     */
    void deleteNote(Long noteId);

    /**
     * Updates an existing note with new values.
     *
     * @param noteId         ID of the note to update
     * @param updatedNoteDto DTO containing new values
     * @return updated Note entity
     * @throws EntityNotFoundException if the note is not found or deleted
     */
    Note updateNote(Long noteId, NoteDto updatedNoteDto);
}
