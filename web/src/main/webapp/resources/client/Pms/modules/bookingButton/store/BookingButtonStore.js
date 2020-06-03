Ext.define("Pms.modules.bookingButton.store.BookingButtonStore", {
    extend: "Pms.abstract.Store",
    model: "Pms.modules.bookingButton.model.BookingButtonModel",
    alias: 'widget.bookingButtonStore',
    url: 'rest/bbs'
//    filters: [
//        function (item) {
//            var checkBox = Ext.ComponentQuery.query('checkbox[name=showDeleted]')[0],
//                showDeleted = checkBox ? checkBox.value : false,
//                showItem = showDeleted ? true : !item.data.deprecated;
//
//            return (item.data.title != 'service.earlyCheckIn' && item.data.title != 'service.lateCheckOut' && showItem);
//        }
//    ]
});