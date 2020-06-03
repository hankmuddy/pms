Ext.define("Pms.desktop.Notification", {
    extend: "Ext.Component",
    config: {
        message: "This is a message!",
        tittle: l('notification.systemNotification'),
        icon: '',
        time: 4000
    },
    statics: {
        Manager: {
            notifications: [],
            el: null
        }
    },
    cls: "pms-notification",
    floating: true,
    width: 300,
    height: 100,
    bodyPadding: 10,
    closable: true,
    position: 'tr',

    corner: "br", //br, bl, tr, tl
    slideInFx: "bounceIn",
    slideOutFx: 'bounceOut',
    data: {
        message: "Error",
        tittle: l('notification.systemNotification'),
        icon: ''
    },

    tpl: [
        '<div class="pms-notification-outside-wrapper">',
        '<div class="pms-notification-inside-wrapper">',
        '<div class="pms-notification-outside">',
        '<div class="pms-notification-inside">',
        '<div class="pms-notification-icon">{icon}</div>',
        '<div class="pms-notification-text">',
        '<span>{tittle}</span><br/><br/>{message}',
        '</div>',
        '<div class="pms-notification-close"><!--<img src="themes/default/images/icons/gray/icons/notification-close.png" />--></div>',
        '</div>',
        '</div>',
        '</div>',
        '</div>'
    ],

    renderSelectors: {
        closeBtn: '.pms-notification-close'
    },

    constructor: function (config) {
        var me = this;
        me.data.message = config.message;
        me.data.tittle = config.tittle || l('notification.systemNotification');
        me.data.icon = config.icon || Pms.notificationError;
        this.initConfig(config);
        this.callParent(arguments);
        return this;
    },

    initComponent: function () {
        var me = this,
            size = Ext.getBody().getViewSize();

        me.renderTo = Ext.getBody();

        me.x = size.width - me.width - 15;
        me.y = 15;
        me.callParent();

        me.doClose = function () {
            me.doClose = Ext.emptyFn;
            me.el.disableShadow();
            me.el.fadeOut({
                listeners: {
                    afteranimate: function () {
                        me.destroy();
                    }
                }
            });
        };

        setTimeout(function () {
            me.destroy();
        }, me.getTime());
    },

    close: function () {
        var me = this;
        me.el.destroy();
    }
});

/**
 * Created by etipalchuk on 17.01.14.
 * http://www.eirik.net/Ext/ux/window/Demo.html
 */

//Ext.define("Pms.desktop.Notification", {
//    extend: "Ext.Component",
//    requires: [
//        'Ext.ux.window.Notification'
//    ],
//
//    data: {
//        message: "Error"
//    },
//
//    constructor: function (config) {
//        var me = this;
//        me.data.message = config.message;

//        Ext.create('widget.uxNotification', {
//            title: 'Notification',
//            position: 'tr',
//            manager: 'instructions',
//            cls: 'ux-notification-light',
//            iconCls: 'ux-notification-icon-information',
//            html: config.message,
//            autoCloseDelay: 1000000,
//            slideBackDuration: 500,
//            slideInAnimation: 'bounceOut',
//            slideBackAnimation: 'easeIn'
//        }).show();
//        Ext.Msg.alert('', me.data.message);
//
//        this.initConfig(config);
//        this.callParent(arguments);
//        return this;
//    }
//});
