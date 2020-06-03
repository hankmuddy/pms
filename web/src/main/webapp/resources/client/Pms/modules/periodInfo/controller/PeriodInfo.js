Ext.define("Pms.modules.periodInfo.controller.PeriodInfo", {
    extend: "Pms.abstract.Controller",
    views: [
        "Pms.modules.periodInfo.view.PeriodInfoForm",
        "Pms.modules.periodInfo.view.PeriodInfoGrid",
        "Pms.modules.periodInfo.view.PeriodInfoAddWindow",
        "Pms.modules.periodInfo.view.PeriodInfoEditWindow"
    ],
    stores: [
        "Pms.modules.periodInfo.store.PeriodInfo"
    ],
    models: [
        "Pms.modules.periodInfo.model.PeriodInfo"
    ],
    refs: [
        {
            ref: 'periodInfoGrid',
            selector: 'periodInfoGrid'
        },
        {
            ref: 'periodInfoStore',
            selector: 'periodInfoStore'
        },
        {
            ref: 'roomTypeEditWindow',
            selector: 'roomTypeEditWindow'
        }
    ],
    init: function () {
//        this.buildItems();
        this.control({
            'periodInfoGrid button[action=new]': {
                click: this.newPeriodInfo
            },
            'periodInfoGrid button[action=edit]': {
                click: this.editPeriodInfo
            },
            'periodInfoGrid button[action=commit]': {
                click: this.commitPeriodInfo
            },
            'periodInfoGrid button[action=delete]': {
                click: this.deletePeriodInfo
            },
            'periodInfoAddWindow button[action=add-periodInfo]': {
                click: this.addPeriodInfo
            },
            'periodInfoEditWindow button[action=save-periodInfo]': {
                click: this.updatePeriodInfo
            },
            'periodInfoAddWindow': {
                close: this.refreshOnClose
            },
            'periodInfoEditWindow': {
                close: this.refreshOnClose
            }
        });
    },
    newPeriodInfo: function (button) {
        var win = Ext.widget('periodInfoAddWindow');
        if (!Ext.isEmpty(button.up('window').data) && !Ext.isEmpty(button.up('window').data.id)) {
            var roomTypeId = button.up('window').data.id;
            win.down('combobox[name=roomType]').setValue(roomTypeId);
        }
        win.show();
    },

    addPeriodInfo: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            rec = Ext.create('Pms.modules.periodInfo.model.PeriodInfo', values),
            grid = this.getPeriodInfoGrid(),
            store = grid.getStore();

        if (form.isValid()) {
            rec.set(values);
            store.add(rec);
            store.sync({
                success: function () {
                    win.close();
                },
                failure: function () {
                    store.reload()
                }
            });
        } else {
//            Ext.Msg.alert(l('error'), l('PeriodInfo.addError'));
            Pms.App.showNotification({
                message: l('periodInfo.addError')
            });
        }
    },

    editPeriodInfo: function (button, e) {
        var grid = button.up('periodInfoGrid'),
            selected = grid.getSelectionModel().getSelection();

        if (selected.length) {
            var rec = selected[0],
                roomTypeId = rec.data.roomType.id;

            console.log(rec);

            if (rec.get('approved')) {
                Ext.Msg.alert(l('error'), Pms.iconError + ' ' + l('periodInfo.editError'));
                return
            }
            var win = Ext.widget('periodInfoEditWindow', {data: rec.data}),
                form = win.down('form');

            form.loadRecord(rec);
            form.down('combobox[name=roomType]').setValue(roomTypeId);
        }
        else {
//            Ext.Msg.alert(l('warning'), l('PeriodInfo.warning'));
            Pms.App.showNotification({
                message: l('periodInfo.warning')
            });
//            Ext.Msg.alert(l('warning'), l('warning.selectRecord'));
        }
    },

    updatePeriodInfo: function (button) {
        var win = button.up('periodInfoEditWindow'),
            form = win.down('form'),
            record = form.getRecord(),
            values = form.getValues(),
            grid = this.getPeriodInfoGrid(),
            store = grid.getStore();

        if (form.isValid()) {
            record.set(values);
            store.sync({
                success: function () {
                    store.reload();
                    win.close();
                },
                failure: function () {
                    store.reload()
                }
            });
        } else {
//            Ext.Msg.alert(l('error'), l('PeriodInfo.updateError'));
            Pms.App.showNotification({
                message: l('periodInfo.updateError')
            });
        }
    },

    commitPeriodInfo: function (button) {
        var grid = button.up('periodInfoGrid'),
            selected = grid.getSelectionModel().getSelection(),
            store = grid.getStore(),
            idArray = '';

        if (selected.length > 0) {
            Ext.Msg.confirm(l('confirm'), l('virtualRoom.deleteThis'), function (btn) {
                if (btn === "yes") {
                    Ext.each(selected, function (data, index) {
                        idArray += data.internalId;
                        if (index != selected.length - 1)
                            idArray += ',';
                    }, this);
                    Pms.Ajax.request({
                        url: 'rest/periodInfo/' + idArray + '/approved',
                        method: 'PUT',
                        headers: { 'Content-Type': 'application/json' },
                        success: function (data) {
                            store.reload();
                            grid.getSelectionModel().clearSelections();
                        }
                    });
                }
            }, this);
        } else {
//            Ext.Msg.alert(l('warning'), l('warning.selectRecord'));
//            Ext.Msg.alert(l('warning'), l('PeriodInfo.warning'));
            Pms.App.showNotification({
                message: l('periodInfo.warning')
            });
        }
    },

    deletePeriodInfo: function (button) {
        var grid = button.up('periodInfoGrid'),
            store = grid.getStore(),
            selected = grid.getSelectionModel().getSelection();

        if (!selected.length) {
            Ext.Msg.alert(l('warning'), l('warning.recordNotSet'));
            return false;
        }

        var record = selected[0];

        if (record.get('approved')) {
//            Ext.Msg.alert(l('warning'), Pms.iconError + l('warning.cantDeleteCommited'));
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

    refreshOnClose: function () {
        var roomTypeEditWindow = this.getRoomTypeEditWindow();
        if (!Ext.isEmpty(roomTypeEditWindow)) {
            var periodInfoGrid = roomTypeEditWindow.down('periodInfoGrid');
            if (!Ext.isEmpty(periodInfoGrid)) {
                periodInfoGrid.getStore().reload();
                periodInfoGrid.getSelectionModel().clearSelections();
            }
        }
    }
});