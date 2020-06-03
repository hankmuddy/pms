Ext.define('Pms.modules.groupRoomUse.store.livingRoomUse', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.groupRoomUse.model.living',
    alias: 'widget.livingRoomUseStore',
    url: 'rest/living',

//    proxyApi: {
//        read: '/api/group_room_use/getByUseType/json'
//    }
});