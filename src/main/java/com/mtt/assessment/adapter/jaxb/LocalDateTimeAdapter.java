package com.mtt.assessment.adapter.jaxb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeAdapter {

    private static final Logger logger = LoggerFactory.getLogger(LocalDateTimeAdapter.class);

    public static LocalDateTime unmarshal(String xmlGregorianCalendar) {
        try {
            LocalDateTime result = LocalDateTime.parse(xmlGregorianCalendar, DateTimeFormatter.ISO_DATE_TIME);
            return result;
        } catch (DateTimeParseException ex) {
            logger.error("Could not parse date" + xmlGregorianCalendar, ex);
            return null;
        }
    }

    public static String marshal(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
