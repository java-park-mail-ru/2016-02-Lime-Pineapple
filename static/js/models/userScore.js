"use strict";

function _newArrowCheck(innerThis, boundThis) { if (innerThis !== boundThis) { throw new TypeError("Cannot instantiate an arrow function"); } }

define(['backbone'], function (Backbone) {
    var UserScore = Backbone.Model.extend({
        defaults: {
            linkedUser: null,
            username: null,
            score: 0
        },

        initialize: function initialize() {
            var _this = this;

            console.log("[UserScore::initialize] initalizing...");
            this.on('increment', function (inc) {
                _newArrowCheck(this, _this);

                console.log("[ CATCHED THAT BITCH EVENT!!]");
                this.increment(inc);
            }.bind(this));
        },

        increment: function increment(count) {
            this.set('score', this.get('score') + count);
        },

        getUserByUsername: function getUserByUsername() {
            throw new SyntaxError("Not implemented");
        }

    });
    return UserScore;
});
