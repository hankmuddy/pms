Ext.define('Admin.modules.bookingButton.view.Create', {
    extend: 'Admin.generic.form.CreateForm',
    title: l('bb.add'),
    width: 550,

    requiredStatus: '<span style="color:red;font-weight:bold" data-qtip="' + l('common.required') + '">*</span>',

    initComponent: function () {
        var me = this;

        me.items = [
            {
                xtype: 'fieldset',
                name: 'info',
                id: 'myForm',
                maxWidth: 500,
                title: l('bb.widgetSettings'),
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
                        xtype: 'combobox',
                        name: 'currency',
                        fieldLabel: l('currency'),
                        allowBlank: false,
                        store: Ext.create('Ext.data.Store', {
                            fields: ['code', 'label'],
                            data: [
                                {code: 'RUR', label: 'p'},
                                {code: 'USD', label: '$'},
                                {code: 'UAH', label: '₴'},
                                {code: 'EUR', label: '€'},
                                {code: 'GPB', label: '£'},
                                {code: 'BYR', label: 'Br'},
                                {code: 'GEL', label: ''},
                                {code: 'MDL', label: ''},
                                {code: 'KZT', label: '₸'},
                                {code: 'AZN', label: ''}
                            ]
                        }).load(),
                        queryMode: 'local',
                        valueField: 'code',
                        listConfig: {
                            getInnerTpl: function () {
                                return '<div class="search-item">' +
                                    '<span><tpl switch="code">' +
                                    '<tpl case="UAH">' +
                                    l('currency.uah') +
                                    '<tpl case="RUR">' +
                                    l('currency.rur') +
                                    '<tpl case="USD">' +
                                    l('currency.usd') +
                                    '<tpl case="EUR">' +
                                    l('currency.eur') +
                                    '<tpl case="GPB">' +
                                    l('currency.gpb') +
                                    '<tpl case="BYR">' +
                                    l('currency.byr') +
                                    '<tpl case="GEL">' +
                                    l('currency.gel') +
                                    '<tpl case="MDL">' +
                                    l('currency.mdl') +
                                    '<tpl case="KZT">' +
                                    l('currency.kzt') +
                                    '<tpl case="AZN">' +
                                    l('currency.azn') +
                                    '</tpl><b> {label}</b></span>' +
                                    '</div>';
                            }
                        },
                        displayField: 'code'
                    },
                    {
                        name: 'name',
                        fieldLabel: l('bb.name') + me.requiredStatus,
                        allowBlank: false
                    },
                    {
                        fieldLabel: l('bb.hotel') + me.requiredStatus,
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
                title: l('bb.bbSettings'),
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
//                    {
//                        xtype: 'button',
//                        text: l('bb.addSettings'),
//                        id: 'addLinkBtn'
//                    }
                ]
            }
        ];
        me.callParent(arguments);

        me.initParams();
    },

    initParams: function () {
        var settings = [
            'textColor',
            'buttonColor',
            'buttonTextColor',
            'buttonHover'
        ];

        var form = Ext.getCmp('settingsForm');

        for (var i = 0; i <= settings.length - 1; i++) {
            var keyField = Ext.create('Ext.form.field.Text', {
                name: 'key',
                hidden: true,
                fieldLabel: 'Key',
                readOnly: true,
                value: settings[i],
                xtype: 'hidden'
            });

            var valueField = Ext.create('Ext.ux.ColorField', {
                fieldLabel: settings[i],
                name: 'value'
            });

            form.add(keyField);
            form.add(valueField);
        }
    }
});
