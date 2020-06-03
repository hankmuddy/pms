Ext.define('Admin.modules.bookingButton.view.View', {
    extend: 'Admin.generic.form.ViewForm',
    items: [
        {
            name: 'currency',
            fieldLabel: l('currency'),
        },
        {
            name: 'name',
            fieldLabel: l('bb.name')
        },
        {
            name: 'hotel',
            fieldLabel: l('bb.hotel')
        },
        {
            name: 'language',
            fieldLabel: l('bb.language')
        },
        {
            name: 'textColor',
            fieldLabel: l('bb.textColor'),
        },
        {
            name: 'backgroundColor',
            fieldLabel: l('bb.backgroundColor'),
        },
        {
            name: 'cancel',
            fieldLabel: l('bb.cancel'),
            renderer: function (val) {
                return val != 'false' ? l('yes') : l('no')
            }
        },
        {
            name: 'width',
            fieldLabel: l('bb.width'),
        },
        {
            name: 'height',
            fieldLabel: l('bb.height'),
        }

    ]
//    items: Ext.create('Admin.modules.user.view.Form').restrictItemKeys(['name', 'fieldLabel']).merge(
//        {
//            language: {
//                name: 'language',
//                fieldLabel: l('user.language'),
//                xtype: 'lookupviewer',
//                lookupType: 'language'
//            },
//            userType: {
//                xtype: 'lookupviewer',
//                lookupType: 'userType',
//                fieldLabel: l('user.userType')
//            },
//            supervisor: {
//                name: 'supervisor',
//                fieldLabel: l('user.supervisor'),
//                xtype: 'displayfield',
//                renderer: function (val) {
//                    if (!val) {
//                        this.hide()
//                    }
//                    else return val
//                }
//            }
//        }
//    ).reject(['password']).toArray()
});
