package com.funds.transfer.controller;

import com.funds.transfer.model.ExchangeRate;
import com.funds.transfer.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/funds/v1")
public class CurrencyExchangeController {

    private ExchangeRateService exchangeRateService;

    @Autowired
    public CurrencyExchangeController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/latest/{baseCurrency}")
    public ExchangeRate getLatestRates(@PathVariable String baseCurrency) {
        return exchangeRateService.getLatestRates(baseCurrency);
    }

    @GetMapping("/pair/{baseCurrency}/{targetCurrency}")
    public ExchangeRate getExchangeRateForPair(@PathVariable String baseCurrency,
                                                       @PathVariable String targetCurrency) {
        return exchangeRateService.getExchangeRateForPair(baseCurrency, targetCurrency);
    }
}
