/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.configuration.testdata.subpackage1;


import eu.ecodex.utils.configuration.api.annotation.ConfigurationDescription;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationLabel;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "example.configuration")
@ConfigurationLabel("Example Configuration")
@ConfigurationDescription("Properties for the example configuration")
public class ExamplePropertiesConfig {

    @NotNull
    @ConfigurationLabel("A text")
    private String text;


    /**
     * a number which should be max 60
     */
    @NotNull
    @ConfigurationDescription("A number.........")
    @Max(60)
    private Integer number;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
