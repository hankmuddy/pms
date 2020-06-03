Ext.define('Pms.modules.otaCalendar.store.Event', {
    extend: 'Sch.data.EventStore',
    model: 'Pms.modules.otaCalendar.model.Event',
    alias: 'widget.otaCalendarEventStore',
    url: 'rest/baseRoomValue',
    autoLoad: false,
    autoSync: false,
    batch: false,
    paging: false,
    pageSize: 50,
//    buffered: true,
//    remoteSort: true,
//    restful: true,

//    sorters: [
//        {
//            property: 'createdDate',
//            direction: 'ASC'
//        }
//    ],

    constructor: function (options) {
        var me = this;
        Ext.apply(me, options || {});
        me.proxy = {
            type: 'rest',
            url: me.url,
            async: false,
            reader: Ext.create('Ext.data.reader.Json', {
                root: 'content',
//                totalProperty: 'page.totalCount'
            }),
            writer: {
                type: 'json',
            },
            simpleSortMode: true,
            simpleGroupMode: true,
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
        },
        groupchange: function (store, groupers) {
            var sortable = !store.isGrouped(),
                headers = grid.headerCt.getVisibleGridColumns(),
                i, len = headers.length;

            for (i = 0; i < len; i++) {
                headers[i].sortable = (headers[i].sortable !== undefined) ? headers[i].sortable : sortable;
            }
        },

        // This particular service cannot sort on more than one field, so if grouped, disable sorting
        beforeprefetch: function (store, operation) {
            if (operation.groupers && operation.groupers.length) {
                delete operation.sorters;
            }
        }
    }
});