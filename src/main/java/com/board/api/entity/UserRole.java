package com.board.api.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class UserRole {

    @Builder
    public UserRole(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long userRoleId;

    @Column(nullable = false)
    private String name;

    @Setter
    @OneToOne
    @JoinColumn(name = "user_seq")
    private AppUser appUser;
}
