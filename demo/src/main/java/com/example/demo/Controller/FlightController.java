package com.example.demo.Controller;

import com.example.demo.FlightProfile.FlightProfile;
import com.example.demo.Model.Flight;
import com.example.demo.Service.FlightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")
@RestController
public class FlightController {

    public static String UPLOAD_DIRECTORY = "/home/gleb/IdeaProjects/FlightManager.server/API/FlightManageAPI/demo/src/main/resources/static";
    @Autowired
    private FlightsService service;

    @PostMapping(
            path = "/flights/add",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addFlight(@RequestParam("file") MultipartFile file,
                                    @RequestParam("departure") String departure,
                                    @RequestParam("destination") String destination) throws IOException {
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        if (service.saveFlight(new Flight(departure, destination, file.getOriginalFilename()))){
            return new ResponseEntity("New node added", HttpStatus.CREATED);
        }
        return new ResponseEntity("Such node is already exist", HttpStatus.CONFLICT);
    }

    @GetMapping("/flights/get")
    public ResponseEntity<Resource> getFlights(@RequestParam(name = "departure", required = false) String departure,
                                               @RequestParam(name = "destination", required = false) String destination) throws IOException {
        List<Flight> lst = new ArrayList<>();
        List<FlightProfile> flights = new ArrayList<>();
        lst = service.getFlights(departure, destination);
        for(Flight fl : lst){
            File file = new File(UPLOAD_DIRECTORY + "/" + fl.getFilePath());
            FileInputStream str = new FileInputStream(file);
            byte[] arr = new byte[(int)file.length()];
            str.read(arr);
            str.close();
            flights.add(new FlightProfile(fl, arr));
        }
        return new ResponseEntity(flights, HttpStatus.OK);
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
