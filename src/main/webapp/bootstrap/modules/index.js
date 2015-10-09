'use strict';

function rootController($rootScope, $scope, $log, $window) {
    $log.info('rootController...');

    $scope.viewSource = function () {
        var s = 'view-source:localhost:1111/' + $rootScope.currentViewSrcUrl;
        $log.info(s);
        $window.open(s);
    };
}
appControllers.controller('rootController', rootController);

var dependents = ['ngRoute', 'ngSanitize'];
dependents.push('ngStorage');
dependents.push('ngNotify');
dependents.push('hSweetAlert');
dependents.push('green.inputmask4angular');
dependents.push('blockUI');
dependents.push('ui.select');
dependents.push('ui.bootstrap');
dependents.push('app.filters');
dependents.push('app.directives');
dependents.push('app.services');
dependents.push('app.controllers');
var app = angular.module('app', dependents);

app.config(function (uiSelectConfig) {
    uiSelectConfig.theme = 'selectize';
    // uiSelectConfig.theme = 'select2';
    // uiSelectConfig.theme = 'bootstrap';
});

app.config(function ($httpProvider) {
    $httpProvider.interceptors.push('generalHttpInterceptor');
});

//app.config(function(blockUIConfig) {
//	blockUIConfig.autoBlock = false;
//});

function appConfig($routeProvider, $locationProvider) {

    $routeProvider.when('/', {
        redirectTo: '/home'
    });

    $routeProvider.when('/home', {
        templateUrl: 'modules/home/d.html',
        controller: 'homeController'
    });

    $routeProvider.when('/access-data', {
        templateUrl: 'modules/accessData/d.html',
        controller: 'accessDataListController'
    });

    $routeProvider.when('/not-found', {
        templateUrl: 'modules/zgeneral/d-notFound.html'
    });

    $routeProvider.otherwise({
        redirectTo: '/not-found'
    });
};
app.config(appConfig);

function appInit($log, $rootScope, $location, $sessionStorage) {
    $log.info('Initialization started...');

    $rootScope.$on("$routeChangeStart", function (event, next, current) {
        // $log.info('Location : ', $location.path());
        var curLocPath = $location.path();

        // $log.info('Before Current Location : ', curLocPath);
        var srcUrl = $location.absUrl().indexOf('wybis-analytics');
        srcUrl = $location.absUrl().substring(0, srcUrl);
        srcUrl = srcUrl + next.templateUrl;
        $rootScope.currentViewSrcUrl = srcUrl;
        // $log.info('srcUrl = ' + srcUrl);
    });

    $rootScope.$on("$routeChangeSuccess", function (event, next, current) {
        // $log.info('Location : ', $location.path());
        var curLocPath = $location.path();
        // $log.info('After Current Location : ', curLocPath);
    });

    $log.info('Initialization finished...');
}
app.run(['$log', '$rootScope', '$location', '$sessionStorage', appInit]);
