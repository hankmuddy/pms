Ext.define('Pms.modules.groupRoomUse.model.booking', {
    extend: 'Ext.data.Model',

    fields: [
        'id',
        'description',
        'comment',
        {
            name: 'room',
            type: 'auto',
            serialize: function (value) {
                return {id: value}
            }
        },
        {
            name: 'baseRoom',
            type: 'auto',
//            serialize: function (value,rec) {
//                debugger
//                return {id: value, type: value.type}
//            }
        },
        {
            name: 'plan',
            type: 'auto',
            serialize: function (value) {
                return {id: value}
            }
        },
        {
            name: 'customerGroup',
            type: 'auto'
        },
        {
            name: 'startDate',
            type: 'date',
            dateFormat: 'timestamp',
            submitFormat: 'unix', serialize: function (value) {
            return parseInt(value.getTime() / 1000)
        }
        },
        {
            name: 'endDate',
            type: 'date',
            dateFormat: 'timestamp',
            submitFormat: 'unix',
            serialize: function (value) {
                return parseInt(value.getTime() / 1000)
            }
        },
        {
            name: 'upgrade',
            type: 'boolean',
            persist: false
        },
        {
            name: 'total',
            type: 'int',
            persist: false
        }, {
            name: 'totalPaid',
            type: 'int',
            persist: false
        },
        {
            name: 'status',
            type: 'string',
            persist: false
        },
        {
            name: 'source',
            type: 'string'
        },
        Pms.stringFieldUseNull('rcode'),
        {
            name: 'customerPays',
            type: 'boolean'
//            persist: false
        }
    ]
});

