package com.molcom.nms.eservicesintegrations.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.molcom.nms.adminmanage.dto.AdminManagementModel;
import com.molcom.nms.adminmanage.repository.AdminManagementRepo;
import com.molcom.nms.eservicesintegrations.dto.AuthUserResponse;
import com.molcom.nms.eservicesintegrations.dto.CompanyDetailResponse;
import com.molcom.nms.eservicesintegrations.dto.setups.ValidateUserResponse;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.general.utils.RestUtil;
import com.molcom.nms.organisationprofile.dto.OrganisationProfileModel;
import com.molcom.nms.organisationprofile.repository.OrganisationProfileRepo;
import com.molcom.nms.rolesandpriviledge.userroles.repository.UserRoleRepo;
import com.molcom.nms.security.JwtSecurityService;
import com.molcom.nms.security.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AuthService {
    @Autowired
    private final RestUtil restUtil;
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private AdminManagementRepo adminManagementRepo;
    @Autowired
    private OrganisationProfileRepo organisationProfileRepo;
    @Autowired
    private UserRoleRepo userRoleRepo;
    @Autowired
    private JwtSecurityService jwtSecurityService;

    @Value("${authorization}")
    private String authorization;
    @Value("${authUrl}")
    private String authUrl;
    @Value("${companyUrl}")
    private String companyUrl;
    @Value("${admin.role.name}")
    private String adminRole;

    public AuthService(RestUtil restUtil) {
        this.restUtil = restUtil;
    }

    // First step: persist user info in admin: update if available
    // call for user role and check if admin
    // Check if organisation exist else persist organisation in organisation profile table

    public GenericResponse<AuthUserResponse> authenticate(String token) throws Exception {
        GenericResponse<AuthUserResponse> response = new GenericResponse<>();
        AuthUserResponse auth = new AuthUserResponse();
        AdminManagementModel admin = new AdminManagementModel();
        OrganisationProfileModel org = new OrganisationProfileModel();
        String isAdmin = "";
        String roleName = "";
        try {
            // call eservices to validate user
            ValidateUserResponse validate = callValidateUser(token);

            if (validate != null) {
                // Get all user roles
                List<String> userRoles = checkUserRole(validate.getUsername());

                // pass company id from validate user to get company details
                String companyId = validate.getOrganization().getOrganizationId();
                CompanyDetailResponse company = callCompanyDetails(companyId);
                log.info("Company Info ::::::::::: {}", company);

                admin.setEmail(validate.getAppUserEmail());
                admin.setName(validate.getFirstName() + validate.getLastName());
                admin.setUserName(validate.getUsername());
                admin.setAssignedRole("");
                admin.setUserId("");
                admin.setSignature("");
                if (company != null) {
                    admin.setOrganisation(company.getCompanyName());
                }

                log.info("Company ::::::::::: {}", admin);
                boolean saveUser = adminManagementRepo.persistUser(admin);
                log.info("Save user in admin table {}", saveUser);

                // Is admin
                auth.setIsAdmin(userRoles != null ? "TRUE" : "FALSE");

                // user roles
                auth.setUserRoles(userRoles);

                Token jwt = jwtSecurityService.create512JwtToken(validate.getUsername());
                auth.setJwt(jwt);

                if (company != null) {
                    int countIfOrganisationExit = organisationProfileRepo.countIfOrganisationExist(company.getCompanyName());
                    log.info("Organisation count {}", countIfOrganisationExit);

                    if (countIfOrganisationExit < 1) {
                        org.setOrganisationName(company.getCompanyName());
                        org.setNccId(company.getNccId());
                        org.setOrganisationProfileId("");
                        int persistOrganisation = organisationProfileRepo.saveOrganisationOnFistLogin(org);
                        log.info("Save company in organisation profile table {}", persistOrganisation);
                    }

                    // company details
                    auth.getCompanyObject().setCompanyName(company.getCompanyName());
                    auth.getCompanyObject().setCompanyContactEmail(company.getCompanyContactEmail());
                    auth.getCompanyObject().setPhoneNumber(company.getPhoneNumber());
                    auth.getCompanyObject().setFax(company.getFax());
                    auth.getCompanyObject().setHeadOfficeAddressCity(company.getHeadOfficeAddressCity());
                    auth.getCompanyObject().setHeadOfficeAddressZip(company.getHeadOfficeAddressZip());
                    auth.getCompanyObject().setHeadOfficeAddressPobox(company.getHeadOfficeAddressPobox());
                    auth.getCompanyObject().setHeadOfficeAddressPmb(company.getHeadOfficeAddressPmb());
                    auth.getCompanyObject().setCompanyContactPhone1(company.getCompanyContactPhone1());
                    auth.getCompanyObject().setCompanyContactPhone2(company.getCompanyContactPhone2());
                    auth.getCompanyObject().setCompanyContactEmail(company.getCompanyContactEmail());
                    auth.getCompanyObject().setNccId(company.getNccId());
                    auth.getCompanyObject().setHeadOfficeAddressStreet(company.getHeadOfficeAddressStreet());
                }

                // user details
                auth.getUserObject().setAppUserId(validate.getAppUserId());
                auth.getUserObject().setAppUserEmail(validate.getAppUserEmail());
                auth.getUserObject().setMobileNumber(validate.getMobileNumber());
                auth.getUserObject().setUserType(validate.getUserType());
                auth.getUserObject().setDateCreated(validate.getDateCreated());
                auth.getUserObject().setFirstName(validate.getFirstName());
                auth.getUserObject().setUsername(validate.getUsername());
                auth.getUserObject().setFirstName(validate.getFirstName());
                auth.getUserObject().setLastName(validate.getLastName());
                auth.getUserObject().setMiddleName(validate.getMiddleName());
                auth.getUserObject().setImage(validate.getImage());
                auth.getUserObject().setAppUserId(validate.getAppUserId());
                auth.getUserObject().setOrganizationId(validate.getOrganization().getOrganizationId());

                response.setResponseCode(ResponseStatus.SUCCESS.getCode());
                response.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                response.setOutputPayload(auth);
                log.info("Final AutoFeeResponse {} ", response);

                return response;
            } else {
                response.setResponseCode(ResponseStatus.FAILED.getCode());
                response.setResponseMessage("Invalid or expired token");
                return response;
            }

        } catch (Exception exe) {
            log.info("Exception occurred !!!! {} ", exe.getMessage());
            return null;
        }

    }

    private List<String> checkUserRole(String username) throws Exception {
        //::::::: get all user roles
        List<AdminManagementModel> userRole = adminManagementRepo.getRolesOfAdmin(username);
        log.info("All roles assigned to user {} {}", username, userRole);
        //::::::: mapping all roles in an array of string
        List<String> mappedRoles = new ArrayList<>();
        userRole.forEach(item -> {
            mappedRoles.add(item.getAssignedRole());
        });
        log.info("Mapped roles {}", mappedRoles);
        return mappedRoles;
    }

    /**
     * Service layer to validate user
     *
     * @param token
     * @return
     * @throws Exception
     */
    private ValidateUserResponse callValidateUser(String token) throws Exception {
        try {
            ValidateUserResponse userResponse = new ValidateUserResponse();
            String url = authUrl + token;
            log.info("Request url :: {} ", url);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", authorization);
            headers.add("Content-Type", "application/json");

            ResponseEntity<String> response = restUtil.setUrl(url).setTimeout(60)
                    .setRequest(headers).get(String.class);

            log.info("AutoFeeResponse from validate user eservices status code :: {} ", response.getStatusCode());
            log.info("AutoFeeResponse from validate user eservices body :: {} ", response.getBody());

            if (response.getStatusCode() == HttpStatus.OK) {
                userResponse = objectMapper.readValue(response.getBody(), ValidateUserResponse.class);
            }

            return userResponse;

        } catch (Exception exe) {
            log.info("Exception occurred {}", exe.getMessage());
            return null;
        }
    }

    /**
     * Service layer to get company details
     *
     * @param companyId
     * @return
     * @throws Exception
     */
    private CompanyDetailResponse callCompanyDetails(String companyId) throws Exception {
        try {
            CompanyDetailResponse companyResponse = new CompanyDetailResponse();
            String url = companyUrl + companyId + "/";

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", authorization);
            headers.add("Content-Type", "application/json");

            ResponseEntity<String> response = restUtil.setUrl(url).setTimeout(60)
                    .setRequest(headers).get(String.class);

            log.info("AutoFeeResponse from get company details eservices status code :: {} ", response.getStatusCode());
            log.info("AutoFeeResponse from get company details  eservices body :: {} ", response.getBody());

            companyResponse = objectMapper.readValue(response.getBody(), CompanyDetailResponse.class);
            return companyResponse;

        } catch (Exception exe) {
            log.info("Exception occurred {}", exe.getMessage());
            return null;
        }
    }
}
