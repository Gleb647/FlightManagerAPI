package com.example.demo.Service;

import com.example.demo.FlightProfile.FlightProfile;
import com.example.demo.Model.FlightInfoEntity;
import com.example.demo.Repository.FlightInfoRepository;
import com.example.demo.Repository.FlightsRepository;
import com.example.demo.Model.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.demo.Controller.FlightController.UPLOAD_DIRECTORY;

@Service
@Transactional
public class FlightsService {

    @Autowired
    private FlightsRepository repository;

    @Autowired
    private FlightInfoRepository flight_info_rep;

    public FlightsService(FlightsRepository repository, FlightInfoRepository flight_info_rep) {
        this.repository = repository;
        this.flight_info_rep = flight_info_rep;
    }

    public boolean updateFlight(Long id, String departure, String destination, MultipartFile file) throws IOException {
        Flight checkFlight = repository.findById(id).orElseThrow(() ->
                new IllegalStateException("Flight with id " + id + " doesn't exist"));
        if (repository.checkIfNodeExist(departure, destination, file.getOriginalFilename()).isEmpty()){
            File fl = new File(UPLOAD_DIRECTORY + "/" + file.getOriginalFilename());
            if (fl != null){
                fl.delete();
            }
            createFileLocal(file);

            checkFlight.setDeparture(departure);
            checkFlight.setDestination(destination);
            checkFlight.setFilePath(file.getOriginalFilename());
            return true;
        }
        return false;
    }

    public List<Flight> getFlights(String departure, String destination){
        List<Flight> lst;
        lst = repository.getAllFlightsOrderedById();
        for (Flight fl : lst){
            checkIfFlightInfosNotExpired(fl);
        }
        if (departure != null){
            lst = repository.findByDepartureLike(departure);
        }
        else if(destination != null){
            lst = repository.findByDestinationLike(destination);
        }
        else {
            lst = repository.getAllFlightsOrderedById();
        }
        return lst;
    }

    public void checkIfFlightInfosNotExpired(Flight fl){
        List<FlightInfoEntity> info = flight_info_rep.findAllExpNotes(fl.getId());
        for (FlightInfoEntity item : info){
            if (LocalDateTime.now().isAfter(item.getDate())){
                repository.findById(item.getFlight().getId()).get().decreaseFlightsAvailableCount();
                flight_info_rep.deleteById(item.getId());
            }
        }
    }

    public Boolean saveFlight(String departure, String destination, MultipartFile file) throws IOException {
        if (departure.isBlank() || destination.isBlank()){
            return false;
        }
        Flight flight = new Flight(departure, destination, file.getOriginalFilename());
        createFileLocal(file);
        if ((!repository.findByDepartureLike(flight.getDeparture()).isEmpty()) &&
                (!repository.findByDestinationLike(flight.getDestination()).isEmpty())){
            return false;
        }
        repository.save(flight);
        return true;
    }

    public void deleteFlight(Long id){
        flight_info_rep.deleteAllFlightInfoByFlightId(id);
        repository.deleteById(id);
    }

    public List<FlightProfile> convertFlights(String departure, String destination) throws IOException {
        List<Flight> lst;
        List<FlightProfile> flights = new ArrayList<>();
        lst = getFlights(departure, destination);
        for(Flight fl : lst){
            File file = new File(UPLOAD_DIRECTORY + "/" + fl.getFilePath());
            FileInputStream str = new FileInputStream(file);
            byte[] arr = new byte[(int)file.length()];
            str.read(arr);
            str.close();
            flights.add(new FlightProfile(fl, arr));
        }
        return flights;
    }

    public void createFileLocal(MultipartFile file) throws IOException {
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
    }
}
