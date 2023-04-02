package com.molcom.nms.organisationprofile.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.organisationprofile.service.IOrganisationProfileService;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(path = "nms/organisationProfile")
public class OrganisationProfileController {

    @Autowired
    private IOrganisationProfileService organisationProfileService;

    @Autowired
    private TokenHandler tokenHandler;

    @GetMapping("/getByOrganisationDetails")
    public GenericResponse<?> getByOrganisationDetails(@RequestParam String organisationName,
                                                       @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return organisationProfileService.getByOrganisationDetails(organisationName);
        }
    }

    /**
     * Endpoint to Search Organization By name
     */

    @GetMapping("/getByOrganisationName")
    public GenericResponse<?> getByOrganisationName(@RequestParam String organisationName,
                                                    @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return organisationProfileService.findByName(organisationName);
        }
    }


    @GetMapping("/getByOrganisationName/list")
    public GenericResponse<?> getByOrganisationNameList(@RequestParam String organisationName,
                                                        @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return organisationProfileService.findByNameList(organisationName);
        }
    }

    /**
     * Endpoint to filterForAmin existing Organization
     * Filters: Organization Name
     *
     * @param queryParam1
     * @param queryValue1
     * @return
     * @throws Exception
     */

    @GetMapping("/filter")
    public GenericResponse<?> filter(@RequestParam String queryParam1,
                                     @RequestParam String queryValue1,
                                     @RequestParam String rowNumber,
                                     @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return organisationProfileService.filter(queryParam1, queryValue1, rowNumber);
        }
    }

//    /**
//     * Endpoint to filterForAmin existing Organization
//     * Filters: Organization Name
//     *
//     * @param queryParam1
//     * @param queryValue1
//     * @return
//     * @throws Exception
//     */

//    @GetMapping("/filter")
//    public GenericResponse<?> filter(@RequestParam String queryParam1,
//                                     @RequestParam String queryValue1,
//                                     String rowNumber,
//                                     @RequestHeader("authorization") String authorization) throws Exception {
//
//
//        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
//        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
//            return handle;
//        } else {
//            return organisationProfileService.filter(queryParam1, queryValue1, rowNumber);
//        }
//    }

    /**
     * Endpoint to delete existing Organization
     *
     * @param organisationProfileId
     * @return
     * @throws Exception
     */

    @DeleteMapping()
    public GenericResponse<?> delete(@RequestParam int organisationProfileId,
                                     @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return organisationProfileService.deleteById(organisationProfileId);
        }
    }

    /**
     * Endpoint to get all Organization
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/getAll")
    public GenericResponse<?> getAll(@RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return organisationProfileService.getAll();
        }
    }
}
