'use strict';
define([
    'jquery',
    'backbone',
    'settings',
    'models/session',
    './baseView',
    'tmpl/login'
    ], function ($, Backbone,  Settings, Session, BaseView, tmpl) {
        var Login = BaseView.extend({
            template: tmpl,
            events: {

                'click .btn-lg-back' : function(e) {
                Backbone.history.navigate("/", true);
            },
                'submit #login-form__btn-success' : '_onSubmitEvent'
            },

            initialize: function () {
            },

            _onSubmitEvent: function (e) {
                e.preventDefault();
                console.log("[views::login::_onSubmitEvent()]: called");
                var $form = $(this),
                    login = $form.find("input[name='username']").val(),
                    password = $form.find("input[name='password']").val();

                //console.log("Sending request to: " + url + " ...");
                var reqObj = {
                    "login": login,
                    "password": password
                };
                console.log("Login: ", login, "Password: ", password, "Request object: ", reqObj);
                console.log("Request parsed as JSON: ", JSON.stringify(reqObj));
                console.log("Login: ", login, "Password: ", password, "Request object: ", reqObj);
                console.log("Request parsed as JSON: ", JSON.stringify(reqObj));
                if (Session.login(reqObj)) {
                    Backbone.trigger("loginSuccess");
                }
            }
        });
        return new Login();
    }
);
