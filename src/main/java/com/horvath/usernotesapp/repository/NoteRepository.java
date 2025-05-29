package com.horvath.usernotesapp.repository;

import com.horvath.usernotesapp.domain.Note;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NoteRepository extends CrudRepository<Note, Long> {
    List<Note> findByUserId(Long userId);
}
