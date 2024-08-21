/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.domibus.connector.web.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.security.core.context.SecurityContextHolder;

@Route(value = LogoutView.ROUTE)
@org.springframework.stereotype.Component
@UIScope
public class LogoutView extends Div implements BeforeEnterObserver {

    public static final String ROUTE = "logout";

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        SecurityContextHolder.clearContext();
        event.rerouteTo(LoginView.ROUTE);

        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().getSession().close();
    }
}
