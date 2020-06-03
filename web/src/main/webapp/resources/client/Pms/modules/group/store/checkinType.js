Ext.define('Pms.modules.group.store.checkinType', {
    extend: 'Pms.abstract.Store',
    alias: 'widget.checkinType',
    fields: [
        'id', 'enum_id', 'name', 'label'
    ],

    proxyApi: {
        read: '/admin_api/enum_item/getlist',
    },

    extraParams: {enum_id: 19}
});