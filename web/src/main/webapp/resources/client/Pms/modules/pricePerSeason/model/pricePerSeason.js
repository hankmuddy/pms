Ext.define('Pms.modules.pricePerSeason.model.pricePerSeason', {
    extend: "Ext.data.Model",
    alias: 'widget.pricePerSeasonModel',
    fields: [
        'season',
        'catalog_rate',
        'price_mon',
        'price_tue',
        'price_wed',
        'price_thu',
        'price_fri',
        'price_sat',
        'price_sun'
    ]
});