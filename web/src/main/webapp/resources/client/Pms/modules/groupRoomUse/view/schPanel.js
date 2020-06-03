Ext.define('Pms.modules.groupRoomUse.view.schPanel', {
    extend: 'Pms.abstract.Panel',
    alias: 'widget.schPanel',

    requires: ['Sch.panel.SchedulerGrid'],
    border: false,
    initComponent: function () {
        var me = this;
        var today = new Date();

        var daysMonth = {
            timeColumnWidth: 40,
            displayDateFormat: 'd/m H:i',
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
                    dateFormat: 'D d',
                    headerCls: 'today-zone',
                    renderer: function (date, a1, a2) {
                        if (new Date(date).getDate() == new Date().getDate() && new Date(date).getMonth() == new Date().getMonth() && new Date(date).getFullYear() == new Date().getFullYear()) {
                            return '<font color="#045FB4">' + Ext.Date.format(date, 'D d') + '</font>';
                        }
                        else if (new Date(Ext.Date.clearTime(date)).getDay() == 0 || new Date(Ext.Date.clearTime(date)).getDay() == 6) {
                            return '<font color="red">' + Ext.Date.format(date, 'D d') + '</font>';
                        }
                        else {
                            return Ext.Date.format(date, 'D d');
                        }
                    }
                }
                // bottom: {
                //     unit: 'HOUR',
                //     increment: 12,
                //     dateFormat: 'G:i'
                // }
            }
        };
        Sch.preset.Manager.registerPreset('daysMonth', daysMonth);

        me.resourceStore = Ext.widget('resourceStore').load({
            params: {
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
            }
        });
        me.eventStore = Ext.widget('eventStore').load({
            url: 'rest/baseRoomUse',
            scope: this,
            params: {
                startDate: new Date(new Date().valueOf() - 10 * 24 * 60 * 60 * 1000).valueOf(),
                endDate: new Date(new Date().valueOf() + 60 * 24 * 60 * 60 * 1000).valueOf()
            }
        });
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
            today = Ext.Date.clearTime(new Date()),// 00:00:00
            todayEnd = Sch.util.Date.add(today, Sch.util.Date.DAY, +1), // +1 day 00:00:00
            startDate = Sch.util.Date.add(today, Sch.util.Date.DAY, -6),
            endDate = Sch.util.Date.add(today, Sch.util.Date.DAY, 60);

        function eventStoreUpdateByGrid(sched) {

            var firstDay = sched.getStart();
            var lastDay = sched.getEnd();
            var statuses = sched.roomUseStatuses;
            var params = {
                startDate: firstDay.valueOf(),
                endDate: lastDay.valueOf()
            };

            if (statuses) {
                params.statuses = statuses.toString();

            }

            sched.eventStore.load({
                scope: this,
                url: 'rest/baseRoomUse',
                params: params
            });
//            sched.scrollToDate(new Date(today.getFullYear(), month, '01'));
//            sched.setStart(new Date(today.getFullYear(), month, 1));
//            sched.setEnd(new Date(today.getFullYear(), month + 1, 1));
//
//            me.eventStoreLoadByDate(new Date(today.getFullYear(), month, 1));
        }

        var sched = Ext.create('Sch.panel.SchedulerGrid', {
            barMargin: 0,
            rowHeight: 40,
//            fitColumns:true,
            border: true,
            viewPreset: 'daysMonth',
            enableEventDragDrop: false,
            eventResizeHandles: 'end',
            startDate: startDate,
            endDate: endDate,

            roomUseStatuses: null, //Filters by room use

//            stateful: true, // State will be saved by the component
            multiSelect: true,

//            resizeValidatorFn: function (resourceRecord, eventRecord, startDate, endDate, e) {
//                return eventRecord.data.status == 'OUTGO' ? false : truez
//            },


            // Setup static columns
            columns: [
                {
                    header: l('room'),
                    sortable: true,
                    width: 50,
                    dataIndex: 'number'
                },
                {
                    header: l('roomType'),
                    sortable: true,
                    width: 150,
                    dataIndex: 'roomType',
                    renderer: function (value) {
                        return value.name;
                    }
                },
                {
                    header: l('accommodation'),
                    sortable: true,
                    width: 50,
                    dataIndex: 'accommodation',
                    renderer: function (value) {
                        return value.shortName;
                    }
                }
            ],
            resourceStore: me.resourceStore,
            eventStore: me.eventStore,
            listeners: {

                eventcontextmenu: this.onEventContextMenu,
                dragcreateend: this.dragCreateEnd,
                scheduledblclick: this.panelDblClick,
                select: function (eventModel, item) {
                    if (item.data.movedFromId)
                        eventModel.getStore().getById(item.data.movedFromId).setCls('chessboard-moved-right');
                },
                deselect: function (eventModel, item) {
                    if (item.data.movedFromId)
                        eventModel.getStore().getById(item.data.movedFromId).setCls(' ');
                }
            },
            groupColor: {},
            // colours, dates and titles of events
            eventRenderer: function (item, resourceRec, tplData, row, col, ds) {
                var start = item.getStartDate(),
                    end = item.getEndDate(),
                    room = resourceRec.data,
                    arrColor = {
                        BOOKING_FREE: [l('filter.bookingStatus.NotApproved'), 'F78181'],
                        BOOKING_WARRANTY: [l('filter.bookingStatus.Approved'), 'F4FA58'],
                        LIVING: [l('filter.bookingStatus.residence'), '64FE2E'],
                        OUTGO: [l('filter.bookingStatus.departure'), 'FE9A2E'],
//                        REPAIR: [l('filter.bookingStatus.repair'), 'AAAAAA'],
                        REFUSE: [l('filter.bookingStatus.rejection'), 'EEEEEE'],
                        NOT_ARRIVED: [l('filter.bookingStatus.notRide'), 'A785E0']
                    },
                    color = '',
                    masterName = '',
                    paid = '',
                    groupColor = this.up('schedulergrid').groupColor,
                    isGroupSort = this.up('schedulergrid').down('checkbox').value;

                var timeDiff = Math.abs(end.getTime() - start.getTime());
                var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));

                function randomColor() {
                    return '#' + (0x1888888 + (Math.random()) * 0x888887).toString(16).substr(1, 6)
                }

                //Show if it moved

                if (isGroupSort) {
                    groupColor['group' + item.data.customerGroup.id] ? color = groupColor['group' + item.data.customerGroup.id] :
                        color = groupColor['group' + item.data.customerGroup.id] = randomColor();
                }
                if (item.data.id) {
                    if (item.data.status && !isGroupSort)
                        tplData.style = 'background-color:#' + arrColor[item.data.status][1] + ';';
                    else if (isGroupSort) {
                        tplData.style = 'background-color:' + color + ';';
                    }
                    else
                        tplData.style = 'background-color:#AAAAAA;';
                    if (item.data.moved) {
                        tplData.style += 'border-radius: 20px 0 0 20px;';
//                        sched.eventStore.getById(item.data.movedFromId).setCls('chessboard-moved-right');
//                        console.log(sched.eventStore.getById(item.data.movedFromId))
                    }

                    // red border for not fully paid outgo periods
                    if (item.data.useType == 'living' &&
                        item.data.endDate <= Ext.Date.clearTime(new Date()) && !item.data.customerGroup.totalPaid == item.data.customerGroup.total) {
                        tplData.style += 'border:2px solid red;';
                    }
                    if (item.data.customerGroup) {
                        var group = item.data.customerGroup;

                        if (group.company)masterName = group.company.name;
                        else masterName = group.customer.lastName + ' ' + group.customer.firstName;
                    }
                    var moneyStatus = item.data.total ? Ext.util.Format.number(item.data.totalPaid / 100, '0') + '/' + Ext.util.Format.number(item.data.total / 100, '0') : '';
                    if (diffDays <= 1) {
                        return;
                    } else if (diffDays <= 2) {
                        return {
                            footerText: '<span style="float:left; font-weight:bold;font-size:110%;">' + masterName + '</span>' + '<span style="float:right;">' + paid + '</span>',
                            headerText: '<span style="float:left;display: inline-block;">' + moneyStatus + '</span>'
                        };
                    }
                    else {
                        return {
                            footerText: '<span style="float:left; font-weight:bold;font-size:110%;">' + masterName + '</span>' + '<span style="float:right;">' + paid + '</span>',
                            headerText: '<span style="float:left;display: inline-block;">' + moneyStatus + '</span>' + '<span style="float:right;display: inline-block;">' + Ext.Date.format(start, 'd/m') + ' &mdash; ' + Ext.Date.format(end, 'd/m') + '</span>'
                        };
                    }
                }
            },
            tipCfg: {
                showDelay: 50,
                autoHide: false,
                anchor: 'b',
                cls: 'sch-event-info'
            },
            tooltipTpl: new Ext.XTemplate(
                    '<tpl if="customerGroup.customer"><div class="sch-event-info-name">{customerGroup.customer.lastName} {customerGroup.customer.firstName} (' + l('roomUse.phone') + ': {customerGroup.customer.phone})</div> <br/> ' +
                    l('room') + ': {room.number} <br/>' +
                    l('from') + ' {startDate:date("d/m/Y")} ' + l('to') + ' {endDate:date("d/m/Y")} ' +
                    '<tpl if="total"><br/>' + l('roomUse.payedForRoom') + ': {totalPaid:/100} ' + l('fromIz') + ' {total:/100}  </tpl>' +
                    '<tpl if="customerGroup.total"><br />' + l('roomUse.payedForGroup') + ': {customerGroup.totalPaid:/100} ' + l('fromIz') + ' {customerGroup.total:/100}</tpl>' +
                    '</tpl>' +
                    '<tpl if="!customerGroup.customer">' + l('roomUse.responsiblePersonEmpty') + '</tpl>' +
                    '<tpl switch="source">' +
                    '<tpl case="BOOKING">' +
                    '<br /><br />' + l('chessboard.BOOKING') +
                    '<tpl case="BOOKING_BUTTON">' +
                    '<br /><br />' + l('chessboard.BOOKING_BUTTON') +
                    '<tpl case="WUBOOK_BUTTON">' +
                    '<br /><br />' + l('chessboard.BOOKING_BUTTON') +
                    '<tpl case="HRS">' +
                    '<br /><br />' + l('chessboard.HRS') +
                    '<tpl case="OSTROVOK">' +
                    '<br /><br />' + l('chessboard.OSTROVOK') +
                    '<tpl case="EXPEDIA">' +
                    '<br /><br />' + l('chessboard.EXPEDIA') +
                    '<tpl case="HOTELS.COM">' +
                    '<br /><br />' + l('chessboard.HOTELS.COM') +
                    '<tpl case="AGODA.COM">' +
                    '<br /><br />' + l('chessboard.AGODA.COM') +
                    '<tpl case="HOTELS.DE">' +
                    '<br /><br />' + l('chessboard.HOTELS.DE') +
                    '<tpl case="FRONT_DESK">' +
                    '<br /><br />' + l('chessboard.FRONT_DESK') +
                    '<tpl default>' +
                    '<br /><br />' + l('chessboard.unknown') + '({source})' +
                    '</tpl>' +
                    '<br />{[this.updatedBy(values.id, values.type)]}',
                    {
                        updatedBy: function(id, type){
                            var str = '';
                            if (type != 'REPAIR' && id) {
                                Pms.Ajax.request({
                                    url: 'rest/roomUse/' + id,
                                    method: 'GET',
                                    async: false,
                                    success: function (response) {
                                        var createdBy = response.content.createdBy;
                                        var updatedBy = response.content.updatedBy;
                                        if(!Ext.isEmpty(createdBy)) str = l('user.createdBy') + ': ' + createdBy.firstName + ' ' + createdBy.lastName + ' (' + l(createdBy.role.name) + ') ' + l('time.at') + ' ' + Ext.Date.format(Pms.fromUTC(response.content.createdDate), 'H:m');
                                        if(!Ext.isEmpty(updatedBy)) str += '<br />' + l('user.updatedBy') + ': ' + updatedBy.firstName + ' ' + updatedBy.lastName + ' (' + l(updatedBy.role.name) + ')';
                                    }
                                });
                            }
                            return str;
                        }
                    }
            ),
            plugins: [
                Ext.create('Sch.plugin.CurrentTimeLine'),
                Ext.create('Sch.plugin.Zones', {
                    pluginId: 'zonePlugin',
                    showHeaderElements: true,
                    innerTpl: '<span class="zone-type">{Type}</span>',
                    store: me.zoneStore
                })
//                Ext.create('Sch.plugin.Lines', {
//                    showHeaderElements: true,
//                    innerTpl: '<span class="line-text">{Text}</span>',
//                    store: me.lineStore
//                })
            ],
            eventBodyTemplate: new Ext.XTemplate(
//                '<tpl if="moved && movedFrom"><div class="chessboard-moved-left"></div></tpl>' +
//                '<tpl if="moved && !movedFrom"><div class="chessboard-moved-right"></div></tpl>' +
                '<div class="value">{headerText}<br />{footerText}</div>'
            ),
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    items: [
                        {
                            text: l('newGroupReservation'),
                            action: 'new-group-booking',
                            cls: 'btn-bigger-font',
                            iconCls: 'pms-chess-toolbar-icon-group-booking'
                        },
                        {
                            text: l('newArrival'),
                            action: 'new-income',
                            cls: 'btn-bigger-font',
                            iconCls: 'pms-chess-toolbar-icon-new-arrival'
                        },
                        {
                            text: l('newReservation'),
                            action: 'new-booking',
                            cls: 'btn-bigger-font',
                            iconCls: 'pms-chess-toolbar-icon-new-booking'
                        },
                        {
                            text: l('repair'),
                            action: 'new-repair',
                            cls: 'btn-bigger-font',
                            iconCls: 'pms-chess-toolbar-icon-repair'
                        },
                        {
                            text: l('searchBookings'),
                            action: 'search-booking',
                            cls: 'btn-bigger-font',
                            iconCls: 'pms-chess-toolbar-icon-search'
                        },
                        {
                            text: l('clients'),
                            action: 'clients',
                            cls: 'btn-bigger-font',
                            iconCls: 'pms-chess-toolbar-icon-clients'
                        },
                        {
                            text: l('companies'),
                            action: 'companies',
                            cls: 'btn-bigger-font',
                            iconCls: 'pms-chess-toolbar-icon-company'
                        },
                        {
                            text: l('console'),
                            action: 'console',
                            cls: 'btn-bigger-font',
                            iconCls: 'pms-chess-toolbar-icon-console'
                        }
                    ]
                },
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    items: [
                        {
                            xtype: 'toolbar',
                            width: '100%',
                            items: [l('filters') + ':', {
                                xtype: 'combobox',
                                multiSelect: true,
                                hideLabel: true,
                                store: Ext.create('Pms.modules.roomType.store.RoomType'),
                                displayField: 'name',
                                valueField: 'id',
                                triggerAction: 'all',
                                emptyText: l('roomType'),
                                queryMode: 'remote',
                                queryParam: '',
                                selectOnFocus: true,
                                indent: true,
                                padding: '2px 5px',
                                valueNotFoundText: null,
                                forceSelection: true,
                                editable: false,
                                trigger1Cls: 'x-form-clear-trigger',
                                trigger2Cls: 'x-form-arrow-trigger',
                                onTrigger1Click: function () {
                                    this.clearValue();
                                },
                                listeners: {
                                    change: function (combo, newVal, prevVal, e) {
                                        var filter = [];
                                        if (newVal.length) {
                                            for (var i = 0; i < newVal.length; i++) {
                                                filter.push({
                                                    field: 'roomType.id',
                                                    comparison: 'eq',
                                                    data: {
                                                        type: 'numeric',
                                                        value: newVal[i]
                                                    }
                                                });
                                            }
                                            sched.resourceStore.load({
                                                params: {
                                                    connective: 'or',
                                                    filter: filter
                                                }
                                            });
                                        }
                                        else {
                                            sched.resourceStore.load();
                                        }
                                        me.reloadEventStore(sched);
                                    }
                                }
                            }/*, {
                             xtype: 'combobox',
                             hideLabel: true,
                             store: Ext.create('Ext.data.ArrayStore', {
                             fields: ['label', 'value'],
                             data: [
                             ['Убран', true],
                             ['Не убран', false]
                             ]
                             }),
                             displayField: 'label',
                             valueField: 'value',
                             triggerAction: 'all',
                             emptyText: l('roomStatus'),
                             queryMode: 'local',
                             queryParam: '',
                             selectOnFocus: true,
                             indent: true,
                             padding: '2px 5px',
                             trigger1Cls: 'x-form-clear-trigger',
                             trigger2Cls: 'x-form-arrow-trigger',
                             onTrigger1Click: function () {
                             this.clearValue();
                             },
                             listeners: {
                             change: function (combo, newVal, prevVal, e) {
                             var filter = [];
                             if (newVal) {
                             filter.push({
                             field: 'cleaned',
                             comparison: 'eq',
                             data: {
                             type: 'boolean',
                             value: newVal
                             }
                             });
                             sched.resourceStore.load({
                             params: {
                             filter: filter
                             }
                             });
                             }
                             else {
                             sched.resourceStore.load();
                             }
                             me.reloadEventStore(sched);
                             }
                             }
                             }*/, {
                                xtype: 'combobox',
                                multiSelect: true,
                                hideLabel: true,
                                id: 'useTypeCombo',
                                store: Ext.create('Pms.modules.roomUse.store.useType'),
                                displayField: 'label',
                                valueField: 'id',
                                triggerAction: 'all',
                                editable: false,
                                emptyText: l('TypeOfUsing'),
                                queryMode: 'local',
                                queryParam: '',
                                selectOnFocus: true,
                                indent: true,
                                padding: '2px 5px',
                                trigger1Cls: 'x-form-clear-trigger',
                                trigger2Cls: 'x-form-arrow-trigger',
                                onTrigger1Click: function () {
                                    this.clearValue();
                                },
                                listeners: {
                                    change: function (combo, newVal, prevVal, e) {
                                        var filter = [];
                                        if (newVal.length) {
                                            sched.roomUseStatuses = newVal;
                                            console.log(newVal);
                                        }
                                        else {
                                            sched.roomUseStatuses = null;
                                        }
                                        eventStoreUpdateByGrid(sched);
                                    }
                                }
                            },
                                {
                                    xtype: 'checkbox',
                                    boxLabel: l('roomUse.groupSort'),
                                    listeners: {
                                        change: function (box) {
                                            box.up('schedulergrid').down('gridpanel[isLocked=false]').getStore().reload()
                                        }
                                    }
                                },
                                ' ',
                                '',
                                {
                                    xtype: 'slider',
                                    width: 100,
                                    value: 40,
                                    increment: 10,
                                    minValue: 30,
                                    maxValue: 150,

                                    listeners: {
                                        change: function (sli, v) {
                                            var schedulingView = sched.getSchedulingView();
                                            schedulingView.setRowHeight(v);
                                        }
                                    }
                                },
                                '',
                                {
                                    text: '+',
                                    handler: function (btn) {
                                        var zoomLevelIndex = sched.getCurrentZoomLevelIndex();
                                        if (zoomLevelIndex <= 12) {
                                            sched.zoomIn();
                                            eventStoreUpdateByGrid(sched);
                                        }
                                    }
                                }, {
                                    text: '-',
                                    handler: function () {
                                        var zoomLevelIndex = sched.getCurrentZoomLevelIndex();
                                        if (zoomLevelIndex >= 11.5) {
                                            sched.zoomOut();
                                            eventStoreUpdateByGrid(sched);
                                        }
                                    }
                                }
                            ]
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
//                            me.reloadEventStore(sched);
                            eventStoreUpdateByGrid(sched);
                        }
                    }, {
                        xtype: 'pmsdatefield',
                        format: 'd/m/y',
                        trigger1Cls: 'x-form-clear-trigger',
                        trigger2Cls: 'x-form-date-trigger',
                        onTrigger1Click: function () {
                            this.setValue('');
                            sched.scrollToDate(Sch.util.Date.add(Ext.Date.clearTime(new Date()), Sch.util.Date.DAY, -6), true);
                        }
                    }, {
                        text: l('goToDate'),
                        iconCls: 'go-to-date',
                        handler: function (btn) {
                            var date = btn.up('toolbar').down('pmsdatefield').getValue();
                            if (!Ext.isEmpty(date)) {
                                sched.scrollToDate(Ext.Date.clearTime(date), true);
//                                me.zoneStore.loadData({StartDate: date, EndDate: Sch.util.Date.add(date, Sch.util.Date.DAY, +1), Type: 'Type', Cls: 'go-to-date-zone'});
//                                sched.getView().refresh();
                                eventStoreUpdateByGrid(sched);
                            }
                        }
                    }, '-', {
                        text: l('backward'),
                        handler: function () {
                            sched.shiftPrevious();
                            eventStoreUpdateByGrid(sched);
                        }
                    }, {
                        text: l('toCurrentDate'),
                        iconCls: 'app-icon-calendar',
                        handler: function () {
                            sched.scrollToDate(Sch.util.Date.add(Ext.Date.clearTime(new Date()), Sch.util.Date.DAY, -6), true);
                            eventStoreUpdateByGrid(sched);
                        }
                    },
                        {
                            text: l('forward'),
                            handler: function () {
                                sched.shiftNext();
                                eventStoreUpdateByGrid(sched);
                            }
                        }, '-',
                        {
                            text: l('month.jan'),
                            style: {'background-color': '#81DAF5'},
                            handler: function () {
                                sched.scrollToDate(new Date(today.getFullYear(), '00', '01'));
                                eventStoreUpdateByGrid(sched);
                            }
                        }, {
                            text: l('month.feb'),
                            style: {'background-color': '#81DAF5'},
                            handler: function () {
                                sched.scrollToDate(new Date(today.getFullYear(), '01', '01'));
                                eventStoreUpdateByGrid(sched);
                            }
                        }, {
                            text: l('month.mar'),
                            style: {'background-color': '#81F781'},
                            handler: function () {
                                sched.scrollToDate(new Date(today.getFullYear(), '02', '01'));
                                eventStoreUpdateByGrid(sched);
                            }
                        }, {
                            text: l('month.apr'),
                            style: {'background-color': '#81F781'},
                            handler: function () {
                                sched.scrollToDate(new Date(today.getFullYear(), '03', '01'));
                                eventStoreUpdateByGrid(sched);
                            }
                        }, {
                            text: l('month.may'),
                            style: {'background-color': '#81F781'},
                            handler: function () {
                                sched.scrollToDate(new Date(today.getFullYear(), '04', '01'));
                                eventStoreUpdateByGrid(sched);
                            }
                        }, {
                            text: l('month.june'),
                            style: {'background-color': '#F3F781'},
                            handler: function () {
                                sched.scrollToDate(new Date(today.getFullYear(), '05', '01'));
                                eventStoreUpdateByGrid(sched);
                            }
                        }, {
                            text: l('month.july'),
                            style: {'background-color': '#F3F781'},
                            handler: function () {
                                sched.scrollToDate(new Date(today.getFullYear(), '06', '01'));
                                eventStoreUpdateByGrid(sched);
                            }
                        }, {
                            text: l('month.aug'),
                            style: {'background-color': '#F3F781'},
                            handler: function () {
                                sched.scrollToDate(new Date(today.getFullYear(), '07', '01'));
                                eventStoreUpdateByGrid(sched);
                            }
                        }, {
                            text: l('month.sept'),
                            style: {'background-color': '#FE642E'},
                            handler: function () {
                                sched.scrollToDate(new Date(today.getFullYear(), '08', '01'));
                                eventStoreUpdateByGrid(sched);
                            }
                        }, {
                            text: l('month.oct'),
                            style: {'background-color': '#FE642E'},
                            handler: function () {
                                sched.scrollToDate(new Date(today.getFullYear(), '09', '01'));
                                eventStoreUpdateByGrid(sched);
                            }
                        }, {
                            text: l('month.nov'),
                            style: {'background-color': '#FE642E'},
                            handler: function () {
                                sched.scrollToDate(new Date(today.getFullYear(), '10', '01'));
                                eventStoreUpdateByGrid(sched);
                            }
                        }, {
                            text: l('month.dec'),
                            style: {'background-color': '#81DAF5'},
                            handler: function () {
                                sched.scrollToDate(new Date(today.getFullYear(), '11', '01'));
                                eventStoreUpdateByGrid(sched);
                            }
                        }]
                }
            ]
        });
        return sched;
    },

    onEventAddContextMenu: function (s, rec, e, r) {
        e.stopEvent();
        var me = this,
            eventStore = s.getEventStore(),
            rows = s.getSelectionModel().getSelection(),
            rowsCount = rows.length,
            sumCapacity = 0,
            groupBookingData = [];

        if (rows.length > 1) {
            for (var i in rows) {
                if (rows[i].phantom) {
                    var resourceStore = rows[i].store.resourceStore,
                        room_id = rows[i].data.room.id,
                        room = resourceStore.getById(room_id).data;
                    sumCapacity += room.capacity;
                    groupBookingData.push([
                        room_id, room.name, room.roomType.name, room.capacity, rows[i].data.startDate, rows[i].data.endDate
                    ]);
                }
                else rows.splice(i, 1); // delete one element starting from i-th element

            }
        }

        // today.setHours(23,59,59,999);
        if (rows.length > 1) itemsList[1].text += ' (' + sumCapacity + ' ' + l('roomUse.places') + ')';
        if (rows.length < 2) itemsList[1].disabled = true;
        // if(since > today) itemsList[2].disabled = true;

        s.ctx = new Ext.menu.Menu({
            items: itemsList,
            listeners: {
                hide: function () {
                    rec.destroy();
                    s.ctx.destroy();
                }
            }
        });
        s.ctx.rec = rec;
        s.ctx.showAt(Ext.EventObject.getXY());
    },

    onEventContextMenu: function (s, rec, e, r) {
        var me = s.up('schPanel');
        e.stopEvent();
        if (rec.phantom) {
            s.up('schPanel').onEventAddContextMenu(s, rec, e, r);
            return false;
        }
        if (rec.data.useType == 'OUTGO' || rec.data.useType == 'REPAIR' || rec.data.useType == 'REFUSE') return;
        var eventStore = this.getEventStore();
        var useType = rec.data.useType,
            itemsList = [
                {
                    text: l('confirmReservation'),
                    iconCls: 'app-icon-commit',
                    disabled: true,
                    handler: function (grid, rowIndex, colIndex) {
                        var confirmSingle = function () {
                            Ext.Ajax.request({
                                url: 'rest/roomUse/' + rec.data.id + '/confirmed',
                                method: 'POST',
                                success: function () {
                                    me.reloadEventStore(me);
                                },
                                failure: function (res) {
                                    var response = JSON.parse(res.responseText)[0],
                                        code = response.code,
                                        source = response.source;
                                    Ext.Msg.alert(l('error'), Pms.iconError + ' ' + l(code));
                                }
                            })
                        }
                        if (rec.data.customerGroup.roomUsesQuantity == 1) {
                            confirmSingle();
                        }
                        else {
                            Ext.Msg.confirm(l('isAllGroup'), l('booking.groupCommitMsg'), function (btn) {
                                    if (btn == 'yes') {
                                        Ext.Ajax.request({
                                            url: 'rest/roomUse/confirmedByGroup/' + rec.data.customerGroup.id,
                                            method: 'POST',
                                            success: function () {
                                                me.reloadEventStore(me);
                                            },
                                            failure: function (res) {
                                                var response = JSON.parse(res.responseText)[0],
                                                    code = response.code,
                                                    source = response.source;
                                                Ext.Msg.alert(l('error'), Pms.iconError + ' ' + l(code));
                                            }
                                        })
                                    }
                                    else {
                                        confirmSingle();
                                    }
                                }
                            );
                        }
                    }
                },
                {
                    text: l('arrival'),
                    iconCls: 'app-icon-living',
                    disabled: true,
                    handler: function (grid, rowIndex, colIndex) {
                        var win = Ext.widget('groupRoomUseIncomeWindow', {data: rec.data});
                        win.show();
                    }
                },
                {
                    text: l('notArrived'),
                    iconCls: 'app-icon-refuse',
                    disabled: true,
                    handler: function (grid, rowIndex, colIndex) {
                        Ext.Ajax.request({
                            url: 'rest/roomUse/' + rec.data.id + '/notArrived',
                            method: 'POST',
                            success: function () {
                                me.reloadEventStore(me);
                            }
                        })
                    }
                },
                {
                    text: l('addGuests'),
                    iconCls: 'app-icon-income',
                    disabled: true,
                    handler: function (grid, rowIndex, colIndex) {
                        var win = Ext.widget('addGuestWindow', {data: rec.data});
                        win.show();
                    }
                },
                {
                    text: l('move'),
                    iconCls: 'app-icon-move',
                    disabled: true,
                    handler: function (grid, rowIndex, colIndex) {
                        var gruData = rec.data,
                            win = Ext.widget('groupRoomUseMoveWindow', {data: gruData}),
                            gruForm = win.down('groupRoomUseMoveForm');

                        new Date() > gruData.startDate ? gruData.sinceDate = new Date() : gruData.sinceDate = gruData.startDate;

                        gruData.plan = gruData.plan.id;
                        gruData.room = null;
                        gruData.baseRoom = gruData.baseRoom.id;
                        console.log(gruData);
                        gruForm.getForm().setValues(gruData);
                        win.show();
                    }
                },
                {
                    text: l('departure'),
                    iconCls: 'app-icon-outgo',
                    disabled: true,
                    handler: function (grid, rowIndex, colIndex) {
                        var win = Ext.widget('groupRoomUseOutgoWindow', {data: rec.data});
                        win.show();
                    }
                },
                {
                    text: l('refuse'),
                    iconCls: 'app-icon-refuse',
                    disabled: true,
                    handler: function (grid, rowIndex, colIndex) {
                        var win = Ext.widget('groupRoomUseRefuseWindow', {data: rec.data}),
                            form = win.down('form');

                        rec.data.date = new Date() < rec.data.startDate ? rec.data.startDate : new Date() < rec.data.endDate ? new Date() : rec.data.endDate;
                        rec.data.room = rec.data.room.id;

                        form.loadRecord(rec);
                        win.show();
                    }
                },
                {
                    text: l('repair.delete'),
                    iconCls: 'app-icon-refuse',
                    hidden: !(rec.data.type == 'REPAIR'),
                    handler: function (grid, rowIndex, colIndex) {
                        Pms.Ajax.request({
                            url: 'rest/repair/' + rec.data.id,
                            method: 'DELETE'
                        });
                        eventStore.reload();
                    }
                }
            ];
        var status = rec.data.status;
        if (rec.data.type == 'REPAIR') {
            for (var n = 0; n <= 6; n++) {
                itemsList[n].hidden = true;
            }
        }
        if (status == 'BOOKING_FREE') {
            itemsList[6].disabled = false;
            itemsList[0].disabled = false;
            itemsList[2].disabled = false;
            itemsList[3].disabled = false;
            itemsList[4].disabled = false;
        }
        if (status == 'BOOKING_WARRANTY') {
            if (Ext.Date.format(new Date(rec.data.date), 'U') <= Ext.Date.format(new Date(), 'U')) {
                itemsList[1].disabled = false;
            }
            itemsList[3].disabled = false;
            itemsList[4].disabled = false;
            itemsList[6].disabled = false;
        }
        else if (status == 'LIVING') {
            itemsList[3].disabled = false;
            itemsList[4].disabled = false;
            itemsList[5].disabled = false;
            itemsList[6].disabled = false;
        }
        if (rec.data.startDate > new Date(Pms.fromUTC(new Date(), true)))
            itemsList[1].disabled = true;
        if (rec.data.source == 'FRONT_DESK' || status == 'NOT_ARRIVED')
            itemsList[2].disabled = true;
        if (rec.data.source != "FRONT_DESK"/* || rec.data.endDate < new Date(Pms.fromUTC(new Date(), true))*/)
            itemsList[6].disabled = true;
        s.ctx = new Ext.menu.Menu({
            items: itemsList,
            listeners: {
                hide: function () {
                    s.ctx.destroy();
                }
            }
        });
        s.ctx.rec = rec;
        s.ctx.showAt(e.getXY());
    },
    dragCreateEnd: function (s, rec, e, r) {
        var me = this;
        var win = Ext.widget('groupRoomUseAddWindow'),
            gruForm = win.down('groupRoomUseForm'),
            gruData = rec.data;


        if (gruData.startDate.getDate() == gruData.endDate.getDate() && gruData.startDate.getMonth() == gruData.endDate.getMonth()) {
//            Ext.Msg.alert({title: l('error'), msg: 'Бронь не может быть меньше 1 ночи'});
//            me.eventStore.reload();
//            return;
        }
        // UTC shift start
        rec.data.startDate = new Date(Pms.toUTC(rec.data.startDate) * 1000);
        rec.data.endDate = new Date(Pms.toUTC(rec.data.endDate) * 1000);
        // UTC shift end
        gruData.room = gruData['room.id'];
        gruData.status = 'BOOKING_FREE';
        gruForm.down('combobox[name=baseRoom]').getStore().reload();
        gruForm.getForm().setValues(gruData);
        win.show();

        //for fixing bug when new room added in PMS, but room store isn't reloaded
        Ext.ComponentQuery.query('combobox[name=room]')[0].getStore().reload()
    },

    panelDblClick: function (scheduler, clickedDate, rowIndex, rec, e) {
        var win = Ext.widget('groupRoomUseAddWindow'),
            gruForm = win.down('groupRoomUseForm'),
            gruData = rec.data,
            endDay = new Date(clickedDate.valueOf());
        endDay.setDate(clickedDate.getDate() + 1);
        gruData.startDate = clickedDate;
        gruData.endDate = endDay;
        gruData.room = gruData['id'];
        gruData.status = 'BOOKING_FREE';
        gruForm.getForm().setValues(gruData);
        win.show();
    },

    reloadEventStore: function (par) {
        if (par.eventStore) {
            var schedulerGrid = par.down('schedulergrid');
            par.eventStore.load({
                url: 'rest/baseRoomUse',
                scope: this,
                params: {
                    startDate: schedulerGrid ? schedulerGrid.getStart().valueOf() : null,
                    endDate: schedulerGrid ? schedulerGrid.getEnd().valueOf() : null
                }
            });
        }
    }
});