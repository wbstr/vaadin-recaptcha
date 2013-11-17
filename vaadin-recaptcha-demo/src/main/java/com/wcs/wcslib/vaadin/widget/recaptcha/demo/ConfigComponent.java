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

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.Arrays;
import java.util.Collection;
import com.wcs.wcslib.vaadin.widget.recaptcha.shared.ReCaptchaOptions.CustomTranslations;

/**
 *
 * @author kumm
 */
public class ConfigComponent extends CustomComponent {

    private HorizontalLayout compositionRoot;
    private NativeSelect themeSelect;
    private TextArea customHtmlArea;
    private TextField langField;
    private TextField customThemeWidgetField;
    private CheckBox useTranslations;
    private CustomTranslations translations;
    private static final String CUSTOM_THEME_WIDGET = "recaptcha_widget_custom";
    private static final String CUSTOM_HTML_SAMPLE = "<div id=\""+CUSTOM_THEME_WIDGET+"\">\n"
            + " My Custom HTML\n"
            + " <div id=\"recaptcha_image\"></div>\n"
            + " <span class=\"recaptcha_only_if_image\">Enter the words above:</span>\n"
            + " <input type=\"text\" id=\"recaptcha_response_field\"/>\n"
            + " <div><a href=\"javascript:Recaptcha.reload()\">Get another CAPTCHA</a></div>\n"
            + "</div>";

    public ConfigComponent() {
        compositionRoot = new HorizontalLayout();
        compositionRoot.setSpacing(true);
        setCompositionRoot(compositionRoot);
        buildComponent();
    }

    public String getTheme() {
        return (String) themeSelect.getValue();
    }

    public String getLang() {
        return langField.getValue();
    }

    public String getCustomHtml() {
        return isCustomTheme() ? customHtmlArea.getValue() : null;
    }

    public String getCustomThemeWidget() {
        return isCustomTheme() ? customThemeWidgetField.getValue() : null;
    }

    public boolean isCustomTheme() {
        return "custom".equals(getTheme());
    }

    public CustomTranslations getCustomTranslations() {
        return useTranslations.getValue() ? translations : null;
    }

    private void buildComponent() {
        compositionRoot.addComponent(createThemeconfLayout());
        compositionRoot.addComponent(createLangconfLayout());
    }

    private Layout createThemeconfLayout() {
        Layout themeLayout = new FormLayout();
        themeSelect = new NativeSelect("theme", Arrays.asList("red", "white", "blackglass", "clean", "custom"));
        themeSelect.setImmediate(true);
        themeSelect.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                customHtmlArea.setEnabled(isCustomTheme());
                customThemeWidgetField.setEnabled(isCustomTheme());
            }
        });
        themeLayout.addComponent(themeSelect);
        customHtmlArea = new TextArea("custom HTML", CUSTOM_HTML_SAMPLE);
        customHtmlArea.setEnabled(false);
        customHtmlArea.setRows(15);
        customHtmlArea.setColumns(40);
        themeLayout.addComponent(customHtmlArea);
        customThemeWidgetField = new TextField("custom_theme_widget",CUSTOM_THEME_WIDGET);
        customThemeWidgetField.setEnabled(false);
        themeLayout.addComponent(customThemeWidgetField);
        return themeLayout;
    }

    private Layout createLangconfLayout() throws FieldGroup.BindException {
        VerticalLayout langLayout = new VerticalLayout();
        langField = new TextField("lang");
        langLayout.addComponent(new FormLayout(langField));
        final FormLayout translationsLayout = new FormLayout();
        translationsLayout.setSpacing(false);
        useTranslations = new CheckBox("use translations below");
        langLayout.addComponent(useTranslations);
        useTranslations.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Boolean checked = useTranslations.getValue();
                for (Component c : translationsLayout) {
                    c.setEnabled(checked);
                }
            }
        });
        translations = new CustomTranslationsBean();
        BeanFieldGroup<CustomTranslationsBean> translationsFieldGroup
                = new BeanFieldGroup<CustomTranslationsBean>(CustomTranslationsBean.class);
        translationsFieldGroup.setItemDataSource((CustomTranslationsBean) translations);
        Collection<Object> propertyIds = translationsFieldGroup.getUnboundPropertyIds();
        translationsFieldGroup.setBuffered(false);
        for (Object property : propertyIds) {
            Field<?> field = translationsFieldGroup.buildAndBind(property);
            ((TextField) field).setNullRepresentation("");
            field.setCaption(field.getCaption().toLowerCase());
            field.setEnabled(false);
            translationsLayout.addComponent(field);
        }
        langLayout.addComponent(translationsLayout);
        return langLayout;
    }

    /**
     * Bean for FieldBeanGroup
     */
    public static class CustomTranslationsBean extends CustomTranslations {

        public String getInstructions_visual() {
            return instructions_visual;
        }

        public void setInstructions_visual(String instructions_visual) {
            this.instructions_visual = instructions_visual;
        }

        public String getInstructions_audio() {
            return instructions_audio;
        }

        public void setInstructions_audio(String instructions_audio) {
            this.instructions_audio = instructions_audio;
        }

        public String getPlay_again() {
            return play_again;
        }

        public void setPlay_again(String play_again) {
            this.play_again = play_again;
        }

        public String getCant_hear_this() {
            return cant_hear_this;
        }

        public void setCant_hear_this(String cant_hear_this) {
            this.cant_hear_this = cant_hear_this;
        }

        public String getVisual_challenge() {
            return visual_challenge;
        }

        public void setVisual_challenge(String visual_challenge) {
            this.visual_challenge = visual_challenge;
        }

        public String getAudio_challenge() {
            return audio_challenge;
        }

        public void setAudio_challenge(String audio_challenge) {
            this.audio_challenge = audio_challenge;
        }

        public String getRefresh_btn() {
            return refresh_btn;
        }

        public void setRefresh_btn(String refresh_btn) {
            this.refresh_btn = refresh_btn;
        }

        public String getHelp_btn() {
            return help_btn;
        }

        public void setHelp_btn(String help_btn) {
            this.help_btn = help_btn;
        }

        public String getIncorrect_try_again() {
            return incorrect_try_again;
        }

        public void setIncorrect_try_again(String incorrect_try_again) {
            this.incorrect_try_again = incorrect_try_again;
        }

    }
}
