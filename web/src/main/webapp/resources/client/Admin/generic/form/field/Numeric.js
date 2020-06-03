Ext.define('Admin.generic.form.field.Numeric', {
    extend: 'Ext.form.field.Number',
    alias: 'widget.numericfield',
    mouseWheelEnabled: false,
    hideTrigger: true,
    validateOnChange: true,
    emptyText:l('common.only_numeric')
});
