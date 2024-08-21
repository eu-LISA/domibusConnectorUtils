/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.spring.quartz.domain;

import eu.ecodex.utils.spring.quartz.annotation.QuartzScheduled;
import java.lang.reflect.Method;
import org.springframework.core.style.ToStringCreator;

public class TriggerAndJobDefinition {

    private final String beanName;
    QuartzScheduled quartzScheduled;
    Method method;
    Object bean;

    public TriggerAndJobDefinition(String beanName, QuartzScheduled quartzScheduled, Method method,
                                   Object bean) {
        this.beanName = beanName;
        this.quartzScheduled = quartzScheduled;
        this.method = method;
        this.bean = bean;
    }

    public String getBeanName() {
        return beanName;
    }

    public QuartzScheduled getScheduled() {
        return quartzScheduled;
    }

    public void setScheduled(QuartzScheduled quartzScheduled) {
        this.quartzScheduled = quartzScheduled;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public String toString() {
        return new ToStringCreator(this)
                .append("bean", this.beanName)
                .append("method", this.method.getName())
                .toString();
    }
}
