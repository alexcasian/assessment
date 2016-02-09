package com.mtt.assessment.message.json;

import com.fasterxml.jackson.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
@JsonPropertyOrder(value = {"date", "time"})
@JsonIgnoreProperties(value = "localDateTime")
public class LocalDateTimeDecorator {

    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
    private final LocalDateTime localDateTime;

    @JsonCreator
    private LocalDateTimeDecorator(@JsonProperty("date") String date, @JsonProperty("time") String time) {
        localDateTime = LocalDateTime.of(LocalDate.parse(date, dateFormatter), LocalTime.parse(time, timeFormatter));
    }

    public LocalDateTimeDecorator(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "en")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "dd-MM-yyyy")
    public LocalDate getDate() {
        return localDateTime.toLocalDate();
    }

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "hh:mm a", locale = "en")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "hh:mm a")
    public LocalTime getTime() {
        return localDateTime.toLocalTime();
    }

    public LocalDateTime getLocalDateTime() { return localDateTime; }
}
