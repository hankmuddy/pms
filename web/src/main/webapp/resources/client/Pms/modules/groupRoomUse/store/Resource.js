Ext.define('Pms.modules.groupRoomUse.store.Resource', {
    extend: 'Sch.data.ResourceStore',
    model: 'Pms.modules.groupRoomUse.model.Resource',
    alias: 'widget.resourceStore',
    url: 'rest/room',
    autoLoad: false,
    autoSync: false,
    batch: false,
    paging: false,
    pageSize: 500,
    remoteSort: true,
//    restful: true,

    sorters: [ {
        property: 'position',
        direction: 'ASC'
    }],
    batch: false,
    constructor: function (options) {
        var me = this;
//        Ext.apply(me, options || {});
        var proxy = {
            type: 'rest',
            async: false,
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
//            noCache: true
        };
        me.setProxy(proxy);
        Ext.apply(me, options || {});
        me.callParent(arguments);
    },

    filterParams: {},

    listeners: {
        beforeload: function (store, operation, eOpts) {
            if (!operation.params) {
                operation.params = {};
            }
            if (!Ext.Object.isEmpty(this.filterParams)) {
                operation.params.filter = this.filterParams;
            }
            if (!Ext.Object.isEmpty(operation.params)) {
                operation.params = Pms.Ajax.encode(operation.params);
            }
        }
        // load: function(store, records, success) {}
    }
});