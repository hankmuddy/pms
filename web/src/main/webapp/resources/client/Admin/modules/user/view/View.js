Ext.define('Admin.modules.user.view.View', {
    extend: 'Admin.generic.form.ViewForm',
    items: Ext.create('Admin.modules.user.view.Form').restrictItemKeys(['name', 'fieldLabel']).merge(
        {
            language: {
                name: 'language',
                fieldLabel: l('user.language'),
                xtype: 'lookupviewer',
                lookupType: 'language'
            },
            userType: {
                xtype: 'lookupviewer',
                lookupType: 'userType',
                fieldLabel: l('user.userType')
            },
            supervisor: {
                name: 'supervisor',
                fieldLabel: l('user.supervisor'),
                xtype: 'displayfield',
                renderer: function (val) {
                    if (!val) {
                        this.hide()
                    }
                    else return val
                }
            }
        }
    ).reject(['password']).toArray()
});
