Ext.define("Pms.modules.user.controller.User", {
    extend: "Pms.abstract.Controller",
    views: [
        "Pms.modules.user.view.UserForm",
        "Pms.modules.user.view.UserGrid",
        "Pms.modules.user.view.UserAddWindow",
        "Pms.modules.user.view.UserEditWindow",
        "Pms.modules.user.view.Viewport",
        "Pms.modules.role.view.RoleGrid"
    ],
    stores: [
        "Pms.modules.user.store.User"
    ],
    models: [
        "Pms.modules.user.model.User"
    ],
    refs: [
        {
            ref: 'userGrid',
            selector: 'userGrid'
        },
        {
            ref: 'userStore',
            selector: 'userStore'
        },
        {
            ref: 'roomTypeEditWindow',
            selector: 'roomTypeEditWindow'
        }
    ],
    subControllers: [
        'Pms.modules.role.controller.Role'
    ],
    init: function (contr, subController) {

        if (!subController) {
            var view = Ext.create("Pms.modules.user.view.Viewport");
            this.buildItems(view);
        }

        this.control({
            'userGrid button[action=new]': {
                click: this.newUser
            },
            'userGrid button[action=edit]': {
                click: this.editUser
            },
            'userAddWindow button[action=add-user]': {
                click: this.addUser
            },
            'userEditWindow button[action=save-user]': {
                click: this.updateUser
            },
            'userAddWindow': {
                close: this.refreshOnClose
            },
            'userEditWindow': {
                close: this.refreshOnClose
            },
            'userForm button[action=save-self]': {
                click: this.saveSelf
            }
        });
    },

    newUser: function (button) {
        var win = Ext.widget('userAddWindow');
        win.show();
    },

    addUser: function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            rec = Ext.create('Pms.modules.user.model.User', values),
            recAuthentication = Ext.create('Pms.modules.user.model.Authentication', values),
            grid = this.getUserGrid(),
            store = grid.getStore();
        delete recAuthentication.data.id;
        delete rec.data.id;
        recAuthentication.data.userType = 'user';
        rec.data.role = {id: rec.data.role};
        if (form.isValid()) {
            Pms.Ajax.request({
                url: 'rest/user',
                method: 'POST',
                jsonData: {
                    user: rec.data,
                    authentication: recAuthentication.data
                },
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
            Ext.Msg.alert(l('warning'), Pms.iconError + ' ' + l('user.addError'));
        }
    },

    editUser: function (button, e) {
        var grid = button.up('userGrid'),
            selected = grid.getSelectionModel().getSelection();

        if (selected.length) {
            var rec = selected[0];
            Pms.Ajax.request({
                url: 'rest/user/' + rec.getId(),
                method: 'GET',
                async: false,
                success: function (res) {
                    var win = Ext.widget('userEditWindow', {
                            data: rec.data
                        }),
                        form = win.down('form');

                    form.loadRecord(rec);
                    form.down('combobox[name=role]').setValue(rec.data.role.id);
                    form.down('combobox[itemId=editWinLanguage]').setValue(res.content.language);
                }
            });


        }
        else {
            Ext.Msg.alert(l('warning'), Pms.iconError + ' ' + l('user.warning'));
        }
    },

    updateUser: function (button) {
        var win = button.up('userEditWindow'),
            form = win.down('form'),
            record = form.getRecord(),
            values = form.getValues(),
            grid = this.getUserGrid(),
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
            Ext.Msg.alert(l('error'), Pms.iconError + ' ' + l('user.updateError'));
        }
    },


    refreshOnClose: function () {
        var roomTypeEditWindow = this.getRoomTypeEditWindow();
        if (!Ext.isEmpty(roomTypeEditWindow)) {
            var userGrid = roomTypeEditWindow.down('userGrid');
            if (!Ext.isEmpty(userGrid)) {
                userGrid.getStore().reload();
                userGrid.getSelectionModel().clearSelections();
            }
        }
    },
    saveSelf: function (btn) {
        var form = btn.up('form').getForm(),
            values = form.getValues();
        values.role = {id: values.role};
        Pms.Ajax.request({
            url: 'rest/user/' + _('user'),
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
    },
//    buildItems: function () {
//        if (typeof(this.win) !== "undefined") {
//            var view = Ext.create("Pms.modules.user.view.Viewport");
////            Pms.Ajax.request({
////                url: 'rest/user/' + _('user'),
////                method: 'GET',
////                async: false,
////                success: function (res) {
////                    var userForm = view.down('userForm');
////                    userForm.getForm().setValues(res.content);
////                    userForm.down('combobox[name=role]').setValue(res.content.role.id)
////                    userForm.down('lookupCombobox[itemId=editWinLanguage]').setValue(res.content.language);
////                }
////            });
//            this.win.add(view);
//        }
//    }
});