Ext.define('Pms.modules.payment.store.moneyType', {
    extend: 'Pms.abstract.Store',
    alias: 'widget.moneyTypeStore',
    fields: [
        'name', 'label'
    ],
    data: [
        {"label": l('paymentType.CASH'), "name": "CASH"},
        {"label": l('paymentType.CARD'), "name": "CARD"},
        {"label": l('paymentType.TRANSFER'), "name": "TRANSFER"},
        {"label": l('paymentType.INTERNATIONAL_ACCOUNT'), "name": "INTERNATIONAL_ACCOUNT"}
    ]
//    queryMode: 'local',
//    url:'',
//    proxyApi: {
//        read: '/admin_api/enum_item/getlist',
//    },

//    extraParams: {enum_id: 13}
});