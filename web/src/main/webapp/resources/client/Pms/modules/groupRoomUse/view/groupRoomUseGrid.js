Ext.define('Pms.modules.groupRoomUse.view.groupRoomUseGrid', {
    extend: "Pms.abstract.Grid",
    // extend : "Ext.grid.Panel",
    requires: [
        "Pms.modules.search.model.Search",
        "Pms.modules.search.store.Search"
    ],

    // title : "",
    id: 'groupRoomUseGrid',
    // border : false,
    editable: false,
    paging: false,
    top: 340,
    width: '100%',
    // layout: 'fit',
    forceFit: true,

    initComponent: function () {
        var me = this;

        me.store = me.store || Ext.create("Pms.modules.room.store.Room");

        me.columns = [
            {
                header: 'property_persons',
                dataIndex: 'property_persons',
                flex: 1
            },
            {
                header: 'property_master_person',
                dataIndex: 'property_master_person',
                flex: 1
            },
            {
                header: 'property_rooms_use',
                dataIndex: 'property_rooms_use',
                flex: 1
            },
            {
                header: 'property_children',
                dataIndex: 'property_children',
                flex: 1
            },
            {
                header: 'property_master_company',
                dataIndex: 'property_master_company',
                flex: 1
            },
            {
                header: 'property_total_amount',
                dataIndex: 'property_total_amount',
                flex: 1
            },
            {
                header: 'property_paid',
                dataIndex: 'property_paid',
                flex: 1
            },
            {
                header: 'property_paid_till',
                dataIndex: 'property_paid_till',
                flex: 1
            },
            {
                header: 'property_paid_fully',
                dataIndex: 'property_paid_fully',
                flex: 1
            },
            {
                header: 'property_income_type',
                dataIndex: 'property_income_type',
                flex: 1
            },
            {
                header: 'property_service_use',
                dataIndex: 'property_service_use',
                flex: 1
            }
        ];
        me.callParent();
    },

    showActive: function (value) {
        if (value) {
            return "Yes";
        } else {
            return "No";
        }
    },

    renderTip: function (val, meta, rec, rowIndex, colIndex, store) {
        if (val === 1) {
            return '<img data-qtip="' + l('makeIncome') +'" src="themes/default/images/icons/gray/icons/remove.png" />';
        }
        else if (val === 0) {
            return '<img data-qtip="' + l('makeOutGo') +'" src="themes/default/images/icons/gray/icons/ok.png" />';
        } else {
            meta.tdAttr = 'data-qtip="' + val + '"';
        }
        return val;
    }
});