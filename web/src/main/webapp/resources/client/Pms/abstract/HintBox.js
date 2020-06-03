Ext.define('Pms.abstract.HintBox', {
    extend: 'Ext.Component',
    alias: 'widget.hintBox',

    width: 12,
    height: 12,
    style: {
//        display: 'inline-block',
        float: 'right'
    },

    initComponent: function () {
        me = this;

        me.callParent();

        me.html = '<span id="hint-' + me.getId() + '" class="hint-box">?</span>',

            me.listeners = {
                el: {
                    click: function (e, t, eOpts) {
                        if (!Ext.isEmpty(me.hint)) {
                            me.showHint(me);
                        }
                    }
                }
            }
    },

    showHint: function (cmp) {
        var me = this;

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
            closeAction: 'destroy',
            bbar: [
                {
                    text: l('prev.btn'),
                    handler: function (btn, e) {
                        me.switchHint(cmp, btn, true);
                    }
                },
                {
                    text: l('next.btn'),
                    handler: function (btn, e) {
                        me.switchHint(cmp, btn);
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
        var me = this,
            node = prev ? me.previousNode('hintBox[hint][hidden=false]') : me.nextNode('hintBox[hint][hidden=false]'),
            relative;

        if (prev) relative = cmp.up ? cmp.up('hintBox[hint][hidden=false][rendered=true]') : null;
        else relative = cmp.down ? cmp.down('hintBox[hint][hidden=false][rendered=true]') : null;

        if (relative) {
            me.showHint(relative);
        }
        else if (node) {
            me.showHint(node);
        }
        btn.up('tooltip').close();
    }
});