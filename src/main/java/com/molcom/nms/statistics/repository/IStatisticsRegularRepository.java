package com.molcom.nms.statistics.repository;

import com.molcom.nms.statistics.dto.RecentApplicationLog;

import java.util.List;

public interface IStatisticsRegularRepository {
    // Count of all number types application
    int countTotalStandardNos();

    int countTotalShortCodes();

    int countTotalISPCs();

    int countTotalSpecialNos();

    // Count of all number types application
    int countTotalSubmittedApplications();

    int countTotalUnSubmittedApplications();

    int countTotalApprovedApplications();

    int countTotalRejectedApplications();

    // Return of five (5) recent application
    List<RecentApplicationLog> getRecentApplications();

    ////// Method overloading //////////////
    // Count of all number types application
    int countTotalStandardNos(String companyName);

    int countTotalShortCodes(String companyName);

    int countTotalISPCs(String companyName);

    int countTotalSpecialNos(String companyName);

    // Count of all number types application
    int countTotalSubmittedApplications(String companyName);

    int countTotalUnSubmittedApplications(String companyName);

    int countTotalApprovedApplications(String companyName);

    int countTotalRejectedApplications(String companyName);

    // Return of five (5) recent application
    List<RecentApplicationLog> getRecentApplications(String companyName);
}
