'use strict';

function _newArrowCheck(innerThis, boundThis) { if (innerThis !== boundThis) { throw new TypeError("Cannot instantiate an arrow function"); } }

define(['underscore', 'backbone', 'settings'], function (_, Backbone, Settings) {

    var ViewManager = Backbone.View.extend({
        initialize: function initialize() {
            var _this = this;
            var _len, views, _key;
            for (_len = arguments.length, views = Array(_len), _key = 0; _key < _len; _key++) {
                views[_key] = arguments[_key];
            }

            this.views = views;
            Backbone.on(Settings.EVENT_VIEWMANAGER_SHOW, function (view) {
                _newArrowCheck(this, _this);

                console.log("[ViewManager::add::listenTo] Triggered!");
                this.hide(view);
                view.$el.show();
            }.bind(this));
        },
        hide: function hide(exceptThisView) {
            console.log("[ViewManager::hide] executing...");
            console.log(exceptThisView);
            _.each(this.views, function (view) {
                if (view !== exceptThisView) {
                    view.hide();
                }
            });
        },
        addViews: function addViews() {
            var _len2, views, _key2;
            for (_len2 = arguments.length, views = Array(_len2), _key2 = 0; _key2 < _len2; _key2++) {
                views[_key2] = arguments[_key2];
            }

            this.views.concat(views);
        }

    });
    return ViewManager;
});
