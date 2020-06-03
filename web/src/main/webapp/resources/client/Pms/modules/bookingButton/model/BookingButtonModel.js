Ext.define("Pms.modules.bookingButton.model.BookingButtonModel", {
    extend: "Ext.data.Model",
    alias: 'widget.bookingButtonModel',
    fields: [
        'id',
        'name',
        'language',
        'color',
        'currency',
        'backgroundColor',
        'keyValues'
//        'title',
//        {
//            name: 'price',
//            serialize: function (value) {
//                return parseInt(value * 100)
//            }
//        },
//        'deprecated',
//        'type',
//        'measure'
    ]
});