Ext.define('Pms.abstract.Store', {
    extend: 'Ext.data.Store',
    autoLoad: false,
    autoSync: false,
    paging: true,
    pageSize: 50,
    remoteSort: true,
    restful: true,
    refresh: false,
    filterParams: [],
    loadParams: {},

    extravailable: false,
    pmsavailable: true,

    sorters: [
        {
            property: 'updatedDate',
            direction: 'DESC'
        }
    ],

    onCreateRecords: function (records, operation, success) {
        if (success) {

            Pms.App.showNotification({
                message: l('saveSuccess.msg'),
                icon: Pms.notificationOk
            });
        } else {
            Pms.App.showNotification({
                message: l('saveError.msg')
            });
        }
    },

    onUpdateRecords: function (records, operation, success) {
        if (success) {
            Pms.App.showNotification({
                message: l('saveSuccess.msg'),
                icon: Pms.notificationOk
            });
        } else {
            Pms.App.showNotification({
                message: l('saveError.msg')
            });
        }
    },

    onDestroyRecords: function (records, operation, success) {
        if (success) {
            Pms.App.showNotification({
                message: l('deleteSuccess.msg'),
                icon: Pms.notificationOk
            });
        } else {
            Pms.App.showNotification({
                message: l('deleteError')
            });
        }
    },

    listeners: {
        beforeload: function (store, operation, eOpts) {
            if (Pms.App.isExtranet() === true && store.extravailable === false || Pms.App.isExtranet() === false && store.pmsavailable === false) {
                return false;
            }
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
        }
    },

    constructor: function (options) {
        var me = this;
        Ext.apply(me, options || {});


        me.proxy = {
            type: 'rest',
            url: me.url,
            reader: Ext.create('Ext.data.reader.Json', {
                root: 'content',
                totalProperty: 'page.totalCount'
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