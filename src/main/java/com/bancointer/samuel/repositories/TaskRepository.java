package com.bancointer.samuel.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bancointer.samuel.domain.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

	List<Task> findByCreatedAt(LocalDate createDate);
	List<Task> findByCreatedAt(LocalDate createDate,Pageable page);
}
