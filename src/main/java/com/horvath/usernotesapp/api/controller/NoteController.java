package com.horvath.usernotesapp.api.controller;

import com.horvath.usernotesapp.api.NoteApi;
import com.horvath.usernotesapp.api.dto.NoteDto;
import com.horvath.usernotesapp.domain.Note;
import com.horvath.usernotesapp.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NoteController implements NoteApi {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @Override
    public ResponseEntity<List<NoteDto>> getNotesByUser(Long userId) {
        return new ResponseEntity<>(
                noteService.getNotesByUser(userId).stream().map(this::mapDtoObject).toList(),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<NoteDto> createNote(Long userId, NoteDto noteDto) {
        NoteDto createdNoteDto = mapDtoObject(noteService.createNoteForUser(userId, noteDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNoteDto);
    }

    @Override
    public ResponseEntity<NoteDto> updateNote(Long noteId, NoteDto noteDto) {
        NoteDto updatedNoteDto = mapDtoObject(noteService.updateNote(noteId, noteDto));
        return ResponseEntity.ok(updatedNoteDto);
    }

    @Override
    public ResponseEntity<Void> deleteNote(Long noteId) {
        noteService.deleteNote(noteId);
        return ResponseEntity.ok().build();
    }

    private NoteDto mapDtoObject(Note note) {
        NoteDto noteDto = new NoteDto();
        noteDto.setId(note.getId());
        noteDto.setText(note.getText());
        noteDto.setCompleted(note.isCompleted());
        return noteDto;
    }
}
