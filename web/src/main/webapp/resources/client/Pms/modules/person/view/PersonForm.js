Ext.define('Pms.modules.person.view.PersonForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.personForm',
    requires: ['Pms.abstract.field.lookup.Picker'],
    layout: 'hbox',
    autoScroll: false,
    data: null,
    listeners: {
        afterRender: function (thisForm, options) {
            this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
                enter: function () {
                    var button = this.down('button[action=updateInfo]') ? this.down('button[action=updateInfo]') : this.down('button[action=update]') ? this.down('button[action=update]') : this.down('button[action=save]');
                    if (!thisForm.down('combobox[isExpanded=true]'))
                        button.fireHandler()
                },
                scope: this
            });
        }
    },

    initComponent: function () {
        var me = this;
        me.items = [
            {
                xtype: 'container',
                margin: 5,
                items: [
                    {
                        xtype: 'fieldset',
                        title: l('common'),
                        defaultType: 'textfield',

                        items: [
                            {
                                xtype: 'hidden',
                                name: 'id'
                            },
                            {
                                fieldLabel: l('person.firstName') + Pms.requiredStatus,
                                name: 'firstName',
                                allowBlank: false
                            },
                            {
                                fieldLabel: l('person.lastName') + Pms.requiredStatus,
                                name: 'lastName',
                                allowBlank: false
                            },
                            {
                                fieldLabel: l('person.phone') + Pms.requiredStatus,
                                name: 'phone',
                                allowBlank: false
                            },
                            {
                                fieldLabel: l('patronymic'),
                                name: 'patronymic'
                            },
//                        {
//                            fieldLabel: 'Гражданство',
//                            name: 'nationality',
//                            xtype: 'combobox',
////                            store: Ext.create('Pms.modules.person.store.Country'),
//                            displayField: 'label',
//                            valueField: 'name',
//                            queryMode: 'remote'
//                        },
                            {
                                fieldLabel: l('language'),
                                name: 'language',
                                xtype: 'combobox',
                                store: Ext.create('Pms.modules.person.store.Language'),
                                displayField: 'language',
                                valueField: 'code',
                                queryMode: 'local'
                            },
                            {
                                fieldLabel: l('dateOfBirth'),
                                xtype: 'pmsdatefield',
                                name: 'dob',
                                format: 'd/m/Y',
                                submitFormat: 'U'
                            },
                            {
                                fieldLabel: l('discount'),
                                xtype: 'hidden',
                                name: 'discount'
//                                minValue: 0,
//                                maxValue: 100
                            }
//                        {
//                            fieldLabel: 'VIP тип',
//                            name: 'property_vip_type',
//                            xtype: 'combobox',
////                            store: Ext.create('Pms.modules.person.store.Vip_type').load(),
//                            displayField: 'label',
//                            valueField: 'name',
//                            queryMode: 'remote'
//                        },
//                        {
//                            fieldLabel: 'Причина',
//                            name: 'property_vip_reason'
//                        }
                        ]
                    },
                    {
                        xtype: 'fieldset',
                        title: l('contacts'),
                        defaultType: 'textfield',

                        items: [
                            {
                                fieldLabel: l('person.country'),
                                name: 'country',
                                xtype: 'combobox',
                                store: Ext.create('Pms.modules.person.store.Country'),
                                valueField: 'abbr',
                                displayField: 'country',
                                queryMode: 'local'
                            },
                            {
                                fieldLabel: l('person.city'),
                                name: 'city'
                            },
                            {
                                fieldLabel: l('person.address'),
                                name: 'address'
                            },
                            {
                                fieldLabel: l('email'),
                                name: 'email',
                                vtype: 'email'
                            }
                        ]
                    }
                ]
            },
            {
                xtype: 'container',
                margin: 5,
                items: [
                    {
                        xtype: 'fieldset',
                        title: l('documents'),
                        defaultType: 'textfield',
                        itemId: 'personFormDocuments',
                        items: [
                            {
                                fieldLabel: l('person.passportNumber'),
                                name: 'passportNumber'
                            },
                            {
                                fieldLabel: l('person.entryValidTill'),
                                xtype: 'pmsdatefield',
                                name: 'passportValidTill',
                                format: 'd/m/Y',
                                submitFormat: 'U'
                            },
                            {
                                fieldLabel: l('person.passportIssued'),
                                xtype: 'pmsdatefield',
                                name: 'passportIssued',
                                format: 'd/m/Y',
                                submitFormat: 'U'
                            },
                            {
                                fieldLabel: l('person.cio'),
                                name: 'cio'
                            },
                            {
                                fieldLabel: l('person.entryType'),
                                xtype: 'lookupCombobox',
                                name: 'entryType',
                                lookupType: 'entryType',
                                valueNotFoundText: null
                            },
                            {
                                fieldLabel: l('person.entryValidFrom'),
                                xtype: 'pmsdatefield',
                                name: 'entryValidFrom',
                                format: 'd/m/Y',
                                submitFormat: 'U'
                            },
                            {
                                fieldLabel: l('person.entryValidTill'),
                                xtype: 'pmsdatefield',
                                name: 'entryValidTill',
                                format: 'd/m/Y',
                                submitFormat: 'U'
                            },
                            {
                                fieldLabel: l('room'),
                                name: 'entryNumber'
                            },
                            {
                                fieldLabel: l('person.visaType'),
                                name: 'visaType',
                                lookupType: 'visaType',
                                xtype: 'lookupCombobox',
                                valueNotFoundText: null
                            }
                        ]
                    },
                    {
                        xtype: 'fieldset',
                        title: l('facility.Transport'),
                        defaultType: 'textfield',

                        items: [
                            {
                                fieldLabel: l('person.carMark'),
                                name: 'carBrand'
                            },
                            {
                                fieldLabel: l('person.carNumber'),
                                name: 'carNumber'
                            }
                        ]
                    }
                ]
            },
            {
                xtype: 'fieldset',
                width: 530,
                height: 475,
                margin: 5,
                padding: 5,
                title: l('person.passportScan'),
                overflowY: 'auto',
                overflowX: false,
                items: [
                    {
                        xtype: 'form',
                        border: false,
                        trackResetOnLoad: true,
                        url: 'rest/document/upload',
                        items: [
                            {
                                fieldLabel: l('scannedDocuments'),
                                name: 'file',
                                xtype: 'filefield',
                                labelWidth: 120
                            }
                        ]
                    },
                    {
                        xtype: 'button',
                        text: l('tooltip.delete'),
                        iconCls: 'app-icon-remove',
                        requestDisable: true,
                        disabled: !me.data || Ext.isEmpty(me.data.identity),
                        handler: function (btn, eOpts) {
                            if (me.data.identity) {
                                Pms.Ajax.request({
                                    url: 'rest/document/doc/' + me.data.identity,
                                    method: 'DELETE',
                                    success: function (response) {
                                        me.data.identity = null;
                                        btn.up('fieldset').down('image').setSrc(Pms.emptyImgSrc);
                                        Pms.App.showNotification({
                                            message: l('photo.deleteSuccess'),
                                            icon: Pms.notificationOk
                                        });
                                    },
                                    failure: function () {
                                        Pms.App.showNotification({
                                            message: l('photo.deleteError')
                                        });
                                    }
                                });
                            } else {
                                Pms.App.showNotification({
                                    message: l('photo.deleteError')
                                });
                            }
                        }
                    },
                    {
                        xtype: 'container',
                        width: '100%',
                        items: [
                            {
                                xtype: 'image',
                                maxWidth: '500px',
                                maxHeight: '500px',
                                border: 5,
                                style: {
                                    borderColor: '#eee',
                                    borderStyle: 'solid',
                                    margin: '15px 0 0 0'
                                },
                                src: (me.data && !Ext.isEmpty(me.data.identity)) ? _('imagesUrlPrefix') + me.data.identity : Pms.emptyImgSrc
                            }
                        ]
                    }
                ]
            }
        ];
        me.callParent(arguments);
    },

    bbar: ['->', {
        iconCls: 'save-action-icon',
        text: l('save.btn'),
        action: 'save',
        requestDisable: true
    }, {
        iconCls: 'app-icon-remove',
        text: l('cancel.btn'),
        handler: function (btn) {
            btn.up('window').close();
        }
    }]
});