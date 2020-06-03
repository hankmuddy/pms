Ext.define('Pms.modules.accommodation.view.AccommodationEditWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.accommodationEditWindow',
    width: 600,
    height: 550,
    border: 0,
    title: l('editAccommodation'),
    closeAction: 'destroy',
    data: null,

    initComponent: function () {
        var me = this;

        me.items = {
            xtype: 'tabpanel',
            layout: 'fit',
            height: '100%',
            items: [
                {
                    title: l('accommodationInfo'),
                    items: [
                        {
                            xtype: 'accommodationForm',
                            layout: 'fit',
                            height: '100%'
                        }
                    ]
                },
                {
                    title: l('rooms'),
                    items: [
                        {
                            xtype: 'roomGrid',
                            paging: false,
                            loadParams: {
                                params: Pms.Ajax.encode({
                                    filter: [
                                        {
                                            field: 'accommodation.id',
                                            comparison: 'eq',
                                            data: {
                                                type: 'numeric',
                                                value: me.data.id
                                            }
                                        }
                                    ]
                                })
                            }
                        }
                    ]
                }
            ]
        };

        me.buttons = [
            {
                text: l('save.btn'),
                action: 'save',
                requestDisable: true
            },
            {
                text: l('cancel.btn'),
                action: 'close',
                scope: this,
                handler: this.close
            }
        ];

        me.callParent();
    }
});