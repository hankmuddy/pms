(function () {
    var _me = 'profile';
    Ext.define('Admin.modules.' + _me + '.view.Wrapper', {
        extend: 'Admin.generic.panel.Wrapper',
        layout: 'fit',
        dockedItems: [
            {
                xtype: 'breadcrumbs',
                dock: 'top',
                config: {
                    title: l('common.profile'),
                    href: _me + '/list',
                    children: [
                        {
                            href: _me + '/edit',
                            title: ''
                        },
                        {
                            href: _me + '/add',
                            title: l('profile.add')
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
