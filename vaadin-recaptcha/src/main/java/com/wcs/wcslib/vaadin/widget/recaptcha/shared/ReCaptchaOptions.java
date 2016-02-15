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
package com.wcs.wcslib.vaadin.widget.recaptcha.shared;

import java.io.Serializable;

/**
 * ReCaptcha options.
 * 
 * Property names are same as javascript property names.
 * See here: https://developers.google.com/recaptcha/docs/customization
 * 
 * @author kumm
 */
public class ReCaptchaOptions implements Serializable {

    public String sitekey;
    public String lang;
    public String theme;
    public String custom_theme_widget;
    public int tabindex;
    public CustomTranslations custom_translations;

    public static class CustomTranslations implements Serializable {

        public String instructions_visual;
        public String instructions_audio;
        public String play_again;
        public String cant_hear_this;
        public String visual_challenge;
        public String audio_challenge;
        public String refresh_btn;
        public String help_btn;
        public String incorrect_try_again;
    }
}
