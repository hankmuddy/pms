Ext.define('Pms.modules.bill.view.payerSelectWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.payerSelectWindow',
    width: 300,
    bill: null,

    initComponent: function () {
        var me = this;

        this.items = [
            {
                xtype: 'form',
                region: 'center',
                bodyStyle: {
                    padding: '3px'
                },
                items: [
                    {
                        xtype: 'combobox',
                        fieldLabel: l('payer'),
                        store: Ext.create('Pms.modules.person.store.GroupMember', {
                            filterParams: [
                                {
                                    field: 'customerGroup.id',
                                    comparison: 'eq',
                                    data: {
                                        type: 'numeric',
                                        value: this.bill.roomUse ? this.bill.roomUse.customerGroup.id : this.bill.group.id
                                    }
                                },
                                {
                                    field: 'person.type',
                                    comparison: 'eq',
                                    data: {
                                        type: 'string',
                                        value: 'adult'
                                    }
                                }
                            ]
                        }),
                        name: 'payer',
                        displayField: 'lastName',
                        valueField: 'id',
                        anchor: '100%',
                        emptyText: l('lastName'),
                        querymode: 'remote',
                        listConfig: {
                            loadingText: l('searchText'),
                            emptyText: l('error.noMatches'),
                            getInnerTpl: function () {
                                return '<div class="search-item"><span><b>{lastName} {firstName}</b></span></div>';
                            }
                        }
                    }
                ]
            }
        ];

        me.buttons = [
            {
                text: l('ok.btn'),
                scope: this,
                handler: function () {
                    var me = this,
                        data = {bill: me.bill},
                        payerCombo = me.down('combobox[name=payer]');
                    if (payerCombo.getValue()) data.payerId = payerCombo.getValue();

                    if (me.down('form').isValid()) {
                        this.close();
                        Ext.widget('billInvoiceWindow', {data: data});
                        Pms.createPreloader();
                    }
                    else {
                        Ext.Msg.alert(l('error'), Pms.iconError + l('requestParsing.incorrectValue'));
                    }
                }
            }
        ];
        this.callParent();
    }
});