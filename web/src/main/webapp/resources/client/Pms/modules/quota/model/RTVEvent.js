Ext.define('Pms.modules.quota.model.RTVEvent', {
    extend: 'Sch.model.Event',

    startDateField: 'startDate',
    endDateField: 'endDate',
    resourceIdField: 'roomType.id',
    nameField: 'name',
    idProperty: 'id',

    fields: [
        'id',
        {name: 'roomTypeId', type: 'auto', mapping: 'roomType.id'},
        {name: 'startDate', type: 'UTCDate', mapping: 'date'},
        {name: 'endDate', type: 'date', mapping: 'startDate', convert: function(val, rec) { return Ext.Date.add(rec.data.startDate, Ext.Date.DAY, 1); } },
        'roomsAvailable'
    ]
});