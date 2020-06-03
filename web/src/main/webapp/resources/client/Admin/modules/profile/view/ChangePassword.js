Ext.define('Admin.modules.profile.view.ChangePassword', {
    extend: 'Admin.generic.form.UpdateForm',
    fieldDefaults: {
        width: 400,
        labelWidth: 180
    },
    items: [
        {
            name: 'oldPassword',
            fieldLabel: l('oldPassword'),
            inputType: 'password',
            required: true
        },
        {
            name: 'newPassword',
            fieldLabel: l('newPassword'),
            required: true,
//            validator: function(value){
//                if(value != '' && value.length < 6){
//                    return l('error.password.lengthToShort')
//                }
//                return true
//            },
            inputType: 'password',
            validateOnChange: false
        },
        {
            name: 'passwordAgain',
            fieldLabel: l('newPasswordAgain'),
            required: true,
            validator: function (value) {
                var password = this.up('form').getForm().findField('newPassword').getValue();
                if (value != password) {
                    return l('error.password.notMatch')
                }
                return true
            },
            inputType: 'password',
            validateOnChange: false
        }
    ],
    buttons: [
        {
            text: l('common.update'),
            handler: function () {
                var me = this.up('form');
                var form = me.getForm();
                if (!form.isValid()) return;
                var data = form.getFieldValues();
                me.fireEvent('updatePassword', data);
            }
        },
        {
            text: l('common.cancel'),
            handler: function () {
                this.up('form').fireEvent('cancel');
            }
        },
    ],
});
