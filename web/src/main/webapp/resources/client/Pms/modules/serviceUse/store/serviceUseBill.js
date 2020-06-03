Ext.define("Pms.modules.serviceUse.store.serviceUseBill", {
    extend: "Pms.modules.serviceUse.store.serviceUse",
    model: "Pms.modules.serviceUse.model.serviceUse",
    url: 'rest/baseServiceUse',
    filters: [
        function (rec) {
            return !rec.data.refund;
        }
    ]
});