package com.molcom.nms.companyrep.service;

import com.molcom.nms.companyrep.dto.CompRepModel;
import com.molcom.nms.companyrep.repository.ICompRepRepository;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CompRepService implements ICompRepService {

    @Autowired
    private ICompRepRepository repository;

    /**
     * Service implementation to save new company representative. The method calls the
     * repository to persist new company representative.
     *
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<CompRepModel> save(CompRepModel model) throws Exception {
        GenericResponse<CompRepModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.save(model);
            log.info("AutoFeeResponse code of company representative save ====== {} ", responseCode);
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

    @Override
    public GenericResponse<CompRepModel> saveSelectedRep(CompRepModel model) throws Exception {
        GenericResponse<CompRepModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.saveSelectedCompRep(model);
            log.info("AutoFeeResponse code of company representative save ====== {} ", responseCode);
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
     * Service implementation to edit company representative. The method calls the
     * repository to edit existing company representative.
     *
     * @param model
     * @param compRepId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<CompRepModel> edit(CompRepModel model, int compRepId) throws Exception {
        GenericResponse<CompRepModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.edit(model, compRepId);
            log.info("AutoFeeResponse code of company representative edit ====== {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while editing company representative {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }


    /**
     * Service implementation to delete by id company representative The method calls the
     * repository to delete existing company representative.
     *
     * @param compRepId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<CompRepModel> deleteById(int compRepId) throws Exception {
        GenericResponse<CompRepModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteById(compRepId);
            log.info("AutoFeeResponse code of company representative delete ====== {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while deleting company representative {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    @Override
    public GenericResponse<CompRepModel> deleteSelectedRep(int selectedCompReId) throws Exception {
        GenericResponse<CompRepModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteSelectedCompRep(selectedCompReId);
            log.info("AutoFeeResponse code of company representative delete ====== {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while deleting company representative {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to find by id company representative. The method calls the
     * repository to find existing company representative by company representative id.
     *
     * @param compRepId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<CompRepModel> findByCompRepId(int compRepId) throws Exception {
        GenericResponse<CompRepModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            CompRepModel compRep = repository.findByCompRepId(compRepId);
            if (compRep != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(compRep);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred while finding company representative by compRepId{} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to find by user id company representatives. The method calls the
     * repository to find existing company representative by user id.
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<CompRepModel>> findByUserId(String userId) throws Exception {
        GenericResponse<List<CompRepModel>> genericResponse = new GenericResponse<>();
        try {
            List<CompRepModel> compRep = repository.findByUserId(userId);

            if (compRep != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(compRep);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred while finding company representative by compRepId{} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Get all
     *
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<CompRepModel>> getAll() throws Exception {
        GenericResponse<List<CompRepModel>> genericResponse = new GenericResponse<>();
        try {
            List<CompRepModel> compRep = repository.getAll();

            if (compRep != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(compRep);
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

    /**
     * Find selected reps by user Id
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<CompRepModel>> findSelectedRep(String userId) throws Exception {
        GenericResponse<List<CompRepModel>> genericResponse = new GenericResponse<>();
        try {
            List<CompRepModel> compRep = repository.findSelectedCompRep(userId);

            if (compRep != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(compRep);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred while finding company representative by compRepId{} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Return reps without image
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<CompRepModel>> findByUserIdWithoutImage(String userId) throws Exception {
        GenericResponse<List<CompRepModel>> genericResponse = new GenericResponse<>();
        try {
            List<CompRepModel> compRep = repository.findByUserIdWithImage(userId);

            if (compRep != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(compRep);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred while finding company representative by compRepId{} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Find reps without image
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<CompRepModel>> findSelectedRepWithoutImage(String userId) throws Exception {
        GenericResponse<List<CompRepModel>> genericResponse = new GenericResponse<>();
        try {
            List<CompRepModel> compRep = repository.findSelectedCompRepWithoutImage(userId);

            if (compRep != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(compRep);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred while finding company representative by compRepId{} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * Service implementation to filterForRegularUser company representatives. The method calls the
     * repository to filterForRegularUser company representative by query param.
     *
     * @param queryParam1 FirstName
     * @param queryValue1
     * @param queryParam2 LastName
     * @param queryValue2
     * @param userId
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<CompRepModel>> filterForRegularUser(String queryParam1, String queryValue1,
                                                                    String queryParam2, String queryValue2,
                                                                    String userId, String rowNumber) throws Exception {
        GenericResponse<List<CompRepModel>> genericResponse = new GenericResponse<>();
        log.info("Query parameter for filtering {} {} ", queryParam1, queryParam2);
        try {
            List<CompRepModel> compRep = repository.filterForRegularUser(queryParam1, queryValue1,
                    queryParam2, queryValue2, userId, rowNumber);

            if (compRep != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(compRep);
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
     * Filter for admin user
     *
     * @param queryParam1
     * @param queryValue1
     * @param queryParam2
     * @param queryValue2
     * @param queryParam3
     * @param queryValue3
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<CompRepModel>> filterForAdminUser(String queryParam1, String queryValue1,
                                                                  String queryParam2, String queryValue2,
                                                                  String queryParam3, String queryValue3,
                                                                  String rowNumber) throws Exception {
        GenericResponse<List<CompRepModel>> genericResponse = new GenericResponse<>();
        log.info("Query parameter for filtering {} {} {} ", queryParam1, queryParam2, queryParam3);
        try {
            List<CompRepModel> compRep = repository.filterForAdminUser(queryParam1, queryValue1,
                    queryParam2, queryValue2, queryParam3, queryValue3, rowNumber);

            if (compRep != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(compRep);
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
}
