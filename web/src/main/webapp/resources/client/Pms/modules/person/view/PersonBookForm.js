Ext.define('Pms.modules.person.view.PersonBookForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.personBookForm',

//    layout: 'vbox',

    data: {},
    personId: null,

    initComponent: function () {
        var me = this,
            selectedPerson = [
                {
                    xtype: 'fieldset',
                    margin: '0 5px',
                    title: l('baseInformation'),
                    defaultType: 'textfield',
                    items: [
                        {
                            fieldLabel: l('person.lastName'),
                            name: 'lastName',
                            readOnly: true
                        },
                        {
                            fieldLabel: l('person.firstName'),
                            name: 'firstName',
                            readOnly: true
                        },
                        {
                            fieldLabel: l('person.phone'),
                            name: 'phone',
                            readOnly: true
                        },
                        {
                            name: 'id',
                            xtype: 'numberfield',
                            hidden: true,
                            readOnly: true
                        }
                    ]
                },
                {
                    xtype: 'fieldset',
                    margin: '0 5px 0 6px',
                    title: l('additionalInformation'),
                    defaultType: 'textfield',
                    items: [
                        {
                            fieldLabel: l('country'),
                            name: 'country',
                            xtype: 'combobox',
                            store: Ext.create('Pms.modules.person.store.Country'),
                            valueField: 'abbr',
                            displayField: 'country',
                            queryMode: 'local',
                            readOnly: true
                        },
                        {
                            fieldLabel: l('city'),
                            name: 'city',
                            readOnly: true
                        },
                        {
                            fieldLabel: l('email'),
                            name: 'email',
                            readOnly: true
                        },
                        {
                            fieldLabel: l('dateOfBirth'),
                            xtype: 'pmsdatefield',
                            name: 'dob',
                            format: 'd/m/Y',
                            submitFormat: 'U',
                            readOnly: true
                        }
                    ]
                }
            ],
            newPerson = [
                {
                    xtype: 'fieldset',
                    margin: '0 5px',
                    title: l('baseInformation'),
                    defaultType: 'textfield',
                    items: [
                        {
                            fieldLabel: l('person.lastName') + Pms.requiredStatus,
                            name: 'lastName',
                            allowBlank: false
                        },
                        {
                            fieldLabel: l('person.firstName') + Pms.requiredStatus,
                            name: 'firstName',
                            allowBlank: false
                        },
                        {
                            fieldLabel: l('person.phone') + Pms.requiredStatus,
                            name: 'phone',
                            allowBlank: false
                        }
                    ]
                },
                {
                    xtype: 'fieldset',
                    margin: '0 5px 0 6px',
                    title: l('additionalInformation'),
                    defaultType: 'textfield',
                    items: [
                        {
                            fieldLabel: l('country'),
                            name: 'country',
                            xtype: 'combobox',
                            store: Ext.create('Pms.modules.person.store.Country'),
                            valueField: 'abbr',
                            displayField: 'country',
                            queryMode: 'local'
                        },
                        {
                            fieldLabel: l('city'),
                            name: 'city'
                        },
                        {
                            fieldLabel: l('email'),
                            name: 'email'
                        },
                        {
                            fieldLabel: l('dateOfBirth'),
                            xtype: 'pmsdatefield',
                            name: 'dob',
                            format: 'd/m/Y',
                            submitFormat: 'U'
                        },
                        {
                            xtype: 'numberfield',
                            name: 'id',
                            hidden: true
                        },
                        {
                            fieldLabel: l('person.autoMark'),
                            name: 'carBrand',
                            hidden: true
                        },
                        {
                            fieldLabel: l('person.carNumber'),
                            name: 'carNumber',
                            hidden: true
                        },
                        {
                            fieldLabel: l('person.visaType'),
                            name: 'visaType',
                            lookupType: 'visaType',
                            xtype: 'lookupCombobox',
                            hidden: true
                        },
                        {
                            fieldLabel: l('person.entryValidFrom'),
                            name: 'entryValidFrom',
                            hidden: true
                        },
                        {
                            fieldLabel: l('person.entryValidTill'),
                            name: 'entryValidTill',
                            hidden: true
                        },
                        {
                            fieldLabel: l('room'),
                            name: 'entryNumber',
                            hidden: true
                        },
                        {
                            fieldLabel: l('person.passportNumber'),
                            name: 'passportNumber',
                            hidden: true
                        },
                        {
                            fieldLabel: l('person.passportValidTill'),
                            name: 'passportValidTill',
                            hidden: true
                        },
                        {
                            fieldLabel: l('person.passportIssued'),
                            name: 'passportIssued',
                            hidden: true
                        },
                        {
                            fieldLabel: l('person.cio'),
                            name: 'cio',
                            hidden: true
                        },
                        {
                            fieldLabel: l('person.entryType'),
                            name: 'entryType',
                            hidden: true
                        },
                        {
                            fieldLabel: l('person.address'),
                            name: 'address',
                            hidden: true
                        },
                        {
                            fieldLabel: l('patronymic'),
                            name: 'patronymic',
                            hidden: true
                        },
                        {
                            fieldLabel: l('language'),
                            name: 'language',
                            hidden: true
                        },
                        {
                            name: 'identity',
                            hidden: true
                        }
                    ]
                }
            ];


        me.items = [
            {
                xtype: 'container',
                width: '100%',
                items: [
                    {
                        xtype: 'fieldset',
                        title: l('searchGuest') ,
                        defaultType: 'textfield',
                        margin: '0 5px',
                        items: [
                            {
                                fieldLabel: l('lastName'),
                                xtype: 'combobox',
                                store: Ext.create('Pms.modules.person.store.Person', {fromSearchCombo: true}),
                                name: 'person',
                                displayField: 'lastName',
                                valueField: 'id',
                                typeAhead: false,
                                hideLabel: true,
                                //                            hideTrigger: true,
                                anchor: '100%',
                                emptyText: l('lastName'),
                                querymode: 'remote',
                                queryParam: '',
                                minChars: 1,
                                listConfig: {
                                    loadingText: l('searchText'),
                                    emptyText: l('error.noMatches'),
                                    getInnerTpl: function () {
                                        return '<div class="search-item">' +
                                            '<span><b>{lastName} {firstName} {patronymic}</b></span>' +
                                            '<tpl if="dob">' +
                                            '<span style="float:right;color:#999999"><i>' +
                                            l('dateOfBirth') + ': {[fm.date(values.dob,"d/m/Y")]}' +
                                            '<tpl if="city"><br />{city}, </tpl>' +
                                            '<tpl if="adress">{address}</tpl>' +
                                            '</i></span>' +
                                            '</tpl>' +
                                            '<br /><i>{email}</i><br /><i>{phone}</i>' +
                                            '</div>';
                                    }
                                },
                                listeners: {
                                    select: function (combo, selection) {
                                        var form = combo.up('personBookForm'),
                                            container = form.down('container[ident=personData]');
                                        container.removeAll();
                                        container.add(selectedPerson);
                                        selection[0].data.person = selection[0].data.id;
                                        form.getForm().setValues(selection[0].data);
                                    },
                                    change: function (combo, newValue, oldValue, eOpts) {
                                        if (newValue && newValue.length > 0) {
                                            combo.store.filterParams = [
                                                {
                                                    field: 'lastName',
                                                    comparison: 'like',
                                                    data: {
                                                        type: 'string',
                                                        value: newValue
                                                    }
                                                }
                                            ];
                                        }
                                    }
                                },
                                trigger1Cls: 'x-form-clear-trigger',
                                onTrigger1Click: function (e) {
                                    var me = this,
                                        form = me.up('personBookForm'),
                                        container = form.down('container[ident=personData]');
                                    me.clearValue();
                                    container.removeAll();
                                    container.add(newPerson);
                                }
                            }
                        ]
                    }
                ]
            },
            {
                xtype: 'container',
                ident: 'personData',
                layout: 'hbox',
                items: me.oldPerson ? selectedPerson : newPerson
            }
        ];
        me.callParent(arguments);
    },

    bbar: ['->', {
        iconCls: 'app-icon-costume-data',
        text: l('person.detailData') + ' ...',
        action: 'detail'
    }]
});