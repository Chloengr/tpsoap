package com.webservices.tpsoap.service;

import java.util.List;
import com.webservices.tpsoap.entity.AuthorsEntity;

public interface AuthorsEntityService {

	public AuthorsEntity getEntityById(int id);
	public AuthorsEntity getEntityByFirstname(String firstname);
	public AuthorsEntity getEntityByLastname(String lastname);
	public AuthorsEntity getEntityByBookId(int bookId);
	public List<AuthorsEntity> getAllEntities();
	public AuthorsEntity addEntity(AuthorsEntity entity);
	public boolean updateEntity(AuthorsEntity entity);
	public boolean deleteEntityById(int id);
}