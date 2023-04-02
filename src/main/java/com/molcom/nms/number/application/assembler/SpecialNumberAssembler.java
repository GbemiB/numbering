package com.molcom.nms.number.application.assembler;

import com.molcom.nms.approvals.repository.ApprovalRepository;
import com.molcom.nms.number.application.dto.SpecialNumberModel;
import com.molcom.nms.number.application.dto.SpecialNumberObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpecialNumberAssembler {

    @Autowired
    private ApprovalRepository approvalRepository;

    public SpecialNumberObject specialNumberBuilder(SpecialNumberModel model) {

        SpecialNumberObject response = new SpecialNumberObject();
        try {

            response.setApplicationId(model.getApplicationId());
            response.setSubType(model.getSubType());
            response.setCompanyName(model.getCompanyName());
            response.setCompanyEmail(model.getCompanyEmail());
            response.setCompanyPhone(model.getCompanyPhone());
            response.setCompanyFax(model.getCompanyFax());
            response.setCompanyState(model.getCompanyState());
            response.setCompanyAddress(model.getCompanyAddress());
            response.setCorrespondingEmail(model.getCorrespondingEmail());
            response.setCorrespondingPhone(model.getCorrespondingPhone());
            response.setCorrespondingFax(model.getCorrespondingFax());
            response.setCorrespondingState(model.getCorrespondingState());
            response.setCorrespondingAddress(model.getCorrespondingAddress());
            response.setInterconnectAgreement(model.getInterconnectAgreement());
            response.setAccessCode(model.getAccessCode());
            response.setAvailableNumber(model.getAvailableNumber());
            response.setNoOfTelcoCompanies(model.getNoOfTelcoCompanies());
            response.setTelcoCompanies(model.getTelcoCompanies());
            response.setHaveYouReachedAgreement(model.getHaveYouReachedAgreement());
            response.setHaveYouMetFinancialReq(model.getHaveYouMetFinancialReq());
            response.setFrequentAssignment(model.getFrequentAssignment());
            response.setAvailableNumber(model.getAvailableNumber());
            response.setUndertakenComment(model.getUndertakenComment());
            response.setApplicationType(model.getApplicationType());
            response.setApplicationStatus(model.getApplicationStatus());
            response.setApplicationPaymentStatus(model.getApplicationPaymentStatus());
            response.setAllocationPaymentStatus(model.getAllocationPaymentStatus());
            response.setCreatedBy(model.getCreatedBy());
            response.setCreatedDate(model.getCreatedDate());
            response.setIpAddress(model.getIpAddress());
            response.setQuantity(model.getQuantity());
            response.setCurrentStep(model.getCurrentStep());
            response.setIsMDA(model.getIsMDA());
            response.setIsApplicationAssigned(model.getIsApplicationAssigned());
            response.setIsApprovalCompleted(model.getIsApprovalCompleted());

        } catch (Exception exception) {
            log.info("Exception occurred while assembling special number response {} ", exception.getMessage());
        }
        return response;
    }
}
