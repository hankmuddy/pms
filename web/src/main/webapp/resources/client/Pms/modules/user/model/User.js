Ext.define("Pms.modules.user.model.User", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'id', type: 'int', useNull: true, persist: false},
        {name: 'username', type: 'auto'},
//        {name: 'password', type: 'auto', persist: false},
//        {name: 'language', type: 'auto', persist: false},
        {name: 'role', type: 'auto',serialize: function(val){
            return {id: val}
        }},
        {name: 'email', type: 'auto'},
        {name: 'firstName', type: 'auto'},
        {name: 'lastName', type: 'auto'},
        {name: 'language', type: 'auto'},
        {name: 'patronymic', type: 'auto'},
        {name: 'address', type: 'auto'},
        {name: 'phone', type: 'auto'},
        {name: 'position', type: 'auto'},
        {name: 'language', type: 'auto'}
    ]
});