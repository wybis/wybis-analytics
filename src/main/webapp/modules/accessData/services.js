function accessDataService($log, $http, $q, wydNotifyService) {
    var basePath = '/session/access-data', isLoggingEnabled = false;

    var service = {
        applications : [],
        results : { frMs : null, toMs : null, items : null},
        itemsMap : {},
        patternMap : {}
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
    }

    service.doSearch = function(application, frMs, toMs) {
        var results = service.results;
        curPageNo = 0;
        results.application = application;
        results.frMs = frMs;
        results.toMs = toMs;
        results.items = [];
        service.itemsMap = {};
        service.patternMap = {};
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
            $log.info(response);
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
        });
    }

    function processItem(item) {
        computeRequestPathKey(item);
        var summary = service.itemsMap[item.requestPathKey];
        if(summary) {
            summary.totalHits += 1;
            if(summary.minTime > item.processTime) {
                summary.minTime = item.processTime;
            }
            if(summary.maxTime < item.processTime) {
                summary.maxTime = item.processTime;
            }
            summary.totalTime += item.processTime;
            summary.items.push(item);
        }
        else {
            summary = {
                id : _.uniqueId(),
                requestPathKey : item.requestPathKey,
                totalHits : 1,
                minTime : item.processTime,
                maxTime : item.processTime,
                avgTime : 0,
                totalTime : item.processTime,
                items : [item]
            };
            service.results.items.push(summary);
            service.itemsMap[item.requestPathKey] = summary;
            service.patternMap[summary.id] = summary;
        }
        summary.avgTime = summary.totalTime / summary.totalHits;
    }

    function computeRequestPathKey(item) {
        //  /tellerTransfers/transaction/304572/pay
        var re = /\/tellerTransfers\/transaction\/(\d+)\/pay/i;
        if(item.requestPath.match(re)) {
            item.requestPathKey = '/tellerTransfers/transaction/id/pay';
            return;
        }

        //  /tellerTransfers/transaction/304572/acknowledge
        re = /\/tellerTransfers\/transaction\/(\d+)\/acknowledge/i;
        if(item.requestPath.match(re)) {
            item.requestPathKey = '/tellerTransfers/transaction/id/acknowledge';
            return;
        }

        //  /tellerTransfers/transaction/304572/acknowledge
        re = /\/tellerTransfers\/transaction\/(\d+)\/cancel/i;
        if(item.requestPath.match(re)) {
            item.requestPathKey = '/tellerTransfers/transaction/id/cancel';
            return;
        }

        //  /tranReceiptCounters/tranReceiptCounter/807331/19868
        re = /\/tranReceiptCounters\/tranReceiptCounter\/\d+\/\d+/i
        if(item.requestPath.match(re)) {
            item.requestPathKey = '/tranReceiptCounters/tranReceiptCounter/id/id';
            return;
        }

        //  /tranReceiptCounters/tranReceiptCounter/923264/transaction/949395/22928
        re = /\/tranReceiptCounters\/tranReceiptCounter\/\d+\/transaction\/\d+\/\d+/i
        if(item.requestPath.match(re)) {
            item.requestPathKey = '/tranReceiptCounters/tranReceiptCounter/id/transaction/id/id';
            return;
        }

        //  /tranReceiptVirtuals/tranReceiptVirtual/11624
        re = /\/tranReceiptVirtuals\/tranReceiptVirtual\/(\d+)/i;
        if(item.requestPath.match(re)) {
            item.requestPathKey = '/tranReceiptVirtuals/tranReceiptVirtual/id';
            return;
        }

        //  /tranReceiptVirtuals/tranReceiptVirtual/11632/transaction/27147
        re = /\/tranReceiptVirtuals\/tranReceiptVirtual\/(\d+)\/transaction\/(\d+)/i;
        if(item.requestPath.match(re)) {
            item.requestPathKey = '/tranReceiptVirtuals/tranReceiptVirtual/id/transaction/id';
            return;
        }

        //  /tranBranchTransfers/transaction/27391/acknowledge
        re = /\/tranBranchTransfers\/transaction\/(\d+)\/acknowledge/i;
        if(item.requestPath.match(re)) {
            item.requestPathKey = '/tranBranchTransfers/transaction/id/acknowledge';
            return;
        }

        //  /tranBranchTransfers/transaction/27391/cancel
        re = /\/tranBranchTransfers\/transaction\/(\d+)\/cancel/i;
        if(item.requestPath.match(re)) {
            item.requestPathKey = '/tranBranchTransfers/transaction/id/cancel';
            return;
        }

        //  /tranReceiptDealers/tranReceiptDealer/88781/transaction/30806/22812
        re = /\/tranReceiptDealers\/tranReceiptDealer\/(\d+)\/transaction\/(\d+)\/(\d+)/i;
        if(item.requestPath.match(re)) {
            item.requestPathKey = '/tranReceiptDealers/tranReceiptDealer/id/transaction/id/id';
            return;
        }

        //  /customers/change-pbl-status/667
        re = /\/customers\/change-pbl-status\/(d+)/i;
        if(item.requestPath.match(re)) {
            item.requestPathKey = '/customers/change-pbl-status/id';
            return;
        }

        item.requestPathKey = item.requestPath;
    }

    service.init();

    return service;
}
appServices.factory('accessDataService', accessDataService);