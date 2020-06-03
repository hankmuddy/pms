/*

 This file is part of Ext JS 4

 Copyright (c) 2011 Sencha Inc

 Contact:  http://www.sencha.com/contact

 GNU General Public License Usage
 This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

 If you are unsure which license is appropriate for your use, please contact the sales department at http://www.sencha.com/contact.

 */
/**
 * @class Ext.chooser.InfoPanel
 * @extends Ext.panel.Panel
 * @author Ed Spencer
 *
 * This panel subclass just displays information about an image. We have a simple template set via the tpl property,
 * and a single function (loadRecord) which updates the contents with information about another image.
 */
Ext.define('Ext.ux.ViewChooser.InfoPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.infopanel',
    id: 'img-detail-panel',

    width: 170,
    minWidth: 170,

    tpl: [
        '<div class="details">',
        '<tpl for=".">',
        (!Ext.isIE6 ? '<img src="/files/{property_file.name}" style="max-width:150px;max-height:150px;" />' :
            '<div style="width:74px;height:74px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=\'/files/{property_file.name}\')"></div>'),
        '<div class="details-info">',
        '<b>Example Name:</b><br />',
        '<span>{property_file.title}</span>',
        // '<b>Example URL:</b>',
        //'<span><a href="http://dev.sencha.com/deploy/touch/examples/{url}" target="_blank">{url}.html</a></span>',
        '<b>Type:</b><br />',
        '<span>{property_file.type}</span>',
        '</div>',
        '</tpl>',
        '</div>'
    ],

    /**
     * Loads a given image record into the panel. Animates the newly-updated panel in from the left over 250ms.
     */
    loadRecord: function (image) {
        this.body.hide();
        this.tpl.overwrite(this.body, image.data);
        this.body.slideIn('l', {
            duration: 250
        });
    }
});
