Ext.define('Pms.modules.groupRoomUse.view.bookDataGrid', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.bookDataGrid',

    data: {},

    initComponent: function () {
        var me = this;

        me.items = this.buildItems();

        me.callParent();
    },

    buildItems: function () {
        var me = this,
            master = '',
            masterGrid = {},
            types = [],
            countries = [],
            useTypes = [],
            sourceTypes = [],
            visitPurposes = [],
            checkinTypes = [];

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
                    phone: me.data.customerGroup.customer.phone

                },
                sourceConfig: {
                    lastName: {displayName: l('lastName')},
                    firstName: {displayName: l('firstName')},
                    email: {displayName: l('email')},
                    phone: {displayName: l('phone')}
                },
                listeners: {
                    beforeedit: function () {
                        return false
                    }
                }
            };
        }
        else {
            master = me.data.customerGroup.company;
            masterGrid = {
                xtype: 'propertygrid',
                title: l('mainCompany'),
                hideHeaders: true,
                editable: false,
                source: {
                    name: me.data.name,
                    email: me.data.email,
                    phone: me.data.phone,

                },
                sourceConfig: {
                    name: {displayName: l('title')},
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
                width: '100%',
                items: [
                    {
                        xtype: 'propertygrid',
                        title: l('settlements'),
                        margin: '0 0 5px 0',
                        hideHeaders: true,
                        editable: false,
                        sortableColumns: false,
                        source: {
                            dateStart: Ext.Date.format(new Date(me.data.startDate * 1000), 'd/m/y'),
                            dateEnd: Ext.Date.format(new Date(me.data.endDate * 1000), 'd/m/y'),
                            room: me.data.room.number + ' (' + me.data.room.roomType.name + ')',
                            customer: master,
                            useType: useTypes[me.data.useType],
//                            property_visit_purpose: visitPurposes[me.data.group_visit_purpose],
//                            property_checkin_type: checkinTypes[me.data.group_checkin_type],
//                            property_source_type: sourceTypes[me.data.group_source_type],
                            fullyPaid: me.data.fullyPaid
                        },
                        sourceConfig: {
                            dateStart: {displayName: l('checkIn')/*, format: 'd/m/y'*/},
                            dateEnd: {displayName: l('checkOut')/*, format: 'd/m/y'*/},
                            room: {displayName: l('room')},
                            customer: {displayName: l('payer')},
                            useType: {displayName: l('roomUse.usingType')},
//                            property_visit_purpose: {displayName: 'Цель приезда'},
//                            property_checkin_type: {displayName: 'Тип заезда'},
//                            property_source_type: {displayName: 'Источник'},
                            fullyPaid: {
                                displayName: l('fullyPaid'),
                                renderer: function (v) {
                                    return v ? '<img src="themes/default/images/icons/gray/icons/ok.png"/>' : '<img src="themes/default/images/icons/gray/icons/remove.png"/>';
                                }
                            },
                        },
                        listeners: {
                            beforeedit: function () {
                                return false
                            }
                        }
                    },
                    // masterGrid
                ]
            }
        ];
    }
});
