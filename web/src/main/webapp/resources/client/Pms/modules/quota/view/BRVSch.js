Ext.define('Pms.modules.quota.view.BRVSch', {
    extend: 'Pms.abstract.Panel',
    alias: 'widget.brvSch',

    border: false,
    today: Ext.Date.clearTime(new Date()),// 00:00:00
    todayEnd: Sch.util.Date.add(Ext.Date.clearTime(new Date()), Sch.util.Date.DAY, +1), // +1 day 00:00:00
    startDate: Sch.util.Date.add(Ext.Date.clearTime(new Date()), Sch.util.Date.DAY, 0),
    endDate: Sch.util.Date.add(Ext.Date.clearTime(new Date()), Sch.util.Date.DAY, 60),

    initComponent: function () {
        var me = this;

        me.listeners = {
            afterrender: function () {
                me.eventStore.reload()
            }
        };

        var daysMonth = {
//            timeColumnWidth: 100,
            displayDateFormat: 'd.m',
            shiftIncrement: 2,
            shiftUnit: 'MONTH',
            timeResolution: {
                unit: 'SECOND',
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
        me.eventStore = Ext.create('Pms.modules.quota.store.BRVEvent', {startDate: me.startDate, endDate: me.endDate});
        me.resourceStore = Ext.create('Pms.modules.quota.store.BRVResource');

        me.items = me.createScheduler();
        me.callParent(arguments);
    },

    createScheduler: function () {
        var me = this,
            today = me.today;

//        console.log(me.resourceStore);
//        console.log(me.eventStore);
        var sched = Ext.create('Sch.panel.SchedulerTree', {
//            title            : l('quota'),
//            height           : ExampleDefaults.height,
//            width            : ExampleDefaults.width,
            rowHeight: 40,
            eventStore: me.eventStore,
            resourceStore: me.resourceStore,
            useArrows: true,
            viewPreset: 'daysMonth',
            startDate: me.startDate,
            endDate: me.endDate,
            multiSelect: true,
            readOnly: true,
            enableDragCreation: false,
            enableEventDragDrop: false,
            eventResizeHandles: 'none',

            eventRenderer: function(event, resource, tplData, row, col, ds) {
                tplData.style = 'background-color:#CEF6F5;';
                if(event.get('closed') == 'CLOSED') tplData.style = 'background-color:#F7D358;';
                if(event.get('closed') == 'CLOSED_TO_CHECKIN') tplData.style = 'background-color:#F4FA58;';
                if(event.get('closedToDeparture')) tplData.style = 'background-color:#F5A9A9;';
                tplData.style += 'height:30px;';

                var minStay = event.get('minStay') ? event.get('minStay') : 0,
                    minStayArrival = event.get('minStayArrival') ? event.get('minStayArrival') : 0,
                    maxStay = event.get('maxStay') ? event.get('maxStay') : 0,
                    OTA = '';

                if(event.get('otaAllowed') == true) OTA = '<b>OTA</b>';

                return {
                    headerText: minStay + '(' + minStayArrival + ')/' + maxStay,
                    footerText: OTA
                }
            },
            eventBodyTemplate: new Ext.XTemplate(
                '<div class="value">{headerText}<br />{footerText}</div>'
            ),
            tipCfg: {
                showDelay: 10,
                autoHide: false,
                anchor: 'b',
                cls: 'sch-event-info'
            },
            tooltipTpl: new Ext.XTemplate(
                    '<tpl if="otaAllowed"><div><b>OTA</b></div></tpl>' +
                    '<div>' + l("ota.closed") + ': <tpl switch="closed">' +
                    '<tpl case="CLOSED">' + l('closed.CLOSED') +
                    '<tpl case="CLOSED_TO_CHECKIN">' + l('closed.CLOSED_TO_CHECKIN') +
                    '<tpl case="OPEN">' + l('closed.OPEN') +
                    '<tpl default> -' +
                    '</tpl></div>' +
                    '<tpl if="closedToDeparture"><div>' + l("otaCalendar.closedToDeparture") + '</div></tpl>' +
                    '<div>' + l("otaCalendar.minStay") + ': {minStay}</div>' +
                    '<div>' + l("otaCalendar.minStayArrival") + ': {minStayArrival}</div>' +
                    '<div>' + l("otaCalendar.maxStay") + ': {maxStay}</div>'
            ),
            columnLines: false,
            rowLines: true,

            columns: [
                {
                    xtype: 'treecolumn',
                    text: l('roomType'),
                    width: 175,
                    sortable: true,
                    dataIndex: 'name'
                },
                {
                    text: l('adults'),
                    width: 80,
                    sortable: true,
                    dataIndex: 'adults'
                },
                {
                    text: l('children'),
                    width: 80,
                    sortable: true,
                    dataIndex: 'children'
                },
                {
                    text: l('additional'),
                    width: 80,
                    sortable: true,
                    dataIndex: 'additional'
                }
            ],
            dragConfig: { enableCopy: true },
            plugins: [
                Ext.create('Sch.plugin.CurrentTimeLine'),
                Ext.create("Sch.plugin.DragSelector")
            ],
            updatablePeriod: true,
            updateDatePeriod: function () {
                var eventStore = sched.getEventStore();
                eventStore.startDate = sched.getStart();
                eventStore.endDate = sched.getEnd();
                eventStore.load();
            },
            listeners: {
                eventdblclick: function(sch, rec, item, index, e) {
                    sch.up('brvSch').fireEvent('openeditwindow', [rec]);
                },
                eventcontextmenu: me.onEventContextMenu,
                zoomchange: function (sch, level, eOpts) {
                    if (sched.updatablePeriod) sched.updateDatePeriod();
                },
                scrollchange: function () {
                    if (sched.updatablePeriod) sched.updateDatePeriod();
                }
            },
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    items: [
                        {
                            xtype: 'toolbar',
                            defaults: {
                                scale: 'small',
                                iconAlign: 'left',
                                width: '60'
                            },
                            items: [
                                {
                                    text: l('quota.setPeriod'),
                                    action: 'setperiod',
                                    iconCls: 'app-icon-calendar'
                                }
                            ]
                        },
                        {
                            xtype: 'toolbar',
                            defaults: {
                                scale: 'small',
                                iconAlign: 'left',
                                width: '60'
                            },
                            items: [
                                    l('scale') + ':',
                                {
                                    text: '+',
                                    handler: function(btn) {
                                        var zoomLevelIndex = sched.getCurrentZoomLevelIndex();
                                        if(zoomLevelIndex < 12 && zoomLevelIndex >= 8) {
                                            sched.zoomIn();
                                        }
                                    }
                                }, {
                                    text: '-',
                                    handler: function() {
                                        var zoomLevelIndex = sched.getCurrentZoomLevelIndex();
                                        if(zoomLevelIndex <= 13 && zoomLevelIndex > 9) {
                                            sched.zoomOut();
                                        }
                                    }
                                }]
                        }
                    ]
                },
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    items: ['->', {
                        text: l('refresh'),
                        iconCls: 'x-tbar-loading',
                        handler: function(btn) {
                            me.resourceStore.reload();
                            me.eventStore.reload();
                        }
                    }, {
                        xtype: 'pmsdatefield',
                        format: 'd/m/y',
                        trigger1Cls: 'x-form-clear-trigger',
                        trigger2Cls: 'x-form-date-trigger',
                        onTrigger1Click: function() {
                            this.setValue('');
                            sched.scrollToDate(/*Sch.util.Date.add(*/Ext.Date.clearTime(new Date())/*, Sch.util.Date.DAY, -6)*/, true);
                            sched.fireEvent('scrollchange');
                        }
                    }, {
                        text: l('goToDate'),
                        iconCls: 'go-to-date',
                        handler: function(btn) {
                            var date = btn.up('toolbar').down('pmsdatefield').getValue();
                            if(!Ext.isEmpty(date)) {
                                sched.scrollToDate(Ext.Date.clearTime(date), true);

                                sched.fireEvent('scrollchange');

                                me.zoneStore.loadData({StartDate: date, EndDate: Sch.util.Date.add(date, Sch.util.Date.DAY, +1), Type: 'Type', Cls: 'go-to-date-zone'});
                                sched.getView().refresh();
                            }
                        }
                    }, '-', {
                        text: l('backward'),
                        handler: function(btn) {
                            sched.shiftPrevious();
                            sched.fireEvent('scrollchange');
                        }
                    }, {
                        text: l('toCurrentDate'),
                        iconCls: 'app-icon-calendar',
                        handler: function(btn) {
                            sched.scrollToDate(Ext.Date.clearTime(new Date()), true);
                            sched.fireEvent('scrollchange');
                        }
                    }, {
                        text: l('forward'),
                        handler: function(btn) {
                            sched.shiftNext();
                            sched.fireEvent('scrollchange');
                        }
                    }, '-', {
                        text: l('month.jan'),
                        style: {'background-color': '#81DAF5'},
                        handler: function(btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '00', '01'));
                            sched.fireEvent('scrollchange');
                        }
                    }, {
                        text: l('month.feb'),
                        style: {'background-color': '#81DAF5'},
                        handler: function(btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '01', '01'));
                            sched.fireEvent('scrollchange');
                        }
                    }, {
                        text: l('month.mar'),
                        style: {'background-color': '#81F781'},
                        handler: function(btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '02', '01'));
                            sched.fireEvent('scrollchange');
                        }
                    }, {
                        text: l('month.apr'),
                        style: {'background-color': '#81F781'},
                        handler: function(btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '03', '01'));
                            sched.fireEvent('scrollchange');
                        }
                    }, {
                        text: l('month.may'),
                        style: {'background-color': '#81F781'},
                        handler: function(btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '04', '01'));
                            sched.fireEvent('scrollchange');
                        }
                    }, {
                        text: l('month.june'),
                        style: {'background-color': '#F3F781'},
                        handler: function(btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '05', '01'));
                            sched.fireEvent('scrollchange');
                        }
                    }, {
                        text: l('month.july'),
                        style: {'background-color': '#F3F781'},
                        handler: function(btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '06', '01'));
                            sched.fireEvent('scrollchange');
                        }
                    }, {
                        text: l('month.aug'),
                        style: {'background-color': '#F3F781'},
                        handler: function(btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '07', '01'));
                            sched.fireEvent('scrollchange');
                        }
                    }, {
                        text: l('month.sept'),
                        style: {'background-color': '#FE642E'},
                        handler: function(btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '08', '01'));
                            sched.fireEvent('scrollchange');
                        }
                    }, {
                        text: l('month.oct'),
                        style: {'background-color': '#FE642E'},
                        handler: function(btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '09', '01'));
                            sched.fireEvent('scrollchange');
                        }
                    }, {
                        text: l('month.nov'),
                        style: {'background-color': '#FE642E'},
                        handler: function(btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '10', '01'));
                            sched.fireEvent('scrollchange');
                        }
                    }, {
                        text: l('month.dec'),
                        style: {'background-color': '#81DAF5'},
                        handler: function(btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '11', '01'));
                            sched.fireEvent('scrollchange');
                        }
                    }]
                }
            ]
        });
        return sched;
    },

    onEventContextMenu: function (sch, rec, e, eOpts) {
        var me = this,
            rows = sch.getSelectionModel().getSelection();

        rows = rows.length ? rows : [rec];

        e.stopEvent();
        sch.ctx = new Ext.menu.Menu({
            items: [{
                text: l('tooltip.edit'),
                iconCls: 'app-icon-edit',
                handler: function (btn, e) {
                    sch.up('brvSch').fireEvent('openeditwindow', rows);
                }
            },{
                text: l('tooltip.delete'),
                iconCls: 'app-icon-remove',
                handler: function (btn, e) {
                    sch.up(me.up('panel').getXType()).fireEvent('deleteevent', sch, rows, e, me.up('panel').getXType());
                }
            }],
            listeners: {
                hide: function () {
                    sch.ctx.destroy();
                }
            }
        });
        sch.ctx.rec = rec;
        sch.ctx.showAt(e.getXY());
    }

//    onResourceContextMenu: function (grid, rec, row, index, e) {
//        e.stopEvent();
//        grid.ctx = new Ext.menu.Menu({
//            items: [{
//                text: l('tooltip.commit'),
//                iconCls: 'app-icon-commit',
//                disabled: rec.data.approved,
//                handler: function (btn, e) {
//                    grid.up('quotaSch').fireEvent('commitplan', grid, rec, e);
//                }
//            }],
//            listeners: {
//                hide: function () {
//                    grid.ctx.destroy();
//                }
//            }
//        });
//        grid.ctx.rec = rec;
//        grid.ctx.showAt(e.getXY());
//    }
});