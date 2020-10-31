package com.webservices.tpsoap.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.webservices.tpsoap.entity.BooksEntity;

@Repository
public interface BooksEntityRepository extends CrudRepository<BooksEntity, Long> {

	public BooksEntity findById(int id);
	public BooksEntity findByTitle(String title);
	public BooksEntity findByIsbn(String isbn);
	public BooksEntity findByDate(String date);
	public BooksEntity findByAuthorId(int authorId);	
}