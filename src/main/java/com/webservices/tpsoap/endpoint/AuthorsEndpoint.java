package com.webservices.tpsoap.endpoint;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.webservices.tpsoap.entity.AuthorsEntity;
import com.webservices.tpsoap.service.AuthorsEntityService;

import com.tpsoap.ws.AuthorType;
import com.tpsoap.ws.GetAllAuthorRequest;
import com.tpsoap.ws.GetAllAuthorResponse;
import com.tpsoap.ws.GetAuthorByIdRequest;
import com.tpsoap.ws.GetAuthorByIdResponse;
//import com.tpsoap.ws.ServiceStatus;

@Endpoint
public class AuthorsEndpoint {

	public static final String NAMESPACE_URI = "http://www.tpsaop.fr/library";

	private AuthorsEntityService service;

	public AuthorsEndpoint() {

	}

	@Autowired
	public AuthorsEndpoint(AuthorsEntityService service) {
		this.service = service;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAuthorsByIdRequest")
	@ResponsePayload
	public GetAuthorByIdResponse getEntityById(@RequestPayload GetAuthorByIdRequest request) {
		GetAuthorByIdResponse response = new GetAuthorByIdResponse();
		AuthorsEntity authorEntity = service.getEntityById(request.getId());
		AuthorType authorType = new AuthorType();
		BeanUtils.copyProperties(authorEntity, authorType);
		response.setAuthorType(authorType);
		return response;

	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllAuthorsRequest")
	@ResponsePayload
	public GetAllAuthorResponse getAllAuthors(@RequestPayload GetAllAuthorRequest request) {
		GetAllAuthorResponse response = new GetAllAuthorResponse();
		List<AuthorType> authorTypeList = new ArrayList<AuthorType>();
		List<AuthorsEntity> authorEntityList = service.getAllEntities();
		for (AuthorsEntity entity : authorEntityList) {
			AuthorType authorType = new AuthorType();
			BeanUtils.copyProperties(entity, authorType);
			authorTypeList.add(authorType);
		}
		response.getAuthorType().addAll(authorTypeList);

		return response;
	}
	

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "addAuthorRequest")
	@ResponsePayload
	public AddAuthorResponse addAuthor(@RequestPayload AddAuthorRequest request) {
		AddAuthorResponse response = new AddAuthorResponse();
		AuthorType newAuthorType = new AuthorType();
		ServiceStatus serviceStatus = new ServiceStatus();

		AuthorsEntity newAuthorEntity = new AuthorsEntity(request.getFirstname(), request.getLastname());
		AuthorsEntity savedAuthorEntity = service.addEntity(newAuthorEntity);

		if (savedAuthorEntity == null) {
			serviceStatus.setStatusCode("CONFLICT");
			serviceStatus.setMessage("Exception while adding Entity");
		} else {

			BeanUtils.copyProperties(savedAuthorEntity, newAuthorType);
			serviceStatus.setStatusCode("SUCCESS");
			serviceStatus.setMessage("Content Added Successfully");
		}

		response.setAuthorType(newAuthorType);
		response.setServiceStatus(serviceStatus);
		return response;

	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateAuthorRequest")
	@ResponsePayload
	public UpdateAuthorResponse updateAuthor(@RequestPayload UpdateAuthorRequest request) {
		UpdateAuthorResponse response = new UpdateAuthorResponse();
		ServiceStatus serviceStatus = new ServiceStatus();
		// 1. Find if Author available
		AuthorsEntity authorFromDB = service.getEntityByFirstname(request.getFirstname()) && service.getEntityByLastname(request.getLastname());
		
		if(authorFromDB == null) {
			serviceStatus.setStatusCode("NOT FOUND");
			serviceStatus.setMessage("Author = " + request.getFirstname() + request.getLastname() + " not found");
		}else {
			
			// 2. Get updated Author information from the request
			authorFromDB.setFirstname(request.getFirstname());
			authorFromDB.setLastname(request.getFirstname());
			// 3. update the Author in database
			
			boolean flag = service.updateEntity(authorFromDB);
			
			if(flag == false) {
				serviceStatus.setStatusCode("CONFLICT");
				serviceStatus.setMessage("Exception while updating Entity=" + request.getFirstname() + request.getLastname());
			}else {
				serviceStatus.setStatusCode("SUCCESS");
				serviceStatus.setMessage("Content updated Successfully");
			}
			
			
		}
		
		response.setServiceStatus(serviceStatus);
		return response;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteAuthorRequest")
	@ResponsePayload
	public DeleteAuthorResponse deleteAuthor(@RequestPayload DeleteAuthorRequest request) {
		DeleteAuthorResponse response = new DeleteAuthorResponse();
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