Ext.define('Pms.modules.groupRoomUse.view.groupBookingForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.groupBookingForm',
    title: l('roomUse.arrivalData'),

    layout: 'vbox',

    storeData: [],

    initComponent: function () {
        var me = this;

        me.items = [
            {
                margin: 5,
                width: '100%',
                xtype: 'groupBookingGrid',
                storeData: me.storeData
            },
//            {
//                xtype: 'fieldset',
//                title: 'Информация о заезде',
//                margin: 5,
//                width: '100%',
//                layout: 'hbox',
//                items: [
//                    {
//                        xtype: 'container',
//                        width: '50%',
//                        items: [
//                            {
//                                xtype: 'combobox',
//                                fieldLabel: l('action'),
//                                name: 'property_use_type',
//                                store: Ext.create('Pms.modules.groupRoomUse.store.useType', {
//                                    bookingWarranty: false,
//                                    living: false,
//                                    refuse: false,
//                                    outgo: false,
//                                    repair: false
//                                }).load(),
//                                displayField: 'label',
//                                valueField: 'name',
//                                queryMode: 'remote'
//                            },
//                            {
//                                xtype: 'combobox',
//                                fieldLabel: 'Источник',
//                                name: 'property_source_type',
//                                store: Ext.create('Pms.modules.group.store.sourceType'),
//                                displayField: 'label',
//                                valueField: 'name',
//                                queryMode: 'remote'
//                            }
//                        ]
//                    },
//                    {
//                        xtype: 'container',
//                        width: '50%',
//                        items: [
//                            {
//                                xtype: 'combobox',
//                                fieldLabel: 'Цель приезда',
//                                name: 'property_visit_purpose',
//                                store: Ext.create('Pms.modules.group.store.visitPurpose'),
//                                displayField: 'label',
//                                valueField: 'name',
//                                queryMode: 'remote'
//                            },
//                            {
//                                xtype: 'combobox',
//                                fieldLabel: 'Тип заезда',
//                                name: 'property_checkin_type',
//                                store: Ext.create('Pms.modules.group.store.checkinType'),
//                                displayField: 'label',
//                                valueField: 'name',
//                                queryMode: 'remote'
//                            }
//                        ]
//                    }
//                ]
//            }
        ];

        me.callParent(arguments);
    }
});