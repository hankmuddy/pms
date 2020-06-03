Ext.define('Pms.modules.child.view.ChildEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.childEditWindow',
    title: l('editChildInfo'),
    autoShow: false,
    width: 1000,
    height: 460,

    data: {},

    initComponent: function () {
        var me = this;

        me.items = [
            {
                xtype: 'tabpanel',
                items: [
                    {
                        title: l('childInfo'),
                        autoScroll: false,
                        items: [
                            {
                                xtype: 'childForm',
                                height: 400,
                                data: me.data
                            }
                        ]
                    }
                ]
            }
        ];

        me.callParent(arguments);
    }
});