Ext.define('Pms.modules.settings.view.imgDataView', {
    extend: 'Ext.view.View',
    alias: 'widget.imgDataView',
//    requires: ['Ext.ux.DataView.Draggable'],

    cls: 'gallery',
    autoScroll: true,
    multiSelect: true,
    trackOver: true,
    overItemCls: 'x-item-over',
    itemSelector: 'div.thumb-wrap',
    emptyText: l('photo.noPhotos'),
//    height: 200,
    tpl: [
        '<tpl for=".">',
        '<div class="thumb-wrap">',
        '<div class="thumb"><img src="' + _('imagesUrlPrefix') + '{code}" /></div>',
//                '<div class="thumb"><img src="/rest/document/photo/{code}" /></div>',
        '</div>',
        '</tpl>',
        '<div class="x-clear"></div>'
    ],

//    plugins: [Ext.create('Ext.ux.DataView.DragSelector', {})],
//    mixins: {
//        draggable: 'Ext.ux.DataView.Draggable'
//    },
//    initComponent: function() {
//        this.mixins.draggable.init(this, {
//            ddConfig: {
//                ddGroup: 'galleryGroup'
//            }
//        });
//
//        this.callParent(arguments);
//    },
    listeners: {
//        itemclick: function (dataview, record, item, index, e, eOpts) {
//            var img = dataview.up('gallery').down('image');
//            if(!Ext.isEmpty(record)) img.setSrc('/rest/document/photo/' + record.data.code);
//        },
//        itemcontextmenu: function (dataview, rec, item, index, e, eOpts) {
//            var me = this;
//
//            e.stopEvent();
//            dataview.ctx = new Ext.menu.Menu({
//                items: [{
//                    text: l('tooltip.delete'),
//                    iconCls: 'app-icon-remove',
////                    handler: function (btn, e) {
////                        dataview.up('gallery').fireEvent('deletephoto', dataview, rec);
////                    }
//                }],
//                listeners: {
//                    hide: function () {
//                        dataview.ctx.destroy();
//                    }
//                }
//            });
//            dataview.ctx.rec = rec;
//            dataview.ctx.showAt(e.getXY());
//        }
    }
});