Ext.define('Pms.desktop.Desktop', {
    extend: "Ext.panel.Panel",
    alias: "widget.desktop",

    requires: [
        'Pms.desktop.Taskbar',
        'Pms.desktop.Shortcuts',
        'Pms.desktop.Wallpaper',
        'Pms.desktop.FitAllLayout',
        'Pms.desktop.Hints',
        'Pms.desktop.LoginWindow'
    ],

    id: "pms-desktop",
    border: false,
    layout: 'fitall',
    extranet: null,

    modulesArray: {
        groupRoomUse: {
            text: l('desktop.checkerboard'),
            iconCls: 'booking-system-shortcut-icon',
            klass: "Pms.modules.groupRoomUse.controller.groupRoomUse",
            index: 0,
            desktop: true,
            extravailable: false,
            pmsavailable: true
        },
        console: {
            text: l('desktop.console'),
            iconCls: 'console1-shortcut-icon',
            klass: "Pms.modules.console.controller.Console",
            index: 1,
            desktop: true,
            extravailable: false,
            pmsavailable: true
        },
        booking: {
            text: l('desktop.bookingSearch'),
            iconCls: 'search-shortcut-icon',
            klass: "Pms.modules.booking.controller.Booking",
            index: 2,
            desktop: true,
            extravailable: true,
            pmsavailable: true
        },
        person: {
            text: l('desktop.clients'),
            iconCls: 'client-shortcut-icon',
            klass: "Pms.modules.person.controller.Person",
            index: 3,
            desktop: true,
            extravailable: true,
            pmsavailable: true
        },
        company: {
            text: l('desktop.company'),
            iconCls: 'agency-shortcut-icon',
            klass: "Pms.modules.company.controller.Company",
            index: 4,
            desktop: true,
            extravailable: true,
            pmsavailable: true
        },
        bill: {
            text: l('desktop.bill'),
            iconCls: 'bill-shortcut-icon',
            klass: "Pms.modules.bill.controller.Bill",
            index: 5,
            desktop: true,
            extravailable: false,
            pmsavailable: true
        },
        payment: {
            text: l('desktop.payment'),
            iconCls: 'payments-shortcut-icon',
            klass: "Pms.modules.payment.controller.Payment",
            index: 6,
            desktop: true,
            extravailable: false,
            pmsavailable: true
        },
        report: {
            text: l('desktop.report'),
            iconCls: 'report-shortcut-icon',
            klass: "Pms.modules.report.controller.Report",
            desktop: true,
            index: 7,
            extravailable: false,
            pmsavailable: true
        }
//        bookingButton: {
//            text: l('bookingButton'),
//            iconCls: 'payments-shortcut-icon',
//            klass: "Pms.modules.bookingButton.controller.BookingButtonController",
//            index: 6,
//            desktop: true
//        }
    },

    initComponent: function () {
        var me = this;

        me.taskbar = Ext.create('Pms.desktop.Taskbar', {
            permissions: me.permissions
        });

        me.dockedItems = [me.taskbar];

        function groupFacilities(content) {
            var allItems = content,
                groupedItems = {};

            for (var i in allItems) {
                var nameArr = allItems[i].name.split('.'),
                    groupName = nameArr[1];
                if (!groupedItems[groupName]) groupedItems[groupName] = [];
                groupedItems[groupName].push(allItems[i]);
            }

            return groupedItems;
        }

        if (Pms.showHints) Ext.create('Pms.desktop.Hints');

        Pms.Ajax.request({
            url: 'rest/roomTypeFacility',
            method: 'GET',
            async: false,
            success: function (response) {
                Pms.facilities = groupFacilities(response.content);
            },
            failure: function () {
                Pms.App.app.showNotification({
                    message: l('facilitiesError')
                });
            }
        });

        Pms.Ajax.request({
            url: 'rest/settings/allFacilities',
            method: 'GET',
            async: false,
            success: function (response) {
                Pms.allFacilities = groupFacilities(response.content);
            },
            failure: function () {
                Pms.App.app.showNotification({ // Unavailiable in Desktop
                    message: l('facilitiesError')
                });
            }
        });

        me.windowMgr = Ext.create("Pms.desktop.WindowManager", {
            taskbar: me.taskbar
        });


        var modulesArray = [
            'Pms.modules.groupRoomUse.controller.groupRoomUse',
            'Pms.modules.console.controller.Console',
            'Pms.modules.booking.controller.Booking',
            'Pms.modules.person.controller.Person',
            'Pms.modules.company.controller.Company',
            'Pms.modules.bill.controller.Bill',
            'Pms.modules.payment.controller.Payment',
            'Pms.modules.report.controller.Report',
//            'Pms.modules.bookingButton.controller.BookingButtonController'
        ];

        for (var i = 0; i < me.permissions.length; i++) {
            switch (me.permissions[i]) {
                case "USER":
                    modulesArray.push('Pms.modules.user.controller.User');
                    break;
                case "SETTINGS":
                    modulesArray.push('Pms.modules.settings.controller.Settings');
                    break;
                default:
                    break;
            }
        }

        var inArray = function (array, obj) {
            var i = array.length;
            while (i--) {
                if (array[i] === obj) {
                    return true;
                }
            }
            return false;
        };

        var shortcut = [];
        for (var key in me.modulesArray) {
            if (me.modulesArray[key].desktop && !(me.extranet === true && me.modulesArray[key].extravailable === false || me.extranet === false && me.modulesArray[key].pmsavailable === false)) {
                if (inArray(modulesArray, me.modulesArray[key].klass)) {
                    shortcut.push(me.modulesArray[key]);
                } else {

                }
            }
        }

        me.shortcuts = Ext.create('Pms.desktop.Shortcuts', {
            applications: shortcut
        });

        me.contextMenuItems = [
            {
                text: l('context.settingsBtn'),
                handler: this.onSettings,
                scope: this
            },
            {
                text: l('context.refreshBtn'),
                handler: function () {
                    location.reload();
                },
                scope: this
            }
        ];

        me.wallpaper = Ext.create('Pms.desktop.Wallpaper', {
            wallpaper: Pms.logoSrc,
            stretch: true
        });

        me.windowMenu = new Ext.menu.Menu(me.createWindowMenu());

        me.items = [
            me.wallpaper,
            me.shortcuts
        ];


        me.callParent();

        me.shortcuts.on("itemClick", me.onShortcutItemClick, me);
        Pms.Ajax.on("sessionExpired", me.showLoginWindow, Pms.App);
    },

    showLoginWindow: function () {
        Ext.require("Pms.modules.login.LoginWindow", function () {
            var win = Ext.create("Pms.modules.login.LoginWindow", {
                modal: true,
                forward: false
            });
            win.show();
        });
//        var win = Ext.widget('loginWindow');
//        win.show();
    },

    createWindowMenu: function () {
        var me = this;

        return {
            defaultAlign: 'br-tr',
            items: [
                {
                    text: 'Restore',
                    handler: me.windowMgr.onWindowMenuRestore,
                    scope: me.windowMgr
                },
                {
                    text: 'Minimize',
                    handler: me.windowMgr.onWindowMenuMinimize,
                    scope: me.windowMgr
                },
                {
                    text: 'Maximize',
                    handler: me.windowMgr.onWindowMenuMaximize,
                    scope: me.windowMgr
                },
                '-',
                {
                    text: 'Close',
                    handler: me.windowMgr.onWindowMenuClose,
                    scope: me
                }
            ],
            listeners: {
                beforeShow: me.windowMgr.onWindowMenuBeforeShow,
                hide: me.windowMgr.onWindowMenuHide,
                scope: me.windowMgr
            }
        };
    },

    onShortcutItemClick: function (dataView, record, el, index, event) {
        Pms.App.runApplication({
            initialConfig: record.raw
        });
    }
});
