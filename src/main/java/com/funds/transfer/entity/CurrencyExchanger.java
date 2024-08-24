package com.funds.transfer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyExchanger {

    private String base_code;
    private String target_code;
    private double conversion_rate;
    private double conversion_result;

}
