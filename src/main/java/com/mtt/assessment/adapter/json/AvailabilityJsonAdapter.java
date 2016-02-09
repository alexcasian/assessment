package com.mtt.assessment.adapter.json;

import com.mtt.assessment.message.Message;
import com.mtt.assessment.message.json.*;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AvailabilityJsonAdapter implements JsonAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AvailabilityJsonAdapter.class);

    @Override
    public Availability toJson(Message message) {

        StopWatch watch = new StopWatch();
        watch.start();

        if (message == null) {
            logger.error("JAXB message is null");
            throw new NullPointerException("XML response message not provided.");
        }

        com.mtt.assessment.message.jxb.Availability jaxbSource = (com.mtt.assessment.message.jxb.Availability) message;

        List<com.mtt.assessment.message.jxb.Availability.Flight> jaxbFlights = jaxbSource.getFlight();
        List<Flight> jsonFlights = new ArrayList<>(jaxbFlights.size());
        for (com.mtt.assessment.message.jxb.Availability.Flight jaxbFlight : jaxbFlights) {

            FarePrices.Builder farePricesBuilder = new FarePrices.Builder();
            com.mtt.assessment.message.jxb.Availability.Flight.Fares jaxbFares = jaxbFlight.getFares();
            for (com.mtt.assessment.message.jxb.Availability.Flight.Fares.Fare jaxbFare : jaxbFares.getFare()) {
                farePricesBuilder.withFare(FarePricesEnum.valueOf(jaxbFare.getClazz()), new Fare(
                        new Amount.Builder(jaxbFare.getBasePrice()).build(),
                        new Amount.Builder(jaxbFare.getFees()).build(),
                        new Amount.Builder(jaxbFare.getTax()).build()));
            }

            Flight jsonFlight = new Flight.Builder(jaxbFlight.getCarrierCode())
                    .andFlightNumber(jaxbFlight.getFlightDesignator())
                    .andDepartsFrom(jaxbFlight.getOriginAirport())
                    .andArrivesAt(jaxbFlight.getDestinationAirport())
                    .andDepartsOn(jaxbFlight.getDepartureDate())
                    .andArrivesOn(jaxbFlight.getArrivalDate())
                    .withFarePrices(farePricesBuilder.build())
                    .build();

            jsonFlights.add(jsonFlight);
        }

        watch.stop();
        logger.info("Finished adapting the JAXB to JSON interface in " + watch.getTime() + " ms");

        return new Availability(jsonFlights);
    }
}
