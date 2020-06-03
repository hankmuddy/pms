Ext.define('Admin.modules.profile.view.Update', {
    extend: 'Admin.generic.form.UpdateForm',
    items: Ext.create('Admin.modules.profile.view.Form').toArray(),
    fieldDefaults: {
        width: 400,
        labelWidth: 180
    },
    buttons: [
        {
            text: l('common.update'),
            handler: function () {
                var me = this.up('form');
                var form = me.getForm();
                if (!form.isValid()) return;
                var values = form.getValues();
                me.fireEvent('updateSubmit', values);
            }
        },
        {
            text: l('common.cancel'),
            handler: function () {
                this.up('form').fireEvent('cancel');
            }
        }
    ],
});
