Ext.define('Pms.modules.otaCalendar.view.otaEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.otaEditWindow',

    title: l('edit.btn'),
    width: 400,
    height: 340,
    baseRoomId: null,
    baseRoomType: null,

    initComponent: function () {
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
                                xtype: "hidden",
                                name: "id"
                            },
                            {
                                fieldLabel: l("baseRoom"),
                                name: "room",
                                xtype: 'displayfield',
                                listeners: {
                                    resize: function (field,width,height) {
                                        var win = this.up('window');
                                        win.setHeight(320 + height);
                                    }
                                }
                            },
                            {
                                fieldLabel: l('date') + Pms.requiredStatus,
                                name: "date",
                                xtype: 'pmsdatefield',
                                format: 'd/m/Y',
                                submitFormat: 'U',
                                allowBlank: false
                            },
                            {
                                xtype: 'checkboxfield',
                                fieldLabel: l("otaCalendar.otaAllowed"),
                                name: 'otaAllowed',
                                inputValue: true,
                                renderer: function (val) {
                                    return !val
                                }
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
                                regex: /^[0-9]+$/
                            },
                            {
                                fieldLabel: l("otaCalendar.minStayArrival"),
                                name: "minStayArrival",
                                regex: /^[0-9]+$/
                            },
                            {
                                fieldLabel: l("otaCalendar.maxStay"),
                                name: "maxStay",
                                regex: /^[0-9]+$/
                            },
                            {
                                fieldLabel: l('ota.closed'),
                                name: 'closed',
                                lookupType: 'closed',
                                xtype: 'lookupCombobox',
                                valueNotFoundText: null
                            },
                        ]
                    }
                ]
            }
        ];

        this.buttons = [
            {
                text: l('save.btn'),
                action: 'save',
                requestDisable: true
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