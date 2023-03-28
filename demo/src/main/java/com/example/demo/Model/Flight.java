package com.example.demo.Model;

import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "flight")
@Transactional
public class Flight{
    @Id
    @SequenceGenerator(name = "flight_sequence", sequenceName = "flight_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flight_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @NotNull
    @Column(name = "departure", nullable = false, columnDefinition = "TEXT")
    private String departure;

    @NotNull
    @Column(name = "destination", nullable = false, columnDefinition = "TEXT")
    private String destination;

    @Column(name = "image", columnDefinition = "TEXT")
    private String filePath;

    @Column(name = "flights_available")
    private Integer flightsAvailable = 0;

    public Flight(String from, String to, String filePath) {
        this.departure = from;
        this.destination = to;
        this.filePath = filePath;
    }

    public Flight(long l, String from, String to, String filePath) {
        this.id = l;
        this.departure = from;
        this.destination = to;
        this.filePath = filePath;
    }

    public synchronized void increaseFlightsAvailableCount(){
        this.flightsAvailable++;
    }

    public synchronized void decreaseFlightsAvailableCount(){
        this.flightsAvailable--;
    }

}
