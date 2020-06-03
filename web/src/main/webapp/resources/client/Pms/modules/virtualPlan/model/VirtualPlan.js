Ext.define("Pms.modules.virtualPlan.model.VirtualPlan", {
    extend: "Ext.data.Model",

    fields: [
        {name:'id', type: 'int', useNull: true, persist: false},
        {name:'plan', type: 'auto', serialize: function(value){
            return {id: value}
        }},// {name: 'room', type: 'auto', serialize: function (value) {if(!Ext.isEmpty(value)) return {id: value}}},
        'name',// {name: 'plan', type: 'auto', serialize: function (value) {if(!Ext.isEmpty(value)) return {id: value}}},
        'variation',// {name: 'season', type: 'auto', serialize: function (value) {if(!Ext.isEmpty(value)) return {id: value}}},
        {name: 'value', type: 'int',serialize: function(val){
            return val * 100
        }},
        {name: 'approved', type: 'boolean'},
        {name: 'plan.id', type: 'int', persist: false}
    ]
});

