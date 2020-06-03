Ext.define('Admin.modules.user.Store', {
    extend: 'Admin.generic.data.Store',
    model: 'Admin.modules.user.Model',
    proxy: Ext.create('Admin.generic.data.proxy.Ajax', {
        url: 'admin/authentication',
        reader: Ext.create('Admin.generic.data.reader.Json')
    })
});
