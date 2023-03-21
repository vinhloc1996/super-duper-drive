package com.udacity.superduperdrive.cloudstorage.mapper;

import com.udacity.superduperdrive.cloudstorage.dto.FileInfoDto;
import com.udacity.superduperdrive.cloudstorage.dto.UpdateFileInfoDto;
import com.udacity.superduperdrive.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT 1 FROM FILES WHERE filename = #{fileName} AND userid = #{userId}")
    Integer checkFileNameExists(String fileName, Integer userId);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId}")
    File getFileById(Integer fileId);

    @Select("SELECT fileId, version, filename, contenttype, filesize, userid FROM FILES WHERE userid = #{userId}")
    List<FileInfoDto> getListFileInfoByUserId(Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    Integer insert(File file);

    @Update("UPDATE FILES SET contenttype = #{contentType}, filesize = #{fileSize}, filedata = #{fileData}, version = #{version} WHERE fileId = #{fileId}")
    void updateFile(UpdateFileInfoDto fileInfo);

    @Select("SELECT version FROM FILES WHERE fileId = #{fileId}")
    Integer getCurrentFileVersion(Integer fileId);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    void deleteFile(Integer fileId);
}
