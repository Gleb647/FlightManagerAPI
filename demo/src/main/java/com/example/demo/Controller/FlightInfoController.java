package com.example.demo.Controller;

import com.example.demo.Logger.CustomLogger;
import com.example.demo.Model.FlightInfoEntity;
import com.example.demo.Service.FlightInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin("*")
@RestController
public class FlightInfoController {

    @Autowired
    private FlightInfoServiceImpl service;

    @PostMapping("/flightinfo/{id}")
    public ResponseEntity addFlightInfo(@PathVariable("id") Long id, @RequestBody FlightInfoEntity flightInfo){
        if (service.addFlightInfo(flightInfo, id) != null){
            CustomLogger.info("{}: flight info added", this.getClass().getName());
            return new ResponseEntity("New node added", HttpStatus.CREATED);
        }
        CustomLogger.warn("{}: flight already exist", this.getClass().getName());
        return new ResponseEntity("Invalid data provided", HttpStatus.CONFLICT);
    }

    @GetMapping("/flightinfo/get/{id}")
    public ResponseEntity getFlightInfo(@PathVariable("id") Long id,
                                        @RequestParam(name = "pageNum", required = false) Integer pageNum,
                                        @RequestParam(name = "pageSize", required = false) Integer pageSize){
        Pageable paging = PageRequest.of(pageNum, pageSize);
        Page<FlightInfoEntity> lst = service.findAllExpNotes(id, paging);
        for (FlightInfoEntity node : lst){
            service.checkIfFlightNotExpired(node.getId());
        }
        return new ResponseEntity(service.findAllExpNotes(id, paging), HttpStatus.OK);
    }

    @GetMapping("/flightinfo/get-flight-info-between/{id}")
    public ResponseEntity getFlightBetween(@PathVariable("id") Long id,
                                           @RequestParam(name = "min", required = false) String min,
                                           @RequestParam(name = "max", required = false) String max,
                                           @RequestParam(name = "pageNum", required = false) Integer pageNum,
                                           @RequestParam(name = "pageSize", required = false) Integer pageSize){
        Pageable paging = PageRequest.of(pageNum, pageSize);
        try{
            return new ResponseEntity(service.findFlightInfoBetween(id, min, max, paging), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity("Invalid data provided", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/flightinfo/delete/{id}")
    public void deleteFlightInfo(@PathVariable("id") Long id){
        CustomLogger.info("{}: flight info {} deleted", this.getClass().getName(), String.valueOf(id));
        service.deleteFlightInfo(id);
    }

    @PutMapping("/flightinfo/change/{id}")
    public ResponseEntity updateFlightInfo(@PathVariable("id") Long id, @RequestBody FlightInfoEntity info){
        if (service.updateFlight(id, info)){
            CustomLogger.info("{}: flight {} updated", this.getClass().getName(), String.valueOf(id));
            return new ResponseEntity("Node updated", HttpStatus.OK);
        }
        CustomLogger.warn("{}: attempt to update flight {} with already existing data",
                this.getClass().getName(), String.valueOf(id));
        return new ResponseEntity("Such node is already exist", HttpStatus.CONFLICT);
    }
}
