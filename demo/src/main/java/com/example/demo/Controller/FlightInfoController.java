package com.example.demo.Controller;

import com.example.demo.Repository.FlightInfoRepository;
import com.example.demo.Repository.FlightsRepository;
import com.example.demo.Model.FlightInfoEntity;
import com.example.demo.Service.FlightInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin("*")
@RestController
public class FlightInfoController {
    @Autowired
    FlightInfoRepository repository;

    @Autowired
    FlightsRepository flights_rep;

    @Autowired
    FlightInfoService service;

    @PostMapping("/flightinfo/{id}")
    public ResponseEntity addUserInfo(@PathVariable("id") Long id, @RequestBody FlightInfoEntity flightInfo){
        try{
            flights_rep.findById(id).get().increaseFlightsAvailableCount();
            return new ResponseEntity(service.addFlightInfo(flightInfo, id), HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity("Invalid data provided", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/flightinfo/get/{id}")
    public ResponseEntity getUserInfo(@PathVariable("id") Long id){
        try{
            service.checkIfFlightNotExpired(id);
            List<FlightInfoEntity> lst = service.findAllExpNotes(id);
            return new ResponseEntity(lst, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity("Invalid data provided", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("flightinfo/get-flight-info-between/{id}")
    public ResponseEntity getFlightBetween(@PathVariable("id") Long id,
                                           @RequestParam(name = "min", required = false) String min,
                                           @RequestParam(name = "max", required = false) String max){
        try{
            List<FlightInfoEntity> lst;
            if (min != null && max != null){
                lst = repository.findAllFlightsBetween(id, Integer.parseInt(min), Integer.parseInt(max));
            }
            else if (min != null){
                lst = repository.findAllFlightsAbove(id, Integer.parseInt(min));
            }
            else {
                lst =  repository.findAllFlightsBelow(id, Integer.parseInt(max));
            }
            return new ResponseEntity(lst, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity("Invalid data provided", HttpStatus.BAD_REQUEST);
        }
    }
}
