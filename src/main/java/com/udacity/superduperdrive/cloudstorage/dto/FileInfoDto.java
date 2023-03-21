package com.udacity.superduperdrive.cloudstorage.dto;

public class FileInfoDto {

    private int version;
    private int fileId;
    private String fileName;
    private String contentType;
    private String fileSize;
    private Integer userId;

    public FileInfoDto(int fileId, int version, String fileName, String contentType, String fileSize, Integer userId) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.version = version;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.userId = userId;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
