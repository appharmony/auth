package com.example.auth.payload.response;

import java.util.Set;

import com.example.auth.model.Role;

import lombok.Data;

@Data
public class SignupResponse {
  private String username;
  private String email;
  private Set<Role> roles;

  public SignupResponse(String username, String email, Set<Role> roles) {
    this.username = username;
    this.email = email;
    this.roles = roles;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Set<Role> getRoles() {
    return roles;
  }
}