package com.horvath.usernotesapp.repository;

import com.horvath.usernotesapp.domain.Note;
import org.springframework.data.repository.CrudRepository;

public interface NoteRepository extends CrudRepository<Note, Long> {
}
