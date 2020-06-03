Ext.define('Admin.modules.user.view.Form', {
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
            xtype: 'lookupCombobox',
            lookupType: 'userType',
            fieldLabel: l('user.userType'),
            listeners: {
                change: function (combo, val) {
                    var supervisorCombo = combo.nextNode();
                    if (val == 'manager' && supervisorCombo.hidden) {
                        supervisorCombo.show()
                    }
                    else if (val != 'manager' && !supervisorCombo.hidden) {
                        supervisorCombo.hide()
                    }
                }
            }
        },
        supervisor: {
            xtype: 'combobox',
            hidden: true,
            fieldLabel: l('user.supervisor'),
            store: Ext.create('Admin.modules.user.Store', {
                filters: [
                    function (rec) {
                        return rec.data.userType == 'managerSupervisor'
                    }
                ]
            }).load(),
            valueField: 'id',
            displayField: 'username'
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
