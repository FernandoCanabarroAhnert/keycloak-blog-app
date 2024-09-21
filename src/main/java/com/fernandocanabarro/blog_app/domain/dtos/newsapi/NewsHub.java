package com.fernandocanabarro.blog_app.domain.dtos.newsapi;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsHub extends RepresentationModel<NewsHub>{

    private String content;
}
