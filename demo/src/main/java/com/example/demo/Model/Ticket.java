package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "ticket")
@Data
@NoArgsConstructor
public class Ticket {
    @Id
    @SequenceGenerator(name = "ticket_sequence", sequenceName = "ticket_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @OneToOne
    private User user;

    @OneToOne
    private FlightInfoEntity flight;

    @Column(name = "date", nullable = false, columnDefinition = "TEXT")
    private LocalDateTime buyingDate;

    public Ticket(User user, FlightInfoEntity flight) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.user = user;
        this.flight = flight;
        this.buyingDate = LocalDateTime.now();
    }
}
