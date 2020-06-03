Ext.define('Pms.modules.bankDetails.view.BankDetailsEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.bankDetailsEditWindow',
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
                action: 'save-bankDetails',
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