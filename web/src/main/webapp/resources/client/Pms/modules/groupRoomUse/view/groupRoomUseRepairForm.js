Ext.define('Pms.modules.groupRoomUse.view.groupRoomUseRepairForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.groupRoomUseRepairForm',

    // title: 'Данные отказа',
    layout: 'vbox',
    autoscroll: true,
    fileupload: true,

    items: [
        {
            xtype: 'hidden',
            name: 'id'
        },
//        {
//            xtype: 'hidden',
//            name: 'property_use_type'
//        },
        {
            fieldLabel: l('room'),
            name: 'room',
            xtype: 'combobox',
            store: Ext.create('Pms.modules.room.store.Room').load(),
            displayField: 'number',
            valueField: 'id',
            queryMode: 'remote'
        },
        {
            xtype: 'pmsdatefield',
            fieldLabel: l('from'),
            name: 'startDate',
            format: 'd/m/y',
            submitFormat: 'U'
        },
        {
            xtype: 'pmsdatefield',
            fieldLabel: l('to'),
            name: 'endDate',
            format: 'd/m/y',
            submitFormat: 'U'
        },
//        {
//            xtype: 'textareafield',
//            grow: true,
//            name: 'description',
//            fieldLabel: 'Описание ремонта',
//            anchor: '100%'
//        }
    ]
});
