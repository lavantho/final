package com.insmart.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
@Entity
@Table(name= "resources")
@Data@AllArgsConstructor
@NoArgsConstructor
public class Resource implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id")
    private Long id;
    private String code;
    private String name;
    private String url;
    private String description;
    @ManyToOne(targetEntity = Resource.class,fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinColumn(name = "parent_id")
    private Resource parent;
    @OneToMany(targetEntity = Permission.class,mappedBy = "resource",cascade = CascadeType.MERGE)
    @JsonIgnore
    private Collection<Resource> resources ;
    @ManyToMany(cascade = CascadeType.MERGE,mappedBy = "resources",fetch = FetchType.EAGER)
    private Collection<Role> roles;
}
