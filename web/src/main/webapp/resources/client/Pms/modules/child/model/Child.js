Ext.define("Pms.modules.child.model.Child", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'id', type: 'int', useNull: true},
        'firstName',
        'lastName',
        'patronymic',
        {
            name: 'dob',
            type: 'UTCDate',
            serialize: Pms.serializeUTC
        },
        {name: 'type', defaultValue: 'child'},
        'identity'
    ]
});

