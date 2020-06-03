Ext.define('Admin.generic.form.UpdateForm', {
    extend: 'Admin.generic.form.ModifiableForm',
    defaultType: 'textfield',
    alias: 'widget.rupdateform',
    buttons: [
        {
            text: l('common.update'),
            handler: function () {
                var me = this.up('form');
                var form = me.getForm();
                if (!form.isValid()) return;
                var record = form.getRecord();
                form.updateRecord(record);
                me.fireEvent('updateSubmit', record);
            }
        },
        {
            text: l('common.cancel'),
            handler: function () {
                this.up('form').fireEvent('cancel');
            }
        }
    ],
    initComponent: function () {
        this.addEvents('updateSubmit');
        this.callParent();
    }
});
