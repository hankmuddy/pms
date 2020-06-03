Ext.define('Pms.modules.otaCalendar.model.Resource', {
    extend: 'Sch.model.Resource',
    idProperty: 'id',
    nameField: 'name',
    fields: [
        {
            name: 'id',
            type: 'int',
            useNull: true
        },
        'name',
        {
            name: 'adults',
            type: 'integer'
        },
        {
            name: 'defaultPrice',
            serialize: function (value) {
                return parseInt(value * 100)
            }
        },
        {name: 'children', type: 'integer'},
        {name: 'shortname', type: 'auto'},
        {name: 'additional', type: 'integer'},
        {name: 'approved', persist: false},
        {name: 'virtualRooms', type: 'auto', persist: false},
        {name: 'virtualRoom', type: 'auto'},
        {name: 'roomType', type: 'auto', persist: false}
    ]
});