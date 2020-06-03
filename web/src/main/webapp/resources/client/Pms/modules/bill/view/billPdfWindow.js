Ext.define('Pms.modules.bill.view.billPdfWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.billPdfWindow',
    title: l('bill.print'),
    width: 800,
    height: 600,
    data: null,

    initComponent: function () {

        var me = this,
            html = '<object width="100%" onload="Pms.removePreloader()" height="100%" data="report/hotelBill?billId=' + me.data.bill.id;
        if (me.data.bankDetails) html += '&bankDetailId=' + me.data.bankDetails.id;
        if (me.data.prePayment) html += '&prePayment=' + me.data.prePayment;
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