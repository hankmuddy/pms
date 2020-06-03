Ext.define('Admin.modules.user.Model', {
    extend: 'Admin.generic.data.Model',
    proxy: {
        type: 'rest',
        url: 'admin/authentication',
        reader: Ext.create('Admin.generic.data.reader.JsonOne')
    },
    fields: [
        {label: 'ID', name: 'id', type: 'int', useNull: true, persist: false},
        {label: 'Username', name: 'username', type: 'string'},
        {name: 'online', type: 'boolean', persist: false},
        {name: 'lastLoggedIn', persist: false},
        {label: 'Password', name: 'password', type: 'string'},
        {label: 'Language', name: 'language', type: 'string'},
        {label: 'UserType', name: 'userType', type: 'string'},
        {label: 'Supervisor', name: 'supervisor', serialize: function (val) {
            if (!val) return
            return {id: val, userType: 'managerSupervisor'}
        }, type: 'auto'},
        {label: 'Hotel', name: 'hotel'/*, serialize: function (val) {
         return {id: val}
         }*/, type: 'auto', persist: false}
    ],
    setTitle: function () {
        return this.get('name')
    }
});
