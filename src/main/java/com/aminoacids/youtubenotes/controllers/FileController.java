package com.aminoacids.youtubenotes.controllers;

import com.aminoacids.youtubenotes.dao.*;
import com.aminoacids.youtubenotes.dao.StateResponse;
import com.aminoacids.youtubenotes.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    @GetMapping("/getall")
    public ResponseEntity<GetFileListResponse> getAll(@RequestHeader(name="Authorization") String token) {
        return ResponseEntity.ok(fileService.getAll(token));
    }

    @GetMapping("/get/{fileID}")
    public ResponseEntity<GetFileResponse> getFile(@PathVariable Long fileID, @RequestHeader(name="Authorization") String token) {
        return ResponseEntity.ok(fileService.getFile(token, fileID));
    }

    @PostMapping("/update")
    public ResponseEntity<StateResponse> writeToFile(@RequestHeader(name="Authorization") String token, @RequestBody WriteFileRequest writeFileRequest) {
        return ResponseEntity.ok(fileService.writeToFile(token, writeFileRequest));
    }

    @PostMapping ("/create")
    public ResponseEntity<CreateFileResponse> createFile(@RequestHeader("Authorization") String token, @RequestBody CreateFileRequest createFileRequest) {
        return ResponseEntity.ok(fileService.createFile(createFileRequest, token));
    }

    @DeleteMapping("/delete/{videoID}")
    public ResponseEntity<StateResponse> deleteFile(@PathVariable Long videoID, @RequestHeader(name="Authorization") String token) {
        return ResponseEntity.ok(fileService.deleteFile(videoID, token));
    }

    @PostMapping("/note/add/{videoID}")
    public ResponseEntity<AddNoteResponse> addNote(@PathVariable Long videoID, @RequestHeader(name="Authorization") String token, @RequestBody AddNoteRequest addNoteRequest) {
        return ResponseEntity.ok(fileService.addNote(videoID, token, addNoteRequest));
    }

    @PostMapping("/note/replace")
    public ResponseEntity<StateResponse> replaceNote(@RequestHeader(name="Authorization") String token, @RequestBody ReplaceNoteRequest replaceNoteRequest) {
        return ResponseEntity.ok(fileService.replaceNote(token, replaceNoteRequest));
    }

    @DeleteMapping("/note/delete/{noteID}")
    public ResponseEntity<StateResponse> deleteNote(@PathVariable Long noteID, @RequestHeader(name="Authorization") String token) {
        return ResponseEntity.ok(fileService.deleteNote(noteID, token));
    }

    @GetMapping("/note/get/{noteID}")
    public ResponseEntity<GetNoteResponse> getNoteByID(@PathVariable Long noteID, @RequestHeader(name="Authorization") String token) {
        return ResponseEntity.ok(fileService.getNoteByID(noteID, token));
    }

    @PostMapping("/share")
    public ResponseEntity<StateResponse> shareFile(@RequestHeader(name="Authorization") String token, @RequestBody ShareFileRequest shareFileRequest) {
        return ResponseEntity.ok(fileService.shareFile(token, shareFileRequest));
    }

}
