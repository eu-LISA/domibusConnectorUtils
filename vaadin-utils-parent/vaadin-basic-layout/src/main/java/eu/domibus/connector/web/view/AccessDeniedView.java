/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.domibus.connector.web.view;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.web.configuration.SecurityUtils;
import eu.domibus.connector.web.layout.BasicLayout;
import eu.domibus.connector.web.login.LoginView;

@UIScope
@Route(value = AccessDeniedView.ROUTE, layout = BasicLayout.class)
@PageTitle("domibusConnector - Administrator")
public class AccessDeniedView extends VerticalLayout implements BeforeEnterObserver {

    public static final String ROUTE = "accessDenied";

    Label l = new Label();
    String view = "";

    public AccessDeniedView() {
        String username = SecurityUtils.getUsername();
        l.setText("User [" + username + "]  has not enough privileges to access " + view);
        add(l);
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!SecurityUtils.isUserLoggedIn()) {
            event.getUI().navigate(LoginView.ROUTE);
        }
        //TODO: get previous view and set to view...
    }
}
