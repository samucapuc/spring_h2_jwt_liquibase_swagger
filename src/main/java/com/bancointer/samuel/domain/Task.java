package com.bancointer.samuel.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_TASK")
	private Integer id;

	@Column(name = "NAME_TASK")
	private String name;

	@Column(name = "WEIGHT")
	private Integer weight;

	@Column(name = "IS_COMPLETED")
	private Boolean completed;

	@CreatedDate
	private LocalDate createdAt;

	@Version
	@Column(name = "VERSION")
	private int version;
	
	@ManyToOne(optional = true)
    @JoinColumn(name="ID_JOB", nullable=false)
	@JsonIgnore
	private Job job;

}
