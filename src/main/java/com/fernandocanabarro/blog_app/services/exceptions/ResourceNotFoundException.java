package com.fernandocanabarro.blog_app.services.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String id){
        super("Recurso não Encontrado! Id = " + id);
    }
}
