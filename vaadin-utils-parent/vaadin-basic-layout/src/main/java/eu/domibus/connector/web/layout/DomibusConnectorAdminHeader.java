/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.domibus.connector.web.layout;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.web.configuration.SecurityUtils;

@SpringComponent
@UIScope
public class DomibusConnectorAdminHeader extends HorizontalLayout implements BeforeEnterObserver {

    Label currentUser = new Label();

    public DomibusConnectorAdminHeader() {
        Div ecodexLogo = new Div();
        Image ecodex = new Image("frontend/images/logo_ecodex_0.png", "eCodex");
        ecodex.setHeight("70px");
        ecodexLogo.add(ecodex);
        ecodexLogo.setHeight("70px");


        Div domibusConnector = new Div();
        Label dC = new Label("domibusConnector - Administration");
        dC.getStyle().set("font-size", "30px");
        dC.getStyle().set("font-style", "italic");
        dC.getStyle().set("color", "grey");
        domibusConnector.add(dC);
        domibusConnector.getStyle().set("text-align", "center");


        Div europaLogo = new Div();
        Image europa = new Image("frontend/images/europa-logo.jpg", "europe");
        europa.setHeight("50px");
        europaLogo.add(europa);
        europaLogo.setHeight("50px");
//		europaLogo.getStyle().set("margin-right", "3em");


        add(ecodexLogo, domibusConnector, europaLogo, currentUser);
        setAlignItems(Alignment.CENTER);
        expand(domibusConnector);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setWidth("95%");
//		headerLayout.getStyle().set("border-bottom", "1px solid #9E9E9E");
//		headerLayout.getStyle().set("padding-bottom", "16px");


    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (SecurityUtils.isUserLoggedIn()) {
            currentUser.setText("User: " + SecurityUtils.getUsername());
        } else {
            currentUser.setText("");
        }
    }
}
