package com.mtt.assessment.message.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonRootName(value="availability")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Availability {

    private final List<Flight> flights;

    @JsonCreator
    public Availability(@JsonProperty("flight") final List<Flight> flights) {
        this.flights = flights;
    }

    @JsonProperty("flight")
    public List<Flight> getFlights() { return flights; }

}
