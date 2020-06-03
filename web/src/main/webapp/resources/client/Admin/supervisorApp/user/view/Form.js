Ext.define('Admin.supervisorApp.user.view.Form', {
    extend: 'Admin.generic.form.FieldConfig',
    requires: ['Admin.generic.form.field.lookup.Picker'],
    items: {
        username: {
            fieldLabel: l('user.username'),
            required: true
        },
        password: {
            fieldLabel: l('user.password'),
            required: true,
            inputType: 'password'
        },
        language: {
            fieldLabel: l('user.language'),
            xtype: 'lookupCombobox',
            lookupType: 'language'
        },
        userType: {
            xtype: 'combobox',
            fieldLabel: l('user.userType'),
            displayField: 'name',
            valueField: 'id',
            store: Ext.create('Ext.data.Store', {
                fields: ['id', 'name'],
                data: [
                    {id: 'manager', name: l('userType.manager')}
                ]
            })
        }
        /*hotel: {
         fieldLabel: l('user.hotel'),
         xtype: 'combobox',
         store: Ext.create('Admin.modules.hotel.Store').load(),
         displayField: 'info',
         valueField: 'id',
         query: 'local',
         displayTpl: new Ext.XTemplate('<tpl for=".">',
         '{info.name}',
         '</tpl>'),
         listConfig: {
         getInnerTpl: function () {
         return '<div class="search-item">{info.name}</div>';
         }
         }
         }*/
    }
});
