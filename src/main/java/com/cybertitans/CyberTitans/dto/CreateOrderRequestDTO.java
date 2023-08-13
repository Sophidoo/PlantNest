package com.cybertitans.CyberTitans.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequestDTO {
    private List<Long> productIds;
}
