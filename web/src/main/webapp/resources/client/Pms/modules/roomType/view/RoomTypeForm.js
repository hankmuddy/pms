Ext.define('Pms.modules.roomType.view.RoomTypeForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.roomTypeForm',
    requires: ['Pms.abstract.field.Money'],

    data: {},
    approved: null,

    initComponent: function () {
        var me = this;
        me.items = this.buildItems();
        me.callParent();
    },

    buildItems: function () {
        var me = this;

        return  [
            {
                xtype: 'container',
                layout: 'hbox',
                margin: 5,
                items: [
                    {
                        xtype: 'fieldset',
                        padding: 10,
                        width: 450,
                        defaults: me.approved ? me.defaults : {
                            xtype: 'textfield',
                            anchor: '100%'
                        },
                        items: [
                            {
                                xtype: 'hidden',
                                name: 'id'
                            },
                            {
                                fieldLabel: l('title'),
                                name: "name",
                                allowBlank: false
                            },
                            {
                                fieldLabel: l('shortname'),
                                name: "shortname",
                                maxLength: 4,
                                enforceMaxLength: 4,
                                allowBlank: false
                            },
                            {
                                fieldLabel: l('countAdults'),
                                name: "adults",
                                allowBlank: false,
                                regex: /^[0-9]+$/
                            },
                            {
                                fieldLabel: l('countChildren'),
                                name: "children",
                                regex: /^[0-9]+$/
                            },
                            {
                                fieldLabel: l('countAdditional'),
                                name: "additional",
                                regex: /^[0-9]+$/
                            },
                            {
                                fieldLabel: l('virtualRoom.defaultPrice'),
                                name: "defaultPrice",
                                xtype: me.approved ? 'displayfield' : 'moneyfield',
                                allowBlank: false,
                                hideTrigger: true,
                                setValue: function (value) {
                                    var me = this;
                                    if (!me.rawValue) {
                                        me.setRawValue(value / 100);
                                    }
                                    else {
                                        me.setRawValue(value);
                                    }
                                    return me;
                                }
                            },
                            {
                                name: 'area',
                                fieldLabel: l('roomType.area'),
                                regex: /^[0-9]+$/
                            },
                            {
                                name: 'smoking',
                                fieldLabel: l('roomType.smoking'),
                                xtype: me.approved ? 'displayfield' : 'lookupCombobox',
                                valueNotFoundText: null,
                                lookupType: 'smoking'
                            },
                            {
                                name: 'description',
                                fieldLabel: l('roomType.details.description'),
                                xtype: me.approved ? 'displayfield' : 'textareafield'
                            }
                        ]
                    },
                    {
                        xtype: 'fieldset',
                        width: 450,
                        title: l('roomType.createDetails'),
                        name: 'details',
                        layout: {
                            type: 'vbox',
                            align: 'center'
                        },
                        defaults: me.approved ? me.defaults : {
                            xtype: 'textfield',
                            anchor: '100%'
                        },
                        margin: '0 0 0 10',
                        layout: 'anchor',
//                        collapsible: true,
                        items: [
                            {
                                name: 'single',
                                fieldLabel: l('roomType.details.single'),
                                regex: /^[0-9]+$/
                            },
                            {
                                name: 'standardDouble',
                                fieldLabel: l('roomType.details.standardDouble'),
                                regex: /^[0-9]+$/
                            },
                            {
                                name: 'largeDouble',
                                fieldLabel: l('roomType.details.largeDouble'),
                                regex: /^[0-9]+$/
                            },
                            {
                                name: 'xLargeDouble',
                                fieldLabel: l('roomType.details.xLargeDouble'),
                                regex: /^[0-9]+$/
                            },
                            {
                                name: 'bunk',
                                fieldLabel: l('roomType.details.bunk'),
                                regex: /^[0-9]+$/
                            },
                            {
                                name: 'sofaBed',
                                fieldLabel: l('roomType.details.sofaBed'),
                                regex: /^[0-9]+$/
                            },
                            {
                                name: 'futon',
                                fieldLabel: l('roomType.details.futon'),
                                regex: /^[0-9]+$/
                            }
                        ]
                    }
                ]
            }
        ];
    }
});