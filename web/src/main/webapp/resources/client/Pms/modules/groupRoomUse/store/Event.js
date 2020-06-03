Ext.define('Pms.modules.groupRoomUse.store.Event', {
    extend: 'Sch.data.EventStore',
    model: 'Pms.modules.groupRoomUse.model.Event',
    alias: 'widget.eventStore',
    url: 'rest/roomUse',

    autoLoad: false,
    autoSync: false,
    batch: false,
    paging: false,
    pageSize: 500,//TODO Think this value over!!!
    remoteSort: true,
    restful: true,

    sorters: [
        {
            property: 'updatedDate',
            direction: 'DESC'
        }
    ],

    constructor: function (options) {
        var me = this;
        Ext.apply(me, options || {});
        me.proxy = {
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
            },
            noCache: true
        };

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