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
package com.wcs.wcslib.vaadin.widget.recaptcha;

import java.net.URLEncoder;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.JavaScriptFunction;
import com.wcs.wcslib.vaadin.widget.recaptcha.shared.ReCaptchaOptions;
import com.wcs.wcslib.vaadin.widget.recaptcha.shared.ReCaptchaState;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import net.tanesha.recaptcha.http.SimpleHttpLoader;

/**
 * Vaadin wrapper component for ReCaptcha javascript API. recaptcha4j wrapped too for server-side validation.
 *
 * @author kumm
 */
@JavaScript("recaptcha-connector.js")
public class ReCaptcha extends AbstractJavaScriptComponent {

    public static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final long serialVersionUID = 1L;

    private String response;
    private final String privateKey;

    public ReCaptcha(String privateKey, String publicKey, ReCaptchaOptions options) {
        this(privateKey, publicKey, options, null);
    }

    public ReCaptcha(String privateKey, String publicKey, ReCaptchaOptions options, String customHtml) {
        super();
        getState().options = options != null ? options : new ReCaptchaOptions();
        getState().options.sitekey = publicKey;
        getState().customHtml = customHtml;
        addFunction("responseChanged", new JavaScriptFunction() {

            @Override
            public void call(JsonArray arguments) {
                response = arguments.getString(0);
            }
        });
        this.privateKey = privateKey;
    }

    @Override
    protected ReCaptchaState getState() {
        return (ReCaptchaState) super.getState();
    }

    /**
     * Validates the answer with server-side ReCaptcha api.
     * When the answer is empty returns false.
     * When the answer is valid, this method will return true for the first time only.
     * When the answer is invalid, you have to reload to get a new chance to pass.
     * This behavior comes from ReCaptcha.
     * Recaptcha4j used for handling the api.
     *
     * @return valid, or not
     */
    public boolean validate() {
        if (isEmpty()) {
            return false;
        }
        String remoteAddr = VaadinService.getCurrentRequest().getRemoteAddr();

        String postParameters = "secret=" + URLEncoder.encode(privateKey) + "&remoteip=" + URLEncoder.encode(remoteAddr)
                + "&response=" + URLEncoder.encode(response);

        String message = new SimpleHttpLoader().httpPost(VERIFY_URL, postParameters);
        if (message != null) {
            JsonObject parse = Json.parse(message);
            JsonValue jsonValue = parse.get("success");
            return jsonValue != null ? jsonValue.asBoolean() : false;
        }
        return false;
    }

    /**
     * Is user filled the input?
     *
     * @return is empty
     */
    public boolean isEmpty() {
        return response == null || response.isEmpty();
    }

    /**
     * Reloads the captcha.
     */
    public void reload() {
        callFunction("reload");
    }

}
