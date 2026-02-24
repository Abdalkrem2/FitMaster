package com.web.fitmaster.model;

import com.web.fitmaster.model.enums.AppRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @ToString.Exclude
    @Column(length = 20,name = "role_name")
    @Enumerated(EnumType.STRING)//we add it because when we store enum in database by default its stored as integer
    private AppRole roleName;

    public Role(AppRole roleName) {
        this.roleName = roleName;
    }
}
