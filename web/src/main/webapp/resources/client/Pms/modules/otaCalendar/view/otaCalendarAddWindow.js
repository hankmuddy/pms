Ext.define('Pms.modules.otaCalendar.view.otaCalendarAddWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.otaCalendarAddWindow',
    requires: ['Pms.abstract.field.lookup.Picker'],
    title: l('setPrices'),
    width: 400,
    height: 340,
    date: null,
    virtualRoom: null,

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

    initComponent: function () {
        var me = this;
        this.items = [
            {
                xtype: 'form',
                id: 'otaCalendarEditForm',
                layout: 'fit',
                border: false,
                autoscroll: true,
                fileupload: true,

                items: [
                    {
                        xtype: "fieldset",
                        padding: 10,
                        defaults: {
                            xtype: "textfield",
                            anchor: '100%',
                            labelWidth: 150
                        },
                        items: [
                            {
                                fieldLabel: l("baseRoom") + Pms.requiredStatus,
                                name: "room",
                                xtype: 'combobox',
                                store: Ext.create('Pms.modules.roomType.store.BaseRoom', {filterParams: [
                                    {field: 'approved', comparison: 'eq', data: {type: 'boolean', value: true}}
                                ]}).load(),
                                displayField: "name",
                                value: me.virtualRoom.id,
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
                                fieldLabel: l('date') + Pms.requiredStatus,
                                name: "date",
                                xtype: 'pmsdatefield',
                                format: 'd/m/Y',
                                submitFormat: 'U',
                                value: me.date,
                                allowBlank: false
                            },
                            {
                                xtype: 'checkboxfield',
                                fieldLabel: l("otaCalendar.otaAllowed"),
                                name: 'otaAllowed',
                                inputValue: true
                            },
                            {
                                xtype: 'checkboxfield',
                                fieldLabel: l("otaCalendar.closedToDeparture"),
                                name: 'closedToDeparture',
                                inputValue: true
                            },
                            {
                                fieldLabel: l("otaCalendar.minStay"),
                                name: "minStay",
                                value: 0,
                                regex: /^[0-9]+$/
                            },
                            {
                                fieldLabel: l("otaCalendar.minStayArrival"),
                                value: 0,
                                name: "minStayArrival",
                                regex: /^[0-9]+$/
                            },
                            {
                                fieldLabel: l("otaCalendar.maxStay"),
                                value: 0,
                                name: "maxStay",
                                regex: /^[0-9]+$/
                            },
                            {
                                fieldLabel: l('ota.closed'),
                                name: 'closed',
                                lookupType: 'closed',
                                value: 'OPEN',
                                xtype: 'lookupCombobox',
                                valueNotFoundText: null
                            },
                        ]
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