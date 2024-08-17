package com.aminoacids.youtubenotes.services;

import com.aminoacids.youtubenotes.dao.*;
import com.aminoacids.youtubenotes.model.File;
import com.aminoacids.youtubenotes.model.Note;
import com.aminoacids.youtubenotes.model.Sharing;
import com.aminoacids.youtubenotes.model.User;
import com.aminoacids.youtubenotes.repositories.FileRepo;
import com.aminoacids.youtubenotes.repositories.NoteRepo;
import com.aminoacids.youtubenotes.repositories.SharingRepo;
import com.aminoacids.youtubenotes.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.plaf.nimbus.State;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepo fileRepo;
    private final UserRepo userRepo;
    private final JWTService jwtService;
    private final NoteRepo noteRepo;
    private final SharingRepo sharingRepo;

    public GetFileListResponse getAll(String token) {
        String email = jwtService.extractBodyEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow();
        List<File> files = fileRepo.findByOwner(user).orElseThrow();
        List<Sharing> sharedFiles = sharingRepo.findByReceiverId(user.getId()).orElseThrow();
        List<FileDAO> fileDAOS = new ArrayList<>();
        for (File file : files) {
            fileDAOS.add(FileDAO.builder()
                    .id(file.getId())
                    .name(file.getName())
                    .owner(file.getOwner().getEmail())
                    .canEdit(true)
                    .videoURL(file.getVideoURL())
                    .lastModified(file.getLastModified())
                    .build());
        }
        for (Sharing sharing : sharedFiles) {
            fileDAOS.add(FileDAO.builder()
                    .id(sharing.getFile().getId())
                    .name(sharing.getFile().getName())
                    .owner(sharing.getFile().getOwner().getEmail())
                    .canEdit(sharing.isCanEdit())
                    .videoURL(sharing.getFile().getVideoURL())
                    .lastModified(sharing.getFile().getLastModified())
                    .build());
        }
        token = jwtService.refreshToken(token);
        return GetFileListResponse.builder()
                .fileList(fileDAOS)
                .jwtToken(token)
                .build();
    }

    public GetFileResponse getFile(String token, Long fileID) {
        String email = jwtService.extractBodyEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow();
        File file = fileRepo.findById(fileID).orElseThrow();
        List<Note> notes = noteRepo.findByFile(file).orElseThrow();
        List<NoteDAO> noteDAOS = new ArrayList<>();
        for (Note note : notes) {
            noteDAOS.add(NoteDAO.builder()
                    .id(note.getId())
                    .body(note.getBody())
                    .time(note.getTime())
                    .build());
        }
        token = jwtService.refreshToken(token);
        if (Objects.equals(file.getOwner().getId(), user.getId())) {
            return GetFileResponse.builder()
                    .owner(user.getEmail())
                    .name(file.getName())
                    .jwtToken(token)
                    .canEdit(true)
                    .id(file.getId())
                    .videoURL(file.getVideoURL())
                    .notes(noteDAOS)
                    .build();
        } else if (sharingRepo.findByFileIdAndReceiverId(fileID, user.getId()).isPresent()) {
            return GetFileResponse.builder()
                    .owner(file.getOwner().getEmail())
                    .name(file.getName())
                    .jwtToken(token)
                    .canEdit(sharingRepo.findByFileIdAndReceiverId(fileID, user.getId()).get().isCanEdit())
                    .id(file.getId())
                    .videoURL(file.getVideoURL())
                    .notes(noteDAOS)
                    .build();
        } else {
            throw new RuntimeException("You don't have permission to access this file");
        }
    }

    public CreateFileResponse createFile(CreateFileRequest createFileRequest, String token) {
        String email = jwtService.extractBodyEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow();
        File file = File.builder()
                .name(createFileRequest.getName())
                .videoURL(createFileRequest.getVideoURL())
                .owner(user)
                .lastModified(new Date(System.currentTimeMillis()))
                .build();
        fileRepo.save(file);
        token = jwtService.refreshToken(token);
        List<File> files = fileRepo.findByOwner(user).orElseThrow();
        return CreateFileResponse.builder()
                .id(files.get(files.size() - 1).getId())
                .jwtToken(token)
                .build();
    }

    public StateResponse deleteFile(Long fileID, String token) {
        String email = jwtService.extractBodyEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow();
        File file = fileRepo.findById(fileID).orElseThrow();
        if (sharingRepo.findByFileIdAndReceiverId(fileID, user.getId()).isPresent()){
            Sharing sharing = sharingRepo.findByFileIdAndReceiverId(fileID, user.getId()).get();
            sharingRepo.delete(sharing);
            token = jwtService.refreshToken(token);
            return StateResponse.builder()
                    .isSuccessful(true)
                    .jwtToken(token)
                    .build();
        }
        if (!Objects.equals(file.getOwner().getEmail(), email))
            throw new RuntimeException("You don't have permission to delete this file");
        noteRepo.deleteAllByFile(file);
        fileRepo.delete(file);
        token = jwtService.refreshToken(token);
        return StateResponse.builder()
                .isSuccessful(true)
                .jwtToken(token)
                .build();
    }

    public AddNoteResponse addNote(Long fileID, String token, AddNoteRequest addNoteRequest) {
        String email = jwtService.extractBodyEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow();
        File file = fileRepo.findById(fileID).orElseThrow();
        if (sharingRepo.findByFileIdAndReceiverId(fileID, user.getId()).isEmpty()
                && !Objects.equals(file.getOwner().getEmail(), email))
            throw new RuntimeException("You don't have permission to add notes to this file");
        Note note = Note.builder()
                .time(addNoteRequest.getTime())
                .body(addNoteRequest.getBody())
                .file(file)
                .build();
        noteRepo.save(note);
        List<Note> notes = noteRepo.findByFile(file).orElseThrow();
        token = jwtService.refreshToken(token);
        return AddNoteResponse.builder()
                .id(notes.get(notes.size() - 1).getId())
                .jwtToken(token)
                .build();
    }

    public StateResponse replaceNote(String token, ReplaceNoteRequest replaceNoteRequest) {
        String email = jwtService.extractBodyEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow();
        File file = noteRepo.findById(replaceNoteRequest.getId()).orElseThrow().getFile();
        if (sharingRepo.findByFileIdAndReceiverId(file.getId(), user.getId()).isEmpty()
                && !Objects.equals(file.getOwner().getEmail(), email))
            throw new RuntimeException("You don't have permission to add notes to this file");
        Note note = noteRepo.findById(replaceNoteRequest.getId()).get();
        note.setTime(replaceNoteRequest.getTime());
        note.setBody(replaceNoteRequest.getBody());
        noteRepo.save(note);
        token = jwtService.refreshToken(token);
        return StateResponse.builder()
                .isSuccessful(true)
                .jwtToken(token)
                .build();
    }

    public StateResponse deleteNote(Long noteID, String token) {
        Note note = noteRepo.findById(noteID).orElseThrow();
        String email = jwtService.extractBodyEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow();
        File file = note.getFile();
        if (sharingRepo.findByFileIdAndReceiverId(file.getId(), user.getId()).isEmpty()
                && !Objects.equals(file.getOwner().getEmail(), email))
            throw new RuntimeException("You don't have permission to add notes to this file");
        noteRepo.delete(note);
        token = jwtService.refreshToken(token);
        return StateResponse.builder()
                .isSuccessful(true)
                .jwtToken(token)
                .build();
    }

    public StateResponse shareFile(String token, ShareFileRequest shareFileRequest) {
        String email = jwtService.extractBodyEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow();
        if (!Objects.equals(fileRepo.findById(shareFileRequest.getFileId()).orElseThrow().getOwner().getEmail(), email)
                && !sharingRepo.findByFileIdAndReceiverId(shareFileRequest.getFileId(), user.getId()).orElseThrow().isCanEdit())
            throw new RuntimeException("You don't have permission to share this file");
        Optional<Sharing> existingSharing = sharingRepo.findByFileIdAndReceiverId(shareFileRequest.getFileId(), userRepo.findByEmail(shareFileRequest.getEmail()).orElseThrow().getId());
        Sharing sharing;
        if (existingSharing.isPresent()
                && existingSharing.get().getFile().getId() == shareFileRequest.getFileId()
                && existingSharing.get().getReceiver() == userRepo.findByEmail(shareFileRequest.getEmail()).orElseThrow()){
            if (existingSharing.get().isCanEdit() == shareFileRequest.isCanEdit()){
                throw new RuntimeException("You have already shared this file with this user");
            } else {
                sharing = existingSharing.get();
                sharing.setCanEdit(shareFileRequest.isCanEdit());
            }
        } else {
            sharing = Sharing.builder()
                    .file(fileRepo.findById(shareFileRequest.getFileId()).orElseThrow())
                    .receiver(userRepo.findByEmail(shareFileRequest.getEmail()).orElseThrow())
                    .owner(user)
                    .canEdit(shareFileRequest.isCanEdit())
                    .build();
        }
        sharingRepo.save(sharing);
        token = jwtService.refreshToken(token);
        return StateResponse.builder()
                .isSuccessful(true)
                .jwtToken(token)
                .build();
    }

    public StateResponse writeToFile(String token, WriteFileRequest writeFileRequest) {
        String email = jwtService.extractBodyEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow();
        File file = fileRepo.findById(writeFileRequest.getId()).orElseThrow();
        if (sharingRepo.findByFileIdAndReceiverId(file.getId(), user.getId()).isEmpty()
                && !Objects.equals(file.getOwner().getEmail(), email))
            throw new RuntimeException("You don't have permission to write to this file");
        file.setName(writeFileRequest.getName());
        file.setLastModified(new Date(System.currentTimeMillis()));
        fileRepo.save(file);
        token = jwtService.refreshToken(token);
        return StateResponse.builder()
                .isSuccessful(true)
                .jwtToken(token)
                .build();
    }

    public GetNoteResponse getNoteByID(Long noteID, String token) {
        String email = jwtService.extractBodyEmail(token);
        User user = userRepo.findByEmail(email).orElseThrow();
        Note note = noteRepo.findById(noteID).orElseThrow();
        File file = note.getFile();
        if (sharingRepo.findByFileIdAndReceiverId(file.getId(), user.getId()).isEmpty()
                && !Objects.equals(file.getOwner().getEmail(), email))
            throw new RuntimeException("You don't have permission to add notes to this file");
        token = jwtService.refreshToken(token);
        return GetNoteResponse.builder()
                .id(note.getId())
                .time(note.getTime())
                .body(note.getBody())
                .jwtToken(token)
                .build();
    }
}
