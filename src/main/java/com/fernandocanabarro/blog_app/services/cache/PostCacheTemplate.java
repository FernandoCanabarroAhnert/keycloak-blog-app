package com.fernandocanabarro.blog_app.services.cache;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

public abstract class PostCacheTemplate<Id,Dto,DtoRequest> {

    public Dto findById(Id id){
        if (getFromCache(id).isPresent()) {
            return getFromCache(id).get();
        }
        Dto obj = getFromDB(id);
        updateCache(id,obj);
        return obj;
    }

    public Dto update(Id id,DtoRequest dto,HttpServletRequest request){
        Dto obj = updateDB(id, dto, request);
        updateCache(id, obj);
        return obj;
    }

    public void delete(Id id,HttpServletRequest request){
        deleteFromDB(id,request);
        deleteFromCache(id);
    }

    abstract public Dto getFromDB(Id id);
    abstract public Optional<Dto> getFromCache(Id id);
    abstract public Dto updateDB(Id id,DtoRequest dto,HttpServletRequest request);
    abstract public void updateCache(Id id,Dto dto);
    abstract public void deleteFromDB(Id id,HttpServletRequest request);
    abstract public void deleteFromCache(Id id);
}
