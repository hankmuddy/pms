Ext.define('Pms.modules.quota.view.RTVEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.rtvEditWindow',

    title: l('quota.editQuota'),
    overflowY: 'auto',
    width: 400,
    height: 200,
    layout: 'fit',
    data: {},

    initComponent: function () {
        var me = this;

        me.items = [{
            xtype: 'form',
            items: [{
                xtype: 'numberfield',
                step: 1,
                minValue: 0,
                maxValue: 100,
                name: 'roomsAvailable',
                fieldLabel: l('quota.roomsAvailable'),
                value: Ext.isEmpty(me.data) ? 0 : me.data[0].data.roomsAvailable,
                margin: 10
            }]
        }];

        me.bbar = ['->', {
            text: l('save.btn'),
            iconCls: 'save-action-icon',
            action: 'process'
        },{
            text: l('cancel.btn'),
            iconCls: 'app-icon-remove',
            scope: this,
            handler: this.close
        },'->'];

        me.callParent(arguments);
    }
});