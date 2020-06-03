Ext.define("Pms.modules.role.controller.Role", {
    extend: "Pms.abstract.Controller",
    views: [
        "Pms.modules.role.view.RoleForm",
        "Pms.modules.role.view.RoleGrid",
        "Pms.modules.role.view.RoleAddWindow",
        "Pms.modules.role.view.RoleEditWindow",
    ],
    stores: [
        "Pms.modules.role.store.Role"
    ],
    models: [
        "Pms.modules.role.model.Role"
    ],
    refs: [
        {
            ref: 'roleGrid',
            selector: 'roleGrid'
        },
        {
            ref: 'roleStore',
            selector: 'roleStore'
        },
        {
            ref: 'roomTypeEditWindow',
            selector: 'roomTypeEditWindow'
        }
    ],
    init: function () {
        this.control({
            'roleGrid button[action=new]': {
                click: this.newRole
            },
            'roleGrid button[action=edit]': {
                click: this.editRole
            },
            'roleAddWindow button[action=add-role]': {
                click: this.addRole
            },
            'roleEditWindow button[action=save-role]': {
                click: this.updateRole
            },
            'roleAddWindow': {
                close: this.refreshOnClose
            },
            'roleEditWindow': {
                close: this.refreshOnClose
            }
        });
    },

    newRole: function (button) {
        var win = Ext.widget('roleAddWindow');
        win.show();
    },

    addRole: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            rec = Ext.create('Pms.modules.role.model.Role', values),
            grid = this.getRoleGrid(),
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
            Ext.Msg.alert(l('warning'), Pms.iconError + ' ' + l('role.addError'));
        }
    },

    editRole: function (button, e) {
        var grid = button.up('roleGrid'),
            selected = grid.getSelectionModel().getSelection();

        if (selected.length) {
            var rec = selected[0];

            var win = Ext.widget('roleEditWindow', {
                    data: rec.data
                }),
                form = win.down('form');

            form.loadRecord(rec);
        }
        else {
            Ext.Msg.alert(l('warning'), Pms.iconError + ' ' + l('role.warning'));
        }
    },

    updateRole: function (button) {
        var win = button.up('roleEditWindow'),
            form = win.down('form'),
            record = form.getRecord(),
            values = form.getValues(),
            grid = this.getRoleGrid(),
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
            Ext.Msg.alert(l('error'), Pms.iconError + ' ' + l('role.updateError'));
        }
    },


    refreshOnClose: function () {
        var roomTypeEditWindow = this.getRoomTypeEditWindow();
        if (!Ext.isEmpty(roomTypeEditWindow)) {
            var roleGrid = roomTypeEditWindow.down('roleGrid');
            if (!Ext.isEmpty(roleGrid)) {
                roleGrid.getStore().reload();
                roleGrid.getSelectionModel().clearSelections();
            }
        }
    },
//    buildItems: function () {
//        if (typeof(this.win) !== "undefined") {
//            var view = Ext.create("Pms.modules.role.view.Viewport");
//            this.win.add(view);
//        }
//    }
});