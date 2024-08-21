/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.configuration.domain;

import java.util.Objects;
import org.springframework.core.style.ToStringCreator;
import org.springframework.lang.Nullable;

public class ConfigurationProperty {

    //TODO: replace with org.springframework.boot.context.properties.source.ConfigurationPropertyName
    private String propertyName;

    private String beanPropertyName;

    private String description;

    private String label;

    private Class type;

    /**
     * Holds the with org.springframework.boot.context.properties.bind.DefaultValue
     * annotated default value
     */
    private String[] defaultValue;

    /**
     * The class this property is part of
     */
    @Nullable
    private Class parentClass;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getBeanPropertyName() {
        return beanPropertyName;
    }

    public void setBeanPropertyName(String beanPropertyName) {
        this.beanPropertyName = beanPropertyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String[] getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String[] defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String toString() {
        return new ToStringCreator(this)
                .append("propertyName", propertyName)
                .append("label", label)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigurationProperty that)) {
            return false;
        }

        return Objects.equals(propertyName, that.propertyName);
    }

    @Override
    public int hashCode() {
        return propertyName != null ? propertyName.hashCode() : 0;
    }

    public Class getParentClass() {
        return parentClass;
    }

    public void setParentClass(Class parentClass) {
        this.parentClass = parentClass;
    }
}
