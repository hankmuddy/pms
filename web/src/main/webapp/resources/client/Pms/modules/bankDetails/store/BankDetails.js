Ext.define('Pms.modules.bankDetails.store.BankDetails', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.bankDetails.model.BankDetails',
    alias: 'widget.bankDetailsStore',
    url: 'rest/bankDetails'
});