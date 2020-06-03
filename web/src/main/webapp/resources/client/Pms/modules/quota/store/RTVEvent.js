Ext.define('Pms.modules.quota.store.RTVEvent', {
    extend: 'Sch.data.EventStore',
    model: 'Pms.modules.quota.model.RTVEvent',
    alias: 'widget.rtvEventStore',
    url: 'rest/roomTypeValue',

    autoLoad: false,
    autoSync: false,
    batch: false,
    paging: false,
    pageSize: 500, // TODO Think this value over!!!
    remoteSort: true,
    restful: true,

    sorters: [
        {
            property: 'date',
            direction: 'ASC'
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

    startDate: Ext.Date.clearTime(new Date()),
    endDate: Sch.util.Date.add(Ext.Date.clearTime(new Date()), Sch.util.Date.DAY, 60),

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
        },
        load: function(store, records, success) {
            var me = this,
                resource = store.resourceStore.data.items,
                emptyRecords = [];

            for (var i in resource) {
                for(var date = me.startDate; date <= me.endDate; date = Sch.util.Date.add(date, Sch.util.Date.DAY, 1)) {
                    var event = Ext.Array.findBy(records, function(item, index) {
                        return item.data.roomTypeId == resource[i].data.roomTypeId && item.data.startDate.getTime() == date.getTime();
                    });
                    if(Ext.isEmpty(event)) {
                        emptyRecords.push(
                            Ext.create(me.model, {
                                'roomType.id': resource[i].data.id,
                                roomTypeId: resource[i].data.id,
                                startDate: Pms.toUTC(date),
                                endDate: Pms.toUTC(Ext.Date.add(date, Ext.Date.DAY, 1)),
                                roomsAvailable: resource[i].data.otaRoomsCount
                            })
                        );
                    }
                }
            }
            store.add(emptyRecords);
        }
    }
});