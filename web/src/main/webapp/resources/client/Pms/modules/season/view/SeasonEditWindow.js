Ext.define('Pms.modules.season.view.SeasonEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.seasonEditWindow',
    title: l('periodEdit'),
    autoShow: false,
    minimizable: false,
    maximizable: false,
    resizable: false,
    width: 300,
    height: 180,
    overflowY: 'auto',
    data: {},

    initComponent: function () {
        var me = this;

        if (me.data.approved) {
            me.width = 800;
            me.height = 400;
        }
        me.items = [
            {
                xtype: 'seasonForm',
                data: me.data
            }
        ];

        me.bbar = ['->', {
            text: l('save.btn'),
            action: me.data.approved ? 'update-living' : 'update',
            iconCls: 'save-action-icon'
        }, {
            text: l('cancel.btn'),
            iconCls: 'app-icon-outgo',
            scope: this,
            handler: this.close
        }];

        this.callParent(arguments);
    }
});