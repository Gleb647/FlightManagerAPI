package com.example.demo.Service;

import com.example.demo.EmailDetails.EmailDetails;
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
        Ticket ticket = new Ticket(user, flight_repo.findById(id).get());
        ticket_repo.save(ticket);
        EmailDetails details = new EmailDetails(
                "glebtkach647@gmail.com",
                "Checking email service.",
                "Simple Email Message",
                null);
        email_service.sendSimpleMail(details);
    }
}
