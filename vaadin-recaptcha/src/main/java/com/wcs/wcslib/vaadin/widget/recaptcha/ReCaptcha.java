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

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.JavaScriptFunction;
import com.wcs.wcslib.vaadin.widget.recaptcha.shared.ReCaptchaOptions;
import com.wcs.wcslib.vaadin.widget.recaptcha.shared.ReCaptchaState;
import elemental.json.JsonArray;

/**
 * Vaadin wrapper component for ReCaptcha javascript API.
 *
 * @author kumm
 */
@JavaScript("recaptcha-connector.js")
public class ReCaptcha extends AbstractJavaScriptComponent {

    public static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final long serialVersionUID = 1L;

    private String response;

    private final ReCaptchaValidator reCaptchaValidator;

    public ReCaptcha(String privateKey, ReCaptchaOptions options) {
        super();
        getState().options = options;
        addFunction("responseChanged", new JavaScriptFunction() {

            @Override
            public void call(JsonArray arguments) {
                response = arguments.getString(0);

            }
        });
        reCaptchaValidator = new ReCaptchaValidator(privateKey);
    }

    @Override
    protected ReCaptchaState getState() {
        return (ReCaptchaState) super.getState();
    }

    /**
     * Validates the answer with server-side ReCaptcha api. When the answer is empty returns false. When the answer is
     * valid, this method will return true for the first time only. When the answer is invalid, you have to reload to
     * get a new chance to pass. This behavior comes from ReCaptcha.
     *
     * @return valid, or not
     */
    public boolean validate() {
        if (isEmpty()) {
            return false;
        }
        return reCaptchaValidator.checkAnswer(response);
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
