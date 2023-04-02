package com.molcom.nms.organisationprofile.repository;


import com.molcom.nms.organisationprofile.dto.*;

import java.sql.SQLException;
import java.util.List;

public interface IOrganisationProfileRepo {

    List<OrganisationRepresentatives> getOrganisationReps(String organisationName) throws SQLException;

    List<OrganisationUsers> getOrganisationUsers(String organisationName) throws SQLException;

    List<OrganisationApplications> getOrganisationApplications(String organisationName) throws SQLException;

    List<OrganisationAllocatedNos> getOrganisationAllocatedNos(String organisationName) throws SQLException;

    int countIfOrganisationExist(String organisationName) throws SQLException;

    int saveOrganisationOnFistLogin(OrganisationProfileModel model) throws SQLException;

    int deleteById(int organisationProfileId) throws SQLException;

    OrganisationProfileModel findByName(String organisationName) throws SQLException;

    OrganisationProfileModel getByOrganisationProileId(int OrganisationProfileId) throws SQLException;

    List<OrganisationProfileModel> getAll() throws SQLException;

    List<OrganisationProfileModel> filter(String queryParam1, String queryValue1, String rowNumber) throws SQLException;


    List<OrganisationProfileModel> findByNameList(String organisationName) throws SQLException;
}

