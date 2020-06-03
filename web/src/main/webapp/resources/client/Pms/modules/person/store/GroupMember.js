Ext.define('Pms.modules.person.store.GroupMember', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.person.model.GroupMember',
    alias: 'widget.groupMemberStore',
    url: 'rest/groupMember'
//    filters: [
//        function (item) {
//            //Show only members without roomUse
//            return (!item.data.roomUse);
//        }
//    ]
});