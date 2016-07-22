/*
 * Copyright 2016 Webstar Csoport.
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
package com.wcs.wcslib.vaadin.widget.recaptcha;

import com.vaadin.server.VaadinService;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Juhasz Gergely
 */
public class ReCaptchaValidator {

    public static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    private final String privateKey;

    public ReCaptchaValidator(String privateKey) {
        this.privateKey = privateKey;
    }

    public boolean checkAnswer(String response) {
        try {
            String remoteAddr = VaadinService.getCurrentRequest().getRemoteAddr();

            String postParameters = "secret=" + URLEncoder.encode(privateKey, "UTF-8")
                    + "&remoteip=" + URLEncoder.encode(remoteAddr, "UTF-8")
                    + "&response=" + URLEncoder.encode(response, "UTF-8");

            String message = new SimpleHttpLoader().httpPost(VERIFY_URL, postParameters);
            if (message != null) {
                JSONObject jsonObject = new JSONObject(message);
                return jsonObject.getBoolean("success");
            }
            return false;
        } catch (UnsupportedEncodingException ex) {
            throw new ReCaptchaException("Cannot encode answer.", ex);
        } catch (JSONException ex) {
            throw new ReCaptchaException("Cannot parse json.", ex);
        }
    }
}
