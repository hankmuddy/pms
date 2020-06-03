Ext.define("Pms.modules.serviceUse.model.serviceUse", {
    extend: "Ext.data.Model",
    fields: [
        'id',
        {
            name: 'date',
            type: 'date',
            dateFormat: 'timestamp',
            serialize: function (val) {
                return val.getTime() / 1000;
            }
        },
        {name: 'service', type: 'auto'},
        {name: 'quantity', type: 'int'},
        {name: 'rawTotal', type: 'int'},
        {name: 'livingAmount', type: 'int', persist: false},
        {name: 'tourismTax', type: 'int'},
        'total',
        'type',
        {name: 'bill', type: 'auto'},
        {name: 'roomUse', type: 'auto'},
        {name: 'bill.id', type: 'int', persist: false},
        {name: 'refund', persist: false},
        'description',
        {name: 'approved', type: 'boolean', persist: false}

    ],
    belongsTo: [
        {
            name: 'bill',
            instanceName: 'billId',
            model: 'Pms.modules.bill.model.Bill',
            getterName: 'getBill',
            setterName: 'setBill',
            associationKey: 'bill'
        }
    ],
});

