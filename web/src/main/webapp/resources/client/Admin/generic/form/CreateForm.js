Ext.define('Admin.generic.form.CreateForm', {
    extend: 'Admin.generic.form.ModifiableForm',
    defaultType: 'textfield',
    alias: 'widget.rcreateform',
    buttons: [
        {
            text: l('common.create'),
            requestDisable: true,
            handler: function () {
                var me = this.up('form'),
                    form = me.getForm();

                if (!form.isValid()) return;
                var data = form.getFieldValues();
                me.fireEvent('createSubmit', data);
            }
        },
//        {
//            text: l('common.reset'),
//            handler: function () {
//                var me = this.up('form');
//                var form = me.getForm()
//                form.reset()
//                var errorpanel = me.down('errorpanel');
//                errorpanel.clear();
//                errorpanel.hide();
//            }
//        },
        {
            text: l('common.cancel'),
            handler: function () {
//                var me = this.up('form');
//                var form = me.getForm();
//                form.reset();
                this.up('form').fireEvent('cancel');
            }
        }
    ],
    initComponent: function () {
        this.addEvents('createSubmit');
        this.callParent();
    }
});
