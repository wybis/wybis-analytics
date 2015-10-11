'use strict';

function rootController($rootScope, $scope, $log, $mdSidenav) {
    $log.debug('rootController...');

    $scope.toggleSideNavBar = function(menuId) {
        $log.info('clicked...');
        $mdSidenav(menuId).toggle();
    };
}
appControllers.controller('rootController', rootController);

var dependents = ['ngMaterial'];
dependents.push('app.filters');
dependents.push('app.directives');
dependents.push('app.services');
dependents.push('app.controllers');
var app = angular.module('app', dependents);
