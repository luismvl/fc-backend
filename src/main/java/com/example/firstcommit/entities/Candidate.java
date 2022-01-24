package com.example.firstcommit.entities;

import com.example.firstcommit.utils.Modality;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "candidates")
public class Candidate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true)
    private String phone;

    private String country;

    private String city;

    private boolean relocation;

    @Enumerated(EnumType.STRING)
    private Modality modality;

    private String cv_url;

//    Relaciones
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "candidates_tags", joinColumns = @JoinColumn(name = "candidate_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private List<Tag> tags = new ArrayList<>();

    @ManyToMany(mappedBy = "candidates")
    @JsonIgnore
    private final List<User> users = new ArrayList<>();

    public Candidate(){}

    public Candidate(Long id, String fullName, String email, String phone, String country,
                     String city, boolean relocation, Modality modality, String cv_url) {
        this.id = id;
        this.name = fullName;
        this.email = email;
        this.phone = phone;
        this.country = country;
        this.city = city;
        this.relocation = relocation;
        this.modality = modality;
        this.cv_url = cv_url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isRelocation() {
        return relocation;
    }

    public void setRelocation(boolean relocation) {
        this.relocation = relocation;
    }

    public Modality getModality() {
        return modality;
    }

    public void setModality(Modality modality) {
        this.modality = modality;
    }

    public String getCv_url() {
        return cv_url;
    }

    public void setCv_url(String cv_url) {
        this.cv_url = cv_url;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<User> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", relocation=" + relocation +
                ", modality='" + modality + '\'' +
                ", cv_url='" + cv_url + '\'' +
                '}';
    }
}
