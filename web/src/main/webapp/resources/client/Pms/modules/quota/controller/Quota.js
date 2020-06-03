Ext.define('Pms.modules.quota.controller.Quota', {
    extend: 'Pms.abstract.Controller',
    views: [
        'Pms.modules.quota.view.Viewport',
        'Pms.modules.quota.view.RTVSch',
        'Pms.modules.quota.view.BRVSch',
        'Pms.modules.quota.view.RTVEditWindow',
        'Pms.modules.quota.view.BRVEditWindow',
        'Pms.modules.quota.view.RTVPeriodWindow',
        'Pms.modules.otaCalendar.view.otaCalendarAddPeriodWindow'
    ],
    stores: ['Pms.modules.quota.store.RTVEvent', 'Pms.modules.quota.store.BRVEvent', 'Pms.modules.quota.store.RTVResource', 'Pms.modules.quota.store.BRVResource'],
    models: ['Pms.modules.quota.model.RTVEvent', 'Pms.modules.quota.model.BRVEvent', 'Pms.modules.quota.model.RTVResource', 'Pms.modules.quota.model.BRVResource'],
    refs: [
        {ref: 'quotaViewport', selector: 'quotaViewport'},
        {ref: 'rtvSch', selector: 'rtvSch'},
        {ref: 'brvSch', selector: 'brvSch'},
        {ref: 'rtvEventStore', selector: 'rtvEventStore'}
    ],
    subControllers: [],

    extravailable: true,

    init: function () {
        var me = this,
            view = Ext.widget('quotaViewport');

        me.buildItems(view);

        me.control({
            'rtvEditWindow button[action=process]': {
                click: me.process
            },
            'rtvSch': {
                openeditwindow: me.openRtvEditWindow,
                deleteevent: me.delete
            },
            'rtvSch button[action=setperiod]': {
                click: me.openRtvPeriodWindow
            },
            'rtvPeriodWindow button[action=save]': {
                click: me.saveRtvPeriod
            },
            'brvEditWindow button[action=process]': {
                click: me.process
            },
            'brvSch': {
                openeditwindow: me.openBrvEditWindow,
                deleteevent: me.delete
            },
            'brvSch button[action=setperiod]': {
                click: me.openBrvPeriodWindow
            },
            'otaCalendarAddPeriodWindow button[action=save]': {
                click: me.saveBrvPeriod
            },
            // refresh actions
            'rtvEditWindow': {
                close: me.refreshOnClose
            },
            'rtvPeriodWindow': {
                close: me.refreshOnClose
            },
            'brvEditWindow': {
                close: me.refreshOnClose
            },
            'otaCalendarAddPeriodWindow': {
                close: me.refreshOnClose
            },
        });
    },

    openRtvEditWindow: function (rows) {
        Ext.widget('rtvEditWindow', {data: rows});
    },

    openBrvEditWindow: function (rows) {
        var win = Ext.widget('brvEditWindow', {data: rows}),
            form = win.down('form');
        form.loadRecord(rows[0]);
        var closedForOta = false;
        if(Ext.isEmpty(rows[0].data.otaAllowed) || rows[0].data.otaAllowed == false  || rows[0].data.otaAllowed == 0) closedForOta = true;
        form.getForm().findField('otaAllowed').setValue(closedForOta);
        win.show();
    },

    openRtvPeriodWindow: function (btn) {
        Ext.widget('rtvPeriodWindow');
    },

    openBrvPeriodWindow: function (btn) {
        Ext.widget('otaCalendarAddPeriodWindow', {title: l('quota.restrictionPeriod')});
    },

    process: function(btn) {
        var me = this,
            win = btn.up('window'),
            formData = win.down('form').getValues(),
            records = win.data,
            urlSuffix = win.getXType() == 'rtvEditWindow' ? 'roomTypeValue' : 'baseRoomValue';

        if(records.length) {
            for (var i in records) {
                var data = {},
                    url = 'rest/' + urlSuffix,
                    method = 'POST';

                if(win.getXType() == 'rtvEditWindow') {
                    data = {
                        roomType: {id: records[i].data.roomTypeId, type: 'roomType'},
                        roomsAvailable: formData.roomsAvailable,
                        date: Pms.toUTC(records[i].data.startDate)
                    };
                } else {
                    data.room = {id: records[i].data['room.id'], roomType: {id: records[i].data.roomTypeId, type: 'roomType'}, type: records[i].data.type};
                    data.date = Pms.toUTC(records[i].data.startDate);
                    data.minStay = Ext.isEmpty(formData.minStay) ? 0 : formData.minStay;
                    data.minStayArrival = Ext.isEmpty(formData.minStayArrival) ? 0 : formData.minStayArrival;
                    data.maxStay = Ext.isEmpty(formData.maxStay) ? 0 : formData.maxStay;
                    if (!Ext.isEmpty(formData.closed)) data.closed = formData.closed;
                    data.closedToDeparture = false;
                    data.otaAllowed = false;
                    if(formData.closedToDeparture == true) data.closedToDeparture = true;
                    if(Ext.isEmpty(formData.otaAllowed) || formData.otaAllowed == false) data.otaAllowed = true;
                }

                if(!records[i].phantom) {
                    data.id = records[i].data.id;
                    url += '/' + data.id;
                    method = 'PUT';
                }

                Pms.Ajax.request({
                    url: url,
                    method: method,
                    jsonData: data,
                    success: function(response) {
                        win.close();
                        Pms.App.showNotification({
                            message: l('saveSuccess.msg'),
                            icon: Pms.notificationOk
                        });
                    },
                    failure: function() {
                        win.close();
                        Pms.App.showNotification({
                            message: l('seasonSaveError')
                        });
                    }
                });
            }
        }
    },

    saveRtvPeriod: function (btn) {
        var win = btn.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            data = {},
            rooms = [],
            roomCombobox = form.down('combobox[name=room]');

        if (form.isValid()) {
            data.dateStart = Pms.toUTC(parseInt(values.dateStart));
            data.dateEnd = Pms.toUTC(parseInt(values.dateEnd));
            Ext.each(roomCombobox.valueModels, function (record) {
                rooms.push({id: record.data.id, type: record.data.type})
            });
            data.roomTypes = rooms;
            data.roomsAvailable = values.roomsAvailable;
            Pms.Ajax.request({
                url: 'rest/roomTypeValue/period',
                method: 'POST',
                jsonData: data,
                success: function () {
                    win.close();
                    Pms.App.showNotification({
                        message: l('saveSuccess.msg'),
                        icon: Pms.notificationOk
                    });
                },
                failure: function () {
                    win.close();
                    Pms.App.showNotification({
                        message: l('saveError.msg')
                    });
                }
            });
        } else {
            Ext.Msg.alert(l('error'), l('error.recordNotSaved'));
        }
    },

    saveBrvPeriod: function (btn) {
        var win = btn.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            roomCombobox = form.down('combobox[name=room]'),
            data = {rooms: []};

        if (form.isValid()) {
            Ext.each(roomCombobox.valueModels, function (record) {
                data.rooms.push({id: record.data.id, type: record.data.type})
            });
            data.dateStart = Pms.toUTC(parseInt(values.dateStart));
            data.dateEnd = Pms.toUTC(parseInt(values.dateEnd));
            data.minStay = Ext.isEmpty(values.minStay) ? 0 : values.minStay;
            data.minStayArrival = Ext.isEmpty(values.minStayArrival) ? 0 : values.minStayArrival;
            data.maxStay = Ext.isEmpty(values.maxStay) ? 0 : values.maxStay;
            if (!Ext.isEmpty(values.closed)) data.closed = values.closed;
            data.closedToDeparture = false;
            data.otaAllowed = false;
            if(values.closedToDeparture == true) data.closedToDeparture = true;
            if(Ext.isEmpty(values.otaAllowed) || values.otaAllowed == false) data.otaAllowed = true;

            Pms.Ajax.request({
                url: 'rest/baseRoomValue/period',
                method: 'POST',
                jsonData: data,
                success: function () {
                    win.close();
                    Pms.App.showNotification({
                        message: l('saveSuccess.msg'),
                        icon: Pms.notificationOk
                    });
                },
                failure: function () {
                    win.close();
                    Pms.App.showNotification({
                        message: l('saveError.msg')
                    });
                }
            });
        } else {
            Ext.Msg.alert(l('error'), l('error.recordNotSaved'));
        }
    },

    delete: function (sch, rows, e, parentXType) {
        var me = this,
            urlSuffix = parentXType == 'rtvSch' ? 'roomTypeValue' : 'baseRoomValue',
            url = 'rest/' + urlSuffix;

        Ext.Msg.confirm(l('confirm'), l('recordDeleteConfirmation'), function (btn) {
            if (btn === 'yes') {
                if(rows.length) {
                    for(var i in rows) {
                        if(!rows[i].phantom) {
                            Pms.Ajax.request({
                                url: url + '/' + rows[i].data.id,
                                method: 'DELETE',
                                headers: {'Content-Type': 'application/json'},
                                success: function(response) {
                                    me.refreshOnClose();
                                    Pms.App.showNotification({
                                        message: l('recordDeleted'),
                                        icon: Pms.notificationOk
                                    });
                                },
                                failure: function(response) {
                                    me.refreshOnClose();
                                    Pms.App.showNotification({
                                        message: l('recordNotDeleted')
                                    });
                                }
                            });
                        }
                    }
                }
            }
        }, this);
    },

    refreshOnClose: function () {
        var me = this,
            quotaViewport = me.getQuotaViewport();
        if (!Ext.isEmpty(quotaViewport)) {
            var rtvSch = quotaViewport.down('rtvSch');
            if(!Ext.isEmpty(rtvSch)) {
                rtvResourceStore = rtvSch.down('grid').getResourceStore();
                if(!Ext.isEmpty(rtvResourceStore)) rtvResourceStore.reload();
                rtvEventStore = rtvSch.down('grid').getEventStore();
                if(!Ext.isEmpty(rtvEventStore)) {
                    rtvEventStore.reload();
                }
                rtvSch.down('grid').getSelectionModel().clearSelections();
            }
            var brvSch = quotaViewport.down('brvSch');
            if(!Ext.isEmpty(brvSch)) {
                brvResourceStore = brvSch.down('schedulertree').getResourceStore();
                if(!Ext.isEmpty(brvResourceStore)) brvResourceStore.reload();
                brvEventStore = brvSch.down('schedulertree').getEventStore();
                if(!Ext.isEmpty(brvEventStore)) brvEventStore.reload();
            }
        }
    }
});
