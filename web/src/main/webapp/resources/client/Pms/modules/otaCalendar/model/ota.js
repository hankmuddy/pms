Ext.define("Pms.modules.otaCalendar.model.ota", {
    extend: "Ext.data.Model",
    alias: 'widget.otaModel',

    fields: [
        {
            name: 'id', type: 'int', useNull: true
        },
        {
            name: 'minStay', type: 'integer'
        },
        {
            name: 'minStayArrival', type: 'integer'
        },
        {
            name: 'maxStay', type: 'integer'
        },
        {
            name: 'closedToDeparture', type: 'boolean'
        },
        {
            name: 'otaAllowed', type: 'boolean',
            serialize: function (val) {
                return !val
            }
        },
        {
            name: 'date', type: 'UTCDate', serialize: Pms.serializeUTC
        },
        {
            name: 'endDate',
            type: 'date',
            persist: false
        },
        {
            name: 'rooms',
            type: 'auto',
//            serialize: function (val) {
//                return {id: val}
//            }
        },
        {
            name: 'room',
            type: 'auto'
        },
        {name: 'closed', type: 'string', serialize: function (val) {
            if (val == "") return null
            return val
        }}
    ]
});