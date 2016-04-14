'use strict';

define([
    'underscore',
    'backbone',
    'settings'
], function(_, Backbone, Settings){

    var ViewManager = Backbone.View.extend({
        initialize : function(...views) {
            this.views = views;
            Backbone.on(Settings.EVENT_VIEWMANAGER_SHOW, (view) => {
                console.log("[ViewManager::add::listenTo] Triggered!");
                this.hide(view);
                view.$el.show();
            });
        },
        hide: function (exceptThisView) {
            console.log("[ViewManager::hide] executing...");
            console.log(exceptThisView);
            _.each(this.views, function(view){
                if (view !== exceptThisView) {
                    view.hide();
                }
            });
        },
        addViews: function(...views) {
            this.views.concat(views);
        }

    });
    return ViewManager;

});