Ext.define('Pms.modules.quota.store.BRVResource', {
    extend: 'Sch.data.ResourceTreeStore',
    model: 'Pms.modules.quota.model.BRVResource',
    alias: 'widget.brvResourceStore',
    url: 'rest/baseRoom',

    autoLoad: false,
    autoSync: false,
    batch: false,
    paging: false,
    remoteSort: true,

    sorters: [{
        property: 'date',
        direction: 'ASC'
    }],
    defaultRootId: 'content',
    defaultRootProperty: 'content',
    folderSort: false,
    root: {
        id: 'tree',
        expanded: true
    },

    constructor: function (options) {
        var me = this;
        Ext.apply(me, options || {});
        var proxy = {
            type: 'rest',
            url: me.url,
            reader: Ext.create('Ext.data.reader.Json', {
                root: 'content'
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