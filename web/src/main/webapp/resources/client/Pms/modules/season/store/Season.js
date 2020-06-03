Ext.define('Pms.modules.season.store.Season', {
    extend: 'Sch.data.EventStore',
    model: 'Pms.modules.season.model.Season',
    alias: 'widget.seasonStore',
    url: 'rest/season',

    extravailable: true,

    autoLoad: false,
    autoSync: false,
    paging: false,
    remoteSort: true,
    restful: true,

    constructor: function (options) {
        var me = this;
        Ext.apply(me, options || {});
        me.proxy = {
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
    }
});