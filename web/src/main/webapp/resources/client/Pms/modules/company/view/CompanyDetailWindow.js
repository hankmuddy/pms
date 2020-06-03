Ext.define('Pms.modules.company.view.CompanyDetailWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.companyDetailWindow',
    title: l('companyInfo'),
    autoShow: false,
    width: 700,
    height: 450,

    initComponent: function () {
        this.items = [
            {
                xtype: 'companyForm'
            }
        ];
        this.callParent(arguments);
    }
});