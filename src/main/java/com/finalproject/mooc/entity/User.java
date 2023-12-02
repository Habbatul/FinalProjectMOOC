package com.finalproject.mooc.entity;

import com.finalproject.mooc.entity.security.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true)
    private String emailAddress;

    @Column(unique = true)
    private String username;

    private String password;
    private String phoneNumber;
    private String city;
    private String country;

    private String urlPhoto;

    private Boolean isActive;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Course> course;

    //relasi cascade
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Order> orders;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Roles> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<RegisterOtp> registerOtp;

    public void addRegisterOtp(RegisterOtp registerOtp) {
        if (this.registerOtp == null) {
            this.registerOtp = new ArrayList<>();
        }
        this.registerOtp.add(registerOtp);
        registerOtp.setUser(this);
    }

}
