/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.configuration.domain;

import eu.ecodex.utils.configuration.api.annotation.ConfigurationDescription;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationLabel;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Holds information about an with
 * {@code @ConfigurationProperties} annotated bean
 */
public class ConfigurationPropertiesBeanInformation {


    private Object bean;

    /**
     * The configuration Properties annotation
     */
    private ConfigurationProperties configurationPropertiesAnnotation;

    /**
     * holds the configuration configurationLabelAnnotation
     */
    private ConfigurationLabel configurationLabelAnnotation;

    /**
     * holds the configuratoin configurationDescriptionAnnotation
     */
    private ConfigurationDescription configurationDescriptionAnnotation;

    /**
     * Name of the spring bean
     */
    private String beanName;

    public ConfigurationLabel getConfigurationLabelAnnotation() {
        return configurationLabelAnnotation;
    }

    public void setConfigurationLabelAnnotation(ConfigurationLabel configurationLabelAnnotation) {
        this.configurationLabelAnnotation = configurationLabelAnnotation;
    }

    public ConfigurationDescription getConfigurationDescriptionAnnotation() {
        return configurationDescriptionAnnotation;
    }

    public void setConfigurationDescriptionAnnotation(
            ConfigurationDescription configurationDescriptionAnnotation) {
        this.configurationDescriptionAnnotation = configurationDescriptionAnnotation;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public ConfigurationProperties getConfigurationPropertiesAnnotation() {
        return configurationPropertiesAnnotation;
    }

    public void setConfigurationPropertiesAnnotation(
            ConfigurationProperties configurationPropertiesAnnotation) {
        this.configurationPropertiesAnnotation = configurationPropertiesAnnotation;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String key) {
        this.beanName = key;
    }
}
