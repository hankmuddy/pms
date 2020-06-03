Ext.define('Pms.modules.otaCalendar.model.Event', {
    extend: 'Sch.model.Event',
    itemId: 'calendarEventModel',
    startDateField: 'date',
    endDateField: 'endDate',
    resourceIdField: 'room.id',

    fields: [
        {name: 'id', type: 'int', useNull: true},
        {name: 'minStay', type: 'integer'},
        {name: 'minStayArrival', type: 'integer'},
        {name: 'maxStay', type: 'integer'},
        {name: 'closedToDeparture', type: 'boolean'},
        {name: 'otaAllowed', type: 'boolean'},
        {name: 'date', type: 'UTCDate'},
        {name: 'endDate', type: 'UTCDate'},
        {name: 'room', type: 'auto'},
        {name: 'closed', type: 'auto'}
    ],
    belongsTo: [
        {
            name: 'room',
            instanceName: 'roomId',
            model: 'Pms.modules.otaCalendar.store.Resource',
            getterName: 'getRoom',
            setterName: 'setRoom',
            associationKey: 'room'
        }
    ]
});