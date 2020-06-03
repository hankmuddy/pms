Ext.define('Admin.generic.form.ViewForm', {
    extend: 'Admin.generic.form.BaseForm',
    requires: ['Admin.generic.form.field.lookup.Viewer', 'Admin.generic.form.field.MoneyViewer'],
    defaultType: 'displayfield'
});
