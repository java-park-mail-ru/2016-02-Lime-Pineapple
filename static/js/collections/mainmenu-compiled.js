/**
 * Created by Raaw on 27-Feb-16.
 */

/*global define */
define(['underscore', 'backbone',
//'backboneLocalstorage',
'models/button'], function (_, Backbone, Store, Todo) {
    'use strict';

    var MainMenuButtonsCollection = Backbone.Collection.extend({
        // Reference to this collection's model.
        model: Button,

        // Save all of the todo items under this example's namespace.
        //localStorage: new Store('todos-backbone'),

        // Filter down the list of all todo items that are finished.
        backButton: function () {
            return this.where({ name: 'back' });
        },

        // Filter down the list to only todo items that are still not finished.
        defaultButton: function () {
            var collection = this.where({ default: true });
            if (collection.length() > 1) throw new EvalError("Default button needs to be set only once");
            return collection;
        }
    });

    return new MainMenuButtonsCollection();
});

//# sourceMappingURL=mainmenu-compiled.js.map