package com.example.firstcommit.entities;

import com.example.firstcommit.utils.Modality;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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

    private String phone;

    private String country;

    private String city;

    private Boolean relocation;

    @Enumerated(EnumType.STRING)
    private Modality modality;

    private String cv_url;

    private String image_url;

    //    Relaciones
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "candidates_tags",
            joinColumns = @JoinColumn(name = "candidate_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
    )
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Candidate() {
    }

    public Candidate(Long id, String fullName, String email, String phone, String country,
                     String city, Boolean relocation, Modality modality) {
        this.id = id;
        this.name = fullName;
        this.email = email;
        this.phone = phone;
        this.country = country;
        this.city = city;
        this.relocation = relocation;
        this.modality = modality;
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

    public Boolean getRelocation() {
        return relocation;
    }

    public void setRelocation(Boolean relocation) {
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
                ", modality=" + modality +
                ", cv_url='" + cv_url + '\'' +
                ", image_url='" + image_url + '\'' +
                '}';
    }

}
