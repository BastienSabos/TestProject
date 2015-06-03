'use strict';

angular.module('testprojectApp')
    .controller('PlayerDetailController', function ($scope, $stateParams, Player) {
        $scope.player = {};
        $scope.load = function (id) {
            Player.get({id: id}, function(result) {
              $scope.player = result;
            });
        };
        $scope.load($stateParams.id);
    });
