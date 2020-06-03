Ext.define('Pms.desktop.TrayClock', {
    extend: 'Ext.toolbar.TextItem',

    alias: 'widget.trayclock',
    cls: 'pms-desktop-trayclock',
    html: '&#160;',

    timeFormat: 'd M Y H:i:s',

    tpl: '{time}',

    initComponent: function () {
        this.callParent();

        if (typeof(this.tpl) === 'string') {
            this.tpl = new Ext.XTemplate(this.tpl);
        }
    },

    afterRender: function () {
        Ext.Function.defer(this.updateTime, 500, this);
        this.callParent();
    },

    onDestroy: function () {
        if (this.timer) {
            window.clearTimeout(this.timer);
            this.timer = null;
        }

        this.callParent();
    },

    updateTime: function () {
        var _date = new Date(),
            time = Ext.Date.format(new Date(Pms.fromUTC(_date, true)), this.timeFormat),
            text = this.tpl.apply({ time: time });
        if (this.lastText !== text) {
            this.setText(text);
            this.lastText = text;
        }
        this.timer = Ext.Function.defer(this.updateTime, 500, this);
    }
});