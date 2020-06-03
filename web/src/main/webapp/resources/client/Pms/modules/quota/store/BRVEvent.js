Ext.define('Pms.modules.quota.store.BRVEvent', {
    extend: 'Sch.data.EventStore',
    model: 'Pms.modules.quota.model.BRVEvent',
    alias: 'widget.brvEventStore',
    url: 'rest/baseRoomValue',

    autoLoad: false,
    autoSync: false,
    batch: false,
    paging: false,
    pageSize: 500, // TODO Think this value over!!!
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
            var me = this;
            if(!Ext.isEmpty(store.resourceStore)) {
                resource = store.resourceStore.nodeStore.data.items,
                    emptyRecords = [];

                for(var i in resource) {
                    for(var date = me.startDate; date <= me.endDate; date = Sch.util.Date.add(date, Sch.util.Date.DAY, 1)) {
                        var event = Ext.Array.findBy(records, function(item, index) {
                            return item.data['room.id'] == resource[i].data.id && item.data.startDate.getTime() == date.getTime();
                        });

                        if(Ext.isEmpty(event)) {
                            emptyRecords.push(
                                Ext.create(me.model, {
                                    'room.id': resource[i].data.id,
                                    startDate: Pms.toUTC(date),
                                    endDate: Pms.toUTC(Ext.Date.add(date, Ext.Date.DAY, 1)),
                                    otaRoomsCount: resource[i].data.otaRoomsCount,
                                    type: resource[i].data.type,
                                    minStay: 0,
                                    minStayArrival: 0,
                                    maxStay: 0,
                                    closed: 'CLOSED',
                                    closedToDeparture: true,
                                    otaAllowed: false
                                })
                            );
                        }
                    }
                }
                store.add(emptyRecords);
            }
        }
    }
});