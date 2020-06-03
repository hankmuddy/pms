Ext.define('Pms.modules.user.view.UserForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.userForm',
    requires: ['Pms.abstract.field.lookup.Picker'],
    layout: 'vbox',
    editForm: false,
    data: {},

    initComponent: function () {
        var me = this;
        me.items = this.buildItems();
        me.callParent();
    },

    buildItems: function () {
        var me = this;
        return  [
            {
                xtype: 'fieldset',
                padding: 10,
                hidden: me.editForm,
                defaults: {
                    xtype: 'textfield',
                    anchor: '100%',
                    allowBlank: !!me.editForm

                },
                items: [
                    {
                        fieldLabel: l('user.username') + Pms.requiredStatus,
                        name: "username"
                    },
                    {
                        fieldLabel: l('user.password') + Pms.requiredStatus,
                        name: "password",
                        inputType: 'password'
                    },
                    {
                        fieldLabel: l('user.language') + Pms.requiredStatus,
                        name: "language",
                        lookupType: 'language',
                        xtype: 'lookupCombobox',
                        valueNotFoundText: null,
                        submitValue: !me.editForm
                    }
                ]
            },
            {
                xtype: 'fieldset',
                padding: 10,
                defaults: {
                    xtype: 'textfield',
                    anchor: '100%'
                },
                items: [
                    {
                        xtype: 'hidden',
                        name: 'id'
                    },
                    {
                        fieldLabel: l('user.firstName') + Pms.requiredStatus,
                        name: "firstName",
                        allowBlank: false
                    },
                    {
                        fieldLabel: l('user.lastName') + Pms.requiredStatus,
                        name: "lastName",
                        allowBlank: false
                    },
                    {
                        fieldLabel: l('user.patronymic'),
                        name: "patronymic"
                    },
                    {
                        fieldLabel: l('user.role') + Pms.requiredStatus,
                        name: "role",
                        xtype: 'combobox',
                        store: Ext.create('Pms.modules.role.store.Role').load(),
                        displayField: 'name',
                        valueField: 'id',
                        allowBlank: false
                    },
                    {
                        fieldLabel: l('user.phone') + Pms.requiredStatus,
                        name: "phone",
                        allowBlank: false
                    },
                    {
                        fieldLabel: l('user.email'),
                        name: "email",
                        vtype: 'email'
                    },
                    {
                        fieldLabel: l('user.address'),
                        name: "address"
                    },
                    {
                        fieldLabel: l('user.position'),
                        name: "position"
                    },
                    {
                        fieldLabel: l('user.language') + Pms.requiredStatus,
                        name: "language",
                        lookupType: 'language',
                        itemId: 'editWinLanguage',
                        xtype: 'lookupCombobox',
                        valueNotFoundText: null,
                        hidden: !me.editForm,
                        submitValue: me.editForm
                    }
                ]
            }
        ];
    }
});