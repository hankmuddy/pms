Ext.define('Pms.modules.analytics.store.Analytics', {
    extend: 'Ext.data.Store',
    model: 'Pms.modules.analytics.model.Analytics',
    alias: 'widget.analyticsStore',
    sorters: [
        {
            property: 'id',
            direction: 'ASC'
        }
    ],
    filterParams: [],
    loadParams: {},
    pageSize: 1000,
    listeners: {
        beforeload: function (store, operation, eOpts) {
            if (!operation.params) {
                operation.params = {};
            }
            if (!Ext.Object.isEmpty(this.loadParams)) {
                operation.params = Ext.ux.clone(this.loadParams.params);
            }
            if (!Ext.Object.isEmpty(this.filterParams) && !store.refresh) {
                if (Ext.isEmpty(operation.params.filter)) operation.params.filter = [];
                for (var i in this.filterParams) {
                    operation.params.filter.push(this.filterParams[i]);
                }
            }
            if (!Ext.Object.isEmpty(operation.params)) {
                operation.params = Pms.Ajax.encode(operation.params);
            }
            store.refresh = false;
        },
    },

    constructor: function (options) {
        var me = this;
        Ext.apply(me, options || {});


        me.proxy = {
            type: 'rest',
            url: me.url,
            reader: Ext.create('Pms.abstract.Reader', {
                parseResponseData: me.parseResponseData
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
    }
});