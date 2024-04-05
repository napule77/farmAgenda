package com.amalfi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amalfi.entity.Utenti;

public interface UtentiRepository extends JpaRepository<Utenti, Integer> {

	Utenti findByEmailAndPassword(String username, String md5pwd);

}
