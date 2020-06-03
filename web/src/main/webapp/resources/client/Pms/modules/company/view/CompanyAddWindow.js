Ext.define('Pms.modules.company.view.CompanyAddWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.companyAddWindow',
    title: l('newCompany'),
    width: 1020,
    height: 600,

    initComponent: function () {
        this.items = [
            {
                xtype: 'companyForm'
            }
        ];
        this.callParent(arguments);
    }
});