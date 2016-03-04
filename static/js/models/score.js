define(['backbone'], function(Backbone){

    var PlayerModel = Backbone.Model.extend({
        defaults : function() {
            return{
                "name": "",
                "score": "0"
            };
        }
    });

    return PlayerModel;
});