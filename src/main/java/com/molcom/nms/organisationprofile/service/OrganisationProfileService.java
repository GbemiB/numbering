package com.molcom.nms.organisationprofile.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.organisationprofile.dto.*;
import com.molcom.nms.organisationprofile.repository.OrganisationProfileRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class OrganisationProfileService implements IOrganisationProfileService {

    @Autowired
    private OrganisationProfileRepo repository;

    /**
     * @param organisationName
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<OrganisationDetailsObject> getByOrganisationDetails(String organisationName) throws Exception {
        GenericResponse<OrganisationDetailsObject> genericResponse = new GenericResponse();
        OrganisationDetailsObject details = new OrganisationDetailsObject();

        try {
            List<OrganisationRepresentatives> reps = repository.getOrganisationReps(organisationName);
            List<OrganisationUsers> users = repository.getOrganisationUsers(organisationName);
            List<OrganisationApplications> applications = repository.getOrganisationApplications(organisationName);
            List<OrganisationAllocatedNos> allocatedNos = repository.getOrganisationAllocatedNos(organisationName);

            details.setOrganisationReps(reps);
            details.setOrganisationUsers(users);
            details.setApplications(applications);
            details.setAllocatedNos(allocatedNos);

            if (reps != null && users != null && applications != null && allocatedNos != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(details);
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred !!!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }


    /**
     * @param organisationProfileId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<OrganisationProfileModel> getByOrganisationProfileId(int organisationProfileId) throws Exception {
        GenericResponse<OrganisationProfileModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            OrganisationProfileModel organisationProfileModel = repository.getByOrganisationProileId(organisationProfileId);
            log.info("AutoFeeResponse code of number renewal find by id ====== {} ", organisationProfileModel);
            if (organisationProfileModel != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(organisationProfileModel);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred !!!!!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }


    /**
     * @param organisationProfileId
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<OrganisationProfileModel> deleteById(int organisationProfileId) throws Exception {
        GenericResponse<OrganisationProfileModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;
        try {
            responseCode = repository.deleteById(organisationProfileId);
            log.info("AutoFeeResponse code ====== {} ", responseCode);
            if (responseCode == 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseCode(ResponseStatus.FAILED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.FAILED.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred !!!! {} ", exception.getMessage());
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
    public GenericResponse<List<OrganisationProfileModel>> getAll() throws Exception {
        GenericResponse<List<OrganisationProfileModel>> genericResponse = new GenericResponse<>();
        try {
            List<OrganisationProfileModel> organisationProfileModels = repository.getAll();
            log.info("Result set from repository {} ====> ", organisationProfileModels);
            if (organisationProfileModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(organisationProfileModels);
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
     * @param organisationName
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<OrganisationProfileModel> findByName(String organisationName) throws Exception {
        GenericResponse<OrganisationProfileModel> genericResponse = new GenericResponse<>();
        int responseCode = 0;

        try {
            OrganisationProfileModel organisationProfileModel = repository.findByName(organisationName);
            log.info("Result set from repository {} ====> ", organisationName);
            if (organisationProfileModel != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(organisationProfileModel);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred !!!!", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    @Override
    public GenericResponse<List<OrganisationProfileModel>> findByNameList(String organisationName) throws Exception {
        GenericResponse<List<OrganisationProfileModel>> genericResponse = new GenericResponse<>();
        int responseCode = 0;

        try {
            List<OrganisationProfileModel> organisationProfileModel = repository.findByNameList(organisationName);
            log.info("Result set from repository:::  {} ====> ", organisationName);
            if (organisationProfileModel != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(organisationProfileModel);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }

        } catch (Exception exception) {
            log.info("Exception occurred !!!! {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }

    /**
     * @param queryParam1
     * @param queryValue1
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public GenericResponse<List<OrganisationProfileModel>> filter(String queryParam1, String queryValue1, String rowNumber) throws Exception {
        GenericResponse<List<OrganisationProfileModel>> genericResponse = new GenericResponse<>();
        log.info("Query parameter for filtering {} ", queryParam1);
        try {
            List<OrganisationProfileModel> organisationProfileModels = repository.filter(queryParam1, queryValue1, rowNumber);
            log.info("Filtering: Result set from repository {} ====> ", organisationProfileModels);

            if (organisationProfileModels != null) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                genericResponse.setOutputPayload(organisationProfileModels);
            } else {
                genericResponse.setResponseCode(ResponseStatus.NOT_FOUND.getCode());
                genericResponse.setResponseMessage(ResponseStatus.NOT_FOUND.getMessage());
            }
        } catch (Exception exception) {
            log.info("Exception occurred while filtering organisation profile {} ", exception.getMessage());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }
}
