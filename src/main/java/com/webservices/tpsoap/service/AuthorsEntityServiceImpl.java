package com.webservices.tpsoap.service;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.webservices.tpsoap.entity.AuthorsEntity;
import com.webservices.tpsoap.entity.BooksEntity;
import com.webservices.tpsoap.repository.AuthorsEntityRepository;

@Service
@Transactional
public class AuthorsEntityServiceImpl implements AuthorsEntityService {

    private AuthorsEntityRepository repository;

    public AuthorsEntityServiceImpl() {

    }

    @Autowired
    public AuthorsEntityServiceImpl(AuthorsEntityRepository repository) {
        this.repository = repository;
    }

	@Override
    public AuthorsEntity getEntityById(int id) {
        return this.repository.findById(id);
    }

    @Override
    public AuthorsEntity getEntityByFirstname(String firstname) {
        return this.repository.findByFirstname(firstname);
    }

	@Override
	public AuthorsEntity getEntityByLastname(String lastname) {
        return this.repository.findByLastname(lastname);
	}

	@Override
	public AuthorsEntity getEntityByBookId(int bookId) {
        return this.repository.findByBookId(bookId);
	}

    @Override
    public List < AuthorsEntity > getAllEntities() {
        List < AuthorsEntity > list = new ArrayList < > ();
        repository.findAll().forEach(e -> list.add(e));
        return list;
    }
    
    @Override
	public AuthorsEntity addEntity(AuthorsEntity entity) {
		try {
			return this.repository.save(entity);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean updateEntity(AuthorsEntity entity) {
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