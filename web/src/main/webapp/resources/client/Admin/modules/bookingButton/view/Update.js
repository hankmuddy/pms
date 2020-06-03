Ext.define('Admin.modules.bookingButton.view.Update', {
    extend: 'Admin.generic.form.UpdateForm',

    initComponent: function () {
        var me = this;

        me.items = [
            {
                xtype: 'fieldset',
                name: 'info',
                id: 'myForm',
                maxWidth: 500,
                title: l('bb.info'),
                defaultType: 'textfield',
                defaults: {
                    width: '75%'
                },
                layout: {
                    type: 'vbox',
                    align: 'center'
                },
                frame: true,
                margin: '5px',
                items: [
                    {
                        name: 'name',
                        fieldLabel: l('bb.name') + me.requiredStatus,
                        allowBlank: false
                    },
                    {
                        fieldLabel: l('bb.hotel'),
                        name: "hotel",
                        xtype: 'combobox',
                        store: Ext.create('Admin.modules.hotel.Store', {sorters: [
                            {
                                property: 'info.name',
                                direction: 'ASC'
                            }
                        ]}).load(),
                        displayField: "hotelId",
                        valueField: "hotelId",
                        queryMode: "remote",
                        listConfig: {
                            getInnerTpl: function () {
                                return '{info.name} ({hotelId})'
                            }
                        },
                        editable: false,
                        allowBlank: false
                    },
                    {
                        fieldLabel: l('bb.language'),
                        xtype: 'lookupCombobox',
                        lookupType: 'language',
                        name: 'language'
                    },
                    {
                        name: 'textColor',
                        fieldLabel: l('bb.textColor'),
                        allowBlank: true
                    },
                    {
                        name: 'backgroundColor',
                        fieldLabel: l('bb.backgroundColor'),
                        allowBlank: true
                    },
                    {
                        xtype: 'checkbox',
                        name: 'cancel',
                        fieldLabel: l('bb.cancel'),
                        allowBlank: false
                    },
                    {
                        name: 'width',
                        fieldLabel: l('bb.width'),
                        allowBlank: true
                    },
                    {
                        name: 'height',
                        fieldLabel: l('bb.height'),
                        allowBlank: true
                    }
                ]
            },
            {
                xtype: 'fieldset',
                name: 'info',
                id: 'settingsForm',
                maxWidth: 500,
                title: l('bb.info'),
                defaultType: 'textfield',
                defaults: {
                    width: '75%'
                },
                layout: {
                    type: 'vbox',
                    align: 'center'
                },
                frame: true,
                margin: '5px',
                items: [

                ]
            }
        ];

        me.callParent(arguments);

//        console.log(me.getForm().getValues());

        me.initParams(me);
    },

    initParams: function (me) {

//        var settings = [
//            'textColor',
//            'buttonColor',
//            'buttonTextColor',
//            'buttonHover'
//        ];
//
//        var form = Ext.getCmp('settingsForm');
//
//        for (var i = 0; i <= settings.length - 1; i++) {
//            var keyField = Ext.create('Ext.form.field.Text', {
//                name: 'key',
//                hidden: true,
//                fieldLabel: 'Key',
//                readOnly: true,
//                value: settings[i],
//                xtype: 'hidden'
//            });
//
//            var valueField = Ext.create('Ext.form.field.Text', {
//                fieldLabel: settings[i],
//                name: 'value'
//            });
//
//            form.add(keyField);
//            form.add(valueField);
//        }
    }

});
