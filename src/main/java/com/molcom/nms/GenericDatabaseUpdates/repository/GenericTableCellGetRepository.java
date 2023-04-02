package com.molcom.nms.GenericDatabaseUpdates.repository;

import com.molcom.nms.number.application.dto.ISPCNumberModel;
import com.molcom.nms.number.application.dto.ShortCodeModel;
import com.molcom.nms.number.application.dto.SpecialNumberModel;
import com.molcom.nms.number.application.dto.StandardNumberModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class GenericTableCellGetRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;


    // Generic method to get company name with applicationId param
    public String getApplicationQuantity(String applicationId) {
        String quantity = "";
        String sql = "";

        String sql1 = "SELECT * FROM StandardNumber WHERE ApplicationId = ?";
        List<StandardNumberModel> standard = jdbcTemplate.query(sql1,
                BeanPropertyRowMapper.newInstance(StandardNumberModel.class), applicationId);

        String sql2 = "SELECT * FROM SpecialNumbers WHERE ApplicationId = ?";
        List<SpecialNumberModel> special = jdbcTemplate.query(sql2,
                BeanPropertyRowMapper.newInstance(SpecialNumberModel.class), applicationId);

        String sql3 = "SELECT * FROM ShortCodeNumbers WHERE ApplicationId = ?";
        List<ShortCodeModel> shortCode = jdbcTemplate.query(sql3,
                BeanPropertyRowMapper.newInstance(ShortCodeModel.class), applicationId);

        String sql4 = "SELECT * FROM IspcNumbers WHERE ApplicationId = ?";
        List<ISPCNumberModel> ispc = jdbcTemplate.query(sql4,
                BeanPropertyRowMapper.newInstance(ISPCNumberModel.class), applicationId);

        if (standard.size() > 0) {
            quantity = standard.get(0).getQuantity();
        } else if (special.size() > 0) {
            quantity = special.get(0).getQuantity();
        } else if (shortCode.size() > 0) {
            quantity = shortCode.get(0).getQuantity();
        } else if (ispc.size() > 0) {
            quantity = ispc.get(0).getQuantity();
        } else {
            quantity = "";
        }
        return quantity;

    }


    // Get short code category
    public String getShortCodeCat(String applicationId) {
        String category = "";
        String sql = "";

        String sql1 = "SELECT * FROM ShortCodeNumbers WHERE ApplicationId = ?";
        List<ShortCodeModel> shortModel = jdbcTemplate.query(sql1,
                BeanPropertyRowMapper.newInstance(ShortCodeModel.class), applicationId);

        if (shortModel.size() > 0) {
            category = shortModel.get(0).getShortCodeCategory();
        } else {
            category = "";
        }
        return category;

    }

    // Get short code service
    public String getShortCodeService(String applicationId) {
        String service = "";
        String sql = "";

        String sql1 = "SELECT * FROM ShortCodeNumbers WHERE ApplicationId = ?";
        List<ShortCodeModel> shortCodeModels = jdbcTemplate.query(sql1,
                BeanPropertyRowMapper.newInstance(ShortCodeModel.class), applicationId);

        if (shortCodeModels.size() > 0) {
            service = shortCodeModels.get(0).getShortCodeService();
        } else {
            service = "";
        }
        return service;

    }


    // Get coverage area
    public String getCoverageArea(String applicationId) {
        String coverageArea = "";
        String sql = "";

        String sql1 = "SELECT * FROM StandardNumber WHERE ApplicationId = ?";
        List<StandardNumberModel> standard = jdbcTemplate.query(sql1,
                BeanPropertyRowMapper.newInstance(StandardNumberModel.class), applicationId);

        if (standard.size() > 0) {
            coverageArea = standard.get(0).getCoverageArea();
        } else {
            coverageArea = "";
        }
        return coverageArea;

    }

    // Get area code
    public String getAreaCode(String applicationId) {
        String areaCode = "";
        String sql = "";

        String sql1 = "SELECT * FROM StandardNumber WHERE ApplicationId = ?";
        List<StandardNumberModel> standard = jdbcTemplate.query(sql1,
                BeanPropertyRowMapper.newInstance(StandardNumberModel.class), applicationId);


        if (standard.size() > 0) {
            areaCode = standard.get(0).getAreaCode();
        } else {
            areaCode = "";
        }
        return areaCode;

    }

    // Get access code using applicationId
    public String getAccessCode(String applicationId) {
        String accessCode = "";
        String sql = "";

        String sql1 = "SELECT * FROM StandardNumber WHERE ApplicationId = ?";
        List<StandardNumberModel> standard = jdbcTemplate.query(sql1,
                BeanPropertyRowMapper.newInstance(StandardNumberModel.class), applicationId);

        String sql2 = "SELECT * FROM SpecialNumbers WHERE ApplicationId = ?";
        List<SpecialNumberModel> special = jdbcTemplate.query(sql2,
                BeanPropertyRowMapper.newInstance(SpecialNumberModel.class), applicationId);


        if (standard.size() > 0) {
            accessCode = standard.get(0).getAccessCode();
        } else if (special.size() > 0) {
            accessCode = special.get(0).getAccessCode();
        } else {
            accessCode = "";
        }
        return accessCode;

    }

    // Generic method to get company name with applicationId param
    public String getCompanyEmail(String applicationId) {
        String companyEmail = "";
        String sql = "";

        String sql1 = "SELECT * FROM StandardNumber WHERE ApplicationId = ?";
        List<StandardNumberModel> standard = jdbcTemplate.query(sql1,
                BeanPropertyRowMapper.newInstance(StandardNumberModel.class), applicationId);

        String sql2 = "SELECT * FROM SpecialNumbers WHERE ApplicationId = ?";
        List<SpecialNumberModel> special = jdbcTemplate.query(sql2,
                BeanPropertyRowMapper.newInstance(SpecialNumberModel.class), applicationId);

        String sql3 = "SELECT * FROM ShortCodeNumbers WHERE ApplicationId = ?";
        List<ShortCodeModel> shortCode = jdbcTemplate.query(sql3,
                BeanPropertyRowMapper.newInstance(ShortCodeModel.class), applicationId);

        String sql4 = "SELECT * FROM IspcNumbers WHERE ApplicationId = ?";
        List<ISPCNumberModel> ispc = jdbcTemplate.query(sql4,
                BeanPropertyRowMapper.newInstance(ISPCNumberModel.class), applicationId);

        if (standard.size() > 0) {
            companyEmail = standard.get(0).getCompanyEmail();
        } else if (special.size() > 0) {
            companyEmail = special.get(0).getCompanyEmail();
        } else if (shortCode.size() > 0) {
            companyEmail = shortCode.get(0).getCompanyEmail();
        } else if (ispc.size() > 0) {
            companyEmail = ispc.get(0).getCompanyEmail();
        } else {
            companyEmail = "";
        }
        return companyEmail;

    }


    // Generic method to get company name with applicationId param
    public String getCompanyNameOfApp(String applicationId) {
        String companyName = "";
        String sql = "";

        String sql1 = "SELECT * FROM StandardNumber WHERE ApplicationId = ?";
        List<StandardNumberModel> standard = jdbcTemplate.query(sql1,
                BeanPropertyRowMapper.newInstance(StandardNumberModel.class), applicationId);

        String sql2 = "SELECT * FROM SpecialNumbers WHERE ApplicationId = ?";
        List<SpecialNumberModel> special = jdbcTemplate.query(sql2,
                BeanPropertyRowMapper.newInstance(SpecialNumberModel.class), applicationId);

        String sql3 = "SELECT * FROM ShortCodeNumbers WHERE ApplicationId = ?";
        List<ShortCodeModel> shortCode = jdbcTemplate.query(sql3,
                BeanPropertyRowMapper.newInstance(ShortCodeModel.class), applicationId);

        String sql4 = "SELECT * FROM IspcNumbers WHERE ApplicationId = ?";
        List<ISPCNumberModel> ispc = jdbcTemplate.query(sql4,
                BeanPropertyRowMapper.newInstance(ISPCNumberModel.class), applicationId);

        if (standard.size() > 0) {
            companyName = standard.get(0).getCompanyName();
        } else if (special.size() > 0) {
            companyName = special.get(0).getCompanyName();
        } else if (shortCode.size() > 0) {
            companyName = shortCode.get(0).getCompanyName();
        } else if (ispc.size() > 0) {
            companyName = ispc.get(0).getCompanyName();
        } else {
            companyName = "";
        }
        return companyName;

    }

    public String getNumberType(String applicationId) {
        String numberType = "";
        String sql = "";

        String sql1 = "SELECT * FROM StandardNumber WHERE ApplicationId = ?";
        List<StandardNumberModel> standard = jdbcTemplate.query(sql1,
                BeanPropertyRowMapper.newInstance(StandardNumberModel.class), applicationId);

        String sql2 = "SELECT * FROM SpecialNumbers WHERE ApplicationId = ?";
        List<SpecialNumberModel> special = jdbcTemplate.query(sql2,
                BeanPropertyRowMapper.newInstance(SpecialNumberModel.class), applicationId);

        String sql3 = "SELECT * FROM ShortCodeNumbers WHERE ApplicationId = ?";
        List<ShortCodeModel> shortCode = jdbcTemplate.query(sql3,
                BeanPropertyRowMapper.newInstance(ShortCodeModel.class), applicationId);

        String sql4 = "SELECT * FROM IspcNumbers WHERE ApplicationId = ?";
        List<ISPCNumberModel> ispc = jdbcTemplate.query(sql4,
                BeanPropertyRowMapper.newInstance(ISPCNumberModel.class), applicationId);

        if (standard.size() > 0) {
            numberType = "STANDARD";
        } else if (special.size() > 0) {
            numberType = "SPECIAL";
        } else if (shortCode.size() > 0) {
            numberType = "SHORT-CODE";
        } else if (ispc.size() > 0) {
            numberType = "ISPC";
        } else {
            numberType = "";
        }
        return numberType;

    }

    public String getNumberSubType(String applicationId) {
        String subType = "";
        String sql = "";

        String sql1 = "SELECT * FROM StandardNumber WHERE ApplicationId = ?";
        List<StandardNumberModel> standard = jdbcTemplate.query(sql1,
                BeanPropertyRowMapper.newInstance(StandardNumberModel.class), applicationId);

        String sql2 = "SELECT * FROM SpecialNumbers WHERE ApplicationId = ?";
        List<SpecialNumberModel> special = jdbcTemplate.query(sql2,
                BeanPropertyRowMapper.newInstance(SpecialNumberModel.class), applicationId);

        String sql3 = "SELECT * FROM ShortCodeNumbers WHERE ApplicationId = ?";
        List<ShortCodeModel> shortCode = jdbcTemplate.query(sql3,
                BeanPropertyRowMapper.newInstance(ShortCodeModel.class), applicationId);

        String sql4 = "SELECT * FROM IspcNumbers WHERE ApplicationId = ?";
        List<ISPCNumberModel> ispc = jdbcTemplate.query(sql4,
                BeanPropertyRowMapper.newInstance(ISPCNumberModel.class), applicationId);

        if (standard.size() > 0) {
            subType = standard.get(0).getSubType().toUpperCase();
        } else if (special.size() > 0) {
            subType = special.get(0).getSubType().toUpperCase();
        } else if (shortCode.size() > 0) {
            subType = "SHORT-CODE";
        } else if (ispc.size() > 0) {
            subType = "ISPC";
        } else {
            subType = "";
        }
        return subType;

    }

}
