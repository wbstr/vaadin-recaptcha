# ReCaptcha Add-on for Vaadin 7

Vaadin-recaptcha is a wrapper component for ReCaptcha.
See http://www.google.com/recaptcha

## Online demo

Try the add-on demo at http://demo.webstar.hu/vaadin-recaptcha

## Usage

1. Create your keys at http://www.google.com/recaptcha
2. Include recaptcha javascript ajax api in your application. 
    Simplest way is annotating your UI, or a parent component:
    ````
    @JavaScript("http://www.google.com/recaptcha/api/js/recaptcha_ajax.js")
    public class MyUI extends UI {
    ````

3. Create, and add the component to your UI
    ```
    ReCaptcha captcha = new ReCaptcha(
        your_private_key,
        your public_key,
        new ReCaptchaOptions() {{//your options
            theme = "white";
        }}
    );
    layout.addComponent(captcha);
    ```

4. Validate somewhere in an event handler
    ```
    if (!captcha.isValid()) {
        Notification.show("Invalid!", Notification.Type.ERROR_MESSAGE);
    } else {
        //do your job
    }
    ```

See [DemoUI.java](vaadin-recaptcha-demo/src/main/java/com/wcs/wcslib/vaadin/widget/recaptcha/demo/DemoUI.java)

## Documentation

For ReCaptcha options see: https://developers.google.com/recaptcha/docs/customization.
