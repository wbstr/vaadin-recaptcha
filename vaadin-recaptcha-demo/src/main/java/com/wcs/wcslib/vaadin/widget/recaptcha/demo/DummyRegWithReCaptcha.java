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

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.wcs.wcslib.vaadin.widget.recaptcha.ReCaptcha;
import com.wcs.wcslib.vaadin.widget.recaptcha.shared.ReCaptchaOptions;

/**
 *
 * @author kumm
 */
public class DummyRegWithReCaptcha extends Panel implements Button.ClickListener {

    private VerticalLayout content;
    private ReCaptcha reCaptcha;
    private BeanFieldGroup<RegistrationBean> fieldGroup;
    private RegistrationBean regBean;
    private int captchaFailCount = 0;

    public DummyRegWithReCaptcha(ConfigComponent config) {
        initFieldGroup();
        reCaptcha = createCaptcha(config);
        setCaption("Dummy registration");
        setSizeUndefined();
        content = new VerticalLayout();
        setContent(content);
        buildContent();
    }

    private void initFieldGroup() {
        fieldGroup = new BeanFieldGroup<RegistrationBean>(RegistrationBean.class);
        regBean = new RegistrationBean();
        fieldGroup.setItemDataSource(regBean);
    }

    private void buildContent() {
        content.setMargin(true);
        content.addComponent(createForm());
        content.addComponent(reCaptcha);
        Button reconfigBtn = createCancelBtn();
        Button submitBtn = new Button("Register", this);
        submitBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        submitBtn.addStyleName(Reindeer.BUTTON_DEFAULT);
        HorizontalLayout actionsLayout = new HorizontalLayout(submitBtn, reconfigBtn);
        actionsLayout.setSpacing(true);
        actionsLayout.setWidth(100, Unit.PERCENTAGE);
        actionsLayout.setComponentAlignment(reconfigBtn, Alignment.MIDDLE_RIGHT);
        content.addComponent(actionsLayout);
    }

    private Layout createForm() {
        RegistrationFields registrationFields = new RegistrationFields();
        fieldGroup.buildAndBindMemberFields(registrationFields);
        GridLayout layout = new GridLayout(2, 2);
        layout.setSpacing(true);
        layout.addComponent(registrationFields.getLogin(), 0, 0);
        layout.addComponent(registrationFields.getPassword(), 0, 1);
        layout.addComponent(registrationFields.getPasswordAgain(), 1, 1);
        registrationFields.login.focus();
        return layout;
    }

    private ReCaptcha createCaptcha(final ConfigComponent config) {
        return new ReCaptcha(
                "6Lfv5OoSAAAAAPEbWhNB0ERopfQpRxr8_5yncOmg", 
                "6Lfv5OoSAAAAAHa4zmExf6w2ja3vm-8ABKgyepq-", 
                new ReCaptchaOptions() {{
                    theme = config.getTheme();
                    lang = config.getLang();
                    custom_translations = config.getCustomTranslations();
                    custom_theme_widget = config.getCustomThemeWidget();
                }}, 
                config.getCustomHtml()
        );
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        try {
            fieldGroup.commit();
        } catch (FieldGroup.CommitException ex) {
            Notification.show("All fields are required!", Notification.Type.ERROR_MESSAGE);
            fieldGroup.discard();
            return;
        }
        if (!regBean.password.equals(regBean.passwordAgain)) {
            Notification.show("passwords do not match", Notification.Type.ERROR_MESSAGE);
            regBean.password = "";
            regBean.passwordAgain = "";
            fieldGroup.discard();
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
    }

    private Button createCancelBtn() {
        return new Button("Cancel", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Page.getCurrent().reload();
            }
        });
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

    public class RegistrationFields {

        private TextField login;
        private PasswordField password;
        private PasswordField passwordAgain;

        public RegistrationFields() {
            login = new TextField("login");
            login.setRequired(true);
            login.setTabIndex(2);
            password = new PasswordField("password");
            password.setRequired(true);
            password.setTabIndex(3);
            passwordAgain = new PasswordField("retype password");
            passwordAgain.setRequired(true);
            passwordAgain.setTabIndex(4);
        }

        public TextField getLogin() {
            return login;
        }

        public void setLogin(TextField login) {
            this.login = login;
        }

        public PasswordField getPassword() {
            return password;
        }

        public void setPassword(PasswordField password) {
            this.password = password;
        }

        public PasswordField getPasswordAgain() {
            return passwordAgain;
        }

        public void setPasswordAgain(PasswordField passwordAgain) {
            this.passwordAgain = passwordAgain;
        }
    }

}
