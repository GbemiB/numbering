package com.molcom.nms.number.renewal.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.invoice.dto.InvoiceModel;
import com.molcom.nms.invoice.repository.InvoiceRepository;
import com.molcom.nms.number.renewal.dto.NumRenewalModel;
import com.molcom.nms.number.renewal.dto.NumRenewalObject;
import com.molcom.nms.number.renewal.repository.NumRenewalRepository;
import com.molcom.nms.number.selectedNumber.dto.SelectedNumberModel;
import com.molcom.nms.number.selectedNumber.repository.ISelectedNumbersRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NumRenewalService implements INumRenewalService {
    @Autowired
    private NumRenewalRepository repository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ISelectedNumbersRepo selectedNumbersRepo;

    /**
     * Service implementation to get billing record by application id. The method calls the
     * repository to get billing record by application id.
     *
     * @param renewalId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<NumRenewalObject> getByRenewalId(int renewalId) throws Exception {
        GenericResponse<NumRenewalObject> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            NumRenewalModel numRenewal = repository.getByRenewalId(renewalId);
            log.info("AutoFeeResponse code of number renewal find by id ====== {} ", numRenewal);
            if (numRenewal != null) {
                NumRenewalObject object = new NumRenewalObject();


                // Get selected renewal fees
                List<InvoiceModel> invoiceModels =
                        invoiceRepository.getInvoiceForRenewal(numRenewal.getApplicationId());

                // Get selected number for the application
                List<SelectedNumberModel> selectedNumberModels =
                        selectedNumbersRepo.getSelectedNumber(numRenewal.getApplicationId());


                object.setNumRenewalModel(numRenewal);
                object.setInvoiceModelList(invoiceModels);
                object.setSelectedNumberModelList(selectedNumberModels);

                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(object);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred while finding company representative {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to filterForRegularUser billing record. The method calls the
     * repository to filterForRegularUser billing record by query param.
     *
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<NumRenewalModel>> filterForRegularUser(String queryParam1, String queryValue1,
                                                                       String queryParam2, String queryValue2,
                                                                       String queryParam3, String queryValue3,
                                                                       String queryParam4, String queryValue4,
                                                                       String queryParam5, String queryValue5,
                                                                       String companyName,
                                                                       String rowNumber) throws Exception {
        GenericResponse<List<NumRenewalModel>> genericResponse = new GenericResponse<>();
        try {
            List<NumRenewalModel> numRenewalModelList = repository.filterForRegularUser(queryParam1, queryValue1,
                    queryParam2, queryValue2,
                    queryParam3, queryValue3,
                    queryParam4, queryValue4,
                    queryParam5, queryValue5,
                    companyName,
                    rowNumber);
            log.info("Result set from repository {} ====> ", numRenewalModelList);

            if (numRenewalModelList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(numRenewalModelList);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while finding company representative {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param queryParam1
     * @param queryValue1
     * @param queryParam2
     * @param queryValue2
     * @param queryParam3
     * @param queryValue3
     * @param queryParam4
     * @param queryValue4
     * @param queryParam5
     * @param queryValue5
     * @param queryParam6
     * @param queryValue6
     * @param queryParam7
     * @param queryValue7
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<NumRenewalModel>> filterForAdminUser(String queryParam1, String queryValue1,
                                                                     String queryParam2, String queryValue2,
                                                                     String queryParam3, String queryValue3,
                                                                     String queryParam4, String queryValue4,
                                                                     String queryParam5, String queryValue5,
                                                                     String queryParam6, String queryValue6,
                                                                     String queryParam7, String queryValue7,
                                                                     String rowNumber) throws Exception {
        GenericResponse<List<NumRenewalModel>> genericResponse = new GenericResponse<>();
        try {
            List<NumRenewalModel> numRenewalModelList = repository.filterForAdminUser(queryParam1, queryValue1,
                    queryParam2, queryValue2,
                    queryParam3, queryValue3,
                    queryParam4, queryValue4,
                    queryParam5, queryValue5,
                    queryParam6, queryValue6,
                    queryParam7, queryValue7,
                    rowNumber);
            log.info("Result set from repository {} ====> ", numRenewalModelList);

            if (numRenewalModelList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(numRenewalModelList);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while finding company representative {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<NumRenewalModel>> getAll() throws Exception {
        GenericResponse<List<NumRenewalModel>> genericResponse = new GenericResponse<>();
        try {
            List<NumRenewalModel> numRenewalModelList = repository.getAll();
            log.info("Result set from repository {} ====> ", numRenewalModelList);

            if (numRenewalModelList != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(numRenewalModelList);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

}
