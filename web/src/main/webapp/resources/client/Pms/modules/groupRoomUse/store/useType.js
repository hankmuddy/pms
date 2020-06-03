Ext.define("Pms.modules.groupRoomUse.store.useType", {
    extend: "Pms.abstract.Store",
    alias: 'widget.useTypeStore',
    fields: [
        'id',
        'enum_id',
        'name',
        'label'
    ],

//    proxyApi: {
//        read: '/admin_api/enum_item/getlist',
//    },

    extraParams: {
        enum_id: 7
    },

    bookingFree: true,
    bookingWarranty: true,
    living: true,
    refuse: true,
    outgo: true,
    repair: true,

    listeners: {
        load: function (store, records, success) {
            var displayOpts = [];
            if (!store.bookingFree) displayOpts.push(36);
            if (!store.bookingWarranty) displayOpts.push(37);
            if (!store.living) displayOpts.push(38);
            if (!store.outgo) displayOpts.push(39);
            if (!store.repair) displayOpts.push(40);
            if (!store.refuse) displayOpts.push(41);
            if (displayOpts.length > 0) {
                for (var i in records) {
                    var useTypeId = records[i].data.id;
                    if (Ext.Array.contains(displayOpts, useTypeId)) {
                        store.remove(records[i]);
                        delete records[i];
                    }
                }
                ;
            }
        }
    }
});