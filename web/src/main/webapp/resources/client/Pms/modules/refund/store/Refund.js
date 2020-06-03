Ext.define('Pms.modules.refund.store.Refund', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.refund.model.Refund',
    alias: 'widget.refundStore',
    url: 'rest/refund',
    extraParams: {
        sort: {
            id: 'DESC'
        }
    }
});