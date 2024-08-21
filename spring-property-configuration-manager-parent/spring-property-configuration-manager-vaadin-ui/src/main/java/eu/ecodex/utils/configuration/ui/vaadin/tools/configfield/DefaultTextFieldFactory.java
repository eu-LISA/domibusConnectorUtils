/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.configuration.ui.vaadin.tools.configfield;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.function.ValueProvider;
import eu.ecodex.utils.configuration.domain.ConfigurationProperty;
import eu.ecodex.utils.configuration.ui.vaadin.tools.ConfigurationFieldFactory;
import eu.ecodex.utils.configuration.ui.vaadin.tools.UiConfigurationConversationService;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class DefaultTextFieldFactory implements ConfigurationFieldFactory {

    @Autowired
    @UiConfigurationConversationService
    ConversionService conversionService;

    @Autowired
    javax.validation.Validator validator;

    @Override
    public boolean canHandle(Class clazz) {
        return conversionService.canConvert(String.class, clazz);
    }

    @Override
    public AbstractField createField(ConfigurationProperty configurationProperty,
                                     Binder<Map<String, String>> binder) {
        TextField tf = new TextField();

        Class parentClass = configurationProperty.getParentClass();

        Binder.BindingBuilder<Map<String, String>, String> propertiesStringBindingBuilder =
                binder.forField(tf);
        if (parentClass != null) {
            //can currently only add a validator if the parent class is known and has Bean Validation
            propertiesStringBindingBuilder =
                    propertiesStringBindingBuilder.withValidator(new Validator<String>() {

                        @Override
                        public ValidationResult apply(String value, ValueContext context) {


                            Object convertedValue = value;
                            try {
                                if (StringUtils.isNotEmpty(value)) {
                                    convertedValue = conversionService.convert(value,
                                            configurationProperty.getType());
                                } else {
                                    convertedValue = null;
                                }
                            } catch (ConversionFailedException conversionFailed) {
                                //TODO: improve error message...
                                return ValidationResult.error(conversionFailed.getMessage());
                            }

                            Set<ConstraintViolation<?>> constraintViolationSet =
                                    validator.validateValue(parentClass,
                                            configurationProperty.getBeanPropertyName(),
                                            convertedValue);
                            if (constraintViolationSet.isEmpty()) {
                                return ValidationResult.ok();
                            }
                            String errors = constraintViolationSet.stream()
                                    .map(constraintViolation -> constraintViolation.getMessage())
                                    .collect(Collectors.joining("\n"));
                            return ValidationResult.error(errors);
                        }
                    });
        }

        propertiesStringBindingBuilder.withNullRepresentation("");

        Binder.Binding<Map<String, String>, String> binding = propertiesStringBindingBuilder.bind(
                (ValueProvider<Map<String, String>, String>) o -> o.getOrDefault(
                        configurationProperty.getPropertyName(), null),
                (Setter<Map<String, String>, String>) (props, value) -> {
                    if (value == null) {
                        props.remove(configurationProperty.getPropertyName());
                    } else {
                        props.put(configurationProperty.getPropertyName(), value);
                    }
                });
        return tf;
    }


}
