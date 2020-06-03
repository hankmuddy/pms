Ext.define('Pms.modules.group.store.sourceType', {
    extend: 'Pms.abstract.Store',
    alias: 'widget.sourceType',
    fields: [
        'id', 'enum_id', 'name', 'label'
    ],

    proxyApi: {
        read: '/admin_api/enum_item/getlist'
    },

    extraParams: {enum_id: 18}
});