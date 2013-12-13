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

window.com_wcs_wcslib_vaadin_widget_recaptcha_ReCaptcha =
function() {
    var element = this.getElement();

    this.onStateChange = function() {
        var state = this.getState();
        var html, elementId;
        if (state.customHtml) {
            html = state.customHtml;
            elementId = null;
        } else {
            html = '<div id="recaptcha_div"></div>';
            elementId = "recaptcha_div";
        }
        element.innerHTML = html;
        Recaptcha.create(state.publicKey, elementId, state.options);
    };
    
    this.reload = function() {
        Recaptcha.reload();
    };

    var connector = this;
    element.onchange = function() {
        connector.responseChanged(Recaptcha.get_challenge(), Recaptcha.get_response());
    };
};