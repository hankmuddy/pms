Ext.define('Pms.modules.quota.model.RTVResource', {
    extend: 'Sch.model.Resource',

    idProperty: 'id',
    nameField: 'name',

    fields: [
        'id',
        'name',
        'adults',
        'children',
        'additional',
        {name: 'expanded', defaultValue: false},
        'type',
        'roomTypeId',
        'otaRoomsCount'/*,
        {name: 'roomTypeId', convert: function(val, rec) {
            var type = rec.data.type, v;
            if(type == 'roomType') v = rec.data.id;
            if(type == 'virtualRoom') v = val;
            console.log(type, v)
            return v;
        } }*/
    ]
});

