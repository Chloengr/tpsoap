package com.webservices.tpsoap.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.webservices.tpsoap.entity.BooksEntity;
import com.webservices.tpsoap.repository.BooksEntityRepository;

@Service
@Transactional
public class BooksEntityServiceImpl implements BooksEntityService {

    private BooksEntityRepository repository;

    public BooksEntityServiceImpl() {

    }

    @Autowired
    public BooksEntityServiceImpl(BooksEntityRepository repository) {
        this.repository = repository;
    }

	@Override
    public BooksEntity getEntityById(int id) {
        return this.repository.findById(id);
    }

    @Override
    public BooksEntity getEntityByTitle(String title) {
        return this.repository.findByTitle(title);
    }

    @Override
    public List < BooksEntity > getAllEntities() {
        List < BooksEntity > list = new ArrayList < > ();
        repository.findAll().forEach(e -> list.add(e));
        return list;
    }

	@Override
	public BooksEntity getEntityByIsbn(String isbn) {
        return this.repository.findByIsbn(isbn);
	}

	@Override
	public BooksEntity getEntityByDate(String date) {
        return this.repository.findByDate(date);
	}

	@Override
	public BooksEntity getEntityByAuthorId(int authorId) {
        return this.repository.findByAuthorId(authorId);
	}

	@Override
	public BooksEntity addEntity(BooksEntity entity) {
		try {
			return this.repository.save(entity);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean updateEntity(BooksEntity entity) {
		try {
			this.repository.save(entity);
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteEntityById(int id) {
		try {
			this.repository.deleteById(id);
			return true;
		}catch(Exception e) {
			return false;
		}
		
	}

}