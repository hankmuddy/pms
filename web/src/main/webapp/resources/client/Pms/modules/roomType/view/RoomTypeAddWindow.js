Ext.define('Pms.modules.roomType.view.RoomTypeAddWindow', {
    extend: 'Pms.abstract.Window',
    requires: [
        'Ext.form.Panel',
        'Pms.abstract.field.Money'
    ],
    alias: 'widget.roomTypeAddWindow',
    title: l('roomTypeAdd'),
    resizable: true,
    width: 650,
    height: 400,

    listeners: {
        afterRender: function (thisForm, options) {
            this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
                enter: function () {
                    if (!thisForm.down('combobox[isExpanded=true]'))
                        this.down('button[action=add-roomType]').fireHandler()
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
                layout: 'fit',
                border: false,
                autoscroll: true,
                fileupload: true,

                items: [
                    {
                        xtype: 'container',
                        layout: 'hbox',
                        margin: 5,
                        items: [
                            {
                                xtype: "fieldset",
                                padding: 10,
                                defaults: {
                                    xtype: "textfield",
                                    anchor: '100%'
                                },
                                width: 300,
                                items: [
                                    {
                                        fieldLabel: l('title') + Pms.requiredStatus,
                                        name: "name",
                                        allowBlank: false
                                    },
                                    {
                                        fieldLabel: l('shortname') + Pms.requiredStatus,
                                        name: "shortname",
                                        maxLength: 4,
                                        enforceMaxLength: 4,
                                        allowBlank: false
                                    },
//                            {
//                                fieldLabel: l('virtualRoom.shortname') + Pms.requiredStatus,
//                                name: "virtualRoom",
//                                maxLength: 4,
//                                enforceMaxLength: 4,
//                                validator: function (val) {
//                                    if (val == this.previousNode().value)
//                                        return l('roomType.shortnameShouldNotBeEqual');
//                                    else return true
//                                },
//                                allowBlank: false
//                            },
                                    {
                                        fieldLabel: l('adults') + Pms.requiredStatus,
                                        name: "adults",
                                        allowBlank: false,
                                        regex: /^[0-9]+$/
                                    },
                                    {
                                        fieldLabel: l('children'),
                                        name: "children",
                                        regex: /^[0-9]+$/
                                    },
                                    {
                                        fieldLabel: l('virtualRoom.additional'),
                                        name: "additional",
                                        regex: /^[0-9]+$/
                                    },
                                    {
                                        fieldLabel: l('virtualRoom.defaultPrice') + Pms.requiredStatus,
                                        name: "defaultPrice",
                                        xtype: 'moneyfield',
                                        allowBlank: false,
                                        hideTrigger: true
                                    },
                                    {
                                        name: 'area',
                                        fieldLabel: l('roomType.area'),
                                        regex: /^[0-9]+$/
                                    },
                                    {
                                        name: 'smoking',
                                        fieldLabel: l('roomType.smoking'),
                                        xtype: 'lookupCombobox',
                                        valueNotFoundText: null,
                                        lookupType: 'smoking'
                                    },
                                    {
                                        name: 'description',
                                        fieldLabel: l('roomType.details.description'),
                                        xtype: 'textareafield'
                                    }
                                ]
                            },
                            {
                                xtype: 'fieldset',
                                width: 300,
                                title: l('roomType.createDetails'),
                                name: 'details',
                                layout: {
                                    type: 'vbox',
                                    align: 'center'
                                },
                                defaults: {
                                    xtype: "textfield",
                                    anchor: '100%'
                                },
                                margin: '0 0 0 10',
                                layout: 'anchor',
//                                collapsible: true,
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
                                        name: 'x' +
                                            'xLargeDouble',
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
                ]
            }
        ];

        this.buttons = [
            {
                text: l('save.btn'),
                requestDisable: true,
                action: 'add-roomType',
                requestDisable: true
            },
            {
                text: l('cancel.btn'),
                scope: this,
                handler: this.close
            }
        ];

        me.callParent();
    }
});