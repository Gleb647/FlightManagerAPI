package com.example.demo.Service;

import com.example.demo.Model.Role;
import com.example.demo.Repository.RoleRepo;
import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("User not found in database");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public User saveUser(User user) {
        if (userRepo.findByUsername(user.getUsername()) == null){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepo.save(user);
        }
        return null;
    }

    public Role saveRole(Role role) {
        return roleRepo.save(role);
    }

    public void addRoleToUser(String username, String roleName) {
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        user.getRoles().add(role);
    }

    public User getUser(String username) {
        return userRepo.findByUsername(username);
    }

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public UserService(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String toString() {
        return "UserServiceImpl{" +
                "userRepo=" + userRepo +
                ", roleRepo=" + roleRepo +
                ", passwordEncoder=" + passwordEncoder +
                '}';
    }
}
