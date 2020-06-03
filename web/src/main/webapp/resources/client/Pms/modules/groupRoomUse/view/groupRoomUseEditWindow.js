Ext.define('Pms.modules.groupRoomUse.view.groupRoomUseEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.groupRoomUseEditWindow',
    requires: [
        'Pms.modules.bill.store.Bill'
    ],
    title: l('roomUse.bookingCard'),
    width: 1100,
    height: 615,
    overflowY: true,
    overflowX: false,
    addGroupFilter: function (params, bill) {
        params.connective = 'or';
        params.filter.push({
            field: bill ? 'bill.group.id' : 'group.id',
            comparison: 'eq',
            data: {
                type: 'numeric',
                value: this.data.customerGroup.id
            }
        });
    },

    singleRoomUseFilters: function () {
        var me = this;
        var isCustomerRoom = null;
        var filters = {
            groupPersonGrid: {
                filter: [
                    {
                        field: 'groupMemberToRoomUses.roomUse.id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: this.data.id
                        }
                    },
                    {
                        field: 'person.type',
                        comparison: 'eq',
                        data: {
                            type: 'string',
                            value: 'adult'
                        }
                    }
                ]
            },
            groupChildGrid: {
                filter: [
                    {
                        field: 'groupMemberToRoomUses.roomUse.id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: this.data.id
                        }
                    },
                    {
                        field: 'person.type',
                        comparison: 'eq',
                        data: {
                            type: 'string',
                            value: 'child'
                        }
                    }
                ]
            },
            serviceUseGroupGrid: {
                filter: [
                    {
                        field: 'bill.roomUse.id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: this.data.id
                        }
                    },
//                    {
//                        field: 'refund',
//                        comparison: 'is_null',
//                        data: {
//                            type: 'boolean',
//                            value: false
//                        }
//                    }
                ]
            },
            billGrid: {
                filter: [
                    {
                        field: 'roomUse.id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: this.data.id
                        }
                    }
                ]
            },
            refundGrid: {
                filter: [
                    {
                        field: 'roomUse.id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: this.data.id
                        }
                    }
                ]
            },
            paymentGrid: {
                filter: [
                    {
                        field: 'bill.roomUse.id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: this.data.id
                        }
                    }
                ]
            }
        };
        Pms.Ajax.request({
            method: 'GET',
            async: false,
            url: 'rest/roomUse/' + me.data.id + '/containsCustomer',
            success: function (res) {
                isCustomerRoom = res.content
            }
        });
        if (isCustomerRoom) {
            me.addGroupFilter(filters.billGrid);
            me.addGroupFilter(filters.paymentGrid, true);
            me.addGroupFilter(filters.serviceUseGroupGrid, true);
            me.addGroupFilter(filters.refundGrid);
        }
        return filters
    },
    groupRoomUseFilters: function () {
        return {
            groupPersonGrid: {
                filter: [
                    {
                        field: 'customerGroup.id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: this.data.customerGroup.id
                        }
                    },
                    {
                        field: 'person.type',
                        comparison: 'eq',
                        data: {
                            type: 'string',
                            value: 'adult'
                        }
                    }
                ]
            },
            groupChildGrid: {
                filter: [
                    {
                        field: 'customerGroup.id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: this.data.customerGroup.id
                        }
                    },
                    {
                        field: 'person.type',
                        comparison: 'eq',
                        data: {
                            type: 'string',
                            value: 'child'
                        }
                    }
                ]
            },
            serviceUseGroupGrid: {
                connective: 'or',
                filter: [
                    {
                        field: 'bill.roomUse.customerGroup.id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: this.data.customerGroup.id
                        }
                    },
                    {
                        field: 'bill.group.id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: this.data.customerGroup.id
                        }
                    },
                ]
            },
            billGrid: {
                connective: 'or',
                filter: [
                    {
                        field: 'roomUse.customerGroup.id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: this.data.customerGroup.id
                        }
                    },
                    {
                        field: 'group.id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: this.data.customerGroup.id
                        }
                    },
                ]
            },
            refundGrid: {
                connective: 'or',
                filter: [
                    {
                        field: 'roomUse.customerGroup.id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: this.data.customerGroup.id
                        }
                    },
                    {
                        field: 'group.id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: this.data.customerGroup.id
                        }
                    },
                ]
            },
            paymentGrid: {
                connective: 'or',
                filter: [
                    {
                        field: 'bill.roomUse.customerGroup.id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: this.data.customerGroup.id
                        }
                    },
                    {
                        field: 'bill.group.id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: this.data.customerGroup.id
                        }
                    },
                ]
            }
        }
    },
    data: null,

    initComponent: function () {
        var me = this;
        me.singleFil = me.singleRoomUseFilters();
        me.groupFil = me.groupRoomUseFilters();
        me.items = [
            {
                xtype: 'tabpanel',
                items: [
                    {
                        title: l('roomUse.arrivalData'),
                        layout: 'fit',
                        overflowY: true,
                        overflowX: false,
                        items: [
                            {
                                xtype: 'groupRoomUseEditForm',
                                data: me.data
                            },
                            {
                                xtype: 'groupRoomUseGroupEditForm',
                                data: me.data,
                                hidden: true,
                                layout: 'fit',
                            }
                        ]
                    },
                    {
                        title: l('roomUse.guests'),
                        autoScroll: false,
                        // layout: 'fit',
                        items: [
                            {
                                xtype: 'groupPersonGrid',
                                data: me.data,
                                overflowY: true,
                                overflowX: false,
                                itemId: 'groupPersonGridBooking',
                                loadParams: {
                                    params: me.singleFil.groupPersonGrid
                                },
                                height: 300,
                                masterPerson: me.data.customerGroup.customer ? me.data.customerGroup.customer.id : null
//                                store: 'Pms.modules.person.store.GroupMember'
                            },
                            {
                                title: l('children'),
                                xtype: 'groupChildGrid',
                                itemId: 'childGridBooking',
                                overflowY: true,
                                overflowX: false,
                                loadParams: {
                                    params: me.singleFil.groupChildGrid
                                },
                                height: 190,
                                margin: '5px 0 0 0',
                                store: 'Pms.modules.person.store.GroupMember',
                                tbar: [
                                    {
                                        text: l('edit.btn'),
                                        action: 'edit-child',
                                        iconCls: 'app-icon-edit2',
                                        gridAction: true,
                                    }
                                ]
                            },

                        ],
                        tbar: [
                            {
                                text: l('addGuests'),
                                iconCls: 'app-icon-income',
                                handler: function (grid, rowIndex, colIndex) {
                                    var win = Ext.widget('addGuestWindow', {
                                        data: me.data,
                                        toGroup: !me.down('button[cls=group-room-use-change-btn]').filterToGroup
                                    });
                                    win.show();
                                    win.on('close', function () {
                                        Ext.ComponentQuery.query('#childGridBooking')[0].getStore().load();
                                        Ext.ComponentQuery.query('#groupPersonGridBooking')[0].getStore().load();
                                    });
                                }
                            },
                            {
                                text: l('edit.btn'),
                                action: 'edit-person',
                                iconCls: 'app-icon-edit2',
                                gridAction: true,
                            }
                        ]
                    },
                    {
                        title: l('roomUse.services'),
                        autoScroll: false,
                        layout: 'fit',
                        items: [
                            {
                                xtype: 'container',
                                layout: 'hbox',
                                items: [
                                    {
                                        xtype: 'catalogOrderGrid',
                                        title: l('roomUse.catalog'),
                                        width: '40%',
                                        margin: '0 5px 0 0',
                                        border: true,
                                        height: '100%',
                                        overflowY: true,
                                        overflowX: false,
                                        data: me.data
                                    },
                                    {
                                        xtype: 'serviceUseGroupGrid',
                                        title: l('desktop.serviceUse'),
                                        width: '60%',
                                        border: true,
                                        height: '100%',
                                        overflowY: true,
                                        overflowX: false,
//                                        autoLoad: false,
                                        loadParams: {
                                            params: me.singleFil.serviceUseGroupGrid,
                                        },
                                        data: me.data
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        title: l('roomUse.bills'),
                        overflowY: true,
                        overflowX: false,
                        layout: 'fit',
                        listeners: {
                            activate: function (tab) {
                                tab.down('billGrid').getStore().reload();
                            }
                        },
                        items: [
                            {
                                xtype: 'billGrid',
                                loadParams: {
                                    params: me.singleFil.billGrid
                                }
                            }
                        ]
                    },
                    {
                        title: l('roomUse.payments'),
                        overflowY: true,
                        overflowX: false,
                        listeners: {
                            activate: function (tab) {
                                tab.down('paymentGrid').getStore().reload();
                            }
                        },
                        layout: 'fit',
                        items: [
                            {
                                xtype: 'paymentGrid',
                                loadParams: {
                                    params: me.singleFil.paymentGrid
                                }
                            }
                        ]
                    },
                    {
                        title: l('roomUse.refunds'),
                        overflowY: true,
                        overflowX: false,
                        layout: 'fit',
                        listeners: {
                            activate: function (tab) {
                                tab.down('refundGrid').getStore().reload();
                            }
                        },
                        items: [
                            {
                                xtype: 'refundGrid',
                                loadParams: {
                                    params: me.singleFil.refundGrid
                                }
                            }
                        ]
                    },
                    {
                        tabConfig: {
                            xtype: 'tbfill'
                        }
                    },
                    {
                        tabConfig: {
                            xtype: 'button',
                            text: l('groupRoomUse.goToGroupBooking'),
                            cls: 'group-room-use-change-btn',
                            filterToGroup: true,
                            handler: function (button) {
                                var store, type,
                                    filters = button.filterToGroup ? me.groupRoomUseFilters() : me.singleRoomUseFilters();
                                for (type in filters) {
                                    store = me.down(type).getStore();
                                    store.loadParams = {params: filters[type]};
                                    store.load();
                                }
                                button.filterToGroup = !button.filterToGroup;
                                button.filterToGroup ? button.setText(l('groupRoomUse.goToGroupBooking')) : button.setText(l('groupRoomUse.goToSingleBooking'));

                                if (!button.filterToGroup) {

                                    me.down('groupRoomUseEditForm').hide();
                                    me.down('groupRoomUseGroupEditForm').show();
                                }
                                else {
                                    me.down('groupRoomUseGroupEditForm').hide();
                                    me.down('groupRoomUseEditForm').show();
                                }
                            }
                        }
                    }
                ]
            }
        ];
        this.callParent(arguments);
    }
});