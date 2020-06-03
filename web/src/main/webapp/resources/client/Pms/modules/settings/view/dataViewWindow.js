Ext.define('Pms.modules.settings.view.dataViewWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.dataViewWindow',
    title: l('settings.hotelPhoto'),
    width: 800,
    height: 500,
    bodyStyle: {
        backgroundColor: '#fff'
    },
    data: {},
    roomTypeId: null,
    roomTypeStore: null,
    roomTypePhotos: [],

    initComponent: function () {
        var me = this;

        me.items = [
            {
                xtype: 'imgDataView',
                store: Ext.create('Pms.modules.settings.store.Gallery').load()
            }
        ];

        me.buttons = [
            {
                text: l('select.btn'),
                action: 'photoselected',
                requestDisable: true
            },
            {
                text: l('cancel.btn'),
                scope: me,
                handler: me.close
            }
        ];

        me.callParent(arguments);
    }
});