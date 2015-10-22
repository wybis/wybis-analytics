'use strict';

function rootController($rootScope, $scope, $log, $window, panels) {
    $log.debug('rootController...');

    $scope.openPanel = function (panelId) {
        panels.open(panelId);
    };

    $scope.viewSource = function () {
        var s = 'view-source:localhost:1111/' + $rootScope.currentViewSrcUrl;
        $log.info(s);
        $window.open(s);
    };
}
appControllers.controller('rootController', rootController);

appControllers.controller('leftPanelController', function ($log, $scope) {
    $log.debug('leftPanelController...');
});

appControllers.controller('rightPanelController', function ($log, $scope) {
    $log.info('rightPanelController...');
});

var dependents = ['ngRoute', 'ngSanitize'];
dependents.push('ngStorage');
dependents.push('ngNotify');
dependents.push('hSweetAlert');
dependents.push('green.inputmask4angular');
dependents.push('blockUI');
dependents.push('angular.panels');
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

var uikitId = 'bootstrap';

app.config(function (panelsProvider) {

    panelsProvider.add({
        id: 'leftPanel',
        position: 'left',
        size: '300px',
        templateUrl: uikitId + '/modules/zgeneral/d-leftPanel.html',
        controller: 'leftPanelController'
    });

    panelsProvider.add({
        id: 'rightPanel',
        position: 'right',
        size: '300px',
        templateUrl: uikitId + '/modules/zgeneral/d-rightPanel.html',
        controller: 'rightPanelController'
    });

});

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

function appInit($log, $rootScope, $location, $sessionStorage, panels) {
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

        panels.close();
    });

    $log.info('Initialization finished...');
}
app.run(['$log', '$rootScope', '$location', '$sessionStorage', 'panels', appInit]);
