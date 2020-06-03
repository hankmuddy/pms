Ext.define('Pms.modules.otaCalendar.store.Resource', {
    extend: 'Sch.data.ResourceStore',
    model: 'Pms.modules.otaCalendar.model.Resource',
    alias: 'widget.otaCalendarResourceStore',
    url: 'rest/baseRoom',
    autoLoad: false,
    autoSync: false,
    batch: false,
    paging: false,
    pageSize: 50,
    remoteSort: true,

    constructor: function (options) {
        var me = this;
        var proxy = {
            type: 'rest',
            url: me.url,
            reader: Ext.create('Ext.data.reader.Json', {
                root: 'content'
//                totalProperty: 'page.totalCount'
            }),
            writer: {
                type: 'json'
            },
            actionMethods: {
                create: 'POST',
                read: 'GET',
                update: 'PUT',
                destroy: 'DELETE'
            }
        };

        me.setProxy(proxy);
        Ext.apply(me, options || {});
        me.callParent(arguments);
    }
});