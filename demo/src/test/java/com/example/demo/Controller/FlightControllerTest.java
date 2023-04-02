package com.example.demo.Controller;

import com.example.demo.FlightProfile.FlightProfile;
import com.example.demo.Model.Flight;
import com.example.demo.Service.FlightsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.postgresql.hostchooser.HostRequirement.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ContextConfiguration(classes = { FlightController.class, FlightsService.class})
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class FlightControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FlightsService base_serv;
    @Test
    void addNewFlight() throws Exception {
        Flight fl = new Flight("minsk", "krakow", null);
        MockMultipartFile jsonFile = new MockMultipartFile(
                "file", "logo2.jpg", "application/json",
                ("{\"file\": \"/home/gleb/IdeaProjects/FlightManager.server/API/FlightManageAPI/demo/src/main/resources/" +
                        "static/logo2.jpg/\"\"}").getBytes());
        when(base_serv.saveFlight(
                ArgumentMatchers.any(String.class), ArgumentMatchers.any(String.class),
                ArgumentMatchers.any(MultipartFile.class))
        ).thenReturn(true);
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/flights/add");
        mvc.perform(builder
                        .file(jsonFile)
                        .param("departure", "minsk")
                        .param("destination", "krakow")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void addExistingFlight() throws Exception {
        Flight fl = new Flight("minsk", "krakow", null);
        MockMultipartFile jsonFile = new MockMultipartFile(
                "file", "logo2.jpg", "application/json",
                ("{\"file\": \"/home/gleb/IdeaProjects/FlightManager.server/API/FlightManageAPI/demo/src/main/resources/" +
                        "static/logo2.jpg/\"\"}").getBytes());
        when(base_serv.saveFlight(fl.getDeparture(), fl.getDestination(), jsonFile)).thenReturn(false);
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/flights/add");
        mvc.perform(builder
                        .file(jsonFile)
                        .param("departure", "minsk")
                        .param("destination", "krakow")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void getFlightsWithoutParams() throws Exception {
        Flight fl1 = new Flight("minsk", "istanbul", null);
        Flight fl2 = new Flight("dublin", "paris", null);
        FlightProfile profile1 = new FlightProfile(fl1, null);
        FlightProfile profile2 = new FlightProfile(fl2, null);
        when(base_serv.convertFlights(null, null)).thenReturn(Arrays.asList(profile1, profile2));

        mvc.perform(MockMvcRequestBuilders
                        .get("/flights/get")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].flight.departure", is(fl1.getDeparture())))
                .andExpect(jsonPath("$.[0].flight.destination", is(fl1.getDestination())))
                .andExpect(jsonPath("$.[1].flight.departure", is(fl2.getDeparture())))
                .andExpect(jsonPath("$.[1].flight.destination", is(fl2.getDestination())))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getFlightsWithParams() throws Exception{
        Flight fl1 = new Flight("minsk", "istanbul", null);
        FlightProfile profile = new FlightProfile(fl1, null);
        when(base_serv.convertFlights("minsk", null)).thenReturn(Arrays.asList(profile));

        mvc.perform(MockMvcRequestBuilders
                        .get("/flights/get")
                        .param("departure", "minsk")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].flight.departure", is(fl1.getDeparture())))
                .andExpect(jsonPath("$.[0].flight.destination", is(fl1.getDestination())))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateFlight() throws Exception {
        MockMultipartFile jsonFile = new MockMultipartFile(
                "file", "logo2.jpg", "application/json",
                ("{\"file\": \"/home/gleb/IdeaProjects/FlightManager.server/API/FlightManageAPI/demo/src/main/resources/" +
                        "static/logo2.jpg/\"\"}").getBytes());
        when(base_serv.updateFlight(
                ArgumentMatchers.any(Long.class), ArgumentMatchers.any(String.class),
                ArgumentMatchers.any(String.class), ArgumentMatchers.any(MultipartFile.class)
        )).thenReturn(true);
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/flights/change/1");
        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });
        mvc.perform(builder
                .file(jsonFile)
                .param("departure", "minsk")
                .param("destination", "istanbul")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}