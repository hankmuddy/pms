Ext.define('Pms.modules.booking.controller.Booking', {
    extend: 'Pms.abstract.Controller',
    views: [
        'Pms.modules.booking.view.Viewport',
        'Pms.modules.booking.view.BookingGrid',
        'Pms.modules.booking.view.BookingFilterForm',
        'Pms.modules.booking.view.bookingViewWindow',
        'Pms.modules.booking.view.ProlongWindow',
        'Pms.modules.serviceUse.view.serviceUseGroupGrid',
        'Pms.modules.groupRoomUse.view.groupRoomUseConfirmWindow',
        'Pms.modules.groupRoomUse.view.groupRoomUseIncomeWindow',
        'Pms.modules.groupRoomUse.view.groupRoomUseOutgoWindow',
        'Pms.modules.groupRoomUse.view.groupRoomUseRefuseWindow',
        'Pms.modules.group.view.GroupBookingSearchGrid',
        'Pms.modules.group.view.GroupBookingSearchFilterForm',
        'Pms.modules.booking.view.GroupedBookingWindow',
        'Pms.modules.groupRoomUse.view.groupRoomUseRefuseForm'
    ],
    stores: [
        'Pms.modules.roomUse.store.roomUse',
        'Pms.modules.serviceUse.store.serviceUse'
    ],
    models: [
        'Pms.modules.roomUse.model.roomUse'
    ],
    refs: [
        {ref: 'bookingViewport', selector: 'bookingViewport'},
        {ref: 'bookingGrid', selector: 'bookingGrid'},
        {ref: 'consoleGrid', selector: 'consoleGrid'},
        {ref: 'bookingStore', selector: 'bookingStore'},
        {ref: 'bookingFilterForm', selector: 'bookingFilterForm'},
        {ref: 'groupRoomUseRefuseForm', selector: 'groupRoomUseRefuseForm'}
    ],

    subControllers: [
        'Pms.modules.groupRoomUse.controller.groupRoomUse',
        'Pms.modules.group.controller.Group'
    ],

    extravailable: true,

    init: function (contr, subController) {
        if (!subController) {
            this.buildItems();
        }

        this.control({
            'bookingGrid': {
                editactionclick: this.edit,
                paymentactionclick: this.edit,
                confirmactionclick: this.confirm,
                prolongactionclick: this.beforeProlong,
                refuseactionclick: this.refuse,
                itemdblclick: this.view,
                outgoactionclick: this.outgo,
                printbuttonclick: this.view,
                cellclick: this.editCustomer
            },
            'consoleGrid': {
                paymentactionclick: this.edit,
                itemdblclick: this.view,
                refuseactionclick: this.refuse,
                cellclick: this.editCustomer
            },
            'prolongWindow button[action=save]': {
                click: this.prolong
            },
            'consoleViewport grid': {
                confirmactionclick: this.confirm
            },
            'groupRoomUseAddWindow': {
                close: this.refreshOnClose
            },
            'groupRoomUseEditWindow': {
                close: this.refreshOnClose
            },
            'groupRoomUseIncomeWindow': {
                close: this.refreshOnClose
            },
            'groupRoomUseOutgoWindow': {
                close: this.refreshOnClose
            },
            'groupRoomUseConfirmWindow': {
                close: this.refreshOnClose
            },
            'groupRoomUseRefuseWindow': {
                close: this.refreshOnClose
            },
            'groupRoomUseGroupAddWindow': {
                close: this.refreshOnClose
            }
        });
    },

    view: function (grid, row, tr) {
        Pms.Ajax.request({
            url: 'rest/roomUse',
            method: 'GET',
            params: Pms.Ajax.encode({
                filter: [
                    {
                        field: 'customerGroup.customer.id',
                        comparison: 'eq',
                        data: {
                            type: 'numeric',
                            value: row.data.customerGroup.customer.id
                        }
                    }
                ]
            }),
            success: function (response) {
                var bookingInfo = response.content[0];
                Pms.Ajax.request({
                    url: 'rest/roomTypeFacility/byRoomType/' + bookingInfo.room.roomType.id,
                    method: 'GET',
                    success: function (response) {
                        var roomTypeInfo = response.content;
                        Pms.Ajax.request({
                            url: 'rest/settings/hotelFacilities',
                            method: 'GET',
                            success: function (response) {
                                var hotelFacilities = response.content;
                                Pms.Ajax.request({
                                    url: 'rest/document/byRoomType/' + bookingInfo.room.roomType.id,
                                    method: 'GET',
                                    success: function (response) {
                                        var roomTypePhotos = response.content[0];
                                        Ext.Ajax.request({
                                            url: 'rest/settings',
                                            method: 'GET',
                                            success: function (res) {
                                                var settings = JSON.parse(res.responseText).content;
                                                Ext.widget('bookingViewWindow', {
                                                    data: bookingInfo,
                                                    settings: settings,
                                                    roomTypeInfo: roomTypeInfo,
                                                    roomTypePhotos: roomTypePhotos,
                                                    hotelFacilities: hotelFacilities
                                                });
                                            }
                                        });
                                    }
                                })
                            }
                        })
                    }
                });
            },
            failure: function (response) {
                var mess = response.error.message,
                    code = response.error.code;

                Pms.App.showNotification({
                    message: l('error') + ' ' + code + mess
                });
            }
        });
    },

    edit: function (grid, rowIndex, colIndex, activeTab) {
        var store = grid.getStore(),
            record = store.getAt(rowIndex),
            win = Ext.widget('groupRoomUseEditWindow', {
                data: record.data
            });

        var gruForm = win.down('groupRoomUseEditForm'),
            gruFormValues = {
                id: record.data.id,
                endDate: record.data.endDate,
                description: record.data.description
            };

        gruForm.getForm().setValues(gruFormValues);

        if (activeTab) {
            win.down('tabpanel').setActiveTab(activeTab);
        }
    },

    confirm: function (grid, rowIndex, colIndex) {
        var me = this,
            store = grid.getStore(),
            rec = store.getAt(rowIndex);

        grid.getSelectionModel().select(rowIndex);
        Ext.Msg.confirm(l('confirm'), l('confirmReservation') + ' №' + rec.data.id + '?', function (btn) {
            if (btn === 'yes') {
                Ext.Ajax.request({
                    url: 'rest/roomUse/' + rec.data.id + '/confirmed',
                    method: 'POST',
                    success: function () {
                        me.refreshOnClose();
                    },
                    failure: function () {
//                        Pms.App.showNotification({
//                            message: l('error') + ' ' + code + mess
//                        });
                    }
                })
            }
        });
    },

    outgo: function (grid, rowIndex, colIndex) {
        var me = this,
            store = grid.getStore(),
            rec = store.getAt(rowIndex);

        grid.getSelectionModel().select(rowIndex);
        var win = Ext.widget('groupRoomUseOutgoWindow', {data: rec.data});
        win.show();
    },

    beforeProlong: function (grid, rowIndex, colIndex) {
        var me = this,
            store = grid.getStore(),
            rec = store.getAt(rowIndex);

        grid.getSelectionModel().select(rowIndex);
        var win = Ext.widget('prolongWindow', {
                data: rec.data
            }),
            form = win.down('form');

        form.loadRecord(rec);
        win.show();
    },

    prolong: function (btn, eOpts) {
        var me = this,
            win = btn.up('window'),
            form = win.down('form'),
            values = form.getValues(),
            record = {data: win.data};

        record.data.createdDate = Pms.toUTC(record.data.createdDate);
        record.data.startDate = Pms.toUTC(record.data.startDate);
        record.data.endDate = Pms.toUTC(values.endDate);
        Pms.Ajax.request({
            url: 'rest/roomUse/' + record.data.id,
            method: 'PUT',
            jsonData: record.data,
            success: function (response) {
                me.refreshOnClose();
                win.close();
            },
            failure: function (response) {
                var source = response.source,
                    code = response.code;
                Ext.Msg.alert(l('error') + ' ' + l(source), Pms.iconError + l(code), function () {
                    me.refreshOnClose();
                });
                win.close();
            }
        });
    },

    refuse: function (grid, rowIndex, colIndex) {
        var me = this,
            store = grid.getStore(),
            rec = store.getAt(rowIndex);

        grid.getSelectionModel().select(rowIndex);
        var win = Ext.widget('groupRoomUseRefuseWindow', {
                data: rec.data
            }),
            form = win.down('form');

        rec.data.date = new Date() < rec.data.startDate ? rec.data.startDate : new Date();
        rec.data.room = rec.data.room.id;
        form.loadRecord(rec);
        win.show();
    },

//    refreshOnClose: function () {
//        var bookingGrid = this.getBookingGrid(),
//            stores = [
//                Ext.data.StoreManager.lookup('Pms.modules.accommodation.store.Summary'),
//                Ext.data.StoreManager.lookup('Pms.modules.roomUse.store.roomUse')
//            ];
//
//        var consoleGrids = Ext.ComponentQuery.query('consoleGrid'),
//            consoleSelModel,
//            bookingGrids = Ext.ComponentQuery.query('bookingGrid'),
//            grids = consoleGrids.concat(bookingGrids);
//        for (var c in grids) {
//            if (typeof(grids[c]) != 'undefined') {
//                consoleSelModel = grids[c].getSelectionModel().getSelection();
//                if (consoleSelModel.length == 1) {
//                    grids[c].getView().focusRow(consoleSelModel[0].index);
//                }
//                grids[c].getStore().reload();
//            }
//        }
//
//        if (typeof(bookingGrid) != 'undefined') {
//            bookingGrid.getStore().reload();
//            bookingGrid.getSelectionModel().clearSelections();
//        }
//
//        for (var i in stores) {
//            if (typeof(stores[i]) != 'undefined') {
//                stores[i].reload();
//            }
//        }
//    },
    refreshOnClose: function () {
        var bookingViewport = this.getBookingViewport();
        if (!Ext.isEmpty(bookingViewport)) {
            var bookingGrid = bookingViewport.down('bookingGrid');
            if (!Ext.isEmpty(bookingGrid)) {
                bookingGrid.getStore().reload();
                bookingGrid.getSelectionModel().clearSelections();
            }
        }

        summaryStore = Ext.data.StoreManager.lookup('Pms.modules.accommodation.store.Summary');
        if (typeof(summaryStore) != 'undefined') summaryStore.reload();
    },

    editCustomer: function (grid, td, cellIndex, rec, tr, rowIndex, e, eOpts) {
        var index = 2;
        if (grid.xtype = 'consoleGrid') {
            index = 3;
        }
        if (cellIndex == index) {
            var customerGroup = rec.data.customerGroup;

            if (customerGroup.company) {
                var customer = customerGroup.company,
                    win = Ext.create('Pms.modules.company.view.CompanyEditWindow', {data: customer}),
                    customerRec = Ext.create('Pms.modules.company.model.Company', customer);
            }
            else {
                var customer = customerGroup.customer,
                    win = Ext.create('Pms.modules.person.view.PersonEditWindow', {data: customer}),
                    customerRec = Ext.create('Pms.modules.person.model.Person', customer);
            }

            var form = win.down('form');

            form.loadRecord(customerRec);
            win.show();
        }
    },

    buildItems: function () {
        if (typeof(this.win) !== 'undefined') {
            var view = Ext.widget('bookingViewport');
            this.win.maximized = true;
            this.win.add(view);
        }
    }
});