function settingsController($rootScope, $scope, $log) {
    $log.debug('settingsController...');
    $rootScope.viewName = 'Settings';

}
appControllers.controller('settingsController', settingsController);
