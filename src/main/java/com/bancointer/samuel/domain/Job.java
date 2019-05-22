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

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class Job implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NAME_JOB")
	private String name;

	@Column(name = "IS_ACTIVE")
	private Boolean active;

	@ManyToOne
	@JoinColumn(name = "ID_PARENT_JOB")
	private Job parentJob;

	@OneToMany(mappedBy = "job", targetEntity = Task.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Task> tasks;

}
