Ext.define("Pms.modules.restriction.controller.Restriction", {
    extend: "Pms.abstract.Controller",
    views: [
        "Pms.modules.restriction.view.RestrictionForm",
        "Pms.modules.restriction.view.RestrictionGrid",
        "Pms.modules.restriction.view.RestrictionAddWindow",
        "Pms.modules.restriction.view.RestrictionEditWindow"
    ],
    stores: [
        "Pms.modules.restriction.store.Restriction"
    ],
    models: [
        "Pms.modules.restriction.model.Restriction"
    ],
    refs: [
        {
            ref: 'restrictionGrid',
            selector: 'restrictionGrid'
        },
        {
            ref: 'restrictionStore',
            selector: 'restrictionStore'
        },
        {
            ref: 'roomTypeEditWindow',
            selector: 'roomTypeEditWindow'
        }
    ],
    subControllers: [
        'Pms.modules.role.controller.Role'
    ],
    init: function () {

        this.control({
            'restrictionGrid button[action=new]': {
                click: this.newRestriction
            },
            'restrictionGrid button[action=edit]': {
                click: this.editRestriction
            },
            'restrictionAddWindow button[action=add-restriction]': {
                click: this.addRestriction
            },
            'restrictionEditWindow button[action=save-restriction]': {
                click: this.updateRestriction
            },
            'restrictionAddWindow': {
                close: this.refreshOnClose
            },
            'restrictionEditWindow': {
                close: this.refreshOnClose
            },
            'restrictionForm button[action=save-self]': {
                click: this.saveSelf
            }
        });
    },

    newRestriction: function (button) {
        var win = Ext.widget('restrictionAddWindow');
        win.show();
    },

    addRestriction: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            rec = Ext.create('Pms.modules.restriction.model.Restriction', values),
            grid = this.getRestrictionGrid(),
            store = grid.getStore();
        if (form.isValid()) {
            Pms.Ajax.request({
                url: 'rest/compactRestriction',
                method: 'POST',
                jsonData: rec.data,
                success: function () {
                    store.reload();
                    win.close();
                    Pms.App.showNotification({
                        message: l('saveSuccess.msg'),
                        icon: Pms.notificationOk
                    });
                },
                failure: function () {
                    store.reload();
                    Pms.App.showNotification({
                        message: l('saveError.msg')
                    });
                }
            });
        } else {
            Ext.Msg.alert(l('warning'), Pms.iconError + ' ' + l('restriction.addError'));
        }
    },

    editRestriction: function (button, e) {
        var grid = button.up('restrictionGrid'),
            selected = grid.getSelectionModel().getSelection();

        if (selected.length) {
            var rec = selected[0];
            var win = Ext.widget('restrictionEditWindow', {
                    data: rec.data
                }),
                form = win.down('form');
            form.loadRecord(rec);
        }
        else {
            Ext.Msg.alert(l('warning'), Pms.iconError + ' ' + l('restriction.warning'));
        }
    },

    updateRestriction: function (button) {
        var win = button.up('restrictionEditWindow'),
            form = win.down('form'),
            record = form.getRecord(),
            values = form.getValues(),
            grid = this.getRestrictionGrid(),
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
            Ext.Msg.alert(l('error'), Pms.iconError + ' ' + l('restriction.updateError'));
        }
    },


    refreshOnClose: function () {
        var roomTypeEditWindow = this.getRoomTypeEditWindow();
        if (!Ext.isEmpty(roomTypeEditWindow)) {
            var restrictionGrid = roomTypeEditWindow.down('restrictionGrid');
            if (!Ext.isEmpty(restrictionGrid)) {
                restrictionGrid.getStore().reload();
                restrictionGrid.getSelectionModel().clearSelections();
            }
        }
    },
    saveSelf: function (btn) {
        var form = btn.up('form').getForm(),
            values = form.getValues();
        values.role = {id: values.role};
        Pms.Ajax.request({
            url: 'rest/restriction/' + _('restriction'),
            method: 'PUT',
            jsonData: values,
            async: false,
            success: function (res) {
                Pms.App.showNotification({
                    message: l('saveSuccess.msg'),
                    icon: Pms.notificationOk
                });
                location.reload();
            },
            failure: function () {
                Pms.App.showNotification({
                    message: l('saveError.msg')
                });
            }
        });
    }
});