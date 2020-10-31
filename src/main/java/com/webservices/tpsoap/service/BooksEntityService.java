package com.webservices.tpsoap.service;

import java.util.List;
import com.webservices.tpsoap.entity.BooksEntity;

public interface BooksEntityService {

	public BooksEntity getEntityById(int id);
	public BooksEntity getEntityByTitle(String title);
	public BooksEntity getEntityByIsbn(String isbn);
	public BooksEntity getEntityByDate(String date);
	public BooksEntity getEntityByAuthorId(int authorId);
	public List<BooksEntity> getAllEntities();
	public BooksEntity addEntity(BooksEntity entity);
	public boolean updateEntity(BooksEntity entity);
	public boolean deleteEntityById(int id);
}