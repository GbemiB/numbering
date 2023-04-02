package com.molcom.nms.invoice.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.invoice.dto.InvoiceModel;
import com.molcom.nms.invoice.service.IInvoiceService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/invoice")
public class InvoiceController {

    @Autowired
    private IInvoiceService invoiceService;

    @Autowired
    private TokenHandler tokenHandler;


    /**
     * @param model
     * @param authorization
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> save(@RequestBody InvoiceModel model,
                                   @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return invoiceService.createInvoice(model);
        }
    }

    /**
     * @param transactionRefId
     * @param authorization
     * @return
     * @throws Exception
     */
    @PatchMapping()
    public Boolean updateStatus(@RequestParam String transactionRefId,
                                @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return false;
        } else {
            return invoiceService.updateStatus(transactionRefId);
        }
    }

    /**
     * @param invoiceId
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getById")
    public GenericResponse<?> getById(@RequestParam int invoiceId,
                                      @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return invoiceService.getById(invoiceId);
        }
    }


    /**
     * @param rowNumber
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getAll")
    public GenericResponse<?> getAll(@RequestParam String rowNumber,
                                     @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return invoiceService.getAll(rowNumber);
        }
    }

    /**
     * @param applicationPaymentStatus
     * @param invoiceType
     * @param invoiceNumber
     * @param startDate
     * @param endDate
     * @param rowNumber
     * @param authorization
     * @return
     * @throws Exception
     */
//    @GetMapping("/filterRegularUser")
//    public GenericResponse<?> filterForRegularUser(@RequestParam String applicationPaymentStatus,
//                                                   @RequestParam String invoiceType,
//                                                   @RequestParam String invoiceNumber,
//                                                   @RequestParam String startDate,
//                                                   @RequestParam String endDate,
//                                                   @RequestParam String rowNumber,
//                                                   @RequestHeader("authorization") String authorization) throws Exception {
//
//        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
//        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
//            return handle;
//        } else {
//            return invoiceService.filterForRegularUser(applicationPaymentStatus, invoiceType, invoiceNumber, startDate,
//                    endDate, rowNumber);
//        }
//    }
    @GetMapping("/filterRegularUser")
    public GenericResponse<?> filterForRegularUser(@RequestParam String companyName,
                                                   @RequestParam String applicationPaymentStatus,
                                                   @RequestParam String invoiceType,
                                                   @RequestParam String invoiceNumber,
                                                   @RequestParam String startDate,
                                                   @RequestParam String endDate,
                                                   @RequestParam String rowNumber,
                                                   @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return invoiceService.filterForRegularUser(companyName, applicationPaymentStatus, invoiceType, invoiceNumber, startDate,
                    endDate, rowNumber);
        }
    }

    /**
     * @param applicationPaymentStatus
     * @param invoiceType
     * @param invoiceNumber
     * @param organization
     * @param startDate
     * @param endDate
     * @param rowNumber
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/filterAdminUser")
    public GenericResponse<?> filterForAdminUser(@RequestParam String applicationPaymentStatus,
                                                 @RequestParam String invoiceType,
                                                 @RequestParam String invoiceNumber,
                                                 @RequestParam String organization,
                                                 @RequestParam String startDate,
                                                 @RequestParam String endDate,
                                                 @RequestParam String rowNumber,
                                                 @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return invoiceService.filterForAdminUser(applicationPaymentStatus, invoiceType, invoiceNumber, organization, startDate,
                    endDate, rowNumber);
        }
    }
}
