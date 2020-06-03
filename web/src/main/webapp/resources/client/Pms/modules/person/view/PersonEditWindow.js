Ext.define('Pms.modules.person.view.PersonEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.personEditWindow',
    title: l('person.editPerson'),
    resizable: false,
    autoShow: false,
    width: 1230,
    height: 580,
    data: {},

    initComponent: function () {
        var me = this;

        me.items = [
            {
                xtype: 'tabpanel',
                items: [
                    {
                        title: l('person.personInformation'),
                        iconCls: 'fa fa-user',
                        autoScroll: false,
                        items: [
                            {
                                xtype: 'personForm',
                                height: 520,
                                data: me.data
                            }
                        ]
                    },
                    {
                        title: l('company.bookingTab'),
                        iconCls: 'fa fa-briefcase',
                        autoScroll: true,
                        layout: 'fit',
                        items: [
                            {
                                xtype: 'consoleGrid',
                                gridName: 'booking',
                                actionItems: [0, 1, 2, 4],
                                loadParams: {params: {filter: [
                                    {field: 'customerGroup.customer.id', comparison: 'eq', data: {type: 'numeric', value: me.data.id}},
                                    {field: 'status', comparison: 'eq', data: {type: 'ROOM_USE_STATUS', value: 'BOOKING_FREE'}}
                                ]}},
                                withToolbar: true,
                                buildToolbar: function () {
                                    return [
                                        {
                                            xtype: 'toolbar',
                                            defaults: {
                                                scale: 'small',
                                                iconAlign: 'left'
                                            },
                                            items: [
                                                {
                                                    text: l('company.createBook'),
                                                    action: 'book',
                                                    iconCls: 'pms-add-icon-16',
                                                    handler: function (btn, e) {
                                                        var win = Ext.widget('groupRoomUseAddWindow', {data: {masterPerson: me.data}}),
                                                            gruForm = win.down('groupRoomUseForm'),
                                                            gruData = {status: 'BOOKING_FREE', startDate: new Date()};
                                                        gruForm.getForm().setValues(gruData);
                                                        win.show();
                                                    }
                                                }
                                            ]
                                        }
                                    ];
                                }
                            }
                        ]
                    },
                    {
                        title: l('company.livingTab'),
                        autoScroll: true,
                        layout: 'fit',
                        items: [
                            {
                                xtype: 'consoleGrid',
                                gridName: 'living',
                                actionItems: [0, 1, 4],
                                loadParams: {
                                    params: {
                                        filter: [
                                            {
                                                field: 'customerGroup.customer.id',
                                                comparison: 'eq',
                                                data: {
                                                    type: 'numeric',
                                                    value: me.data.id
                                                }
                                            },
                                            {
                                                field: 'status',
                                                comparison: 'eq',
                                                data: {
                                                    type: 'ROOM_USE_STATUS',
                                                    value: 'LIVING'
                                                }
                                            }
                                        ]
                                    }
                                }
                            }
                        ]
                    },
                    {
                        title: l('company.refuseTab'),
                        autoScroll: true,
                        layout: 'fit',
                        items: [
                            {
                                xtype: 'consoleGrid',
                                gridName: 'refuse',
                                actionItems: [/*0, 3, 4*/],
                                loadParams: {
                                    params: {
                                        filter: [
                                            {
                                                field: 'customerGroup.customer.id',
                                                comparison: 'eq',
                                                data: {
                                                    type: 'numeric',
                                                    value: me.data.id
                                                }
                                            },
                                            {
                                                field: 'status',
                                                comparison: 'eq',
                                                data: {
                                                    type: 'ROOM_USE_STATUS',
                                                    value: 'REFUSE'
                                                }
                                            }
                                        ]
                                    }
                                }
                            }
                        ]
                    },
                    {
                        title: l('company.billsTab'),
                        iconCls: 'fa fa-file-text',
                        autoScroll: true,
                        layout: 'fit',
                        hidden: Pms.App.isExtranet(),
                        items: [
                            {
                                xtype: 'billGrid',
                                loadParams: {
                                    params: {
                                        filter: [
                                            {
                                                field: 'roomUse.customerGroup.customer.id',
                                                comparison: 'eq',
                                                data: {
                                                    type: 'numeric',
                                                    value: me.data.id
                                                }
                                            }
                                        ]
                                    }
                                }
                            }
                        ]
                    },
                    {
                        title: l('company.paymentsTab'),
                        autoScroll: true,
                        layout: 'fit',
                        hidden: Pms.App.isExtranet(),
                        items: [
                            {
                                xtype: 'paymentGrid',
                                loadParams: {
                                    params: {
                                        filter: [
                                            {
                                                field: 'bill.roomUse.customerGroup.customer.id',
                                                comparison: 'eq',
                                                data: {
                                                    type: 'numeric',
                                                    value: me.data.id
                                                }
                                            }
                                        ]
                                    }
                                }
                            }
                        ]
                    }
                ]
            }
        ];

        me.callParent(arguments);
    }
});