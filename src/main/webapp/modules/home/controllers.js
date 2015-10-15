function homeController($rootScope, $scope, $log) {
    $log.debug('homeController...');
    $rootScope.viewName = 'Home';

    $scope.message = '';

}
appControllers.controller('homeController', homeController);
