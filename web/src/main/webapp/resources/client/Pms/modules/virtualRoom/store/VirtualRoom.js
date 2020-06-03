Ext.define('Pms.modules.virtualRoom.store.VirtualRoom', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.virtualRoom.model.VirtualRoom',
    alias: 'widget.virtualRoomStore',
    url: 'rest/virtualRoom',
    extravailable: true
});