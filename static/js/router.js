define([
    'jquery',
    'underscore',
    'backbone',
    'views/all_views'
], function ($, underscore, Backbone, Views) {
    var Router = Backbone.Router.extend({
            routes: {
                "game": "gameAction",
                "scoreboard": "scoreboardAction",
                "login": "loginAction",
                "*default": "defaultAction"
            },

            _createView: function (ViewClass, viewName) {
                var view = this.views[viewName];
                if (this.views[viewName] === undefined) {
                    view = this.views[viewName] = new ViewClass();
                    view.render();
                }
                return view;
            },

            _allHide: function () {
                var key;
                for (key in this.views) {
                    if (this.views[key]) {
                        this.views[key].hide();
                    }
                }
            },

            _setTagNameViewsEl: function (view, wantTagName) {
                if (this.views[view] !== undefined && (this.views[view].$el.tagName === undefined || (wantTagName && wantTagName.isString()))) {
                    this.views[view].$el.appendTo($(wantTagName));
                }
            },

            initialize: function () {
                this.views = {};
                console.log("initialize in router");
                this._createView(Views.btnBack, "btnBack");
                // this["btn_back"] ==== this.btn_back !=== this."btn_back"
                this.defaultAction();
            },

            defaultAction: function () {
                this._createView(Views.main, "main");
                this._allHide();

                this._setTagNameViewsEl("main", "#view__holder");
                console.log(this.views.main + "fsfsd");

                this.views.main.show();

                console.log(this.views.main.$el, "defaultAction, changed to main");
            },

            scoreboardAction: function () {
                this._allHide();
                this._createView(Views.scoreboard, "scoreboard");

                this._setTagNameViewsEl("scoreboard", "#view__holder");
                this._setTagNameViewsEl("btnBack", "#view__btn_back");

                this.views.scoreboard.show();
                this.views.btnBack.show();

                console.log("scoreboardAction, Changed to scoreboard");
            },

            gameAction: function () {
                this._allHide();
                this._createView(Views.gameAction, "game");

                this._setTagNameViewsEl("game", "#view__holder");
                this._setTagNameViewsEl("btnBack", "#view__btn_back");

                this.views.game.show();
                this.views.btnBack.show();

                console.log(this.views.game.$el, "gameAction, changed to game");
            },

            loginAction: function () {
                this._allHide();
                this._createView(Views.login, "login");

                this._setTagNameViewsEl("login", "#view__holder");
                this._setTagNameViewsEl("btnBack", "#view__btn_back");

                this.views.login.show();
                this.views.btnBack.show();

                console.log(this.views.login.$el + "loginAction, changed to login");
            }

        });
        return new Router();
    }

);