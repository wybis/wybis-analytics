'use strict';

function rootController($rootScope, $scope, $log, $mdSidenav) {
    $log.debug('rootController...');

    $scope.toggleSideNavBar = function(menuId) {
        $log.info('clicked...');
        $mdSidenav(menuId).toggle();
    };
}
appControllers.controller('rootController', rootController);

var dependents = ['ngRoute', 'ngSanitize'];
dependents.push('ngMaterial');
dependents.push('ngMdIcons');
dependents.push('md.data.table');
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

var uikitId = 'material';
function appConfig($routeProvider, $locationProvider) {

    $routeProvider.when('/', {
        redirectTo: '/home'
    });

    $routeProvider.when('/home', {
        templateUrl: uikitId + '/modules/home/d.html',
        controller: 'homeController'
    });

    $routeProvider.when('/access-data', {
        templateUrl: uikitId + '/modules/accessData/d.html',
        controller: 'accessDataListController'
    });

    $routeProvider.when('/settings', {
        templateUrl: uikitId + '/modules/settings/d.html',
        controller: 'settingsController'
    });

    $routeProvider.when('/not-found', {
        templateUrl: uikitId + '/modules/zgeneral/d-notFound.html'
    });

    $routeProvider.otherwise({
        redirectTo: '/not-found'
    });
};
app.config(appConfig);

function appInit($log, $rootScope, $location, $sessionStorage, $mdSidenav) {
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

        if($mdSidenav('left').isOpen()) {
            $mdSidenav('left').close();
        }
    });

    $rootScope.$on("$routeChangeSuccess", function (event, next, current) {
        // $log.info('Location : ', $location.path());
        var curLocPath = $location.path();
        // $log.info('After Current Location : ', curLocPath);
        //$mdSidenav('left').toggle();
    });

    $log.info('Initialization finished...');
}
app.run(['$log', '$rootScope', '$location', '$sessionStorage', '$mdSidenav', appInit]);
