'use strict';

angular.module('testProjectApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


