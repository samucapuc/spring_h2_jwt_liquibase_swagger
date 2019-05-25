package com.bancointer.samuel.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "NAME_JOB", unique = true)
	private String name;

	@Column(name = "IS_ACTIVE")
	private Boolean active;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_PARENT_JOB")
	private Job parentJob;

	@OneToMany(mappedBy = "job", targetEntity = Task.class, fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH,CascadeType.REMOVE } )
	private List<Task> tasks;
	
	@Version
	private Integer version;
	
	
}
