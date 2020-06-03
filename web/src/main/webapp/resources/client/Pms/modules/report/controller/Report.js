Ext.define('Pms.modules.report.controller.Report', {
    extend: 'Pms.abstract.Controller',

    views: [
        'Pms.modules.report.view.Viewport'
    ],

    init: function (contr, subController) {
        if (!subController) {
            this.buildItems();
        }
    },

    buildItems: function () {
        if (!Ext.isEmpty(this.win)) {
            var view = Ext.create('Pms.modules.report.view.Viewport');
            this.win.setWidth(500);
            this.win.setHeight(500);
            this.win.add(view);
        }
    }
});
