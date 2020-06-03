Ext.define('Admin.modules.hotel.timezone.Store', {
    extend: 'Admin.generic.data.Store',
    fields: ['id', 'name', 'offset'],
    url: 'admin/hotel/timeZones',
    proxy: Ext.create('Admin.generic.data.proxy.Ajax', {
        type: 'rest',
        url: 'admin/hotel/timeZones',
        reader: Ext.create('Ext.data.reader.Json', {
            root: 'content'
//                totalProperty: 'page.totalCount'
        })
    })
});
