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
import elemental.json.Json;
import elemental.json.JsonObject;
import elemental.json.JsonValue;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author Juhasz Gergely
 */
public class ReCaptchaValidator {

    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    private final String privateKey;

    public ReCaptchaValidator(String privateKey) {
        this.privateKey = privateKey;
    }

    public boolean checkAnswer(String response) {
        String remoteAddr = VaadinService.getCurrentRequest().getRemoteAddr();

        String postParameters;
        try {
            postParameters = "secret=" + URLEncoder.encode(privateKey, "UTF-8")
                    + "&remoteip=" + URLEncoder.encode(remoteAddr, "UTF-8")
                    + "&response=" + URLEncoder.encode(response, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ReCaptchaException("Cannot encode answer.", e);
        }

        String message;
        try {
            message = httpPost(VERIFY_URL, postParameters);
        } catch (IOException e) {
            throw new ReCaptchaException("Cannot load URL: " + e.getMessage(), e);
        }

        JsonObject parse = Json.parse(message);
        JsonValue jsonValue = parse.get("success");
        return jsonValue != null && jsonValue.asBoolean();
    }

    private String httpPost(String urlStr, String postdata) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(10000);
        connection.setUseCaches(false);

        try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
            writer.write(postdata);
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
        }

        return response.toString();
    }
}
