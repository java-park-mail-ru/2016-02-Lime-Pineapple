'use strict';

define([
    'underscore',
    'jquery',
    'backbone',
    './user',
    'settings'
    ],
    function (_, $, Backbone, User, Settings) {
        return Backbone.Model.extend({
            urlRoot: '/session',
            defaults: {
                user: null,
                user_id: 0
            },
            initialize: function () {
                //UsersManager.attachEvent(this);
                this.user = new UserModel();
                this.trigger("createUser", this.user);
                console.log("[Session::initialize()]: begin to create" );
            },

            updateSessionUser: function updateSessionUser(userData) {
                this.user.set(_.pick(userData, _.keys(this.user.defaults)));
            },

            checkAuth: function() {
                console.log("[Session::checkAuth()]: before start");
                var self = this;
                //this.fetch({
                //    success: function(mod, res){
                //        if(!res.error && res.user){
                //            self.updateSessionUser(res.user);
                //            self.set({ logged_in : true });
                //        } else {
                //            self.set({ logged_in : false });
                //            this.trigger("loginAction");
                //        }
                //    }, error:function(mod, res){
                //        self.set({ logged_in : false });
                //    }
                //}).complete( function(){
                //});
                if (!self.user.logged_in) {
                    console.log("[Session::checkAuth()]: before start");
                    //this.trigger("#loginAction");
                    Backbone.history.navigate("login", true);
                }
            },


            login: function login(opts) {
                console.log("session login func" + this.user.url());
                var self = this;
                this.user.save({ login: opts.login, password: opts.password, logged_in: true }, {
                    success: function success(model, res) {
                        console.log("SUCCESS");
                        self.user.set({ logged_in: true, login: opts.login });
                        Backbone.history.history.back();
                        return true;
                    },
                    error: function error(model, res) {
                        console.log("NOTSUCCESS");
                        Backbone.history.history.back();
                        return true; // must be false, when front will be use backend

                    }
                });
            },

            logout: function logout() {
                var self = this;
                this.user.save({ login: this.user.login }, {
                    success: function success(model, res) {
                        self.user.set({ logged_in: false, login: "" });
                        Backbone.history.history.back();
                        return true;
                    },
                    error: function error(model, res) {
                        Backbone.history.history.back();
                    }
                });
            },

            signup: function signup(opts) {}

        });
    }
);
