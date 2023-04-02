package com.molcom.nms.statistics.controller;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.security.TokenHandler;
import com.molcom.nms.statistics.service.IStatisticAdminService;
import com.molcom.nms.statistics.service.IStatisticRegularService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "nms/statistics")
public class StatisticsController {
    @Autowired
    private IStatisticAdminService adminUserStatistics;

    @Autowired
    private IStatisticRegularService regularUserStatistics;

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/adminUser")
    public GenericResponse<?> adminUserStatistics(@RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return adminUserStatistics.getAdminStatistics();
        }
    }

    /**
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/regularUser")
    public GenericResponse<?> regularUserStatistics(@RequestHeader("authorization") String authorization) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return regularUserStatistics.getRegularStatistics();
        }
    }

    /**
     * @param authorization
     * @return
     * @throws Exception
     */
    @GetMapping("/regularUser/company")
    public GenericResponse<?> regularUserStatistics(@RequestHeader("authorization") String authorization,
                                                    @RequestParam String companyName) throws Exception {


        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return regularUserStatistics.getRegularStatistics(companyName);
        }
    }

    @GetMapping("/regularUser/company/v2")
    public GenericResponse<?> regularUserStatisticsV2(@RequestHeader("authorization") String authorization,
                                                      @RequestBody String companyName) throws Exception {

        GenericResponse<?> handle = tokenHandler.tokenHandler(authorization);
        if (handle.getResponseCode().equals(com.molcom.nms.general.utils.ResponseStatus.UNAUTHORIZED.getCode())) {
            return handle;
        } else {
            return regularUserStatistics.getRegularStatistics(companyName);
        }
    }
}