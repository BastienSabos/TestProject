'use strict';

angular.module('testprojectApp')
    .controller('PlayerController', function ($scope, Player) {
        $scope.players = [];
        $scope.loadAll = function() {
            Player.query(function(result) {
               $scope.players = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Player.update($scope.player,
                function () {
                    $scope.loadAll();
                    $('#savePlayerModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Player.get({id: id}, function(result) {
                $scope.player = result;
                $('#savePlayerModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Player.get({id: id}, function(result) {
                $scope.player = result;
                $('#deletePlayerConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Player.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePlayerConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.player = {identifier: null, name: null, id: null};
        };
    });
