"use strict";
define([
        'backbone'
    ],
    function (Backbone) {
        var UserScore = Backbone.Model.extend(
            {
                defaults: {
                    linkedUser: null,
                    username: null,
                    score: 0
                },


                initialize: function () {
                    console.log("[UserScore::initialize] initalizing...");
                    this.on('increment', inc => {
                        console.log("[ CATCHED THAT BITCH EVENT!!]");
                        this.increment(inc);

                    });
                },

                increment: function (count) {
                    this.set('score', this.get('score')+count);
                },

                getUserByUsername: function () {
                    throw new SyntaxError("Not implemented");
                }



            }
        );
        return UserScore;
    }
);