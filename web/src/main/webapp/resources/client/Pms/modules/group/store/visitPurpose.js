Ext.define('Pms.modules.group.store.visitPurpose', {
    extend: 'Pms.abstract.Store',
    alias: 'widget.visitPurpose',
    fields: [
        'id',
        'enum_id',
        'name',
        'label'
    ],

    proxyApi: {
        read: '/admin_api/enum_item/getlist'
    },

    extraParams: {enum_id: 17}
});