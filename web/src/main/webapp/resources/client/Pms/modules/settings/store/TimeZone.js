Ext.define('Pms.modules.settings.store.TimeZone', {
    extend: 'Admin.generic.data.Store',
    alias: 'widget.timeZoneStore',
    fields: ['id', 'name', 'offset'],
    url: 'admin/hotel/timeZones',
    proxy: Ext.create('Ext.data.proxy.Ajax', {
        type: 'rest',
        headers: {'Content-type': 'application/json;charset=UTF-8'},
        url: 'admin/hotel/timeZones',
        reader: Ext.create('Ext.data.reader.Json', {
            root: 'content'
//                totalProperty: 'page.totalCount'
        })
    })
});