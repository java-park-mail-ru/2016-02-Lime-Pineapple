'use strict';
define([
        'jquery',
        'underscore',
        'backbone',
        'views/allViews',
        'controllers/viewManager',
        'models/session'
],
    function ($, underscore, Backbone, Views, ViewManager, Session) {
        var Router = Backbone.Router.extend({
                routes: {
                    "game": "gameAction",
                    "scoreboard": "scoreboardAction",
                    "login": "loginAction",
                    "logout": "logoutAction",
                    "*default": "defaultAction"
                },

                _setTagNameViewsEl: function (view, wantTagName) {
                    if (view !== undefined && (view.$el.tagName === undefined || (wantTagName && wantTagName.isString()))) {
                            view.$el.appendTo($(wantTagName));
                    }
                },

                initialize: function () {
                    this.VM = new ViewManager(
                        Views.game,
                        Views.main,
                        Views.scoreboard,
                        Views.login
                    );
                    this._setTagNameViewsEl(Views.game, "#page__view-holder");
                    this._setTagNameViewsEl(Views.main, "#page__view-holder");
                    this._setTagNameViewsEl(Views.scoreboard, "#page__view-holder");
                    this._setTagNameViewsEl(Views.login, "#page__view-holder");
                    // this.defaultAction(); - Works without it
                },

                defaultAction: function () {
                    Views.main.show();
                },

                scoreboardAction: function () {
                    console.log("Scoreboard action showing...");
                    Views.scoreboard.show();
                },

                gameAction: function () {
                    Views.game.show();
                },

                loginAction: function () {
                    Views.login.show();
                },

                logoutAction: function() {
                    Session.logout();
                }
            });

        return new Router();
    }

);