package com.molcom.nms.statistics.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.statistics.dto.AdminUserStatistics;
import com.molcom.nms.statistics.repository.IStatisticsAdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StatisticAdminService implements IStatisticAdminService {
    @Autowired
    private IStatisticsAdminRepository adminRepository;

    /**
     * Service implementation to get admin user statistics
     *
     * @return
     */
    @Override
    public GenericResponse<AdminUserStatistics> getAdminStatistics() {
        GenericResponse<AdminUserStatistics> genericResponse = new GenericResponse<>();
        AdminUserStatistics adminUserStatistics = new AdminUserStatistics();
        try {
            // Coverage area count assembler
            int totalCoverageCount = adminRepository.countTotalCoverageArea();
            int geographicalCoverageCount = adminRepository.countGeographicalCoverageArea();
            int nationalCoverageCount = adminRepository.countNationalCoverageArea();

            adminUserStatistics.getCoverageAreaCount().setTotalCoverageArea(totalCoverageCount);
            adminUserStatistics.getCoverageAreaCount().setGeographicalCoverageArea(geographicalCoverageCount);
            adminUserStatistics.getCoverageAreaCount().setNationalCoverageArea(nationalCoverageCount);

            // Approved Application status count assembler
            int countApprovedStandardNos = adminRepository.countApprovedStandardNos();
            int countApprovedShortCodes = adminRepository.countApprovedShortCodes();
            int countApprovedISPCs = adminRepository.countApprovedISPCs();
            int countApprovedSpecialNos = adminRepository.countApprovedSpecialNos();

            adminUserStatistics.getApprovedApplicationCount().setSpecialNumbers(countApprovedStandardNos);
            adminUserStatistics.getApprovedApplicationCount().setShortCodes(countApprovedShortCodes);
            adminUserStatistics.getApprovedApplicationCount().setISPCs(countApprovedISPCs);
            adminUserStatistics.getApprovedApplicationCount().setSpecialNumbers(countApprovedSpecialNos);

            // Pending Application status count assembler
            int countPendingStandardNos = adminRepository.countPendingStandardNos();
            int countPendingShortCodes = adminRepository.countPendingShortCodes();
            int countPendingISPCs = adminRepository.countPendingISPCs();
            int countPendingSpecialNos = adminRepository.countPendingSpecialNos();

            adminUserStatistics.getPendingApplicationCount().setSpecialNumbers(countPendingStandardNos);
            adminUserStatistics.getPendingApplicationCount().setShortCodes(countPendingShortCodes);
            adminUserStatistics.getPendingApplicationCount().setISPCs(countPendingISPCs);
            adminUserStatistics.getPendingApplicationCount().setSpecialNumbers(countPendingSpecialNos);

            // Rejected Application status count assembler
            int countRejectedStandardNos = adminRepository.countRejectedStandardNos();
            int countRejectedShortCodes = adminRepository.countRejectedShortCodes();
            int countRejectedISPCs = adminRepository.countRejectedISPCs();
            int countRejectedSpecialNos = adminRepository.countRejectedSpecialNos();

            adminUserStatistics.getRejectedApplicationCount().setSpecialNumbers(countRejectedStandardNos);
            adminUserStatistics.getRejectedApplicationCount().setShortCodes(countRejectedShortCodes);
            adminUserStatistics.getRejectedApplicationCount().setISPCs(countRejectedISPCs);
            adminUserStatistics.getRejectedApplicationCount().setSpecialNumbers(countRejectedSpecialNos);

            // Addition of service assembler
            int approvedServices = adminRepository.countApprovedServices();
            int pendingServices = adminRepository.countPendingServices();
            int submittedServices = adminRepository.countSubmittedServices();
            int rejectedServices = adminRepository.countRejectedServices();

            log.info("Approved {} ", approvedServices);
            log.info("Pending {} ", pendingServices);
            log.info("Submitted {} ", submittedServices);
            log.info("Rejected {} ", rejectedServices);

            adminUserStatistics.getAdditionOfServicesCount().setApprovedServices(approvedServices);
            adminUserStatistics.getAdditionOfServicesCount().setPendingServices(pendingServices);
            adminUserStatistics.getAdditionOfServicesCount().setSubmittedServices(submittedServices);
            adminUserStatistics.getAdditionOfServicesCount().setRejectedServices(rejectedServices);

        } catch (Exception exception) {
            // Setting the generic response
            log.info("Exception occurred {}", exception.getMessage());
        }

        // Setting the generic response
        genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
        genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
        genericResponse.setOutputPayload(adminUserStatistics);

        return genericResponse;
    }
}
