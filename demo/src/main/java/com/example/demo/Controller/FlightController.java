package com.example.demo.Controller;

import com.example.demo.FlightProfile.FlightProfile;
import com.example.demo.Logger.CustomLogger;
import com.example.demo.Service.FlightConverterUtils;
import com.example.demo.Service.FlightConverterUtilsImpl;
import com.example.demo.Service.FlightsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin("*")
@RestController
public class FlightController {
    @Autowired
    private FlightsServiceImpl service;

    @Autowired
    private FlightConverterUtilsImpl flightConverterUtilsImpl;

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
    public ResponseEntity<List<FlightProfile>> getFlights(
                @RequestParam(name = "departure", required = false) String departure,
                @RequestParam(name = "destination", required = false) String destination,
                @RequestParam(name = "pageNum", required = false) Integer pageNum,
                @RequestParam(name = "pageSize", required = false) Integer pageSize){
        Pageable paging = PageRequest.of(pageNum, pageSize);
        return new ResponseEntity<>(flightConverterUtilsImpl.convertFlights(departure, destination, paging), HttpStatus.OK);
    }

    @DeleteMapping("/flights/delete/{id}")
    public ResponseEntity deleteFlight(@PathVariable("id") Long id){
        service.deleteFlight(id);
        CustomLogger.info("{}: flight {} deleted", this.getClass().getName(), String.valueOf(id));
        return new ResponseEntity(
                flightConverterUtilsImpl.convertFlights(null,null, null), HttpStatus.OK);
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
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        CustomLogger.warn("{}: attempt to update flight {} with already existing data",
                this.getClass().getName(), String.valueOf(id));
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

}
