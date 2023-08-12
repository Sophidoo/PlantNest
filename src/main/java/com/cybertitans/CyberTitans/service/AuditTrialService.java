package com.cybertitans.CyberTitans.service;

import com.cybertitans.CyberTitans.dto.AuditTrialDTO;
import com.cybertitans.CyberTitans.dto.AuditTrialResponseDTO;
import com.cybertitans.CyberTitans.model.User;

public interface AuditTrialService {
    AuditTrialResponseDTO getAllAuditTrials(int pageNo, int pageSize, String sortBy, String sortDir);
    void addToAuditTrial(AuditTrialDTO auditTrialDTO);
}
