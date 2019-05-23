package com.bancointer.samuel.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bancointer.samuel.domain.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

	List<Task> findByCreatedAt(LocalDate createDate);
	Page<Task> findByCreatedAt(LocalDate createDate,Pageable page);
	List<Task> findByNameIgnoreCase(String name);
}
