Ext.define('Pms.modules.virtualPlan.view.VirtualPlanAddWindow', {
    extend: 'Pms.abstract.Window',
    requires: ['Ext.form.Panel'],
    alias: 'widget.virtualPlanAddWindow',
    title: l('virtualPlan.addWinTitle'),
    width: 300,
    height: 210,

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
                xtype: 'virtualPlanForm'
            }
        ];

        this.buttons = [
            {
                text: l('save.btn'),
                action: 'add-virtualPlan',
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