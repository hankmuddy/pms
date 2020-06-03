Ext.define('Pms.desktop.Application', {
    extend: 'Ext.app.Application',
    mixins: {
        observable: "Ext.util.Observable"
    },
    requires: [
        'Pms.desktop.Desktop',
        'Pms.desktop.Notification',
        'Pms.desktop.TaskbarContainer',
        'Pms.desktop.WindowManager',
        'Pms.desktop.LoginWindow',
        'Pms.Lookup'
    ],

    useQuickTips: true,

    allowFunctions: true,

    isExtranet: function() {
        return _('extranet');
    },

    constructor: function () {
        var me = this;

        me.addEvents({
            'ready': true
        });

        me.callParent(arguments);

        this.buildDesktop();
    },

    getPermissions: function () {
//        USER_UPDATE
//        USER_DELETE
//        SETTINGS_SAVE
//        USER_VIEW
//        USER_CREATE
//        USER_LIST

        var permissions = _("permissions"),
            modulePermissions = [],
            appPermissions = {};

        var inArray = function (array, comparer) {
            for (var i = 0; i < array.length; i++) {
                if (array[i] == comparer) return true;
            }
            return false;
        };

        var i = 0;

        for (i = 0; i < permissions.length; i++) {
            var permission = permissions[i].split('_');
            if (!inArray(modulePermissions, permission[0])) {
                modulePermissions.push(permission[0]);
            }
        }

//        for (i = 0; i < modulePermissions.length; i++) {
//            if(modulePermissions[i] == "USER") {
//                modulePermissions[i] = {
//                    USER: "Pms.Module.Settings"
//                };
//            }
//
//            if(modulePermissions[i] == "SETTINGS") {
//                modulePermissions[i] = {
//                    SETTINGS: "Pms.Module.Settings"
//                };
//            }
//        }

//        console.log(modulePermissions);

        return modulePermissions;
    },

    buildDesktop: function () {
        var me = this;

        if (me.useQuickTips) {
            Ext.QuickTips.init();
        }

        me.desktop = new Pms.desktop.Desktop({permissions: me.getPermissions(), extranet: me.isExtranet()});

        me.viewport = new Ext.container.Viewport({
            layout: 'fit',
            items: me.desktop
        });

        Ext.EventManager.on(window, "beforeunload", me.onUnload, me);

        me.fireEvent('ready', me);
    },

    runApplication: function (item) {
        var app = item.initialConfig,
            me = this;

        me.showApplication(app);
    },

    showApplication: function (info, options) {
        var me = this,
            klass = info.klass,
            win,
            permissions = _("permissions"),
            cfg;

        if(me.isExtranet() === true && info.extravailable === false) {
            me.showNotification({
                message: l('notAvailableOnExtranet')
            });
            return;
        }
        else if(me.isExtranet() === false && info.pmsavailable === false) {
            me.showNotification({
                message: l('notAvailableInPms')
            });
            return;
        }
        else win = this.desktop.windowMgr.createWindow(info);

        if (win) {
            var arr = klass.split("."),
                appname = arr[0];
//            Ext.Loader.setPath(Pms.desktop.modules + appname);
            Ext.Loader.require(klass, function () {
                var controller = me.controllers.get(klass),
                    id = klass;

                me.desktop.add(win);
                me.desktop.windowMgr.loader.hide();

                if (!controller) {
                    // uncomment following for defaults:
                    controller = Ext.create(me.getModuleClassName(klass, 'controller'), {
                        permissions: permissions,
                        application: me,
                        id: klass
                    });

                    if(me.isExtranet() === true && controller.extravailable === false || me.isExtranet() === false && controller.pmsavailable === false) {
                        delete controller;
                        return;
                    }
                    me.controllers.add(controller);
                }
                controller.win = win;
                controller.init(me);
                controller.onLaunch(me);
                //                win.on("destroy", function () {
                //                    me.removeController(controller);
                //                });
                controller.inUse++;
                controller.onDesktop = true;

                if (controller.subControllers) {
                    for (var i in controller.subControllers) {
                        me.addSubController(controller.subControllers[i]);
                    }
                }
                win.show();
            });
        }
        else {
            me.showNotification({
                message: l('app.errorNotification')
            });
        }
    },

    addSubController: function (subController) {
        var me = this;

        Ext.Loader.require(subController, function () {
            var controller = me.controllers.get(subController);

            if (!controller) {
                controller = Ext.create(me.getModuleClassName(subController, 'controller'), {
                    application: me,
                    id: subController
                });

                if(me.isExtranet() === true && controller.extravailable === false || me.isExtranet() === false && controller.pmsavailable === false) {
                    delete controller;
                    return;
                }
                me.controllers.add(controller);
                controller.init(me, true);
                controller.onLaunch(me);

                if(controller.subControllers.length) {
                    for(var i in controller.subControllers) {
                        me.addSubController(controller.subControllers[i]);
                    }
                }
            }
            controller.inUse++;
        });
    },

    removeController: function (controller) {
        var me = this,
            subControllers = controller.subControllers;

        if (subControllers.length) {
            for (var i in subControllers) {
                me.destroyController(me.controllers.get(subControllers[i]), true);
            }
        }

        me.destroyController(controller, false);
    },

    // @vars: controller, sub = true/false, if controller is a subController or a module controller
    destroyController: function (controller, sub) {
        var me = this;

        if (!sub) {
            controller.onDesktop = false;
        }
        controller.inUse--;

        if (controller.inUse == 0) {
            me.controllers.remove(controller);
            for (var i = 0, len = controller.selectors.length; i < len; i++) {
                var obj = controller.selectors[i];
                for (var s in obj) {
                    for (var ev in obj[s]) {
                        delete me.eventbus.bus[ev][s];
                    }
                }
            }
            delete controller;
        }
    },

    showNotification: function (data) {
        Ext.create("Pms.desktop.Notification", {
            message: data.message,
            icon: data.icon
        });
    },

    showLoginWindow: function () {
//        console.log('ok');
//        var win = Ext.widget('loginWindow');
//        win.show();
        return true;
    },

    onError: function (data) {
        if (data.error.code = 2301) {
            document.location.href = '/login';
        }
    },

    onUnload: function (e) {
        if (this.fireEvent('beforeunload', this) === false) {
            e.stopEvent();
        }
    },

    inArray: function (el, arr) {
        for (var i in arr) {
            if (arr[i] === el) {
                return true;
            }
        }
        return false;
    }
});