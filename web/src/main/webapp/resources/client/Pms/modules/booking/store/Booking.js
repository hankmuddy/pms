Ext.define('Pms.modules.booking.store.Booking', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.booking.model.Booking',
    alias: 'widget.bookingStore',
    url: 'rest/roomUse'
});