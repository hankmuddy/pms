Ext.define('Pms.modules.settings.view.gallery', {
    extend: 'Pms.abstract.Panel',
    alias: 'widget.gallery',
    requires: [
        'Pms.modules.settings.store.Gallery'
//        'Ext.ux.DataView.Draggable'
    ],

    data: {},
    type: null,
    photoSelect: false,
    photoUpload: false,
    roomTypeId: null,
    imgViewWidth: '100%',
    imgViewRatio: null,
    imgViewHeight: 400,
    thumbsWidth: null,
    thumbsRatio: null,
    thumbsHeight: 150,

    initComponent: function () {
        var me = this;

        me.store = Ext.create('Pms.modules.settings.store.Gallery', {
            roomTypeId: me.roomTypeId
        }).load();

        me.items = [
            {
                xtype: 'panel',
                type: 'imgPanel',
                resizable: false,
                layout: 'fit',
                data: {src: Pms.emptyImgSrc},
                tpl: ['<img src="{src}" style="vertical-align:middle;max-width:98%;max-height:98%;">'],
                height: me.imgViewHeight,
                bodyStyle: {
                    lineHeight: me.imgViewHeight + 'px',
                    width: '100%',
                    border: '0px',
                    borderBottom: '1px solid #ddd',
                    textAlign: 'center'
                },
                listeners: {
                    resize: function (panel, width, height, oldWidth, oldHeight, eOpts) {
                        panel.setBodyStyle('line-height', height + 'px');
                    }
                }
            },
            {
                xtype: 'panel',
                height: 30,
                hidden: !(me.photoSelect || me.photoUpload),
                bodyStyle: {
                    backgroundColor: '#eee',
                    border: '0px',
                    borderBottom: '1px solid #ddd'
                },
                defaults: {
                    xtype: 'button',
                    margin: 3
                },
                items: [
                    {
                        text: l('settings.photoSelect'),
                        iconCls: 'pms-image-add-icon',
                        hidden: !me.photoSelect,
                        hint: l('hint.choosePhotoForRoomType'),
                        handler: function (btn, e) {
                            var roomTypePhotos = [];
                            if (me.roomTypeId) {
                                var roomTypeStore = btn.up('panel').nextNode().getStore(),
                                    roomTypeItems = roomTypeStore.data.items;

                                for (var i in roomTypeItems) {
                                    roomTypePhotos.push(roomTypeItems[i].data.code);
                                }
                                Ext.widget('dataViewWindow', {roomTypeId: me.roomTypeId, roomTypeStore: roomTypeStore, roomTypePhotos: roomTypePhotos, galleryInstance: me});
                            }
                        }
                    },
                    {
                        text: l('settings.photoUpload'),
                        iconCls: 'app-icon-upload',
                        hidden: !me.photoUpload,
                        hint: l('hint.downloadPhotoInGallery'),
                        handler: function (btn, e) {
                            Ext.widget('uploadWindow', {uploadType: 'PHOTO'});
                        }
                    }
                ]
            },
            {
                xtype: 'dataview',
                cls: 'gallery',
                store: me.store,
                autoScroll: true,
                multiSelect: true,
                trackOver: true,
                overItemCls: 'x-item-over',
                itemSelector: 'div.thumb-wrap',
                emptyText: l('photo.noPhotos'),
                height: me.thumbsHeight,
                tpl: [
                    '<tpl for=".">',
                    '<div class="thumb-wrap">',
                        '<div class="thumb"><img src="' + _('imagesUrlPrefix') + '{code}" /></div>',
                    '</div>',
                    '</tpl>',
                    '<div class="x-clear"></div>'
                ],
                listeners: {
                    itemclick: function (dataview, record, item, index, e, eOpts) {
                        var imgPanel = dataview.up('gallery').down('panel[type=imgPanel]');
                        if (!Ext.isEmpty(record)) imgPanel.update({src: _('imagesUrlPrefix') + record.data.code});
                    },
                    refresh: function (dataview, eOpts) {
                        var imgPanel = dataview.up('gallery').down('panel[type=imgPanel]');
                        if (!Ext.isEmpty(dataview.store.data.items)) imgPanel.update({src: _('imagesUrlPrefix') + dataview.store.data.items[0].data.code});
                        else imgPanel.update({src: Pms.emptyImgSrc});
                    },
                    itemcontextmenu: me.onContextMenu
                }
            }
        ];

        me.callParent();
    },

    onContextMenu: function (dataview, rec, item, index, e, eOpts) {
        var me = this;

        e.stopEvent();
        dataview.ctx = new Ext.menu.Menu({
            items: [
                {
                    text: l('tooltip.mainPhoto'),
                    iconCls: 'pms-success-icon-16',
                    handler: function (btn, e) {
                        dataview.up('gallery').fireEvent('mainphoto', dataview, rec);
                    }
                },
                {
                    text: l('tooltip.delete'),
                    iconCls: 'app-icon-remove',
                    handler: function (btn, e) {
                        dataview.up('gallery').fireEvent('deletephoto', dataview, rec);
                    }
                }
            ],
            listeners: {
                hide: function () {
                    dataview.ctx.destroy();
                }
            }
        });
        dataview.ctx.rec = rec;
        dataview.ctx.showAt(e.getXY());
    }
});

