package com.molcom.nms.organisationprofile.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.organisationprofile.dto.OrganisationDetailsObject;
import com.molcom.nms.organisationprofile.dto.OrganisationProfileModel;

import java.util.List;

public interface IOrganisationProfileService {
    GenericResponse<OrganisationProfileModel> getByOrganisationProfileId(int organisationProfileId) throws Exception;

    GenericResponse<OrganisationDetailsObject> getByOrganisationDetails(String organisationName) throws Exception;

    GenericResponse<OrganisationProfileModel> deleteById(int coverageId) throws Exception;

    GenericResponse<List<OrganisationProfileModel>> getAll() throws Exception;

    GenericResponse<OrganisationProfileModel> findByName(String organisationName) throws Exception;

    GenericResponse<List<OrganisationProfileModel>> findByNameList(String organisationName) throws Exception;


    GenericResponse<List<OrganisationProfileModel>> filter(String queryParam1, String queryValue1, String rowNumber) throws Exception;


}
