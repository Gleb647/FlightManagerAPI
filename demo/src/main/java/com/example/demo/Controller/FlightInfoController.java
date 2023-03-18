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
    public ResponseEntity addFlightInfo(@PathVariable("id") Long id, @RequestBody FlightInfoEntity flightInfo){
        try{
            flights_rep.findById(id).get().increaseFlightsAvailableCount();
            return new ResponseEntity(service.addFlightInfo(flightInfo, id), HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity("Invalid data provided", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/flightinfo/get/{id}")
    public ResponseEntity getFlightInfo(@PathVariable("id") Long id){
        try{
            List<FlightInfoEntity> lst = service.findAllExpNotes(id);
            for (FlightInfoEntity node : lst){
                service.checkIfFlightNotExpired(node.getId());
            }
            return new ResponseEntity(service.findAllExpNotes(id), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity("Invalid data provided", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/flightinfo/get-flight-info-between/{id}")
    public ResponseEntity getFlightBetween(@PathVariable("id") Long id,
                                           @RequestParam(name = "min", required = false) String min,
                                           @RequestParam(name = "max", required = false) String max){
        try{
            return new ResponseEntity(service.findFlightInfoBetween(id, min, max), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity("Invalid data provided", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/flightinfo/delete/{id}")
    public void deleteFlightInfo(@PathVariable("id") Long id){
        flights_rep.findById(repository.findById(id).get().getFlight().getId()).get().decreaseFlightsAvailableCount();
        repository.deleteById(id);
    }

    @PutMapping("/flightinfo/change/{id}")
    public ResponseEntity updateFlightInfo(@PathVariable("id") Long id, @RequestBody FlightInfoEntity info){
        if (service.updateFlight(id, info)){
            return new ResponseEntity("Node updated", HttpStatus.OK);
        }
        return new ResponseEntity("Such node is already exist", HttpStatus.CONFLICT);
    }
}
