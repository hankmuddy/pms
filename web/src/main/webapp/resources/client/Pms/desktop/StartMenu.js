Ext.define('Pms.desktop.StartMenu', {
    extend: 'Ext.panel.Panel',

    requires: [
        'Ext.menu.Menu',
        'Ext.toolbar.Toolbar',
        'Pms.desktop.SupportWindow',
        'Pms.desktop.AboutWindow',
        'Pms.desktop.HelpWindow',
        'Pms.desktop.UserWindow',
        'Pms.desktop.SettingsWindow'
    ],

    ariaRole: 'menu',

    cls: 'x-menu pms-start-menu',

    defaultAlign: 'bl-tl',

    iconCls: 'user',
    preventHeader: true,
    floating: true,

    text: 'admin',

    shadow: false,

    frame: false,

    title: '',

    preventHeader: true,
    // We have to hardcode a width because the internal Menu cannot drive our width.
    // This is combined with changing the align property of the menu's layout from the
    // typical 'stretchmax' to 'stretch' which allows the the items to fill the menu
    // area.
    width: 232,
    height: 465,

    initComponent: function () {
        var me = this;

//        me.title = l('menu');

        var menu = me.setHandler();

        me.menu = new Ext.menu.Menu({
            cls: 'ux-start-menu-body',
            border: false,
            floating: false,
            items: menu
//            defaults: {
//                height: 30
//            }
        });

        me.menu.layout.align = 'stretch';

        me.items = [me.menu];
        me.layout = 'fit';

        Ext.menu.Manager.register(me);

        me.callParent();

//        var toolbarItems = [
//            {
//                text: l('startMenu.support'),
//                iconCls: 'icon-phone',
//                handler: me.onSupport,
//                scope: me
//            },
//            {
//                text: l('startMenu.faq'),
//                iconCls: 'icon-comments',
//                handler: me.onHelp,
//                scope: me
//            },
//            {
//                text: l('startMenu.notifications'),
//                iconCls: 'icon-comments',
//                handler: me.onNotification,
//                scope: me
//            },
//            {
//                text: l('startMenu.about'),
//                iconCls: 'icon-info',
//                handler: me.onAbout,
//                scope: me
//            },
//            '-',
//            {
//                text: l('startMenu.logout'),
//                iconCls: 'icon-off',
//                handler: me.onLogout,
//                scope: me
//            }
//        ];

        me.toolbar = new Ext.toolbar.Toolbar(Ext.apply(
            {
                dock: 'right',
                cls: 'ux-start-menu-toolbar',
                vertical: true,
                width: 100
            },
            {
                width: 0
//                items: toolbarItems
            }
        ));

        me.toolbar.layout.align = 'stretch';
        me.addDocked(me.toolbar);

        delete me.toolItems;

        me.on('deactivate', function () {
            me.hide();
        });
    },

    menuItemClick: function (rec) {
        Pms.App.runApplication({
            initialConfig: rec
        });
    },

    setHandler: function () {
        var me = this,
            manuItems = [{
                    text: _('name'),
                    iconCls: 'icon-user',
    //                klass: "Pms.modules.settings.controller.User",
                    scope: Pms.App,
                    disabled: _('isAdmin'),
                    cls: 'start-menu-active-user',
                    handler: me.onUserClick,
                    height: 60,
                    extravailable: true,
                    pmsavailable: true
                },
                {
                    text: l('startMenu.hotelSettings'),
                    iconCls: 'icon-start-menu-settings',
                    klass: "Pms.modules.settings.controller.Settings",
                    scope: Pms.App,
                    handler: me.menuItemClick,
                    hint: l('startMenu.hotelSettings') + '. ' + l('hints'),
                    height: 35,
                    extravailable: true,
                    pmsavailable: true
                },
                {
                    text: Pms.App.app.isExtranet() === true ? l('startMenu.roomType') : l('startMenu.roomsNTypes'),
                    iconCls: 'icon-tasks',
                    klass: "Pms.modules.roomType.controller.RoomType",
                    scope: Pms.App,
                    handler: me.menuItemClick,
                    height: 35,
                    extravailable: true,
                    pmsavailable: true
                },
                {
                    text: l('desktop.plan'),
                    iconCls: 'icon-calendar',
                    klass: "Pms.modules.plan.controller.Plan",
                    scope: Pms.App,
                    handler: me.menuItemClick,
                    height: 35,
                    extravailable: true,
                    pmsavailable: true
                },
                {
                    text: l('desktop.quota'),
                    iconCls: 'icon-calendar',
                    klass: "Pms.modules.quota.controller.Quota",
                    scope: Pms.App,
                    handler: me.menuItemClick,
                    height: 35,
                    extravailable: true,
                    pmsavailable: false
                },
                {
                    text: l('startMenu.services'),
                    iconCls: 'icon-tariff',
                    klass: "Pms.modules.catalog.controller.Catalog",
                    scope: Pms.App,
                    handler: me.menuItemClick,
                    height: 35,
                    extravailable: false,
                    pmsavailable: true
                },
    //                {
    //                    text: l('desktop.report'),
    //                    iconCls: 'icon-report',
    //                    klass: "Pms.modules.report.controller.Report",
    //                    scope: Pms.App,
    //                    handler: me.menuItemClick,
    //                    height: 35,
    //                    extravailable: false,
    //                    pmsavailable: true
    //                },
                {
                    text: l('desktop.analytics'),
                    iconCls: 'icon-report',
                    klass: "Pms.modules.analytics.controller.Analytics",
                    scope: Pms.App,
                    handler: me.menuItemClick,
                    height: 35,
                    extravailable: false,
                    pmsavailable: true
                },
                {
                    text: l('startMenu.usersAndRoles'),
                    iconCls: 'icon-users',
                    klass: "Pms.modules.user.controller.User",
                    scope: Pms.App,
                    handler: me.menuItemClick,
                    height: 35,
                    extravailable: false,
                    pmsavailable: true
                },
                {
                    text: l('startMenu.support'),
                    iconCls: 'icon-phone',
                    handler: me.onSupport,
                    scope: me,
                    height: 35,
                    extravailable: false,
                    pmsavailable: true
                },
    //            {
    //                text: l('startMenu.faq'),
    //                iconCls: 'icon-faq',
    //                handler: me.onHelp,
    //                scope: me,
    //                height: 30,
    //                extravailable: false,
    //                pmsavailable: true
    //            },
                {
                    text: l('startMenu.notifications'),
                    iconCls: 'icon-notifications',
                    handler: me.onNotification,
                    scope: me,
                    height: 35,
                    extravailable: false,
                    pmsavailable: true
                },
                {
                    text: l('startMenu.about'),
                    iconCls: 'icon-info',
                    handler: me.onAbout,
                    scope: me,
                    height: 35,
                    extravailable: true,
                    pmsavailable: true
                },
    //            '-',
                {
                    text: l('startMenu.logout'),
                    iconCls: 'icon-off',
                    handler: me.onLogout,
                    scope: me,
                    height: 35,
                    extravailable: true,
                    pmsavailable: true
            }],
            availableItems = [];

        for(var i in manuItems) {
            if(!(Pms.App.app.isExtranet() === true && manuItems[i].extravailable === false || Pms.App.app.isExtranet() === false && manuItems[i].pmsavailable === false)) availableItems.push(manuItems[i]);
        }

        return availableItems;
    },

    addMenuItem: function () {
        var cmp = this.menu;
        cmp.add.apply(cmp, arguments);
    },

    addToolItem: function () {
        var cmp = this.toolbar;
        cmp.add.apply(cmp, arguments);
    },

    showBy: function (cmp, pos, off) {
        var me = this;

        if (me.floating && cmp) {
            me.layout.autoSize = true;
            me.show();

            // Component or Element
            cmp = cmp.el || cmp;

            // Convert absolute to floatParent-relative coordinates if necessary.
            var xy = me.el.getAlignToXY(cmp, pos || me.defaultAlign, off);
            if (me.floatParent) {
                var r = me.floatParent.getTargetEl().getViewRegion();
                switch (me.position) {
                    case "top"        :
                        xy[0] -= r.x;
                        xy[1] = r.bottom - r.y / 2;
                        break;
                    case "bottom"    :
                        xy[0] -= r.x;
                        xy[1] -= r.y;
                        break;
                    default    :
                        break;
                }

            }
            me.showAt(xy);
            me.doConstrain();
        }
        return me;
    },

    onAbout: function () {
        var win = Ext.widget('aboutWindow');
        win.show();
    },

    onSupport: function () {
        var win = Ext.widget('supportWindow');
        win.show();
    },

    onNotification: function () {
        if (window.webkitNotifications) {
            if (window.webkitNotifications.checkPermission() == 1) { // 0 is PERMISSION_ALLOWED
                window.webkitNotifications.requestPermission()
            }
            else {
                var notification = window.webkitNotifications.createNotification('https://cdn1.iconfinder.com/data/icons/perfect-flat-icons-2/512/Info_information_user_about_card_button_symbol.png',
                    l('attention'), l('notification.alreadyAllowed'));
                notification.show();
            }
        } else {
            if (Notification.permission !== "granted") {
                Notification.requestPermission()
            } else {
                new Notification(
                    l('attention'),
                    {
                        icon: 'https://cdn1.iconfinder.com/data/icons/perfect-flat-icons-2/512/Info_information_user_about_card_button_symbol.png',
                        body: l('notification.alreadyAllowed')
                    }
                );
            }
        }
    },
    onUserClick: function (item) {
        if(_('isAdmin')){
            return
        }
        var win = Ext.widget('userWindow');
        Pms.Ajax.request({
            url: 'rest/profile',
            method: 'GET',
            async: false,
            success: function (res) {
                var userForm = win.down('userForm');
                userForm.getForm().setValues(res.content);
                userForm.down('combobox[name=role]').setValue(res.content.role.id);
                userForm.down('lookupCombobox[itemId=editWinLanguage]').setValue(res.content.language);
            }
        });
        win.show();
    },

    onHelp: function () {
        var win = Ext.widget('helpWindow');
        win.show();
    },

    onSettings: function () {
        var win = Ext.widget('settingsWindow');
        win.show();
    },

    onLogout: function () {
        Ext.Msg.confirm(l('logout.info'), l('logout.message'), function (btn) {
            if (btn === "yes") {
                document.location.href = 'logout';
            }
        });
    }
});