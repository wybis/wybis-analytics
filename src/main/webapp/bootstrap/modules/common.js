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

