Ext.define('Pms.modules.groupRoomUse.view.groupRoomUseConfirmForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.groupRoomUseConfirmForm',

    layout: 'vbox',

    items: [
        {
            xtype: 'hidden',
            name: 'id'
        },
        {
            fieldLabel: l('room'),
            name: 'room',
            xtype: 'combobox',
            store: Ext.create('Pms.modules.room.store.Room').load(),
            displayField: 'number',
            valueField: 'id',
            queryMode: 'remote'
        },
//        {
//            xtype: 'combobox',
//            fieldLabel: 'Источник',
//            name: 'property_source_type',
////            store: Ext.create('Pms.modules.group.store.sourceType').load(),
//            displayField: 'label',
//            valueField: 'name',
//            queryMode: 'remote'
//        },
//        {
//            xtype: 'combobox',
//            fieldLabel: 'Цель приезда',
//            name: 'property_visit_purpose',
////            store: Ext.create('Pms.modules.group.store.visitPurpose').load(),
//            displayField: 'label',
//            valueField: 'name',
//            queryMode: 'remote'
//        },
//        {
//            xtype: 'combobox',
//            fieldLabel: 'Тип заезда',
//            name: 'property_checkin_type',
////            store: Ext.create('Pms.modules.group.store.checkinType').load(),
//            displayField: 'label',
//            valueField: 'name',
//            queryMode: 'remote'
//        }
    ]
});
