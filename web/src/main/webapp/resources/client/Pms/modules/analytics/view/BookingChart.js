Ext.define('Pms.modules.analytics.view.BookingChart', {
    extend: 'Pms.abstract.Chart',
    alias: 'widget.bookingChart',
    store: 'Pms.modules.analytics.store.AnalyticsBooking',
//    shadow: true,
    animate: true,
    minHeight: 300,
    background: {
        fill: '#fff'
    },
    legend: {
        position: 'right'
    },
    theme: 'Category2',
    axes: [
        {
            type: 'Numeric',
            position: 'left',
            fields: ['total','OSTROVOK','FRONT_DESK', 'EXPEDIA', 'BOOKING', 'HOTEL_DE', 'ITWG', 'IN_ITALIA', 'HOTELS_COM', 'RESERVER_IT', 'ACCOMODATIONZ_COM',
                'HOTEL_BEDS', 'VENERE_COM', 'HRS_COM', 'TRAVEL_EUROPE', 'ATRAPALO', 'MBE_TRAVEL', 'ESCAPIO', 'BB_PLANET', 'SPLENDIA', 'AGODA',
                'HOSTELS_CLUB', 'LASTMINUTE', 'TRAVELOCITY', 'SABRE', 'BUDGETPLACES', 'ORBITZ', 'LATE_ROOMS', 'IN_TOSCANA', 'HOSTEL_WORLD', 'BOOKING_BUTTON'],
            title: l('analytic.arrivals') + ', %',
            minorTickSteps: 1,
            grid: {
                even: {
                    fill: '#eee',
                    stroke: '#ddd'
                }
            }
        },
        {
            type: 'Category',
            position: 'bottom',
            fields: ['date'],
            title: l('date'),
            label: {
                renderer: function (val) {
                    return Ext.Date.format(val, 'd M Y');
                }
            },
            grid: true
        }
    ],

    series: [
        {
            type: 'line',
            highlight: {
                size: 7,
                radius: 7
            },
            axis: 'left',
            fill: true,
            xField: 'date',
            yField: 'total',
            title: l('totalGrid'),
            smooth: true,
            markerConfig: {
                type: 'circle',
                size: 5,
                radius: 5,
                'stroke-width': 0
            },
        },
//        {
//            type: 'line',
//            highlight: {
//                size: 7,
//                radius: 7
//            },
//            axis: 'left',
//            fill: true,
//            xField: 'date',
//            yField: 'FRONT_DESK',
//            markerConfig: {
//                type: 'circle',
//                size: 5,
//                radius: 5,
//                'stroke-width': 0
//            }
//        },
//        {
//            type: 'line',
//            highlight: {
//                size: 7,
//                radius: 7
//            },
//            axis: 'left',
//            fill: true,
//            xField: 'date',
//            yField: 'HOTEL_DE',
//            markerConfig: {
//                type: 'circle',
//                size: 5,
//                radius: 5,
//                'stroke-width': 0
//            }
//        },
//        {
//            type: 'line',
//            highlight: {
//                size: 7,
//                radius: 7
//            },
//            axis: 'left',
//            fill: true,
//            xField: 'date',
//            yField: 'BOOKING',
//            markerConfig: {
//                type: 'circle',
//                size: 5,
//                radius: 5,
//                'stroke-width': 0
//            }
//        }
    ],
    initComponent: function () {
        this.callParent(arguments);
        var me = this
        me.getStore().load({
            callback: function () {
                if (me.series.items.length > 1)
                    return
                for (var source in me.getStore().sourceObj) {
                    me.series.add({
                        type: 'line',
                        highlight: {
                            size: 7,
                            radius: 7
                        },
                        axis: 'left',
                        fill: true,
                        smooth: true,
                        xField: 'date',
                        yField: source,
                        title: l('source.' + source),
                        markerConfig: {
                            type: 'circle',
                            size: 5,
                            radius: 5,
                            'stroke-width': 0
                        }
                    })
                }
                me.redraw();
            }
        })
    }
});