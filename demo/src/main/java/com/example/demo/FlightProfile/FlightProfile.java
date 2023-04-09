package com.example.demo.FlightProfile;

import com.example.demo.Model.Flight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightProfile {

    private Flight flight;
    private byte[] file;
}
