package com.molcom.nms.billingcycle.service;

import com.molcom.nms.billingcycle.dto.BillingCycleModel;
import com.molcom.nms.general.dto.GenericResponse;

import java.util.List;

public interface IBillingCycleService {
    GenericResponse<BillingCycleModel> save(BillingCycleModel model) throws Exception;

    GenericResponse<BillingCycleModel> edit(BillingCycleModel model, int billingId) throws Exception;

    GenericResponse<BillingCycleModel> deleteById(int billingId) throws Exception;

    GenericResponse<List<BillingCycleModel>> getAll() throws Exception;

    GenericResponse<BillingCycleModel> findById(int billingId) throws Exception;

    GenericResponse<List<BillingCycleModel>> filter(String queryParam1, String queryValue1,
                                                    String queryParam2, String queryValue2, String rowNumber) throws Exception;
}
