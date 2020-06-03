Ext.define('Pms.modules.groupRoomUse.model.groupBooking', {
    extend: 'Ext.data.Model',

    fields: [
        'id',
        {name: 'roomUses', type: 'array'},
        {name: 'group', type: 'auto'}
    ]
});