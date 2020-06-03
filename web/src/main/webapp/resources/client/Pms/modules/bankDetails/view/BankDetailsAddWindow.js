Ext.define('Pms.modules.bankDetails.view.BankDetailsAddWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.bankDetailsAddWindow',
    requires: ['Pms.abstract.field.lookup.Picker'],
    title: l('bankDetails.information'),
    closeAction: 'destroy',
    width: 900,
    height: 425,
    autoScroll: false,

    initComponent: function () {
        var me = this;

        this.items = [
            {
                xtype: 'bankDetailsForm'
            }
        ];

        this.buttons = [
            {
                text: l('save.btn'),
                action: 'add-bankDetails',
                requestDisable: true
            },
            {
                text: l('cancel.btn'),
                scope: this,
                handler: this.close
            }
        ];

        this.callParent();
    }
});