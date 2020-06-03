Ext.define('Pms.modules.groupRoomUse.model.living', {
    extend: 'Ext.data.Model',

    fields: [
        {
            name: 'id',
            type: 'int',
            useNull: true
        },
        {
            name: 'roomType',
            type: 'auto',
            serialize: function (value) {
                return {
                    id: value
                }
            }
        },
        'roomAndBoard',
        'title',
        {
            name: 'type',
            serialize: function () {
                return 'living'
            }
        },
        {
            name: 'price',
            type: 'integer'
        }
    ]
});

