package com.example.demo.Service;

import com.example.demo.FlightProfile.FlightProfile;
import com.example.demo.Logger.CustomLogger;
import com.example.demo.Model.Flight;
import com.example.demo.Model.FlightInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface FlightService {
    boolean updateFlight(Long id, String departure, String destination, MultipartFile file);

    Page<Flight> getFlights(String departure, String destination, Pageable paging);

    void checkIfFlightInfosNotExpired(Flight fl, Pageable pageable);

    Boolean saveFlight(String departure, String destination, MultipartFile file);

     void deleteFlight(Long id);
}
