package com.molcom.nms.statistics.repository;

public interface IStatisticsAdminRepository {
    //   Coverage count
    int countNationalCoverageArea();

    int countGeographicalCoverageArea();

    int countTotalCoverageArea();

    //    Approved application count
    int countApprovedStandardNos();

    int countApprovedShortCodes();

    int countApprovedISPCs();

    int countApprovedSpecialNos();

    //    Pending application count
    int countPendingStandardNos();

    int countPendingShortCodes();

    int countPendingISPCs();

    int countPendingSpecialNos();

    //    Rejected application count
    int countRejectedStandardNos();

    int countRejectedShortCodes();

    int countRejectedISPCs();

    int countRejectedSpecialNos();

    //    Approved Addition of service count
    int countApprovedServices();

    int countPendingServices();

    int countSubmittedServices();

    int countRejectedServices();
}

