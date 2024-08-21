/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.spring.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

public class ResourceConverterTest {

    private ResourceConverter resourceConverter;

    @BeforeAll
    public static void beforeAll() throws IOException {
        try {
            Path path = Paths.get("./target/testfile");
            Files.createFile(path);
        } catch (FileAlreadyExistsException e) {

        }
    }

    @BeforeEach
    public void initConverter() {

        this.resourceConverter = new ResourceConverter();
    }

    @Test
    void testClasspathResource() {
        Resource convert = resourceConverter.convert("classpath:/META-INF/spring.factories");
        assertThat(convert).isNotNull();
    }

    @Test
    void testFileResource1() throws IOException {
        Resource convert = resourceConverter.convert("file://./target/testfile");
        assertThat(convert).isNotNull();
    }

    @Test
    void testFileResource2() throws IOException {
        Resource convert = resourceConverter.convert("file://./target/testfile");
        assertThat(convert).isNotNull();
    }

    @Test
    void testFileResource3() throws IOException {
        Resource convert = resourceConverter.convert("file:./target/testfile");
        assertThat(convert).isNotNull();
    }

    @Test
    void testFileResource4() throws IOException {
        Resource convert = resourceConverter.convert("file:C:/test/testfile");
        assertThat(convert).isNotNull();
    }

    @Test
    void testUrlResource() throws IOException {
        Resource convert = resourceConverter.convert("http://www.example.com/nonexisting");
        assertThat(convert).isNotNull();
    }


}