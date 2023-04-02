package com.molcom.nms.companyrep.controller;

import com.molcom.nms.companyrep.dto.CompRepModel;
import com.molcom.nms.companyrep.service.ICompRepService;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/companyRep")
public class CompanyRepresentativeController {
    @Autowired
    private ICompRepService compRepService;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * Endpoint to add new company representative
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> saveCompRep(@RequestBody CompRepModel model,
                                          @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return compRepService.save(model);
        }
    }

    /**
     * Endpoint to get selected company representative
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping("/selectCompanyRep")
    public GenericResponse<?> saveSelectedRep(@RequestBody CompRepModel model,
                                              @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return compRepService.saveSelectedRep(model);
        }
    }

    /**
     * Endpoint to edit existing company representative
     *
     * @param model
     * @param compRepId
     * @return
     * @throws Exception
     */
    @PatchMapping()
    public GenericResponse<?> editCompRep(@RequestBody CompRepModel model,
                                          @RequestParam int compRepId,
                                          @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return compRepService.edit(model, compRepId);
        }
    }

    /**
     * Endpoint to delete existing company representative
     *
     * @param compRepId
     * @return
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> deleteCompRep(@RequestParam int compRepId,
                                            @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return compRepService.deleteById(compRepId);
        }
    }

    /**
     * @param selectedCompReId
     * @param authorization
     * @return
     * @throws Exception
     */
    @DeleteMapping("/deleteSelectedRep")
    public GenericResponse<?> deleteSelectedRep(@RequestParam int selectedCompReId,
                                                @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return compRepService.deleteSelectedRep(selectedCompReId);
        }
    }

    /**
     * Endpoint to find company representative by company representative id (the unique id for the rep)
     *
     * @param compRepId
     * @return
     * @throws Exception
     */
    @GetMapping("/getByCompRepId")
    public GenericResponse<?> getByCompRepId(@RequestParam int compRepId,
                                             @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return compRepService.findByCompRepId(compRepId);
        }
    }

    /**
     * Endpoint to find company representative by user logged in
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @GetMapping("/getByUserId")
    public GenericResponse<?> getByUserId(@RequestParam String userId,
                                          @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return compRepService.findByUserId(userId);
        }
    }

    /**
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getAll")
    public GenericResponse<?> getAll(@RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return compRepService.getAll();
        }
    }

    /**
     * @param userId
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getByUserIdWithoutImage")
    public GenericResponse<?> getByUserIdWithoutImage(@RequestParam String userId,
                                                      @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return compRepService.findByUserIdWithoutImage(userId);
        }
    }

    /**
     * @param applicationId
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getSelectedRepByAppId")
    public GenericResponse<?> findSelectedRep(@RequestParam String applicationId,
                                              @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return compRepService.findSelectedRep(applicationId);
        }
    }

    /**
     * @param applicationId
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/getSelectedRepByAppIdWithoutImage")
    public GenericResponse<?> findSelectedRepWithoutImage(@RequestParam String applicationId,
                                                          @RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return compRepService.findSelectedRepWithoutImage(applicationId);
        }
    }

    /**
     * Endpoint to filterForRegularUser company representative
     * Filters are queryParam1=FirstName and queryParam2=LastName
     *
     * @param queryParam1
     * @param queryValue1
     * @param queryParam2
     * @param queryValue2
     * @param rowNumber
     * @return
     * @throws Exception
     */

    @GetMapping("/filterRegularUser")
    public GenericResponse<?> filterForRegularUser(@RequestParam String queryParam1,
                                                   @RequestParam String queryValue1,
                                                   @RequestParam String queryParam2,
                                                   @RequestParam String queryValue2,
                                                   @RequestParam String userId,
                                                   @RequestParam String rowNumber,
                                                   @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return compRepService.filterForRegularUser(queryParam1, queryValue1, queryParam2, queryValue2,
                    userId, rowNumber);
        }
    }

    /**
     * Endpoint to filterForRegularUser company representative
     * Filters are FirstName, LastName and Organization
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
    @GetMapping("/filterAdminUser")
    public GenericResponse<?> filterForAdminUser(@RequestParam String queryParam1,
                                                 @RequestParam String queryValue1,
                                                 @RequestParam String queryParam2,
                                                 @RequestParam String queryValue2,
                                                 @RequestParam String queryParam3,
                                                 @RequestParam String queryValue3,
                                                 @RequestParam String rowNumber,
                                                 @RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return compRepService.filterForAdminUser(queryParam1, queryValue1, queryParam2, queryValue2, queryParam3,
                    queryValue3, rowNumber);
        }
    }
}
