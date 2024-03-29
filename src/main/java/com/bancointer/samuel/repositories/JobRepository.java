package com.bancointer.samuel.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bancointer.samuel.domain.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {

	List<Job> findByNameIgnoreCase(String name);	
}
