package com.funds.transfer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class ExchangeRate {

    private String result;
    private String base_code;
    private Map<String, Double> conversion_rates;

}
