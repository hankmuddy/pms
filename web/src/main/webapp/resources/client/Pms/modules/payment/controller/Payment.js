Ext.define('Pms.modules.payment.controller.Payment', {
    extend: 'Pms.abstract.Controller',

    views: [
        'Pms.modules.payment.view.Viewport',
        'Pms.modules.payment.view.PaymentAddWindow',
        'Pms.modules.payment.view.paymentViewWindow',
        'Pms.modules.payment.view.PaymentEditWindow',
        'Pms.modules.payment.view.PaymentPrintWindow',
        'Pms.modules.payment.view.PaymentGrid',
        'Pms.modules.payment.view.PaymentPrintGrid',
        'Pms.modules.payment.view.PaymentForm',
        'Pms.modules.payment.view.PaymentFilterForm',
        'Pms.modules.bill.view.Viewport',
        'Pms.modules.bill.view.BillGrid',
        'Pms.modules.serviceUse.view.serviceUseGroupGrid',
        'Pms.modules.groupRoomUse.view.groupRoomUseEditWindow'
    ],
    stores: [
        'Pms.modules.payment.store.Payment',
        'Pms.modules.payment.store.groupPayment',
        'Pms.modules.payment.store.moneyType',
        'Pms.modules.bill.store.Bill'
    ],
    models: [
        'Pms.modules.payment.model.Payment',
        'Pms.modules.bill.model.Bill'
    ],
    refs: [
        {ref: 'paymentViewport', selector: 'paymentViewport'},
        {ref: 'paymentAddWindow', selector: 'paymentAddWindow'},
        {ref: 'paymentEditWindow', selector: 'paymentEditWindow'},
        {ref: 'paymentPrintWindow', selector: 'paymentPrintWindow'},
        {ref: 'paymentGrid', selector: 'paymentGrid'},
        {ref: 'paymentPrintGrid', selector: 'paymentPrintGrid'},
        {ref: 'paymentForm', selector: 'paymentForm'},
        {ref: 'paymentGroupForm', selector: 'paymentGroupForm'},

        {ref: 'serviceUseGroupGrid', selector: 'serviceUseGroupGrid'},

        {ref: 'groupRoomUseEditWindow', selector: 'groupRoomUseEditWindow'},

        {ref: 'billViewport', selector: 'billViewport'},
        {ref: 'billGrid', selector: 'billGrid'}
    ],

    subControllers: [
        'Pms.modules.person.controller.Person',
        'Pms.modules.company.controller.Company'
    ],

    init: function (contr, subController) {

        if (!subController) {
            var view = Ext.widget('paymentViewport');
            this.buildItems(view);
        }

        this.control({
            'paymentGrid button[action=print]': {
                click: function (btn) {
                    var grid = btn.up('paymentGrid'),
                        rows = grid.getSelectionModel().getSelection();
                    if (rows.length) this.beforePrint(grid, rows[0].index);
                    else Ext.Msg.alert(l('error'), l('warning.selectRecord'));
                }
            },
            'paymentGrid': {
//                itemdblclick: this.view,
                printbuttonclick: /*this.beforePrint*/this.view,
                deletebuttonclick: this.delete,
                cellclick: this.editCustomer
            },
            'paymentAddWindow button[action=save]': {
                click: this.add
            },
            'paymentPrintWindow button[action=print]': {
                click: this.print
            },
            'billGrid': {
                paymentbuttonclick: this.create
            },
            'billGrid button[action=payment]': {
                click: function (btn) {
                    var grid = btn.up('billGrid'),
                        rows = grid.getSelectionModel().getSelection();
                    if (rows.length) this.create(grid, rows[0].index);
                    else Pms.App.showNotification({message: l('warning.selectRecord')});
                }
            },
            // refresh actions
            'paymentAddWindow': {
                close: this.refreshOnClose
            },
            'paymentEditWindow': {
                close: this.refreshOnClose
            },
            'personEditWindow': {
                close: this.refreshOnClose
            },
            'companyEditWindow': {
                close: this.refreshOnClose
            }
        });
    },

    create: function (grid, rowIndex, colIndex) {
        var store = grid.getStore(),
            row = store.getAt(rowIndex),
            rec = row.data;

        if (rec.totalPaid >= rec.total) {
            Pms.App.showNotification({message: l('payment.currentBillFullyPaid')});
            return false;
        }

        var paymentAddWindow = Ext.widget('paymentAddWindow', {data: rec}),
            paymentForm = paymentAddWindow.down('paymentForm'),
            sum = rec.total - rec.totalPaid;

        paymentForm.getForm().setValues({
            bill: rec.id,
            total: sum
        });
        paymentAddWindow.show();
    },

    add: function (button) {
        var win = button.up('paymentAddWindow'),
            paymentForm = win.down('paymentForm'),
            data = paymentForm.getForm().getValues(),
            rec = Ext.create('Pms.modules.payment.model.Payment', data),
            store = Ext.create('Pms.modules.payment.store.Payment'),
            billId = win.data.id,
            billMsg = '',
            time = paymentForm.down('timefield').value;

        rec.data.date.setHours(rec.data.date.getHours() + time.getHours());
        rec.data.date.setMinutes(rec.data.date.getMinutes() + time.getMinutes());
        rec.set('bill', billId);

        store.getProxy().appendId = false;
        if (paymentForm.isValid()) {
            store.add(rec);
            store.sync({
                success: function () {
                    Ext.Ajax.request({
                        url: 'rest/bill/' + billId + '/fullyPaid',
                        method: 'GET',
                        success: function (res) {
                            var responseObj = JSON.parse(res.responseText);
                            if (responseObj.content) billMsg += '<br />' + l('payment.billFullyPaid');
                            else billMsg += '<br />' + l('payment.billNotFullyPaid');
                            win.close();
                            Pms.App.showNotification({
                                message: l('payment.paymentReady') + billMsg,
                                icon: Pms.notificationOk
                            });
                        }
                    })
                }
            });
        }
        else {
            Pms.App.showNotification({
                message: l('error.incorrect_form_data')
            });
        }
    },

    view: function (grid, rowIndex, colIndex) {
        var store = grid.getStore(),
            rec = store.getAt(rowIndex);

        Pms.createPreloader();
        Ext.create('Ext.window.Window', {
            title: l('payment.pko') + ' №' + rec.data.id,
            width: 1000,
            height: 600,
            maximizable: true,
            layout: 'fit',
            listeners: {
                close: Pms.removePreloader
            },
            items: [
                {
                    xtype: 'component',
                    height: '100%',
                    width: '100%',
                    html: '<object width="100%" height="100%" onload="Pms.removePreloader()" data="report/creditAccountOrder?paymentId=' + rec.data.id + '"></object>'
                }
            ]
        }).show();
    },

    beforePrint: function (grid, rowIndex, colIndex) {
        var store = grid.getStore(),
            row = store.getAt(rowIndex),
            rec = row.data;

        if (!rec.approved) {
            Pms.App.showNotification({
                message: l('cantPrintNotConfirmedPayment')
            });
            return false;
        }

        Ext.widget('paymentPrintWindow', {payment: row}).show();
    },

    print: function (button, e, options) {
        var win = button.up('paymentPrintWindow'),
            grid = win.down('paymentPrintGrid'),
            msgTitle = l('checkPrint');

        Ext.ux.grid.Printer.printAutomatically = false;
        Ext.ux.grid.Printer.print(grid);
    },

    delete: function (grid, rowIndex, colIndex) {
        var me = this,
            store = grid.getStore(),
            row = store.getAt(rowIndex),
            rec = row.data;

        if (!rec.approved) {
            Ext.Msg.confirm(l('paymentDelete'), l('confirmation'), function (btn) {
                if (btn === 'yes') {
                    store.remove(row);
                    store.sync();
                    me.refreshOnClose();
                }
            });
        }
        else {
            Pms.App.showNotification({
                message: l('cantDeleteConfirmedPayment')
            });
        }
    },

    editCustomer: function (grid, td, cellIndex, rec, tr, rowIndex, e, eOpts) {
        if (cellIndex == 3) {
            var customerGroup = rec.data.bill.roomUse ? rec.data.bill.roomUse.customerGroup : rec.data.bill.group;

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
        var paymentViewport = this.getPaymentViewport();
        if (!Ext.isEmpty(paymentViewport)) {
            var paymentGrid = paymentViewport.down('paymentGrid');
            if (!Ext.isEmpty(paymentGrid)) {
                paymentGrid.getStore().reload();
                paymentGrid.getSelectionModel().clearSelections();
            }
        }

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
            var gruPaymentGrid = gruWin.down('paymentGrid');
            if (!Ext.isEmpty(gruPaymentGrid)) {
                gruPaymentGrid.getStore().reload();
                gruPaymentGrid.getSelectionModel().clearSelections();
            }
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
    }
});
