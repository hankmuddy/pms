Ext.define('Pms.modules.periodInfo.view.PeriodInfoAddWindow', {
    extend: 'Pms.abstract.Window',
    requires: ['Ext.form.Panel'],
    alias: 'widget.periodInfoAddWindow',

    title: l('periodInfo.addWinTitle'),
    width: 400,
    height: 200,
    listeners: {
        afterRender: function (thisForm, options) {
            this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
                enter: function () {
                    if (!thisForm.down('combobox[isExpanded=true]'))
                        this.down('button[action=add-roomType]').fireHandler()
                },
                scope: this
            });
        }
    },

    initComponent: function () {
        var me = this;

        this.items = [
            {
                xtype: 'periodInfoForm'
            }
        ];

        this.buttons = [
            {
                text: l('save.btn'),
                action: 'add-periodInfo',
                requestDisable: true
            },
            {
                text: l('cancel.btn'),
                scope: this,
                handler: this.close
            }
        ];

        me.callParent();
    }
});