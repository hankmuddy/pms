Ext.define('Pms.modules.serviceUse.controller.serviceUse', {
    extend: 'Pms.abstract.Controller',

    views: [
        'Pms.modules.serviceUse.view.serviceUseGroupGrid',
        'Pms.modules.serviceUse.view.serviceUseAddWindow',
        'Pms.modules.serviceUse.view.serviceUseEditWindow',
        'Pms.modules.serviceUse.view.serviceUseForm',
        'Pms.modules.serviceUse.view.serviceUseBillGrid'
    ],
    stores: [
        'Pms.modules.serviceUse.store.serviceUse',
        'Pms.modules.serviceUse.store.serviceUseBill',
        'Pms.modules.serviceUse.store.serviceUseBillDiscount'
    ],
    models: [
        'Pms.modules.serviceUse.model.serviceUse'
    ],
    refs: [
        {ref: 'serviceUseStore', selector: 'serviceUseStore'},
        {ref: 'serviceUseForm', selector: 'serviceUseForm'},
        {ref: 'serviceUseAddWindow', selector: 'serviceUseAddWindow'},
        {ref: 'serviceUseEditWindow', selector: 'serviceUseEditWindow'},
        {ref: 'serviceUseGroupGrid', selector: 'serviceUseGroupGrid'},
        {ref: 'serviceUseBillGrid', selector: 'serviceUseBillGrid'},

        {ref: 'groupPersonGrid', selector: 'groupPersonGrid'},
        {ref: 'groupRoomUseEditWindow', selector: 'groupRoomUseEditWindow'},

        {ref: 'bookingGrid', selector: 'bookingGrid'},
        {ref: 'billGrid', selector: 'billGrid'},
        {ref: 'livingGrid', selector: 'livingGrid'},
        {ref: 'refuseGrid', selector: 'refuseGrid'}
    ],

    init: function (contr, subController) {
        if (!subController) {
            var view = Ext.create('Pms.modules.serviceUse.view.Viewport');
            this.buildItems(view);
        }

        this.control({
            'serviceUseAddWindow button[action=save]': {
                click: this.addServiceUse
            },
            'serviceUseEditWindow button[action=save]': {
                click: this.updateServiceUse
            },
            'serviceUseEditWindow': {
                close: this.onCloseEditWindow
            },
            'serviceUseGroupGrid button[action=sale]': {
                click: this.beforeSaleCreate
            },
            'serviceUseGroupGrid button[action=refund]': {
                click: this.beforeRefundCreate
            }
        });
    },

    addServiceUse: function (button) {
        var win = button.up('serviceUseAddWindow'),
            form = win.down('serviceUseForm'),
            values = form.getValues(),
            grid = this.getGroupRoomUseEditWindow().down('serviceUseGroupGrid'),
            store = grid.getStore(),
            rec = Ext.create('Pms.modules.serviceUse.model.serviceUse', values),
            group;

        if (form.isValid()) {
            if (!parseInt(rec.data.bill)) {
                if (rec.data.bill == l('serviceUse.newBillOnCustomer')) {
                    group = {id: win.customerGroup.id};
                }
                else {
                    group = null;
                }
                rec.data.bill = {
//                    roomUse: {
//                        id: parseInt(win.roomUse)
//                    },
                    group: group
                };
            }
            else {
                rec.data.bill = {id: parseInt(rec.data.bill)}
            }
            rec.data.service = {id: parseInt(values.service), type: form.serviceType};
            rec.data.roomUse = {id: parseInt(values.roomUse)};
            rec.data.type = form.serviceType + 'Use';
            var oldProxy = store.getProxy();
            store.setProxy({
                type: 'rest',
                url: 'rest/' + form.serviceType + 'Use',
                reader: Ext.create('Ext.data.reader.Json', {
                    root: 'content'
                }),
                writer: {
                    type: 'json'
                }});
            store.remove(store.getNewRecords()) //remove discount record if have some
            store.add(rec);
            store.sync({
                success: function () {
                    win.close();
                    store.setProxy(oldProxy);
                    store.reload();
                }
            });
        } else {
            Pms.App.showNotification({
                message: l('error.recordNotSaved')
            });
        }
    },

    updateServiceUse: function (button) {
        var win = button.up('serviceUseEditWindow'),
            serviceUseForm = win.down('serviceUseForm'),
            values = serviceUseForm.getValues(),
            serviceUseGroupGrid = this.getGroupRoomUseEditWindow().down('serviceUseGroupGrid'),
            serviceUseStore = serviceUseGroupGrid.getStore();

        values.service = serviceUseForm.service;
        values.date = parseInt(values.date);
        values.quantity = parseInt(values.quantity);
        values.bill = {id: values.bill};
        values.type = serviceUseForm.service.type + 'Use';
        if (serviceUseForm.isValid()) {
            Pms.Ajax.request({
                url: 'rest/' + values.service.type + 'Use/' + serviceUseForm.serviceUse.id,
                method: 'PUT',
                jsonData: values
            })
        }
        else {
            Pms.App.showNotification({
                message: l('serviceSaveError')
            });
        }
    },

    onCloseEditWindow: function (win) {
        var grid = this.getGroupRoomUseEditWindow().down('serviceUseGroupGrid');
        if (typeof(grid) != 'undefined') {
            grid.getStore().reload();
            grid.getSelectionModel().clearSelections();
        }
    },

    beforeSaleCreate: function (button) {
        var win = button.up('groupRoomUseEditWindow'),
            serviceUseGroupGrid = win.down('serviceUseGroupGrid'),
            group = serviceUseGroupGrid.group,
            groupId = group.id,
            serviceUses = serviceUseGroupGrid.getStore().data.items,
            selected = serviceUseGroupGrid.getSelectionModel().getSelection(),
            serviceUseIds = [],
            personGrid = this.getGroupPersonGrid(),
            persons = personGrid.getStore().data.items,
            personIds = [];

        if (selected.length) {
            for (var i in selected) serviceUseIds.push(selected[i].internalId);
        }
        else {
            for (var j in serviceUses) serviceUseIds.push(serviceUses[j].internalId);
        }

        if (persons.length) {
            for (var p in persons) personIds.push(persons[p].internalId);
        }

        var billSaleWindow = Ext.widget('billSaleWindow', {
                filterIds: serviceUseIds,
                persons: personIds,
                filter: {
                    property_bill: {
                        notexists: null
                    }
                }
            }),
            billForm = billSaleWindow.down('billForm');

        billForm.getForm().setValues({
            property_io_type: 'sale',
            property_group: groupId,
            property_payer_person: group.property_master_person,
            property_payer_company: group.property_master_company
        });
        billSaleWindow.show();
    },

    beforeRefundCreate: function (button) {
        Pms.App.showNotification({
            message: l('refundByAccount')
        });
    },

//    buildItems: function () {
//        if (typeof(this.win) !== "undefined") {
//            var view = Ext.create('Pms.modules.serviceUse.view.Viewport');
//            this.win.add(view);
//        }
//    }
});
