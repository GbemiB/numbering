package com.molcom.nms.jobQueue.scheduler;

import com.molcom.nms.jobQueue.services.InvoiceProcessor;
import com.molcom.nms.jobQueue.services.ReportingProcessor;
import com.molcom.nms.numberReport.ispc.repository.ReportIspcRepository;
import com.molcom.nms.numberReport.shortcode.repository.ReportShortCodeRepository;
import com.molcom.nms.numberReport.special.repository.ReportSpecialRepository;
import com.molcom.nms.numberReport.standard.repository.ReportStandardNoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InvoiceScheduler {

    @Autowired
    InvoiceProcessor invoiceProcessor;

    @Autowired
    ReportingProcessor reportingProcessor;

    @Autowired
    ReportIspcRepository reportIspcRepository;

    @Autowired
    ReportShortCodeRepository reportShortCodeRepository;

    @Autowired
    ReportSpecialRepository reportSpecialRepository;

    @Autowired
    ReportStandardNoRepository reportStandardNoRepository;

    @Scheduled(fixedDelayString = "${scheduler.fixed.delay:60000}",
            initialDelayString = "${scheduler.initial.delay:60000}")
    public void reporting() throws Exception {
        log.info("STARTED BACKGROUND JOB FOR NUMBER REPORTING ");
        // Job runs for reporting
        reportingProcessor.sendNumberForReporting();
    }

    @Scheduled(fixedDelayString = "${scheduler.fixed.delay:60000}",
            initialDelayString = "${scheduler.initial.delay:60000}")
    public void allocationReportingStatus() throws Exception {
        log.info("STARTED BACKGROUND JOB FOR ALLOCATION REPORTING STATUS");
        // Job runs for reporting and update allocation status or short code, special and standard numbers
        reportSpecialRepository.updateAllocationStatus();
        reportStandardNoRepository.updateAllocationStatus();
        reportShortCodeRepository.updateAllocationStatus();
    }


    @Scheduled(fixedDelayString = "${scheduler.fixed.delay:60000}",
            initialDelayString = "${scheduler.initial.delay:60000}")
    public void fetchInvoices() throws Exception {
        log.info("STARTED BACKGROUND JOB FOR FETCHING INVOICE THAT REQUIRE SEND TO ESERVICES ");
        invoiceProcessor.sendInvoiceToEserviceJob();
    }

    @Scheduled(fixedDelayString = "${scheduler.fixed.delay:60000}",
            initialDelayString = "${scheduler.initial.delay:60000}")
    public void fetchNotification() throws Exception {
        log.info("STARTED BACKGROUND JOB FOR FETCHING ESERVICES NOTIFICATION QUEUE ");
        invoiceProcessor.fetchEserviceNotificationQueue();
    }

    @Scheduled(fixedDelayString = "${scheduler.fixed.delay:60000}",
            initialDelayString = "${scheduler.initial.delay:60000}")
    public void updateExpiredApplicationForRenewal() throws Exception {
        log.info("STARTED BACKGROUND JOB FOR UPDATING EXPIRED APPLICATION FOR RENEWAL TO BEGIN ");
        invoiceProcessor.updateExpiredApplicationForRenewal();
    }

    @Scheduled(fixedDelayString = "${scheduler.fixed.delay:60000}",
            initialDelayString = "${scheduler.initial.delay:60000}")
    public void autoRenewApplication() throws Exception {
        log.info("STARTED BACKGROUND JOB FOR RENEW APPLICATION ");
        invoiceProcessor.checkForExpiredApplications();
    }
}
