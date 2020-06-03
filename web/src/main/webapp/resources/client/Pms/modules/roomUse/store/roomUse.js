Ext.define('Pms.modules.roomUse.store.roomUse', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.roomUse.model.roomUse',
    alias: 'widget.roomUseStore',
    url: Pms.App.isExtranet() ? 'rest/extranetRoomUse' : 'rest/roomUse',
    extravailable: true
});