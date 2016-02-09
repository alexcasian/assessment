package com.mtt.assessment.message.json;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum FarePricesEnum {
    FIF("first"),
    CIF("business"),
    YIF("economy");

    private String fareDescription;

    FarePricesEnum(String fareDescription) {
        this.fareDescription = fareDescription;
    }

    public String getFareDescription() {
        return fareDescription;
    }

    @JsonCreator
    public static FarePricesEnum fromFareDescription(String fareDescription) {
        FarePricesEnum result = null;
        for (FarePricesEnum value : FarePricesEnum.values()) {
            if (value.getFareDescription().equals(fareDescription)) {
                result = value;
            }
        }

        if (result == null) {
            throw new IllegalArgumentException("Invalid fare price description provided");
        }

        return result;
    }

}
