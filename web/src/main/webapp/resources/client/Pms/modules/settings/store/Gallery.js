Ext.define('Pms.modules.settings.store.Gallery', {
    extend: 'Pms.abstract.Store',
    alias: 'widget.galleryStore',
    fields: ['code'],

    constructor: function (options) {
        var me = this;
        me.url = (!Ext.isEmpty(options) && !Ext.isEmpty(options.roomTypeId)) ? 'rest/document/byRoomType/' + options.roomTypeId : 'rest/document/photos';
        me.callParent();
    },
    extravailable: true
});