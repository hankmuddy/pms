Ext.define('Pms.modules.company.view.CompanyBookForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.companyBookForm',
//    layout: 'vbox',
    items: [
        {
            xtype: 'container',
            width: '100%',
            items: [
                {
                    xtype: 'fieldset',
                    title: l('company.companySearch'),
                    defaultType: 'textfield',
                    margin: '0 5px',
                    items: [
                        {
                            xtype: 'hidden',
                            name: 'id'
                        },
                        {
                            fieldLabel: l('company'),
                            xtype: 'combobox',
                            store: Ext.create('Pms.modules.company.store.Company', {fromSearchCombo: true}),
                            name: 'company',
                            displayField: 'name',
                            valueField: 'id',
                            typeAhead: false,
                            hideLabel: true,
                            hideTrigger: true,
                            anchor: '100%',
                            emptyText: l('company'),
                            querymode: 'remote',
                            queryParam: '',
                            minChars: 1,
                            listConfig: {
                                loadingText: l('searchText'),
                                emptyText: l('error.noMatches'),
                                getInnerTpl: function () {
                                    return '<div class="search-item">' +
                                        '<b>{name}</b>' +
                                        '</div>';
                                }
                            },
                            listeners: {
                                select: function (combo, selection) {
                                    var form = combo.up('companyBookForm');
                                    form.getForm().setValues(selection[0].data);
                                },
                                change: function (combo, newValue, oldValue, eOpts) {
                                    if (newValue && newValue.length > 0) {
                                        combo.store.filterParams = [
                                            {
                                                field: 'name',
                                                comparison: 'like',
                                                data: {
                                                    type: 'string',
                                                    value: newValue
                                                }
                                            }
                                        ];
                                    }
                                }
                            }
                        }
                    ]
                }
            ]
        },
        {
            xtype: 'container',
            layout: 'hbox',
            items: [
                {

                    xtype: 'fieldset',
                    margin: '0 5px',
                    title: l('common'),
                    defaultType: 'textfield',

                    items: [
                        {
                            fieldLabel: l('title') + Pms.requiredStatus,
                            name: 'name',
                            allowBlank: false
                        },
                        {
                            fieldLabel: l('inn'),
                            name: 'inn'
                        },
                        {
                            xtype: 'fieldcontainer',
                            layout: 'hbox',
                            items: [
                                {
                                    xtype: 'checkbox',
                                    fieldLabel: l('responsiblePerson'),
                                    name: 'customer',
                                    inputValue: true,
                                    uncheckedValue: 'false',
                                    width: 150,
                                    listeners: {
                                        change: function (button, val) {
                                            var win = button.up('window'),
                                                tabPanel = win.down('tabpanel');

                                            function addNewTab() {
                                                tabPanel.tabCount = tabPanel.items.items.length - 2;
                                                tabPanel.insert(tabPanel.tabCount, {
                                                    title: l('responsiblePerson'),// + tabPanel.tabCount,
                                                    itemId: 'companyCustomer',
                                                    items: [
                                                        {
                                                            xtype: 'personBookForm'
                                                        }
                                                    ]
                                                }).show();
                                                tabPanel.tabCount++;
                                            }

                                            if (val) {
                                                addNewTab();
                                                win.down('checkboxfield[name=isGroupMember]').enable();
                                                win.down('checkboxfield[name=customerPays]').enable();
                                            }
                                            else {
                                                tabPanel.down('#companyCustomer').close();
                                                win.down('checkboxfield[name=isGroupMember]').disable().setValue(false);
                                                win.down('checkboxfield[name=customerPays]').disable().setValue(false);
                                            }
                                        }
                                    }
                                },
//                                {
//                                    xtype: 'checkbox',
//                                    name: 'includeCustomer',
//                                    inputValue: true,
//                                    fieldLabel: l('addToGroup'),
//                                    disabled: true
//                                }
                            ]
                        }

                    ]
                },
                {
                    xtype: 'fieldset',
                    margin: '0 5px 0 6px',
                    title: l('contacts'),
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
                            fieldLabel: l('phone'),
                            name: 'phone'
                        }
                    ]
                }
            ]
        }
    ],

    bbar: ['->', {
        iconCls: 'app-icon-costume-data',
        text: l('companyDetails'),
        action: 'detail'
    }]
});