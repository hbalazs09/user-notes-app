package com.horvath.usernotesapp.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.horvath.usernotesapp.api.dto.NoteDto;
import com.horvath.usernotesapp.api.dto.UserDto;
import com.horvath.usernotesapp.domain.Note;
import com.horvath.usernotesapp.domain.User;
import com.horvath.usernotesapp.service.NoteService;
import com.horvath.usernotesapp.service.UserService;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NoteControllerTest {

    private final MockMvc mockMvc;
    private final UserService userService;
    private final NoteService noteService;
    private final ObjectMapper objectMapper;
    private final Flyway flyway;

    private static final String BASE_URL = "/web/api/note";

    @Autowired
    public NoteControllerTest(MockMvc mockMvc,
                              UserService userService,
                              NoteService noteService,
                              ObjectMapper objectMapper,
                              Flyway flyway) {
        this.mockMvc = mockMvc;
        this.userService = userService;
        this.noteService = noteService;
        this.objectMapper = objectMapper;
        this.flyway = flyway;
    }

    @BeforeEach
    void resetDatabase() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void createNote_shouldReturnCreatedNote() throws Exception {
        Long userId = createUserAndGetId();

        NoteDto noteDto = new NoteDto();
        noteDto.setText("Test note");
        noteDto.setCompleted(false);

        mockMvc.perform(post(BASE_URL + "/user/" + userId + "/create-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.text").value("Test note"));
    }

    @Test
    void getNotesByUser_shouldReturnNotes() throws Exception {
        Long userId = createUserAndGetId();
        NoteDto noteDto = new NoteDto();
        noteDto.setText("Test note");
        noteDto.setCompleted(false);
        noteService.createNoteForUser(userId, noteDto);

        mockMvc.perform(get(BASE_URL + "/user/" + userId + "/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].text", is("Test note")));
    }

    @Test
    void updateNote_shouldUpdateAndReturnNote() throws Exception {
        Long userId = createUserAndGetId();
        NoteDto noteDto = new NoteDto();
        noteDto.setText("Original Text");
        noteDto.setCompleted(false);
        Note note = noteService.createNoteForUser(userId, noteDto);

        NoteDto updateDto = new NoteDto();
        updateDto.setText("Updated Text");
        updateDto.setCompleted(true);

        mockMvc.perform(put(BASE_URL + "/" + note.getId() + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Updated Text"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void deleteNote_shouldSoftDeleteNote() throws Exception {
        Long userId = createUserAndGetId();
        NoteDto noteDto = new NoteDto();
        noteDto.setText("Original Text");
        noteDto.setCompleted(false);
        Note note = noteService.createNoteForUser(userId, noteDto);
        mockMvc.perform(delete(BASE_URL + "/" + note.getId() + "/delete"))
                .andExpect(status().isOk());
    }

    @Test
    void updateNote_shouldReturnNotFound() throws Exception {
        NoteDto updateDto = new NoteDto();
        updateDto.setText("Updated Text");
        updateDto.setCompleted(true);

        mockMvc.perform(put(BASE_URL + "/9999/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createNote_shouldReturnBadRequest() throws Exception {
        NoteDto updateDto = new NoteDto();
        updateDto.setCompleted(true);
        mockMvc.perform(post(BASE_URL + "/user/1/create-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getNotesByUser_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/user/999/notes"))
                .andExpect(status().isNotFound());
    }

    private Long createUserAndGetId() {
        UserDto userDto = new UserDto();
        userDto.setName("user");
        User user = userService.createUser(userDto);
        return user.getId();
    }
}