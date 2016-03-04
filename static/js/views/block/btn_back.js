/**
 * Created by leegheid on 01.03.16.
 */
define([
    'jquery',
    'backbone',
    'tmpl/btn_back'
], function(
    $,
    Backbone,
    tmpl
){

    return Backbone.View.extend({
        template: tmpl,
        className: "js-btn_back",
        events: {
            "click": 'clickBtn'
        },

        initialize: function () {

        },

        clickBtn: function() {
            console.log("click on back");
            Backbone.history.history.back();
        },

        show: function () {
            console.log("i am here, in btn.show()");
            console.log(this);
            console.log(this.$el);
            this.$el.show();
        },


        hide: function () {
            this.$el.hide();
        },

        render: function () {
            console.log("btn_back.render was called");
            console.log(this.$el);
            this.$el.html(this.template({}));
            return this;
        }

    });
    //return View;
});