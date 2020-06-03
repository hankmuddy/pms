Ext.define('Pms.modules.quota.view.BRVEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.brvEditWindow',
    requires: ['Pms.abstract.field.lookup.Picker'],

    title: l('quota.editRestriction'),
    autoShow: false,
    overflowY: 'auto',
    width: 400,
    height: 325,
    layout: 'fit',
    data: {},

    initComponent: function () {
        var me = this;

        me.items = [{
            xtype: 'form',
            bodyStyle: {
                padding: '10px'
            },
            defaults: {
                labelWidth: 150
            },
            items: [{
                    xtype: 'checkboxfield',
                    fieldLabel: l("otaCalendar.otaAllowed"),
                    name: 'otaAllowed',
                    inputValue: true,
//                    listeners: {
//                        afterrender: function (field) {
//                            console.log(field.value, field.checked);
//                        }
//                    }
                },{
                    xtype: 'checkboxfield',
                    fieldLabel: l("otaCalendar.closedToDeparture"),
                    name: 'closedToDeparture',
                    inputValue: true
                },{
                    fieldLabel: l("otaCalendar.minStay"),
                    name: "minStay",
                    xtype: 'numberfield',
                    step: 1,
                    minValue: 0,
                    maxValue: 100,
//                    value: Ext.isEmpty(me.data) ? 0 : me.data[0].data.minStay
                },{
                    fieldLabel: l("otaCalendar.minStayArrival"),
                    name: "minStayArrival",
                    xtype: 'numberfield',
                    step: 1,
                    minValue: 0,
                    maxValue: 100,
//                    value: Ext.isEmpty(me.data) ? 0 : me.data[0].data.minStayArrival
                },{
                    fieldLabel: l("otaCalendar.maxStay"),
                    name: "maxStay",
                    xtype: 'numberfield',
                    step: 1,
                    minValue: 0,
                    maxValue: 100,
//                    value: Ext.isEmpty(me.data) ? 0 : me.data[0].data.maxStay
                },{
                    fieldLabel: l('ota.closed'),
                    name: 'closed',
                    lookupType: 'closed',
                    xtype: 'lookupCombobox',
                    valueNotFoundText: null,
//                    value: Ext.isEmpty(me.data) ? '' : me.data[0].data.closed
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