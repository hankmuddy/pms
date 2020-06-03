Ext.define('Pms.desktop.Hints', {
    requires: ['Pms.abstract.HintBox'],

    constructor: function () {
        var Hint = this;

//        Hint.sayHello();

        Ext.Function.interceptBefore(Ext.form.Field.prototype, 'initComponent', function () {
            var me = this;
            if (!Ext.isEmpty(me.hint) && !Ext.isEmpty(me.fieldLabel)) {
                me.fieldLabel = me.fieldLabel + '<span id="hint-' + me.getId() + '" class="hint-box">?</span>';
                me.labelWidth = me.labelWidth + 19;
            }
        });

        Ext.Function.interceptBefore(Ext.button.Button.prototype, 'initComponent', function () {
            var me = this;
            if (!Ext.isEmpty(me.hint) && !Ext.isEmpty(me.text)) {
                me.text = me.text + '<span id="hint-' + me.getId() + '" class="hint-box">?</span>';
            }
        });

        Ext.Function.interceptBefore(Ext.form.FieldSet.prototype, 'initComponent', function () {
            var me = this;
            if (!Ext.isEmpty(me.hint) && !Ext.isEmpty(me.title)) {
                me.title = me.title + '<span id="hint-' + me.getId() + '" class="hint-box">?</span>';
            }
        });

        Ext.Function.interceptAfter(Ext.panel.Panel.prototype, 'initComponent', function () {
            var me = this;

            if (!Ext.isEmpty(me.hint) && !Ext.isEmpty(me.title)) {
                me.title = me.title + '<span id="hint-' + me.getId() + '" class="hint-box">?</span>';

                me.listeners = {
                    render: function (cmp) {
                        Hint.showHint(cmp);
                    }
                };
            }
        });
    },

    sayHello: function () {
        var win = Ext.create('Ext.Window', {
            title: l('hints.sayHello'),
            width: 400,
            height: 200,
            layout: 'fit',
            items: {
                border: false
            },
            tpl: [
                l('hints.disableMessage')
            ],
            bbar: [
                {
                    text: l('next') + ' >>',
                    handler: function () {
                        console.log('next');
                    }
                },
                {
                    text: l('close.btn'),
                    handler: this.close
                }
            ]
        }).center().show();
    },

    showHint: function (cmp) {
        var Hint = this;

        cmp.focus();

        Ext.create('Ext.tip.ToolTip', {
            html: cmp.hint,
            title: cmp.title || cmp.text || cmp.fieldLabel || null,
            anchor: 'left',
            shadow: true,
            autoShow: true,
            autoHide: false,
            width: '250px',
            minHeight: '150px',
            target: 'hint-' + cmp.getId(),
            ahchorTo: 'hint-' + cmp.getId(),
            bbar: [
                {
                    text: l('prev.btn'),
                    handler: function (btn, e) {
                        Hint.switchHint(cmp, btn, true);
                    }
                },
                {
                    text: l('next.btn'),
                    handler: function (btn, e) {
                        Hint.switchHint(cmp, btn);
                    }
                },
                {
                    text: l('close.btn'),
                    handler: function (btn, e) {
                        btn.up('tooltip').close();
                    }
                }
            ]
        });
    },

    switchHint: function (cmp, btn, prev) {
        var Hint = this,
            node = prev ? cmp.previousNode('[hint][hidden=false]') : cmp.nextNode('[hint][hidden=false]'),
            relative;

        if (prev) relative = cmp.up ? cmp.up('[hint][hidden=false][rendered=true]') : null;
        else relative = cmp.down ? cmp.down('[hint][hidden=false][rendered=true]') : null;

        if (relative) {
            console.log(relative);
            Hint.showHint(relative);
//            cmp.fireEvent('showHint' + me.getId().replace('-', ''), relative);
        }
        else if (node) {
            console.log(node);
            Hint.showHint(node);
//            cmp.fireEvent('showHint' + me.getId().replace('-', ''), node);
        }
        btn.up('tooltip').close();
    }
});