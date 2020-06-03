Ext.define('Pms.modules.plan.view.PlanAddWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.planAddWindow',
    title: l('plan.new'),
    resizable: false,
    autoShow: false,
    width: 370,
    height: 150,
    data: {},

    listeners: {
        afterRender: function (thisForm, options) {
            this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
                enter: function () {
                    if (!thisForm.down('combobox[isExpanded=true]'))
                        this.down('button[action=save]').fireHandler()
                },
                scope: this
            });
        }
    },

    initComponent: function () {
        var me = this;

        me.items = [
            {
                xtype: 'planForm',
                data: me.data
            }
        ];

        me.buttons = [
            {
                text: l('save.btn'),
                iconCls: 'app-icon-ok',
                action: 'save',
                requestDisable: true
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