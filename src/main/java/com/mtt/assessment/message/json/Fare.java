package com.mtt.assessment.message.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public final class Fare {

    private final Amount ticket;

    private final Amount bookingFee;

    private final Amount tax;

    @JsonCreator
    public Fare(@JsonProperty("ticket") Amount ticket, @JsonProperty("bookingFee") Amount bookingFee, @JsonProperty("tax") Amount tax) {
        this.ticket = ticket;
        this.bookingFee = bookingFee;
        this.tax = tax;
    }

    public Amount getTicket() { return ticket; }

    public Amount getBookingFee() { return bookingFee; }

    public Amount getTax() { return tax; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fare fare = (Fare) o;

        if (!ticket.equals(fare.ticket)) return false;
        if (!bookingFee.equals(fare.bookingFee)) return false;
        return tax.equals(fare.tax);

    }

    @Override
    public int hashCode() {
        int result = ticket.hashCode();
        result = 31 * result + bookingFee.hashCode();
        result = 31 * result + tax.hashCode();
        return result;
    }
}
