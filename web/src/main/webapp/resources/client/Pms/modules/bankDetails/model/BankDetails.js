Ext.define("Pms.modules.bankDetails.model.BankDetails", {
    extend: "Ext.data.Model",
    alias: 'widget.bankDetailsModel',

    fields: [
        'id',
        'name',
        Pms.stringFieldUseNull('holder'),
        {name: 'bankName', useNull: true},
        {name: 'account', type: 'int', useNull: true},
        {name: 'edrpou', type: 'int', useNull: true},
        {name: 'mfo', type: 'int', useNull: true},
        {name: 'defaultDetails', persist: false},
        {name: 'blocked', persist: false},
        {name: 'description', useNull: true},
        'paymentType',
        Pms.stringFieldUseNull('swift'),
        Pms.stringFieldUseNull('bankAddress'),
        Pms.stringFieldUseNull('corrBankName'),
        Pms.stringFieldUseNull('corrAccount'),
        Pms.stringFieldUseNull('corrSwift'),
        Pms.stringFieldUseNull('corrBankAddress'),
        {name: 'cardNumber', type: 'int', useNull: 'true'},
        Pms.stringFieldUseNull('additional'),
        Pms.stringFieldUseNull('iban')
    ]
});