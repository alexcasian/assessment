package com.mtt.assessment.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.mtt.assessment.controller.FlightAvailabilityController.ORIGIN_AIRPORT;
import static com.mtt.assessment.controller.FlightAvailabilityController.DEPARTURE_DATE;
import static com.mtt.assessment.controller.FlightAvailabilityController.DESTINATION_AIRPORT;
import static com.mtt.assessment.controller.FlightAvailabilityController.RETURN_DATE;
import static com.mtt.assessment.controller.FlightAvailabilityController.NUMBER_OF_PASSENGERS;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.mtt.assessment.adapter.json.AvailabilityJsonAdapter;
import com.mtt.assessment.adapter.json.JsonAdapter;
import com.mtt.assessment.message.Message;
import com.mtt.assessment.message.json.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtt.assessment.Application;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebAppConfiguration
public class FlightAvailabilityControllerTest {

	@Value("${mockairline.endpoint.url}")
	private String endpointUrl;

	private MockMvc mockMvc;

    @Before
    public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(new FlightAvailabilityController(endpointUrl, new AvailabilityJsonAdapter())).build();
    }

	@Test
	public void testDemoEndpoint() throws Exception {

		mockMvc.perform(get("/demo")
                .param(ORIGIN_AIRPORT, "DUB")
                .param(DESTINATION_AIRPORT, "DEL")
                .param(DEPARTURE_DATE, "20151103")
                .param(RETURN_DATE, "20151125")
                .param(NUMBER_OF_PASSENGERS, "4")
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
	}

    @Test
    public void testDemoWithBadEndpointUrl() throws Exception {

        mockMvc = MockMvcBuilders.standaloneSetup(new FlightAvailabilityController("", new AvailabilityJsonAdapter())).build();
        mockMvc.perform(get("/demo").accept(MediaType.ALL))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType("application/json")).andReturn();
    }

    @Test
    public void testDemoWithBadAdapter() throws Exception {

        mockMvc = MockMvcBuilders.standaloneSetup(new FlightAvailabilityController(endpointUrl, new JsonAdapter() {
            @Override
            public  Object toJson(Message message) {
                return new Object();
            }
        })).build();

        mockMvc.perform(get("/demo").accept(MediaType.ALL))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType("application/json")).andReturn();
    }

    @Test
    public void testDemoConversion() throws Exception {

		MvcResult result = mockMvc.perform(get("/demo").accept(MediaType.ALL)).andReturn();

		Availability availability = toJson(result.getResponse().getContentAsString(), Availability.class);

		assertNotNull("Availability not set", availability);
    	assertFalse("No flights are available", availability.getFlights().isEmpty());

        Flight flight = availability.getFlights().get(0);
        assertNotNull("Operator not set ", flight.getOperator());
        assertNotNull("Flight number not set ", flight.getFlightNumber());
        assertNotNull("Departs from not set ", flight.getDepartsFrom());
        assertNotNull("Arrives at not set ", flight.getArrivesAt());

        assertNotNull("Departs on not set ", flight.getDepartsOn());
        LocalDate departsOnDate = flight.getDepartsOn().getDate();
        LocalTime departsOnTime = flight.getDepartsOn().getTime();
        assertNotNull("Departs on date not set ", departsOnDate);
        assertNotNull("Departs on time not set ", departsOnTime);
        assertEquals("Data mismatch between integral date and separate views", flight.getDepartsOn().getLocalDateTime(), LocalDateTime.of(departsOnDate, departsOnTime));

        assertNotNull("Arrives on not set ", flight.getArrivesOn());
        LocalDate arrivesOnDate = flight.getArrivesOn().getDate();
        LocalTime arrivesOnTime = flight.getArrivesOn().getTime();
        assertNotNull("Arrives on date not set ", arrivesOnDate);
        assertNotNull("Arrives on time not set ", arrivesOnTime);
        assertEquals("Data mismatch between integral date and separate views", flight.getArrivesOn().getLocalDateTime(), LocalDateTime.of(arrivesOnDate, arrivesOnTime));

        assertNotNull("Flight time not set ", flight.getFlightTime());
        Duration duration = Duration.between(flight.getDepartsOn().getLocalDateTime(), flight.getArrivesOn().getLocalDateTime());
        assertEquals("", flight.getFlightTime(), String.format("%d:%02d", duration.toHours(), (duration.toMinutes() - duration.toHours() * 60)));

        FarePrices farePrices = flight.getFarePrices();
        assertNotNull("Fare prices not set ", farePrices);
        assertNotNull("First class price not set ", farePrices.getFirst());
        assertNotNull("First class booking fee not set ", farePrices.getFirst().getBookingFee());
        assertNotNull("First class booking fee amount not set ", farePrices.getFirst().getBookingFee().getAmount());
        assertNotNull("First class booking fee currency  not set ", farePrices.getFirst().getBookingFee().getCurrency());
        assertNotNull("First class tax not set ", farePrices.getFirst().getTax());
        assertNotNull("First class tax amount not set ", farePrices.getFirst().getTax().getAmount());
        assertNotNull("First class tax currency not set ", farePrices.getFirst().getTax().getCurrency());
        assertNotNull("First class ticket not set ", farePrices.getFirst().getTicket());
        assertNotNull("First class ticket amount not set ", farePrices.getFirst().getTicket().getAmount());
        assertNotNull("First class ticket currency not set ", farePrices.getFirst().getTicket().getCurrency());

        assertNotNull("Business class price not set ", farePrices.getBusiness());
        assertNotNull("Business class booking fee not set ", farePrices.getBusiness().getBookingFee());
        assertNotNull("Business class booking fee amount not set ", farePrices.getBusiness().getBookingFee().getAmount());
        assertNotNull("Business class booking fee currency not set ", farePrices.getBusiness().getBookingFee().getCurrency());
        assertNotNull("Business class tax not set ", farePrices.getBusiness().getTax());
        assertNotNull("Business class tax amount not set ", farePrices.getBusiness().getTax().getAmount());
        assertNotNull("Business class tax currency not set ", farePrices.getBusiness().getTax().getCurrency());
        assertNotNull("Business class ticket not set ", farePrices.getBusiness().getTicket());
        assertNotNull("Business class ticket amount not set ", farePrices.getBusiness().getTicket().getAmount());
        assertNotNull("Business class ticket currency not set ", farePrices.getBusiness().getTicket().getCurrency());

        assertNotNull("Economy class price not set ", farePrices.getEconomy());
        assertNotNull("Economy class booking fee not set ", farePrices.getEconomy().getBookingFee());
        assertNotNull("Economy class booking fee amount not set ", farePrices.getEconomy().getBookingFee().getAmount());
        assertNotNull("Economy class booking fee currency not set ", farePrices.getEconomy().getBookingFee().getCurrency());
        assertNotNull("Economy class tax not set ", farePrices.getEconomy().getTax());
        assertNotNull("Economy class tax amount not set ", farePrices.getEconomy().getTax().getAmount());
        assertNotNull("Economy class tax currency not set ", farePrices.getEconomy().getTax().getCurrency());
        assertNotNull("Economy class ticket not set ", farePrices.getEconomy().getTicket());
        assertNotNull("Economy class ticket amount not set ", farePrices.getEconomy().getTicket().getAmount());
        assertNotNull("Economy class ticket currency not set ", farePrices.getEconomy().getTicket().getCurrency());
    }

    private <T> T toJson(String src, Class<T> clazz) {
    	ObjectMapper jsonMapper = new ObjectMapper();
    	try {
    		return jsonMapper.readValue(src, clazz);
    	} catch (IOException e) {
    		throw new IllegalArgumentException("Failed to convert to json object ", e);
    	}
    }

}
