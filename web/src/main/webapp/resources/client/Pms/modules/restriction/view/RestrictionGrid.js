Ext.define("Pms.modules.restriction.view.RestrictionGrid", {
    extend: "Pms.abstract.Grid",
    alias: 'widget.restrictionGrid',
    store: "Pms.modules.restriction.store.Restriction",

    initComponent: function () {
        var me = this;

        me.columns = [
            {
                header: l('restriction.name'),
                dataIndex: 'name',
            },
            {
                header: l('restriction.minStay'),
                dataIndex: "minStay",
            },
            {
                header: l('restriction.minStayArrival'),
                dataIndex: "minStayArrival",
            },
            {
                header: l('restriction.maxStay'),
                dataIndex: "maxStay",
            },
            {
                header: l('restriction.closed'),
                dataIndex: "closed",
                renderer: function(value){
                    return l('closed.' + value)
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
                    iconAlign: 'left'
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
                    }
                ]
            }
        ];
    }
});