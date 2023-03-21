package com.udacity.superduperdrive.cloudstorage.dto;

public class UpdateFileInfoDto extends FileInfoDto {

    private byte[] fileData;

    public UpdateFileInfoDto(int fileId, int version, String fileName, String contentType, String fileSize, Integer userId, byte[] fileData) {
        super(fileId, version, fileName, contentType, fileSize, userId);
        this.fileData = fileData;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}
