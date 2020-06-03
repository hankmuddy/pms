Ext.define('Pms.modules.accommodation.view.AccommodationGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.accommodationGrid',
    store: 'Pms.modules.accommodation.store.Accommodation',

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
                header: l('title'),
                dataIndex: 'name',
                flex: 1
            },
            {
                header: l('shortname'),
                dataIndex: 'shortName',
                flex: 1
            },
            {
                xtype: 'booleancolumn',
                header: l('approved'),
                dataIndex: 'approved',
                flex: 1,
                trueText: Pms.iconAccept,
                falseText: Pms.iconNotAccept
            }
        ];
        me.callParent();
    }
});