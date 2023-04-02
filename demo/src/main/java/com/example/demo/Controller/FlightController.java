package com.example.demo.Controller;

import com.example.demo.Logger.CustomLogger;
import com.example.demo.Service.FlightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@CrossOrigin("*")
@RestController
public class FlightController {
    @Autowired
    private FlightsService service;

    @PostMapping(
            path = "/flights/add",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addFlight(@RequestParam("file") MultipartFile file,
                                    @RequestParam("departure") String departure,
                                    @RequestParam("destination") String destination){
        if (service.saveFlight(departure, destination, file)){
            CustomLogger.info("{}: flight added", this.getClass().getName());
            return new ResponseEntity("New node added", HttpStatus.CREATED);
        }
        CustomLogger.warn("{}: attempt to add existing flight", this.getClass().getName());
        return new ResponseEntity("Such node is already exist", HttpStatus.CONFLICT);
    }

    @GetMapping("/flights/get")
    public ResponseEntity<Resource> getFlights(@RequestParam(name = "departure", required = false) String departure,
                                               @RequestParam(name = "destination", required = false) String destination){
        return new ResponseEntity(service.convertFlights(departure, destination), HttpStatus.OK);
    }

    @DeleteMapping("/flights/delete/{id}")
    public ResponseEntity deleteFlight(@PathVariable("id") Long id) throws IOException {
        service.deleteFlight(id);
        CustomLogger.info("{}: flight {} deleted", this.getClass().getName(), String.valueOf(id));
        return new ResponseEntity(service.convertFlights(null,null), HttpStatus.OK);
    }

    @PutMapping(path = "/flights/change/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateFlight(@PathVariable("id") Long id,
                                       @RequestParam("file") MultipartFile file,
                                       @RequestParam("departure") String departure,
                                       @RequestParam("destination") String destination){
        if (service.updateFlight(id, departure, destination, file)){
            CustomLogger.info("{}: flight {} updated", this.getClass().getName(), String.valueOf(id));
            return new ResponseEntity("Node updated", HttpStatus.OK);
        }
        CustomLogger.warn("{}: attempt to update flight {} with already existing data",
                this.getClass().getName(), String.valueOf(id));
        return new ResponseEntity("Such node is already exist", HttpStatus.CONFLICT);
    }

}
