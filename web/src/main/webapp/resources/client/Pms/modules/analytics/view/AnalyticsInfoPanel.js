Ext.define('Pms.modules.analytics.view.AnalyticsInfoPanel', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.analyticsInfoPanel',
    layout: 'hbox',
    margin: 5,
    defaults: {
        xtype: 'panel',
        border: 1,
        width: '25%',
        margin: 0,
        bodyPadding: 5,
        cls: 'analytics-info-panel'
    },
    items: [
        {
            title: l('analytic.averageArrival'),
            items: [
                {
                    name: 'averageArrival',
                    xtype: 'displayfield',
                    hideLabel: true,
                    cls: 'analytic-info-panel-text',
                    height: 25,
                    width: '100%'
                }
            ]
        },
        {
            title: l('analytic.averageSum'),
            items: [
                {
                    name: 'averageSum',
                    xtype: 'displayfield',
                    hideLabel: true,
                    cls: 'analytic-info-panel-text',
                    height: 25,
                    width: '100%'
                }
            ]
        },
        {
            title: l('analytic.roomUseQuantity'),
            items: [
                {
                    name: 'roomUseQuantity',
                    xtype: 'displayfield',
                    hideLabel: true,
                    cls: 'analytic-info-panel-text',
                    height: 25,
                    width: '100%'
                }
            ]
        },
        {
            title: l('analytic.totalSum'),
            items: [
                {
                    name: 'totalSum',
                    xtype: 'displayfield',
                    hideLabel: true,
                    cls: 'analytic-info-panel-text',
                    height: 25,
                    width: '100%'
                }
            ]
        }
    ]
});