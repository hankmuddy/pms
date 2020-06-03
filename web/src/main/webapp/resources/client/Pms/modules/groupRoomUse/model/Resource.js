Ext.define('Pms.modules.groupRoomUse.model.Resource', {
    extend: 'Sch.model.Resource',

    idProperty: 'id',
    nameField: 'name',

    fields: [
        'id',
        {name: 'number'},
        {name: 'roomType', type: 'auto', serialize: function (value) {
            return {
                    id: value
                }
            }
        },
        {name: 'accommodation', type: 'auto', serialize: function (value) {
            return {
                    id: value
                }
            }
        },
        'roomUse',
        {name: 'approved', type: 'bool'},
        {name: 'floor', type: 'int'}
    ]
});