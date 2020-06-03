Ext.define('Pms.modules.virtualPlan.controller.VirtualPlan', {
    extend: 'Pms.abstract.Controller',
    views: [
        "Pms.modules.virtualPlan.view.VirtualPlanGrid",
        "Pms.modules.virtualPlan.view.VirtualPlanForm",
        "Pms.modules.virtualPlan.view.VirtualPlanEditWindow",
        "Pms.modules.virtualPlan.view.VirtualPlanAddWindow",
    ],
    stores: [
        "Pms.modules.virtualPlan.store.VirtualPlan"
    ],
    models: [
        "Pms.modules.virtualPlan.model.VirtualPlan"
    ],

    refs: [
        {ref: 'virtualPlanGrid', selector: 'virtualPlanGrid'}
    ],

    init: function () {
//        this.buildItems();
        this.control({
            'virtualPlanGrid button[action=new]': {
                click: this.newVirtualPlan
            },
            'virtualPlanGrid button[action=edit]': {
                click: this.editVirtualPlan
            },
            'virtualPlanGrid button[action=commit]': {
                click: this.commitVirtualPlan
            },
            'virtualPlanGrid button[action=delete]': {
                click: this.deleteVirtualPlan
            },
            'virtualPlanAddWindow button[action=add-virtualPlan]': {
                click: this.addVirtualPlan
            },
            'virtualPlanEditWindow button[action=save-virtualPlan]': {
                click: this.updateVirtualPlan
            },
            'virtualPlanAddWindow': {
                close: this.refreshOnClose
            },
            'virtualPlanEditWindow': {
                close: this.refreshOnClose
            }
        });
    },

    newVirtualPlan: function (button) {
        var win = Ext.widget('virtualPlanAddWindow');
//        if (!Ext.isEmpty(button.up('window').data) && !Ext.isEmpty(button.up('window').data.id)) {
//            var roomTypeId = button.up('window').data.id;
//            win.down('combobox[name=roomType]').setValue(roomTypeId);
//            win.down('combobox[name=roomType]').readOnly = true;
//        }
        win.show();
    },

    addVirtualPlan: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            rec = Ext.create('Pms.modules.virtualPlan.model.VirtualPlan', values),
            grid = this.getVirtualPlanGrid(),
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
            Ext.Msg.alert(l('warning'), Pms.iconError + ' ' + l('virtualPlan.addError'));
        }
    },

    editVirtualPlan: function (button, e) {
        var grid = button.up('virtualPlanGrid'),
            selected = grid.getSelectionModel().getSelection();

        if (selected.length) {
            var rec = selected[0],
                planId = rec.data.plan.id;

            if (rec.get('approved')) {
                Ext.Msg.alert(l('error'), Pms.iconError + ' ' + l('virtualPlan.editError'));
                return
            }
            var win = Ext.widget('virtualPlanEditWindow', {
                    data: rec.data
                }),
                form = win.down('form');

            form.loadRecord(rec);
            form.down('combobox[name=plan]').setValue(planId);
            form.down('combobox[name=plan]').readOnly = true;
        }
        else {
            Ext.Msg.alert(l('warning'), Pms.iconError + ' ' + l('virtualPlan.warning'));
        }
    },

    updateVirtualPlan: function (button) {
        var win = button.up('virtualPlanEditWindow'),
            form = win.down('form'),
            record = form.getRecord(),
            values = form.getValues(),
            grid = this.getVirtualPlanGrid(),
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
            Ext.Msg.alert(l('error'), Pms.iconError + ' ' + l('virtualPlan.updateError'));
        }
    },

    commitVirtualPlan: function (button) {
        var grid = button.up('virtualPlanGrid'),
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
                        url: 'rest/virtualPlan/' + idArray + '/approved',
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
                message: l('virtualPlan.warning')
            });
        }
    },

    deleteVirtualPlan: function (button) {
        var grid = button.up('virtualPlanGrid'),
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
                store.sync(
//                    {
//                    success: function () {
//                        Pms.App.showNotification({
//                            message: l('deleteSuccess.msg'),
//                            icon: Pms.notificationOk
//                        });
//                    },
//                    failure: function () {
//                        Pms.App.showNotification({
//                            message: l('deleteError')
//                        });
//                    }
//                }
                );
            }
        }, this);
    },

    refreshOnClose: function () {
        var virtualPlanGrid = this.getVirtualPlanGrid();
//        if (!Ext.isEmpty(roomTypeEditWindow)) {
//            var virtualPlanGrid = roomTypeEditWindow.down('virtualPlanGrid');
//            if (!Ext.isEmpty(virtualPlanGrid)) {
        virtualPlanGrid.getStore().reload();
        virtualPlanGrid.getSelectionModel().clearSelections();
//            }
//        }
    }
});