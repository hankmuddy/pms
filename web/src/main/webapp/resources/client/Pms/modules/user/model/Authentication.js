Ext.define("Pms.modules.user.model.Authentication", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'username', type: 'auto', persist: false},
        {name: 'password', type: 'auto', persist: false},
        {name: 'language', type: 'auto', persist: false},
    ]
});