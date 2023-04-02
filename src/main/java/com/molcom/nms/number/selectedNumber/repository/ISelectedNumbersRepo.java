package com.molcom.nms.number.selectedNumber.repository;


import com.molcom.nms.number.selectedNumber.dto.DistinctNumber;
import com.molcom.nms.number.selectedNumber.dto.SelectedNumberModel;

import java.sql.SQLException;
import java.util.List;

public interface ISelectedNumbersRepo {
    int countPerApplicationId(String applicationId) throws SQLException;

    int saveSelectedNumber(SelectedNumberModel selectedNumberModel) throws SQLException;

    int deleteByAppId(String applicationId) throws SQLException;

    int deleteSelectedNumber(String selectedNumberId) throws SQLException;

    List<SelectedNumberModel> getSelectedNumber(String applicationId) throws SQLException;

    List<DistinctNumber> getExpiredDistinct() throws SQLException;

    // Update allocation status to allocated, date allocated and validity period when invoice is sent to eservices
    int updateAfterAllocation(String applicationId, String companyAllocatedTo) throws SQLException;

    // get expired application and set flag ProcessForRenewal to true
    int updateApplicationForRenewal() throws SQLException;

    // Manually expire application
    int manuallyExpireApplication(String applicationId) throws SQLException;

    // update status not to be picked for renewal
    int updateApplicationNotToBePickedRenewal(String applicationId) throws SQLException;

    List<SelectedNumberModel> getRejectedNumbers(String applicationId) throws SQLException;

    int dropRejectedNumbers(String applicationId) throws SQLException;

}
