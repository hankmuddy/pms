Ext.define("Pms.modules.catalog.store.Catalog", {
    extend: "Pms.abstract.Store",
    model: "Pms.modules.catalog.model.Catalog",
    alias: 'widget.catalogStore',
    url: 'rest/simpleService',
    filters: [
        function (item) {
            var checkBox = Ext.ComponentQuery.query('checkbox[name=showDeleted]')[0],
                showDeleted = checkBox ? checkBox.value : false,
                showItem = showDeleted ? true : !item.data.deprecated;

            return (item.data.title != 'service.earlyCheckIn' && item.data.title != 'service.lateCheckOut' && showItem);
        }
    ]
});