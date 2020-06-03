Ext.define('Pms.desktop.Wallpaper', {
    extend: 'Ext.Component',
    alternateClassName: 'Pms.Wallpaper',

    statics: {
        CENTERED: "centered",
        TILED: "tiled",
        STRETCH: "stretch"
    },
    cls: "pms-wallpaper",
    stretch: true,
    mode: "stretch",
    wallpaper: null,

    initComponent: function () {
        var me = this;
        me.html = '<img class="wotermark" src="' + me.wallpaper + '">';
        me.callParent();
    },

    afterRender: function () {
        var me = this;
        me.callParent();

//        if (me.wallpaper) {
//            me.setWallpaper(me.wallpaper, me.mode);
//        }
    },

    applyState: function () {
        var me = this,
            old = me.wallpaper;

        me.callParent(arguments);

        if (old != me.wallpaper) {
            me.setWallpaper(me.wallpaper);
        }
    },

    getState: function () {
        return this.wallpaper && { wallpaper: this.wallpaper };
    },

    setWallpaper: function (wallpaper, mode) {
        var me = this, imgEl, bkgnd;

        me.mode = mode;
        me.wallpaper = wallpaper;

        if (me.rendered) {
            imgEl = me.el.dom.firstChild;

            Ext.fly(imgEl).hide();

            if (me.mode === "stretch") {
                imgEl.src = wallpaper;

                me.el.removeCls('pms-wallpaper-tiled');
                me.el.removeCls('pms-wallpaper-centered');
                Ext.fly(imgEl).setStyle({
                    width: '100%',
                    height: '100%'
                }).show();
            } else if (me.mode === "centered") {
                bkgnd = 'url(' + wallpaper + ')';

                me.el.removeCls('pms-wallpaper-tiled');
                me.el.addCls('pms-wallpaper-centered');
            } else {
                bkgnd = 'url(' + wallpaper + ')';

                me.el.removeCls('pms-wallpaper-centered');
                me.el.addCls('pms-wallpaper-tiled');
            }

            me.el.setStyle({
                backgroundImage: bkgnd || ''
            });
        }
        return me;
    }
});