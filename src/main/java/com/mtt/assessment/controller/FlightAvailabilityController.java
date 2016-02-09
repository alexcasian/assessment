package com.mtt.assessment.controller;

import com.mtt.assessment.adapter.json.JsonAdapter;
import com.mtt.assessment.message.json.Availability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mtt.assessment.client.RestClient;
import com.mtt.assessment.client.spring.RestTemplateClient;

@RestController
public class FlightAvailabilityController {
	
	public static final String ORIGIN_AIRPORT = "originAirport";
    public static final String DESTINATION_AIRPORT = "destinationAirport";
    public static final String DEPARTURE_DATE = "departureDate";
    public static final String RETURN_DATE= "returnDate";
    public static final String NUMBER_OF_PASSENGERS = "numberOfPassengers";
	
	private String endpointUrl;
	private JsonAdapter jsonAdapter;

	@Autowired
	public FlightAvailabilityController(@Value("${mockairline.endpoint.url}") String endpointUrl, JsonAdapter jsonAdapter) {
		this.endpointUrl = endpointUrl;
		this.jsonAdapter = jsonAdapter;
	}
	
    @RequestMapping(value = "/demo", produces = "application/json")
    public Availability demo(
    		@RequestParam(value=ORIGIN_AIRPORT, defaultValue="DUB") String originAirport, 
    		@RequestParam(value=DESTINATION_AIRPORT, defaultValue="DEL") String destinationAirport,
    		@RequestParam(value=DEPARTURE_DATE, defaultValue="20151007") String departureDate,
    		@RequestParam(value=RETURN_DATE, defaultValue="20151020") String returnDate,
    		@RequestParam(value=NUMBER_OF_PASSENGERS, defaultValue="1") Integer numberOfPassengers) {

    	RestClient client = new RestTemplateClient.Builder(endpointUrl)
    	.withParam(ORIGIN_AIRPORT, originAirport)
    	.withParam(DESTINATION_AIRPORT, destinationAirport)
    	.withParam(DEPARTURE_DATE, departureDate)
    	.withParam(RETURN_DATE, returnDate)
    	.withParam(NUMBER_OF_PASSENGERS, numberOfPassengers)
    	.build();

    	return jsonAdapter.toJson(client.performGet(com.mtt.assessment.message.jxb.Availability.class));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        ResponseEntity response = new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return response;
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<String> handleClassCastException(ClassCastException ex) {
        ResponseEntity response = new ResponseEntity(ex.getMessage(), HttpStatus.NOT_IMPLEMENTED);
        return response;
    }


}
