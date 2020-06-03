Ext.define("Pms.modules.otaCalendar.model.PricePerDay", {
    extend: "Ext.data.Model",
    alias: 'widget.otaModel',
    // idProperty: 'id',
    // nameField: 'name',

    fields: [
        'id',
        {
            name: 'date',
            type: 'date',
            dateFormat: 'timestamp',
            submitFormat: 'unix',
            serialize: function(value) {
                return parseInt(value.getTime() / 1000)
            }
        },
        'price',
        'roomsAvailable',
        'adultBedPrice',
        'childBedPrice',
        {
            name: 'calendarService',
            type: 'auto'
        },
        'minStay',
        'maxStay',
        'minStayArrival',
        'otaAllowed'
    ]
});