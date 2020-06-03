Ext.define('Pms.modules.season.model.Season', {
    extend: 'Sch.model.Event',

    startDateField: 'start',
    endDateField: 'until',
    resourceIdField: 'plan.id',

    fields: [
        'id',
        {name: 'plan', type: 'auto'},
        {
            name: 'start',
            type: 'UTCDate',
            serialize: Pms.serializeUTC
        },
        {
            name: 'until',
            type: 'UTCDate',
            serialize: Pms.serializeUTC
        },
        'approved'
    ]
});