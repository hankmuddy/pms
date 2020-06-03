Ext.define('Admin.generic.ux.Breadcrumbs', {
    extend: 'Ext.toolbar.Toolbar',
    alias: 'widget.breadcrumbs',
    initComponent: function () {
        var config = this.getInitialConfig();
        this.pageTree = {};
        this.buildPageTree(config);
        this.callParent()
    },
    buildPageTree: function (config) {
        var traverse = function (config, store, parent) {
            var parent = store[config.href] = {
                title: config.title,
                href: config.href,
                parent: parent
            };
            if (!config.children) return;
            for (var i = 0; i < config.children.length; i++) {
                traverse(config.children[i], store, parent);
            }
        };

        traverse(config, this.pageTree, null);
    },
    getBranch: function (href, conf) {
        var branch = this.pageTree[href];
        if (!branch) throw Error('Breadcrumbs building failed: unknown branch - ' + href);
        if (!conf) return branch;
        var node = branch = Ext.clone(branch);
        do {
            for (var key in conf) {
                if (node.href == key) {
                    node.href = conf[key].href;
                    node.title = conf[key].title;
                    node.clickable = conf[key].clickable;
                    node.fn = conf[key].fn;
                    break
                }
            }
            node = node.parent
        } while (node);
        return branch
    },
    getCurrentUrl: function () {
        return this.currentUrl;
    },
    setBreadcrumbs: function (href, conf) {
        this.show();
        this.currentUrl = location.hash;
        this.removeAll();
        var nodePage = this.getBranch(href, conf);

        var addBtn = function (nodePage, cmp, opt) {
            var opt = opt || {};
            var btnCnf = {
                xtype: "tbtext",
                text: nodePage.title
            };
            if (nodePage.clickable || !opt.nolink) {
                btnCnf.autoEl = {
                    tag: 'a',
                    href: '#' + nodePage.href,
                    onclick: nodePage.fn ? nodePage.fn : ""
                }
            }
            cmp.insert(0, btnCnf);
        };

        var buildBreadcrumbs = function (nodePage, cmp) {
            if (!nodePage) return;
            cmp.insert(0, '>')
            addBtn(nodePage, cmp);
            buildBreadcrumbs(nodePage.parent, cmp)
        };

        addBtn(nodePage, this, {nolink: true});
        buildBreadcrumbs(nodePage.parent, this)
    }

});
