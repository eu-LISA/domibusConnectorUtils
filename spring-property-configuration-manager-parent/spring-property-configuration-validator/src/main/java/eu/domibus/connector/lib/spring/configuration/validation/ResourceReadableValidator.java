/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.domibus.connector.lib.spring.configuration.validation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.core.io.Resource;

public class ResourceReadableValidator
        implements ConstraintValidator<CheckResourceIsReadable, Resource> {


    @Override
    public void initialize(CheckResourceIsReadable constraintAnnotation) {

    }

    @Override
    public boolean isValid(Resource value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        try {
            InputStream inputStream = value.getInputStream();
            if (inputStream == null) {
//                context.buildConstraintViolationWithTemplate("eu.domibus.connector.lib.spring.configuration.validation.resource_input_stream_valid")
//                        .addConstraintViolation();
                String message = String.format(
                        "Cannot open provided resource [%s]! Check if the path is correct and exists!",
                        value);
                context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
                return false;
            }
            inputStream.close();
        } catch (IOException e) {
            String message = String.format(
                    "Cannot open provided resource [%s]! Check if the path is correct and exists!",
                    value);
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        }
        return true;
    }

    private String getUrl(Resource value) {
        URL url = null;
        try {
            url = value.getURL();
            return url.toString();
        } catch (IOException e) {
            return value.toString();
        }
    }
}
