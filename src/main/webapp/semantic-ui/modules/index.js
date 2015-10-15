'use strict';

function rootController($rootScope, $scope, $log, $window) {
    $log.info('rootController...');

    $scope.viewSource = function () {
        var s = 'view-source:localhost:1111/' + $rootScope.currentViewSrcUrl;
        $log.info(s);
        $window.open(s);
    };

    $scope.toggleSideBar = function(sideBarId) {
      $('#' + sideBarId).sidebar('toggle');
      //  $('#leftSideBar').sidebar('toggle');
      //  $('#rightSideBar').sidebar('toggle');
      //  $('#topSideBar').sidebar('toggle');
      //  $('#bottomSideBar').sidebar('toggle');
    };
}
appControllers.controller('rootController', rootController);

var dependents = ['ngRoute', 'ngSanitize'];
dependents.push('ngStorage');
dependents.push('green.inputmask4angular');
dependents.push('app.filters');
dependents.push('app.directives');
dependents.push('app.services');
dependents.push('app.controllers');
var app = angular.module('app', dependents);

app.config(function ($httpProvider) {
    $httpProvider.interceptors.push('generalHttpInterceptor');
});

function appConfig($routeProvider, $locationProvider) {

    $routeProvider.when('/', {
        redirectTo: '/home'
    });

    $routeProvider.when('/home', {
        templateUrl: 'semantic-ui/modules/home/d.html',
        controller: 'homeController'
    });

    $routeProvider.when('/access-data', {
        templateUrl: 'semantic-ui/modules/accessData/d.html',
        controller: 'accessDataListController'
    });

    $routeProvider.when('/not-found', {
        templateUrl: 'semantic-ui/modules/zgeneral/d-notFound.html'
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
