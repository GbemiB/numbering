package com.molcom.nms.additionofservice.controller;

import com.molcom.nms.additionofservice.dto.AdditionOfServiceModel;
import com.molcom.nms.additionofservice.service.AdditionOfService;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.security.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/addition-of-service")
public class AdditionOfServiceController {
    @Autowired
    private AdditionOfService serviceService;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * Endpoint to save addition of service
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping()
    public GenericResponse<?> save(@RequestBody AdditionOfServiceModel model,
                                   @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return serviceService.save(model);
        }
    }

    /**
     * Endpoint to delete addition fo service by id
     *
     * @param additionOfServiceId
     * @return
     * @throws Exception
     */
    @DeleteMapping()
    public GenericResponse<?> delete(@RequestParam int additionOfServiceId,
                                     @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return serviceService.deleteById(additionOfServiceId);
        }

    }

    /**
     * Endpoint to get addition of service by id
     *
     * @param applicationId
     * @return
     * @throws Exception
     */
    @GetMapping("/getById")
    public GenericResponse<?> getById(@RequestParam String applicationId,
                                      @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return serviceService.findById(applicationId);
        }
    }

    /**
     * Endpoint to get allocated short code per company the short code is allocated to
     *
     * @param companyName
     * @return
     * @throws Exception
     */
    @GetMapping("/allocatedShortCodes")
    public GenericResponse<?> getAllocatedShortCodes(@RequestParam String companyName,
                                                     @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return serviceService.getAllocatedNos(companyName);
        }
    }

    /**
     * Endpoint to get all addition of service application
     *
     * @return
     * @throws Exception
     */
    @GetMapping("getAll")
    public GenericResponse<?> getAll(@RequestHeader("authorization") String authorization) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return serviceService.getAll();
        }
    }

    /**
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("getAllByOrganisation")
    public GenericResponse<?> getAll(@RequestHeader("authorization") String authorization,
                                     String organisation) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return serviceService.getAllByOrganisation(organisation);
        }
    }

    /**
     * Endpoint to filter addition of service application
     *
     * @param queryParam1 ShortCode
     * @param queryValue1
     * @param queryParam2 ApplicationStatus
     * @param queryValue2
     * @param queryValue3 { start date value}
     * @param queryValue4 { end date value}
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @GetMapping("/filter")
    public GenericResponse<?> filter(@RequestParam String queryParam1,
                                     @RequestParam String queryValue1,
                                     @RequestParam String queryParam2,
                                     @RequestParam String queryValue2,
                                     @RequestParam String queryValue3,
                                     @RequestParam String queryValue4,
                                     @RequestParam String rowNumber,
                                     @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return serviceService.filter(queryParam1, queryValue1, queryParam2, queryValue2,
                    queryValue3, queryValue4, rowNumber);
        }
    }


    @GetMapping("/filterRegularUser")
    public GenericResponse<?> filter(@RequestParam String companyName,
                                     @RequestParam String queryParam1,
                                     @RequestParam String queryValue1,
                                     @RequestParam String queryParam2,
                                     @RequestParam String queryValue2,
                                     @RequestParam String queryValue3,
                                     @RequestParam String queryValue4,
                                     @RequestParam String rowNumber,
                                     @RequestHeader("authorization") String authorization) throws Exception {
        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return serviceService.filterForRegularUser(companyName, queryParam1, queryValue1, queryParam2, queryValue2,
                    queryValue3, queryValue4, rowNumber);
        }
    }
}
