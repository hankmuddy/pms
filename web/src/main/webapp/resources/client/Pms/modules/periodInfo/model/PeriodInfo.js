Ext.define("Pms.modules.periodInfo.model.PeriodInfo", {
    extend: "Ext.data.Model",
    fields: [
        {name: 'id', type: 'int', useNull: true},
        {
            name: 'dateStart',
            type: 'UTCDate',
            serialize: Pms.serializeUTC
        },
        {
            name: 'livingPrice',
            type: 'integer',
            serialize: function (value) {
                return parseInt(value * 100)
            }
        },
        {
            name: 'roomType',
            type: 'auto',
            serialize: function (val) {
                return {id: val}
            }
        }
    ]
});