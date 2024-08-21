/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.configuration.example1.validators;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CompareStringsValidator implements ConstraintValidator<CompareStrings, Object> {

    private String[] propertyNames;
    private StringComparisonMode comparisonMode;
    private boolean allowNull;

    @Override
    public void initialize(CompareStrings constraintAnnotation) {
        this.propertyNames = constraintAnnotation.propertyNames();
        this.comparisonMode = constraintAnnotation.matchMode();
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Object target, ConstraintValidatorContext context) {
        boolean isValid = true;
        List<String> propertyValues = new ArrayList<String>(propertyNames.length);
        for (int i = 0; i < propertyNames.length; i++) {
            String propertyValue =
                    ConstraintValidatorHelper.getPropertyValue(String.class, propertyNames[i],
                            target);
            if (propertyValue == null) {
                if (!allowNull) {
                    isValid = false;
                    break;
                }
            } else {
                propertyValues.add(propertyValue);
            }
        }

        if (isValid) {
            isValid = ConstraintValidatorHelper.isValid(propertyValues, comparisonMode);
        }

        if (!isValid) {
            /*
             * if custom message was provided, don't touch it, otherwise build the
             * default message
             */
            String message = context.getDefaultConstraintMessageTemplate();
            message = (message.isEmpty()) ?
                    ConstraintValidatorHelper.resolveMessage(propertyNames, comparisonMode) :
                    message;

            context.disableDefaultConstraintViolation();
            ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder =
                    context.buildConstraintViolationWithTemplate(message);
            for (String propertyName : propertyNames) {
                ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext
                        nbdc = violationBuilder.addNode(propertyName);
                nbdc.addConstraintViolation();
            }
        }

        return isValid;
    }
}
