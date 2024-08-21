/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.configuration.ui.vaadin.tools;

import eu.ecodex.utils.configuration.ui.vaadin.tools.configfield.DefaultTextFieldFactory;
import eu.ecodex.utils.configuration.ui.vaadin.tools.configforms.ConfigurationFormsFactoryImpl;
import eu.ecodex.utils.configuration.ui.vaadin.tools.views.ListConfigurationPropertiesComponent;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

@Configuration
@ComponentScan(basePackageClasses = {ListConfigurationPropertiesComponent.class,
        DefaultTextFieldFactory.class, ConfigurationFormsFactoryImpl.class})
public class ConfigurationPropertiesUiAutoConfiguration {


    @Autowired(required = false)
    @Qualifier(ConfigurationPropertiesBinding.VALUE)
    private Set<Converter<?, ?>> converters = new HashSet<>();

    @Autowired(required = false)
    @Qualifier(UiConfigurationPropertyConverter.VALUE)
    private Set<Converter<?, ?>> uiConfigurationConverters = new HashSet<>();

    @Bean
    @UiConfigurationConversationService
    public ConversionService configurationPropertyUiConversionService() {
        Set<Converter<?, ?>> mergedConverters =
                Stream.of(uiConfigurationConverters.stream(), converters.stream())
                        .flatMap(Function.identity())
                        .distinct()
                        .collect(Collectors.toSet());

        ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
        bean.setConverters(mergedConverters);
        bean.afterPropertiesSet();
        ConversionService object = bean.getObject();
        return object;
    }

}
