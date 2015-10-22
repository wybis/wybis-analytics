function homeController($rootScope, $scope, $log) {
    $log.debug('homeController...');
    $rootScope.viewName = 'Home';

}
appControllers.controller('homeController', homeController);
