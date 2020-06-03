Ext.define("Pms.modules.room.controller.Room", {
    extend: "Pms.abstract.Controller",
    views: [
        "Pms.modules.room.view.Viewport",
        "Pms.modules.room.view.RoomGrid",
        "Pms.modules.room.view.RoomEditWindow",
        "Pms.modules.room.view.RoomAddWindow",
        "Pms.modules.room.view.RoomForm",
        "Pms.modules.room.view.RoomApprovedForm",
        'Pms.modules.groupRoomUse.view.Viewport'
    ],
    stores: [
        "Pms.modules.room.store.Room"
    ],
    models: [
        "Pms.modules.room.model.Room"
    ],
    refs: [
        {ref: 'roomGrid', selector: 'roomGrid'},
        {ref: 'roomStore', selector: 'roomStore'},
        {ref: 'groupRoomUseViewport', selector: 'groupRoomUseViewport'}
    ],

    init: function (contr, subController) {

        if (!subController) {
            var view = Ext.create("Pms.modules.room.view.Viewport");
            this.buildItems(view);
        }

        this.control({
            "roomGrid button[action=new]": {
                click: this.newRoom
            },
            "roomGrid button[action=delete]": {
                click: this.deleteRoom
            },
            "roomGrid button[action=commit]": {
                click: this.commitRoom
            },
            'roomGrid button[action=edit]': {
                click: this.editRoom
            },
            'roomGrid': {
                itemdblclick: this.editRoom
            },
            'roomEditWindow button[action=save-room]': {
                click: this.updateRoom
            },
            'roomAddWindow': {
                close: this.refreshOnClose
            },
            'roomEditWindow': {
                close: this.refreshOnClose
            },
            'roomAddWindow button[action=add-room]': {
                click: this.addRoom
            }
        });
    },

    newRoom: function () {
        var win = Ext.widget('roomAddWindow');
        win.show();
    },

    addRoom: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            rec = Ext.create('Pms.modules.room.model.Room', values),
            grid = this.getRoomGrid(),
            store = grid.getStore(),
            number = rec.data.number,
            numberArr = number.split(','),
            records = [];

        Ext.each(numberArr, function (num) {
            values.number = num;
            var numRec = Ext.create('Pms.modules.room.model.Room', values);
            records.push(numRec);
        });
        if (form.isValid()) {
            store.add(records);
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
            Ext.Msg.alert(l('warning'), l('error.recordNotSaved'));
        }
    },

    editRoom: function (btn, e) {
        var livingGrid = btn.up('roomGrid'),
            selected = livingGrid.getSelectionModel().getSelection(),
            approved, record;

        if (selected.length) {
            record = selected[0];
            approved = record.get('approved');
        }
        var win = Ext.widget('roomEditWindow', {approved: approved});
        win.down('form').loadRecord(record);
        var roomTypeId = record.get('roomType').id,
            accommodationId = record.get('accommodation').id;

        win.down('combobox[name=accommodation]').setValue(accommodationId);

        if (!approved) {
            win.down('combobox[name=roomType]').setValue(roomTypeId)
        }
        else win.down('displayfield[name=roomType]').setValue(record.data.roomType.name);
    },

    updateRoom: function (button) {
        var win = button.up('roomEditWindow'),
            form = win.down('form').getForm(),
            record = form.getRecord(),
            values = form.getValues(),
            grid = this.getRoomGrid(),
            store = grid.getStore();

        if (form.isValid()) {
            record.set(values);
            store.add(record);
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
            Ext.Msg.alert(l('warning'), l('roomIsNotChanged'));
        }
    },

    commitRoom: function (button) {
        var me = this,
            grid = button.up('roomGrid'),
            selected = grid.getSelectionModel().getSelection(),
            idArray = '';

        if (selected.length > 0) {
            Ext.Msg.confirm(l('confirm'), l('commitRoom'), function (btn) {
                if (btn === 'yes') {
                    Ext.each(selected, function (data, index) {
                        idArray += data.internalId;
                        if (index != selected.length - 1)
                            idArray += ',';
                    }, this);
                    Pms.Ajax.request({
                        url: 'rest/room/' + idArray + '/approved',
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        success: function (response) {
                            me.refreshOnClose(true);
                            Pms.App.showNotification({
                                message: l('commitSuccess'),
                                icon: Pms.notificationOk
                            });
                        },
                        failure: function () {
                            Pms.App.showNotification({
                                message: l('commitError')
                            });
                        }
                    });
                }

            }, this);
        } else {
            Ext.Msg.alert(l('warning'), l('warning.selectRecord'));
        }
    },

    deleteRoom: function (button, e) {
        var me = this,
            grid = button.up('roomGrid'),
            store = grid.getStore(),
            selected = grid.getSelectionModel().getSelection();
        var record;
        if (selected.length > 0) {
            record = selected[0];
        }
        else {
            Ext.Msg.alert(l('warning'), l('warning.recordNotSet'));
            return
        }
        if (!record.get('approved')) {
            Ext.Msg.confirm(l('deletingRoom'), l('common.confirm.delete.message'), function (btn) {
                if (btn === 'yes') {
                    store.remove(record);
                    store.sync({
                        success: function () {
                            me.refreshOnClose();
                        },
                        failure: function () {
                            store.reload();
                        }
                    });
                }
            });
        }
        else {
            Ext.Msg.alert(l('warning'), l('cantDeleteCommited'));
        }
    },

    refreshOnClose: function (refreshSchPanel) {
        var grid = this.getRoomGrid();
        if (typeof(grid) != 'undefined') {
            grid.getStore().reload();
            grid.getSelectionModel().clearSelections();
        }
        if (refreshSchPanel) {
            var groupRoomUseViewport = this.getGroupRoomUseViewport();
            if (!Ext.isEmpty(groupRoomUseViewport)) {
                var schPanel = groupRoomUseViewport.down('schPanel');
                if (!Ext.isEmpty(schPanel)) {
                    var resourceStore = schPanel.down('grid').getResourceStore();
                    if (!Ext.isEmpty(resourceStore)) {
                        resourceStore.reload();
                    }
                }
            }
        }
    }
});