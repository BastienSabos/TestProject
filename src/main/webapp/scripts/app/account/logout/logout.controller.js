'use strict';

angular.module('testprojectApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
