Ext.define('Pms.modules.analytics.store.AnalyticsBooking', {
    extend: 'Pms.modules.analytics.store.Analytics',
    model: 'Pms.modules.analytics.model.Analytics',
    alias: 'widget.analyticsBookingStore',
    url: 'rest/roomUse',
    sorters: [
        {
            property: 'id',
            direction: 'ASC'
        }
    ],
    sourceObj: {},
    constructor: function () {
        var me = this;
        me.parseResponseData = function (data) {

            var oldRecords = data.content,
                from = Pms.toUTC(Ext.ComponentQuery.query('datefield[name=analyticsFrom]')[0].getValue()),
                to = Pms.toUTC(Ext.ComponentQuery.query('datefield[name=analyticsTo]')[0].getValue()),
                dateMap = {},
                newRecords = [],
                roomQuantity = 0,
                totalSum = 0,
                averageSum = 0,
                arrival = 0;

            Pms.Ajax.request({
                url: 'rest/room/count',
                method: 'GET',
                async: false,
                success: function (response) {
                    roomQuantity = response.content;
                }
            });
            for (var i = from; i <= to; i += 86400) {
                dateMap[i] = {
                    total: 0
                };
            }
            Ext.each(oldRecords, function (oldRec) {
                totalSum += oldRec.total;
                for (var i = oldRec.startDate; i < oldRec.endDate; i += 86400) {
                    if (dateMap[i] || dateMap[i] == 0) {
                        dateMap[i].total++;
                        if (!dateMap[i][oldRec.source]) {
                            dateMap[i][oldRec.source] = 1
                            if (!me.sourceObj[oldRec.source]) {
                                me.sourceObj[oldRec.source] = '';
                            }
                        }
                        else {
                            dateMap[i][oldRec.source]++;
                        }
                        arrival++;
                    }
                }
            });
            for (var i in dateMap) {
                for(var source in dateMap[i]){
                    dateMap[i][source] = dateMap[i][source] / roomQuantity * 100;
                }
                dateMap[i].date = i;
                dateMap[i].id = parseInt(i);
                newRecords.push(dateMap[i]);
            }
            averageSum = oldRecords.length ? totalSum / oldRecords.length / 100 : 0;
            Ext.ComponentQuery.query('displayfield[name=totalSum]')[0].setValue(Ext.util.Format.number(totalSum / 100, '0.00'));
            Ext.ComponentQuery.query('displayfield[name=averageSum]')[0].setValue(Ext.util.Format.number(averageSum, '0.00'));
            Ext.ComponentQuery.query('displayfield[name=roomUseQuantity]')[0].setValue(oldRecords.length);
            Ext.ComponentQuery.query('displayfield[name=averageArrival]')[0].setValue(Ext.util.Format.number(arrival / roomQuantity / Object.keys(dateMap).length * 100, '0.00') + '%');
            data.content = newRecords;
            data.page.totalCount = newRecords.length;
            return data
        }
        me.callParent(arguments);
    }

});