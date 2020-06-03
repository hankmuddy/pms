Ext.define("Pms.modules.booking.model.Booking", {
    extend: "Ext.data.Model",

    fields: [
        'id',
        'rate',
        'group',
        'description',
        'source_type',
        'use_type',
        {
            name: 'room',
            type: 'auto',
            serialize: function (value) {
                return {id: value}
            }
        },
        'rate_policy',
        'status',
        'source',
        {name: 'total', type: 'int'},
        {name: 'totalPaid', type: 'int'},
        {name: 'createdDate', type: 'UTCDate', serialize: Pms.serializeUTC},
        {name: 'date', type: 'UTCDate', serialize: Pms.serializeUTC},
        {name: 'startDate', type: 'UTCDate', serialize: Pms.serializeUTC},
        {name: 'endDate', type: 'UTCDate', serialize: Pms.serializeUTC}
    ]
});