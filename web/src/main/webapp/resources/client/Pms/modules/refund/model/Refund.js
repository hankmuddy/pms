Ext.define('Pms.modules.refund.model.Refund', {
    extend: 'Ext.data.Model',
    fields: [
        'id',
        {
            name: 'serviceUses',
            type: 'auto'
        },
        {
            name: 'createdDate',
            type: 'UTCDate',
            serialize: Pms.serializeUTC
        },
        {name: 'total', type: 'int'},
        {name: 'roomUse', type: 'auto'},
        {name: 'group', type: 'auto', persist: false}
    ]
});

