Ext.define("Pms.modules.group.model.Group", {
    extend: "Ext.data.Model",
    fields: [
        'id',
        'company',
        'customer',
        'discount',
        'includeCustomer',
        'pov',
        'roomUsesQuantity',
        {name: 'total', type: 'int'},
        {name: 'totalPaid', type: 'int'},
    ]
});

