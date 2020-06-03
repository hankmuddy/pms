Ext.define('Pms.modules.child.view.ChildAddWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.childAddWindow',
    title: l('newChild'),
    width: 1000,
    height: 450,

    initComponent: function () {
        this.items = [
            {
                xtype: 'childForm'
            }
        ];

        this.callParent(arguments);
    }
});