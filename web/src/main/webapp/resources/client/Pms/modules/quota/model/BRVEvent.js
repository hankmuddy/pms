Ext.define('Pms.modules.quota.model.BRVEvent', {
    extend: 'Sch.model.Event',

    startDateField: 'startDate',
    endDateField: 'endDate',
    resourceIdField: 'room.id',
    nameField: 'type',
    idProperty: 'id',

    fields: [
        'id',
        {name: 'room.id', type: 'auto', mapping: 'room.id'},
//        {name: 'roomId', type: 'auto', mapping: 'room.id'},
        {name: 'startDate', type: 'UTCDate', mapping: 'date'},
        {name: 'endDate', type: 'date', mapping: 'startDate', convert: function(val, rec) { return Ext.Date.add(rec.data.startDate, Ext.Date.DAY, 1); } },
        {name: 'otaRoomsCount', type: 'auto', mapping: 'room.roomType.otaRoomsCount'},
        {name: 'type', type: 'auto', mapping: 'room.type'},
        {name: 'roomTypeId', type: 'auto', mapping: 'room.roomType.id'},
        'minStay', // int
        'minStayArrival', // int
        'maxStay', // int
        'closed', // 0/1/2
        {name: 'closedToDeparture', type: 'boolean'},
        {name: 'otaAllowed', type: 'boolean'}
    ],
//    belongsTo: [
//        {
//            name: 'roomType',
//            instanceName: 'roomType',
//            model: 'Pms.modules.roomType.model.RoomType',
//            getterName: 'getRoomType',
//            setterName: 'setRoomType',
//            associationKey: 'roomType'
//        }
//    ]
});