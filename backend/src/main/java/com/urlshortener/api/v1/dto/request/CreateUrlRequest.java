package com.urlshortener.api.v1.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class CreateUrlRequest {

    @NotBlank(message = "URL is required")
    @URL(message = "Enter a valid URL staring with http:// ot https://")
    @Size(max = 8192)
    private String originalUrl;

    @Size(max = 100)
    private String title;

    public CreateUrlRequest() {

    }

    public CreateUrlRequest(String originalUrl, String title) {
        this.originalUrl = originalUrl;
        this.title = title;
    }


}
