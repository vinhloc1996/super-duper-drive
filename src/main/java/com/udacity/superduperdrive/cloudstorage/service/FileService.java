package com.udacity.superduperdrive.cloudstorage.service;

import com.udacity.superduperdrive.cloudstorage.dto.FileInfoDto;
import com.udacity.superduperdrive.cloudstorage.dto.UpdateFileInfoDto;
import com.udacity.superduperdrive.cloudstorage.helper.UtilityMethod;
import com.udacity.superduperdrive.cloudstorage.mapper.FileMapper;
import com.udacity.superduperdrive.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    private String trimFileName(String filename){
        if (UtilityMethod.IsStringNullOrEmpty(filename))
            return null;

        return filename.trim();
    }

    public void createFile(MultipartFile multipartFile, Integer userId) throws Exception {
        File newFile = new File(
                trimFileName(multipartFile.getOriginalFilename()),
                multipartFile.getContentType(),
                String.valueOf(multipartFile.getSize()),
                userId,
                multipartFile.getBytes()
        );

        fileMapper.insert(newFile);
    }

    public List<FileInfoDto> getFilesByUserId(Integer userId){
        return fileMapper.getListFileInfoByUserId(userId);
    }

    public File getFileById(Integer fileId){
        return fileMapper.getFileById(fileId);
    }

    //TODO: Implement logic to check name and allow increasing version number or replacing entire file if the name is the same
    public void updateFile(UpdateFileInfoDto fileInfoDto){
        fileMapper.updateFile(fileInfoDto);
    }

    public void deleteFile(Integer fileId){
        fileMapper.deleteFile(fileId);
    }

    public boolean isFilenameExisted(String filename, Integer userId) {
        Integer checkExisted = fileMapper.checkFileNameExists(trimFileName(filename), userId);
        return !(checkExisted == null || checkExisted == 0);
    }


}