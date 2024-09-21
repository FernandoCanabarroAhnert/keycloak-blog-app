package com.fernandocanabarro.blog_app.domain.dtos.newsapi;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsApiNotice implements Serializable{

    private String title;
    private String link;
    private String description;
    private String pubDate;
    private String image_url;
    private String source_name;
    private String source_url;
    private List<String> category;
}
