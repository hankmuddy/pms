Ext.define('Pms.modules.otaCalendar.view.otaCalendarSch', {
    extend: 'Pms.abstract.Panel',
    alias: 'widget.otaCalendarSch',
    requires: ['Sch.panel.SchedulerGrid', 'Pms.modules.otaCalendar.view.otaCalendarAddWindow'],

    border: false,
    eventStoreLoadByDate: function (date) {
        var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
        var lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 1);

        this.eventStore.load({
            scope: this,
            params: Pms.Ajax.encode({
                filter: [
                    {
                        field: 'date',
                        comparison: 'gte',
                        data: {
                            type: 'date',
                            value: firstDay.getTime() / 1000
                        }
                    },
                    {
                        field: 'date',
                        comparison: 'lte',
                        data: {
                            type: 'date',
                            value: lastDay.getTime() / 1000
                        }
                    }
                ]
            })
        })
    },


    initComponent: function () {
        var me = this;
        var today = new Date();
        Ext.Date.clearTime(today);

        var daysMonth = {
            timeColumnWidth: 100,
            displayDateFormat: 'd.m',
            shiftIncrement: 2,
            shiftUnit: 'MONTH',
            timeResolution: {
                unit: 'DAY',
                increment: 1
            },
            defaultSpan: 60,
            headerConfig: {
                top: {
                    unit: 'MONTH',
                    dateFormat: 'M Y'
                },
                middle: {
                    unit: 'DAY',
                    increment: 1,
                    dateFormat: 'D d'
                }
            }
        };

        Sch.preset.Manager.registerPreset('daysMonth', daysMonth);
        var eventLimit = 0;
        me.eventStore = Ext.widget('otaCalendarEventStore');
        me.resourceStore = Ext.widget('otaCalendarResourceStore').load({
            params: Pms.Ajax.encode({
                filter: [
                    {
                        field: 'approved',
                        comparison: 'eq',
                        data: {
                            type: 'boolean',
                            value: true
                        }
                    }
                ]
            }),
            callback: function (records) {
                eventLimit = records.length * 31;
                me.eventStore.pageSize = eventLimit;
                me.eventStoreLoadByDate(today);

            }
        });
//        console.log(me.eventStore);
//        console.log(me.resourceStore);
        me.items = this.createScheduler();
        me.callParent(arguments);
    },
    schClick: function (scheduler, clickedDate, rowIndex, resource, e, eOpts) {
        var win = Ext.widget('otaCalendarAddWindow', {
            date: clickedDate,
            virtualRoom: resource.data
        });
        win.show();
    },

    createScheduler: function () {
        var me = this;
        var today = new Date();
        var firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
        var lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 1);

        function eventStoreScrollToMonth(month) {
            sched.scrollToDate(new Date(today.getFullYear(), month, '01'));
            sched.setStart(new Date(today.getFullYear(), month, 1));
            sched.setEnd(new Date(today.getFullYear(), month + 1, 1));

            me.eventStoreLoadByDate(new Date(today.getFullYear(), month, 1));
        }

        var sched = Ext.create('Sch.panel.SchedulerGrid', {
            barMargin: 3,
            rowHeight: 40,
            enabledHdMenu: false,
            readOnly: true,
            columns: [
//                {
//                    header: 'Тариф',
//                    sortable: false,
//                    width: 150,
//                    dataIndex: 'title'
//                },
                {
                    header: l('virtualRoom.name'),
                    sortable: false,
                    width: 200,
                    dataIndex: 'name',
                    renderer: function (val, metaData, rec) {
                        return val + ' ' + rec.data.adults + '/' + rec.data.children + '/' + rec.data.additional
                    }
                }
            ],
            startDate: today,
            endDate: lastDay,
            viewPreset: 'daysMonth',
            border: false,
            stateful: true,
            resourceStore: me.resourceStore,
            eventStore: me.eventStore,
            listeners: {
                scheduleclick: me.schClick,
                activate: function () {
                    sched.scrollToDate(new Date())
                }
            },
            eventRenderer: function (item, resourceRec, tplData, row, col, ds) {
                if (item.data.otaAllowed) {
                    tplData.style = 'background-color:' + '#64FE2E'
                }
                else {
                    tplData.style = 'background-color:' + '#F78181'
                }
                return {
                    headerText: '<span style="float:right;font-weight:bold;font-size:110%;">' + item.data.minStay + '/' + item.data.minStayArrival + '/' + item.data.maxStay + '</span>',
                    footerText: '<div>' + '</div>'
                }
            },
            eventBodyTemplate: '<div class="value">{headerText}<br />{footerText}</div>',
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    items: [
//                        {
//                            text: l('refresh'),
//                            iconCls: 'x-tbar-loading',
//                            handler: function (btn) {
//                                sched.eventStore.reload();
//                            }
//                        },
//                        '|',
//                        {
//                            text: l('setPrices.period'),
//                            iconCls: 'x-tbar-calendar',
//                            handler: function (btn) {
//                                var win = Ext.widget('otaCalendarAddPeriodWindow');
//                                win.show();
//                            }
//                        },
//                        ' ',
//                        {
//                            xtype: 'slider',
//                            width: 100,
//                            value: 80,
//                            increment: 10,
//                            minValue: 30,
//                            maxValue: 150,
//
//                            listeners: {
//                                change: function (sli, v) {
//                                    var schedulingView = sched.getSchedulingView();
//                                    schedulingView.setRowHeight(v);
//                                }
//                            }
//                        },
                        '->',
                        {
                            text: l('backward'),
                            handler: function () {
                                var month = sched.getStart().getMonth() - 1;
                                eventStoreScrollToMonth(month);
                            }
                        },
                        {
                            text: l('toCurrentDate'),
                            iconCls: 'app-icon-calendar',
                            handler: function () {
                                var month = new Date().getMonth();
                                sched.scrollToDate(new Date());
                                sched.setStart(new Date(today.getFullYear(), month, 1));
                                sched.setEnd(new Date(today.getFullYear(), month + 1, 1));

                                me.eventStoreLoadByDate(new Date(today.getFullYear(), month, 1));
                            }
                        },
                        {
                            text: l('forward'),
                            handler: function () {
                                var month = sched.getStart().getMonth() + 1;
                                eventStoreScrollToMonth(month);
                            }
                        },
                        '-',
                        {
                            text: l('month.jan'),
                            action: 'jan',
                            style: {'background-color': '#81DAF5'},
                            handler: function () {
                                eventStoreScrollToMonth(0);
                            }
                        },
                        {
                            text: l('month.feb'),
                            action: 'feb',
                            style: {'background-color': '#81DAF5'},
                            handler: function () {
                                eventStoreScrollToMonth(1);
                            }
                        },
                        {
                            text: l('month.mar'),
                            action: 'mar',
                            style: {'background-color': '#81F781'},
                            handler: function () {
                                eventStoreScrollToMonth(2);
                            }
                        },
                        {
                            text: l('month.apr'),
                            action: 'apr',
                            style: {'background-color': '#81F781'},
                            handler: function () {
                                eventStoreScrollToMonth(3);
                            }
                        },
                        {
                            text: l('month.may'),
                            action: 'may',
                            style: {'background-color': '#81F781'},
                            handler: function () {
                                eventStoreScrollToMonth(4);
                            }
                        },
                        {
                            text: l('month.june'),
                            action: 'jun',
                            style: {'background-color': '#F3F781'},
                            handler: function () {
                                eventStoreScrollToMonth(5);
                            }
                        },
                        {
                            text: l('month.july'),
                            action: 'jul',
                            style: {'background-color': '#F3F781'},
                            handler: function () {
                                eventStoreScrollToMonth(6);
                            }
                        },
                        {
                            text: l('month.aug'),
                            action: 'aug',
                            style: {'background-color': '#F3F781'},
                            handler: function () {
                                eventStoreScrollToMonth(7);
                            }
                        },
                        {
                            text: l('month.sept'),
                            action: 'sep',
                            style: {'background-color': '#FE642E'},
                            handler: function () {
                                eventStoreScrollToMonth(8);
                            }
                        },
                        {
                            text: l('month.oct'),
                            action: 'oct',
                            style: {'background-color': '#FE642E'},
                            handler: function () {
                                eventStoreScrollToMonth(9);
                            }
                        },
                        {
                            text: l('month.nov'),
                            action: 'nov',
                            style: {'background-color': '#FE642E'},
                            handler: function () {
                                eventStoreScrollToMonth(10);
                            }
                        },
                        {
                            text: l('month.dec'),
                            action: 'dec',
                            style: {'background-color': '#81DAF5'},
                            handler: function () {
                                eventStoreScrollToMonth(11);
                            }
                        }
                    ]
                },
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    items: [
                        {
                            text: l('refresh'),
                            iconCls: 'x-tbar-loading',
                            handler: function (btn) {
                                sched.eventStore.reload();
                            }
                        },
                        '|',
                        {
                            text: l('setPrices.period'),
                            iconCls: 'x-tbar-calendar',
                            handler: function (btn) {
                                var win = Ext.widget('otaCalendarAddPeriodWindow');
                                win.show();
                            }
                        },
                        ' ',
                        {
                            xtype: 'slider',
                            width: 100,
                            value: 40,
                            increment: 10,
                            minValue: 20,
                            maxValue: 100,

                            listeners: {
                                change: function (sli, v) {
                                    var schedulingView = sched.getSchedulingView();
                                    schedulingView.setRowHeight(v);
                                }
                            }
                        },
//                        '',
//                        {
//                            text: '+',
//                            handler: function (btn) {
//                                var zoomLevelIndex = sched.getCurrentZoomLevelIndex();
//                                if (zoomLevelIndex <= 12) {
//                                    sched.zoomIn();
//                                }
//
//                            }
//                        }, {
//                            text: '-',
//                            handler: function () {
//                                var zoomLevelIndex = sched.getCurrentZoomLevelIndex();
//                                if (zoomLevelIndex >= 11.5) {
//                                    sched.zoomOut();
//                                }
//                            }
//                        }
                    ]
                }
            ]
        });
        return sched;
    }
});