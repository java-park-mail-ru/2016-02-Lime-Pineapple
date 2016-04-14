'use strict';
define([
    'jquery',
    'underscore',
    'backbone',
    'settings',
    'pixi',
    './baseView',
    'tmpl/game'
], function(
    $,
    _,
    Backbone,
    Settings,
    pixi,
    BaseView,
    tmpl
){
    var Game = BaseView.extend({
        template: tmpl,

        defaults:{
            'renderer': "null",
            'stage':    "null",
            containers: {
                container:                      null,
                containerDistantFighting:       null,
                containerInfighting:            null,
                containerEnemyInfighting:       null,
                containerEnemyDistantFighting:  null,
                containerEnemy:                 null
            }
        },

        initialize: function(){
            BaseView.prototype.initialize.call(this);
            this.stage = new pixi.Container();
            this.containers = this.defaults.containers;

            for (var key in this.containers){
                this.containers[key] = new pixi.Container();
            }
            console.log(this.containers);
            this.containers.containerInfighting.interactive = true;
            this.containers.containerInfighting.buttonMode = true;
            this.containers.containerDistantFighting.interactive = true;
            this.containers.containerDistantFighting.buttonMode = true;

            for(var key in this.containers){
                this.stage.addChild(this.containers[key]);
            }
            this.oneLineHeight = $(window).height()/6;
            this.widht = $(window).width();

            this.setContainerPosition();

            let self = this;
            this.containers.containerInfighting
                .on('mousedown', function(event){
                    self.onClickBattleField(event, self.containers.containerInfighting);
                });

            this.containers.containerDistantFighting
                .on('mousedown', function(event){
                    self.onClickBattleField(event, self.containers.containerDistantFighting);

                });

            window.addEventListener('resize', function() {
                self.resize(self);
            });


            this.createDeck(this.containers.container);
            this.createDeck(this.containers.containerEnemy);

        },

        //don't work
        setContainerPosition: function(){
            this.oneLineHeight = $(window).height()/6;
            this.widht = $(window).width();
            this.renderer = pixi.autoDetectRenderer($("#game_window").width()/1.2, $("#game_window").height());
            let i = 4;
            for (var key in this.containers){
                this.containers[key].y = 0;
                this.containers[key].x = 0;
                this.containers[key].y = i * this.oneLineHeight;
                --i;
            }

            this.containers.containerInfighting.hitArea = new pixi.Rectangle(0, 0, this.widht / 1.5, this.oneLineHeight);
            this.containers.containerDistantFighting.hitArea = new pixi.Rectangle(0, 0, this.widht / 1.5, this.oneLineHeight);
        },

        show: function () {
            BaseView.prototype.show.call(this);
            let self = this;
            this.intervalID = setInterval(function() {
                self.animate(self);
            }, 100);
            $("#page__site-header").addClass("topped");
        },

        hide: function() {
            BaseView.prototype.hide.call(this);
            clearInterval(this.intervalID);
            $("#page__site-header").removeClass("topped");
        },

        resize: function(self){
            self.setContainerPosition();
        },


        render: function () {
            BaseView.prototype.render.call(this);
            this.renderer = pixi.autoDetectRenderer($("#game_window").width()/1.2, $("#game_window").height(), {transparent: true});
            document.getElementById("game_window").appendChild(this.renderer.view);
        },

        createDeck: function(container) {
            let self = this;
            for (let i = 0; i < 8; i++) {

                let card = new pixi.Sprite.fromImage('static/resources/card' + (Math.floor(Math.random() * (8 - 1 + 1)) + 1) + '.png');
                card.interactive = true;
                card.buttonMode = true;
                card.width = this.oneLineHeight - this.oneLineHeight/6;
                card.height = this.oneLineHeight;
                card.x = card.width * i + 2 + card.width/2;
                card.y = card.y + card.height/2;
                card.anchor.set(0.5);
                card
                    .on('click', function(event) {
                        self.onClickCard(event, container, card);
                    })
                    .on('touchstart', function(event) {
                        self.onClickCard(event, container, card)
                    });
                container.addChild(card);
            }
        },

        removeGapsInDeck: function(container) {
            let wid;
            console.log(container.children.length);
            if (container.children.length){
                wid = container.getChildAt(0).width;
            }
            for (let i = 0; i < container.children.length; i++){
                container.getChildAt(i).x = wid * i + 2 + wid/2;
            }
        },

        animate: function (self) {
            console.log("i am here");
            if (!self.containers.container.children.length){
                self.containers = {};
                self.renderer = null;
                self.stage = null;
                self.initialize();
                self.hide();
                self.render();
                self.show();
            }
            self.renderer.render(self.stage);
        },

        AImove: function() {
            if (this.containers.containerEnemy.children.length) {
                var card = this.containers.containerEnemy.getChildAt((Math.floor(Math.random() * (this.containers.containerEnemy.children.length))));
                this.containers.containerEnemy.removeChild(card);
                let r = Math.floor(Math.random() * (2) + 1);
                if (r == 1){
                    this.containers.containerEnemyDistantFighting.addChild(card);
                    this.removeGapsInDeck(this.containers.containerEnemyDistantFighting);
                }
                else{
                    this.containers.containerEnemyInfighting.addChild(card);
                    this.removeGapsInDeck(this.containers.containerEnemyInfighting);
                }
            }
        },


        onClickBattleField: function(event, container){
            console.log("in onClickBattleField");
            if (this.infoCard && this.infoCard.renderable){
                var card = new pixi.Sprite(this.infoCard.texture);

                card.width = this.infoCard.width / 2.5;
                card.height = this.infoCard.height / 2.5;
                card.anchor.set(0);

                card.x = container.children.length * card.width + 2;
                card.y = 0;

                container.addChild(card);
                this.infoCard.renderable = false;
                for (var i = 0; i < this.containers.container.children.length; i++){
                    this.containers.container.children[i].interactive = true;
                }
                this.containers.container.removeChild(this.infoCard.card);
                this.removeGapsInDeck(this.containers.container);
                this.AImove();
            }
        },

        onClickCard: function(event, container, card) {
            console.log(event.data.originalEvent.which);
            switch (event.data.originalEvent.which) {
                case 1:
                    if (card.alpha == 0.5){
                        card.alpha = 1;
                        this.infoCard.renderable = false;
                        for (var i = 0; i < container.children.length; i++){
                            container.children[i].interactive = true;
                        }
                    }
                    else {
                        this.infoCard = new pixi.Sprite(card.texture);

                        for (var i = 0; i < container.children.length; i++){
                            container.children[i].interactive = false;
                        }
                        card.interactive = true;
                        card.alpha = 0.5;
                        this.infoCard.width = card.width * 2.5;
                        this.infoCard.height = card.height * 2.5;
                        this.infoCard.anchor.set(0.5);
                        this.infoCard.x = this.renderer.width - this.infoCard.width / 2;
                        this.infoCard.y = this.infoCard.height / 2;
                        this.infoCard.card = card;
                        this.infoCard.renderable = true;
                        this.stage.addChild(this.infoCard);

                    }
                    break;
            }
        }

});
    return new Game();
});
