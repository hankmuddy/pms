Ext.define('Pms.modules.payment.store.groupPayment', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.payment.model.Payment',
    alias: 'widget.groupPayment',

    groupField: 'bill_id',

    proxyApi: {
        create: 'rest/payment',
        read: 'rest/payment',
        update: 'rest/payment',
        destroy: 'rest/payment'
    },

    extraParams: {sort: {id: 'DESC'}}
});