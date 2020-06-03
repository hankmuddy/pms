Ext.define('Pms.modules.group.view.GroupGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.groupGrid',
    store: 'Pms.modules.group.store.Group',

    border: false,
    editable: true,
    paging: true,
    forceFit: true,

    initComponent: function () {
        var me = this;

        me.columns = [
            {
                header: '№',
                dataIndex: 'id',
                width: 30
            },
            {
                header: l('roomUse'),
                dataIndex: 'property_rooms_use',
                flex: 1
            },
            {
                header: l('adults'),
                dataIndex: 'property_persons',
                flex: 1,
                renderer: function (v) {
                    var items = '';
                    Ext.each(v, function (val) {
                        items += val['property_name'] + val['property_surname'] + '<br />';
                    });
                    return items;
                }
            },
            {
                header: l('children'),
                dataIndex: 'property_children',
                flex: 1,
                renderer: function (v) {
                    var items = '';
                    Ext.each(v, function (val) {
                        items += val['property_name'] + val['property_surname'] + '<br />';
                    });
                    return items;
                }
            },
            {
                header: l('masterPerson'),
                dataIndex: 'property_master_person',
                flex: 1,
                renderer: function (v) {
                    return v[0]['property_name'] + v[0]['property_surname'];
                }
            },
            {
                header: l('company'),
                dataIndex: 'property_master_company',
                flex: 1,
                renderer: function (v) {
                    if (v == null) return '&mdash;';
                    return v[0]['property_name'];
                }
            },
            {
                header: l('payer'),
                dataIndex: 'property_def_payer',
                flex: 1,
                renderer: function (v) {
                    return v == 'master_company' ? l('company') : l('mainGuest');
                }
            },
            {
                header: l('services'),
                dataIndex: 'property_service_use',
                flex: 1
            },
            {
                header: l('paid'),
                dataIndex: 'property_paid',
                flex: 1
            },
            {
                header: l('paidTill'),
                dataIndex: 'property_paid_till',
                flex: 1
            },
            {
                header: l('paidFully'),
                dataIndex: 'property_paid_fully',
                flex: 1
            },
            {
                header: l('totalAmount'),
                dataIndex: 'property_total_amount',
                flex: 1
            }
        ];
        me.callParent();
    }
});