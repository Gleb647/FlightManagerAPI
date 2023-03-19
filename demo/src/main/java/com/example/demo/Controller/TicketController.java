package com.example.demo.Controller;

import com.example.demo.Model.Ticket;
import com.example.demo.Model.User;
import com.example.demo.Service.TicketService;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
public class TicketController {

    @Autowired
    public TicketService ticket_service;

    @GetMapping("/buy-ticket/{id}")
    public ResponseEntity buyTicket(@PathVariable("id") Long id,
                          @RequestParam(name = "username", required = false) String username){
        if (ticket_service.buyTicket(id, username)) {
            return new ResponseEntity("Ticket bought", HttpStatus.OK);
        }
        return new ResponseEntity("Error while buying ticket", HttpStatus.BAD_REQUEST);
    }
}
