package com.molcom.nms.filestorage.repository;

import com.molcom.nms.filestorage.dto.FileStorageModel;
import com.molcom.nms.general.utils.CurrentTimeStamp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
public class FileStorageRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int fileCount(String fileName, String applicationId) throws SQLException {
        String sql = "SELECT count(*) FROM FileStorage WHERE FileName=? AND ApplicationId=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, fileName, applicationId);
    }

    public int save(FileStorageModel model) throws SQLException {
        String sql = "INSERT INTO FileStorage (FileName, FileSize, FileUrl, ApplicationId, FileType, CreatedDate) " +
                "VALUES(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE FileSize=?, FileType=?, CreatedDate=?";
        return jdbcTemplate.update(sql,
                model.getFileName(),
                model.getFileSize(),
                model.getFileUrl(),
                model.getApplicationId(),
                model.getFileType(),
                CurrentTimeStamp.getCurrentTimeStamp(),
                model.getFileSize(),
                model.getFileType(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }

    public int deleteByFileId(String fileId) throws SQLException {
        String sql = "DELETE FROM FileStorage WHERE FileId=?";
        return jdbcTemplate.update(sql, fileId);
    }

    public int deleteByFileType(String fileType, String applicationId) throws SQLException {
        String sql = "DELETE FROM FileStorage WHERE FileType=? and ApplicationId=?";
        return jdbcTemplate.update(sql, fileType, applicationId);
    }


    public int deleteAll(String applicationId) throws SQLException {
        String sql = "DELETE FROM FileStorage WHERE ApplicationId=?";
        return jdbcTemplate.update(sql, applicationId);
    }

    public List<FileStorageModel> getByApplicationId(String applicationId) throws SQLException {
        String sql = "SELECT * from FileStorage WHERE ApplicationId=? ORDER BY CreatedDate ASC";
        List<FileStorageModel> fileStorageModel = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(FileStorageModel.class), applicationId);
        return fileStorageModel;
    }


    public FileStorageModel getById(int fileId) throws SQLException {
        String sql = "SELECT * FROM FileStorage WHERE FileId=?";
        try {
            FileStorageModel fileStorageModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(FileStorageModel.class), fileId);
            log.info("FileStorageModel {} ", fileStorageModel);
            return fileStorageModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }
}
