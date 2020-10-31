package com.webservices.tpsoap.endpoint;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.webservices.tpsoap.entity.BooksEntity;
import com.webservices.tpsoap.service.BooksEntityService;

import com.tpsoap.ws.BookType;
import com.tpsoap.ws.GetAllBooksRequest;
import com.tpsoap.ws.GetAllBooksResponse;
import com.tpsoap.ws.GetBookByIdRequest;
import com.tpsoap.ws.GetBookByIdResponse;
//import com.tpsoap.ws.ServiceStatus;

@Endpoint
public class BooksEndpoint {

	public static final String NAMESPACE_URI = "http://www.tpsaop.fr/library-ws";

	private BooksEntityService service;

	public BooksEndpoint() {

	}

	@Autowired
	public BooksEndpoint(BooksEntityService service) {
		this.service = service;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBookByIdRequest")
	@ResponsePayload
	public GetBookByIdResponse getEntityById(@RequestPayload GetBookByIdRequest request) {
		GetBookByIdResponse response = new GetBookByIdResponse();
		BooksEntity bookEntity = service.getEntityById(request.getId());
		BookType bookType = new BookType();
		BeanUtils.copyProperties(bookEntity, bookType);
		response.setBookType(bookType);
		return response;

	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllBookRequest")
	@ResponsePayload
	public GetAllBookResponse getAllBooks(@RequestPayload GetAllBookRequest request) {
		GetAllBookResponse response = new GetAllBookResponse();
		List<BookType> bookTypeList = new ArrayList<BookType>();
		List<BooksEntity> bookEntityList = service.getAllEntities();
		for (BooksEntity entity : bookEntityList) {
			BookType bookType = new BookType();
			BeanUtils.copyProperties(entity, bookType);
			bookTypeList.add(bookType);
		}
		response.getBookType().addAll(bookTypeList);

		return response;
	}
	

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "addBookRequest")
	@ResponsePayload
	public AddBookResponse addBook(@RequestPayload AddBookRequest request) {
		AddBookResponse response = new AddBookResponse();
		BookType newBookType = new BookType();
		ServiceStatus serviceStatus = new ServiceStatus();

		BooksEntity newBookEntity = new BooksEntity(request.getTitle(), request.getIsbn(), request.getDate(), request.getAuthorId());
		BooksEntity savedBookEntity = service.addEntity(newBookEntity);

		if (savedBookEntity == null) {
			serviceStatus.setStatusCode("CONFLICT");
			serviceStatus.setMessage("Exception while adding Entity");
		} else {

			BeanUtils.copyProperties(savedBookEntity, newBookType);
			serviceStatus.setStatusCode("SUCCESS");
			serviceStatus.setMessage("Content Added Successfully");
		}

		response.setBookType(newBookType);
		response.setServiceStatus(serviceStatus);
		return response;

	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateBookRequest")
	@ResponsePayload
	public UpdateBookResponse updateBook(@RequestPayload UpdateBookRequest request) {
		UpdateBookResponse response = new UpdateBookResponse();
		ServiceStatus serviceStatus = new ServiceStatus();
		// 1. Find if book available
		BooksEntity bookFromDB = service.getEntityByTitle(request.getTitle()) && service.getEntityByDate(request.getDate()) || service.getEntityByIsbn(request.getIsbn());
		
		if(bookFromDB == null) {
			serviceStatus.setStatusCode("NOT FOUND");
			serviceStatus.setMessage("Book = " + request.getTitle() + " not found");
		}
		else {
			
			// 2. Get updated book information from the request
			bookFromDB.setTitle(request.getTitle());
			bookFromDB.setDate(request.getDate());
			bookFromDB.setIsbn(request.getIsbn());
			// 3. update the book in database
			
			boolean flag = service.updateEntity(bookFromDB);
			
			if(flag == false) {
				serviceStatus.setStatusCode("CONFLICT");
				serviceStatus.setMessage("Exception while updating Entity=" + request.getTitle());;
			}else {
				serviceStatus.setStatusCode("SUCCESS");
				serviceStatus.setMessage("Content updated Successfully");
			}
			
			
		}
		
		response.setServiceStatus(serviceStatus);
		return response;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteBookRequest")
	@ResponsePayload
	public DeleteBookResponse deleteBook(@RequestPayload DeleteBookRequest request) {
		DeleteBookResponse response = new DeleteBookResponse();
		ServiceStatus serviceStatus = new ServiceStatus();

		boolean flag = service.deleteEntityById(request.getId());

		if (flag == false) {
			serviceStatus.setStatusCode("FAIL");
			serviceStatus.setMessage("Exception while deleting Entity id=" + request.getId());
		} else {
			serviceStatus.setStatusCode("SUCCESS");
			serviceStatus.setMessage("Content Deleted Successfully");
		}

		response.setServiceStatus(serviceStatus);
		return response;
	}

}