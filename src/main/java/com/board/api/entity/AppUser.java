package com.board.api.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString(callSuper = true)
@Getter
@NoArgsConstructor
public class AppUser extends DateAndAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long userSeq;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String idType;

    @Column(nullable = false)
    private String idValue;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private UserRole userRole;

    public void setUserRole(UserRole userRole) {
        userRole.setAppUser(this);
        this.userRole = userRole;
    }

    @Builder
    public AppUser(String userId, String password, String name, String idType, String idValue) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.idType = idType;
        this.idValue = idValue;
    }

}
