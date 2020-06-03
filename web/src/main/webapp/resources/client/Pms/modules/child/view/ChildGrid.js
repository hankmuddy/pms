Ext.define('Pms.modules.child.view.ChildGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.childGrid',
    store: 'Pms.modules.child.store.Child',

    forceFit: true,

    initComponent: function () {
        var me = this;

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
                tpl: '<span style="text-decoration:underline;cursor:pointer;">{lastName} {firstName}</span>',
                flex: 3
            },
            {
                header: l('dateOfBirth'),
                dataIndex: 'dob',
                xtype: 'datecolumn',
                format: 'd/m/y',
                flex: 1
            }
        ];

        me.callParent();
    }
});