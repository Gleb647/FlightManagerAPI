package com.example.demo.Service;

import com.example.demo.Repository.FlightsRepository;
import com.example.demo.Repository.FlightInfoRepository;
import com.example.demo.Model.Flight;
import com.example.demo.Model.FlightInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class FlightInfoService {
    @Autowired
    private FlightInfoRepository flightInfoRepo;

    @Autowired
    private FlightsRepository flightRepo;

    public FlightInfoEntity addFlightInfo(FlightInfoEntity flightInfo, Long id){
        flightRepo.findById(id).get().increaseFlightsAvailableCount();
        Flight flight = flightRepo.findById(id).get();
        flightInfo.setFlight(flight);
        return flightInfoRepo.save(flightInfo);
    }

    public void checkIfFlightNotExpired(Long id){
        FlightInfoEntity flightInfo = flightInfoRepo.findById(id).get();
        if (LocalDateTime.now().isAfter(flightInfo.getDate())){
            flightRepo.findById(flightInfo.getFlight().getId()).get().decreaseFlightsAvailableCount();
            flightInfoRepo.deleteById(id);
        }
    }

    public List<FlightInfoEntity> findFlightInfoBetween(Long id, String min, String max){
        List<FlightInfoEntity> lst;
        if (min != null && max != null){
            lst = flightInfoRepo.findAllFlightsBetween(id, Integer.parseInt(min), Integer.parseInt(max));
        }
        else if (min != null){
            lst = flightInfoRepo.findAllFlightsAbove(id, Integer.parseInt(min));
        }
        else {
            lst =  flightInfoRepo.findAllFlightsBelow(id, Integer.parseInt(max));
        }
        return lst;
    }

    public boolean updateFlight(Long id, FlightInfoEntity info){
        FlightInfoEntity checkFlight = flightInfoRepo.findById(id).orElseThrow(() ->
                new IllegalStateException("Flightinfo with id " + id + " doesn't exist"));
        if (flightInfoRepo.checkIfNodeExist(info.getCarrier(), info.getFlightDuration(),
                checkFlight.getCost(), info.getDate()).isEmpty()){
            checkFlight.setCarrier(info.getCarrier());
            checkFlight.setFlightDuration(info.getFlightDuration());
            checkFlight.setCost(info.getCost());
            checkFlight.setDate(info.getDate());
            flightInfoRepo.save(checkFlight);
            return true;
        }
        return false;
    }

    public List<FlightInfoEntity> findAllExpNotes(Long id){
        return flightInfoRepo.findAllExpNotes(id);
    }

    public void deleteFlightInfo(Long id){
        flightRepo.findById(flightInfoRepo.findById(id).get().getFlight().getId()).get().decreaseFlightsAvailableCount();
        flightInfoRepo.deleteById(id);
    }
}
