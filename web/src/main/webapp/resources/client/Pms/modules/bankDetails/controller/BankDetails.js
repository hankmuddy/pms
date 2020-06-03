Ext.define("Pms.modules.bankDetails.controller.BankDetails", {
    extend: "Pms.abstract.Controller",
    views: [
        "Pms.modules.bankDetails.view.BankDetailsEditWindow",
        "Pms.modules.bankDetails.view.BankDetailsAddWindow",
        'Pms.modules.bankDetails.view.BankDetailsForm'
    ],
    stores: [
        "Pms.modules.bankDetails.store.BankDetails"
    ],
    models: [
        "Pms.modules.bankDetails.model.BankDetails"
    ],
    refs: [
        {ref: 'bankDetailsGrid', selector: 'bankDetailsGrid'},
        {ref: 'bankDetailsStore', selector: 'bankDetailsStore'}
    ],

    init: function (contr, subController) {
        if (!subController) {
            var view = Ext.create("Pms.modules.bankDetails.view.Viewport");
            this.buildItems(view);
        }


        this.control({
            "bankDetailsGrid button[action=new]": {
                click: this.newBankDetails
            },
            "bankDetailsGrid button[action=delete]": {
                click: this.deleteBankDetails
            },
            'bankDetailsGrid button[action=edit]': {
                click: this.editBankDetails
            },
            'bankDetailsGrid button[action=block]': {
                click: this.blockBankDetails
            },
            'bankDetailsGrid button[action=default]': {
                click: this.setDefault
            },
            'bankDetailsGrid': {
                itemdblclick: this.editBankDetails
            },
            'bankDetailsEditWindow button[action=save-bankDetails]': {
                click: this.updateBankDetails
            },
            'bankDetailsAddWindow': {
                close: this.onCloseEditWindow
            },
            'bankDetailsEditWindow': {
                close: this.onCloseEditWindow
            },
            'bankDetailsAddWindow button[action=add-bankDetails]': {
                click: this.addBankDetails
            }
        });
    },

    newBankDetails: function () {
        var win = Ext.widget('bankDetailsAddWindow');
        win.show();
    },

    addBankDetails: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            rec = Ext.create('Pms.modules.bankDetails.model.BankDetails', values),
            grid = this.getBankDetailsGrid(),
            store = grid.getStore();

        if (form.isValid()) {
            store.add(rec);
            store.sync({
                success: function () {
                    win.close();
                },
//                failure: function () {
//                    Pms.App.showNotification({
//                        message: l('saveError.msg')
//                    });
//                }
            });
        } else {
            Ext.Msg.alert(l('error'), Pms.iconError + ' ' + l('error.incorrect_form_data'));
        }
    },

    editBankDetails: function (button, e) {
        var win = button.up('win'),
            record,
            grid = button.up('bankDetailsGrid'),
            store = grid.getStore(),
            selected = grid.getSelectionModel().getSelection();

        if (selected.length) {
            record = selected[0];
        }
        if (record.get('approved')) {
            Ext.Msg.alert(l('error'), Pms.iconError + ' ' + l('cantEditRecord'));
            return;
        }
        win = Ext.widget('bankDetailsEditWindow');
        win.down('form').loadRecord(record);
    },

    updateBankDetails: function (button) {
        var win = button.up('bankDetailsEditWindow'),
            form = win.down('form').getForm(),
            record = form.getRecord(),
            values = form.getValues(),
            grid = this.getBankDetailsGrid(),
            store = grid.getStore();

        if (form.isValid()) {
            record.set(values);
            store.add(record);
            store.sync({
                success: function () {
                    win.close();
                },
//                failure: function () {
//                    Pms.App.showNotification({
//                        message: l('saveError.msg')
//                    });
//                }
            });
        } else {
            Ext.Msg.alert(l('error'), Pms.iconError + ' ' + l('error.incorrect_form_data'));
        }
    },


    deleteBankDetails: function (button, e) {
        var grid = button.up('bankDetailsGrid'),
            store = grid.getStore(),
            selected = grid.getSelectionModel().getSelection(),
            record;

        if (selected.length > 0) {
            record = selected[0];
        }
        else {
            Ext.Msg.alert(l('warning'), l('warning.recordNotSet'));
            return
        }
        Ext.Msg.confirm(l('bankDetails.delete'), l('confirmation'), function (btn) {
            if (btn === 'yes') {
                store.remove(record);
                store.sync({
                    success: function () {
                        store.reload();
                    },
                    failure: function () {
                        store.reload();
                    }
                });
            }
        });
    },
    setDefault: function (button) {
        var grid = button.up('bankDetailsGrid'),
            store = grid.getStore(),
            rec,
            me = this,
            selected = grid.getSelectionModel().getSelection();

        if (selected.length) {
            rec = selected[0];
            Pms.Ajax.request({
                url: 'rest/bankDetails/' + rec.getId() + '/default',
                method: 'PUT',
                success: function () {
                    store.reload();
                    grid.getSelectionModel().clearSelections();
                    Pms.App.showNotification({
                        message: l('accountAssignedByDefault'),
                        icon: Pms.notificationOk
                    });
                },
                failure: function () {
                    store.reload();
                    Pms.App.showNotification({
                        message: l('saveError.msg')
                    });
                }
            })
        }
        else {
            Ext.Msg.alert(l('error'), Pms.iconError + ' ' + l('warning.selectRecord'));
        }
    },

    blockBankDetails: function (button) {
        var me = this,
            grid = button.up('bankDetailsGrid'),
            store = grid.getStore(),
            rec, blockedStr,
            selected = grid.getSelectionModel().getSelection();

        if (selected.length) {
            rec = selected[0];
            if (rec.data.blocked)
                blockedStr = l('bankDetails.blockMessage');
            else
                blockedStr = l('bankDetails.unBlockMessage');

            Pms.Ajax.request({
                url: 'rest/bankDetails/' + rec.getId() + '/blocked',
                method: 'PUT',
                jsonData: {
                    blocked: !rec.data.blocked
                },
                success: function () {
                    store.reload();
                    grid.getSelectionModel().clearSelections();
                    Pms.App.showNotification({
                        message: blockedStr,
                        icon: Pms.notificationOk
                    });
                }
            })
        }
        else Ext.Msg.alert(l('error'), Pms.iconError + ' ' + l('warning.selectRecord'));
    },

    onCloseEditWindow: function () {
        var grid = Ext.ComponentQuery.query('bankDetailsGrid[rendered=true]')[0];
        if (!Ext.isEmpty(grid)) {
            grid.getStore().reload();
            grid.getSelectionModel().clearSelections();
        }
    },

//    buildItems: function () {
//        if (typeof(this.win) !== "undefined") {
//            var view = Ext.create("Pms.modules.bankDetails.view.Viewport");
//            this.win.add(view);
//        }
//    }
});