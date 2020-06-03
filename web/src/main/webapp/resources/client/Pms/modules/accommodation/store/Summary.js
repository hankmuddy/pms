Ext.define('Pms.modules.accommodation.store.Summary', {
    extend: 'Pms.abstract.Store',
    alias: 'widget.summaryStore',
    fields: [
        'rooms_count',
        'rooms_available_now',
        'rooms_unavailable_now',
        'rooms_in_use_now',
        'rooms_free_now',
        'rooms_living_now',
        'guests_living_now',
        'bookings_from_now'
    ],

    proxyApi: {
        read: '/api/accommodation/summary/json'
    }
});