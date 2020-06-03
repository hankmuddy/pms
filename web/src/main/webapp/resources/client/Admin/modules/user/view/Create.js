Ext.define('Admin.modules.user.view.Create', {
    extend: 'Admin.generic.form.CreateForm',
    title: l('user.add'),
    width: 550,
    items: Ext.create('Admin.modules.user.view.Form').toArray(),
    requiredStatus: '<span style="color:red;font-weight:bold" data-qtip="' + l('common.required') + '">*</span>'
//    initComponent: function () {
//        var me = this;
//        me.callParent(arguments);
//    }
});
