package com.wcs.wcslib.vaadin.widget.recaptcha;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class SimpleHttpLoader {

    public String httpGet(String urlS) {
        InputStream in = null;
        URLConnection connection = null;
        try {
            URL url = new URL(urlS);
            connection = url.openConnection();

            setTimeouts(connection);

            in = connection.getInputStream();

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            while (true) {
                int rc = in.read(buf);
                if (rc <= 0) {
                    break;
                } else {
                    bout.write(buf, 0, rc);
                }
            }

            // return the generated javascript.
            return bout.toString();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load URL: " + e.getMessage(), e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                // swallow.
            }
        }
    }

    public String httpPost(String urlS, String postdata) {
        InputStream in = null;
        URLConnection connection = null;
        try {
            URL url = new URL(urlS);
            connection = url.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);

            setTimeouts(connection);

            OutputStream out = connection.getOutputStream();
            out.write(postdata.getBytes());
            out.flush();

            in = connection.getInputStream();

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            while (true) {
                int rc = in.read(buf);
                if (rc <= 0) {
                    break;
                } else {
                    bout.write(buf, 0, rc);
                }
            }

            out.close();
            in.close();

            // return the generated javascript.
            return bout.toString();
        } catch (IOException e) {
            throw new ReCaptchaException("Cannot load URL: " + e.getMessage(), e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                // swallow.
            }
        }
    }

    private void setTimeouts(URLConnection connection) {
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(10000);
    }

}
