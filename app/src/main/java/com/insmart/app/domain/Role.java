package com.insmart.app.domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name= "roles")
@Data@NoArgsConstructor
@AllArgsConstructor
public class Role implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;
    @Column(name = "`name`")
    private String name;
    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    private Collection<Resource> resources;
}
