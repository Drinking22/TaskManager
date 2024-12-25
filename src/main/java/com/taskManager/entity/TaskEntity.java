package com.taskManager.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)

@Entity
@Table(name = "tasks")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonProperty("user_id")
    private UserEntity user;

    private String title;
    private String description;

    public TaskEntity(Long id, UserEntity user, String title, String description) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.description = description;
    }

    public TaskEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
