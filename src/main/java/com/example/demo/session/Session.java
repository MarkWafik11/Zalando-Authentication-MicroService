package com.example.demo.session;

import com.example.demo.user.Role;
public class Session {
    private String token;
    private Role role;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}
