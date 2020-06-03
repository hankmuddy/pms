Ext.define('Admin.modules.profile.Store', {
    extend: 'Admin.generic.data.Store',
    model: 'Admin.modules.profile.Model',
    proxy: Ext.create('Admin.generic.data.proxy.Ajax', {
        url: 'admin/authentication',
        reader: Ext.create('Admin.generic.data.reader.Json')
    })
});
