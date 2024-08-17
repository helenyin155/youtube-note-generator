package com.aminoacids.youtubenotes.repositories;

import com.aminoacids.youtubenotes.model.File;
import com.aminoacids.youtubenotes.model.Sharing;
import com.aminoacids.youtubenotes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SharingRepo extends JpaRepository<Sharing, Long> {
    Optional<List<Sharing>> findByFile(File file);

    Optional<List<Sharing>> findByReceiver(User receiver);

    Optional<List<Sharing>> findByOwner(User owner);

    Optional<List<Sharing>> findByFileId(Long fileId);

    Optional<List<Sharing>> findByReceiverId(Long receiverId);

    Optional<List<Sharing>> findByOwnerId(Long ownerId);

    Optional<Sharing> findByFileIdAndReceiverId(Long fileId, Long receiverId);
}
