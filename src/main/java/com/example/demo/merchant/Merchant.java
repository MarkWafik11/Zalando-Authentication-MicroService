package com.example.demo.merchant;

import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
@Table
public class Merchant {
    @Id
    @SequenceGenerator(
            name = "merchant_sequence",
            sequenceName = "merchant_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "merchant_sequence"
    )

    private Long id;
    private String brandName;
    private String email;
    private String hotline;
    private LocalDate dateJoined;

    public Merchant() {
    }
    public Merchant(Long id, String brandName, String email, String hotline, LocalDate dateJoined) {
        this.id = id;
        this.brandName = brandName;
        this.email = email;
        this.hotline = hotline;
        this.dateJoined = dateJoined;
    }

    public Merchant(String brandName, String email, String hotline, LocalDate dateJoined) {
        this.brandName = brandName;
        this.email = email;
        this.hotline = hotline;
        this.dateJoined = dateJoined;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public LocalDate getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(LocalDate dateJoined) {
        this.dateJoined = dateJoined;
    }

    @Override
    public String toString() {
        return "Merchant{" +
                "id=" + id +
                ", brandName='" + brandName + '\'' +
                ", email='" + email + '\'' +
                ", hotline='" + hotline + '\'' +
                ", dateJoined=" + dateJoined +
                '}';
    }
}
