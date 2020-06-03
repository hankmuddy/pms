Ext.define('Pms.modules.bill.view.billInvoiceWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.billInvoiceWindow',
    title: l('bill.print'),
    width: 800,
    height: 600,
    data: null,

    initComponent: function () {
//        Pms.createPreloader();
        var me = this,
            html = '<object width="100%" onload="Pms.removePreloader()" height="100%" data="report/invoice?billId=' + me.data.bill.id;


        if (me.data.payerId) html += '&payerId=' + me.data.payerId;
        html += '"></object>';

        me.items = [
            {
                xtype: 'panel',
                height: '100%',
                width: '100%',
                html: html
            }
        ];

        me.buttons = [
            {
                text: l('cancel.btn'),
                scope: me,
                handler: me.close
            }
        ];

        me.callParent(arguments);
    }
});