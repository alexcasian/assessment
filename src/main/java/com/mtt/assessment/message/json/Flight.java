package com.mtt.assessment.message.json;

import com.fasterxml.jackson.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;

@JsonPropertyOrder(value={"operator", "flightNumber", "departsFrom", "arrivesAt", "departsOn", "arrivesOn", "flightTime", "farePrices"})
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Flight {

    private final String operator;

    private final String flightNumber;

    private final String departsFrom;

    private final String arrivesAt;

    private final LocalDateTime departsOn;

    private final LocalDateTime arrivesOn;

    private final FarePrices farePrices;

    @JsonCreator
    private Flight(@JsonProperty("operator") String operator, @JsonProperty("flightNumber") String flightNumber, @JsonProperty("departsFrom") String departsFrom,
                   @JsonProperty("arrivesAt") String arrivesAt, @JsonProperty("departsOn") LocalDateTimeDecorator departsOn,
                   @JsonProperty("arrivesOn") LocalDateTimeDecorator arrivesOn, @JsonProperty("farePrices") FarePrices farePrices) {
        this.operator = operator;
        this.flightNumber = flightNumber;
        this.departsFrom = departsFrom;
        this.arrivesAt = arrivesAt;
        this.departsOn = departsOn.getLocalDateTime();
        this.arrivesOn = arrivesOn.getLocalDateTime();
        this.farePrices = farePrices;
    }

    private Flight(final Builder builder) {
        operator = builder.operator;
        flightNumber = builder.flightNumber;
        departsFrom = builder.departsFrom;
        arrivesAt = builder.arrivesAt;
        departsOn = builder.departsOn;
        arrivesOn = builder.arrivesOn;
        farePrices = builder.farePrices;
    }

    public String getOperator() {
        return operator;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getDepartsFrom() {
        return departsFrom;
    }

    public String getArrivesAt() {
        return arrivesAt;
    }

    public LocalDateTimeDecorator getDepartsOn() {
        return new LocalDateTimeDecorator(departsOn);
    }

    public LocalDateTimeDecorator getArrivesOn() {return new LocalDateTimeDecorator(arrivesOn); }

    public String getFlightTime() {
        Duration duration = Duration.between(departsOn, arrivesOn);
        return String.format("%d:%02d", duration.toHours(), (duration.toMinutes() - duration.toHours() * 60));
    }

    public FarePrices getFarePrices() {
        return farePrices;
    }

    public final static class Builder {

        private String operator;

        private String flightNumber;

        private String departsFrom;

        private String arrivesAt;

        private LocalDateTime departsOn;

        private LocalDateTime arrivesOn;

        private FarePrices farePrices;

        public Builder(String operator) {
            this.operator = operator;
        }

        public Builder andFlightNumber(final String flightNumber) {
            this.flightNumber = flightNumber;
            return this;
        }

        public Builder andDepartsFrom(final String departsFrom) {
            this.departsFrom = departsFrom;
            return this;
        }

        public Builder andArrivesAt(final String arrivesAt) {
            this.arrivesAt = arrivesAt;
            return this;
        }

        public Builder andDepartsOn(final LocalDateTime departsOn) {
            this.departsOn = departsOn;
            return this;
        }

        public Builder andArrivesOn(final LocalDateTime arrivesOn) {
            this.arrivesOn = arrivesOn;
            return this;
        }

        public Builder withFarePrices(final FarePrices farePrices) {
            this.farePrices = farePrices;
            return this;
        }

        public Flight build() {
            return new Flight(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flight flight = (Flight) o;

        if (operator != null ? !operator.equals(flight.operator) : flight.operator != null) return false;
        if (flightNumber != null ? !flightNumber.equals(flight.flightNumber) : flight.flightNumber != null)
            return false;
        if (departsFrom != null ? !departsFrom.equals(flight.departsFrom) : flight.departsFrom != null) return false;
        if (arrivesAt != null ? !arrivesAt.equals(flight.arrivesAt) : flight.arrivesAt != null) return false;
        if (departsOn != null ? !departsOn.equals(flight.departsOn) : flight.departsOn != null) return false;
        if (arrivesOn != null ? !arrivesOn.equals(flight.arrivesOn) : flight.arrivesOn != null) return false;
        return !(farePrices != null ? !farePrices.equals(flight.farePrices) : flight.farePrices != null);

    }

    @Override
    public int hashCode() {
        int result = operator != null ? operator.hashCode() : 0;
        result = 31 * result + (flightNumber != null ? flightNumber.hashCode() : 0);
        result = 31 * result + (departsFrom != null ? departsFrom.hashCode() : 0);
        result = 31 * result + (arrivesAt != null ? arrivesAt.hashCode() : 0);
        result = 31 * result + (departsOn != null ? departsOn.hashCode() : 0);
        result = 31 * result + (arrivesOn != null ? arrivesOn.hashCode() : 0);
        result = 31 * result + (farePrices != null ? farePrices.hashCode() : 0);
        return result;
    }
}
