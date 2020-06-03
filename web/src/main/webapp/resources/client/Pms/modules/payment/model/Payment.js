Ext.define('Pms.modules.payment.model.Payment', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'id', type: 'int', persist: false},
        {name: 'bill', type: 'auto', serialize: function (value) {
            return {id: parseInt(value)}
        }},
        {name: 'bankDetails', type: 'auto', serialize: function (value) {
            return {id: parseInt(value)}
        }},
        {name: 'date',
            type: 'UTCDate', serialize: Pms.serializeUTC},
//            type: 'date', dateFormat: 'timestamp', serialize: function (value) {return parseInt(value.getTime() / 1000)}},
        {name: 'total', serialize: function (value) {
            return parseInt(value * 100)
        }},
//        'method',
        {name: 'approved', type: 'boolean', persist: false},
        'description',
        {name: 'billTotal', persist: false, convert: function (val, rec) {
            if (rec.data.bill)
                return rec.data.bill.total;
        }},
        {name: 'billTotalPaid', persist: false, convert: function (val, rec) {
            if (rec.data.bill)
                return rec.data.bill.totalPaid;
        }},
//        'commit',
//        'refund',
//        'bill_id',
//        'payer_person',
//        'payer_company'
    ],
//    associations: [
//        {
//            type: 'belongsTo',
//            model: 'Pms.modules.bill.model.Bill',
//            name: 'bill',
//            associationKey: 'bill',
//            primaryKey: 'id',
//            foreignKey: 'payments'
//        }
//    ]
});

