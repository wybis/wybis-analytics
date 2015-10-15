appFilters.filter('capitalizeFilter', function () {
    return function (input) {
        input = _.capitalize(input);
        return input;
    };
});

appDirectives.directive('wydFocusOn', function () {
    return function (scope, elem, attr) {
        return scope.$on('wydFocusOn', function (e, name) {
            if (name === attr.wydFocusOn) {
                return elem[0].focus();
            }
        });
    };
});

function wydFocusService($rootScope, $timeout) {
    return function (name) {
        return $timeout(function () {
            return $rootScope.$broadcast('wydFocusOn', name);
        });
    };
}
appServices.factory('wydFocusService', ['$rootScope', '$timeout', wydFocusService]);

function generalHttpInterceptor($log, $rootScope, $q, $window) {
    return {
        'request': function (config) {
            var xUserId = $rootScope.xUserId || 'null';
            $log.info('xUserId = ' + xUserId);
            config.headers['X-UserId'] = xUserId;
            return config;
        },

        'requestError': function (rejection) {
            $log.error(rejection);
            return rejection;
        },

        'response': function (response) {
            return response;
        },

        'responseError': function (rejection) {
            $log.error(rejection);
            if (rejection.status == 419) {
                // $window.location = 'index-d.html#/signin'
                $rootScope.$emit('session:invalid', 'Invalid session...');
            }
            return rejection;
        }
    };
}
appServices.factory('generalHttpInterceptor', generalHttpInterceptor);

appControllers.controller('sessionResponseController', function ($log, $scope, panels) {
    $log.info('sessionResponseController...');
    $scope.message = 'session response...';

    $scope.$on('session:response', function (event, data) {
        $log.debug('session response started...');
        $scope.message = data;
        panels.open('sessionResponse');
        $log.debug('session response finished...');
    });
});
