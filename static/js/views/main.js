define([
    'jquery',
    'underscore',
    'backbone',
    'tmpl/main_page'
], function(
    $,
    _,
    Backbone,
    tmpl
){
    return Backbone.View.extend({
        className: "view__main",
        template: tmpl,
        //el: $(".view__main"), // DOM элемент widget'а

        initialize: function () {
        },

        show: function () {
            console.log("main.show()");
            this.$el.show();
            console.log(this.$el);
        },

        //remove: function() {
        //    this.$el.empty().off(); /* off to unbind the events */
        //    this.stopListening();
        //    return this;
        //},

        hide: function () {
            //this.remove();
            this.$el.hide();
        },

        render: function () {
            //this.$el.appendTo($("#view__holder"));
            console.log("main.show.render()");
            this.$el.html(this.template({}));


            return this;
        }
    });
});
