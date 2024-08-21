/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.domibus.connector.lib.spring.configuration.validation;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class ResourceReadableValidatorTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    public void testResourceIsNull_shouldNotValidate() {
        TestEntity t = new TestEntity();

        Set<ConstraintViolation<TestEntity>> validate = validator.validate(t);

        validate.stream().forEach(
                a -> System.out.println(a.getMessage())
        );
        assertThat(validate).hasSize(1);

    }

    @Test
    public void testResourceConfiguredPathDoesNotExist_shouldNotValidate() {
        Resource res = new FileSystemResource("/dhjafjkljadflkjdaskldfaskjhdfs");
        TestEntity t = new TestEntity();
        t.setResource(res);

        Set<ConstraintViolation<TestEntity>> validate = validator.validate(t);

        validate.stream().forEach(
                a -> System.out.println(a.getMessage())
        );
        assertThat(validate).hasSize(1);

    }

    public static class TestEntity {

        @CheckResourceIsReadable
        Resource resource;

        public Resource getResource() {
            return resource;
        }

        public void setResource(Resource resource) {
            this.resource = resource;
        }
    }


}