Ext.define("Pms.modules.living.model.Living", {
    extend: "Ext.data.Model",

    fields: [
        'id',
        'room',// {name: 'room', type: 'auto', serialize: function (value) {if(!Ext.isEmpty(value)) return {id: value}}},
        'plan',// {name: 'plan', type: 'auto', serialize: function (value) {if(!Ext.isEmpty(value)) return {id: value}}},
        'season',// {name: 'season', type: 'auto', serialize: function (value) {if(!Ext.isEmpty(value)) return {id: value}}},
        Pms.money('mon'),
        Pms.money('tue'),
        Pms.money('wed'),
        Pms.money('thu'),
        Pms.money('fri'),
        Pms.money('sat'),
        Pms.money('sun'),
        'type'
    ]
});

