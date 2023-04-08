package com.example.demo.Service;

import com.example.demo.FlightProfile.FlightProfile;
import com.example.demo.Logger.CustomLogger;
import com.example.demo.Model.FlightInfoEntity;
import com.example.demo.Repository.FlightInfoRepository;
import com.example.demo.Repository.FlightsRepository;
import com.example.demo.Model.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightsServiceImpl implements FlightService{

    //private static String UPLOAD_DIRECTORY = "/data";
    private static String UPLOAD_DIRECTORY ="/home/gleb/IdeaProjects/FlightManager.server/API/FlightManageAPI/demo/src/main/resources/static";

    @Autowired
    private FlightsRepository repository;

    @Autowired
    private FlightInfoRepository flight_info_rep;

    @Autowired
    private FlightConverterUtilsImpl flightConverterUtilsImpl;

    public FlightsServiceImpl(FlightsRepository repository, FlightInfoRepository flight_info_rep) {
        this.repository = repository;
        this.flight_info_rep = flight_info_rep;
    }

    public boolean updateFlight(Long id, String departure, String destination, MultipartFile file){
        Flight checkFlight = repository.findById(id).orElseThrow(() ->
                new IllegalStateException("Flight with id " + id + " doesn't exist"));
        if (repository.checkIfNodeExist(departure, destination, file.getOriginalFilename()).isEmpty()){
            File fl = new File(UPLOAD_DIRECTORY + "/" + checkFlight.getFilePath());
            fl.delete();
            flightConverterUtilsImpl.createFileLocal(file);
            checkFlight.setDeparture(departure);
            checkFlight.setDestination(destination);
            checkFlight.setFilePath(file.getOriginalFilename());
            repository.save(checkFlight);
            return true;
        }
        return false;
    }

    public Page<Flight> getFlights(String departure, String destination, Pageable paging){
        Page<Flight> lst;
        lst = repository.getAllFlightsOrderedById(paging);
        for (Flight fl : lst){
            checkIfFlightInfosNotExpired(fl, paging);
        }
        if (departure != null){
            lst = repository.findByDepartureLike(departure, paging);
        }
        else if(destination != null){
            lst = repository.findByDestinationLike(destination, paging);
        }
        else {
            lst = repository.getAllFlightsOrderedById(paging);
        }
        return lst;
    }

    @Transactional
    public void checkIfFlightInfosNotExpired(Flight fl, Pageable paging){
        List<FlightInfoEntity> info = flight_info_rep.findAllExpNotes(fl.getId(), paging).getContent();
        for (FlightInfoEntity item : info){
            if (LocalDateTime.now().isAfter(item.getDate())){
                repository.findById(item.getFlight().getId()).get().decreaseFlightsAvailableCount();
                flight_info_rep.deleteById(item.getId());
            }
        }
    }

    public Boolean saveFlight(String departure, String destination, MultipartFile file){
        if (departure.isBlank() || destination.isBlank()){
            return false;
        }
        Flight flight = new Flight(departure, destination, file.getOriginalFilename());
        flightConverterUtilsImpl.createFileLocal(file);
        if ((!repository.findByDepartureLike(flight.getDeparture(), null).isEmpty()) &&
                (!repository.findByDestinationLike(flight.getDestination(), null).isEmpty())){
            return false;
        }
        repository.save(flight);
        return true;
    }

    @Transactional
    public void deleteFlight(Long id){
        flight_info_rep.deleteAllFlightInfoByFlightId(id);
        repository.deleteById(id);
    }
}
