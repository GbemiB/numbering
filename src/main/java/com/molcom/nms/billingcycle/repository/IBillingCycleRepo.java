package com.molcom.nms.billingcycle.repository;

import com.molcom.nms.billingcycle.dto.BillingCycleModel;

import java.sql.SQLException;
import java.util.List;

public interface IBillingCycleRepo {

    int isExist(String billingCycle) throws SQLException;

    int save(BillingCycleModel model) throws SQLException;

    int edit(BillingCycleModel model, int billingId) throws SQLException;

    int deleteById(int billingId) throws SQLException;

    BillingCycleModel findById(int billingId) throws SQLException;

    List<BillingCycleModel> getAll() throws SQLException;

    // BillingName and BillingType
    List<BillingCycleModel> filter(String queryParam1, String queryValue1, String queryParam2, String queryValue2, String rowNumber) throws SQLException;

}
