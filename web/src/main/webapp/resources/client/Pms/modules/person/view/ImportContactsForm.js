Ext.define('Pms.modules.person.view.ImportContactsForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.importContactsForm',
    requires: ['Pms.abstract.field.ExcelCombo'],
    layout: 'vbox',

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
                defaults: {
                    xtype: 'excelcombo',
                    anchor: '100%',
                    labelWidth: 180
                },
                items: [
                    {
                        xtype: 'text',
                        text: l('person.cellsNumberAccordance')
                    },
                    {
                        fieldLabel: l('person.firstName') + Pms.requiredStatus,
                        name: "firstName",
                        allowBlank: false
                    },
                    {
                        fieldLabel: l('person.lastName') + Pms.requiredStatus,
                        name: "lastName",
                        allowBlank: false
                    },
                    {
                        fieldLabel: l('patronymic'),
                        name: "patronymic"
                    },
                    {
                        fieldLabel: l('dateOfBirth'),
                        name: "dob",
                    },
                    {
                        fieldLabel: l('phone'),
                        name: "phone",
                    },
                    {
                        fieldLabel: l('email'),
                        name: "email",
                    },
                    {
                        fieldLabel: l('address'),
                        name: "address"
                    },
                    {
                        fieldLabel: l('person.passportNumber'),
                        name: "position"
                    },
                    {
                        fieldLabel: l('person.passportValidTill'),
                        name: 'entryValidTill',
                    },
                    {
                        fieldLabel: l('person.passportIssued'),
                        name: 'passportIssued',
                    },
                    {
                        fieldLabel: l('person.cio'),
                        name: 'cio'
                    },
                    {
                        xtype: 'form',
                        border: false,
                        trackResetOnLoad: true,
                        url: 'import',
                        items: [
                            {
                                fieldLabel: l('import.excelFile'),
                                name: 'file',
                                xtype: 'filefield',
                                labelWidth: 100,
                                anchor: '100%',
                                listeners: {
                                    afterrender: function (cmp) {
                                        cmp.fileInputEl.set({
                                            accept: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
                                        });
                                    }
                                }
                            }
                        ]
                    }
                ]
            },

        ];
    }
});