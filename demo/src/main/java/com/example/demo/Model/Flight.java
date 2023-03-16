package com.example.demo.Model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "flight")
public class Flight{
    @Id
    @SequenceGenerator(name = "flight_sequence", sequenceName = "flight_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flight_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "departure", nullable = false, columnDefinition = "TEXT")
    private String departure;

    @Column(name = "destination", nullable = false, columnDefinition = "TEXT")
    private String destination;

    @Column(name = "flights_available")
    private Integer flightsAvailable = 0;

    @OneToMany(mappedBy = "fl")
    private List<FlightInfoEntity> info;

    public Flight(String departure, String destination) {
        this.departure = departure;
        this.destination = destination;
        this.flightsAvailable = 0;
    }

    public Flight(Long id, String departure, String destination) {
        this.id = id;
        this.departure = departure;
        this.destination = destination;
        this.flightsAvailable = 0;
    }

    public Flight() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getFlightsAvailable() {
        return flightsAvailable;
    }

    public void setFlightsAvailable(Integer flightsAvailable) {
        this.flightsAvailable = flightsAvailable;
    }
//
//    public List<FlightInfoEntity> getInfo() {
//        return info;
//    }
//
//    public void setInfo(List<FlightInfoEntity> info) {
//        this.info = info;
//    }

    public synchronized void increaseFlightsAvailableCount(){
        this.flightsAvailable++;
    }

    public synchronized void decreaseFlightsAvailableCount(){
        this.flightsAvailable--;
    }


    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", departure='" + departure + '\'' +
                ", destination='" + destination + '\'' +
                ", flightsAvailable=" + flightsAvailable +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return Objects.equals(id, flight.id) && Objects.equals(departure, flight.departure) && Objects.equals(destination, flight.destination) && Objects.equals(flightsAvailable, flight.flightsAvailable) && Objects.equals(info, flight.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, departure, destination, flightsAvailable, info);
    }
}
