Ext.define("Pms.desktop.Shortcuts", {
    extend: "Ext.view.View",

    cls: "pms-shortcuts-view",
    overItemCls: "pms-shortcut-over",
    trackOver: true,
    itemSelector: ".pms-shortcut-select",

    /**
     * @cfg {String} shortcutTpl
     * This XTemplate is used to render items in the DataView. If this is changed, the
     * {@link shortcutItemSelect} will probably also need to changed.
     */
    shortcutTpl: [
        '<tpl for=".">',
        '<tpl if="window.innerHeight &lt;= 730 && index == 6">',
        '<div class="pms-shortcut-tiny-first">',
        '<tpl else>',
        '<tpl if="window.innerHeight &lt;= 730 && index == 7">',
        '<div class="pms-shortcut-tiny">',
        '<tpl else>',
        '<tpl if="window.innerHeight &lt;= 810 && index == 7">',
        '<div class="pms-shortcut-tiny-report-only">',
        '<tpl else>',
        '<div class="pms-shortcut">',
        ' </tpl>',
        ' </tpl>',
        ' </tpl>',
        '<div class="pms-shortcut-select">',
        '<div class="pms-shortcut-icon {iconCls}">',
        '<img src="', Ext.BLANK_IMAGE_URL, '" title="{text}">',
        '</div>',
        '<span class="pms-shortcut-text">{text}</span>',
        '</div>',
        '</div>',
        '</tpl>',
        '<div class="x-clear"></div>'
    ],

    initComponent: function () {
        var me = this;

        me.store = Ext.create("Ext.data.ArrayStore", {
            fields: ["text", "class", "iconCls", "index"]
        });

        me.getShorcuts(me.applications);
        me.tpl = new Ext.XTemplate(me.shortcutTpl);

        me.callParent();
    },

    getShorcuts: function (applications) {
        if (applications) {
            Ext.each(applications, function (app) {
                this.store.add(app);
            }, this);
        }
    },

    afterRender: function () {
        var me = this;
        me.callParent(arguments);
    }
});