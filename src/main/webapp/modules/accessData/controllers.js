function accessDataListController($rootScope, $scope, $log, wydNotifyService, accessDataService) {
    $log.debug('accessDataListController...');
    $rootScope.viewName = 'Access Data';

    var datePattern = 'YYYY - MM - DD HH : mm', dateMask = '9999 - 99 - 99   99 : 99', searchCriteria = {};
    var scDefault = {
        application: null,
        frString: '',
        frPattern: datePattern,
        frMask: {mask: dateMask},
        frValidity: false,
        frMilliSeconds: 0,
        toString: '',
        toPattern: datePattern,
        toMask: {mask: dateMask},
        toValidity: false,
        toMilliSeconds: 0,
        dateRange: null
    };
    _.assign(searchCriteria, scDefault);
    $scope.searchCriteria = searchCriteria;

    $scope.pathSummary = null;
    $scope.dateRanges = [];
    initDateRanges();

    $scope.applications = accessDataService.applications;

    $scope.results = accessDataService.results;

    $scope.search = function () {
        wydNotifyService.removeAll();

        var application = searchCriteria.application;
        if (_.isObject(application) == false) {
            application = _.find($scope.applications, function (item) {
                return item.id == searchCriteria.application;
            });
        }

        if (searchCriteria.application.id === 0) {
            wydNotifyService.addError('Please select application...', true);
            return;
        }

        var frMoment = new moment(searchCriteria.frString, datePattern)
        searchCriteria.frValidity = frMoment.isValid();
        searchCriteria.frMilliSeconds = frMoment.valueOf();
        //$log.info(frMoment)
        if (!searchCriteria.frValidity) {
            wydNotifyService.addError('Invalid from timestamp...', true);
            return;
        }

        var toMoment = new moment(searchCriteria.toString, datePattern)
        searchCriteria.toValidity = toMoment.isValid();
        searchCriteria.toMilliSeconds = toMoment.valueOf();
        //$log.info(toMoment)

        if (!searchCriteria.toValidity) {
            wydNotifyService.addError('Invalid to timestamp...', true);
            return;
        }

        if (!frMoment.isBefore(toMoment)) {
            wydNotifyService.addError('From timestamp should be before To timestamp...', true);
            return;
        }

        //$log.info(searchCriteria);

        accessDataService.doSearch(application, searchCriteria.frMilliSeconds, searchCriteria.toMilliSeconds);

        $scope.showSummaryView();
    };

    $scope.onDateRangeChange = function () {
        var dateRange = searchCriteria.dateRange;
        if (_.isObject(dateRange) == false) {
            dateRange = _.find($scope.dateRanges, function (item) {
                return item.name == searchCriteria.dateRange;
            });
        }
        //$log.info(dateRange);
        if (dateRange.st) {
            searchCriteria.frString = dateRange.st.format(datePattern);
            searchCriteria.toString = dateRange.ed.format(datePattern);
        }
        else {
            searchCriteria.frString = '   -    -      :   ';
            searchCriteria.toString = '   -    -      :   ';
        }
        //$log.info('frString = ' + searchCriteria.frString + '  toString = ' + searchCriteria.toString)
    };

    $scope.showPathView = function (pathSummary) {
        $scope.pathSummary = pathSummary;
    };

    $scope.showSummaryView = function () {
        $scope.pathSummary = null;
    };

    function initDateRanges() {
        var dateRanges = $scope.dateRanges, dr = null;

        var st = null, ed = null;
        dr = {name: '<Select Date Range>', st: st, ed: ed};
        dateRanges.push(dr);

        st = new moment(), ed = new moment();
        st.startOf('day');
        dr = {name: 'From today to now', st: st, ed: ed};
        dateRanges.push(dr);

        st = new moment(), ed = new moment();
        st.subtract(1, 'days');
        st.startOf('day');
        dr = {name: 'Last one day to now', st: st, ed: ed};
        dateRanges.push(dr);

        st = new moment(), ed = new moment();
        st.subtract(2, 'days');
        st.startOf('day');
        dr = {name: 'Last two days to now', st: st, ed: ed};

        st = new moment(), ed = new moment();
        st.subtract(1, 'weeks');
        st.startOf('day');
        dr = {name: 'Last one week to now', st: st, ed: ed};
        dateRanges.push(dr);

        st = new moment(), ed = new moment();
        st.subtract(2, 'weeks');
        st.startOf('day');
        dr = {name: 'Last two week to now', st: st, ed: ed};
        dateRanges.push(dr);

        st = new moment(), ed = new moment();
        st.subtract(1, 'months');
        st.startOf('day');
        dr = {name: 'Last one month to now', st: st, ed: ed};
        dateRanges.push(dr);

        st = new moment(), ed = new moment();
        st.subtract(2, 'months');
        st.startOf('day');
        dr = {name: 'Last two months to now', st: st, ed: ed};
        dateRanges.push(dr);

        st = new moment(), ed = new moment();
        st.subtract(3, 'months');
        st.startOf('day');
        dr = {name: 'Last three months to now', st: st, ed: ed};
        dateRanges.push(dr);

        st = new moment(), ed = new moment();
        st.subtract(6, 'months');
        st.startOf('day');
        dr = {name: 'Last six months to now', st: st, ed: ed};
        dateRanges.push(dr);

        st = new moment(), ed = new moment();
        st.subtract(1, 'years');
        st.startOf('day');
        dr = {name: 'Last one year to now', st: st, ed: ed};
        dateRanges.push(dr);
    }

    function init() {
        if ($scope.applications.length > 0) {
            searchCriteria.application = $scope.applications[0];
        }
        if ($scope.dateRanges.length > 0) {
            searchCriteria.dateRange = $scope.dateRanges[0];
        }
    }

    init();
}
appControllers.controller('accessDataListController', accessDataListController);
