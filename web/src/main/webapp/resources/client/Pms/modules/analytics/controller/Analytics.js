Ext.define('Pms.modules.analytics.controller.Analytics', {
    extend: 'Pms.abstract.Controller',
    views: [
        "Pms.modules.analytics.view.BookingChart",
        "Pms.modules.analytics.view.Viewport",
        "Pms.modules.analytics.view.AnalyticsDateForm",
        'Pms.modules.analytics.view.AnalyticsInfoPanel',
        'Pms.modules.analytics.view.AnalyticsSourceGrid'
    ],
    stores: [
        "Pms.modules.analytics.store.Analytics",
        "Pms.modules.analytics.store.AnalyticsBooking",
        "Pms.modules.analytics.store.AnalyticsBookingSource"
    ],
    models: [
        "Pms.modules.analytics.model.Analytics"
    ],

    refs: [
        {
            ref: 'analyticsGrid', selector: 'analyticsGrid'
        }
    ],

    init: function (contr, subController) {
        if (!subController) {
            var view = Ext.create('Pms.modules.analytics.view.Viewport');
            this.buildItems(view);
        }
        var me = this;

        me.control({
            'analyticsDateForm button[action=reload-chart]': {
                click: this.reloadChart
            }
        });
    },

    reloadChart: function (btn) {
        var win = btn.up('window'),
            chart = win.down('chart'),
            from = Pms.toUTC(win.down('datefield[name=analyticsFrom]').getValue()),
            to = Pms.toUTC(win.down('datefield[name=analyticsTo]').getValue()),
            form = win.down('analyticsDateForm'),
            grid = win.down('grid'),
            loadParams = {
                params: {
                    filter: [
                        {
                            field: 'startDate',
                            comparison: 'lte',
                            data: {
                                type: 'date',
                                value: to
                            }
                        },
                        {
                            field: 'endDate',
                            comparison: 'gte',
                            data: {
                                type: 'date',
                                value: from
                            }
                        },
                        {
                            field: 'status',
                            comparison: 'neq',
                            data: {
                                type: 'ROOM_USE_STATUS',
                                value: 'REFUSE'
                            }
                        }
                    ]
                }
            };
        if (!form.isValid())
            return;

        chart.store.loadParams = loadParams;
        grid.getStore().loadParams = loadParams;
        chart.store.reload({
            success: function () {
                chart.redraw();
            }
        });
        grid.getStore().reload()

    },

    buildItems: function (view) {
        if (typeof(this.win) !== "undefined") {
            this.win.maximized = true;
            this.win.add(view);
        }
    }
});