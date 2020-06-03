Ext.define('Pms.modules.roomUse.model.roomUse', {
    extend: 'Ext.data.Model',

    fields: [
        'id',
        'active',
        'approved',
        'bill',
        'customerGroup',
        'description',
        'baseRoom',
        'plan',
        'room',
        {name: 'total', type: 'int'},
        {name: 'totalPaid', type: 'int'},
        {
            name: 'createdDate',
            type: 'UTCDate',
            serialize: Pms.serializeUTC
        },
        {
            name: 'startDate',
            type: 'UTCDate',
            serialize: Pms.serializeUTC
        },
        {
            name: 'endDate',
            type: 'UTCDate',
            serialize: Pms.serializeUTC
        },
        'status',
        'source',
        'useType',
        'checkInTime',
        'checkOutTime'
    ],

    associations: [
        {
            type: 'belongsTo',
            model: 'Pms.modules.room.model.Room',
            name: 'room',
            associationKey: 'room',
            primaryKey: 'id',
            foreignKey: 'room_uses'
        },
        {
            type: 'belongsTo',
            model: 'Pms.modules.group.model.Group',
            name: 'group',
            associationKey: 'group',
            primaryKey: 'id',
            foreignKey: 'rooms_use'
        }
    ]
});

