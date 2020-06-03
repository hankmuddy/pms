Ext.define('Pms.modules.groupRoomUse.view.groupRoomUseIncomeForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.groupRoomUseIncomeForm',

    layout: 'vbox',
    autoscroll: true,
    fileupload: true,
    isGroup: null,
    initComponent: function () {
        this.items =
            [
                {
                    xtype: 'hidden',
                    name: 'id'
                },
                {
                    name: 'arrivalTime',
                    xtype: 'timefield',
                    format: 'H : i',
                    submitFormat: 'H : i',
                    fieldLabel: l('arrivalTime') + Pms.requiredStatus,
                    increment: 5,
                    allowBlank: false,
                    value: new Date(Pms.fromUTC(new Date(), true))
                },
                {
                    xtype: 'checkboxfield',
                    fieldLabel: l("isAllGroup"),
                    inputValue: true,
                    labelWidth: 280,
                    hidden: this.isGroup
                }
            ];
        this.callParent(arguments);
    }
});
