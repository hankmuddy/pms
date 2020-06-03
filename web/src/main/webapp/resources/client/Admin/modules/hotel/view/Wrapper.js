(function () {
    var _me = 'hotel';
    Ext.define('Admin.modules.' + _me + '.view.Wrapper', {
        extend: 'Admin.generic.panel.Wrapper',
        dockedItems: [
            {
                xtype: 'breadcrumbs',
                dock: 'top',
                config: {
                    title: l('common.' + _me),
                    href: _me + '/list',
                    children: [
                        {
                            href: _me + '/edit',
                            title: ''
                        },
                        {
                            href: _me + '/add',
                            title: l('common.create')
                        },
                        {
                            href: _me + '/view',
                            title: ''
                        }
                    ]
                }
            }
        ]
    })
})();
