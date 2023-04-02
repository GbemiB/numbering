package com.molcom.nms.statistics.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.statistics.dto.RecentApplicationLog;
import com.molcom.nms.statistics.dto.RegularUserStatistics;
import com.molcom.nms.statistics.repository.IStatisticsRegularRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StatisticRegularService implements IStatisticRegularService {
    @Autowired
    private IStatisticsRegularRepository repository;

    /**
     * Service implementation to get regular user statistics
     *
     * @return
     */
    @Override
    public GenericResponse<RegularUserStatistics> getRegularStatistics() {
        GenericResponse<RegularUserStatistics> genericResponse = new GenericResponse<>();
        RegularUserStatistics regularUserStatistics = new RegularUserStatistics();

        try {
            int submittedApplication = repository.countTotalSubmittedApplications();
            int unSubmittedApplication = repository.countTotalUnSubmittedApplications();
            int approvedApplication = repository.countTotalApprovedApplications();
            int rejectedApplication = repository.countTotalRejectedApplications();

            // Call regular user repository to get counts of each number types
            int totalStandardNumbers = repository.countTotalStandardNos();
            int totalShortCodes = repository.countTotalShortCodes();
            int totalISPC = repository.countTotalISPCs();
            int totalSpecialNumbers = repository.countTotalSpecialNos();

            // Call regular user repository to get 5 recent applications across all number categories
            List<RecentApplicationLog> recents = repository.getRecentApplications();

            // Building regular user statistics
            regularUserStatistics.setSubmittedApplicationCount(submittedApplication);
            regularUserStatistics.setUnSubmittedApplicationCount(unSubmittedApplication);
            regularUserStatistics.setApprovedApplicationCount(approvedApplication);
            regularUserStatistics.setRejectedApplicationCount(rejectedApplication);

            regularUserStatistics.setTotalStandardNumbersCount(totalStandardNumbers);
            regularUserStatistics.setTotalShortCodesCount(totalShortCodes);
            regularUserStatistics.setTotalISPCCount(totalISPC);
            regularUserStatistics.setTotalSpecialNumbersCount(totalSpecialNumbers);

            regularUserStatistics.setRecentApplicationLog(recents);

            genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
            genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            genericResponse.setOutputPayload(regularUserStatistics);
        } catch (Exception exception) {
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    @Override
    public GenericResponse<RegularUserStatistics> getRegularStatistics(String companyName) {
        String compName = (companyName != null ? companyName.trim().toUpperCase() : "");
        GenericResponse<RegularUserStatistics> genericResponse = new GenericResponse<>();
        RegularUserStatistics regularUserStatistics = new RegularUserStatistics();

        try {
            int submittedApplication = repository.countTotalSubmittedApplications(compName);
            int unSubmittedApplication = repository.countTotalUnSubmittedApplications(compName);
            int approvedApplication = repository.countTotalApprovedApplications(compName);
            int rejectedApplication = repository.countTotalRejectedApplications(compName);

            // Call regular user repository to get counts of each number types
            int totalStandardNumbers = repository.countTotalStandardNos(compName);
            int totalShortCodes = repository.countTotalShortCodes(compName);
            int totalISPC = repository.countTotalISPCs(compName);
            int totalSpecialNumbers = repository.countTotalSpecialNos(compName);

            // Call regular user repository to get 5 recent applications across all number categories
            List<RecentApplicationLog> recents = repository.getRecentApplications(compName);

            // Building regular user statistics
            regularUserStatistics.setSubmittedApplicationCount(submittedApplication);
            regularUserStatistics.setUnSubmittedApplicationCount(unSubmittedApplication);
            regularUserStatistics.setApprovedApplicationCount(approvedApplication);
            regularUserStatistics.setRejectedApplicationCount(rejectedApplication);

            regularUserStatistics.setTotalStandardNumbersCount(totalStandardNumbers);
            regularUserStatistics.setTotalShortCodesCount(totalShortCodes);
            regularUserStatistics.setTotalISPCCount(totalISPC);
            regularUserStatistics.setTotalSpecialNumbersCount(totalSpecialNumbers);

            regularUserStatistics.setRecentApplicationLog(recents);

            genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
            genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            genericResponse.setOutputPayload(regularUserStatistics);
        } catch (Exception exception) {
            log.info("Exception occurred getting statistics for company {}", exception.getStackTrace());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }
}
