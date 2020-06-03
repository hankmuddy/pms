Ext.define('Pms.modules.groupRoomUse.model.Event', {
    extend: 'Sch.model.Event',

    startDateField: 'startDate',
    endDateField: 'endDate',
    resourceIdField: 'room.id',
    nameField: 'useType',
    idProperty: 'id',

    fields: [
        'id',
        {
            name: 'customerGroup',
            type: 'auto',
            serialize: function (value) {
                return {customer: value}
            }
        },
        'description',
        'comment',
        'status',
        {
            name: 'room',
            serialize: function (value) {
                return {id: value}
            }
        },
        {name: 'plan'},
        {name: 'moved'},
        {name: 'movedFromId'},
        {name: 'type', persist: false},
        {name: 'source', persist: false},
        {name: 'total', type: 'int'},
        {name: 'totalPaid', type: 'int'},
        {name: 'baseRoom'},
        {name: 'checkInTime'},
        {name: 'checkOutTime'},
        {name: 'date', type: 'date', dateFormat: 'timestamp'},
        {name: 'startDate', type: 'UTCDate', serialize: Pms.serializeUTC},//type: 'date', dateFormat: 'timestamp'},
        {name: 'endDate', type: 'UTCDate', serialize: Pms.serializeUTC}//type: 'date', dateFormat: 'timestamp'},
    ],
    belongsTo: [
        {
            name: 'room',
            instanceName: 'roomId',
            model: 'Pms.modules.room.model.Room',
            getterName: 'getRoom',
            setterName: 'setRoom',
            associationKey: 'room'
        }
    ]
});