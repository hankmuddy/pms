Ext.define('Pms.modules.plan.view.PlanSch', {
    extend: 'Pms.abstract.Panel',
    requires: ['Sch.panel.SchedulerGrid'],
    alias: 'widget.planSch',

    border: false,

    initComponent: function () {
        var me = this;
        var today = new Date();
        var dateToday = today;
        Ext.Date.clearTime(today);
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
        me.eventStore = Ext.create('Pms.modules.season.store.Season').load();
        me.resourceStore = Ext.create('Pms.modules.plan.store.Plan').load(
            /*{scope: this, callback: function(records, operation, success) {
             console.log('resourceStore loaded');
             console.log(records, operation, success);
             if(success) {
             me.eventStore.load({scope: this, callback: function(records, operation, success) {
             console.log('eventStore loaded');
             console.log(records, operation, success);
             if(success) {
             for(var i in records) {
             records[i].data.until = Ext.Date.add(records[i].data.until, Ext.Date.SECOND, 86399);
             }
             }
             }});
             }
             }}*/);

        me.zoneStore = Ext.create('Ext.data.JsonStore', {
            model: Ext.define('Zone', {
                extend: 'Sch.model.Range', fields: ['Type']
            })
        });

        me.items = this.createScheduler();
        me.callParent(arguments);
    },

    createScheduler: function () {
        var me = this,
            today = new Date();

        Ext.Date.clearTime(today);

        var sched = Ext.create('Sch.panel.SchedulerGrid', {
            barMargin: 5,
            rowHeight: 30,
            enabledHdMenu: false,
            readOnly: false,
            enableEventDragDrop: false,
            eventResizeHandles: 'none',
            multiSelect: true,
            columns: [
                {
                    xtype: 'rownumberer',
                    header: '№',
                    width: 30,
                    sortable: false,
                    shrinkWrap: 3,
                    renderer: function (value, meta, record) {
                        return record.index + 1;
                    }
                },
                {
                    header: l('title'),
                    dataIndex: 'name',
                    minWidth: 120,
                },
                {
                    header: l('living.gridBoard'),
                    dataIndex: 'board',
                    width: 55
                },
                {
                    header: l('approved'),
                    xtype: 'booleancolumn',
                    width: 50,
                    dataIndex: 'approved',
                    renderer: function (value, meta, record) {
                        value = value ? Pms.iconOk : Pms.iconRemove;
                        if(record.data.defaultPlan)
                            return value + ' <img src="themes/default/images/icons/gray/icons/key.png" />';
                        return value
                    }
                }
            ],
            startDate: today,
            endDate: Sch.util.Date.add(today, Sch.util.Date.DAY, 60),
            viewPreset: 'daysMonth',
            border: false,
            stateful: true,
            resourceStore: me.resourceStore,
            eventStore: me.eventStore,
            plugins: [
//                Ext.create('Pms.modules.plan.view.PlanSeasonEditor'),
                Ext.create('Sch.plugin.CurrentTimeLine'),
                Ext.create('Sch.plugin.Zones', {
                    pluginId: 'zonePlugin',
                    showHeaderElements: true,
                    innerTpl: '<span class="zone-type">{Type}</span>',
                    store: me.zoneStore
                })
            ],
            listeners: {
                itemdblclick: function (grid, rec, item, index, e) {
                    grid.up('planSch').fireEvent('editplan', grid, rec, item, index, e);
                },
                itemcontextmenu: this.onResourceContextMenu,
                eventcontextmenu: this.onEventContextMenu,
                dragcreateend: function (sch, rec, resource, e, eOpts) {
                    // UTC shift start
                    rec.data.start = new Date(Pms.toUTC(rec.data.start) * 1000);
                    rec.data.until = new Date(Pms.toUTC(rec.data.until) * 1000);
                    // UTC shift end
                    sch.up('planSch').fireEvent('newseason', sch, rec, resource, e, eOpts);
                },
                eventdblclick: function (sch, rec, e, r) {
                    sch.up('planSch').fireEvent('editseason', sch, rec, e, r);
                }
            },
            eventRenderer: function (item, resourceRec, tplData, row, col, ds) {
                if (item.data.approved) tplData.style = 'background-color:#64FE2E;';

                var zoomLevel = sched.getCurrentZoomLevelIndex();

                if (zoomLevel == 11 || zoomLevel == 12) {
                    tplData.width += sched.getTimeColumnWidth();
                }
                else if (zoomLevel == 10) {
                    tplData.width += 6;
                }
                else if (zoomLevel == 9) {
                    tplData.width += 4;
                }

                return {
                    headerText: '<div>' + Ext.Date.format(item.data.start, 'd/m/Y') + ' - ' + Ext.Date.format(item.data.until, 'd/m/Y') + '</div>',
                    footerText: ''//'<div>' + item.data.minStay + '&mdash;' + item.data.minStayArrival + '/' + item.data.maxStay + '</div>'
                }
            },
            eventBodyTemplate: '<div class="value">{headerText}<br />{footerText}</div>',
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
                                    text: l('plan.addBtn'),
                                    action: 'new',
                                    iconCls: 'pms-add-icon-16'
                                }
//                                {
//                                    text: l('edit.btn'),
//                                    action: 'edit',
//                                    iconCls: 'app-icon-edit2',
//                                    gridAction: true
//                                },
//                                {
//                                    text: l('commit.btn'),
//                                    action: 'commit',
//                                    iconCls: 'pms-success-icon-16',
//                                    gridAction: true
//                                },
//                                {
//                                    text: l('delete.btn'),
//                                    action: 'delete',
//                                    iconCls: 'pms-delete-icon-16',
//                                    gridAction: true
//                                }
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
                                    handler: function (btn) {
                                        var zoomLevelIndex = sched.getCurrentZoomLevelIndex();
//                                        console.log(zoomLevelIndex);
                                        if (zoomLevelIndex < 12 && zoomLevelIndex >= 8) {
                                            sched.zoomIn();
                                        }
                                    }
                                }, {
                                    text: '-',
                                    handler: function () {
                                        var zoomLevelIndex = sched.getCurrentZoomLevelIndex();
//                                        console.log(zoomLevelIndex);
                                        if (zoomLevelIndex <= 13 && zoomLevelIndex > 9) {
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
                        handler: function (btn) {
                            me.resourceStore.reload();
                            me.eventStore.load(function (records, operation, success) {
//                                console.log('reload', records, operation, success);
                                for (var i in records) {
                                    records[i].data.until = Ext.Date.add(records[i].data.until, Ext.Date.SECOND, 86399);
                                }
                            });
                        }
                    }, {
                        xtype: 'pmsdatefield',
                        format: 'd/m/y',
                        trigger1Cls: 'x-form-clear-trigger',
                        trigger2Cls: 'x-form-date-trigger',
                        onTrigger1Click: function () {
                            this.setValue('');
                            sched.scrollToDate(/*Sch.util.Date.add(*/Ext.Date.clearTime(new Date())/*, Sch.util.Date.DAY, -6)*/, true);
                        }
                    }, {
                        text: l('goToDate'),
                        iconCls: 'go-to-date',
                        handler: function (btn) {
                            var date = btn.up('toolbar').down('pmsdatefield').getValue();
                            if (!Ext.isEmpty(date)) {
                                sched.scrollToDate(Ext.Date.clearTime(date), true);
                                me.zoneStore.loadData({StartDate: date, EndDate: Sch.util.Date.add(date, Sch.util.Date.DAY, +1), Type: 'Type', Cls: 'go-to-date-zone'});
                                sched.getView().refresh();
                            }
                        }
                    }, '-', {
                        text: l('backward'),
                        handler: function (btn) {
                            sched.shiftPrevious();
                        }
                    }, {
                        text: l('toCurrentDate'),
                        iconCls: 'app-icon-calendar',
                        handler: function (btn) {
                            sched.scrollToDate(Ext.Date.clearTime(new Date()), true);
                        }
                    }, {
                        text: l('forward'),
                        handler: function (btn) {
                            sched.shiftNext();
                        }
                    }, '-', {
                        text: l('month.jan'),
                        style: {'background-color': '#81DAF5'},
                        handler: function (btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '00', '01'));
                        }
                    }, {
                        text: l('month.feb'),
                        style: {'background-color': '#81DAF5'},
                        handler: function (btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '01', '01'));
                        }
                    }, {
                        text: l('month.mar'),
                        style: {'background-color': '#81F781'},
                        handler: function (btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '02', '01'));
                        }
                    }, {
                        text: l('month.apr'),
                        style: {'background-color': '#81F781'},
                        handler: function (btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '03', '01'));
                        }
                    }, {
                        text: l('month.may'),
                        style: {'background-color': '#81F781'},
                        handler: function (btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '04', '01'));
                        }
                    }, {
                        text: l('month.june'),
                        style: {'background-color': '#F3F781'},
                        handler: function (btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '05', '01'));
                        }
                    }, {
                        text: l('month.july'),
                        style: {'background-color': '#F3F781'},
                        handler: function (btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '06', '01'));
                        }
                    }, {
                        text: l('month.aug'),
                        style: {'background-color': '#F3F781'},
                        handler: function (btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '07', '01'));
                        }
                    }, {
                        text: l('month.sept'),
                        style: {'background-color': '#FE642E'},
                        handler: function (btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '08', '01'));
                        }
                    }, {
                        text: l('month.oct'),
                        style: {'background-color': '#FE642E'},
                        handler: function (btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '09', '01'));
                        }
                    }, {
                        text: l('month.nov'),
                        style: {'background-color': '#FE642E'},
                        handler: function (btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '10', '01'));
                        }
                    }, {
                        text: l('month.dec'),
                        style: {'background-color': '#81DAF5'},
                        handler: function (btn) {
                            sched.scrollToDate(new Date(today.getFullYear(), '11', '01'));
                        }
                    }]
                }
            ]
        });
        return sched;
    },

    onEventContextMenu: function (sch, rec, e, r) {
        var me = this,
            rows = sch.getSelectionModel().getSelection();

        rows = rows.length ? rows : [rec];

        e.stopEvent();
        sch.ctx = new Ext.menu.Menu({
            items: [
                {
                    text: l('tooltip.commit'),
                    iconCls: 'app-icon-commit',
                    disabled: rec.data.approved,
                    handler: function (btn, e) {
                        sch.up('planSch').fireEvent('commitseason', sch, rows, e, r);
                    }
                },
                {
                    text: l('tooltip.delete'),
                    iconCls: 'app-icon-remove',
                    disabled: rec.data.approved,
                    handler: function (btn, e) {
                        sch.up('planSch').fireEvent('deleteseason', sch, rec, e, r);
                    }
                }
            ],
            listeners: {
                hide: function () {
                    sch.ctx.destroy();
                }
            }
        });
        sch.ctx.rec = rec;
        sch.ctx.showAt(e.getXY());
    },
    onResourceContextMenu: function (grid, rec, row, index, e) {

        e.stopEvent();
        grid.ctx = new Ext.menu.Menu({
            items: [
                {
                    text: l('tooltip.commit'),
                    iconCls: 'app-icon-commit',
                    disabled: rec.data.approved,
                    handler: function (btn, e) {
                        grid.up('planSch').fireEvent('commitplan', grid, rec, e);
                    }
                },
                {
                    text: l('tooltip.delete'),
                    iconCls: 'app-icon-remove',
                    disabled: rec.data.approved,
                    handler: function (btn, e) {
                        grid.up('planSch').fireEvent('deleteplan', grid, rec, e);
                    }
                },
                {
                    text: l('setDefault'),
                    iconCls: 'app-icon-key',
                    disabled: !rec.data.approved,
                    handler: function (btn, e) {
                        grid.up('planSch').fireEvent('defaultplan', grid, rec, e);
                    }
                }
            ],
            listeners: {
                hide: function () {
                    grid.ctx.destroy();
                }
            }
        });
        grid.ctx.rec = rec;
        grid.ctx.showAt(e.getXY());
    }
});