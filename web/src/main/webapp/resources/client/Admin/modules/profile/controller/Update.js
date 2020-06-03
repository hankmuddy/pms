Ext.define('Admin.modules.profile.controller.Update', {
    extend: 'Admin.generic.app.controller.Update',
    viewClass: 'Admin.modules.profile.view.Update',
    viewChangePassword: 'Admin.modules.profile.view.ChangePassword',
    modelClass: 'Admin.modules.profile.Model',
    convertBeforeRender: function (record) {
        var debitCompanyVal = null;
        var debitCompanyReceived = record.get('debitCompany');
        if (debitCompanyReceived) {
            debitCompanyVal = debitCompanyReceived.id;
            record.set('debitCompany', debitCompanyVal)
        }
        return record;
    },
    initView: function () {
        if (Admin.generic.Utils.getHashPart(4) == 'password')
            this.view = Ext.create(this.viewChangePassword);
        else
            this.view = Ext.create(this.viewClass)
    },
    setBreadcrumbs: function (record) {

    },
    setHandlersOnViewEvents: function () {
        this.view.on('updatePassword', this.onUpdatePassword, this)
        this.callParent(arguments)
    },
    unsetHandlersOnViewEvents: function () {
        this.view.un('updatePassword', this.onUpdatePassword, this)
        this.callParent(arguments)
    },
    onUpdateSubmit: function (data) {
        Admin.generic.Utils.request(({
            scope: this,
            method: 'PUT',
            url: 'admin/authentication/' + this.currentId + '/language',
            jsonData: {value: data.language},
            success: function (record, b, c) {
                Admin.getApplication().navigateTo('profile');
            },
            failure: function (a, b) {
                var errorpanel = this.view.down('errorpanel'),
                    msg = JSON.parse(a.responseText)[0].code;
                errorpanel.setErrorTitle(l(msg));
                errorpanel.show();
            }
        }))
    },
    onUpdatePassword: function (data) {
        delete data.passwordAgain;
        Admin.generic.Utils.request(({
            scope: this,
            method: 'PUT',
            url: 'admin/authentication/' + this.currentId + '/password',
            jsonData: data,
            success: function (record, b, c) {
                Admin.getApplication().navigateTo('profile');
            },
            failure: function (a, b) {
                console.log(arguments)
                var code = JSON.parse(a.responseText)[0].code;
                var errorpanel = this.view.down('errorpanel')
                errorpanel.setErrorTitle(l(code));
                errorpanel.show();
            }
        }))
    },
    onCancel: function () {
//        history.back();
        var errorpanel = this.view.down('errorpanel')
        if (errorpanel) {
            errorpanel.clear();
            errorpanel.hide();
        }
        Admin.getApplication().navigateTo('profile');
    }
});
