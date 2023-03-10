package com.example.demo.Service;

import com.example.demo.Repository.FlightInfoRepository;
import com.example.demo.Repository.FlightsRepository;
import com.example.demo.Model.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

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

    public boolean updateFlight(Long id, Flight flight) {
        Flight checkFlight = repository.findById(id).orElseThrow(() ->
                new IllegalStateException("Flight with id " + id + " doesn't exist"));
        if (repository.checkIfNodeExist(flight.getDeparture(), flight.getDestination()).isEmpty()){
            checkFlight.setDeparture(flight.getDeparture());
            checkFlight.setDestination(flight.getDestination());
            return true;
        }
        return false;
    }

    public List<Flight> getFlights(String departure, String destination){
        List<Flight> lst;
        if (departure != null){
            lst = repository.findByDepartureLike(departure);
        }
        else if(destination != null){
            lst = repository.findByDestinationLike(destination);
        }
        else {
            HashMap<String, Integer> response = new HashMap<>();
            lst = repository.getAllFlightsOrderedById();
        }
        return lst;
    }

    public Boolean saveFlight(Flight flight){
        if (flight.getDeparture().isBlank() || flight.getDestination().isBlank()){
            return false;
        }
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
}
