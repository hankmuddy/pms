Ext.define("Pms.modules.serviceUse.store.serviceUse", {
    extend: "Pms.abstract.Store",
    model: "Pms.modules.serviceUse.model.serviceUse",
    alias: 'widget.serviceUseStore',
    groupField: 'bill.id',
    url: 'rest/serviceUse'
});