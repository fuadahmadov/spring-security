package com.springsecurity.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.springsecurity.constant.RoleName;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, unique = true)
    private RoleName name;
}