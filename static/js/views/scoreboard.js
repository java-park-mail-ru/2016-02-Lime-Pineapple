define([
    'jquery',
    'backbone',
    'tmpl/scoreboard',
    'collections/scores'
], function(
    $,
    Backbone,
    tmpl,
    Scores
){

    return Backbone.View.extend({
        template: tmpl,

        events: {
            "initView": 'render',
            "show": 'show'
        },

        initialize: function () {
            this.collection = new Scores();
            var i;
            for (i = 0; i < 3; i++) {
                this.collection.add({name: "Тим", score: 55});
                this.collection.add({name: "Ида", score: 6});
                this.collection.add({name: "Роб", score: 545});
            }
            this.collection.sort();
        },

        show: function () {
            this.$el.show();
        },
        hide: function () {
            this.$el.hide();
        },

        render: function () {
            console.log("[views::scoreboard::render()]: called");
            console.log(this.$el);
            console.log(this.collection);
            this.$el.html(this.template({person: this.collection.toJSON()}));

            return this;
        }

    });
    //return View;
});