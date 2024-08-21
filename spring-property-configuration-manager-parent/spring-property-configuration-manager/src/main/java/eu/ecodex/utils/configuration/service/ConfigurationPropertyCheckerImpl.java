/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.configuration.service;

import eu.ecodex.utils.configuration.domain.ConfigurationPropertiesBean;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.validation.BindValidationException;
import org.springframework.boot.context.properties.bind.validation.ValidationBindHandler;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.Validator;

public class ConfigurationPropertyCheckerImpl implements ConfigurationPropertyChecker {


    private static final Logger LOGGER =
            LogManager.getLogger(ConfigurationPropertyCheckerImpl.class);

//    private ConfigurationPropertySource configurationPropertySource;

    @Autowired
    private Validator validator;

    @Autowired
    private ConfigurationPropertyCollector configurationPropertyCollector;

    public ConfigurationPropertyCheckerImpl() {
    }

    public ConfigurationPropertyCheckerImpl(
            ConfigurationPropertyCollector configurationPropertyCollector, Validator validator) {
        this.configurationPropertyCollector = configurationPropertyCollector;
        this.validator = validator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public void setConfigurationPropertyCollector(
            ConfigurationPropertyCollector configurationPropertyCollector) {
        this.configurationPropertyCollector = configurationPropertyCollector;
    }

//    public String getStringValueForProperty(ConfigurationProperty configProperty) {
//        return null;
//    }
//
//    public Object getValueForProperty(ConfigurationProperty configProperty) {
//        return null;
//    }


    public List<ValidationErrors> validateConfiguration(
            ConfigurationPropertySource configurationPropertySource, Class... basePackageFilter) {

        List<String> packageName = Arrays.asList(basePackageFilter)
                .stream()
                .map(Class::getPackage)
                .map(Package::getName)
                .collect(Collectors.toList());
        return validateConfiguration(configurationPropertySource, packageName);
    }

    public List<ValidationErrors> validateConfiguration(
            ConfigurationPropertySource configurationPropertySource, String... basePackageFilter) {
        return validateConfiguration(configurationPropertySource, Arrays.asList(basePackageFilter));
    }

    @Override
    public List<ValidationErrors> validateConfiguration(
            ConfigurationPropertySource configurationPropertySource,
            Collection<Class> configurationClasses) {
        LOGGER.debug("#isConfigurationValid for classes: [{}]", configurationClasses);

//        Collection<ConfigurationPropertiesBean> configurationBeans = configurationPropertyCollector.getConfigurationBeans(basePackageFilter);

        List<ValidationErrors> collectErrors = configurationClasses.stream()
                .map(entry -> this.isConfigValidForClazz(configurationPropertySource, entry))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return collectErrors;
    }


    /**
     * {@inheritDoc}
     * <p>
     * Tests if the configuration is valid, all properties are loaded from the
     * provided configuration source
     */
    public List<ValidationErrors> validateConfiguration(
            ConfigurationPropertySource configurationPropertySource,
            List<String> basePackageFilter) {
        LOGGER.debug("#isConfigurationValid for packages: [{}]", basePackageFilter);

        Collection<ConfigurationPropertiesBean> configurationBeans =
                configurationPropertyCollector.getConfigurationBeans(basePackageFilter);

        List<ValidationErrors> collectErrors = configurationBeans.stream()
                .map(entry -> this.isConfigValidForClazz(configurationPropertySource,
                        entry.getBeanClazz()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return collectErrors;
    }

    private Optional<ValidationErrors> isConfigValidForClazz(
            ConfigurationPropertySource configurationPropertySource, Class configClass) {
//        Class<?> configClass = entry.getBeanClazz();

//            Object config = configClass.getDeclaredConstructor().newInstance();
        ConfigurationProperties annotation =
                AnnotationUtils.getAnnotation(configClass, ConfigurationProperties.class);
        if (annotation == null) {
            return Optional.empty();
        }
        String prefix = (String) AnnotationUtils.getValue(annotation);
        if (prefix == null) {
            prefix = "";
        }

        Bindable<?> bindable = Bindable.of(configClass).withAnnotations(annotation);
        Binder b = new Binder(configurationPropertySource);

        LOGGER.debug("Binding class [{}] with prefix [{}]", configClass, prefix);


//        DataBinder dataBinder = new DataBinder();

        ValidationBindHandler validationBindHandler = new ValidationBindHandler(validator);

//        validationBindHandler.onFailure()

        try {
            BindResult<?> bind = b.bind(prefix, bindable, validationBindHandler);
        } catch (BindValidationException bindValidationException) {
            return Optional.of(bindValidationException.getValidationErrors());
        } catch (BindException bindException) {
            Throwable cause = bindException.getCause();
            if (cause instanceof BindValidationException bve) {
                return Optional.of(bve.getValidationErrors());
            }
        }


//        if (bind.isBound()) {
//            LOGGER.trace("is bound!");
//        }

        //TODO: validate bounded variables
        return Optional.empty();
    }


}
