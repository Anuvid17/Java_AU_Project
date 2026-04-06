package com.quizsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entity representing a user in the quiz system.
 * Each user has a unique username, an encoded password, and a role (ADMIN or STUDENT).
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Unique username for login */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be 3-50 characters")
    @Column(unique = true, nullable = false)
    private String username;

    /** BCrypt-encoded password */
    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    /** User role: ADMIN or STUDENT */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // ===== Constructors =====

    public User() {}

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // ===== Getters & Setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
