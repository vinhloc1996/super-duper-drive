package com.udacity.superduperdrive.cloudstorage.service;

import com.udacity.superduperdrive.cloudstorage.mapper.NoteMapper;
import com.udacity.superduperdrive.cloudstorage.mapper.UserMapper;
import com.udacity.superduperdrive.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final UserMapper userMapper;
    private final NoteMapper noteMapper;

    public NoteService(UserMapper userMapper, NoteMapper noteMapper) {
        this.userMapper = userMapper;
        this.noteMapper = noteMapper;
    }

    public int createNote(String title, String description, Integer userId) {
        Note note = new Note(title, description, userId);
        return noteMapper.insert(note);
    }

    public List<Note> getNotesByUserId(Integer userId){
        return noteMapper.getNotesByUserId(userId);
    }

    public List<Note> getNotesByUsername(String username){
        Integer userId = userMapper.getUserIdByUsername(username);
        return noteMapper.getNotesByUserId(userId);
    }

    public Note getNote(Integer noteId){
        return noteMapper.getNoteById(noteId);
    }

    public void deleteNote(Integer noteId) {
        noteMapper.deleteNote(noteId);
    }

    public void updateNote(Integer noteId, String title, String description) {
        int currentVersion = noteMapper.getNoteVersionById(noteId);
        if (currentVersion <= 0)
            return;
        currentVersion += 1;
        noteMapper.updateNote(noteId, title, description, currentVersion);
    }
}