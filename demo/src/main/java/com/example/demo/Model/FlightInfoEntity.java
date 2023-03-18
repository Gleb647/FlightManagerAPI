package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "flightInfo")
public class FlightInfoEntity {
    @Id
    @SequenceGenerator(name = "flightInfo_sequence", sequenceName = "flightInfo_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flightInfo_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "carrier", nullable = false, columnDefinition = "TEXT")
    private String carrier;

    @Column(name = "flightDuration", nullable = false, columnDefinition = "INTEGER")
    private Integer flightDuration;

    @Column(name = "cost", nullable = false, columnDefinition = "INTEGER")
    private Integer cost;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "date", nullable = false, columnDefinition = "TEXT")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight fl;

    public FlightInfoEntity(){

    }

    public FlightInfoEntity(Long id, String carrier, Integer flightDuration, Integer cost, LocalDateTime utilDate, Flight fl) {
        this.id = id;
        this.carrier = carrier;
        this.flightDuration = flightDuration;
        this.cost = cost;
        this.date = utilDate;
    }

    public FlightInfoEntity(String carrier, Integer flightDuration, Integer cost, LocalDateTime date, Flight fl) {
        this.carrier = carrier;
        this.flightDuration = flightDuration;
        this.cost = cost;
        this.date = date;
        this.fl = fl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public Integer getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(Integer flightDuration) {
        this.flightDuration = flightDuration;
    }

    public Integer getCost() { return cost; }

    public void setCost(Integer cost) { this.cost = cost; }

    public Flight getFlight() {
        return fl;
    }

    public void setFlight(Flight flight) {
        this.fl = flight;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime utilDate) {
        this.date = utilDate;
    }
}
