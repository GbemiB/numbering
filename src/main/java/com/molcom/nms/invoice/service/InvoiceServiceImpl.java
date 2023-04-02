package com.molcom.nms.invoice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.molcom.nms.GenericDatabaseUpdates.repository.GenericTableUpdateRepository;
import com.molcom.nms.fee.selectedFeeSchedule.dto.SelectedFeeScheduleModel;
import com.molcom.nms.fee.selectedFeeSchedule.repository.ISelectedFeeScheduleRepository;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.RefGenerator;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.general.utils.RestUtil;
import com.molcom.nms.invoice.dto.CheckPaymentResponse;
import com.molcom.nms.invoice.dto.InvoiceModel;
import com.molcom.nms.invoice.dto.InvoiceObject;
import com.molcom.nms.invoice.repository.InvoiceRepository;
import com.molcom.nms.number.application.dto.ShortCodeModel;
import com.molcom.nms.number.application.dto.SpecialNumberModel;
import com.molcom.nms.number.application.dto.StandardNumberModel;
import com.molcom.nms.number.application.repository.IISPCNumberRepository;
import com.molcom.nms.number.application.repository.ISpecialNumberRepository;
import com.molcom.nms.number.application.repository.IStandardNumberRepository;
import com.molcom.nms.number.application.repository.ShortCodeRepository;
import com.molcom.nms.number.selectedNumber.dto.SelectedNumberModel;
import com.molcom.nms.number.selectedNumber.repository.ISelectedNumbersRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class InvoiceServiceImpl implements IInvoiceService {

    @Autowired
    private final RestUtil restUtil;
    @Autowired
    private final ObjectMapper mapper;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private IStandardNumberRepository standardNumberRepository;
    @Autowired
    private ISpecialNumberRepository specialNumberRepository;
    @Autowired
    private IISPCNumberRepository iispcNumberRepository;
    @Autowired
    private ShortCodeRepository shortCodeRepository;
    @Autowired
    private InvoiceRepository repository;

    @Autowired
    private ISelectedFeeScheduleRepository selectedFeeScheduleRepository;

    @Autowired
    private GenericTableUpdateRepository genericTableUpdateRepository;

    @Autowired
    private ISelectedNumbersRepo selectedNumbersRepo;

    @Value("${authorization}")
    private String authorization;

    @Value("${payStatus}")
    private String payStatus;

    public InvoiceServiceImpl(RestUtil restUtil, ObjectMapper mapper) {
        this.restUtil = restUtil;
        this.mapper = mapper;
    }

    /**
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<InvoiceModel> createInvoice(InvoiceModel model) throws Exception {
        GenericResponse<InvoiceModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.createInvoice(model);
            log.info("AutoFeeResponse code  ====== {} ", responseCode);
            if (responseCode == 1) {

                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while saving new company representative {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }


    /**
     * @param transactionRefId
     * @return
     * @throws Exception
     */
    @Override
    public Boolean updateStatus(String transactionRefId) throws Exception {
        try {
            CheckPaymentResponse checkPaymentResponse = new CheckPaymentResponse();

            String url = payStatus + transactionRefId;
            log.info("Request coming in::::: {} and url :: {} ", transactionRefId, url);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", authorization);
            headers.add("Content-Type", "application/json");

            log.info("Request coming in::::: {} and url :: {} ", transactionRefId, url);

            ResponseEntity<String> response = restUtil.setUrl(url).setTimeout(60)
                    .setRequest(headers).get(String.class);

            log.info("AutoFeeResponse from eservices status code :: {} ", response.getStatusCode());
            log.info("AutoFeeResponse from eservices body :: {} ", response.getBody());

            checkPaymentResponse = mapper.readValue(response.getBody(), CheckPaymentResponse.class);

            if (checkPaymentResponse != null) {
                String statusFromEservices = checkPaymentResponse.getStatus();
                int resp = repository.updateStatus(transactionRefId, statusFromEservices);
                return resp == 1;
            }
            return false;
        } catch (Exception exe) {
            return false;
        }
    }


    /**
     * @param invoiceId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<InvoiceObject> getById(int invoiceId) throws Exception {
        GenericResponse<InvoiceObject> genericResponse = new GenericResponse<>();
        try {
            InvoiceObject invoiceObject = new InvoiceObject();

            InvoiceModel invoiceModel = repository.getById(invoiceId);
            if (invoiceModel != null) {
                String appId = invoiceModel.getApplicationId();
                List<SelectedNumberModel> selectedNumberModels = selectedNumbersRepo.getSelectedNumber(appId);
                List<SelectedFeeScheduleModel> selectedFeeScheduleModel = new ArrayList<>();

                if (Objects.equals(invoiceModel.getInvoiceType(), "NEW")) {
                    selectedFeeScheduleModel = selectedFeeScheduleRepository.findByApplicationIdNewFee(appId);
                }
                if (Objects.equals(invoiceModel.getInvoiceType(), "RENEWAL")) {
                    selectedFeeScheduleModel = selectedFeeScheduleRepository.
                            findByApplicationIdRenewalFee(appId);
                }
                if (Objects.equals(invoiceModel.getInvoiceType(), "APPLICATION")) {
                    selectedFeeScheduleModel = selectedFeeScheduleRepository.
                            findByApplicationIdApplictaionFee(appId);
                }

                // assembling final response
                invoiceObject.setInvoiceModel(invoiceModel);
                invoiceObject.setSelectedFees(selectedFeeScheduleModel);
                invoiceObject.setSelectedNumbers(selectedNumberModels);

                log.info("Invoice Object {}", invoiceObject);
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(invoiceObject);
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }

        } catch (Exception exe) {
            log.info("Exception occurred {} ", exe.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }


    /**
     * @param invoiceId
     * @return
     * @throws Exception
     */
    @Override
    public InvoiceObject getByIdPlain(int invoiceId) throws Exception {
        try {
            InvoiceObject invoiceObject = new InvoiceObject();

            InvoiceModel invoiceModel = repository.getById(invoiceId);
            if (invoiceModel != null) {
                String appId = invoiceModel.getApplicationId();
                List<SelectedFeeScheduleModel> selectedFeeScheduleModel = selectedFeeScheduleRepository.
                        findByApplicationId(appId);
                List<SelectedNumberModel> selectedNumberModels = selectedNumbersRepo.getSelectedNumber(appId);

                // assembling final response
                invoiceObject.setInvoiceModel(invoiceModel);
                invoiceObject.setSelectedFees(selectedFeeScheduleModel);
                invoiceObject.setSelectedNumbers(selectedNumberModels);

                log.info("Invoice Object {}", invoiceObject);
                return invoiceObject;
            }

        } catch (Exception exe) {
            log.info("Exception occurred {} ", exe.getMessage());
        }
        return null;
    }

    /**
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<InvoiceModel>> getAll(String rowNumber) throws Exception {
        GenericResponse<List<InvoiceModel>> genericResponse = new GenericResponse<>();
        try {
            List<InvoiceModel> invoiceModelList = repository.getAll(rowNumber);
            log.info("Result set {} ====> ", invoiceModelList);

            if (invoiceModelList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(invoiceModelList);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception !!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }


    /**
     * @param applicationPaymentStatus
     * @param invoiceType
     * @param invoiceNumber
     * @param startDate
     * @param endDate
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<InvoiceModel>> filterForRegularUser(String applicationPaymentStatus,
                                                                    String invoiceType, String invoiceNumber,
                                                                    String startDate, String endDate, String rowNumber) throws Exception {
        GenericResponse<List<InvoiceModel>> genericResponse = new GenericResponse<>();
        try {
            List<InvoiceModel> invoiceModelList = repository.filterForRegularUser(applicationPaymentStatus, invoiceType,
                    invoiceNumber, startDate, endDate, rowNumber);
            log.info("Filtering: Result set from repository {} ====> ", invoiceModelList);

            if (invoiceModelList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(invoiceModelList);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while filtering company representative {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    @Override
    public GenericResponse<List<InvoiceModel>> filterForRegularUser(String companyName, String applicationPaymentStatus, String invoiceType, String invoiceNumber, String startDate, String endDate, String rowNumber) throws Exception {
        GenericResponse<List<InvoiceModel>> genericResponse = new GenericResponse<>();
        try {
            List<InvoiceModel> invoiceModelList = repository.filterForRegularUser(companyName, applicationPaymentStatus, invoiceType,
                    invoiceNumber, startDate, endDate, rowNumber);
            log.info("Filtering: Result set from repository {} ====> ", invoiceModelList);

            if (invoiceModelList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(invoiceModelList);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while filtering company representative {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    @Override
    public GenericResponse<List<InvoiceModel>> filterForAdminUser(String applicationPaymentStatus,
                                                                  String invoiceType, String invoiceNumber,
                                                                  String organization, String startDate,
                                                                  String endDate, String rowNumber) throws Exception {
        GenericResponse<List<InvoiceModel>> genericResponse = new GenericResponse<>();
        try {
            List<InvoiceModel> invoiceModelList = repository.filterForAdminUser(applicationPaymentStatus, invoiceType,
                    invoiceNumber, organization, startDate, endDate, rowNumber);
            log.info("Filtering: Result set from repository {} ====> ", invoiceModelList);

            if (invoiceModelList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(invoiceModelList);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while filtering company representative {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }


    /**
     * @param applicationId
     * @param subType
     * @param amount
     * @param status
     * @param isAllocationFee
     * @param feeDescription
     * @return
     * @throws Exception
     */
    @Override
    public int persistInvoice(String applicationId, String subType, Integer amount, String status, boolean isAllocationFee, String feeDescription, String invoiceType) throws Exception {
        InvoiceModel invoiceModel = new InvoiceModel();
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
//        No invoice for ispc number
        int responseCode = 0;
        int isUpdatedForSend = 0;
        String invoiceNumber = "NMS000" + RefGenerator.getRefNo(3);

        try {
            switch (subType.toUpperCase()) {
                case "NATIONAL":
                    StandardNumberModel nat = standardNumberRepository.getStandardNoByApplicationId(applicationId);
                    invoiceModel.setInvoiceId("");
                    invoiceModel.setInvoiceNumber(invoiceNumber);
                    invoiceModel.setNumberType("Standard");
                    invoiceModel.setApplicationId(applicationId);
                    invoiceModel.setOrganization((nat.getCompanyName() != null) ? nat.getCompanyName() : "");
                    invoiceModel.setNumberSubType("National");
                    invoiceModel.setAmount(amount);
                    invoiceModel.setStatus(status);
                    invoiceModel.setInitiatorEmail((nat.getCompanyEmail() != null) ? nat.getCompanyEmail() : "");
                    invoiceModel.setInitiatorUsername((nat.getCreatedBy() != null) ? nat.getCreatedBy() : "");
                    invoiceModel.setValueDate(timeStamp);
                    log.info("Is application type {}", nat.getApplicationType());
                    if (isAllocationFee) {
                        if (invoiceType != null &&
                                Objects.equals(invoiceType, "NEW")) {
                            invoiceModel.setInvoiceType("NEW");
                            invoiceModel.setDescription("NATIONAL NUMBERING RESOURCE ALLOCATION OFFER INVOICE");
                            invoiceModel.setShouldSendToEservices("TRUE");
                        } else {
                            invoiceModel.setInvoiceType("RENEWAL");
                            invoiceModel.setDescription("Standard National Number Renewal Fee");
                            invoiceModel.setShouldSendToEservices("TRUE");
                        }
                    } else {
                        invoiceModel.setInvoiceType("APPLICATION");
                        invoiceModel.setDescription(feeDescription);
                    }
                    responseCode = invoiceRepository.createInvoice(invoiceModel);
                    log.info("Inside national invoice {}", responseCode);

                    return responseCode;

                case "GEOGRAPHICAL":
                    StandardNumberModel geo = standardNumberRepository.getStandardNoByApplicationId(applicationId);
                    log.info("Get by id standard {}", geo);
                    invoiceModel.setInvoiceId("");
                    invoiceModel.setInvoiceNumber(invoiceNumber);
                    invoiceModel.setNumberType("Standard");
                    invoiceModel.setApplicationId(applicationId);
                    invoiceModel.setOrganization((geo.getCompanyName() != null) ? geo.getCompanyName() : "");
                    invoiceModel.setNumberSubType("Geographical");
                    invoiceModel.setAmount(amount);
                    invoiceModel.setStatus(status);
                    invoiceModel.setInitiatorEmail((geo.getCompanyEmail() != null) ? geo.getCompanyEmail() : "");
                    invoiceModel.setInitiatorUsername((geo.getCreatedBy() != null) ? geo.getCreatedBy() : "");
                    invoiceModel.setValueDate(timeStamp);
                    log.info("Is application type {}", geo.getApplicationType());
                    if (isAllocationFee) {
                        if (invoiceType != null &&
                                Objects.equals(invoiceType, "NEW")) {
                            invoiceModel.setInvoiceType("NEW");
                            invoiceModel.setDescription("GEOGRAPHICAL NUMBERING RESOURCE ALLOCATION OFFER INVOICE");
                            invoiceModel.setShouldSendToEservices("TRUE");
                        } else {
                            invoiceModel.setInvoiceType("RENEWAL");
                            invoiceModel.setDescription("Standard Geographical Number Renewal Fee");
                            invoiceModel.setShouldSendToEservices("TRUE");
                        }
                    } else {
                        invoiceModel.setInvoiceType("APPLICATION");
                        invoiceModel.setDescription(feeDescription);
                    }

                    responseCode = invoiceRepository.createInvoice(invoiceModel);
                    log.info("Inside geographical invoice {}", responseCode);
                    return responseCode;

                case "VANITY":
                    SpecialNumberModel vanity = specialNumberRepository.getSpecialNoByApplicationId(applicationId);
                    log.info("Get by id vanity {}", vanity);
                    log.info("Is application type {}", vanity.getApplicationType());
                    invoiceModel.setInvoiceId("");
                    invoiceModel.setInvoiceNumber(invoiceNumber);
                    invoiceModel.setNumberType("Special");
                    invoiceModel.setApplicationId(applicationId);
                    invoiceModel.setOrganization((vanity.getCompanyName() != null) ? vanity.getCompanyName() : "");
                    invoiceModel.setNumberSubType("Vanity");
                    invoiceModel.setAmount(amount);
                    invoiceModel.setStatus(status);
                    invoiceModel.setInitiatorEmail((vanity.getCompanyEmail() != null) ? vanity.getCompanyEmail() : "");
                    invoiceModel.setInitiatorUsername((vanity.getCreatedBy() != null) ? vanity.getCreatedBy() : "");
                    invoiceModel.setValueDate(timeStamp);
                    if (isAllocationFee) {
                        if (invoiceType != null &&
                                Objects.equals(invoiceType, "NEW")) {
                            invoiceModel.setInvoiceType("NEW");
                            invoiceModel.setDescription("VANITY NUMBERING RESOURCE ALLOCATION OFFER INVOICE");
                            invoiceModel.setShouldSendToEservices("TRUE");
                        } else {
                            invoiceModel.setInvoiceType("RENEWAL");
                            invoiceModel.setDescription("Special Vanity Number Renewal Fee");
                            invoiceModel.setShouldSendToEservices("TRUE");
                        }

                    } else {
                        invoiceModel.setInvoiceType("APPLICATION");
                        invoiceModel.setDescription(feeDescription);
                    }

                    responseCode = invoiceRepository.createInvoice(invoiceModel);
                    log.info("Inside vanity invoice {}", responseCode);

                    return responseCode;


                case "TOLL-FREE":
                    SpecialNumberModel tollFree = specialNumberRepository.getSpecialNoByApplicationId(applicationId);
                    log.info("Get by id {}", tollFree);
                    log.info("Is application type {}", tollFree.getApplicationType());
                    invoiceModel.setInvoiceId("");
                    invoiceModel.setInvoiceNumber(invoiceNumber);
                    invoiceModel.setApplicationId(applicationId);
                    invoiceModel.setOrganization((tollFree.getCompanyName() != null) ? tollFree.getCompanyName() : "");
                    invoiceModel.setNumberType("Special");
                    invoiceModel.setNumberSubType("Toll-free");
                    invoiceModel.setAmount(amount);
                    invoiceModel.setStatus(status);
                    invoiceModel.setInitiatorEmail((tollFree.getCompanyEmail() != null) ? tollFree.getCompanyEmail() : "");
                    invoiceModel.setInitiatorUsername((tollFree.getCompanyName() != null) ? tollFree.getCreatedBy() : "");
                    invoiceModel.setValueDate(timeStamp);
                    if (isAllocationFee) {
                        if (invoiceType != null &&
                                Objects.equals(invoiceType, "NEW")) {
                            invoiceModel.setInvoiceType("NEW");
                            invoiceModel.setDescription("TOLL-FREE NUMBERING RESOURCE ALLOCATION OFFER INVOICE");
                            invoiceModel.setShouldSendToEservices("TRUE");
                        } else {
                            invoiceModel.setInvoiceType("RENEWAL");
                            invoiceModel.setDescription("Special Toll-Free Number Renewal Fee");
                            invoiceModel.setShouldSendToEservices("TRUE");
                        }

                    } else {
                        invoiceModel.setInvoiceType("APPLICATION");
                        invoiceModel.setDescription(feeDescription);
                    }

                    responseCode = invoiceRepository.createInvoice(invoiceModel);
                    log.info("Inside toll-free invoice {}", responseCode);

                    return responseCode;

                case "SHORT-CODE":
                    ShortCodeModel shortCode = shortCodeRepository.getShortCodeByApplId(applicationId);
                    log.info("Get by id {}", shortCode);
                    log.info("Is application type {}", shortCode.getApplicationType());
                    invoiceModel.setApplicationId(applicationId);
                    invoiceModel.setInvoiceId("");
                    invoiceModel.setInvoiceNumber(invoiceNumber);
                    invoiceModel.setOrganization((shortCode.getCompanyName() != null) ? shortCode.getCompanyName() : "");
                    invoiceModel.setNumberType("Short-Code");
                    invoiceModel.setNumberSubType("Short-Code");
                    invoiceModel.setAmount(amount);
                    invoiceModel.setDescription(feeDescription);
                    invoiceModel.setStatus(status);
                    invoiceModel.setInitiatorEmail((shortCode.getCompanyEmail() != null) ? shortCode.getCompanyEmail() : "");
                    invoiceModel.setInitiatorUsername((shortCode.getCreatedBy() != null) ? shortCode.getCreatedBy() : "");
                    invoiceModel.setValueDate(timeStamp);

                    log.info("Short code application type:::::::: {}, ", shortCode.getApplicationType());
                    if (isAllocationFee) {
                        if (invoiceType != null &&
                                Objects.equals(invoiceType, "NEW")) {
                            invoiceModel.setInvoiceType("NEW");
                            invoiceModel.setDescription("Short Code Allocation Fee");
                            invoiceModel.setShouldSendToEservices("TRUE");
                        } else {
                            invoiceModel.setInvoiceType("RENEWAL");
                            invoiceModel.setDescription("Short code Number Renewal Fee");
                            invoiceModel.setShouldSendToEservices("TRUE");
                        }
                    } else {
                        invoiceModel.setInvoiceType("APPLICATION");
                        invoiceModel.setDescription(feeDescription);
                    }

                    responseCode = invoiceRepository.createInvoice(invoiceModel);
                    log.info("Inside short-code invoice {}", responseCode);

                    return responseCode;

                default:
                    break;
            }
        } catch (Exception exe) {
            log.info("Exception occurred during persist of application fee invoice {} ", exe.getMessage());
        }
        return 0;
    }
}
