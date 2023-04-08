package com.example.demo.Service;

import com.example.demo.FlightProfile.FlightProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FlightConverterUtils {
    List<FlightProfile> convertFlights(String departure, String destination, Pageable paging);

    void createFileLocal(MultipartFile file);
}
