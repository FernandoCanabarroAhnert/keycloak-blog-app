package com.fernandocanabarro.blog_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fernandocanabarro.blog_app.domain.dtos.newsapi.NewsApiNotice;
import com.fernandocanabarro.blog_app.domain.dtos.newsapi.NewsHub;
import com.fernandocanabarro.blog_app.services.NewsService;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService service;

    @GetMapping
    public ResponseEntity<NewsHub> getNoticesCategories(){
        return ResponseEntity.ok(service.getNoticesCategories());   
    }

    @GetMapping("/latest")
    public ResponseEntity<Page<NewsApiNotice>> getLatestNews(){
        return ResponseEntity.ok(service.getLatestNews());   
    }

    @GetMapping("/{category}")
    public ResponseEntity<Page<NewsApiNotice>> getNoticesByCategory(@PathVariable String category){
        return ResponseEntity.ok(service.getNoticesByCategory(category));   
    }
}
