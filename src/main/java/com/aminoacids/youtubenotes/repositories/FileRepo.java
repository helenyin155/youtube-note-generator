package com.aminoacids.youtubenotes.repositories;

import com.aminoacids.youtubenotes.model.File;
import com.aminoacids.youtubenotes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepo extends JpaRepository<File, Long> {
    Optional<List<File>> findByOwner(User ownerId);

    Optional<List<File>> findByOwnerEmail(String email);

    Optional<List<File>> findByOwnerId(Long id);
}
