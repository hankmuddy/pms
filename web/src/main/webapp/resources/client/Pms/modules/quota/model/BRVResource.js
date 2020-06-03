Ext.define('Pms.modules.quota.model.BRVResource', {
    extend: 'Sch.model.Resource',

    idProperty: 'id',
    nameField: 'name',

    fields: [
        'id',
        'name',
        'adults',
        'children',
        'additional',
        {name: 'expanded', defaultValue: true},
        'type'
    ]
});

