Ext.define('Pms.modules.roomType.store.BaseRoom', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.roomType.model.RoomType',
    alias: 'widget.baseRoomStore',
    url: 'rest/baseRoom',
    extravailable: true
});