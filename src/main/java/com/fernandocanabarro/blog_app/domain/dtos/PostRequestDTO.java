package com.fernandocanabarro.blog_app.domain.dtos;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDTO {

    @NotBlank(message = "Campo Obrigatório")
    private String title;
    @NotBlank(message = "Campo Obrigatório")
    private String text;
    private String mediaUrl;
    private List<String> tags;
}
