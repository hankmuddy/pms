Ext.define('Pms.modules.groupRoomUse.store.bookingRoomUse', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.groupRoomUse.model.booking',
    alias: 'widget.bookingRoomUseStore',
    url: 'rest/roomUse'
});