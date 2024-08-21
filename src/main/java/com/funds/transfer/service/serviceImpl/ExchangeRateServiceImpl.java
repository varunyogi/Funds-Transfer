package com.funds.transfer.service.serviceImpl;

import com.funds.transfer.feign.CurrencyExchangeRate;
import com.funds.transfer.model.ExchangeRate;
import com.funds.transfer.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final CurrencyExchangeRate currencyExchangeRate;

    @Autowired
    public ExchangeRateServiceImpl(CurrencyExchangeRate currencyExchangeRate) {
        this.currencyExchangeRate = currencyExchangeRate;
    }

    @Override
    public ExchangeRate getLatestRates(String baseCurrency) {
        return currencyExchangeRate.getLatestRates(baseCurrency);
    }

    @Override
    public ExchangeRate getExchangeRateForPair(String baseCurrency, String targetCurrency) {
        return currencyExchangeRate.getExchangeRateForPair(baseCurrency, targetCurrency);
    }
}
