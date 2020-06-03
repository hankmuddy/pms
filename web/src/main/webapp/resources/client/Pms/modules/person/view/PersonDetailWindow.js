Ext.define('Pms.modules.person.view.PersonDetailWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.personDetailWindow',
    title: l('person.personInformation'),
    autoShow: false,
    width: 1230,
    height: 550,
    data: null,

    initComponent: function () {
        var me = this;
        this.items = [
            {
                xtype: 'personForm',
                data: me.data,
                bbar: ['->', {
                    iconCls: 'save-action-icon',
                    text: l('refresh'),
                    action: 'update',
                    requestDisable: true
                }, {
                    iconCls: 'app-icon-remove',
                    text: l('cancel.btn'),
                    handler: function (btn) {
                        btn.up('window').close();
                    }
                }]
            }
        ];

        this.callParent(arguments);
    }
});