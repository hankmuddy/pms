Ext.define('Pms.desktop.Taskbar', {
    extend: 'Ext.toolbar.Toolbar',

    requires: [
        'Pms.desktop.StartMenu',
        'Pms.desktop.StartButton',
        'Pms.desktop.TrayClock'
    ],

    dock: 'bottom',
    id: 'system-taskbar',

    quickStart: [],

    initComponent: function () {
        var me = this;
        me.items = this.buildItems();
//        console.log(me.permissions);
        me.callParent();
    },

    buildItems: function () {
        var me = this,
            splitter = {
                xtype: "splitter",
                html: "&#160;",
                height: 30,
                width: 2, // TODO - there should be a CSS way here
                cls: "x-toolbar-separator x-toolbar-separator-horizontal poh-toolbar-splitter"
            };


        me.startMenu = Ext.create('Pms.desktop.StartMenu', {
            position: me.dock
        });

        me.startButton = Ext.create('Pms.desktop.StartButton', {
            menu: me.startMenu
        });

        me.quickStart = Ext.create("Ext.toolbar.Toolbar", this.getQuickStart());
        me.windowBar = Ext.create("Pms.desktop.TaskbarContainer");

        me.trayClock = Ext.create('Pms.desktop.TrayClock');

        return [
            me.startButton,
//            splitter,
            me.windowBar,
            {
                src: "themes/default/images/icons/world.png",
                xtype: 'image'
            },
            _('hotel').name + ' (' + _('hotelId') + ')',
//            splitter,
            '&nbsp;',
            '<div class="pms-split-circle"></div>',
            '&nbsp;',
            me.trayClock
        ];
    },

    getQuickStart: function () {
        var me = this, ret = {
            minWidth: 20,
//            width: 60,
            enableOverflow: true,
            cls: "pms-toolbar-container",
            items: [
//                {
//                    overflowText: "Show desktop",
//                    tooltip: {
//                        text: "Show desktop",
//                        align: 'bl-tl'
//                    },
//                    iconCls: "pms-desktop-icon",
//                    handler: function () {
//                        Pms.App.showNotification({
//                            message: "Testing this notification! this is just a dommy text!"
//                        });
//                    }
//                    // handler: Pms.App.runApplication,
//                    // }
//                },
//                {
//                    overflowText: "Settings",
//                    tooltip: { text: "Settings", align: 'bl-tl' },
//                    iconCls: "pms-settings-icon",
//                    handler: function () {
//                        Pms.App.showNotification({
//                            message: "Testing this notification! this is just a dommy text!"
//                        });
//                    }
//                }
            ]
        };

        Ext.each(this.quickStart, function (item) {
            Ext.applyIf(item, {
                tooltip: {
                    text: item.text,
                    align: 'bl-tl'
                },
                overflowText: item.text,
                iconCls: "pms-default-quickstart-icon",
                scope: Pms.App,
                handler: Pms.App.runApplication
            });
        });

        return ret;
    },

    addTaskButton: function (win) {
        var config = {
            cls: "pms-toolbar-button",
            iconCls: win.iconCls,
            enableToggle: true,
            toggleGroup: 'all',
//            width: 120,
            text: Ext.util.Format.ellipsis(win.title, 20),
            listeners: {
                click: this.onWindowBtnClick,
                scope: this
            },
            win: win
        };

        var cmp = this.windowBar.add(config);
        cmp.toggle(true);
        return cmp;
    },

    removeTaskButton: function (btn) {
        var found, me = this;
        me.windowBar.items.each(function (item) {
            if (item === btn) {
                found = item;
            }
            return !found;
        });
        if (found) {
            me.windowBar.remove(found);
        }
        return found;
    },

    onWindowBtnClick: function (btn) {
        var win = btn.win;

        if (win.minimized || win.hidden) {
            win.show();
        } else if (win.active) {
            win.minimize();
        } else {
            win.toFront();
        }
    },

    setActiveButton: function (btn) {
        if (btn) {
            btn.toggle(true);
        } else {
            this.windowBar.items.each(function (item) {
                if (item.isButton) {
                    item.toggle(false);
                }
            });
        }
    }
});