Ext.define('Pms.modules.child.view.groupChildGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.groupChildGrid',
    store: 'Pms.modules.person.store.GroupMember',

    masterPerson: false,

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
            }
        ];

        me.callParent();
    }
});