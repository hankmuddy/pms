Ext.define('Pms.modules.groupRoomUse.view.RefuseInfoWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.refuseInfoWindow',

    title: l('refuse'),
    width: 500,
    height: 500,
    modal: false,

    data: {},

    initComponent: function () {
        var me = this;

//        if (typeof this.data.customerGroup != 'undefined') {
//            this.height = this.data.customerGroup.roomUsesQuantity == 1 ? 125 : 165;
//        } else {
//            this.height = 165;
//        }
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
                    phone: {displayName: l('phone')}
                },
                listeners: {
                    validateedit: function () {
                        return false
                    }
                }
            };
        }

        me.items = [
            {
                xtype: 'panel',
                overflowY: true,
                overflowX: false,
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
                            fullyPaid: me.data.total - me.data.totalPaid,
                            description: me.data.description
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
                            description: {displayName: l('description')},
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
                    masterGrid
                ]
            }
        ];

        me.buttons = [
//            {
//                text: l('save.btn'),
//                action: 'save',
//                requestDisable: true
//            },
            {
                text: l('cancel.btn'),
                scope: me,
                handler: me.close
            }
        ];

        me.callParent(arguments);
    }
});