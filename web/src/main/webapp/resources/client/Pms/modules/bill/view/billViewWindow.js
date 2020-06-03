Ext.define('Pms.modules.bill.view.billViewWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.billViewWindow',

    title: l('bill.print'),
    width: 800,
    height: 600,
    defLogo: '/themes/default/images/logo_slogan.png',

    bill: null,
    settings: null,
    bankDetails: null,
    groupMember: null,

    initComponent: function () {
        var me = this,
            bill = me.bill;
        settings = me.settings.hotelInfo,
            logo = settings.logo ? _('imagesUrlPrefix') + settings.logo : this.defLogo,
            serviceUses = bill.serviceUses,
            customerGroup = bill.roomUse ? bill.roomUse.customerGroup : bill.group,
            bankDetails = me.bankDetails || {account: '&mdash;', bankName: '&mdash;', mfo: '&mdash;', edrpou: '&mdash;'},
//            requisite = bill.property_def_requisite,
            payer,
            customerEmail = customerGroup.customer ? customerGroup.customer.email : customerGroup.company.email,
            discount = 0;
        if (customerGroup.company) payer = customerGroup.company.name;
        else if (bill.forCustomer) payer = customerGroup.customer.firstName + ' ' + customerGroup.customer.lastName;
        else if (!me.groupMember) payer = '';
        else  payer = me.groupMember.firstName + ' ' + me.groupMember.lastName;
        var html = '<!DOCTYPE html>' +
            '<html>' +
            '<head>' +
            '<meta charset="UTF-8" />' +
            '<link rel="stylesheet" type="text/css" href="/themes/default/css/bill.css">' +
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
            '<tr><td>' + l('hotel.phone') + ': ' + (settings.accountPhone || '&mdash;') + '</td></tr>' +
//                '<tr><td>' + bankDetails.holder + '</td></tr>' +
            '<tr><td>' + l('bankDetails') + ': ' + bankDetails.account + '</td></tr>' +
            '<tr><td>' + l('bank') + ': ' + bankDetails.bankName + '</td></tr>' +
            '<tr><td>' + l('mfo') + ': ' + bankDetails.mfo + '</td></tr>' +
            '<tr><td>' + l('edrpou') + ': ' + bankDetails.edrpou + '</td></tr>' +
            '</tbody>' +
            '</table>' +
            '</div>';
//        }
        html += '</div>' +

            '<div class="invoice-info">' +
            '<div class="client-billing">' +
            '<div class="unvoice-number">' + l('bill') + ' № ' + bill.id + '</div>' +
            l('date') + ': ' + Ext.Date.format(new Date(bill.createdDate * 1000), 'd/m/Y') +
            '<br/>' +
            l('payer') + ': ' + payer;

        html += '</div>' +
            '<table  border="1" cellpadding="5" cellspacing="0" class="client-payment">' +
            '<tbody>' +
            '<tr>' +
            '<th>' + l('serviceUse.name') + '</th>' +
            '<th>' + l('serviceUse.singlePrice') + '</th>' +
            '<th>' + l('serviceUse.quantity') + '</th>' +
            '<th>' + l('sum') + ', грн.</th>' +
            '</tr>';
        for (var i in serviceUses) {
            if (serviceUses[i].type == 'livingUse') {
                discount += serviceUses[i].rawTotal * customerGroup.discount / 100;
            }
            serviceTitle = serviceUses[i].service.title || l('company.livingTab');
            serviceUses[i].refund ? refund = '<tr class="service-use-refund">' : refund = '<tr>';
            html += refund +
                '<td>' + serviceTitle + '</td>' +
                '<td>' + Ext.util.Format.number(serviceUses[i].rawTotal / serviceUses[i].quantity / 100, '0.00') + '</td>' +
                '<td>' + serviceUses[i].quantity + '</td>' +
                '<td>' + Ext.util.Format.number(serviceUses[i].total / 100, '0.00') + '</td>' +
                '</tr>';
        }
        html += '<tr>' +
            '<td colspan="3" align="right"><strong>' + l("total") + ':</strong></td>' +
            '<td><strong>' + Ext.util.Format.number((bill.total + discount) / 100, '0.00') + '</strong></td>' +
            '</tr>';
        if (discount) {
            html +=
                '<tr>' +
                    '<td colspan="3" align="right"><strong>' + l("discount") + ':</strong></td>' +
                    '<td><strong>' + Ext.util.Format.number(discount / 100, '0.00') + ' (' + customerGroup.discount + '%)' + '</strong></td>' +
                    '</tr>';
        }
        if (me.prePayment) {
            html +=
                '<tr>' +
                    '<td colspan="3" align="right"><strong>' + l("bill.prePayment") + ':</strong></td>' +
                    '<td><strong>' + Ext.util.Format.number(bill.total * me.prePayment / 100 / 100, '0.00') + ' (' + me.prePayment + '%)' + '</strong></td>' +
                    '</tr>';
        }
        html +=
            '<tr>' +
                '<td colspan="3" align="right"><strong>' + l("totalToPay") + ':</strong></td>' +
                '<td><strong>' + Ext.util.Format.number(bill.total / 100, '0.00') + '</strong></td>' +
                '</tr>' +
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
                text: l('booking.send'),
                scope: me,
                handler: function () {
                    Pms.Ajax.request({
                        url: 'rest/mail',
                        method: 'POST',
                        jsonData: {
                            to: customerEmail,
                            subject: l('bill.prePayment'),
                            content: html
                        }
                    })
                }
            },
            {
                text: l('printBill'),
                scope: me,
                handler: function () {
//                    if (payer == '') {
//                        Pms.App.showNotification({message: Pms.iconError + ' ' + l('bill.unableToPrintWithoutPayer')});
//                        return;
//                    }
                    var el = document.createElement('div');
                    el.innerHTML = html
                    Pms.Print.print(el, true);
                }
            },
            {
                text: l('cancel.btn'),
                scope: me,
                handler: me.close
            }
        ];

        me.callParent(arguments);
    }
});