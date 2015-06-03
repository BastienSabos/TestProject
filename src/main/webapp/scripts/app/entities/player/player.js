'use strict';

angular.module('testprojectApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('player', {
                parent: 'entity',
                url: '/player',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'testprojectApp.player.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/player/players.html',
                        controller: 'PlayerController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('player');
                        return $translate.refresh();
                    }]
                }
            })
            .state('playerDetail', {
                parent: 'entity',
                url: '/player/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'testprojectApp.player.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/player/player-detail.html',
                        controller: 'PlayerDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('player');
                        return $translate.refresh();
                    }]
                }
            });
    });
