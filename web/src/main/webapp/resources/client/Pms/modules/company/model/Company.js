Ext.define('Pms.modules.company.model.Company', {
    extend: 'Ext.data.Model',
    fields: [
        'id',
        'name',
        'ownershipType',
        'country',
        'city',
        'email',
        'phone',
        'inn',
        'registrationAddress',
        'listMembership',
        'membershipReason',
        {
            name: 'documents',
            type: 'auto',
            serialize: function (val) {
                if (!val) return []; else return val;
            }
        },
        'bankName',
        'bankMfo',
        'accountNumber',
        'legalAddress',
        'postAddress',
        'discount'
    ]
});

