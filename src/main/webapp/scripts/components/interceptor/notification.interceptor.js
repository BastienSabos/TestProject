 'use strict';

angular.module('testProjectApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-testProjectApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-testProjectApp-params')});
                }
                return response;
            }
        };
    });
