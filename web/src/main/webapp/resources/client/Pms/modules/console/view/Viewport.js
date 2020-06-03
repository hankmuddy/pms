Ext.define('Pms.modules.console.view.Viewport', {
    extend: 'Pms.abstract.Viewport',
    alias: 'widget.consoleViewport',

    autoScroll: false,

    store: Ext.create('Pms.modules.roomUse.store.roomUse').load(),

    getStore: function(filters) {
        var me = this;
        me.store.filterBy = [];

//        if(filters.length) {
//            for(var i in filters) {
//                me.store.filters.push(filters[i]);
//            }
//        }

        return me.store.load();
    },

    initComponent: function () {
        var me = this;

        me.tbar = this.buildTopButtons();

        me.items = this.buildItems();

        me.callParent();
    },

    buildItems: function () {
        var me = this;

        return [
            {
                xtype: 'tabpanel',
                region: 'center',
                listeners: {
                    tabchange: function (tabPanel, newCard) {
                        if (newCard.itemId == 'consoleGroupGridPanel')
                            return;

                        var grid = newCard.down('grid');
                        grid.getStore().reload();
                    }
                },
                items: [
                    {
                        title: l('reservation'),
                        layout: 'border',
                        itemId: 'consoleGroupGridPanel',
                        items: [
                            {
                                xtype: 'container',
                                width: '50%',
                                region: 'center',
                                layout: 'border',
                                split: true,
                                items: [
                                    {
                                        title: l('living'),
                                        xtype: 'consoleGrid',
                                        gridName: 'living',
                                        height: '50%',
                                        region: 'center',
                                        paging: true,
                                        border: true,
                                        split: true,
                                        overflowY: true,
//                                        loadParams: {
//                                            params: {
//                                                filter: [
//                                                    {
//                                                        field: 'status',
//                                                        comparison: 'eq',
//                                                        data: {
//                                                            type: 'room_use_status',
//                                                            value: 'LIVING'
//                                                        }
//                                                    }
//                                                ]
//                                            }
//                                        },
                                        actionItems: [0, 1, 2, 4],
                                        gridStoreId: 'console',
                                        storeParams: {
                                            pageSize: 100,
                                            model: 'Pms.modules.roomUse.model.roomUse'
                                        },
                                        rowFilter: function (rec) {
                                            return rec.data.status == 'LIVING';
                                        },
                                        paging: false,
                                        refreshBtn: true
                                    },
                                    {
                                        title: l('reservations'),
                                        xtype: 'consoleGrid',
                                        gridName: 'bookings',
                                        height: '50%',
                                        region: 'south',
                                        paging: true,
                                        border: true,
                                        split: true,
                                        overflowY: true,
//                                        loadParams: {
//                                            params: {
//                                                connective: 'or',
//                                                filter: [
//                                                    {
//                                                        field: 'status',
//                                                        comparison: 'eq',
//                                                        data: {
//                                                            type: 'room_use_status',
//                                                            value: 'BOOKING_FREE'
//                                                        }
//                                                    },
//                                                    {
//                                                        field: 'status',
//                                                        comparison: 'eq',
//                                                        data: {
//                                                            type: 'room_use_status',
//                                                            value: 'BOOKING_WARRANTY'
//                                                        }
//                                                    }
//                                                ]
//                                            }
//                                        },
                                        actionItems: [0, 1, 2, 4],
                                        gridStoreId: 'console',
                                        storeParams: {
                                            pageSize: 100,
                                            model: 'Pms.modules.roomUse.model.roomUse'
                                        },
                                        rowFilter: function (rec) {
                                            return rec.data.status == 'BOOKING_FREE' || rec.data.status == 'BOOKING_WARRANTY';
                                        },
                                        paging: false,
                                        refreshBtn: true
                                    }
                                ]
                            },
                            {
                                xtype: 'container',
                                width: '50%',
                                region: 'east',
                                layout: 'border',
                                split: true,
                                items: [
                                    {
                                        xtype: 'consoleGrid',
                                        title: l('arrival'),
                                        gridName: 'income',
                                        height: '34%',
                                        region: 'north',
                                        paging: true,
                                        border: true,
                                        split: true,
                                        overflowY: true,
//                                        loadParams: {
//                                            params: {
//                                                filter: [
//                                                    {
//                                                        field: 'startDate',
//                                                        comparison: 'lte',
//                                                        data: {
//                                                            type: 'date',
//                                                            value: Ext.Date.format(new Date(), 'U')
//                                                        }
//                                                    },
//                                                    {
//                                                        field: 'status',
//                                                        comparison: 'neq',
//                                                        data: {
//                                                            type: 'room_use_status',
//                                                            value: 'LIVING'
//                                                        }
//                                                    },
//                                                    {
//                                                        field: 'status',
//                                                        comparison: 'neq',
//                                                        data: {
//                                                            type: 'room_use_status',
//                                                            value: 'OUTGO'
//                                                        }
//                                                    },
//                                                    {
//                                                        field: 'status',
//                                                        comparison: 'neq',
//                                                        data: {
//                                                            type: 'room_use_status',
//                                                            value: 'REFUSE'
//                                                        }
//                                                    }
//                                                ]
//                                            }
//                                        },
                                        actionItems: [0, 1, 2, 4],
                                        gridStoreId: 'console',
                                        storeParams: {
                                            pageSize: 100,
                                            model: 'Pms.modules.roomUse.model.roomUse'
                                        },
                                        rowFilter: function (rec) {
                                            return rec.data.startDate <= new Date() && rec.data.status != 'LIVING' && rec.data.status != 'OUTGO' && rec.data.status != 'REFUSE';
                                        },
                                        paging: false,
                                        refreshBtn: true
                                    },
                                    {
                                        title: l('departure'),
                                        xtype: 'consoleGrid',
                                        gridName: 'outgo',
                                        height: '33%',
                                        region: 'center',
                                        paging: true,
                                        border: true,
                                        split: true,
                                        overflowY: true,
//                                        loadParams: {
//                                            params: {
//                                                filter: [
//                                                    {
//                                                        field: 'endDate',
//                                                        comparison: 'eq',
//                                                        data: {
//                                                            type: 'date',
//                                                            value: Ext.Date.format(new Date(), 'U')
//                                                        }
//                                                    },
//                                                    {
//                                                        field: 'status',
//                                                        comparison: 'eq',
//                                                        data: {
//                                                            type: 'room_use_status',
//                                                            value: 'LIVING'
//                                                        }
//                                                    }
//                                                ]
//                                            }
//                                        },
                                        actionItems: [0, 3],
                                        gridStoreId: 'console',
                                        storeParams: {
                                            pageSize: 100,
                                            model: 'Pms.modules.roomUse.model.roomUse'
                                        },
                                        rowFilter: function (rec) {
                                            return Ext.Date.clearTime(rec.data.endDate) == Ext.Date.clearTime(new Date()) && rec.data.status == 'LIVING';
                                        },
                                        paging: false,
                                        refreshBtn: true
                                    },
                                    {
                                        title: l('missedBooking'),
                                        xtype: 'consoleGrid',
                                        gridName: 'skipped',
                                        height: '33%',
                                        region: 'south',
                                        paging: true,
                                        border: true,
                                        split: true,
                                        overflowY: true,
//                                        loadParams: {
//                                            params: {
//                                                filter: [
//                                                    {
//                                                        field: 'startDate',
//                                                        comparison: 'lt',
//                                                        data: {
//                                                            type: 'date',
//                                                            value: Ext.Date.format(Ext.Date.clearTime(Ext.Date.add(new Date(), Ext.Date.DAY, -1)), 'U')
//                                                        }
//                                                    },
//                                                    {
//                                                        field: 'status',
//                                                        comparison: 'in',
//                                                        data: {
//                                                            type: 'room_use_status',
//                                                            value: 'BOOKING_FREE'
//                                                        }
//                                                    },
//                                                    {
//                                                        field: 'status',
//                                                        comparison: 'in',
//                                                        data: {
//                                                            type: 'room_use_status',
//                                                            value: 'BOOKING_WARRANTY'
//                                                        }
//                                                    },
//                                                    {
//                                                        field: 'status',
//                                                        comparison: 'in',
//                                                        data: {
//                                                            type: 'room_use_status',
//                                                            value: 'LIVING'
//                                                        }
//                                                    }
//                                                ]
//                                            }
//                                        },
                                        actionItems: [0, 1, 2, 4],
                                        gridStoreId: 'console',
                                        storeParams: {
                                            pageSize: 100,
                                            model: 'Pms.modules.roomUse.model.roomUse'
                                        },
                                        rowFilter: function (rec) {
                                            return rec.data.startDate < Ext.Date.clearTime(Ext.Date.add(new Date(), Ext.Date.DAY, -1)) && (rec.data.status == 'BOOKING_FREE' || rec.data.status == 'BOOKING_WARRANTY');
                                        },
                                        paging: false,
                                        refreshBtn: true
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        title: l('living'),
                        layout: 'border',
                        items: [
                            {
                                xtype: 'container',
                                region: 'center',
                                layout: 'fit',
                                items: [
                                    {
                                        xtype: 'bookingGrid',
                                        itemId: 'living',
                                        loadParams: {
                                            params: {
                                                filter: [
                                                    {
                                                        field: 'status',
                                                        comparison: 'eq',
                                                        data: {
                                                            type: 'room_use_status',
                                                            value: 'LIVING'
                                                        }
                                                    }
                                                ]
                                            }
                                        }
//                                        gridStoreId: 'console',
//                                        storeParams: {
//                                            model: 'Pms.modules.roomUse.model.roomUse'
//                                        },
//                                        rowFilter: function (rec) {
//                                            return rec.data.status == 'LIVING';
//                                        },
//                                        autoLoading: false // no need to load store again, current grid uses store with provided gridStoreId
                                    }
                                ]
                            }
//                            {
//                                xtype: 'panel',
//                                title: l('filters'),
//                                region: 'east',
//                                split: false,
//                                width: 400,
//                                autoScroll: true,
//                                collapsible: true,
//                                collapsed: true,
//                                items: [
//                                    {
//                                        bookingGridName: 'living',
//                                        xtype: 'bookingFilterForm'
//                                    }
//                                ]
//                            }
                        ]
                    },
                    {
                        title: l('reservations'),
                        layout: 'border',
                        items: [
                            {
                                xtype: 'container',
                                region: 'center',
                                layout: 'fit',
                                items: [
                                    {
                                        xtype: 'bookingGrid',
                                        itemId: 'reservations',
                                        loadParams: {
                                            params: {
                                                connective: 'or',
                                                filter: [
                                                    {
                                                        field: 'status',
                                                        comparison: 'eq',
                                                        data: {
                                                            type: 'room_use_status',
                                                            value: 'BOOKING_FREE'
                                                        }
                                                    },
                                                    {
                                                        field: 'status',
                                                        comparison: 'eq',
                                                        data: {
                                                            type: 'room_use_status',
                                                            value: 'BOOKING_WARRANTY'
                                                        }
                                                    }
                                                ]
                                            }
                                        },
//                                        gridStoreId: 'console',
//                                        storeParams: {
//                                            model: 'Pms.modules.roomUse.model.roomUse'
//                                        },
//                                        rowFilter: function (rec) {
//                                            return rec.data.status == 'BOOKING_FREE' || rec.data.status == 'BOOKING_WARRANTY';
//                                        },
//                                        autoLoading: false // no need to load store again, current grid uses store with provided gridStoreId
                                    }
                                ]
                            },
//                            {
//                                xtype: 'panel',
//                                title: l('filters'),
//                                region: 'east',
//                                split: false,
//                                width: 400,
//                                autoScroll: true,
//                                collapsible: true,
//                                collapsed: true,
//                                items: [
//                                    {
//                                        bookingGridName: 'reservations',
//                                        xtype: 'bookingFilterForm'
//                                    }
//                                ]
//                            }
                        ]
                    },
                    {
                        title: l('arrival'),
                        layout: 'border',
                        items: [
                            {
                                xtype: 'container',
                                region: 'center',
                                layout: 'fit',
                                items: [
                                    {
                                        xtype: 'bookingGrid',
                                        itemId: 'arrival',
                                        loadParams: {
                                            params: {
                                                filter: [
                                                    {
                                                        field: 'startDate',
                                                        comparison: 'lte',
                                                        data: {
                                                            type: 'date',
                                                            value: Ext.Date.format(new Date(), 'U')
                                                        }
                                                    },
                                                    {
                                                        field: 'status',
                                                        comparison: 'neq',
                                                        data: {
                                                            type: 'room_use_status',
                                                            value: 'LIVING'
                                                        }
                                                    },
                                                    {
                                                        field: 'status',
                                                        comparison: 'neq',
                                                        data: {
                                                            type: 'room_use_status',
                                                            value: 'OUTGO'
                                                        }
                                                    },
                                                    {
                                                        field: 'status',
                                                        comparison: 'neq',
                                                        data: {
                                                            type: 'room_use_status',
                                                            value: 'REFUSE'
                                                        }
                                                    }
                                                ]
                                            }
                                        }
//                                        gridStoreId: 'console',
//                                        storeParams: {
//                                            model: 'Pms.modules.roomUse.model.roomUse'
//                                        },
//                                        rowFilter: function (rec) {
//                                            return rec.data.startDate <= new Date() && rec.data.status != 'LIVING' && rec.data.status != 'OUTGO' && rec.data.status != 'REFUSE';
//                                        },
//                                        autoLoading: false // no need to load store again, current grid uses store with provided gridStoreId
                                    }
                                ]
                            }
//                            {
//                                xtype: 'panel',
//                                title: l('filters'),
//                                region: 'east',
//                                split: false,
//                                width: 400,
//                                autoScroll: true,
//                                collapsible: true,
//                                collapsed: true,
//                                items: [
//                                    {
//                                        bookingGridName: 'arrival',
//                                        xtype: 'bookingFilterForm'
//                                    }
//                                ]
//                            }
                        ]
                    },
                    {
                        title: l('departure'),
                        layout: 'border',
                        items: [
                            {
                                xtype: 'container',
                                region: 'center',
                                layout: 'fit',
                                items: [
                                    {
                                        xtype: 'bookingGrid',
                                        itemId: 'departure',
                                        loadParams: {
                                            params: {
                                                filter: [
                                                    {
                                                        field: 'endDate',
                                                        comparison: 'eq',
                                                        data: {
                                                            type: 'date',
                                                            value: Ext.Date.format(new Date(), 'U')
                                                        }
                                                    },
                                                    {
                                                        field: 'status',
                                                        comparison: 'eq',
                                                        data: {
                                                            type: 'room_use_status',
                                                            value: 'LIVING'
                                                        }
                                                    }
                                                ]
                                            }
                                        }
//                                        gridStoreId: 'console',
//                                        storeParams: {
//                                            model: 'Pms.modules.roomUse.model.roomUse'
//                                        },
//                                        rowFilter: function (rec) {
//                                            return Ext.Date.clearTime(rec.data.endDate) == Ext.Date.clearTime(new Date()) && rec.data.status == 'LIVING';
//                                        },
//                                        autoLoading: false // no need to load store again, current grid uses store with provided gridStoreId
                                    }
                                ]
                            }
//                            {
//                                xtype: 'panel',
//                                title: l('filters'),
//                                region: 'east',
//                                split: false,
//                                width: 400,
//                                autoScroll: true,
//                                collapsible: true,
//                                collapsed: true,
//                                items: [
//                                    {
//                                        bookingGridName: 'departure',
//                                        xtype: 'bookingFilterForm'
//                                    }
//                                ]
//                            }
                        ]
                    },
                    {
                        title: l('missedBooking'),
                        layout: 'border',
                        items: [
                            {
                                xtype: 'container',
                                region: 'center',
                                layout: 'fit',
                                items: [
                                    {
                                        xtype: 'bookingGrid',
                                        itemId: 'missedBooking',
                                        loadParams: {
                                            params: {
                                                filter: [
                                                    {
                                                        field: 'startDate',
                                                        comparison: 'lt',
                                                        data: {
                                                            type: 'date',
                                                            value: Ext.Date.format(Ext.Date.clearTime(Ext.Date.add(new Date(), Ext.Date.DAY, -1)), 'U')
                                                        }
                                                    },
                                                    {
                                                        field: 'status',
                                                        comparison: 'in',
                                                        data: {
                                                            type: 'room_use_status',
                                                            value: 'BOOKING_FREE'
                                                        }
                                                    },
                                                    {
                                                        field: 'status',
                                                        comparison: 'in',
                                                        data: {
                                                            type: 'room_use_status',
                                                            value: 'BOOKING_WARRANTY'
                                                        }
                                                    },
                                                    {
                                                        field: 'status',
                                                        comparison: 'in',
                                                        data: {
                                                            type: 'room_use_status',
                                                            value: 'LIVING'
                                                        }
                                                    }
                                                ]
                                            }
                                        }
//                                        gridStoreId: 'console',
//                                        storeParams: {
//                                            model: 'Pms.modules.roomUse.model.roomUse'
//                                        },
//                                        rowFilter: function (rec) {
//                                            return rec.data.startDate < Ext.Date.clearTime(Ext.Date.add(new Date(), Ext.Date.DAY, -1)) && (rec.data.status == 'BOOKING_FREE' || rec.data.status == 'BOOKING_WARRANTY' || rec.data.status == 'LIVING');
//                                        },
//                                        autoLoading: false // no need to load store again, current grid uses store with provided gridStoreId
                                    }
                                ]
                            },
                        ]
                    },
                    {
                        title: l('filter.bookingStatus.rejection'),
                        layout: 'border',
                        items: [
                            {
                                xtype: 'container',
                                region: 'center',
                                layout: 'fit',
                                items: [
                                    {
                                        xtype: 'bookingGrid',
                                        itemId: 'rejectedBooking',
                                        loadParams: {
                                            params: {
                                                filter: [
                                                    {
                                                        field: 'status',
                                                        comparison: 'in',
                                                        data: {
                                                            type: 'room_use_status',
                                                            value: 'REFUSE'
                                                        }
                                                    }
                                                ]
                                            }
                                        }
//                                        gridStoreId: 'console',
//                                        storeParams: {
//                                            model: 'Pms.modules.roomUse.model.roomUse'
//                                        },
//                                        rowFilter: function (rec) {
//                                            return rec.data.status == 'REFUSE';
//                                        },
//                                        autoLoading: false // no need to load store again, current grid uses store with provided gridStoreId
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
        ];
    },

    filterLiving: function(rec, id) {
        console.log(rec, id)
        return rec.status == 'LIVING';
    },

    buildTopButtons: function () {

    }
});
