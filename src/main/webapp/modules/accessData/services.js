function accessDataService($log, $http, $q, wydNotifyService) {
    var basePath = '/session/access-data', isLoggingEnabled = false;

    var service = {
        applications : [],
        pathKeyPatterns : [],
        results : { application : null, frMs : null, toMs : null, items : null},
        itemsMap : {}
    }, curPageNo = 0;

    service.init = function() {
        var applications = service.applications, app = null;
        app = {id : 0, code : 'select-application', name : '<Select Application>'};
        applications.push(app);
        app = {id : 1, code : 'moneybox-main', name : 'MoneyBox Main'};
        applications.push(app);
        app = {id : 2, code : 'moneybox-report', name : 'MoneyBox Report'};
        applications.push(app);
        app = {id : 3, code : 'moneybox-ras', name : 'MoneyBox Risk Analysis'};
        applications.push(app);
        app = {id : 4, code : 'harmoney-main', name : 'Harmoney Main'};
        applications.push(app);

        var pathKeyPatterns = service.pathKeyPatterns, pathKeyPattern = null;

        //  /tellerTransfers/transaction/304572/pay
        pathKeyPattern = {
            pathKey : '/tellerTransfers/transaction/id/pay',
            pattern : /\/tellerTransfers\/transaction\/(\d+)\/pay/i
        };
        pathKeyPatterns.push(pathKeyPattern);

        //  /tellerTransfers/transaction/304572/acknowledge
        pathKeyPattern = {
            pathKey : '/tellerTransfers/transaction/id/acknowledge',
            pattern : /\/tellerTransfers\/transaction\/(\d+)\/acknowledge/i
        };
        pathKeyPatterns.push(pathKeyPattern);

        //  /tellerTransfers/transaction/304572/acknowledge
        pathKeyPattern = {
            pathKey : '/tellerTransfers/transaction/id/cancel',
            pattern : /\/tellerTransfers\/transaction\/(\d+)\/cancel/i
        };
        pathKeyPatterns.push(pathKeyPattern);

        //  /tranReceiptCounters/tranReceiptCounter/807331/19868
        pathKeyPattern = {
            pathKey : '/tranReceiptCounters/tranReceiptCounter/id/id',
            pattern : /\/tranReceiptCounters\/tranReceiptCounter\/\d+\/\d+/i
        };
        pathKeyPatterns.push(pathKeyPattern);

        //  /tranReceiptCounters/tranReceiptCounter/923264/transaction/949395/22928
        pathKeyPattern = {
            pathKey : '/tranReceiptCounters/tranReceiptCounter/id/transaction/id/id',
            pattern : /\/tranReceiptCounters\/tranReceiptCounter\/\d+\/transaction\/\d+\/\d+/i
        };
        pathKeyPatterns.push(pathKeyPattern);

        //  /tranReceiptVirtuals/tranReceiptVirtual/11624
        pathKeyPattern = {
            pathKey : '/tranReceiptVirtuals/tranReceiptVirtual/id',
            pattern : /\/tranReceiptVirtuals\/tranReceiptVirtual\/(\d+)/i
        };
        pathKeyPatterns.push(pathKeyPattern);

        //  /tranReceiptVirtuals/tranReceiptVirtual/11632/transaction/27147
        pathKeyPattern = {
            pathKey : '/tranReceiptVirtuals/tranReceiptVirtual/id/transaction/id',
            pattern : /\/tranReceiptVirtuals\/tranReceiptVirtual\/(\d+)\/transaction\/(\d+)/i
        };
        pathKeyPatterns.push(pathKeyPattern);

        //  /tranBranchTransfers/transaction/27391/acknowledge
        pathKeyPattern = {
            pathKey : '/tranBranchTransfers/transaction/id/acknowledge',
            pattern : /\/tranBranchTransfers\/transaction\/(\d+)\/acknowledge/i
        };
        pathKeyPatterns.push(pathKeyPattern);

        //  /tranBranchTransfers/transaction/27391/cancel
        pathKeyPattern = {
            pathKey : '/tranBranchTransfers/transaction/id/cancel',
            pattern : /\/tranBranchTransfers\/transaction\/(\d+)\/cancel/i
        };
        pathKeyPatterns.push(pathKeyPattern);

        //  /tranReceiptDealers/tranReceiptDealer/88781/transaction/30806/22812
        pathKeyPattern = {
            pathKey : '/tranReceiptDealers/tranReceiptDealer/id/transaction/id/id',
            pattern : /\/tranReceiptDealers\/tranReceiptDealer\/(\d+)\/transaction\/(\d+)\/(\d+)/i
        };
        pathKeyPatterns.push(pathKeyPattern);

        //  /customers/change-pbl-status/667
        pathKeyPattern = {
            pathKey : '/customers/change-pbl-status/id',
            pattern : /\/customers\/change-pbl-status\/(d+)/i
        };
        pathKeyPatterns.push(pathKeyPattern);
    }

    service.doSearch = function(application, frMs, toMs) {
        var results = service.results;
        results.application = application;
        results.frMs = frMs;
        results.toMs = toMs;
        results.items = [];
        service.itemsMap = {};
        service.patternMap = {};

        curPageNo = 0;

        search();
    };

    function search() {
        curPageNo++
        var results = service.results;
        var path = basePath + '/search?pageNumber=' + curPageNo;
        path += '&applicationCode=' + results.application.code;
        path += '&fromMilliSeconds=' + results.frMs + '&toMilliSeconds=' + results.toMs;
        //$log.info(path);
        $http.get(path).success(function (response) {
            //$log.info(response);
            if(response.type != 0) {
                return;
            }
            //response.data.content = _.slice(response.data.content, 0, 20);
            _.forEach(response.data.content, function(item) {
                processItem(item);
            });
            if(response.data.last != true) {
                search();
            }
            else {
                sortByDefaultOrder();
            }
        });
    }

    function processItem(item) {
        detectRequestPathKey(item);

        var accessDataSummary = service.itemsMap[item.requestPathKey];
        if(!accessDataSummary) {
            accessDataSummary = {
                requestPathKey : item.requestPathKey,
                totalHits : 0,
                minTime : 0,
                maxTime : 0,
                avgTime : 0,
                totalTime : 0,
                items : []
            };
            service.itemsMap[item.requestPathKey] = accessDataSummary;
            service.results.items.push(accessDataSummary);
        }

        if(accessDataSummary.minTime > item.processTime) {
            accessDataSummary.minTime = item.processTime;
        }
        if(accessDataSummary.maxTime < item.processTime) {
            accessDataSummary.maxTime = item.processTime;
        }
        accessDataSummary.totalHits += 1;
        accessDataSummary.totalTime += item.processTime;
        accessDataSummary.avgTime = accessDataSummary.totalTime / accessDataSummary.totalHits;

        accessDataSummary.items.push(item);
    }

    function detectRequestPathKey(item) {
        var pathKeyPatterns = service.pathKeyPatterns, pathKey = null;

        for (var i = 0; i < pathKeyPatterns.length; i++) {
            var pathKeyPattern = pathKeyPatterns[i];
            if (item.requestPath.match(pathKeyPattern.pattern)) {
                pathKey = pathKeyPattern.pathKey;
                i = pathKeyPatterns.length + 1;
            }
        }

        if(!pathKey) {
            pathKey = item.requestPath;
        }

        item.requestPathKey = pathKey;
    }

    function sortByDefaultOrder() {
        var items0 = _.sortByOrder(service.results.items, 'avgTime'), items1 = [];
        _.forEach(items0, function(ads){
            ads.items = _.sortBy(ads.items, 'processTime').reverse();
            items1.unshift(ads);
        });
        service.results.items = items1;
    }

    service.init();

    return service;
}
appServices.factory('accessDataService', accessDataService);