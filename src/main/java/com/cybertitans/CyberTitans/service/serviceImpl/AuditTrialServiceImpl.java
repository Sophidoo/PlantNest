package com.cybertitans.CyberTitans.service.serviceImpl;

import com.cybertitans.CyberTitans.dto.AllOrderResponseDTO;
import com.cybertitans.CyberTitans.dto.AuditTrialDTO;
import com.cybertitans.CyberTitans.dto.AuditTrialResponseDTO;
import com.cybertitans.CyberTitans.dto.OrderResponseDTO;
import com.cybertitans.CyberTitans.model.AuditTrial;
import com.cybertitans.CyberTitans.model.Orders;
import com.cybertitans.CyberTitans.model.User;
import com.cybertitans.CyberTitans.repository.AuditTrialRepository;
import com.cybertitans.CyberTitans.service.AuditTrialService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditTrialServiceImpl implements AuditTrialService {
    private final AuditTrialRepository auditTrialRepository;
    private final ModelMapper mapper;

    public AuditTrialServiceImpl(AuditTrialRepository auditTrialRepository, ModelMapper mapper) {
        this.auditTrialRepository = auditTrialRepository;
        this.mapper = mapper;
    }

    @Override
    public AuditTrialResponseDTO getAllAuditTrials(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<AuditTrial> audits = auditTrialRepository.findAll(pageable);
        AuditTrialResponseDTO auditResponse = getAuditResponse(audits);
        return auditResponse;
    }

    @Override
    public void addToAuditTrial(AuditTrialDTO auditTrialDTO) {
        AuditTrial auditTrial = new AuditTrial();
        auditTrial.setUser(auditTrialDTO.getUser());
        auditTrial.setDate(auditTrialDTO.getDate());
        auditTrial.setAudit(auditTrial.getAudit());
        auditTrialRepository.save(auditTrial);
    }

    private AuditTrialResponseDTO getAuditResponse(Page<AuditTrial> audits){
        List<AuditTrial> auditList = audits.getContent();
        List<AuditTrialDTO> content = auditList.stream().map(order -> mapper.map(audits, AuditTrialDTO.class)).collect(Collectors.toList());

        AuditTrialResponseDTO auditResponse = new AuditTrialResponseDTO();
        auditResponse.setContent(content);
        auditResponse.setPageNo(audits.getNumber());
        auditResponse.setPageSize(audits.getSize());
        auditResponse.setTotalElements(audits.getNumberOfElements());
        auditResponse.setTotalPages(audits.getTotalPages());
        auditResponse.setLast(audits.isLast());

        return auditResponse;

    }
}
