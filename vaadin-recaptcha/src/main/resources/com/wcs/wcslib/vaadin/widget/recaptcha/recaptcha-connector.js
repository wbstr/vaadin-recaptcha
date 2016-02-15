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
	var connector = this;
	
	this.onStateChange = function() {
		this.stateChangedDuringLoad = true;
	}
	
    var init = function(){
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
	        
	        //Add callback to grecaptcha options to force server update on user input
	        state.options['callback'] = function(){
	        	connector.responseChanged(grecaptcha.getResponse());
        	};
        	
        	//Add expired-callback to reload component automatically
        	state.options['expired-callback'] = this.reload;
        	
	        grecaptcha.render(elementId, state.options);
	    };
	    
	    this.reload = function() {
	    	grecaptcha.reset();
	    };

    }.bind(this);
    
    //Global scope to enable call outside this script.
    recaptchaVaadinOnLoadCallBack = function(){
    	init();
    	//Vaadin may update the component while recaptcha is loading in parallel. We will init the component if that happened
    	if(this.stateChangedDuringLoad){
    		this.onStateChange();
    	};
    }.bind(this);
    
    //We are checking if recaptcha is already on the window context. We will either init the component or load recaptcha
    if(window['grecaptcha']){
    	init();
    }
    else{
    	var recaptchaImport = document.createElement('script');
    	//We are passing the "onload" parameter to enable the component rendering after recaptcha has been loaded
    	recaptchaImport.src = 'https://www.google.com/recaptcha/api.js?onload=recaptchaVaadinOnLoadCallBack&render=explicit';
    	document.getElementsByTagName('head')[0].appendChild(recaptchaImport);
    }
};