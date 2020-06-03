Ext.define('Pms.modules.child.view.ChildBookForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.childBookForm',

    layout: 'vbox',
    // autoScroll: true,

    items: [
        {
            xtype: 'container',
            width: '100%',
            items: [
                {
                    xtype: 'fieldset',
                    title: l('searchGuest'),
                    defaultType: 'textfield',
                    margin: '0 5px',
                    items: [
                        {
                            xtype: 'hidden',
                            name: 'id'
                        },
                        {
                            xtype: 'combobox',
                            store: Ext.create('Pms.modules.child.store.Child', {fromSearchCombo: true}),
                            name: 'childSearch',
                            displayField: 'lastName',
                            valueField: 'id',
                            typeAhead: false,
                            hideLabel: true,
                            hideTrigger: true,
                            anchor: '100%',
                            emptyText: l('childLastName'),
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
                                        '</i></span>' +
                                        '</tpl>' +
                                        '</div>';
                                }
                            },
                            listeners: {
                                select: function (combo, selection) {
                                    var form = combo.up('childBookForm');
                                    form.getForm().setValues(selection[0].data);
                                },
                                change: function (combo, newValue, oldValue, eOpts) {
                                    if (newValue && newValue.length > 0) {
                                        combo.store.filterVal = newValue;
                                    }
                                }
                            }
                        },
                        {
                            xtype: 'hidden',
                            name: 'isChild',
                            value: true
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
                            fieldLabel: l('patronymic'),
                            name: "patronymic"
                        },
                        {
                            fieldLabel: l('dateOfBirth'),
                            name: "dob",
                            xtype: 'pmsdatefield',
                            format: 'd/m/y',
                            submitFormat: 'U'
                        }
                    ]
                },
                {
                    xtype: 'fieldset',
                    margin: '0 5px 0 6px',
                    title: l('documents'),
                    items: [
                        {
                            fieldLabel: l('birthCertificateNumber'),
                            xtype: 'textfield',
                            name: "birthCertificate"
                        },
                        {
                            fieldLabel: l('scannedDocuments'),
                            xtype: 'filefield',
                            name: "file"
                        }
                    ]
                }
            ]
        }
    ]
});