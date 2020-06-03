Ext.define('Pms.modules.groupRoomUse.store.refuseRoomUse', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.groupRoomUse.model.refuse',
    alias: 'widget.refuseRoomUseStore',
    url: 'rest/room'
//    proxyApi: {
//        read: '/api/group_room_use/getByUseType/json'
//    }
});