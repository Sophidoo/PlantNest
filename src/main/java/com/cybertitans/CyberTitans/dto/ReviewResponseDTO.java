package com.cybertitans.CyberTitans.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDTO {
    private List<ReviewDTO> content;
    private int pageNo;
    private int pageSize;
    private int pageElement;
    private int totalPages;
    private boolean last;


}
