package com.molcom.nms.number.configurations.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.number.configurations.dto.ConnectedTelcoCompany;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
public class ConnectTelcoCompRepo implements IConnectTelcoCompRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Data layer to add connected telecommunication
     *
     * @param connectedTelcoCompany
     * @return
     * @throws SQLException
     */
    @Override
    public int saveConnectedTelcoCompanies(ConnectedTelcoCompany connectedTelcoCompany) throws SQLException {
        String sql = "INSERT INTO ConnectedTelcoCompanies (ApplicationId, NameOfCompany, PointOfConnection," +
                " DateSelector, CreatedDate) " +
                "VALUES(?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                connectedTelcoCompany.getApplicationId(),
                connectedTelcoCompany.getNameOfCompany(),
                connectedTelcoCompany.getPointOfConnection(),
                connectedTelcoCompany.getDateSelector(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }


    /**
     * Data layer to delete existing  connected telecommunication
     *
     * @param interconnectId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteConnectedTelcoCompanies(String interconnectId) throws SQLException {
        String sql = "DELETE FROM ConnectedTelcoCompanies WHERE InterconnectId = ?";
        return jdbcTemplate.update(sql, interconnectId);
    }

    /**
     * Data layer to get connected telecommunication by application id
     *
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<ConnectedTelcoCompany> getConnectedTelcoCompanies(String applicationId) throws SQLException {
        String sql = "SELECT * FROM ConnectedTelcoCompanies WHERE ApplicationId = ?";
        log.info("APP ID {}", applicationId);
        log.info("SQL QUERY {}", sql);
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(ConnectedTelcoCompany.class), applicationId);
    }
}
