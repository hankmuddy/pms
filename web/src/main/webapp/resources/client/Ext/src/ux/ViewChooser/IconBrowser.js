/*

This file is part of Ext JS 4

Copyright (c) 2011 Sencha Inc

Contact:  http://www.sencha.com/contact

GNU General Public License Usage
This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

If you are unsure which license is appropriate for your use, please contact the sales department at http://www.sencha.com/contact.

*/
/**
 * @class Ext.chooser.IconBrowser
 * @extends Ext.view.View
 * @author Ed Spencer
 * 
 * This is a really basic subclass of Ext.view.View. All we're really doing here is providing the template that dataview
 * should use (the tpl property below), and a Store to get the data from. In this case we're loading data from a JSON
 * file over AJAX.
 */
Ext.define('Ext.ux.ViewChooser.IconBrowser', {
    extend: 'Ext.view.View',
    alias: 'widget.iconbrowser',
    
    uses: 'Ext.data.Store',
    
    singleSelect: true,
    overItemCls: 'x-view-over',
    itemSelector: 'div.thumb-wrap',
    tpl: [
        // '<div class="details">',
            // '<tpl for=".">',
            //     '<div class="thumb-wrap">',
            //         '<div class="thumb">',
            //         (!Ext.isIE6 ? '<img src="/files/{property_file.name}" />' : '<div style="width:74px;height:74px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=\'/files/{property_file.name}\')"></div>'),
            //         '</div>',
            //         '<span>{property_file.title}</span>',
            //     '</div>',
            // '</tpl>'
        // '</div>'
        '<tpl for=".">',
            '<div class="thumb-wrap" style="float:left;margin:5px;width:170px;height:180px;">',
                '<div class="x-editable"><a href="/files/{property_file.name}" alt="{property_title}" target="_blank">Image link</a></div>',
                '<div class="thumb"><img src="/files/{property_file.name}" title="{property_description}" width="150" style="border:5px solid #ddd;margin: 5;"></div>',
                '<span class="x-editable">{property_file.title}&nbsp;</span>',
            '</div>',
        '</tpl>',
        '<div class="x-clear"></div>'
    ],
    
    initComponent: function() {
        // this.store = Ext.create('Ext.data.Store', {
        //     autoLoad: true,
        //     fields: ['name', 'thumb', 'url', 'type'],
        //     proxy: {
        //         type: 'ajax',
        //         url : 'icons.json',
        //         reader: {
        //             type: 'json',
        //             root: ''
        //         }
        //     }
        // });
        this.store = Ext.create(this.store).load();
        this.callParent(arguments);
        this.store.sort();
    }
});
