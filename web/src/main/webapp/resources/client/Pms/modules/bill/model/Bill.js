Ext.define('Pms.modules.bill.model.Bill', {
    extend: 'Ext.data.Model',
    fields: [
        'id',
        {name: 'serviceUses', type: 'auto'},
        {name: 'createdDate', type: 'UTCDate', serialize: Pms.serializeUTC},
        {name: 'total', type: 'int'},
        {name: 'roomUse', type: 'auto'},
        {name: 'group', type: 'auto'},
        {name: 'totalPaid', type: 'int'},
        {name: 'approved', type: 'bool'},
        {name: 'forCustomer', type: 'bool'},
        {name: 'description'},
        {name: 'discount'}
    ]
});