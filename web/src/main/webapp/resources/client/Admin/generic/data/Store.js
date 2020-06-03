Ext.define('Admin.generic.data.Store',{
    extend: 'Ext.data.Store',
    pageSize: 100,
    remoteSort: true,
    filterParams: [],
    loadParams: {},
    sorters: [ {
        property: 'createdDate',
        direction: 'DESC'
    }],
    listeners: {
        beforeload: function (store, operation, eOpts) {
            if (!operation.params) {
                operation.params = {};
            }
            if (!Ext.Object.isEmpty(this.loadParams)) {
                operation.params = Ext.clone(this.loadParams.params);
            }
            if (!Ext.Object.isEmpty(this.filterParams) && !store.refresh) {
                if (Ext.isEmpty(operation.params.filter)) operation.params.filter = [];
                for (var i in this.filterParams) {
                    operation.params.filter.push(this.filterParams[i]);
                }
            }
            if (!Ext.Object.isEmpty(operation.params)) {
                operation.params = Admin.generic.Utils.encode(operation.params);
            }
            store.refresh = false;
        }
    }
})

