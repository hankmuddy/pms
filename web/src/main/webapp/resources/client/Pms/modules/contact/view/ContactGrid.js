Ext.define('Pms.modules.contact.view.ContactGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.contactGrid',
    store: 'Pms.modules.contact.store.Contact',
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
                dataIndex: 'name',
                xtype: 'templatecolumn',
                tpl: '<span style="text-decoration:underline;cursor:pointer;">{name}</span>',
                flex: 3
            },
            {
                header: l('person.position'),
                dataIndex: 'post',
                flex: 1
            },
            {
                header: l('phone'),
                dataIndex: 'phone',
                flex: 1
            },
            {
                header: l('email'),
                dataIndex: 'email',
                flex: 1
            },
            {
                header: l('description'),
                dataIndex: 'description',
                flex: 1
            }
        ];

        me.callParent();
    }
});