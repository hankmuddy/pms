Ext.define('Pms.modules.person.view.PersonRoomIncomeWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.personRoomIncomeWindow',
    groupId: null,
    roomUseId: null,
    width: 300,
    income: false,
    toRoomUseIncome: false,
    groupMemberStore: null,

    initComponent: function () {
        var me = this;

        this.items = [
            {
                xtype: 'grid',
                overflowY: true,
                paging: false,
                selType: 'checkboxmodel',
                store: me.groupMembersStore,
                region: 'center',
                border: true,
                columns: [
                    {
                        header: l('firstName'),
                        dataIndex: 'person',
                        xtype: 'templatecolumn',
                        sortable: false,
                        tpl: '<span style="text-decoration:underline;cursor:pointer;">{person.firstName} {person.lastName} {person.patronymic}</span>',
                        flex: 1
                    },
                    {
                        header: l('contacts'),
                        dataIndex: 'person',
                        xtype: 'templatecolumn',
                        sortable: false,
                        tpl: '{person.phone}<br />{person.email}',
                        flex: 1
                    }
                ]
            }
        ];
        me.buttons = [
            {
                text: l('makeIncome'),
                action: 'income'
            },
            {
                text: l('cancel.btn'),
                scope: this,
                handler: this.close
            }
        ];
        this.callParent();
    }
});