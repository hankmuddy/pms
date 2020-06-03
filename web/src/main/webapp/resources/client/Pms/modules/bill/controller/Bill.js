Ext.define('Pms.modules.bill.controller.Bill', {
    extend: 'Pms.abstract.Controller',

    views: [
        'Pms.modules.bill.view.Viewport',
        'Pms.modules.bill.view.BillRefundWindow',
        'Pms.modules.bill.view.BillEditWindow',
        'Pms.modules.bill.view.BillGrid',
        'Pms.modules.bill.view.BillForm',
        'Pms.modules.bill.view.BillFilterForm',
        'Pms.modules.bill.view.BillPrintGrid',
        'Pms.modules.bill.view.BillPrintWindow',
        'Pms.modules.bill.view.billViewWindow',
        'Pms.modules.bill.view.billPdfWindow',
        'Pms.modules.bill.view.billInvoiceWindow',
        'Pms.modules.bill.view.payerSelectWindow',

        'Pms.modules.groupRoomUse.view.groupRoomUseEditWindow',
        'Pms.modules.person.view.groupPersonGrid',

        'Pms.modules.payment.view.PaymentAddWindow',
        'Pms.modules.payment.view.PaymentGrid',
        'Pms.modules.payment.view.PaymentForm',

        'Pms.modules.serviceUse.view.serviceUseGroupGrid',
        'Pms.modules.serviceUse.view.serviceUseBillGrid',
        'Pms.modules.refund.view.RefundGrid',
        'Pms.modules.refund.view.RefundFilterForm',
        'Pms.modules.bill.view.BillPrePaymentWindow'
    ],
    stores: [
        'Pms.modules.bill.store.Bill',
        'Pms.modules.payment.store.moneyType',
        'Pms.modules.refund.store.Refund',
        'Pms.modules.payment.store.Payment'
    ],
    models: [
        'Pms.modules.bill.model.Bill',
        'Pms.modules.refund.model.Refund',
        'Pms.modules.payment.model.Payment'
    ],
    refs: [
        {ref: 'billViewport', selector: 'billViewport'},
        {ref: 'billGrid', selector: 'billGrid'},
        {ref: 'billRefundWindow', selector: 'billRefundWindow'},
        {ref: 'billEditWindow', selector: 'billEditWindow'},
        {ref: 'billForm', selector: 'billForm'},

        {ref: 'refundGrid', selector: 'refundGrid'},

        {ref: 'groupRoomUseEditWindow', selector: 'groupRoomUseEditWindow'},
        {ref: 'groupPersonGrid', selector: 'groupPersonGrid'},

        {ref: 'paymentGrid', selector: 'paymentGrid'},
        {ref: 'paymentAddWindow', selector: 'paymentAddWindow'},
        {ref: 'paymentForm', selector: 'paymentForm'},

        {ref: 'serviceUseGroupGrid', selector: 'serviceUseGroupGrid'},
        {ref: 'serviceUseBillGrid', selector: 'serviceUseBillGrid'}
    ],

    subControllers: [
        'Pms.modules.payment.controller.Payment'
//        'Pms.modules.person.controller.Person',
//        'Pms.modules.company.controller.Company'
    ],

    init: function (contr, subController) {
        if (!subController) {
            var view = Ext.create('Pms.modules.bill.view.Viewport');
            this.buildItems(view);
        }

        this.control({
            'billGrid button[action=edit]': {
                click: function (btn) {
                    var grid = btn.up('billGrid'),
                        rows = grid.getSelectionModel().getSelection();
                    if (rows.length) this.edit(grid, rows[0].index);
                    else Ext.Msg.alert(l('error'), l('warning.selectRecord'));
                }
            },
            'billGrid button[action=commit]': {
                click: function (btn) {
                    var grid = btn.up('billGrid'),
                        rows = grid.getSelectionModel().getSelection();
                    if (rows.length) this.commit(grid, rows[0].index);
                    else Ext.Msg.alert(l('error'), l('warning.selectRecord'));
                }
            },
            'billGrid button[action=refund]': {
                click: function (btn) {
                    var grid = btn.up('billGrid'),
                        rows = grid.getSelectionModel().getSelection();
                    if (rows.length) this.beforeRefund(grid, rows[0].index);
                    else Ext.Msg.alert(l('error'), l('warning.selectRecord'));
                }
            },
            'billGrid button[action=delete]': {
                click: function (btn) {
                    var grid = btn.up('billGrid'),
                        rows = grid.getSelectionModel().getSelection();
                    if (rows.length) this.delete(grid, rows[0].index);
                    else Ext.Msg.alert(l('error'), l('warning.selectRecord'));
                }
            },
            'billGrid': {
//                celldblclick: this.view,
                editbuttonclick: this.edit,
                commitbuttonclick: this.commit,
                invoicebuttonclick: this.invoice,
                viewhotelbillbuttonclick: this.view,//HotelBill,
                refundbuttonclick: this.beforeRefund,
                deletebuttonclick: this.delete,
                cellclick: this.editCustomer
            },
            'billEditWindow button[action=save]': {
                click: this.update
            },
            'billRefundWindow button[action=refund]': {
                click: this.refund
            },
            'billPrintWindow button[action=print]': {
                click: this.print
            },
            // refresh actions
            'billEditWindow': {
                close: this.refreshOnClose
            },
            'billRefundWindow': {
                close: this.refreshOnClose
            },
            'personEditWindow': {
                close: this.refreshOnClose
            },
            'companyEditWindow': {
                close: this.refreshOnClose
            },
            'billPdfWindow': {
                close: Pms.removePreloader
            },
            'billInvoiceWindow': {
                close: Pms.removePreloader
            }
        });
    },

    view: function (grid, rowIndex, colIndex) {
        var store = grid.getStore(),
            rec = store.getAt(rowIndex),
            id = rec.getId();

        Ext.Ajax.request({
            url: 'rest/bill/' + id,
            method: 'GET',
            failure: function (response) {
                var mess = response.error.message,
                    code = response.error.code;

                Pms.App.showNotification({
                    message: l('error') + ' ' + code + mess
                });
            },
            success: function (res) {
                var rec = JSON.parse(res.responseText).content;
                Ext.Ajax.request({
                    url: 'rest/settings',
                    method: 'GET',
                    success: function (res) {
                        var settings = JSON.parse(res.responseText).content;
                        Ext.Ajax.request({
                            url: 'rest/bankDetails/default',
                            method: 'GET',
                            success: function (res) {
                                var bankDetails = JSON.parse(res.responseText).content;
                                var groupMember;
                                if (rec.roomUse) {
                                    Ext.Ajax.request({
                                        url: 'rest/groupMemberToRoomUse',
                                        async: false,
                                        params: Pms.Ajax.encode({
                                            filter: [
                                                {
                                                    field: 'roomUse.id',
                                                    comparison: 'eq',
                                                    data: {
                                                        type: 'numeric',
                                                        value: rec.roomUse.id
                                                    }
                                                }
                                            ],
                                            limit: 1
                                        }),
                                        method: 'GET',
                                        success: function (res) {
                                            var members = JSON.parse(res.responseText).content;
//                                        if (!members.length && !rec.forCustomer) {
//                                            Ext.Msg.alert(l('error'), Pms.iconError + ' ' + l('bill.roomUseMustConstrainGroupMembers'))
//                                        }
//                                        else {
                                            groupMember = members[0] ? members[0].groupMember.person : null;

//                                        }
                                        }
                                    });
                                }
                                else {
                                    groupMember = rec.group.customer;
                                }
                                Ext.widget('billPrePaymentWindow', {
                                    bill: rec,
                                    settings: settings,
                                    bankDetails: bankDetails,
                                    groupMember: groupMember
                                });

                            }
                        });
                    }
                })
            }
        });
    },

    invoice: function (grid, rowIndex, colIndex) {
        var store = grid.getStore(),
            rec = store.getAt(rowIndex),
            id = rec.getId();

        Ext.Ajax.request({
            url: 'rest/bill/' + id,
            method: 'GET',
            failure: function (response) {
                var mess = response.error.message,
                    code = response.error.code;

                Pms.App.showNotification({
                    message: l('error') + ' ' + code + mess
                });
            },
            success: function (res) {
                var rec = JSON.parse(res.responseText).content;
                Ext.widget('payerSelectWindow', {
                    bill: rec
                });
            }
        });
    },

//    invoice: function (grid, rowIndex, colIndex) {
//        var store = grid.getStore(),
//            rec = store.getAt(rowIndex),
//            id = rec.getId();
//        Ext.widget('billInvoiceWindow', {
//            data: rec.data
//        });
//    },

    edit: function (grid, rowIndex, colIndex) {
        var store = grid.getStore(),
            row = store.getAt(rowIndex),
            rec = row.data,
            group = rec.customerGroup;

        var billEditWindow = Ext.widget('billEditWindow', {bill: rec}),
            form = billEditWindow.down('billForm').getForm();

        rec.customerGroup = group;
        group.customer ? rec.customer = group.customer.id : null;
        group.company ? rec.company = group.company.id : null;
        form.setValues(rec);
        billEditWindow.show();
    },

    update: function (button) {
        var win = button.up('billEditWindow'),
            approved = win.bill.approved,
            serviceUses = win.down('serviceUseBillGrid').getStore().data.items,
            form = win.down('billForm'),
            data = form.getForm().getValues(),
            customerGroupId = data.customerGroup;

        data.customerGroup = {
            id: customerGroupId,
            customer: {id: data.customer}
        };

        delete data.customer;
        if (!approved) {
            data.property_service_uses = [];
            if (!serviceUses.length) {
                Pms.App.showNotification({
                    message: Pms.iconPrice + ' ' + l('cantDeleteServicesFromBill')
                });
                return;
            }
            for (var s in serviceUses) data.property_service_uses.push(serviceUses[s].internalId);
        }

        if (form.isValid()) {
            Pms.Ajax.request({
                url: 'rest/bill' + data.id,
                method: 'PUT',
                jsonData: data,
                success: function (response) {
                    var bill = response.data[0];
                    win.close();
                    Pms.App.showNotification({
                        message: l('bill') + ' №' + bill.id + '<br />' + l('sum') + ': ' + bill.property_total,
                        icon: Pms.notificationOk
                    });
                },
                failure: function (response) {
                    var mess = response.error.message,
                        code = response.error.code;
                    Ext.Msg.alert(l('error') + ' ' + code, mess);
                }
            });
        }
        else {
            Ext.Msg.alert(l('warning'), l('checkFormFields'));
        }
    },

    commit: function (cell, rowIndex, colIndex) {
        var grid = this.getBillGrid(),
            selected = grid.getSelectionModel().getSelection(),
            store = cell.getStore(),
            idArray = '', rec = null;
        if (selected.length == 0) selected = cell.getSelectionModel().getSelection();
        if (selected.length > 0) {
            Ext.Msg.confirm(l('confirm'), l('confirmBill'), function (btn) {
                if (btn === "yes") {
                    Ext.each(selected, function (data, index) {
                        idArray += data.internalId;
                        if (index != selected.length - 1)
                            idArray += ',';
                    }, this);
                    Pms.Ajax.request({
                        url: "rest/bill/" + idArray + '/approved',
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        success: function (data) {
                            store.load();
                            grid.getSelectionModel().clearSelections();
                        }
                    });
                }
            }, this);
        }
        else if (rowIndex != undefined) {
            rec = cell.getRecord(rowIndex);
            idArray = rec.getId();
            Pms.Ajax.request({
                url: "rest/bill/" + idArray + '/approved',
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                success: function (data) {
                    store.reload();
                    grid.getSelectionModel().clearSelections();
                }
            });
        } else {
            Ext.Msg.alert(l('warning'), l('warning.selectRecord'));
        }

    },

    beforePrint: function (grid, rowIndex, colIndex) {
        var store = grid.getStore(),
            row = store.getAt(rowIndex),
            rec = row.data,
            msgTitle = l('billPrintTitle'),
            win = grid.up('groupRoomUseEditWindow');

        if (!rec.approved) {
            Pms.App.showNotification({
                message: Pms.iconError + ' ' + l('confirmBillBeforePrint')
            });
            return;
        }
        Ext.widget('billPrintWindow', {bill: rec}).show();
    },

    print: function (button, e, options) {
        var win = button.up('billPrintWindow'),
            grid = win.down('grid');

        Ext.ux.grid.Printer.printAutomatically = false;
        Ext.ux.grid.Printer.print(grid);
    },

    delete: function (grid, rowIndex, colIndex) {
        var store = grid.getStore(),
            row = store.getAt(rowIndex),
            rec = row.data;

        if (!rec.approved) {
            Ext.Msg.confirm(l('deletingBill'), l('confirmation'), function (btn) {
                if (btn === 'yes') {
                    store.remove(row);
                    store.sync({
                        success: function () {
                            store.reload();
                        },
                        failure: function () {
                            store.reload();
                        }
                    });
                }
            });
        }
        else {
            Pms.App.showNotification({
                message: l('cantDeleteConfimedBill')
            });
        }
    },

    beforeRefund: function (grid, rowIndex, colIndex) {
        var store = grid.getStore(),
            row = store.getAt(rowIndex),
            rec = row.data;

        Ext.widget('billRefundWindow', {bill: rec}).show();
    },

    refund: function (button) {
        var win = button.up('billRefundWindow'),
            grid = win.down('grid'),
            selected = grid.getSelectionModel().getSelection(),
            serviceArr = [],
            roomUse = {id: selected[0].data.roomUse.id},
            me = this;

        if (selected.length > 0) {
            Ext.Msg.confirm(l('confirm'), l('makeRefund'), function (btn) {
                if (btn === "yes") {
                    Ext.each(selected, function (rec, index) {
                        serviceArr.push({id: rec.data.id, type: rec.data.type});
                    }, this);
                    Pms.Ajax.request({
                        url: "rest/refund/",
                        prevError: true,
                        jsonData: {serviceUses: serviceArr, roomUse: roomUse},
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        success: function (data) {
                            win.close();
                        },
                        failure: Pms.bankDetailsRequired
                    });

                }

            }, this);
        } else {
            Ext.Msg.alert(l('warning'), l('warning.selectRecord'));
        }
    },

    editCustomer: function (grid, td, cellIndex, rec, tr, rowIndex, e, eOpts) {
        if (cellIndex == 1) {
            var customerGroup = rec.data.roomUse ? rec.data.roomUse.customerGroup : rec.data.group;

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

    refreshOnClose: function () {
        var billViewport = this.getBillViewport();
        if (!Ext.isEmpty(billViewport)) {
            var billGrid = billViewport.down('billGrid');
            if (!Ext.isEmpty(billGrid)) {
                billGrid.getStore().reload();
                billGrid.getSelectionModel().clearSelections();
            }
        }
        var gruWin = this.getGroupRoomUseEditWindow();
        if (!Ext.isEmpty(gruWin)) {
            var gruBillGrid = gruWin.down('billGrid');
            if (!Ext.isEmpty(gruBillGrid)) {
                gruBillGrid.getStore().reload();
                gruBillGrid.getSelectionModel().clearSelections();
            }
            var serviceUseGroupGrid = gruWin.down('serviceUseGroupGrid');
            if (!Ext.isEmpty(serviceUseGroupGrid)) {
                serviceUseGroupGrid.getStore().reload();
                serviceUseGroupGrid.getSelectionModel().clearSelections();
            }
        }
        var refundGrid = this.getRefundGrid();
        if (!Ext.isEmpty(refundGrid)) {
            refundGrid.getStore().reload();
        }
    },

//    buildItems: function () {
//        if (!Ext.isEmpty(this.win)) {
//            var view = Ext.create('Pms.modules.bill.view.Viewport');
//            this.win.add(view);
//        }
//    }
});
