/*
 * Copyright 2013 kumm.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wcs.wcslib.vaadin.widget.recaptcha.demo;


import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import javax.servlet.annotation.WebServlet;

@Title("ReCaptcha Add-on Demo")
@SuppressWarnings("serial")
@Theme("valo")
public class DemoUI extends UI {

    @WebServlet(value = "/*")
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {

        final VerticalLayout layout = new VerticalLayout();
        final ConfigComponent configComponent = new ConfigComponent();

        layout.addComponent(new Label("<h1>Vaadin ReCaptcha Add-on Demo</h1>"
                + "<p>See "
                + "<a href=\"https://developers.google.com/recaptcha/docs/customization\" target=\"_blank\">"
                + "ReCaptcha API"
                + "</a>"
                + " to understand theese settings. Or just Press 'SHOW' :)"
                + "</p>", ContentMode.HTML));
        layout.addComponent(configComponent);
        layout.setSpacing(true);
        layout.setMargin(true);

        Button showBtn = new Button("SHOW", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                layout.removeAllComponents();
                DummyRegWithReCaptcha dummyRegWithReCaptcha = new DummyRegWithReCaptcha(configComponent);
                layout.addComponent(dummyRegWithReCaptcha);
                layout.setComponentAlignment(dummyRegWithReCaptcha, Alignment.MIDDLE_CENTER);
            }
        });

        layout.addComponent(showBtn);
        setContent(layout);
    }

}
