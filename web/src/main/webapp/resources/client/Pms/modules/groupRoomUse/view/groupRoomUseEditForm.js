Ext.define('Pms.modules.groupRoomUse.view.groupRoomUseEditForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.groupRoomUseEditForm',

    data: {},

    initComponent: function () {
        var me = this;

        me.items = this.buildItems();

        me.bbar = this.buildToolbar();

        me.callParent(arguments);
    },

    buildItems: function () {
        var me = this,
            master = '',
            masterGrid = {},
            useTypes = [];
        if (me.data.customerGroup.customer) {
            master = me.data.customerGroup.customer.lastName + ' ' + me.data.customerGroup.customer.firstName;
            masterGrid = {
                xtype: 'propertygrid',
                title: l('masterPerson'),
                hideHeaders: true,
//                editable: false,
                source: {
                    lastName: me.data.customerGroup.customer.lastName,
                    firstName: me.data.customerGroup.customer.firstName,
                    email: me.data.customerGroup.customer.email,
                    phone: me.data.customerGroup.customer.phone,
                    pov: l('pov.' + me.data.customerGroup.pov)

                },
                sourceConfig: {
                    lastName: {displayName: l('lastName')},
                    firstName: {displayName: l('firstName')},
                    email: {displayName: l('email')},
                    phone: {displayName: l('phone')},
                    pov: {displayName: l('purposeOfVisit')}
                },
                listeners: {
                    validateedit: function () {
                        return false
                    }
                }
            };
        }
        else {
            master = me.data.customerGroup.company.name;
            masterGrid = {
                xtype: 'propertygrid',
                nameColumnWidth: 140,
                title: l('mainCompany'),
                hideHeaders: true,
//                editable: false,
                source: {
                    name: me.data.customerGroup.company.name,
//                    type: types[me.data.customerGroup.company.name],
                    email: me.data.customerGroup.company.email || l('notSpecified'),
                    phone: me.data.customerGroup.company.phone || l('notSpecified'),

                },
                sourceConfig: {
                    name: {displayName: l('title')},
//                    type: {displayName: l('type')},
                    email: {displayName: l('email')},
                    phone: {displayName: l('phone')},
                },
                listeners: {
                    validateedit: function () {
                        return false
                    }
                }
            };
        }

        return [
            {
                xtype: 'container',
                layout: 'hbox',
                items: [
                    {
                        xtype: 'container',
                        width: '50%',
                        items: [
                            {
                                xtype: 'propertygrid',
                                title: l('settlements'),
                                margin: '0 0 5px 0',
                                hideHeaders: true,
                                editable: false,
                                sortableColumns: false,
                                source: {
                                    room: me.data.room.number + ' (' + me.data.room.roomType.name + ')',
                                    dateStart: Ext.Date.format(me.data.startDate, 'd/m/y'),
                                    dateEnd: Ext.Date.format(me.data.endDate, 'd/m/y'),
                                    earlyCheckIn: Pms.checkForCheckInOut(true, me.data.checkInTime),
                                    lateCheckOut: Pms.checkForCheckInOut(false, me.data.checkOutTime),
                                    nights: (me.data.endDate - me.data.startDate) / 1000 / 60 / 60 / 24,
                                    payer: master,
                                    useType: useTypes[me.data.useType],
                                    total: Ext.util.Format.number(me.data.total / 100, '0.00'),
                                    totalPaid: Ext.util.Format.number(me.data.totalPaid / 100, '0.00'),
                                    fullyPaid: me.data.total - me.data.totalPaid
                                },
                                sourceConfig: {
                                    room: {displayName: l('room')},
                                    dateStart: {displayName: l('checkIn'), format: 'd/m/y'},
                                    dateEnd: {displayName: l('checkOut'), format: 'd/m/y'},
                                    earlyCheckIn: {displayName: l('hotel.info.earlyCheckIn'),
                                        renderer: function (v) {
                                            return v;
                                        }
                                    },
                                    lateCheckOut: {displayName: l('hotel.info.lateCheckOut'),
                                        renderer: function (v) {
                                            return v;
                                        }
                                    },
                                    nights: {displayName: l('roomUse.night')},
                                    payer: {displayName: l('payer')},
                                    useType: {displayName: l('roomUse.usingType')},
                                    total: {displayName: l('total')},
                                    totalPaid: {displayName: l('totalPaid')},
//                                    property_visit_purpose: {displayName: 'Цель приезда'},
//                                    property_checkin_type: {displayName: 'Тип заезда'},
//                                    property_source_type: {displayName: 'Источник'},
                                    fullyPaid: {
                                        displayName: l('fullyPaid'),
                                        renderer: function (v) {
                                            return v ? Pms.iconCross : Pms.iconOk;
                                        }
                                    }
                                },
                                listeners: {
                                    validateedit: function () {
                                        return false
                                    }
                                }
                            },
                            masterGrid,
                            {
                                xtype: 'button',
                                text: l('roomUse.lookCardNumber'),
                                margin: '10 0 0 115',
                                iconCls: 'fa fa-credit-card',
                                hidden: me.data.source == 'FRONT_DESK',
                                handler: function () {
                                    Ext.Msg.prompt(l('enterPassword'), l('roomUse.enterCreditCardPassword'), function (btn, pass) {
                                        var params = {
                                            id: me.data.id,
                                            pwd: pass
                                        };
                                        if (btn == 'ok') {
                                            Pms.Ajax.request({
                                                url: 'wubooking/cc',
                                                method: 'GET',
                                                params: Pms.Ajax.encode(params),
                                                success: function (res) {
                                                    var data = res.content;
                                                    var win = Ext.widget('ccViewWindow', {
                                                        data: data
                                                    });
                                                    win.show();
                                                }
                                            })
                                        }
                                    })
                                }
                            },
                            {
                                xtype: 'fieldset',
                                title: l('editSettlements'),
                                margin: '5px 0 0 0',
                                items: [
                                    {
                                        xtype: 'hidden',
                                        name: 'id'
                                    },
//                                    {
//                                        fieldLabel: l('payer'),
//                                        name: 'customer',
//                                        xtype: 'combobox',
//                                        store: Ext.create('Pms.modules.person.store.Person').load(),
//                                        displayField: 'lastName',
//                                        valueField: 'id',
//                                        queryMode: 'remote'
//                                    },
                                    {
                                        xtype: 'pmsdatefield',
                                        fieldLabel: l('checkOut'),
                                        name: 'endDate',
                                        minValue: me.data.endDate,
                                        format: 'd/m/y',
                                        submitFormat: 'U'
                                    },
//                                    {
//                                        xtype: 'combobox',
//                                        fieldLabel: 'Источник',
//                                        name: 'property_source_type',
////                                        store: Ext.create('Pms.modules.group.store.sourceType').load(),
//                                        displayField: 'label',
//                                        valueField: 'name',
//                                        queryMode: 'remote'
//                                    },
//                                    {
//                                        xtype: 'combobox',
//                                        fieldLabel: 'Цель приезда',
//                                        name: 'property_visit_purpose',
////                                        store: Ext.create('Pms.modules.group.store.visitPurpose').load(),
//                                        displayField: 'label',
//                                        valueField: 'name',
//                                        queryMode: 'remote'
//                                    },
//                                    {
//                                        xtype: 'combobox',
//                                        fieldLabel: 'Тип заезда',
//                                        name: 'property_checkin_type',
////                                        store: Ext.create('Pms.modules.group.store.checkinType').load(),
//                                        displayField: 'label',
//                                        valueField: 'name',
//                                        queryMode: 'remote'
//                                    },
                                    {
                                        xtype: 'textareafield',
                                        fieldLabel: l('description'),
                                        name: 'description',
                                        value: me.data.description,
                                        height: 35,
                                        width: 400
                                    },
                                    {
                                        xtype: 'textareafield',
                                        fieldLabel: l('roomUse.comment'),
                                        name: 'comment',
                                        value: me.data.comment,
                                        height: 35,
                                        width: 400
                                    }
                                ]
                            }
                        ]
                    },
//                    {
//                        xtype: 'panel',
//                        width: '50%',
//                        height: '100%',
//                        title: 'Скан документа главного группы',
//                        margin: '0 0 0 5px',
//                        items: [
//                            {
//                                xtype: 'image',
//                                width: '100%',
//                                border: 5,
//                                style: {
//                                    borderColor: '#eee',
//                                    borderStyle: 'solid',
//                                    margin: '0px 0 0 0'
//                                },
//                                src: (me.data && me.data.customerGroup.customer && me.data.customerGroup.customer.identity) ? _('imagesUrlPrefix') + me.data.customerGroup.customer.identity : Pms.emptyImgSrc
//                            }
//                        ]
//                    }
                    {
                        xtype: 'panel',
                        name: 'scanPanel',
                        width: '50%',
                        height: '100%',
                        margin: '0 0 0 5px',
                        bodyStyle: {
                            padding: '5px',
                        },
                        title: l('masterPersonScan'),
                        overflowY: 'auto',
                        overflowX: false,
                        items: [
                            {
                                xtype: 'form',
                                name: 'scanForm',
                                border: false,
                                trackResetOnLoad: true,
                                url: 'rest/document/upload',
                                items: [
                                    {
                                        xtype: 'hidden',
                                        name: 'identity'
                                    },
                                    {
                                        fieldLabel: l('loadScan'),
                                        name: 'file',
                                        xtype: 'filefield',
                                        labelWidth: 150,
                                        anchor: '100%',
                                        listeners: {
                                            change: function (field, value, eOpts) {
                                                var documentsForm = field.up('form[name=scanForm]'),
                                                    identityField = documentsForm.down('hidden[name=identity]'),
                                                    imageField = field.up('panel[name=scanPanel]').down('image');

                                                documentsForm.getForm().submit({
                                                    scope: this,
                                                    params: {type: 'SCAN'},
                                                    success: function (s, res) {
                                                        var documentFile = res.result.content;
                                                        identityField.setValue(documentFile);
                                                        imageField.setSrc(_('imagesUrlPrefix') + documentFile);
                                                    }
                                                })
                                            }
                                        }
                                    }
                                ]
                            },
                            {
                                xtype: 'button',
                                text: l('tooltip.delete'),
                                iconCls: 'app-icon-remove',
//                                requestDisable: true,
                                disabled: Ext.isEmpty(me.data.customerGroup.customer) || me.data.customerGroup.customer.identity == null ? true : false, // : true,
                                handler: function (btn, eOpts) {
                                    Pms.Ajax.request({
                                        url: 'rest/document/doc/' + me.data.customerGroup.customer.identity,
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
                                }
                            },
                            {
                                xtype: 'container',
                                width: '100%',
                                items: [
                                    {
                                        xtype: 'image',
                                        border: 5,
                                        style: {
                                            maxWidth: '500px',
                                            borderColor: '#eee',
                                            borderStyle: 'solid',
                                            margin: '15px 0 0 0'
                                        },
                                        src: (me.data.customerGroup.customer && me.data.customerGroup.customer.identity) ? _('imagesUrlPrefix') + me.data.customerGroup.customer.identity : Pms.emptyImgSrc
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
        ];
    },

    buildToolbar: function () {
        return ['->', {
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
        }];
    }
});
