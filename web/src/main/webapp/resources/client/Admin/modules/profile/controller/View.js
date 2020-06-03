Ext.define('Admin.modules.profile.controller.View', {
    extend: 'Admin.generic.app.controller.View',
    modelClass: 'Admin.modules.profile.Model',
    viewClass: 'Admin.modules.profile.view.View',
    getId: function () {
        this.currentId = _('userId');
    },
    setHandlersOnViewEvents: function () {
        this.callParent(arguments);
//        this.view.on('back', this.onBack, this);
        this.view.on('changePassword', this.onChangePassword, this);
    },
    unsetHandlersOnViewEvents: function () {
        this.callParent(arguments);
//        this.view.un('back', this.onBack, this);
        this.view.un('changePassword', this.onChangePassword, this);
    },
//    onBack: function(){
//        Admin.getAdminlication().navigateTo('');
//    },
    setBreadcrumbs: function (record, conf) {

    },
    onChangePassword: function () {
        Admin.getApplication().navigateTo(this.links['edit'] + '/' + this.currentId + '/password')
    }
});
