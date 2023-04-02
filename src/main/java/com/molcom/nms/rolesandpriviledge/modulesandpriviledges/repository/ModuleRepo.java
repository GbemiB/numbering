package com.molcom.nms.rolesandpriviledge.modulesandpriviledges.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.general.utils.RefGenerator;
import com.molcom.nms.rolesandpriviledge.modulesandpriviledges.dto.BulkUploadItem;
import com.molcom.nms.rolesandpriviledge.modulesandpriviledges.dto.BulkUploadRequestModule;
import com.molcom.nms.rolesandpriviledge.modulesandpriviledges.dto.BulkUploadResponseModel;
import com.molcom.nms.rolesandpriviledge.modulesandpriviledges.dto.ModulesModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class ModuleRepo implements IModuleRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * @param model
     * @param roleType
     * @return
     * @throws SQLException
     */
    @Override
    public int save(ModulesModel model, String roleType) throws SQLException {
        String sql = "INSERT INTO Modules (RoleType, Modules, IsCreated, IsDeleteAllow, IsReadAllow, IsUpdatedAllow," +
                "CreatedBy, UpdatedBy, CreatedDate) " +
                "VALUES(?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                roleType,
                model.getModules(),
                model.getIsCreated(),
                model.getIsDeleteAllow(),
                model.getIsReadAllow(),
                model.getIsUpdatedAllow(),
                model.getCreatedBy(),
                model.getUpdatedBy(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }

    /**
     * @param modulesId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteById(int modulesId) throws SQLException {
        String sql = "DELETE FROM Modules WHERE ModulesId = ?";
        return jdbcTemplate.update(sql, modulesId);
    }

    /**
     * @param roleType
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteByRoleType(String roleType) throws SQLException {
        String sql = "DELETE FROM Modules WHERE RoleType = ?";
        return jdbcTemplate.update(sql, roleType);
    }

    /**
     * @return
     * @throws SQLException
     */
    @Override
    public List<ModulesModel> getAll() throws SQLException {
        String sql = "SELECT DISTINCT Modules FROM Modules";
        List<ModulesModel> modulesModelList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ModulesModel.class));
        log.info("ModulesModel {} ", modulesModelList);
        return modulesModelList;
    }


    /**
     * Get all modules for a particular role
     *
     * @param roleType
     * @return
     * @throws SQLException
     */
    @Override
    public List<ModulesModel> getModuleByRoleType(String roleType) throws SQLException {
        String sql = "SELECT * FROM Modules WHERE RoleType=?";
        List<ModulesModel> modulesModelList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ModulesModel.class), roleType);
        log.info("ModulesModel {} ", modulesModelList);
        return modulesModelList;
    }

    /**
     * @param bulkUploadRequest
     * @param roleType
     * @return
     * @throws SQLException
     */
    @Override
    public BulkUploadResponseModel bulkInsert(BulkUploadRequestModule bulkUploadRequest, String roleType) throws SQLException {
        log.info("Bulk insert");
        BulkUploadResponseModel bulkUploadResponse = new BulkUploadResponseModel();
        List<BulkUploadItem> bulkUploadItem = new ArrayList<>();
        bulkUploadRequest.getModuleList().forEach(bulkItem -> {
            ModulesModel model = new ModulesModel();
            BulkUploadItem item = new BulkUploadItem();
            model.setModules(bulkItem.getModules());
            model.setIsCreated(bulkItem.getIsCreated());
            model.setIsReadAllow(bulkItem.getIsReadAllow());
            model.setIsDeleteAllow(bulkItem.getIsDeleteAllow());
            model.setIsUpdatedAllow(bulkItem.getIsUpdatedAllow());
            model.setCreatedBy(bulkItem.getCreatedBy());
            try {
                int responseCode = save(model, roleType);
                log.info("AutoFeeResponse code for insert of {} ", bulkItem.getBulkUploadId());
                // Checking if the insert was successful for the individual bulk upload
                if (Objects.equals(responseCode, 1)) {
                    item.setItemId(bulkItem.getBulkUploadId());
                    item.setItemResCode("000");
                    item.setItemResMessage("SUCCESS");
                } else {
                    // Checking if the insert failed for the individual bulk upload
                    item.setItemId(bulkItem.getBulkUploadId());
                    item.setItemResCode("999");
                    item.setItemResMessage("FAILED TO INSERT RECORD TO");
                }
                // Adding response code for insert of each item in the list
                bulkUploadItem.add(item);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        bulkUploadResponse.setAllList(bulkUploadItem);
        bulkUploadResponse.setBatchId(RefGenerator.getRefNo(5)); //Batch Id
        bulkUploadResponse.setTotalCount(String.valueOf(bulkUploadRequest.getModuleList().size()));
        return bulkUploadResponse;
    }

}

