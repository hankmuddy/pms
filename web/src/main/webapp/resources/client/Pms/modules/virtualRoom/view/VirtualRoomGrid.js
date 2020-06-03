Ext.define("Pms.modules.virtualRoom.view.VirtualRoomGrid", {
    extend: "Pms.abstract.Grid",
    alias: 'widget.virtualRoomGrid',
    store: "Pms.modules.virtualRoom.store.VirtualRoom",

    initComponent: function () {
        var me = this;

        me.columns = [
            {
                header: l('virtualRoom.name'),
                dataIndex: 'name',
                flex: 3
            },
            {
                header: l('virtualRoom.roomType'),
                dataIndex: 'roomType',
                renderer: function (val) {
                    return val.name;
                },
                flex: 2
            },
            {
                header: l('virtualRoom.shortname'),
                dataIndex: 'shortname',
                flex: 1
            },
            {
                header: l('virtualRoom.adults'),
                dataIndex: 'adults',
                flex: 1
            },
            {
                header: l('virtualRoom.children'),
                dataIndex: 'children',
                flex: 1
            },
            {
                header: l('virtualRoom.additional'),
                dataIndex: 'additional',
                flex: 1
            },
            {
                header: l('virtualRoom.approved'),
                xtype: 'booleancolumn',
                dataIndex: 'approved',
                flex: 1,
                renderer: function (value, meta, record) {
                    value = value ? Pms.iconAccept : Pms.iconNotAccept;
                    if (record.data.defaultBaseRoom)
                        return value + ' <img src="themes/default/images/icons/gray/icons/key.png" />';
                    return value
                }
            }
        ];
        me.callParent(arguments);
    },
    buildToolbar: function () {
        return [
            {
                xtype: 'toolbar',
                defaults: {
                    scale: 'small',
                    iconAlign: 'left',
                    width: '60'
                },
                items: [
                    {
                        text: l('add.btn'),
                        action: 'new',
                        iconCls: 'pms-add-icon-16'
                    },
                    {
                        text: l('edit.btn'),
                        action: 'edit',
                        iconCls: 'app-icon-edit2',
                        gridAction: true
                    },
                    {
                        text: l('commit.btn'),
                        action: 'commit',
                        iconCls: 'pms-success-icon-16',
                        gridAction: true
                    },
                    {
                        text: l('delete.btn'),
                        action: 'delete',
                        iconCls: 'pms-delete-icon-16',
                        gridAction: true
                    },
                    {
                        text: l('setDefault'),
                        action: 'default',
                        iconCls: 'app-icon-key',
                        gridAction: true
                    },
                    {
                        xtype: 'button',
                        text: l('exportToExcel'),
                        iconCls: 'fa fa-file-excel-o',
                        handler: function (b, e) {
                            b.up('grid').downloadExcelXml();
                        }
                    }
                ]
            }
        ]
    }
});