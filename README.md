# ReCaptcha Add-on for Vaadin 7

Vaadin-recaptcha is a wrapper component for ReCaptcha.
See http://www.google.com/recaptcha

## Online demo

Try the add-on demo at http://demo.webstar.hu/vaadin-recaptcha

## Usage

1. Create your keys at http://www.google.com/recaptcha
2. (Optional) Include recaptcha javascript ajax api in your application.
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
    if (!captcha.validate()) {
        Notification.show("Invalid!", Notification.Type.ERROR_MESSAGE);
        captcha.reload();
    } else {
        //do your job
    }
    ```

See [DummyRegWithReCaptcha.java](vaadin-recaptcha-demo/src/main/java/com/wcs/wcslib/vaadin/widget/recaptcha/demo/DummyRegWithReCaptcha.java)

## Documentation

For ReCaptcha options see: https://developers.google.com/recaptcha/docs/display.

## Known Issues and limitations

1. The racaptha api renders with javascript using ajax after page load. It means the component becames visible after everything is rendered, with flickering, and layout repaint. To avoid this you can fix the size from css, or put the component into a fixed size parent.

2. The javascript api does not support more recaptcha on one page.

3. The javascript api does not suport changing options (like theme, or language) on the same page. Once a component is rendered, all other components inherit the options, until page reload.
