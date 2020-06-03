Ext.define('Pms.modules.payment.store.Payment', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.payment.model.Payment',
    alias: 'widget.paymentStore',
    url: 'rest/payment',
});