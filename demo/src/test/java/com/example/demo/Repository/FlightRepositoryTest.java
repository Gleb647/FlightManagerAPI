package com.example.demo.Repository;

import com.example.demo.FlightsApplication;
import com.example.demo.Model.Flight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {FlightsApplication.class})
public class FlightRepositoryTest {

    @Autowired
    FlightsRepository flightsRepository;

    @BeforeEach
    void setup(){
        Flight flight = new Flight(1L,"krakow", "london");
        flightsRepository.save(flight);
    }

    @Test
    void checkIfNodeExistTestIfNodeExist(){
        Flight flight = new Flight(1L,"krakow", "london");
        assertFalse(flightsRepository.checkIfNodeExist(flight.getDeparture(), flight.getDestination()).isEmpty());
    }

    @Test
    void checkIfNodeExistTestIfNodeNotExist(){
        Flight flight = new Flight(1L,"krakow", "londn");
        assertTrue(flightsRepository.checkIfNodeExist(flight.getDeparture(), flight.getDestination()).isEmpty());
    }
}
