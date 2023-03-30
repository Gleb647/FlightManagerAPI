package com.example.demo.Repository;

import com.example.demo.FlightsApplication;
import com.example.demo.Model.Flight;
import com.example.demo.Model.FlightInfoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = {FlightsApplication.class})
class FlightInfoRepositoryTest {

    @Autowired
    public FlightsRepository baseRepo;

    @Autowired
    public FlightInfoRepository infoRepo;


    @BeforeEach
    public void setup(){
        Flight flight1 = new Flight("minsk", "istanbul", null);
        Flight flight2 = new Flight("dublin", "paris", null);

        FlightInfoEntity flightInfo1 = new FlightInfoEntity("turkish airlines", 2, 100,
                LocalDateTime.now(), flight1);
        FlightInfoEntity flightInfo2 = new FlightInfoEntity("american airlines", 11, 500,
                LocalDateTime.now(), flight1);
        FlightInfoEntity flightInfo3 = new FlightInfoEntity("ireland airlines", 5, 300,
                LocalDateTime.now(), flight1);
        FlightInfoEntity flightInfo4 = new FlightInfoEntity("pekin airlines", 3, 200,
                LocalDateTime.now(), flight2);
        baseRepo.save(flight1);
        baseRepo.save(flight2);
        infoRepo.save(flightInfo1);
        infoRepo.save(flightInfo2);
        infoRepo.save(flightInfo3);
        infoRepo.save(flightInfo4);
    }

    @Test
    void findAllExpNotesTest() {
        assertEquals(infoRepo.findAllExpNotes(1L).size(), 3);
        assertEquals(infoRepo.findAllExpNotes(2L).size(), 1);
    }

    @Test
    public void findAllBetweenTest() {
        assertEquals(infoRepo.findAllFlightsBetween(1L,300, 500).size(), 2);
        assertEquals(infoRepo.findAllFlightsBetween(1L,100, 0).size(), 0);
        assertEquals(infoRepo.findAllFlightsBetween(1L,500, 501).size(), 1);
        assertEquals(infoRepo.findAllFlightsBetween(1L,100, 500).size(), 3);
    }

    @Test
    public void findAllFlightsAboveTest() {
        assertEquals(infoRepo.findAllFlightsAbove(1L,300).size(), 2);
        assertEquals(infoRepo.findAllFlightsAbove(1L,100).size(), 3);
        assertEquals(infoRepo.findAllFlightsAbove(1L,501).size(), 0);
        assertEquals(infoRepo.findAllFlightsAbove(1L,500).size(), 1);
    }

    @Test
    public void findAllFlightsBelowTest() {
        assertEquals(infoRepo.findAllFlightsBelow(1L,300).size(), 2);
        assertEquals(infoRepo.findAllFlightsBelow(1L,100).size(), 1);
        assertEquals(infoRepo.findAllFlightsBelow(1L,501).size(), 3);
        assertEquals(infoRepo.findAllFlightsBelow(1L,500).size(), 3);
    }

    @Test
    void deleteAllFlightInfoByFlightIdTest() {
        Flight flight1 = new Flight("paris", "madrid", null);
        Flight flight2 = new Flight("erevan", "moscow", null);

        FlightInfoEntity flightInfo1 = new FlightInfoEntity("france airlines", 2, 100,
                LocalDateTime.now(), flight1);
        FlightInfoEntity flightInfo2 = new FlightInfoEntity("spain airlines", 11, 500,
                LocalDateTime.now(), flight1);
        FlightInfoEntity flightInfo3 = new FlightInfoEntity("tegeran airlines", 5, 300,
                LocalDateTime.now(), flight1);
        FlightInfoEntity flightInfo4 = new FlightInfoEntity("moscow airlines", 3, 200,
                LocalDateTime.now(), flight2);
        baseRepo.save(flight1);
        baseRepo.save(flight2);
        infoRepo.save(flightInfo1);
        infoRepo.save(flightInfo2);
        infoRepo.save(flightInfo3);
        infoRepo.save(flightInfo4);

        infoRepo.deleteAllFlightInfoByFlightId(4L);
        assertEquals(infoRepo.findAllExpNotes(4L).size(), 0);
        assertEquals(baseRepo.findAll().size(), 4);
        baseRepo.deleteById(4L);
        assertEquals(baseRepo.findAll().size(), 3);
    }

}