package com.insmart.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@Table(name= "permissions")
public class Permission implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long id;
    @ManyToOne(targetEntity = Resource.class)
    @JoinColumn(name = "resource_id")
    private Resource resource;
    @Column(name = "`read`")
    private Boolean read;
    @Column(name = "`write`")
    private Boolean write;
    @Column(name = "`update`")
    private Boolean update;
    @Column(name = "`delete`")
    private Boolean delete;
}
