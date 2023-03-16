package com.example.demo.Controller;

import com.example.demo.Model.Flight;
import com.example.demo.Service.FlightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
public class FlightController {
    @Autowired
    private FlightsService service;

    @PostMapping("/flights/add")
    public ResponseEntity addFlight(@RequestBody Flight flight) {
        if (service.saveFlight(flight)){
            return new ResponseEntity("New node added", HttpStatus.CREATED);
        }
        return new ResponseEntity("Such node is already exist", HttpStatus.CONFLICT);
    }

    @GetMapping("/flights/get")
    public ResponseEntity getFlights(@RequestParam(name = "departure", required = false) String departure,
                                   @RequestParam(name = "destination", required = false) String destination){
        return new ResponseEntity(service.getFlights(departure, destination), HttpStatus.OK);
    }

    @DeleteMapping("/flights/delete/{id}")
    public void deleteFlight(@PathVariable("id") Long id){
        service.deleteFlight(id);
    }

    @PutMapping("/flights/change/{id}")
    public ResponseEntity updateFlight(@PathVariable("id") Long id, @RequestBody Flight flight){
        if (service.updateFlight(id, flight)){
            return new ResponseEntity("Node updated", HttpStatus.OK);
        }
        return new ResponseEntity("Such node is already exist", HttpStatus.CONFLICT);
    }

}
