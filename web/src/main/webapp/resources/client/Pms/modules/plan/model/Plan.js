Ext.define('Pms.modules.plan.model.Plan', {
//    extend: 'Ext.data.Model',
    extend: 'Sch.model.Resource',

    idProperty: 'id',
    nameField: 'name',

    fields: [
        'id',
        'name',
        'board',
        {name: 'approved', type: 'boolean'},
        {name: 'defaultPlan', type: 'boolean'}
    ]
});

