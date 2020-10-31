package com.webservices.tpsoap.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.webservices.tpsoap.entity.AuthorsEntity;

@Repository
public interface AuthorsEntityRepository extends CrudRepository<AuthorsEntity, Long> {

	public AuthorsEntity findById(int id);
	public AuthorsEntity findByFirstname(String firstname);
	public AuthorsEntity findByLastname(String lastname);
	public AuthorsEntity findByBookId(int bookId);	
}