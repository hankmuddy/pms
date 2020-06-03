Ext.define('Admin.modules.hotel.Store', {
    extend: 'Admin.generic.data.Store',
    model: 'Admin.modules.hotel.Model',
    proxy: Ext.create('Admin.generic.data.proxy.Ajax', {
        url: 'admin/hotel',
        reader: Ext.create('Admin.generic.data.reader.Json')
    })
});
