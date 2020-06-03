Ext.define('Pms.modules.refund.controller.Refund', {
    extend: 'Pms.abstract.Controller',

    views: [
        'Pms.modules.refund.view.RefundEditWindow',
        'Pms.modules.refund.view.RefundGrid',
        'Pms.modules.refund.view.RefundForm',
        'Pms.modules.refund.view.RefundFilterForm'
    ],
    stores: [
        'Pms.modules.refund.store.Refund'
    ],
    models: [
        'Pms.modules.refund.model.Refund'
    ]
});
