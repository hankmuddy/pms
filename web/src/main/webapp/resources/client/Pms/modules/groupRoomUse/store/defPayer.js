Ext.define('Pms.modules.groupRoomUse.store.defPayer', {
    extend: 'Pms.abstract.Store',
    alias: 'widget.defPayerStore',
    fields: [
        'id', 'enum_id', 'name', 'label'
    ],

//    proxyApi: {
//        read: '/admin_api/enum_item/getlist',
//    },

    extraParams: {enum_id: 8}
});