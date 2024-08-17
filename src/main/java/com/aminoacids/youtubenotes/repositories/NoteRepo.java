package com.aminoacids.youtubenotes.repositories;

import com.aminoacids.youtubenotes.model.File;
import com.aminoacids.youtubenotes.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepo extends JpaRepository<Note, Long> {
    Optional<List<Note>> findByFile(File file);
    Optional<List<Note>> findByFileId(Long fileId);

    void deleteAllByFile(File file);
}
