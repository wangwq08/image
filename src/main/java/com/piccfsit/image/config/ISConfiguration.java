package com.piccfsit.image.config;

import com.piccfsit.image.domain.ImagePath;
import com.piccfsit.image.domain.ImageSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ImagePath.class, ImageSize.class})
public class ISConfiguration {

    @Autowired
    public ImagePath ip;

    @Autowired
    public ImageSize is;

}



