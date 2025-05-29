package com.horvath.usernotesapp.api;

import com.horvath.usernotesapp.api.dto.NoteDto;
import com.horvath.usernotesapp.api.exception.ErrorResponse;
import com.horvath.usernotesapp.api.exception.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Note API", description = "Operations related to note management")
@RequestMapping("/web/api/note")
public interface NoteApi {

    @Operation(summary = "Get notes by user", description = "Retrieves all notes for the specified user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notes retrieved successfully",
                    content = @Content(schema = @Schema(implementation = NoteDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/user/{userId}/notes")
    ResponseEntity<List<NoteDto>> getNotesByUser(@PathVariable Long userId);

    @Operation(summary = "Create a new note", description = "Creates a new note for a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Note created successfully",
                    content = @Content(schema = @Schema(implementation = NoteDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/user/{userId}/create-note")
    ResponseEntity<NoteDto> createNote(@PathVariable Long userId, @RequestBody @Valid NoteDto noteDto);

    @Operation(summary = "Update an existing note", description = "Updates a specific note by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note updated successfully",
                    content = @Content(schema = @Schema(implementation = NoteDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Note not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflict during update",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PutMapping("/{noteId}/update")
    ResponseEntity<NoteDto> updateNote(@PathVariable Long noteId, @RequestBody @Valid NoteDto noteDto);

    @Operation(summary = "Delete a note", description = "Soft deletes a note by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Note not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflict during deletion",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{noteId}/delete")
    ResponseEntity<Void> deleteNote(@PathVariable Long noteId);
}
