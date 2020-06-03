Ext.define("Pms.modules.room.model.Room", {
    extend: "Ext.data.Model",
    alias: 'widget.roomModel',

    fields: [
        'id',
        {name: 'number'},
        {
            name: 'roomType',
            type: 'auto',
            serialize: function (value,rec) {
                if(rec.data.approved) return
                return {
                    id: value,
                    type: 'roomType'
                }
            }
        },
        {
            name: 'accommodation',
            type: 'auto',
            serialize: function (value) {
                return {
                    id: value
                }
            }
        },
        'roomUse',
        'baseRoom',
        {name: 'approved', type: 'bool'},
        {name: 'floor', type: 'int'}
    ],

    associations: [
        {
            type: 'belongsTo',
            model: 'Pms.modules.roomType.model.RoomType',
            name: 'room_type',
            associationKey: 'room_type',
            primaryKey: 'id',
            foreignKey: 'rooms'
        },
        {
            type: 'belongsTo',
            model: 'Pms.modules.accommodation.model.Accommodation',
            name: 'accommodation',
            associationKey: 'accommodation',
            primaryKey: 'accommodationId',
            foreignKey: 'id'
        },
        {
            type: 'hasMany',
            model: 'Pms.modules.room_use.model.RoomUse',
            name: 'room_uses',
            associationKey: 'room_uses',
            primaryKey: 'id',
            foreignKey: 'room'
        }
    ]
});