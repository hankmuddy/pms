Ext.define('Pms.modules.analytics.store.AnalyticsBookingSource', {
    extend: 'Pms.modules.analytics.store.Analytics',
    model: 'Pms.modules.analytics.model.Analytics',
    alias: 'widget.analyticsBookingStoreSource',
    url: 'rest/roomUse',
    sorters: [
        {
            property: 'id',
            direction: 'ASC'
        }
    ],
    parseResponseData: function (data) {
        var oldRecords = data.content,
            from = Pms.toUTC(Ext.ComponentQuery.query('datefield[name=analyticsFrom]')[0].getValue()),
            to = Pms.toUTC(Ext.ComponentQuery.query('datefield[name=analyticsTo]')[0].getValue()),
            sourceMap = {},
            newRecords = [];
        Ext.each(oldRecords, function (oldRec) {
            if (!sourceMap[oldRec.source]) {
                sourceMap[oldRec.source] = {
                    quantity: 1,
                    sum: oldRec.total
                };
            }
            else {
                sourceMap[oldRec.source].quantity++;
                sourceMap[oldRec.source].sum += oldRec.total;
            }
        });
        var i = 0;
        for (var source in sourceMap) {
            newRecords.push({
                id: ++i,
                source: source,
                quantity: sourceMap[source].quantity,
                sum: sourceMap[source].sum,
                percent: Ext.util.Format.number(sourceMap[source].quantity / oldRecords.length * 100, '0.00') + '%'
            });
        }
        data.content = newRecords;
        data.page.totalCount = newRecords.length;
        return data
    }
});