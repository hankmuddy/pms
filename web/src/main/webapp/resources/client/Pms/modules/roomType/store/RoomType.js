Ext.define('Pms.modules.roomType.store.RoomType', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.roomType.model.RoomType',
    alias: 'widget.roomTypeStore',
    url: 'rest/roomType',
    extravailable: true
});