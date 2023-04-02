package com.molcom.nms.idManagement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
public class IdManagementService {

    @Autowired
    private IdManagementRepository idManagementRepository;

    /**
     * Get application id
     *
     * @return
     */
    public static String getApplicationId() {
        String appId = "";
        String string1 = "A000000";
        Random random = new Random();
        int string2 = random.nextInt(900) + 100;
        appId = string1 + string2;
        return appId;

    }

    /**
     * Get invoice id
     *
     * @return
     */
    public static String getInvoiceId() {
        String string1 = "NMS000";
        String invoiceId = "";
        Random random = new Random();
        int string2 = random.nextInt(900) + 100;
        invoiceId = string1 + string2;

        return invoiceId;
    }

    /**
     * Generate application id
     *
     * @return
     */
    public String generateApplicationId() throws Exception {
        String id;
        int isExist = 0;

        id = getApplicationId();

        isExist = idManagementRepository.checkForDuplicate(id);
        log.info("Count in service {}", isExist);
        while (isExist >= 1) {
            id = getApplicationId();
        }
        log.info("Id {}", id);
        return id;
    }

    /**
     * Generate invoice id
     *
     * @return
     */
    public String generateInvoiceId() throws Exception {
        String id;
        int isExist = 0;

        id = getInvoiceId();

        isExist = idManagementRepository.checkForDuplicate(id);
        log.info("Count in service {}", isExist);
        while (isExist >= 1) {
            id = getInvoiceId();
        }
        log.info("Id {}", id);
        return id;
    }
}
