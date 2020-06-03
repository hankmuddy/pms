Ext.define('Admin.modules.profile.view.View', {
    extend: 'Admin.generic.form.ViewForm',
    fieldDefaults: {
        width: 400,
        labelWidth: 180
    },
    dockedItems: [
        {
            xtype: 'toolbar',
            dock: 'top',
            items: [
                {
                    text: l('common.edit'),
                    handler: function () {
                        this.up('form').fireEvent('edit');
                    },
                },
                {
                    text: l('changePassword'),
                    handler: function () {
                        this.up('form').fireEvent('changePassword');
                    },
                },
//                 {
//                     text: l('profile.back'),
//                     handler: function(){
//                         this.up('form').fireEvent('back');
//                     },
//                 },
            ]
        }
    ],
    items: [
        {
            name: 'username',
            fieldLabel: l('user.username'),
        },
        {
            name: 'userType',
            fieldLabel: l('user.userType'),
        },
//        {
//            name: 'backButton',
//            xtype: 'button',
//            text: l('profile.back')
//        }

    ]

});
