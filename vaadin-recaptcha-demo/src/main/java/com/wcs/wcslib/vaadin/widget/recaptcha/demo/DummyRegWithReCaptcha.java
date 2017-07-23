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

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.wcs.wcslib.vaadin.widget.recaptcha.ReCaptcha;
import com.wcs.wcslib.vaadin.widget.recaptcha.shared.ReCaptchaOptions;

/**
 *
 * @author kumm
 */
public class DummyRegWithReCaptcha extends Panel implements Button.ClickListener {

    private VerticalLayout content;
    private ReCaptcha reCaptcha;
    private Binder<RegistrationBean> binder;
    private int captchaFailCount = 0;
    private Runnable showFunc;
    private RegistrationFields registrationFields;
    private Label statusLabel;

    public DummyRegWithReCaptcha(ConfigComponent config, Runnable showFunc) {
        this.showFunc = showFunc;
        bindFields();
        reCaptcha = createCaptcha(config);
        setCaption("Dummy registration");
        setSizeUndefined();
        content = new VerticalLayout();
        setContent(content);
        buildContent();
    }

    private void bindFields() {
        binder = new Binder<>(RegistrationBean.class);
        registrationFields = new RegistrationFields();
        binder.forMemberField(registrationFields.login)
                .asRequired("Required!");
        binder.forMemberField(registrationFields.password).asRequired("Required!");
        binder.forMemberField(registrationFields.passwordAgain).asRequired("Required!");
        binder.bindInstanceFields(registrationFields);
        binder.setBean(new RegistrationBean());
        binder.withValidator((regBean) ->
                regBean.password.equals(regBean.passwordAgain),"passwords do not match");
        statusLabel = new Label();
        binder.setStatusLabel(statusLabel);
    }

    private void buildContent() {
        content.setMargin(true);
        content.addComponent(createForm());
        reCaptcha.setWidth(100, Unit.PERCENTAGE);
        content.addComponent(reCaptcha);
        Button reconfigBtn = createCancelBtn();
        Button submitBtn = new Button("Register", this);
        submitBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        submitBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        HorizontalLayout actionsLayout = new HorizontalLayout(submitBtn, reconfigBtn);
        actionsLayout.setSpacing(true);
        actionsLayout.setWidth(100, Unit.PERCENTAGE);
        actionsLayout.setComponentAlignment(reconfigBtn, Alignment.MIDDLE_RIGHT);
        content.addComponent(actionsLayout);
    }

    private Layout createForm() {
        GridLayout layout = new GridLayout(2, 3);
        layout.setSpacing(true);
        layout.addComponent(registrationFields.login, 0, 0);
        layout.addComponent(registrationFields.password, 0, 1);
        layout.addComponent(registrationFields.passwordAgain, 1, 1);
        layout.addComponent(statusLabel, 0, 2, 1, 2);
        registrationFields.login.focus();
        return layout;
    }

    private ReCaptcha createCaptcha(final ConfigComponent config) {
        return new ReCaptcha(
                "6Lc8ESUTAAAAAB_8IHl5FcE8o_QCToT44hOhBa-1",
                new ReCaptchaOptions() {
                    {
                        theme = config.getTheme();
                        sitekey = "6Lc8ESUTAAAAABAYgG11XSkPRY-YXTKKFCjGb9GJ";
                        type = config.getType();
                        size = config.getSize();
                    }
                },
                config.getLang()
        );
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        final BinderValidationStatus<RegistrationBean> status = binder.validate();
        if (status.hasErrors()) {
            return;
        }

        //captcha should be the last validation
        if (!reCaptcha.validate()) {
            if (++captchaFailCount > 2) {
                content.setEnabled(false);
                Notification.show("You are an evil bot!", Notification.Type.ERROR_MESSAGE);
            } else {
                if (!reCaptcha.isEmpty()) {
                    Notification.show("Invalid Captcha!", Notification.Type.ERROR_MESSAGE);
                    reCaptcha.reload();
                } else {
                    Notification.show("Please fill the captcha!", Notification.Type.ERROR_MESSAGE);
                }
            }
            return;
        }

        //after a successfull captcha validation you should hide it
        //since you can use a code just once
        content.removeAllComponents();
        content.addComponent(new Label("Congratulations!"));
        content.addComponent(new Button("again", (Button.ClickListener) e -> showFunc.run()));
    }

    private Button createCancelBtn() {
        return new Button("Cancel", event -> Page.getCurrent().reload());
    }

    public class RegistrationBean {

        private String login = "";
        private String password = "";
        private String passwordAgain = "";

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPasswordAgain() {
            return passwordAgain;
        }

        public void setPasswordAgain(String passwordAgain) {
            this.passwordAgain = passwordAgain;
        }
    }

    private class RegistrationFields {

        private TextField login;
        private PasswordField password;
        private PasswordField passwordAgain;

        public RegistrationFields() {
            login = new TextField("login");
            login.setTabIndex(2);
            password = new PasswordField("password");
            password.setTabIndex(3);
            passwordAgain = new PasswordField("retype password");
            passwordAgain.setTabIndex(4);
        }

    }

}
