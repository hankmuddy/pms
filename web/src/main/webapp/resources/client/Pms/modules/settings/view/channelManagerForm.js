Ext.define('Pms.modules.settings.view.channelManagerForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.channelManagerForm',

    layout: 'border',
    bodyStyle: {
        backgroundColor: '#fff'
    },
    autoScroll: false,

    data: {},

    initComponent: function () {
        var me = this;


        me.items = [
            {
                xtype: 'fieldset',
                width: 300,
                title: l('settings.wubook.import'),
                name: 'details',
//                hidden: !_('channelsEnabled'),
                layout: 'vbox',
                items: [
                    {//import plans, seasons and roomtypes
                        xtype: 'button',
                        iconCls: 'fa fa-cloud-download',
                        text: l('importFromWubook.button'),
                        action: 'importFromWubook',
                        requestDisable: true,
                        hidden: _('importStatus') === 'DATA_IMPORTED' || _('importStatus') === 'RESERVATIONS_IMPORTED',
                        hint: l('importFromWubook.hint')
                    },
                    {
                        xtype: 'button',
                        iconCls: 'fa fa-cloud-upload',
                        text: l('exportToWubook.button'),
                        action: 'exportToWubook',
                        requestDisable: true,
                        hidden: _('importStatus') != 'INIT',
                        hint: l('importFromWubook.hint')
                    },
                    {//importBookings
                        xtype: 'button',
                        iconCls: 'fa fa-cloud',
                        text: l('importToWubook.button'),
                        action: 'importBookings',
                        requestDisable: true,
                        hidden: _('importStatus') === 'INIT' || _('importStatus') === 'RESERVATIONS_IMPORTED',
                        hint: l('importToWubook.hint')
//                                    margins: '20 0 0 0'
                    },
                    {
                        title: l('settings.synchronizeQuota'),
                        xtype: 'fieldset',
                        layout: 'vbox',
                        width: 200,
                        hidden: _('importStatus') !== 'RESERVATIONS_IMPORTED',
                        items: [
                            {
                                xtype: 'fieldcontainer',
                                layout: 'hbox',
                                defaultType: 'datefield',
                                padding: '20 0 0 0',
                                items: [
                                    {
                                        name: 'dfrom',
                                        width: 160,
                                        fieldLabel: l('from'),
                                        startLabel: l('from'),
                                        format: 'd/m/Y',
                                        labelWidth: 20,
                                        allowBlank: false
//                                            margins: '0 0 0 20'
                                    }
                                ]
                            },
                            {
                                xtype: 'datefield',
                                name: 'dto',
                                width: 160,
                                fieldLabel: l('to'),
                                startLabel: l('to'),
                                format: 'd/m/Y',
                                labelWidth: 20,
                                defaultAlign: "tl-bl?",
                                allowBlank: false
                            },
                            {//synchronizeQuota
                                xtype: 'button',
                                iconCls: 'fa fa-code-fork',
                                text: l('updateQuota.button'),
                                action: 'updateQuota',
                                requestDisable: true,
                                hint: l('synchronizeQuota.hint'),
                                margins: '0 0 0 25'
                            }
                        ]
                    }
                ]
            }
        ];

        me.callParent();
    },

    bbar: [
//        {
//            iconCls: 'booking-system-shortcut-icon',
//            text: l('importFromWubook.button'),
//            action: 'importFromWubook',
//            requestDisable: true,
//            hint: l('importFromWubook.hint')
//        },
        '->',
//        {
//            iconCls: 'save-action-icon',
//            text: l('save.btn'),
//            action: 'update',
//            requestDisable: true,
////            hint: 'Сохранить настройки'
//        },
        {
            iconCls: 'app-icon-remove',
            text: l('cancel.btn'),
            handler: function (btn) {
                btn.up('window').close();
            }
        }
    ]
});