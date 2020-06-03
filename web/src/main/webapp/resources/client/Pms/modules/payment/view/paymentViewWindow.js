Ext.define('Pms.modules.payment.view.paymentViewWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.paymentViewWindow',

    title: l('payment.view'),
    width: 800,
    height: 600,
    payment: null,
    defLogo: '/themes/default/images/logo_slogan.png',

    initComponent: function () {
        var me = this,
            bill = me.payment.bill,
            serviceUses = bill.serviceUses,
            bankDetails = me.bankDetails,
            customerGroup = bill.group ? bill.group : bill.roomUse.customerGroup,
            settings = me.settings.hotelInfo,
            logo = settings.logo ? _('imagesUrlPrefix') + settings.logo : this.defLogo,
            discount = 0,
//            requisite = bill.property_def_requisite,
            payer = customerGroup.company ? customerGroup.company.name : customerGroup.customer.firstName + ' ' + customerGroup.customer.lastName;
        var html = '<!DOCTYPE html>' +
            '<html>' +
            '<head>' +
            '<meta charset="UTF-8" />' +
            '<link rel="stylesheet" type="text/css" href="themes/default/css/bill.css">' +
            '<title>' + l('bill') + '</title>' +
            '</head>' +
            '<body>' +
            '<div class="wrapper-invoice">' +
            '<div class="header-invoice">' +
            '<div class="hotel-logo">' +
            '<img src="' + logo + '" alt="logo-hotel" />' +
            '</div>' +
            '<div class="hotel-payment-info">' +
            '<table border="0" cellpadding="3" cellspacing="0">' +
            '<tbody>' +
            '<tr><td><strong>' + settings.name + '</strong></td></tr>' +
            '<tr><td>' + l('hotel.address') + ': ' + settings.officialAddress + '</td></tr>' +
            '<tr><td>' + l('hotel.phone') + ': ' + settings.accountPhone + '</td></tr>' +
//                '<tr><td>' + bankDetails.holder + '</td></tr>' +
            '<tr><td>' + l('bankDetails') + ': ' + bankDetails.account + '</td></tr>' +
            '<tr><td>' + l('bank') + ': ' + bankDetails.bankName + '</td></tr>' +
            '<tr><td>' + l('mfo') + ': ' + bankDetails.mfo + '</td></tr>' +
            '<tr><td>' + l('edrpou') + ': ' + bankDetails.edrpou + '</td></tr>' +
            '</tbody>' +
            '</table>' +
            '</div>';

        html += '</div>' +

            '<div class="invoice-info">' +
            '<div class="client-billing">' +
            '<div class="unvoice-number">' + l('payment.assignment') + ' № ' + me.payment.id + '</div>' +
            l('bill') + ' №' + bill.id +
            '<br/>' +
            l('payment.date') + ': ' + Ext.Date.format(me.payment.date, 'd/m/Y') +
            '<br/>'
        // if(bill.property_room_rate) {
        //     html += '<br/>' +
        //     'Срок проживания: с ' + bill.property_date + '  по ' + bill.property_date;
        // }
        html += '</div>' +
            '<table  border="1" cellpadding="5" cellspacing="0" class="client-payment">' +
            '<tbody>' +
            '<tr>' +
            '<th>' + l('payer') + '</th>' + '<td>' + payer + '</td>' +
            '</tr>' +
            '<tr>' +
            '<th>' + l('paymentType') + '</th>' + '<td>' + l('paymentType.' + bankDetails.paymentType) + '</td>' +
            '</tr>' +
            '<tr>' +
            '<th>' + l('payment.sum') + '</th>' + '<td>' + Ext.util.Format.number(me.payment.total / 100, '0.00') + '</td>' +
            '</tr>';
        html += /*'<tr>' +
         '<td colspan="3" align="right"><strong>Всего:</strong></td>' +
         '<td><strong>' + bill.total + '</strong></td>' +
         '</tr>' +
         '<tr>' +
         '<td colspan="3" align="right"><strong>К оплате:</strong></td>' +
         '<td><strong>' + bill.total + '</strong></td>' +
         '</tr>' +*/
            '</tbody>' +
                '</table>' +
                '</div>' +
                '<div class="signature">' + l('signature') + ': <hr /></div>' +
                '</div>' +
                '</body>' +
                '</html>';

        me.items = [
            {
                xtype: 'panel',
                overflowY: 'scroll',
                html: html
            }
        ];

        me.buttons = [
            {
                text: l('payment.print'),
                scope: me,
                handler: function () {
                    var el = document.createElement('div');
                    el.innerHTML = html
                    Pms.Print.print(el, true);
                }
            },
            {
                text: 'Ок',
                scope: me,
                handler: me.close
            }
        ]

        me.callParent(arguments);
    }
});