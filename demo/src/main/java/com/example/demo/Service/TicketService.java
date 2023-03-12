package com.example.demo.Service;

import com.example.demo.EmailDetails.EmailDetails;
import com.example.demo.Model.FlightInfoEntity;
import com.example.demo.Model.Ticket;
import com.example.demo.Model.User;
import com.example.demo.Repository.FlightInfoRepository;
import com.example.demo.Repository.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    @Autowired
    public UserService user_service;

    @Autowired
    public FlightInfoRepository flight_repo;

    @Autowired
    public TicketRepo ticket_repo;

    @Autowired
    public EmailService email_service;

    public void buyTicket(Long id, String username){
        User user = user_service.getUser(username);
        String name =user.getName();
        FlightInfoEntity flight = flight_repo.findById(id).get();
        Ticket ticket = new Ticket(user, flight);
        ticket_repo.save(ticket);
        EmailDetails details = new EmailDetails(
                "glebtkach@rambler.ru",
                "Dear "+ name,
                "Info about flight:\n"+
                        flight.getDate()+"\n"+
                        flight.getCarrier()+"\n"+
                        flight.getCost()+"\n"+
                        flight.getFlightDuration()+"\n",
                null);
        email_service.sendSimpleMail(details);
    }
}
