/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.domibus.connector.web.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;

public class LoginErrorDialog extends Dialog {

    public LoginErrorDialog(String errorMessage) {
        Div loginExceptionDiv = new Div();
        Label loginException = new Label(errorMessage);
        loginException.getStyle().set("font-weight", "bold");
        loginException.getStyle().set("color", "red");
        loginExceptionDiv.add(loginException);
        loginExceptionDiv.getStyle().set("text-align", "center");
        loginExceptionDiv.setVisible(true);
        add(loginExceptionDiv);

        Div okContent = new Div();
        okContent.getStyle().set("text-align", "center");
        okContent.getStyle().set("padding", "10px");
        Button okButton = new Button("OK");
        okButton.addClickListener(e2 -> {

            close();
        });
        okButton.setAutofocus(true);
        okContent.add(okButton);

        add(okContent);
    }


}
