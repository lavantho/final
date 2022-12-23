package com.insmart.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name= "organizations")
@Data@AllArgsConstructor
@NoArgsConstructor
public class Organization implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_id")
    private Long id;
    private String code;
    private String name;
    private String description;
    @ManyToOne(targetEntity = Organization.class)
    @JoinColumn(name = "parent_id")
    private Organization parent;
    @OneToMany(mappedBy = "parent",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Organization> children;
}
