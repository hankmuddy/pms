Ext.define('Pms.modules.booking.view.ProlongWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.prolongWindow',

    title: l('prolong') + ' ' + l('period'),
    width: 340,
    height: 100,
    modal: false,
    layout: 'fit',

    data: {},

    initComponent: function () {
        var me = this;

        console.log(this.data);

        me.items = [
            {
                xtype: 'form',
                bodyStyle: {
                    padding: '10px'
                },
                items: [{
                    xtype: 'pmsdatefield',
                    fieldLabel: l('endDate') + Pms.requiredStatus,
                    name: 'endDate',
                    format: 'd/m/y',
                    minValue: me.data.endDate,
                    submitFormat: 'U',
                    allowBlank: false
                }]
            }
        ];

        me.buttons = [
            {
                text: l('save.btn'),
                action: 'save',
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