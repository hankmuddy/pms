Ext.define('Pms.modules.company.view.CompanyGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.companyGrid',
    store: 'Pms.modules.company.store.Company',

    border: false,
    editable: true,
    paging: true,

    multiSelect: true,
    forceFit: true,

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
                xtype: 'templatecolumn',
                tpl: '<span style="text-decoration:underline;cursor:pointer;">{name}</span>',
                flex: 2
            },
            {
                header: l('phone'),
                dataIndex: 'phone',
                flex: 1
            },
            {
                header: l('email'),
                dataIndex: 'email',
                flex: 1
            },
            {
                header: l('country'),
                dataIndex: 'country',
                flex: 1,
                renderer: function (val, col) {
                    if (val) {
                        return Ext.create('Pms.modules.person.store.Country')
                            .findRecord('abbr', val)
                            .get('country');
                    } else {
                        return "&mdash;";
                    }
                }
            },
            {
                header: l('city'),
                dataIndex: 'city',
                flex: 1
            },
            {
                header: l('company.inn'),
                dataIndex: 'inn',
                flex: 1,
                hidden: true
            },
            {
                header: l('bankName'),
                dataIndex: 'bankName',
                flex: 1,
                hidden: true
            },
            {
                header: l('company.accountNumber'),
                dataIndex: 'accountNumber',
                flex: 1,
                hidden: true
            },
            {
                header: l('company.bankMfo'),
                dataIndex: 'bankMfo',
                flex: 1,
                hidden: true
            },
            {
                header: l('company.legalAddress'),
                dataIndex: 'legalAddress',
                flex: 1,
                hidden: true
            },
            {
                header: l('company.postAddress'),
                dataIndex: 'postAddress',
                flex: 1,
                hidden: true
            },
            {
                header: l('discount'),
                dataIndex: 'discount',
                flex: 1,
                renderer: function (val) {
                    return val ? val + '%' : '&mdash;'
                }
            }
        ];
        me.callParent();
    }
});