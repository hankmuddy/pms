Ext.define('Pms.modules.accommodation.model.Accommodation', {
    extend: 'Ext.data.Model',
    fields: [
        {
            name: 'id',
            useNull: true
        },
        'name',
        'shortName',
        'approved'
    ]
});