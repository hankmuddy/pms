Ext.define('Pms.modules.analytics.view.AnalyticsDateForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.analyticsDateForm',
    layout: 'hbox',
    defaults: {
        xtype: 'datefield',
        margin: 10,
        labelWidth: 50,
        width: 200
    },
    items: [
        {
            name: 'analyticsFrom',
            fieldLabel: l('from'),
            format: 'd/m/Y',
            maxValue: new Date(),
            validator: function () {
                var dateFrom = this.getValue()
                var dateTo = this.nextNode().getValue();
                if ((dateTo - dateFrom) > 5184000000) {
                    return l('analytic.mustBeLessThenTwoMonth')
                }
                if (dateTo < dateFrom) {
                    return l('analytic.incorrectDateData')
                }
                return true
            },
            value: new Date(new Date() - 604800000) //new Date - 1 week
        },
        {
            name: 'analyticsTo',
            fieldLabel: l('to'),
            format: 'd/m/Y',
            value: new Date(),
            validator: function () {
                var dateTo = this.getValue()
                var dateFrom = this.previousNode().getValue();
                if ((dateTo - dateFrom) > 5184000000) {
                    return l('analytic.mustBeLessThenTwoMonth')
                }
                if (dateTo < dateFrom) {
                    return l('analytic.incorrectDateData')
                }
                return true
            },
            maxValue: new Date()
        },
        {
            xtype: 'button',
            text: l('refresh'),
            action: 'reload-chart'
        }
    ]
});