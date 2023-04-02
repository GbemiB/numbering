package com.molcom.nms.rolesandpriviledge.userroles.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.rolesandpriviledge.userroles.dto.AssignRoleModel;
import com.molcom.nms.rolesandpriviledge.userroles.dto.UserRoleModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
public class UserRoleRepo implements IUserRoleRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public int doesRoleExist(String roleType) throws Exception {
        String role = (roleType != null ? roleType.toUpperCase() : "");
        String sql = "SELECT count(*) FROM UserRole WHERE UPPER(RoleType)=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, role);
    }

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int save(UserRoleModel model) throws SQLException {
        String sql = "INSERT INTO UserRole (RoleType, CreatedBy, CreatedDate ) " +
                "VALUES(?,?,?)";
        return jdbcTemplate.update(sql,
                model.getRoleType(),
                model.getCreatedBy(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int edit(UserRoleModel model) throws SQLException {
        String sql = "INSERT INTO UserRole (RoleType, CreatedBy, UpdatedBy, CreatedDate, ModifiedDate ) " +
                "VALUES(?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                model.getRoleType(),
                model.getCreatedBy(),
                model.getUpdatedBy(),
                CurrentTimeStamp.getCurrentTimeStamp(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }

    /**
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public int assignRoleToUser(AssignRoleModel model) throws Exception {
        String sql = "INSERT INTO UserAssignedRoles (Username, RoleName, CreatedDate ) " +
                "VALUES(?,?,?)";
        return jdbcTemplate.update(sql,
                model.getUsername(),
                model.getRoleName(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }

    /**
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public List<UserRoleModel> getAllRoles(String rowNumber) throws Exception {
        String sql = "SELECT * from UserRole  ORDER BY RoleType  ASC LIMIT " + rowNumber + "";
        List<UserRoleModel> roleModelList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(UserRoleModel.class));
        log.info("UserRoleModel {} ", roleModelList);
        return roleModelList;
    }


    /**
     * @param username
     * @return
     * @throws Exception
     */
    @Override
    public List<AssignRoleModel> getRolesAssignedToUser(String username) throws Exception {
        String sql = "SELECT * FROM UserAssignedRoles WHERE Username = ?";
        List<AssignRoleModel> assignRoleModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(AssignRoleModel.class), username);
        log.info("AssignRoleModel {} ", assignRoleModels);
        return assignRoleModels;
    }

    /**
     * @param userRoleId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteById(int userRoleId) throws SQLException {
        String sql = "DELETE FROM UserRole WHERE UserTypeId = ?";
        return jdbcTemplate.update(sql, userRoleId);
    }


    /**
     * @param userRoleId
     * @return
     * @throws SQLException
     */
    @Override
    public UserRoleModel findById(int userRoleId) throws SQLException {
        String sql = "SELECT * FROM UserRole WHERE UserTypeId = ?";
        try {
            UserRoleModel userRoleModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(UserRoleModel.class), userRoleId);
            log.info("UserRoleModel {} ", userRoleModel);
            return userRoleModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }

    /**
     * @param userRoleName
     * @return
     * @throws SQLException
     */
    @Override
    public UserRoleModel findByRoleName(String userRoleName) throws SQLException {
        String sql = "SELECT * FROM UserRole WHERE RoleType = ?";
        List<UserRoleModel> userRoleModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(UserRoleModel.class), userRoleName);
        if (userRoleModels != null && userRoleModels.size() >= 1) {
            UserRoleModel role = userRoleModels.get(0);
            log.info("UserRoleModel {} ", userRoleModels);
            return role;
        } else {
            return null;
        }
    }

    /**
     * @param queryParam
     * @param queryValue
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    @Override
    public List<UserRoleModel> filter(String queryParam, String queryValue, String rowNumber) throws SQLException {
        String sql = "SELECT * from UserRole WHERE  " + queryParam + "  LIKE '%" + queryValue + "%' " + "ORDER BY  RoleType ASC LIMIT " + rowNumber + "";
        List<UserRoleModel> userRoleModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(UserRoleModel.class));
        log.info("UserRoleModel {} ", userRoleModels);
        return userRoleModels;
    }

}
