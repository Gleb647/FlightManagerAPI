package com.example.demo.Service;

import com.example.demo.FlightProfile.FlightProfile;
import com.example.demo.Logger.CustomLogger;
import com.example.demo.Model.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightConverterUtilsImpl implements FlightConverterUtils{

    private static String UPLOAD_DIRECTORY = "/home/gleb/IdeaProjects/FlightManager.server/API/FlightManageAPI/demo/src/main/resources/static";

    @Autowired
    private FlightService flightService;

    public List<FlightProfile> convertFlights(String departure, String destination, Pageable paging) {
        List<Flight> lst;
        List<FlightProfile> flights = new ArrayList<>();
        lst = flightService.getFlights(departure, destination, paging).getContent();
        try {
            for(Flight fl : lst){
                File currentClass = new File(URLDecoder.decode(this.getClass()
                        .getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .getPath(), "UTF-8"));
                String classDirectory = currentClass.getParent();
                CustomLogger.info("{}: uploading directory", classDirectory);
                File file = new File(UPLOAD_DIRECTORY + "/" + fl.getFilePath());
                FileInputStream str = new FileInputStream(file);
                byte[] arr = new byte[(int)file.length()];
                str.read(arr);
                str.close();
                flights.add(new FlightProfile(fl, arr));
            }
        }
        catch (IOException e){
            CustomLogger.error("{}: error while converting flight images", this.getClass().getName());
        }
        return flights;
    }

    public void createFileLocal(MultipartFile file){
        try{
            CustomLogger.info("{}: uploading directory", String.valueOf(Path.of("./")));
            StringBuilder fileNames = new StringBuilder();
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename());
            Files.write(fileNameAndPath, file.getBytes());
        }
        catch (IOException e){
            CustomLogger.error("{}: error while uploading image", this.getClass().getName());
        }
    }
}
