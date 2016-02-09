package com.mtt.assessment.message.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
public final class Amount {

    private final String currency;
    private final BigDecimal amount;

    @JsonCreator
    private Amount(@JsonProperty("currency") String currency, @JsonProperty("amount") BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public String getCurrency() { return currency; }

    public BigDecimal getAmount() { return amount; }

    public static class Builder {
        private String currencyAndAmount;

        public Builder(String currencyAndAmount) {
            this.currencyAndAmount = currencyAndAmount;
        }

        public Amount build() {
            if (StringUtils.isEmpty(currencyAndAmount)) {
                throw new IllegalArgumentException("Currency and amount value cannot be empty");
            }

            String[] values = StringUtils.split(currencyAndAmount);
            assert(values.length == 2);

            final String currency = values[0];
            final BigDecimal amount = new BigDecimal(values[1]);

            return new Amount(currency, amount);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Amount amount1 = (Amount) o;

        if (currency != null ? !currency.equals(amount1.currency) : amount1.currency != null) return false;
        return !(amount != null ? !amount.equals(amount1.amount) : amount1.amount != null);

    }

    @Override
    public int hashCode() {
        int result = currency != null ? currency.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Amount{" +
                "currency='" + currency + '\'' +
                ", amount=" + amount +
                '}';
    }
}
