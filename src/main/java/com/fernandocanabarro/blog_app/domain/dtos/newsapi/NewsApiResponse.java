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
public class NewsApiResponse implements Serializable{

    private List<NewsApiNotice> results;
}
