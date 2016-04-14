'use strict';
define([
    'underscore',
    'jquery',
    'backbone',
    './user',
    'settings'
], function (_, $, Backbone, User, Settings) {
    return Backbone.Model.extend( {
        urlRoot: '/session',
        defaults: {
            user: null,
            user_id: 0
        },
        initialize: function () {
            //UsersManager.attachEvent(this);
            console.log("[Session::initialize()]: begin to create" );
        },

        updateSessionUser: function( userData ){
            this.user.set(_.pick(userData, _.keys(this.user.defaults)));
        },


        login: function(opts){
            console.log("session login func" + this.user.url());
            var self = this;
            this.user.save({login: opts.login, password: opts.password, logged_in: true}, {
                success: function(model, res){
                    console.log("SUCCESS");
                    self.user.set({logged_in: true, login: opts.login});
                    Backbone.history.history.back();
                    return true;
                },
                error: function(model, res){
                    console.log("NOTSUCCESS");
                    Backbone.history.history.back();
                    return true; // must be false, when front will be use backend
                }
            });
        },

        logout: function(){
            var self = this;
            this.user.save({login: this.user.login}, {
                success: function(model, res){
                    self.user.set({logged_in: false, login: ""});
                    Backbone.history.history.back();
                    return true;
                },
                error: function(model, res){
                    Backbone.history.history.back();
                }
            });
        },

        signup: function(opts){
        },


    });
});