package com.patelbnb.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

@Entity
@Table(name = "authority")
public class Authority implements Serializable {

    @NotNull
    @Size(max = 50)
    @Id
    @Column(length = 50)
    private String name;

    public @NotNull @Size(max = 50) String getName() {
        return name;
    }

    public void setName(@NotNull @Size(max = 50) String name) {
        this.name = name;
    }
}
