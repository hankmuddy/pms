Ext.define('Pms.modules.company.view.CompanyEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.companyEditWindow',
    title: l('company.editWinTitle'),
    autoShow: false,
    width: 1020,
    height: 600,
    data: {},

    initComponent: function () {
        var me = this;

        var documentsData = [];
        if (!Ext.isEmpty(me.data.documents)) {
            for (var i in me.data.documents) {
                documentsData.push([me.data.documents[i]]);
            }
        }

        me.items = [
            {
                xtype: 'tabpanel',
                items: [
                    {
                        title: l('company.infoTab'),
                        autoScroll: false,
                        layout: 'fit',
                        items: [
                            {
                                xtype: 'companyForm',
                                data: me.data,
                                documentsData: documentsData
                            }
                        ]
                    },
                    {
                        title: l('contacts'),
                        autoScroll: true,
                        layout: 'fit',
                        items: [
                            {
                                xtype: 'contactGrid',
                                loadParams: {
                                    params: {
                                        filter: [
                                            {
                                                field: 'company.id',
                                                comparison: 'eq',
                                                data: {
                                                    type: 'numeric',
                                                    value: me.data.id
                                                }
                                            }
                                        ]
                                    }
                                },
                                tbar: [
                                    {
                                        xtype: 'toolbar',
                                        defaults: {
                                            scale: 'small',
                                            iconAlign: 'left',
                                            width: '60'
                                        },
                                        items: [
                                            {
                                                text: l('add.btn'),
                                                action: 'new',
                                                iconCls: 'pms-add-icon-16'
                                            },
                                            {
                                                text: l('edit.btn'),
                                                action: 'edit',
                                                iconCls: 'app-icon-edit2'
                                            },
                                            {
                                                text: l('delete.btn'),
                                                action: 'delete',
                                                disabled: false,
                                                iconCls: 'pms-delete-icon-16'
                                            }
                                        ]
                                    }
                                ]
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
                                loadParams: {
                                    params: {
                                        filter: [
                                            {
                                                field: 'customerGroup.company.id',
                                                comparison: 'eq',
                                                data: {
                                                    type: 'numeric', value: me.data.id
                                                }
                                            },
                                            {
                                                field: 'status',
                                                comparison: 'eq',
                                                data: {
                                                    type: 'ROOM_USE_STATUS',
                                                    value: 'BOOKING_FREE'
                                                }
                                            }
                                        ]
                                    }
                                },
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
                                                        var win = Ext.widget('groupRoomUseAddWindow', {
                                                                data: {
                                                                    masterCompany: me.data
                                                                }
                                                            }),
                                                            gruForm = win.down('groupRoomUseForm'),
//                                                            gruData = {status: 'BOOKING_FREE'};
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
                                                field: 'customerGroup.company.id',
                                                comparison: 'eq', data: {
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
                                actionItems: [/*0*/],
                                loadParams: {
                                    params: {
                                        filter: [
                                            {
                                                field: 'customerGroup.company.id',
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
                                                field: 'roomUse.customerGroup.company.id',
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
                                                field: 'bill.roomUse.customerGroup.company.id',
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