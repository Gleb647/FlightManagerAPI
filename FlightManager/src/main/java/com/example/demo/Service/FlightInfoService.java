package com.example.demo.Service;

import com.example.demo.Repository.FlightsRepository;
import com.example.demo.Repository.FlightInfoRepository;
import com.example.demo.Model.Flight;
import com.example.demo.Model.FlightInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightInfoService {
    @Autowired
    private FlightInfoRepository flightInfoRepo;

    @Autowired
    private FlightsRepository flightRepo;

    public FlightInfoEntity addFlightInfo(FlightInfoEntity flightInfo, Long id){
        Flight flight = flightRepo.findById(id).get();
        flightInfo.setFlight(flight);
        return flightInfoRepo.save(flightInfo);
    }

    public List<FlightInfoEntity> findAllExpNotes(Long id){
        return flightInfoRepo.findAllExpNotes(id);
    }
}
