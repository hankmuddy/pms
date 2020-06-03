Ext.define('Pms.modules.groupRoomUse.view.groupRoomUseRefuseForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.groupRoomUseRefuseForm',

    data: {},
    initComponent: function () {
        var me = this;

        me.items = [
            {
                xtype: 'hidden',
                name: 'id'
            },
            {
                xtype: 'hidden',
                name: 'status'
            },
            {
                fieldLabel: l('room'),
                name: 'room',
                xtype: 'combobox',
                store: Ext.create('Pms.modules.room.store.Room').load(),
                displayField: 'number',
                valueField: 'id',
                queryMode: 'remote',
                allowBlank: false,
                readOnly: true
            },
            {
                xtype: 'pmsdatefield',
                fieldLabel: l('roomUse.refuseDate') + Pms.requiredStatus,
                name: 'date',
                format: 'd/m/y',
                minValue: me.data.startDate,
                maxValue: me.data.endDate,
                submitFormat: 'U',
                allowBlank: false,
                value: new Date() < me.data.startDate ? me.data.startDate : new Date() < me.data.endDate ? new Date() : me.data.endDate
            },
            {
                xtype: 'checkboxfield',
                fieldLabel: l("isAllGroup"),
                inputValue: true,
                labelWidth: 280,
                hidden: (typeof this.data.customerGroup != 'undefined' && this.data.customerGroup.roomUsesQuantity == 1) ? true : false
            }
        ];

        me.callParent(arguments);
    }
});
