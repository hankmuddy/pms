Ext.define("Pms.modules.virtualRoom.controller.VirtualRoom", {
    extend: "Pms.modules.roomType.controller.RoomType",
    views: [
        "Pms.modules.virtualRoom.view.VirtualRoomForm",
        "Pms.modules.virtualRoom.view.VirtualRoomGrid",
        "Pms.modules.virtualRoom.view.VirtualRoomAddWindow",
        "Pms.modules.virtualRoom.view.VirtualRoomEditWindow"
    ],
    stores: [
        "Pms.modules.virtualRoom.store.VirtualRoom"
    ],
    models: [
        "Pms.modules.virtualRoom.model.VirtualRoom"
    ],
    refs: [
        {
            ref: 'virtualRoomGrid',
            selector: 'virtualRoomGrid'
        },
        {
            ref: 'virtualRoomStore',
            selector: 'virtualRoomStore'
        },
        {
            ref: 'roomTypeEditWindow',
            selector: 'roomTypeEditWindow'
        }
    ],

    extravailable: true,

    init: function () {
//        this.buildItems();
        this.control({
            'virtualRoomGrid button[action=new]': {
                click: this.newVirtualRoom
            },
            'virtualRoomGrid button[action=edit]': {
                click: this.editVirtualRoom
            },
            'virtualRoomGrid button[action=commit]': {
                click: this.commitVirtualRoom
            },
            'virtualRoomGrid button[action=delete]': {
                click: this.deleteVirtualRoom
            },
            'virtualRoomGrid button[action=default]': {
                click: this.setDefaultVirtualRoom
            },
            'virtualRoomAddWindow button[action=add-virtualRoom]': {
                click: this.addVirtualRoom
            },
            'virtualRoomEditWindow button[action=save-virtualRoom]': {
                click: this.updateVirtualRoom
            },
            'virtualRoomAddWindow': {
                close: this.refreshOnClose
            },
            'virtualRoomEditWindow': {
                close: this.refreshOnClose
            }
        });
    },

    newVirtualRoom: function (button) {
        var win = Ext.widget('virtualRoomAddWindow');
        if (!Ext.isEmpty(button.up('window').data) && !Ext.isEmpty(button.up('window').data.id)) {
            var roomTypeId = button.up('window').data.id;
            win.down('combobox[name=roomType]').setValue(roomTypeId);
            win.down('combobox[name=roomType]').readOnly = true;
        }
        win.show();
    },

    addVirtualRoom: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            rec = Ext.create('Pms.modules.virtualRoom.model.VirtualRoom', values),
            grid = this.getVirtualRoomGrid(),
            store = grid.getStore();

        if (form.isValid()) {
            rec.set(values);
            store.add(rec);
            store.sync({
                success: function () {
                    store.reload();
                    win.close();
                },
                failure: function () {
                    store.reload();
                }
            });
        } else {
            Ext.Msg.alert(l('warning'), Pms.iconError + ' ' + l('virtualRoom.addError'));
        }
    },

    editVirtualRoom: function (button, e) {
        var grid = button.up('virtualRoomGrid'),
            selected = grid.getSelectionModel().getSelection();

        if (selected.length) {
            var rec = selected[0],
                roomTypeId = rec.data.roomType.id;

            if (rec.get('approved')) {
                Ext.Msg.alert(l('error'), Pms.iconError + ' ' + l('virtualRoom.editError'));
                return
            }
            var win = Ext.widget('virtualRoomEditWindow', {
                    data: rec.data
                }),
                form = win.down('form');

            form.loadRecord(rec);
            form.down('combobox[name=roomType]').setValue(roomTypeId);
            form.down('combobox[name=roomType]').readOnly = true;
        }
        else {
            Ext.Msg.alert(l('warning'), Pms.iconError + ' ' + l('virtualRoom.warning'));
        }
    },

    updateVirtualRoom: function (button) {
        var win = button.up('virtualRoomEditWindow'),
            form = win.down('form'),
            record = form.getRecord(),
            values = form.getValues(),
            grid = this.getVirtualRoomGrid(),
            store = grid.getStore();

        if (form.isValid()) {
            record.set(values);
            store.sync({
                success: function () {
                    store.reload();
                    win.close();
                },
                failure: function () {
                    store.reload();
                }
            });
        } else {
            Ext.Msg.alert(l('error'), Pms.iconError + ' ' + l('virtualRoom.updateError'));
        }
    },

    commitVirtualRoom: function (button) {
        var grid = button.up('virtualRoomGrid'),
            selected = grid.getSelectionModel().getSelection(),
            store = grid.getStore(),
            idArray = '',
            me = this;

        if (selected.length > 0) {
            Ext.Msg.confirm(l('confirm'), l('virtualRoom.deleteThis'), function (btn) {
                if (btn === "yes") {
                    Ext.each(selected, function (data, index) {
                        idArray += data.internalId;
                        if (index != selected.length - 1)
                            idArray += ',';
                    }, this);
                    Pms.Ajax.request({
                        url: 'rest/virtualRoom/' + idArray + '/approved',
                        method: 'PUT',
                        headers: { 'Content-Type': 'application/json' },
                        success: function (data) {
                            store.reload();
                            me.updateOtaCalendar();
                            grid.getSelectionModel().clearSelections();
                            Pms.App.showNotification({
                                message: l('commitSuccess'),
                                icon: Pms.notificationOk
                            });
                        }
                    });
                }
            }, this);
        } else {
            Pms.App.showNotification({
                message: l('virtualRoom.warning')
            });
        }
    },

    deleteVirtualRoom: function (button) {
        var grid = button.up('virtualRoomGrid'),
            store = grid.getStore(),
            selected = grid.getSelectionModel().getSelection();

        if (!selected.length) {
            Ext.Msg.alert(l('warning'), l('warning.recordNotSet'));
            return false;
        }

        var record = selected[0];

        if (record.get('approved')) {
            Pms.App.showNotification({
                message: l('warning.cantDeleteCommited')
            });
            return false;
        }

        Ext.Msg.confirm(l('confirm'), l('common.confirm.delete.message'), function (btn) {
            if (btn === 'yes') {
                store.remove(selected[0]);
                store.sync();
            }
        }, this);
    },

    updateOtaCalendar: function () {
        var otaPanel = Ext.ComponentQuery.query('otaCalendarSch')[0],
            otaCalendar,
            resourceStore;
        if (otaPanel) {
            otaCalendar = otaPanel.down('grid');
            resourceStore = otaCalendar.getResourceStore();
            resourceStore.reload();
        }
    },

    setDefaultVirtualRoom: function (button) {
        var grid = button.up('virtualRoomGrid'),
            selected = grid.getSelectionModel().getSelection(),
            store = grid.getStore(),
            me = this;

        if (selected.length > 0) {
            var id = selected[0].getId();
            Pms.Ajax.request({
                url: 'rest/baseRoom/' + id + '/default',
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                success: function (data) {
                    store.reload();
                    button.up('window').down('roomTypeGrid').getStore().reload();
                    grid.getSelectionModel().clearSelections();
                    Pms.App.showNotification({
                        message: l('success'),
                        icon: Pms.notificationOk
                    });
                }
            });
        }
        else {
            Pms.App.showNotification({
                message: l('virtualRoom.warning')
            });
        }
    },

    refreshOnClose: function () {
        var roomTypeEditWindow = this.getRoomTypeEditWindow();
        if (!Ext.isEmpty(roomTypeEditWindow)) {
            var virtualRoomGrid = roomTypeEditWindow.down('virtualRoomGrid');
            if (!Ext.isEmpty(virtualRoomGrid)) {
                virtualRoomGrid.getStore().reload();
                virtualRoomGrid.getSelectionModel().clearSelections();
            }
        }
    }
});