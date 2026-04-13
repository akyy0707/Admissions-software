package com.tuyensinh.DTO;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "xt_users")
public class UserDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "status")
    private Boolean status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ThiSinhDTO thiSinh;

    public enum Role {
        ADMIN,
        USER
    }

    public UserDTO() {
        this.createdAt = new Date();
    }

    public UserDTO(String username, String password, Role role, Boolean status) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
        this.createdAt = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public ThiSinhDTO getThiSinh() {
        return thiSinh;
    }

    public void setThiSinh(ThiSinhDTO thiSinh) {
        this.thiSinh = thiSinh;
    }
}