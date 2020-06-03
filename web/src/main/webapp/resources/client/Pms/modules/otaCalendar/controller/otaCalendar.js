Ext.define("Pms.modules.otaCalendar.controller.otaCalendar", {
    extend: 'Pms.abstract.Controller',
    views: [
        'Pms.modules.otaCalendar.view.Viewport',
        'Pms.modules.otaCalendar.view.otaCalendarSch',
        'Pms.modules.otaCalendar.view.otaCalendarAddWindow',
        'Pms.modules.otaCalendar.view.otaCalendarAddPeriodWindow',
        'Pms.modules.otaCalendar.view.otaEditWindow'
    ],

    stores: [
        'Pms.modules.otaCalendar.store.Event',
        'Pms.modules.otaCalendar.store.Resource',
        'Pms.modules.otaCalendar.store.ota',
        'Pms.modules.catalog.store.Catalog'
    ],

    models: [
        'Pms.modules.otaCalendar.model.Resource',
        'Pms.modules.otaCalendar.model.Event',
        'Pms.modules.otaCalendar.model.ota',
        'Pms.modules.otaCalendar.model.PricePerDay'
    ],

    refs: [
        {
            ref: 'schPanel',
            selector: 'otaCalendarSch'
        },
        {
            ref: 'otaCalendarAddWindow',
            selector: 'otaCalendarAddWindow'
        },
        {
            ref: 'resourceStore',
            selector: 'resourceStore'
        },
        {
            ref: 'eventStore',
            selector: 'eventStore'
        }
    ],

    init: function (contr, subController) {
        if (!subController) {
            var view = Ext.create('Pms.modules.otaCalendar.view.Viewport');
            this.buildItems(view);
        }

        this.control({
            'otaCalendarViewport button[action=new]': {
                click: this.addOta
            },
            'otaCalendarAddWindow button[action=save]': {
                click: this.saveOta
            },
            'otaCalendarAddPeriodWindow button[action=save]': {
                click: this.savePeriod
            },
            'otaCalendarSch grid': {
                eventdblclick: this.editOta
            },
            'otaEditWindow button[action=save]': {
                click: this.updateOta
            }
        });
    },

    addOta: function () {
        var win = Ext.widget('otaCalendarAddWindow');
        win.show();
    },

    editOta: function (scheduler, record, e, eOpts) {
        var win = Ext.widget('otaEditWindow', {
                baseRoomId: record.data.room.id,
                baseRoomType: record.data.room.type
            }),
            recordData = record.data,
            form = win.down('form');
        form.loadRecord(record);
        form.getForm().findField('room').setValue(recordData.room.name);
        form.getForm().findField('otaAllowed').setValue(!recordData.otaAllowed);
        win.roomId = recordData.room.id;
        win.show();
    },

    updateOta: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            rec = Ext.create('Pms.modules.otaCalendar.model.ota', values),
            schPanel = this.getSchPanel(),
            schGrid = schPanel.down('grid'),
            eventStore = schGrid.getEventStore(),
            store = Ext.create('Pms.modules.otaCalendar.store.ota');
        if (form.isValid()) {
            rec.set(values);
            /*toUTC*/
            rec.data.date = Pms.toUTC(rec.data.date);
            rec.data.room = {id: win.baseRoomId, type: win.baseRoomType};
            rec.data.otaAllowed = !rec.data.otaAllowed;
            delete rec.data.id;
            Pms.Ajax.request({
                url: 'rest/baseRoomValue/' + values.id,
                method: 'PUT',
                jsonData: rec.data,
                success: function () {
                    eventStore.reload();
                    win.close();
                    Pms.App.showNotification({
                        message: l('saveSuccess.msg'),
                        icon: Pms.notificationOk
                    });
                },
                failure: function () {
                    eventStore.reload();
                    Pms.App.showNotification({
                        message: l('saveError.msg')
                    });
                }
            });

        } else {
            Ext.Msg.alert(l('error'), l('error.recordNotSaved'));
        }
    },

    saveOta: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            rec = Ext.create('Pms.modules.otaCalendar.model.ota', values),
            schPanel = this.getSchPanel(),
            schGrid = schPanel.down('grid'),
            eventStore = schGrid.getEventStore(),
            store = Ext.create('Pms.modules.otaCalendar.store.ota');
        if (form.isValid()) {
            rec.data.room = {
                id: values.room,
                type: form.down('combobox[name=room]').valueModels[0].data.type
            };
            store.add(rec);

            store.sync({
                success: function () {
                    store.reload();
                    eventStore.reload();
                    win.close();
                },
                failure: function () {
                    eventStore.reload();
                }
            });
        } else {
            Ext.Msg.alert(l('error'), l('error.recordNotSaved'));
        }
    },

    savePeriod: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            rec = Ext.create('Pms.modules.otaCalendar.model.ota', values),
            schPanel = this.getSchPanel(),
            schGrid = schPanel.down('grid'),
            eventStore = schGrid.getEventStore(),
            roomCombobox = form.down('combobox[name=room]'),
            rooms = [],
            store = Ext.create('Pms.modules.otaCalendar.store.ota');
        if (form.isValid()) {
            rec.data.dateStart = Pms.toUTC(parseInt(values.dateStart));
            rec.data.dateEnd = Pms.toUTC(parseInt(values.dateEnd));
            Ext.each(roomCombobox.valueModels, function (record) {
                rooms.push({id: record.data.id, type: record.data.type})
            });
            rec.data.rooms = rooms;
            rec.data.otaAllowed = !rec.data.otaAllowed;
            Pms.Ajax.request({
                url: 'rest/baseRoomValue/period',
                method: 'POST',
                jsonData: rec.data,
                success: function () {
                    eventStore.reload();
                    win.close();
                    Pms.App.showNotification({
                        message: l('saveSuccess.msg'),
                        icon: Pms.notificationOk
                    });
                },
                failure: function () {
                    eventStore.reload();
                    Pms.App.showNotification({
                        message: l('saveError.msg')
                    });
                }
            });
        } else {
            Ext.Msg.alert(l('error'), l('error.recordNotSaved'));
        }
    }
});