Ext.define('Admin.generic.app.controller.Main', {
    extend: 'Ext.app.Controller',
    card: 'home',
    defaultAction: 'list',
    getActionController: function (action) {
        if (!action) return null;
        var actionControllerClassName = this.route(action);

        if (!actionControllerClassName) return null;
        try {
            return actionController = this.getController(actionControllerClassName);
        } catch (e) {
            // this.notFound({async:true});
            return null
        }
    },
    // routeMap:{
    //     list: 'Admin.name.controller.List',
    // },
    // route: function(key){
    //     if(this.routeMap && this.routeMap[key]) return this.routeMap[key];
    //     var routeMap = {
    //         list: 'List',
    //         add: 'Create',
    //         edit: 'Update',
    //         view: 'View',
    //     };
    //     if(!routeMap[key]) return null;
    //     return this.getNameSpacePrefix() + 'controller.'+ routeMap[key]
    // },
    route: function (key) {
        if (this['action_' + key]) {
            return this['action_' + key]
        }
    },
    getNameSpacePrefix: function () {
        return this.$className.split('controller')[0];
        // return 'Admin.'+this.name+'.';
    },
    dispatchAction: function (_action, _params) {
        while (true) {
            var action = _action,
                actionController = this.getActionController(_action);
            if (actionController) break;

            var actionPart = Admin.generic.Utils.getHashPart(2);
            if ((actionPart === null) && this.defaultAction) {
                action = this.defaulAction;
                actionController = this.getActionController(this.defaultAction);
                if (actionController)break;
            }

            if (Admin.generic.Utils.isNotEmptyString(actionPart)) {
                action = actionPart;
                actionController = this.getActionController(actionPart);
                if (actionController)break;
            }
            console.error(this, 'Unknown Action:', action);
            Admin.getApplication().notFound();
            break;
        }
        this.loadPage(actionController)
    },
    findContentPanel: function (actionController) {
        var panel = Ext.ComponentQuery.query('#' + this.name)[0]
        if (!panel) throw Error("Can not find in NavTabPanel: #" + this.name);
        panel.removeAll(true);
        return panel;
    },
    loadPage: function (actionController) {
        var panel = this.findContentPanel(actionController);
        Ext.suspendLayouts();
        this.view = Ext.create(this.viewClass);
        this.breadcrumbs = this.view.down('breadcrumbs')
        var childView = Ext.create('Ext.container.Container', {
            layout: 'fit'
        });
        actionController.run({
            view: childView,
            name: this.name,
            breadcrumbs: this.breadcrumbs,
            links: {
                'list': this.name + '/list',
                'add': this.name + '/add',
                'edit': this.name + '/edit',
                'view': this.name + '/view'
            }
        });
        this.view.add(childView);
        panel.add(this.view);
        Ext.resumeLayouts();
        var tabpanel = panel.up('tabpanel');
        while (tabpanel) {
            delete panel.initialConfig['href'];
            tabpanel.suspendEvent('tabchange');
            tabpanel.setActiveTab(panel.getItemId());
            // tabpanel.getLayout().setActiveItem(panel);
            tabpanel.resumeEvent('tabchange');
            panel = tabpanel.up('panel');
            tabpanel = panel.up('tabpanel');
        }
        var contentWrapper = Ext.ComponentQuery.query('#contentwrapper')[0];
        contentWrapper.getLayout().setActiveItem(this.card)
        var viewport = panel.up('viewport');
        viewport.setVisible(true);
        this.view.show();
    },
});
