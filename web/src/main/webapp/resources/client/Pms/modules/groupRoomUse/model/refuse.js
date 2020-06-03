Ext.define('Pms.modules.groupRoomUse.model.refuse', {
    extend: 'Ext.data.Model',

    fields: [
        'id',
        'description',
        'group',
        'rate',
        'rate_policy',
        'room',
        {
            name: 'startDate',
            type: 'date',
            dateFormat: 'timestamp'
        },
        {
            name: 'endDate',
            type: 'date',
            dateFormat: 'timestamp'
        },
        'use_type'
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

