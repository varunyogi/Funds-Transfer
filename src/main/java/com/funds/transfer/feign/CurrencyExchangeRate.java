package com.funds.transfer.feign;

import com.funds.transfer.model.ExchangeRate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "exchangeRateClient", url = "https://v6.exchangerate-api.com/v6/fb189402c9fe4913aadba199")
public interface CurrencyExchangeRate {

    @GetMapping("/latest/{baseCurrency}")
    ExchangeRate getLatestRates(@PathVariable("baseCurrency") String baseCurrency);

    @GetMapping("/pair/{baseCurrency}/{targetCurrency}")
    ExchangeRate getExchangeRateForPair(@PathVariable("baseCurrency") String baseCurrency,
                                        @PathVariable("targetCurrency") String targetCurrency);
}