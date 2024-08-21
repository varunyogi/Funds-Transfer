package com.funds.transfer.service;

import com.funds.transfer.model.ExchangeRate;

public interface ExchangeRateService {

    public ExchangeRate getLatestRates(String baseCurrency);

    public ExchangeRate getExchangeRateForPair(String baseCurrency, String targetCurrency);
}

