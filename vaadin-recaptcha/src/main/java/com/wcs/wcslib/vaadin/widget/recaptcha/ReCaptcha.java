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

import com.wcs.wcslib.vaadin.widget.recaptcha.shared.ReCaptchaOptions;
import com.vaadin.annotations.JavaScript;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.JavaScriptFunction;
import com.wcs.wcslib.vaadin.widget.recaptcha.shared.ReCaptchaState;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Vaadin wrapper component for ReCaptcha javascript API.
 * recaptcha4j wrapped too for server-side validation.
 * 
 * @author kumm
 */
@JavaScript("recaptcha-connector.js")
public class ReCaptcha extends AbstractJavaScriptComponent {

    private String challenge;
    private String response;
    private final String privateKey;

    public ReCaptcha(String privateKey, String publicKey, ReCaptchaOptions options) {
        this(privateKey, publicKey, options, null);
    }
    
    public ReCaptcha(String privateKey, String publicKey, ReCaptchaOptions options, String customHtml) {
        super();
        this.privateKey = privateKey;
        getState().publicKey = publicKey;
        getState().options = options;
        getState().customHtml = customHtml;
        addFunction("responseChanged", new JavaScriptFunction() {
            @Override
            public void call(JSONArray arguments) throws JSONException {
                challenge = arguments.getString(0);
                response = arguments.getString(1);
            }
        });
    }

    @Override
    protected ReCaptchaState getState() {
        return (ReCaptchaState) super.getState();
    }

    public boolean isValid() {
        if (challenge == null || response==null) {
            return false;
        }
        String remoteAddr = VaadinService.getCurrentRequest().getRemoteAddr();
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey(privateKey);
        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, response);
        //incorrect-captcha-sol
        return reCaptchaResponse.isValid();
    }

}
