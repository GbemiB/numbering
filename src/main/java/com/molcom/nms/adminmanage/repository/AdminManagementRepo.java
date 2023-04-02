package com.molcom.nms.adminmanage.repository;

import com.molcom.nms.adminmanage.dto.AdminManagementModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class AdminManagementRepo implements IAdminManagementRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Data layer to save new admin
     *
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int save(AdminManagementModel model) throws SQLException {
        String sql = "INSERT INTO AdminManagement (Name, Email, UserName, AssignedRole, Signature, Organisation) " +
                "VALUES(?,?,?,?,?,?)";

        return jdbcTemplate.update(sql,
                model.getName(),
                model.getEmail(),
                model.getUserName(),
                model.getAssignedRole(),
                model.getSignature(),
                model.getOrganisation().toLowerCase()
        );

    }

    /**
     * Data layer to persist new admin
     *
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public boolean persistUser(AdminManagementModel model) throws SQLException {

        try {
            String sql1 = "SELECT * FROM AdminManagement WHERE userName = ?";

            List<AdminManagementModel> adminManagementModels = jdbcTemplate.query(sql1,
                    BeanPropertyRowMapper.newInstance(AdminManagementModel.class), model.getUserName());
            log.info("AdminManagementModel {} ", adminManagementModels);
            log.info("Number of users with username {} ", adminManagementModels.size());

            if (adminManagementModels.size() < 1) {
                String sql2 = "INSERT INTO AdminManagement (" +
                        "name, Email, userName, " +
                        "assignedRole, Signature, Organisation) " +
                        "VALUES(?,?,?,?,?,?)";

                int res = jdbcTemplate.update(sql2,
                        model.getName(),
                        model.getEmail(),
                        model.getUserName(),
                        model.getAssignedRole(),
                        model.getSignature(),
                        model.getOrganisation());
                return res == 1;
            } else {
                return true;
            }
        } catch (Exception exe) {
            log.info("Exception occurred in admin management {}", exe.getMessage());
            return false;
        }
    }

    /**
     * Data layer to edit existing admin
     *
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int editRole(AdminManagementModel model, int adminManageId) throws SQLException {
        String sql = "UPDATE AdminManagement set AssignedRole= ? WHERE AdminManageId = ?";

        return jdbcTemplate.update(sql, model.getAssignedRole(), adminManageId);
    }

    /**
     * Data layer to edit admin signature
     *
     * @param signature
     * @return
     * @throws SQLException
     */
    @Override
    public int editSignature(String signature, int adminManageId) throws SQLException {
        if (signature == "DELETE") {
            String sql = "UPDATE AdminManagement set Signature= null WHERE AdminManageId = ?";
            return jdbcTemplate.update(sql, adminManageId);
        } else {
            String sql = "UPDATE AdminManagement set Signature= ? WHERE AdminManageId = ?";
            return jdbcTemplate.update(sql, signature, adminManageId);
        }

    }


    /**
     * Data layer to get all admins
     *
     * @param signature
     * @return
     * @throws SQLException
     */
    @Override
    public List<AdminManagementModel> getAll(String rowNumber) throws Exception {
        String sql = "SELECT * from AdminManagement ORDER BY Name  ASC LIMIT " + rowNumber + "";
        List<AdminManagementModel> adminManagementModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(AdminManagementModel.class));
        log.info("AdminManagementModel {} ", adminManagementModels);

        return adminManagementModels;

    }

    /**
     * Data layer to filter admins
     *
     * @param queryParam1
     * @param queryValue1
     * @param queryParam2
     * @param queryValue2
     * @param queryParam3
     * @param queryValue3
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    @Override
    public List<AdminManagementModel> filter(String queryParam1, String queryValue1, String queryParam2, String queryValue2, String queryParam3, String queryValue3, String rowNumber) throws SQLException {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from AdminManagement WHERE ");

        if (queryValue1 != null
                && !queryValue1.isEmpty()
                && !Objects.equals(queryValue1, "")
                && !Objects.equals(queryValue1.toUpperCase(), "SELECT")
                && !Objects.equals(queryValue1.toUpperCase(), "UNDEFINED")) {
            sqlBuilder.append("" + queryParam1 + " = '").append("" + queryValue1 + "").append("' AND ");
        }

        if (queryValue2 != null
                && !queryValue2.isEmpty()
                && !Objects.equals(queryValue2, "")
                && !Objects.equals(queryValue2.toUpperCase(), "SELECT")
                && !Objects.equals(queryValue2.toUpperCase(), "UNDEFINED")) {
            sqlBuilder.append("" + queryParam2 + " = '").append("" + queryValue2 + "").append("' AND ");
        }

        if (queryValue3 != null
                && !queryValue3.isEmpty()
                && !Objects.equals(queryValue3, "")
                && !Objects.equals(queryValue3.toUpperCase(), "SELECT")
                && !Objects.equals(queryValue3.toUpperCase(), "UNDEFINED")) {
            sqlBuilder.append("" + queryParam3 + " = '").append("" + queryValue3 + "").append("' AND ");
        }

        sqlBuilder.append("UserName IS NOT NULL ");
        sqlBuilder.append("ORDER BY UserName ASC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();
        log.info("sql {} ", sql);

        List<AdminManagementModel> adminManagementModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(AdminManagementModel.class));
        log.info("AdminManagementModel {} ", adminManagementModels);
        return adminManagementModels;
    }

    /**
     * Data layer to get admin by username
     *
     * @param username
     * @return
     */
    @Override
    public List<AdminManagementModel> getAdminByUsername(String username) {
        String sql = "SELECT * from AdminManagement WHERE userName=?";
        List<AdminManagementModel> adminManagementModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(AdminManagementModel.class), username);
        log.info("AdminManagementModel {} ", adminManagementModels);

        return adminManagementModels;
    }

    /**
     * Data layer to get admins by roles
     *
     * @param roleName
     * @return
     */
    @Override
    public List<AdminManagementModel> getAllAdminWithRole(String roleName) {
        String sql = "SELECT * from AdminManagement WHERE AssignedRole=?";
        List<AdminManagementModel> adminManagementModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(AdminManagementModel.class), roleName);
        log.info("AdminManagementModel {} ", adminManagementModels);

        return adminManagementModels;
    }

    /**
     * Data layer to get roles of admins
     *
     * @param username
     * @return
     */
    @Override
    public List<AdminManagementModel> getRolesOfAdmin(String username) {
        String sql = "SELECT * from AdminManagement WHERE userName=?";
        List<AdminManagementModel> adminManagementModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(AdminManagementModel.class), username);
        log.info("AdminManagementModel {} ", adminManagementModels);

        return adminManagementModels;
    }

    /**
     * Data layer to get emails of fellow admin
     *
     * @param username
     * @return
     */
    @Override
    public List<String> getEmailOfFellowAdmin(String username) {
        List<String> allEmails = new ArrayList<>();

        // Get the user organisation
        String userOrganisation = getEmailOrganisation(username);

        if (userOrganisation != null || userOrganisation != "") {

            // get emails of all admins in the organisation
            String sql2 = "SELECT * from AdminManagement WHERE Organisation=?";
            List<AdminManagementModel> admins = jdbcTemplate.query(sql2,
                    BeanPropertyRowMapper.newInstance(AdminManagementModel.class), userOrganisation);

            admins.forEach(email -> {
                allEmails.add(email.getEmail());
            });
            return allEmails;
        } else {
            return null;
        }
    }

    @Override
    public List<String> getAdminEmails() {
        List<String> allEmails = new ArrayList<>();
        String sql2 = "SELECT * from AdminManagement WHERE AssignedRole != '' AND AssignedRole IS NOT NULL";
        List<AdminManagementModel> admins = jdbcTemplate.query(sql2,
                BeanPropertyRowMapper.newInstance(AdminManagementModel.class));

        admins.forEach(email -> {
            allEmails.add(email.getEmail());
        });
        return allEmails;
    }

    @Override
    public List<String> getCollaboratorAdmins() {
        List<String> allEmails = new ArrayList<>();
        String sql2 = "SELECT * from AdminManagement WHERE UPPER(AssignedRole) ='COLLABORATOR ADMIN'";
        List<AdminManagementModel> admins = jdbcTemplate.query(sql2,
                BeanPropertyRowMapper.newInstance(AdminManagementModel.class));

        admins.forEach(email -> {
            allEmails.add(email.getEmail());
        });
        return allEmails;
    }

    /**
     * Data layer to get central admin for an organisation
     *
     * @param organisation
     * @return
     */
    @Override
    public List<String> getEmailOfCollaboratorAdminForOrgan(String organisation) {
        List<String> allEmails = new ArrayList<>();

        String org = (organisation != null ? organisation.toUpperCase() : "");
        String role = "COLLABORATOR ADMIN";

        if (org != "") {
            // get emails of all admins in the organisation with collaborator admin
            String sql2 = "SELECT * from AdminManagement WHERE UPPER(AssignedRole)=? AND UPPER(Organisation)=?";
            List<AdminManagementModel> admins = jdbcTemplate.query(sql2,
                    BeanPropertyRowMapper.newInstance(AdminManagementModel.class), role, org);

            admins.forEach(email -> {
                allEmails.add(email.getEmail());
            });
            return allEmails;
        } else {
            return null;
        }
    }

    /**
     * Data layer to get all admin for an organisation
     *
     * @param organisation
     * @return
     */
    @Override
    public List<String> getEmailOfAllAdminForOrganisation(String organisation) {
        List<String> allEmails = new ArrayList<>();
        String org = (organisation != null ? organisation.trim() : "");

        if (org != "") {
            String sql2 = "SELECT * from AdminManagement WHERE Organisation=?";
            List<AdminManagementModel> admins = jdbcTemplate.query(sql2,
                    BeanPropertyRowMapper.newInstance(AdminManagementModel.class), org);
            log.info("Ad {}", admins);

            admins.forEach(email -> {
                allEmails.add(email.getEmail());
            });
            return allEmails;
        } else {
            return null;
        }
    }

    /**
     * Data layer to get email of an admin organisation
     *
     * @param username
     * @return
     */
    @Override
    public String getEmailOrganisation(String username) {
        String organisation = "";

        // get organisation of the user
        String sql1 = "SELECT Organisation FROM AdminManagement WHERE Username=?";

        List<AdminManagementModel> admins = jdbcTemplate.query(sql1,
                BeanPropertyRowMapper.newInstance(AdminManagementModel.class), username);

        if (admins.size() == 0) {
            organisation = "";
        } else {
            AdminManagementModel a = admins.get(0);
            organisation = admins.get(0).getOrganisation();
        }
        return organisation;
    }
}
