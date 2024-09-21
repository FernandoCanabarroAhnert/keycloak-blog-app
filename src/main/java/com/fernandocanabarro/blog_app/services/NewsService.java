package com.fernandocanabarro.blog_app.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RListMultimap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fernandocanabarro.blog_app.controllers.NewsController;
import com.fernandocanabarro.blog_app.domain.dtos.newsapi.NewsApiNotice;
import com.fernandocanabarro.blog_app.domain.dtos.newsapi.NewsApiResponse;
import com.fernandocanabarro.blog_app.domain.dtos.newsapi.NewsHub;

@Service
public class NewsService {

    @Autowired
    private RestTemplate restTemplate;

    private String newsApiKey = "pub_536570a1b9571d64a61d536e7747bb477e4c7";
    private String baseUrl = "https://newsdata.io/api/1/latest?country=br&apikey=" + newsApiKey;

    private List<String> categories = new ArrayList<>(Arrays.asList("sports","politics","technology","business","environment","entertainment"));


    private RListMultimap<String,NewsApiNotice> cacheList;

    public NewsService(RedissonClient client) {
        cacheList = client.getListMultimap("notices",new TypedJsonJacksonCodec(NewsApiNotice.class));
    }

    public NewsHub getNoticesCategories(){
        NewsHub newsHub = new NewsHub("Abaixo estão as categorias das notícias disponíveis:");
        for (String category : categories){
            newsHub.add(linkTo(methodOn(NewsController.class).getNoticesByCategory(category)).withRel("Consultar Notícias da Categoria: " + category));
        }
        newsHub.add(linkTo(methodOn(NewsController.class).getLatestNews()).withRel("Consultar Notícias em Alta"));
        return newsHub;
    }

    public Page<NewsApiNotice> getLatestNews(){
        List<NewsApiNotice> list = cacheList.getAll("latest");
        return new PageImpl<>(list, PageRequest.of(0, 10), list.size());
    }

    public Page<NewsApiNotice> getNoticesByCategory(String category){
        List<NewsApiNotice> list = cacheList.getAll(category);
        return new PageImpl<>(list, PageRequest.of(0, 10), list.size());
    }

    @Scheduled(fixedRate = 12,timeUnit = TimeUnit.MINUTES)
    private void updateCache(){
        cacheList.clear();
        for (String category : categories){
            ResponseEntity<NewsApiResponse> response = restTemplate.getForEntity(String.format("https://newsdata.io/api/1/latest?country=br&category=%s&apikey=pub_536570a1b9571d64a61d536e7747bb477e4c7",category), NewsApiResponse.class);
            List<NewsApiNotice> list = response.getBody().getResults();
            cacheList.putAll(category, list);
        }
        ResponseEntity<NewsApiResponse> response = restTemplate.getForEntity(baseUrl, NewsApiResponse.class);
        List<NewsApiNotice> list = response.getBody().getResults();
        cacheList.putAll("latest", list);
    }
    
}
