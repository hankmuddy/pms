Ext.define('Pms.modules.group.view.GroupGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.groupGrid',
    store: 'Pms.modules.group.store.Group',

    initComponent: function () {
        var me = this,
            sourceTypes = [],
            visitPurposes = [],
            checkinTypes = [];

        me.columns = [
            {
                header: '№',
                dataIndex: 'id',
                width: 30
            },
            {
                header: l('roomUse'),
                dataIndex: 'room',
                flex: 1
            },
            {
                header: l('adults'),
                dataIndex: 'groupMember',
                flex: 1,
                renderer: function (val) {
                    var items = '';
                    Ext.each(val, function (value) {
                        if (value['type'] == 'adult')
                            items += value['firstName'] + ' ' + value['lastName'] + '<br />';
                    });
                    return items;
                }
            },
            {
                header: l('children'),
                dataIndex: 'groupMember',
                flex: 1,
                renderer: function (val) {
                    var items = '';
                    Ext.each(val, function (value) {
                        if (value['type'] == 'child')
                            items += value['firstName'] + ' ' + value['lastName'] + '<br />';
                    });
                    if (items == '') return '&mdash;';
                    return items;
                }
            },
            {
                header: l('masterPerson'),
                dataIndex: 'customer',
                flex: 1,
                renderer: function (val) {
                    if (val == null) return '&mdash;';
                    return val['firstName'] + ' ' + val['lastName'];
                }
            },
            {
                header: l('company'),
                dataIndex: 'company',
                flex: 1,
                renderer: function (val) {
                    if (val == null) return '&mdash;';
                    return val['name'];
                }
            },
            {
                header: l('payer'),
                dataIndex: 'customer',
                flex: 1,
                renderer: function (v) {
                    if (v == null) return l('company');
                    return l('mainGuest');
                }
            }
        ];
        me.callParent();
    }
});
