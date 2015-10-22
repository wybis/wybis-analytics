function historyBackButton($window) {
    return {

        restrict : 'E',

        replace : true,

        template : '<button class="btn btn-default btn-sm">Back</button>',

        link : function(scope, elem, attr) {
            elem.on('click', function() {
                $window.history.back();
            });
        }
    };
}
appDirectives.directive('historyBackButton', [ '$window', historyBackButton ]);

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
