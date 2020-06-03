Ext.define('Admin.modules.profile.Model', {
    // extend: 'Ext.data.Model',
    extend: 'Admin.modules.user.Model',
    proxy: {
        type: 'rest',
        url: 'admin/authentication',
        reader: Ext.create('Admin.generic.data.reader.JsonOne')
    },
//    fields: [
//        {label: l('common.id'), name: 'id', type: 'int', useNull: true},
//        {label: l('user.nickname'), name: 'nickname', type: 'string'},
//        {label: l('common.password'), name: 'password', type: 'string'},
//        {label: l('common.loginName'), name: 'loginName', type: 'string'},
//        {label: l('user.role'), name: 'role', type: 'string'},
//        {label: l('user.supervisor'), name: 'supervisor', type: 'auto'},
//    ],
});
