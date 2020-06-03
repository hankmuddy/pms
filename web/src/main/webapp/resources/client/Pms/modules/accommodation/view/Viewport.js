Ext.define("Pms.modules.accommodation.view.Viewport", {
    extend: "Pms.abstract.Viewport",
    alias: 'widget.accommodationViewport',

    layout: 'fit',
    autoScroll: false,

    buildItems: function () {
        return [
            {
                xtype: 'accommodationGrid',
                withToolbar: true
            }
        ];
    },

    buildTopButtons: function () {
    }
});
