package com.molcom.nms.idManagement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Repository
@Slf4j
public class IdManagementRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * @param id
     * @return
     * @throws SQLException
     */
    public int checkForDuplicate(String id) throws SQLException {
        String sql = "SELECT count(*) FROM IdManagement WHERE Id=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, id);
    }

    /**
     * @param id
     * @param idType
     * @return
     * @throws SQLException
     */
    public int save(String id, String idType) throws SQLException {
        int responseCode = 0;
        int count = checkForDuplicate(id);
        log.info("Count in repository {}", count);
        if (count >= 0) {
            String sql = "INSERT INTO IdManagement (Id, IdType) VALUES(?,?)";
            log.info(sql);
            responseCode = jdbcTemplate.update(sql,
                    id,
                    idType
            );
            return responseCode;
        } else {

        }
        return -1;
    }
}
