Ext.define('Admin.modules.hotel.view.View', {
    extend: 'Admin.generic.form.ViewForm',
    items: Ext.create('Admin.modules.hotel.view.Form').restrictItemKeys(['name', 'fieldLabel']).toArray()
});
