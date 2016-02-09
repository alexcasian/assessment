package com.mtt.assessment.message.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.HashMap;
import java.util.Map;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
public final class FarePrices {

    private final Map<FarePricesEnum, Fare> fares;

    @JsonCreator
    private FarePrices(Map<FarePricesEnum, Fare> fares) {
        this.fares = fares;
    }

    private FarePrices(Builder builder) {
        fares = builder.fares;
    }

    public Fare getFirst() { return fares.get(FarePricesEnum.FIF); }

    public Fare getBusiness() { return fares.get(FarePricesEnum.CIF); }

    public Fare getEconomy() { return fares.get(FarePricesEnum.YIF); }

    public final static class Builder {
        private final Map<FarePricesEnum, Fare> fares = new HashMap<>(3);

        public Builder withFare(FarePricesEnum fareType, final Fare fare) {
            fares.put(fareType, fare);
            return this;
        }

        public FarePrices build() {
            return new FarePrices(this);
        }
    }
}
