Ext.define('Pms.modules.analytics.view.AnalyticsSourceGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.analyticsSourceGrid',
    store: 'Pms.modules.analytics.store.AnalyticsBookingSource',
    paging: false,
    cls: 'analytics-grid',
    columns: [
        {
            header: l('analytic.source'),
            dataIndex: 'source',
            renderer: function (val) {
                return l('source.' + val)
            }
        },
        {
            header: l('analytic.percent'),
            dataIndex: 'percent'
        },
        {
            header: l('analytic.quantity'),
            dataIndex: 'quantity'
        },
        {
            header: l('analytic.sum'),
            dataIndex: 'sum',
            renderer: function (val) {
                return Ext.util.Format.number(val / 100, '0.00')
            }
        }
    ]
});