package com.example.image.config;

import com.example.image.domain.ImagePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ImagePath.class)
public class ISConfiguration {

    @Autowired
    public ImagePath ip;
}
