Ext.define('Pms.modules.quota.view.RTVPeriodWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.rtvPeriodWindow',
//    requires: ['Pms.abstract.field.lookup.Picker'],

    listeners: {
        afterRender: function (thisForm, options) {
            this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
                enter: function () {
                    if (!thisForm.down('combobox[isExpanded=true]'))
                        this.down('button[action=save]').fireHandler()
                },
                scope: this
            });
        }
    },
    title: l('quota.quotaPeriod'),
    width: 400,
    height: 200,

    initComponent: function () {
        var me = this;
        me.items = [
            {
                xtype: 'form',
                border: false,
                defaults: {
                    labelWidth: 150,
                    anchor: '100%',
                    margin: 5
                },
                items: [
                    {
                        fieldLabel: l("roomType") + Pms.requiredStatus,
                        name: "room",
                        xtype: 'combobox',
                        multiSelect: true,
                        store: Ext.create('Pms.modules.roomType.store.RoomType', {filterParams: [
                            {field: 'approved', comparison: 'eq', data: {type: 'boolean', value: true}}
                        ]}).load(),
                        displayField: "name",
                        valueField: "id",
                        queryMode: "local",
                        listConfig: {
                            getInnerTpl: function () {
                                return '<div class="search-item">' +
                                    '<span><b>{name}</b> {adults}, {children}, {additional} </span>' +
                                    '</div>';
                            }
                        },
                        allowBlank: false
                    },
                    {
                        fieldLabel: l('dateFrom') + Pms.requiredStatus,
                        name: "dateStart",
                        xtype: 'pmsdatefield',
                        format: 'd/m/Y',
                        submitFormat: 'U',
                        value: new Date(Pms.fromUTC(new Date(), true)),
                        allowBlank: false
                    },
                    {
                        fieldLabel: l('dateTo') + Pms.requiredStatus,
                        name: "dateEnd",
                        xtype: 'pmsdatefield',
                        format: 'd/m/Y',
                        submitFormat: 'U',
                        allowBlank: false
                    },
                    {
                        xtype: 'numberfield',
                        step: 1,
                        minValue: 0,
                        maxValue: 100,
                        name: 'roomsAvailable',
                        fieldLabel: l('quota.roomsAvailable') + Pms.requiredStatus,
                        value: Ext.isEmpty(me.data) ? 0 : me.data[0].data.roomsAvailable,
                        allowBlank: false
                    }
                ]
            }
        ];

        me.buttons = [
            {
                text: l('save.btn'),
                action: 'save',
                requestDisable: true,
            },
            {
                text: l('cancel.btn'),
                scope: this,
                handler: this.close
            }
        ];

        this.callParent(arguments);
    }
});