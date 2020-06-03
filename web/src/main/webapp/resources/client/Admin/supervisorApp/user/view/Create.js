Ext.define('Admin.supervisorApp.user.view.Create', {
    extend: 'Admin.modules.user.view.Create',
    items: Ext.create('Admin.supervisorApp.user.view.Form').toArray(),
    requiredStatus: '<span style="color:red;font-weight:bold" data-qtip="' + l('common.required') + '">*</span>',
//    initComponent: function () {
//        var me = this;
//        me.callParent(arguments);
//    }
})
;
