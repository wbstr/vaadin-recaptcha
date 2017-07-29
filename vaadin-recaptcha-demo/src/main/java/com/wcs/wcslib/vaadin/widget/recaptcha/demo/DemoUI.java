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
import com.vaadin.data.Binder;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;
import java.io.Serializable;
import java.util.Arrays;

@Title("ReCaptcha Add-on Demo")
@SuppressWarnings("serial")
@Theme("valo")
public class DemoUI extends UI {

    private VerticalLayout layout;
    private ConfigForm configForm;
    private Config config;

    @WebServlet(value = "/*")
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {

        layout = new VerticalLayout();
        configForm = new ConfigForm();
        final Binder<Config> binder = new Binder<>(Config.class);
        binder.bindInstanceFields(configForm);
        config = new Config();
        binder.setBean(config);

        layout.addComponent(new Label("<h1>Vaadin ReCaptcha Add-on Demo</h1>"
                + "<p>See "
                + "<a href=\"https://developers.google.com/recaptcha/docs/customization\" target=\"_blank\">"
                + "ReCaptcha API"
                + "</a>"
                + " to understand these settings. Or just Press 'SHOW' :)"
                + "</p>", ContentMode.HTML));
        layout.addComponent(configForm);

        Button showBtn = new Button("SHOW", (Button.ClickListener) event -> show());

        layout.addComponent(showBtn);
        setContent(layout);
    }

    private void show()  {
        layout.removeAllComponents();
        DummyRegWithReCaptcha dummyRegWithReCaptcha = new DummyRegWithReCaptcha(config, () -> show());
        layout.addComponent(dummyRegWithReCaptcha);
        layout.setComponentAlignment(dummyRegWithReCaptcha, Alignment.MIDDLE_CENTER);
    }

    public static class ConfigForm extends FormLayout {
        private NativeSelect<String> theme = new NativeSelect<>("theme", Arrays.asList("light", "dark"));
        private NativeSelect<String> type = new NativeSelect<>("type", Arrays.asList("image", "audio"));
        private NativeSelect<String> size = new NativeSelect<>("size", Arrays.asList("normal", "compact"));
        private TextField lang = new TextField("lang");

        public ConfigForm() {
            addComponents(theme, type, size, lang);
        }
    }

    public static class Config implements Serializable {
        private String theme = "light";
        private String type = "image";
        private String size = "normal";
        private String lang;

        public String getTheme() {
            return theme;
        }

        public String getType() {
            return type;
        }

        public String getSize() {
            return size;
        }

        public String getLang() {
            return lang;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }
    }

}
