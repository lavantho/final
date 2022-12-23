package com.insmart.app.domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
@Entity
@Table(name= "users")
@Data@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String name;
    private String username;
    private String password;
    private String email;
    private String code;
    private String description;
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Role> roles ;
}
