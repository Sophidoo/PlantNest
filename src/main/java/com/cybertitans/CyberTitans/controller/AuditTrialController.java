package com.cybertitans.CyberTitans.controller;

import com.cybertitans.CyberTitans.dto.AuditTrialResponseDTO;
import com.cybertitans.CyberTitans.service.AuditTrialService;
import com.cybertitans.CyberTitans.utls.AppConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/audit")
public class AuditTrialController {
    private final AuditTrialService auditTrialService;

    public AuditTrialController(AuditTrialService auditTrialService) {
        this.auditTrialService = auditTrialService;
    }

    @GetMapping
    public ResponseEntity<AuditTrialResponseDTO> audit(
            @RequestParam(value = "pageNo", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @RequestParam(value = "filterParam", defaultValue = AppConstant.DEFAULT_FILTER_PARAMETER, required = false) String filterParam
    ){
        AuditTrialResponseDTO allAuditTrials = auditTrialService.getAllAuditTrials(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allAuditTrials, HttpStatus.OK);
    }

}
