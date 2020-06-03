Ext.define('Pms.modules.contact.model.Contact', {
    extend: 'Ext.data.Model',
    fields: [
        'id',
        {
            name: 'company',
            type: 'auto',
            serialize: function (value) {
                return {
                    id: value
                }
            }
        },
        'name',
        'email',
        'phone',
        'description',
        'post'
    ],
    associations: [
        {
            type: 'belongsTo',
            model: 'Pms.modules.company.model.Company',
            name: 'company',
            associationKey: 'company',
            primaryKey: 'id',
            foreignKey: 'contacts'
        }
    ]
});

