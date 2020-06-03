Ext.define('Admin.modules.user.view.Update', {
    extend: 'Admin.generic.form.UpdateForm',
    items: Ext.create('Admin.modules.user.view.Form').reject(['userId']).toArray()
});
