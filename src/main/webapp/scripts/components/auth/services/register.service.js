'use strict';

angular.module('testprojectApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


