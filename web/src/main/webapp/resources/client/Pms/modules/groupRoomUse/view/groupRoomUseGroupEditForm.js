Ext.define('Pms.modules.groupRoomUse.view.groupRoomUseGroupEditForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.groupRoomUseGroupEditForm',

    data: {},

    initComponent: function () {
        var me = this;

        me.items = this.buildItems();

        me.bbar = this.buildToolbar();

        me.callParent();
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
                editable: false,
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
                    beforeedit: function () {
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
                editable: false,
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
                    beforeedit: function () {
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
                                title: l('groupRoomUse.groupInfo'),
                                margin: '0 0 5px 0',
                                hideHeaders: true,
                                editable: false,
                                source: {
//                                    roomCount: me.data.room.number + ' (' + me.data.room.roomType.name + ')',
                                    payer: master,
                                    useType: useTypes[me.data.useType],
                                    total: Ext.util.Format.number(me.data.customerGroup.total / 100, '0.00'),
                                    totalPaid: Ext.util.Format.number(me.data.customerGroup.totalPaid / 100, '0.00'),
                                    zfullyPaid: me.data.customerGroup.total - me.data.customerGroup.totalPaid
                                },
                                sourceConfig: {
//                                    roomCount: {displayName: l('room.count')},
                                    payer: {displayName: l('payer')},
                                    useType: {displayName: l('roomUse.usingType')},
                                    total: {displayName: l('total')},
                                    totalPaid: {displayName: l('totalPaid')},
//                                    property_visit_purpose: {displayName: 'Цель приезда'},
//                                    property_checkin_type: {displayName: 'Тип заезда'},
//                                    property_source_type: {displayName: 'Источник'},
                                    zfullyPaid: {
                                        displayName: l('fullyPaid'),
                                        renderer: function (v) {
                                            return v ? Pms.iconCross : Pms.iconOk;
                                        }
                                    }
                                },
                                listeners: {
                                    beforeedit: function () {
                                        return false
                                    }
                                }
                            },
                            masterGrid,
                            {
                                xtype: 'button',
                                text: l('roomUse.lookCardNumber'),
                                margin: '10 0 0 115',
                                hidden: me.data.source != 'BOOKING',
                                handler: function () {
                                    Ext.Msg.prompt(l('enterPassword'), l('roomUse.enterCreditCardPassword'), function (btn, pass) {
                                        if(btn == 'cancel') return
                                        var params = {
                                            id: me.data.id,
                                            pwd: pass
                                        };
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
                                    })
                                }
                            },
                        ]
                    },
                    {
                        xtype: 'panel',
                        width: '50%',
                        height: '100%',
                        title: l('roomUse.rooms'),
                        margin: '0 0 0 5px',
                        items: [
                            {
                                xtype: 'roomUseRoomGrid',
                                paging: false,
                                loadParams: {
                                    params: {
                                        filter: [
                                            {
                                                field: 'customerGroup.id',
                                                comparison: 'eq',
                                                data: {
                                                    type: 'numeric',
                                                    value: me.data.customerGroup.id
                                                }
                                            },
                                            {
                                                field: 'status',
                                                comparison: 'neq',
                                                data: {
                                                    type: 'ROOM_USE_STATUS',
                                                    value: 'REFUSE'
                                                }
                                            }
                                        ]
                                    }
                                }
                            }
                        ],
                        tbar: [
                            {
                                text: l('addRoom'),
                                iconCls: 'app-icon-add',
                                handler: function (button) {
                                    var bookWin = button.up('window'),
                                        grid = bookWin.down('roomUseRoomGrid'),
                                        existRoomStore = grid.getStore(),
                                        win = Ext.widget('addRoomWindow', {data: me.data, modelData: existRoomStore.data.items});
                                    win.show();
                                }
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
