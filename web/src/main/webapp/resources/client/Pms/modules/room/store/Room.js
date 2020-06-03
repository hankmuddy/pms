Ext.define('Pms.modules.room.store.Room', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.room.model.Room',
    alias: 'widget.roomStore',
    url: 'rest/room',
    extraParams: {
        sort: {
            property_name: 'ASC'
        }
    },
    sorters: [
        {
            property: 'position',
            direction: 'ASC'
        }
    ],
});