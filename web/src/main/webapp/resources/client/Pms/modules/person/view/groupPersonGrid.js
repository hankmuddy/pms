Ext.define('Pms.modules.person.view.groupPersonGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.groupPersonGrid',
    store: 'Pms.modules.person.store.GroupMember',

    masterPerson: false,

    data: {},

    initComponent: function () {
        var me = this,
            actionButtons = [
                {
                    iconCls: 'app-icon-costume-data',
                    tooltip: l('person.guestForm'),
                    handler: function (grid, rowIndex, colIndex) {
                        var rec = grid.getStore().getAt(rowIndex);
                        me.openReportWindow('guestForm?customerId=' + rec.data.id, l('person.guestForm'));
                    }
                },
                {
                    iconCls: 'app-icon-print',
                    tooltip: l('person.guestCard'),
                    handler: function (grid, rowIndex, colIndex) {
                        var rec = grid.getStore().getAt(rowIndex);
                        me.openReportWindow('guestCard?customerId=' + rec.data.id, l('person.guestCard'));
                    }
                }
            ];

        if (_('hotel').country == 'UA') actionButtons.push({
            iconCls: 'app-icon-costume-data1',
            tooltip: l('person.hotelCard'),
            handler: function (grid, rowIndex, colIndex) {
                var rec = grid.getStore().getAt(rowIndex);
                me.openReportWindow('hotelCard?customerId=' + rec.data.id, l('person.hotelCard'));
            }
        })
        me.columns = [
            {
                xtype: 'rownumberer',
                header: '№',
                width: 35,
                sortable: false,
                shrinkWrap: 3,
                renderer: function (value, meta, record) {
                    return record.index + 1;
                }
            },
            {
                header: l('firstName'),
                dataIndex: 'firstName',
                xtype: 'templatecolumn',
                tpl: '<span style="text-decoration:underline;cursor:pointer;">{firstName} {lastName} {patronymic}</span>',
                flex: 1,
                sortable: false
            },
            {
                header: l('contacts'),
                dataIndex: 'phone',
                xtype: 'templatecolumn',
                tpl: '{phone}<br />{email}',
                flex: 1,
                sortable: false
            },
            {
                header: l('masterPerson'),
                renderer: function (v, col) {
                    var rec = col.record.data,
                        personId = rec.person.id;
                    return personId == this.masterPerson ? Pms.iconOk : '';
                }
            },
            {
                header: l('actions'),
                xtype: 'actioncolumn',
                sortable: false,
                width: 60,
                items: actionButtons
            }
        ];

        me.callParent();
    },

    openReportWindow: function (urlSuffix, winTitle) {
        Ext.create('Ext.window.Window', {
            title: winTitle,
            width: 1000,
            height: 600,
            maximizable: true,
            listeners: {
                close: Pms.removePreloader
            },
            layout: 'fit',
            items: [
                {
                    xtype: 'box',
                    autoEl: {
                        tag: 'iframe',
                        style: 'height: 100%; width: 100%',
                        src: '/app/report/' + urlSuffix,
                        onload: 'Pms.removePreloader()'
                    },
                    listeners: {
                        beforerender: function (panel) {
                            panel.setLoading(true, true);
                            Pms.createPreloader();
                        }
                    }
                }
            ]
        }).show();
    }
});