package com.example.demo.Service;

import com.example.demo.EmailDetails.EmailDetails;
import com.example.demo.Model.Flight;
import com.example.demo.Model.FlightInfoEntity;
import com.example.demo.Model.Ticket;
import com.example.demo.Model.User;
import com.example.demo.Repository.FlightInfoRepository;
import com.example.demo.Repository.FlightsRepository;
import com.example.demo.Repository.TicketRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TicketService {

    public UserService user_service;

    public FlightInfoRepository flight_info_repo;

    public TicketRepo ticket_repo;

    public EmailService email_service;

    @Transactional
    public boolean buyTicket(Long id, String username){
        User user = user_service.getUser(username);
        String name = user.getName();
        FlightInfoEntity flightinfo = flight_info_repo.findById(id).get();
        Ticket ticket = new Ticket(user, flightinfo);
        ticket_repo.save(ticket);
        Flight flight = flightinfo.getFlight();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");
        EmailDetails details = new EmailDetails(
                        username,
                "Info about flight:\n"+
                        "From: "+flight.getDeparture()+"\n"+
                        "To: "+flight.getDestination()+"\n"+
                        "Date: "+flightinfo.getDate().format(formatter)+"\n"+
                        "Carrier: "+flightinfo.getCarrier()+"\n"+
                        "Cost: "+flightinfo.getCost()+"$\n"+
                        "Flight duration: "+flightinfo.getFlightDuration()+"h\n",
                "Dear "+ name,
                null);
        return email_service.sendSimpleMail(details);
    }
}
