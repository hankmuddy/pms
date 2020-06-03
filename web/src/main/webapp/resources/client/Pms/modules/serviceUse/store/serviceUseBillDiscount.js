Ext.define("Pms.modules.serviceUse.store.serviceUseBillDiscount", {
    extend: "Pms.modules.serviceUse.store.serviceUseBill",
    model: "Pms.modules.serviceUse.model.serviceUse",
    url: 'rest/baseServiceUse',
    pageSize: 1000,
    filters: [
        function (rec) {
            return !rec.data.refund;
        }
    ],
    listeners: {
        load: function (store, records) {
            var billDiscounts = {},
                billId = null;
            Ext.each(records, function (rec) {
                billId = rec.data.bill.id;
                if (rec.data.bill.discount > 0 && !billDiscounts[billId]) {
                    billDiscounts[billId] = {
                        total: -rec.data.bill.discount,
                        service: { title: l('discount')},
                        bill: rec.data.bill,
                        quantity: 1,
                        'bill.id': billId,
                        id: null
                    }
                }
            });
            for (var id in billDiscounts) {
                store.add(new Ext.create('Pms.modules.serviceUse.model.serviceUse', billDiscounts[id]))
            };
        },
        beforeload: function (store, operation, eOpts) {
            if (!operation.params) {
                operation.params = {};
            }
            if (!Ext.Object.isEmpty(this.loadParams)) {
                operation.params = Ext.ux.clone(this.loadParams.params);
            }
            if (!Ext.Object.isEmpty(this.filterParams) && !store.refresh) {
                if (Ext.isEmpty(operation.params.filter)) operation.params.filter = [];
                for (var i in this.filterParams) {
                    operation.params.filter.push(this.filterParams[i]);
                }
            }
            if (!Ext.Object.isEmpty(operation.params)) {
                operation.params = Pms.Ajax.encode(operation.params);
            }
            store.refresh = false;
        }
    }
});