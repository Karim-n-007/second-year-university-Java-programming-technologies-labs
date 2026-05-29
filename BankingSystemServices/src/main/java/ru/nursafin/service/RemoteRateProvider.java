package ru.nursafin.service;

import ru.nursafin.model.CurrencyRate;

import java.util.Optional;

public interface RemoteRateProvider {
    Optional<CurrencyRate> fetchRate(String currency);
}
