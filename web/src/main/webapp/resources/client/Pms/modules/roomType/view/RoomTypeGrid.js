Ext.define("Pms.modules.roomType.view.RoomTypeGrid", {
    extend: "Pms.abstract.Grid",
    alias: 'widget.roomTypeGrid',
    store: "Pms.modules.roomType.store.RoomType",

    border: false,
    paging: true,

    multiSelect: true,
    forceFit: true,

    initComponent: function () {
        var me = this;

        me.columns = [
            {
                header: l('title'),
                dataIndex: 'name',
                flex: 3
            },
            {
                header: l('adults'),
                dataIndex: 'adults',
                flex: 1
            },
            {
                header: l('children'),
                dataIndex: 'children',
                flex: 1
            },
            {
                header: l('countAdditional'),
                dataIndex: 'additional',
                flex: 1
            },
            {
                xtype: 'booleancolumn',
                header: l('approved'),
                dataIndex: 'approved',
                flex: 1,
                renderer: function (value, meta, record) {
                    value = value ? Pms.iconAccept : Pms.iconNotAccept;
                    if(record.data.defaultBaseRoom)
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
                        flex: 1,
                        iconCls: 'fa fa-file-excel-o',
                        text: l('exportToExcel'),
                        handler: function(b, e) {
                            b.up('grid').downloadExcelXml();
                        }
                    }
                ]
            }
        ]
    }
});