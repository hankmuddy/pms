Ext.define('Pms.modules.bill.store.Bill', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.bill.model.Bill',
    alias: 'widget.billStore',
    url: 'rest/bill',

//    extraParams: {
//        sort: {
//            id: 'DESC'
//        }
//    }
});