package com.example.demo.user;

import com.example.demo.customer.Customer;
import com.example.demo.merchant.Merchant;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String password;

    @OneToOne(mappedBy = "userDetails1")
    private Customer customer;

    @OneToOne(mappedBy = "userDetails2")
    private Merchant merchant;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "customer_id", referencedColumnName = "id")
//    private Customer customerDetails;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "merchant_id", referencedColumnName = "id")
//    private Merchant merchantDetails;
//
//    public Merchant getMerchantDetails() {
//        return merchantDetails;
//    }
//
//    public void setMerchantDetails(Merchant merchantDetails) {
//        this.merchantDetails = merchantDetails;
//    }
//
//    public Customer getCustomerDetails() {
//        return customerDetails;
//    }
//
//    public void setCustomerDetails(Customer customerDetails) {
//        this.customerDetails = customerDetails;
//    }
//    @Enumerated(EnumType.STRING)
//    private Role role;
//
//    @OneToMany(mappedBy = "user")
    //private List<Token> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //return role.getAuthorities();
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}