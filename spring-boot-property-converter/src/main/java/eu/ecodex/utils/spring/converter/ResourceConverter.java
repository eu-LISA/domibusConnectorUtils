/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.spring.converter;

import java.net.MalformedURLException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

public class ResourceConverter implements Converter<String, Resource> {

    @Override
    public Resource convert(String source) {
        if (source.startsWith("classpath:")) {
            return new ClassPathResource(source.substring("classpath:".length()));
        }
        if (source.startsWith("file://")) {
            return new FileSystemResource(source.substring("file://".length()));
        }
        if (source.startsWith("file:")) {
            return new FileSystemResource(source.substring("file:".length()));
        }
        try {
            return new UrlResource(source);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
