Ext.define("Pms.modules.role.model.Role", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'id', type: 'int', useNull: true},
        {name: 'name', type: 'auto', convert: function(value){
            return l(value)
        }},
        {name: 'permissions', type: 'array', serialize: function () {
            return []
        }}
    ]
});