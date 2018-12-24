!function(e) {

    var intervalTime = 1000; //ms
    var hrefUrl = e.location.href;
    var hostname = e.location.hostname;
    var pathname = e.location.pathname;
    var host = e.location.host;
    var pageTime = (new Date).getTime();

    e.startWebViewMonitor = function() {

        console.log("WangQing_startWebViewMonitor");

        // 已经开始执行了就返回
        if (e.monitorStarted) return !1;
        e.monitorStarted = !0;

        setTimeout(function() {
            var navigationTiming = {
                type: "monitor_resourceTiming",
                payload: {
                    url: hrefUrl,
                    domain: hostname,
                    uri: pathname,
                    navigationTiming: performanceTiming.getNavigationTiming(),
                    resourceTiming: performanceTiming.getResourceTiming()
                }
            };
            sendResourceTiming(navigationTiming);
        }, 0);

        var getResourceTiming = function() {
            var timing = performanceTiming.getResourceTiming();
            if (timing.length > 0) {
                var resourceTiming = {
                    type: "monitor_resourceTiming",
                    payload: {
                        url: hrefUrl,
                        domain: hostname,
                        uri: pathname,
                        navigationTiming: {},
                        resourceTiming: timing,
                    }
                };
                sendResourceTiming(resourceTiming);
            }
        };

        e.setInterval(getResourceTiming, intervalTime); //每隔3秒执行一次

        var already = !0;
        e.addEventListener("beforeunload",
            function() {
                already && (already = !1, getResourceTiming())
            }
        );
        e.addEventListener("unload",
            function() {
                already && (already = !1, getResourceTiming())
            }
        );
    }

    function sendResourceTiming(e) {
        console.log("WangQing_" + JSON.stringify(e));
        myObj.sendResource(JSON.stringify(e))
    };


    function sendErrors() {
        var err = errorMonitor.getError();
        if (err.length > 0) {
            var errorInfo = {
                type: "monitor_error",
                payload: {
                    url: hrefUrl,
                    domain: hostname,
                    uri: pathname,
                    error_list: err
                }
            };

            console.log("WangQing_" + JSON.stringify(errorInfo));
            myObj.sendError(JSON.stringify(errorInfo))
        }
    };


    /**
     * 在这里每隔三秒去发送ajax和error错误信息
     */
    e.setInterval(
        function() {
            console.log("sendErrors data");
            sendErrors();
        }, intervalTime
    );

    var performanceTiming = function() {
        function navigationTiming() {
            if (!e.performance || !e.performance.timing) return {};
            var time = e.performance.timing;
            return {
                navigationStart: time.navigationStart,
                redirectStart: time.redirectStart,
                redirectEnd: time.redirectEnd,
                fetchStart: time.fetchStart,
                domainLookupStart: time.domainLookupStart,
                domainLookupEnd: time.domainLookupEnd,
                connectStart: time.connectStart,
                secureConnectionStart: time.secureConnectionStart ? time.secureConnectionStart: time.connectEnd - time.secureConnectionStart,
                connectEnd: time.connectEnd,
                requestStart: time.requestStart,
                responseStart: time.responseStart,
                responseEnd: time.responseEnd,
                unloadEventStart: time.unloadEventStart,
                unloadEventEnd: time.unloadEventEnd,
                domLoading: time.domLoading,
                domInteractive: time.domInteractive,
                domContentLoadedEventStart: time.domContentLoadedEventStart,
                domContentLoadedEventEnd: time.domContentLoadedEventEnd,
                domComplete: time.domComplete,
                loadEventStart: time.loadEventStart,
                loadEventEnd: time.loadEventEnd,
                pageTime: pageTime
            }
        }
        function resourceTiming() {
            if (!e.performance || !e.performance.getEntriesByType) return [];
            for (var time = e.performance.getEntriesByType("resource"), resArr = [], i = 0; i < time.length; i++) {
                var t = time[i].secureConnectionStart ? time[i].secureConnectionStart: time[i].connectEnd - time[i].secureConnectionStart,
                    res = {
                        connectEnd: time[i].connectEnd,
                        connectStart: time[i].connectStart,
                        domainLookupEnd: time[i].domainLookupEnd,
                        domainLookupStart: time[i].domainLookupStart,
                        duration: time[i].duration,
                        entryType: time[i].entryType,
                        fetchStart: time[i].fetchStart,
                        initiatorType: time[i].initiatorType,
                        name: time[i].name,
                        redirectEnd: time[i].redirectEnd,
                        redirectStart: time[i].redirectStart,
                        requestStart: time[i].requestStart,
                        responseEnd: time[i].responseEnd,
                        responseStart: time[i].responseStart,
                        secureConnectionStart: t,
                        startTime: time[i].startTime
                    };
                resArr.push(res);
            }
            return resArr;
        }
        return {
            cacheResourceTimingLength: 0,
            getNavigationTiming: function() {
                return navigationTiming();
            },
            getResourceTiming: function() {
                var timing = resourceTiming();
                var len = timing.length;
                return timing.length != this.cacheResourceTimingLength ?
                    (timing = timing.slice(this.cacheResourceTimingLength, len), this.cacheResourceTimingLength = len, timing) : []
            }
        }
    }();

    var errorMonitor = function() {
        var errors = [];
        return e.addEventListener && e.addEventListener("error",
            function(e) {
                var eInfo = {};
                eInfo.time = e.timeStamp || (new Date).getTime(),
                    eInfo.url = e.filename,
                    eInfo.msg = e.message,
                    eInfo.line = e.lineno,
                    eInfo.column = e.colno,
                    e.error ? (eInfo.type = e.error.name, eInfo.stack = e.error.stack) : (eInfo.msg.indexOf("Uncaught ") > -1 ? eInfo.stack = eInfo.msg.split("Uncaught ")[1] + " at " + eInfo.url + ":" + eInfo.line + ":" + eInfo.column: eInfo.stack = eInfo.msg + " at " + eInfo.url + ":" + eInfo.line + ":" + eInfo.column, eInfo.type = eInfo.stack.slice(0, eInfo.stack.indexOf(":"))),
                eInfo.type.toLowerCase().indexOf("script error") > -1 && (eInfo.type = "ScriptError"),
                    errors.push(eInfo);
            }, !1), {
            getError: function() {
                return errors.splice(0, errors.length);
            }
        }
    }();

} (this);