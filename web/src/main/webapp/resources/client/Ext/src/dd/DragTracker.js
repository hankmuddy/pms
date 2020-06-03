/*
 This file is part of Ext JS 4.2

 Copyright (c) 2011-2013 Sencha Inc

 Contact:  http://www.sencha.com/contact

 Commercial Usage
 Licensees holding valid commercial licenses may use this file in accordance with the Commercial
 Software License Agreement provided with the Software or, alternatively, in accordance with the
 terms contained in a written agreement between you and Sencha.

 If you are unsure which license is appropriate for your use, please contact the sales department
 at http://www.sencha.com/contact.

 Build date: 2013-05-16 14:36:50 (f9be68accb407158ba2b1be2c226a6ce1f649314)
 */
/**
 * A DragTracker listens for drag events on an Element and fires events at the start and end of the drag,
 * as well as during the drag. This is useful for components such as {@link Ext.slider.Multi}, where there is
 * an element that can be dragged around to change the Slider's value.
 *
 * DragTracker provides a series of template methods that should be overridden to provide functionality
 * in response to detected drag operations. These are onBeforeStart, onStart, onDrag and onEnd.
 * See {@link Ext.slider.Multi}'s initEvents function for an example implementation.
 */
Ext.define('Ext.dd.DragTracker', {

    uses: ['Ext.util.Region'],

    mixins: {
        observable: 'Ext.util.Observable'
    },

    /**
     * @property {Boolean} active
     * Indicates whether the user is currently dragging this tracker.
     * @readonly
     */
    active: false,

    /**
     * @property {HTMLElement} dragTarget
     * The element being dragged.
     *
     * Only valid during drag operations.
     *
     * If the {@link #delegate} option is used, this will be the delegate element which was mousedowned.
     * @readonly
     */

    /**
     * @cfg {Boolean} trackOver
     * Set to true to fire mouseover and mouseout events when the mouse enters or leaves the target element.
     *
     * This is implicitly set when an {@link #overCls} is specified.
     *
     * If the {@link #delegate} option is used, these events fire only when a delegate element is entered of left.
     */
    trackOver: false,

    /**
     * @cfg {String} overCls
     * A CSS class to add to the DragTracker's target element when the element (or, if the {@link #delegate}
     * option is used, when a delegate element) is mouseovered.
     *
     * If the {@link #delegate} option is used, these events fire only when a delegate element is entered of left.
     */

    /**
     * @cfg {Ext.util.Region/Ext.Element} constrainTo
     * A {@link Ext.util.Region Region} (Or an element from which a Region measurement will be read)
     * which is used to constrain the result of the {@link #getOffset} call.
     *
     * This may be set any time during the DragTracker's lifecycle to set a dynamic constraining region.
     */

    /**
     * @cfg {Number} tolerance
     * Number of pixels the drag target must be moved before dragging is
     * considered to have started.
     */
    tolerance: 5,

    /**
     * @cfg {Boolean/Number} autoStart
     * Specify `true` to defer trigger start by 1000 ms.
     * Specify a Number for the number of milliseconds to defer trigger start.
     */
    autoStart: false,

    /**
     * @cfg {String} delegate
     * A {@link Ext.DomQuery DomQuery} selector which identifies child elements within the DragTracker's encapsulating
     * Element which are the tracked elements. This limits tracking to only begin when the matching elements are mousedowned.
     *
     * This may also be a specific child element within the DragTracker's encapsulating element to use as the tracked element.
     */

    /**
     * @cfg {Boolean} [preventDefault=true]
     * Specify `false` to enable default actions on onMouseDown events.
     */

    /**
     * @cfg {Boolean} [stopEvent=false]
     * Specify `true` to stop the `mousedown` event from bubbling to outer listeners from the target element (or its delegates).
     */

    constructor: function (config) {
        var me = this;
        Ext.apply(me, config);
        me.addEvents(
            /**
             * @event mouseover
             * Fires when the mouse enters the DragTracker's target element (or if {@link #delegate} is
             * used, when the mouse enters a delegate element).
             *
             * **Only available when {@link #trackOver} is `true`**
             *
             * @param {Object} this
             * @param {Object} e event object
             * @param {HTMLElement} target The element mouseovered.
             */
            'mouseover',

            /**
             * @event mouseout
             * Fires when the mouse exits the DragTracker's target element (or if {@link #delegate} is
             * used, when the mouse exits a delegate element).
             *
             * **Only available when {@link #trackOver} is `true`**
             *
             * @param {Object} this
             * @param {Object} e event object
             */
            'mouseout',

            /**
             * @event mousedown
             * Fires when the mouse button is pressed down, but before a drag operation begins. The
             * drag operation begins after either the mouse has been moved by {@link #tolerance} pixels,
             * or after the {@link #autoStart} timer fires.
             *
             * Return `false` to veto the drag operation.
             *
             * @param {Object} this
             * @param {Object} e event object
             */
            'mousedown',

            /**
             * @event mouseup
             * @param {Object} this
             * @param {Object} e event object
             */
            'mouseup',

            /**
             * @event mousemove
             * Fired when the mouse is moved. Returning false cancels the drag operation.
             * @param {Object} this
             * @param {Object} e event object
             */
            'mousemove',

            /**
             * @event beforestart
             * @param {Object} this
             * @param {Object} e event object
             */
            'beforedragstart',

            /**
             * @event dragstart
             * @param {Object} this
             * @param {Object} e event object
             */
            'dragstart',

            /**
             * @event dragend
             * @param {Object} this
             * @param {Object} e event object
             */
            'dragend',

            /**
             * @event drag
             * @param {Object} this
             * @param {Object} e event object
             */
            'drag'
        );

        me.dragRegion = new Ext.util.Region(0, 0, 0, 0);

        if (me.el) {
            me.initEl(me.el);
        }

        // Dont pass the config so that it is not applied to 'this' again
        me.mixins.observable.constructor.call(me);
        if (me.disabled) {
            me.disable();
        }

    },

    /**
     * Initializes the DragTracker on a given element.
     * @param {Ext.Element/HTMLElement} el The element
     */
    initEl: function (el) {
        var me = this;

        me.el = Ext.get(el);

        // The delegate option may also be an element on which to listen
        me.handle = Ext.get(me.delegate);

        // If delegate specified an actual element to listen on, we do not use the delegate listener option
        me.delegate = me.handle ? undefined : me.delegate;

        if (!me.handle) {
            me.handle = me.el;
        }

        // Add a mousedown listener which reacts only on the elements targeted by the delegate config.
        // We process mousedown to begin tracking.
        me.handleListeners = {
            scope: me,
            delegate: me.delegate,
            mousedown: me.onMouseDown
        };

        // If configured to do so, track mouse entry and exit into the target (or delegate).
        // The mouseover and mouseout CANNOT be replaced with mouseenter and mouseleave
        // because delegate cannot work with those pseudoevents. Entry/exit checking is done in the handler.
        if (me.trackOver || me.overCls) {
            Ext.apply(me.handleListeners, {
                mouseover: me.onMouseOver,
                mouseout: me.onMouseOut
            });
        }
        me.mon(me.handle, me.handleListeners);
    },

    disable: function () {
        this.disabled = true;
    },

    enable: function () {
        this.disabled = false;
    },

    destroy: function () {
        var me = this;

        if (me.active) {
            // endDrag has a mandatory event parameter
            me.endDrag({});
        }
        me.clearListeners();
        me.mun(me.handle, me.handleListeners);
        me.el = me.handle = null;
    },

    // When the pointer enters a tracking element, fire a mouseover if the mouse entered from outside.
    // This is mouseenter functionality, but we cannot use mouseenter because we are using "delegate" to filter mouse targets
    onMouseOver: function (e, target) {
        var me = this;
        if (!me.disabled) {
            if (Ext.EventManager.contains(e) || me.delegate) {
                me.mouseIsOut = false;
                if (me.overCls) {
                    me.el.addCls(me.overCls);
                }
                me.fireEvent('mouseover', me, e, me.delegate ? e.getTarget(me.delegate, target) : me.handle);
            }
        }
    },

    // When the pointer exits a tracking element, fire a mouseout.
    // This is mouseleave functionality, but we cannot use mouseleave because we are using "delegate" to filter mouse targets
    onMouseOut: function (e) {
        var me = this;

        if (me.mouseIsDown) {
            me.mouseIsOut = true;
        } else {
            if (me.overCls) {
                me.el.removeCls(me.overCls);
            }
            me.fireEvent('mouseout', me, e);
        }
    },

    onMouseDown: function (e, target) {
        var me = this,
            el;

        // If this is disabled, or the mousedown has been processed by an upstream DragTracker, return
        if (me.disabled || e.dragTracked) {
            return;
        }

        // This information should be available in mousedown listener and onBeforeStart implementations
        me.dragTarget = me.delegate ? target : me.handle.dom;
        me.startXY = me.lastXY = e.getXY();
        me.startRegion = Ext.fly(me.dragTarget).getRegion();

        if (me.fireEvent('mousedown', me, e) === false ||
            me.fireEvent('beforedragstart', me, e) === false ||
            me.onBeforeStart(e) === false) {
            return;
        }

        // Track when the mouse is down so that mouseouts while the mouse is down are not processed.
        // The onMouseOut method will only ever be called after mouseup.
        me.mouseIsDown = true;

        // Flag for downstream DragTracker instances that the mouse is being tracked.
        e.dragTracked = true;

        // See Ext.dd.DragDropManager::handleMouseDown
        el = me.el.dom;
        if (Ext.isIE && el.setCapture) {
            el.setCapture();
        }

        if (me.preventDefault !== false) {
            e.preventDefault();
        }
        Ext.getDoc().on({
            scope: me,
            mouseup: me.onMouseUp,
            mousemove: me.onMouseMove,
            selectstart: me.stopSelect
        });
        if (me.autoStart) {
            me.timer = Ext.defer(me.triggerStart, me.autoStart === true ? 1000 : me.autoStart, me, [e]);
        }
    },

    onMouseMove: function (e, target) {
        var me = this,
            xy = e.getXY(),
            s = me.startXY;

        e.preventDefault();

        me.lastXY = xy;
        if (!me.active) {
            if (Math.max(Math.abs(s[0] - xy[0]), Math.abs(s[1] - xy[1])) > me.tolerance) {
                me.triggerStart(e);
            } else {
                return;
            }
        }

        // Returning false from a mousemove listener deactivates
        if (me.fireEvent('mousemove', me, e) === false) {
            me.onMouseUp(e);
        } else {
            me.onDrag(e);
            me.fireEvent('drag', me, e);
        }
    },

    onMouseUp: function (e) {
        var me = this;
        // Clear the flag which ensures onMouseOut fires only after the mouse button
        // is lifted if the mouseout happens *during* a drag.
        me.mouseIsDown = false;

        // If we mouseouted the el *during* the drag, the onMouseOut method will not have fired. Ensure that it gets processed.
        if (me.mouseIsOut) {
            me.mouseIsOut = false;
            me.onMouseOut(e);
        }
        e.preventDefault();

        // See Ext.dd.DragDropManager::handleMouseDown
        if (Ext.isIE && document.releaseCapture) {
            document.releaseCapture();
        }

        me.fireEvent('mouseup', me, e);
        me.endDrag(e);
    },

    /**
     * @private
     * Stop the drag operation, and remove active mouse listeners.
     */
    endDrag: function (e) {
        var me = this,
            wasActive = me.active;

        Ext.getDoc().un({
            mousemove: me.onMouseMove,
            mouseup: me.onMouseUp,
            selectstart: me.stopSelect,
            scope: me
        });
        me.clearStart();
        me.active = false;
        if (wasActive) {
            me.onEnd(e);
            me.fireEvent('dragend', me, e);
        }
        // Private property calculated when first required and only cached during a drag
        // Remove flag from event singleton.  Using "Ext.EventObject" here since "endDrag" is called directly in some cases without an "e" param
        me._constrainRegion = Ext.EventObject.dragTracked = null
    },

    triggerStart: function (e) {
        var me = this;
        me.clearStart();
        me.active = true;
        me.onStart(e);
        me.fireEvent('dragstart', me, e);
    },

    clearStart: function () {
        var timer = this.timer;
        if (timer) {
            clearTimeout(timer);
            this.timer = null;
        }
    },

    stopSelect: function (e) {
        e.stopEvent();
        return false;
    },

    /**
     * Template method which should be overridden by each DragTracker instance. Called when the user first clicks and
     * holds the mouse button down. Return false to disallow the drag
     * @param {Ext.EventObject} e The event object
     * @template
     */
    onBeforeStart: function (e) {

    },

    /**
     * Template method which should be overridden by each DragTracker instance. Called when a drag operation starts
     * (e.g. the user has moved the tracked element beyond the specified tolerance)
     * @param {Ext.EventObject} e The event object
     * @template
     */
    onStart: function (xy) {

    },

    /**
     * Template method which should be overridden by each DragTracker instance. Called whenever a drag has been detected.
     * @param {Ext.EventObject} e The event object
     * @template
     */
    onDrag: function (e) {

    },

    /**
     * Template method which should be overridden by each DragTracker instance. Called when a drag operation has been completed
     * (e.g. the user clicked and held the mouse down, dragged the element and then released the mouse button)
     * @param {Ext.EventObject} e The event object
     * @template
     */
    onEnd: function (e) {

    },

    /**
     * Returns the drag target. This is usually the DragTracker's encapsulating element.
     *
     * If the {@link #delegate} option is being used, this may be a child element which matches the
     * {@link #delegate} selector.
     *
     * @return {Ext.Element} The element currently being tracked.
     */
    getDragTarget: function () {
        return this.dragTarget;
    },

    /**
     * @private
     * @returns {Ext.Element} The DragTracker's encapsulating element.
     */
    getDragCt: function () {
        return this.el;
    },

    /**
     * @private
     * Return the Region into which the drag operation is constrained.
     * Either the XY pointer itself can be constrained, or the dragTarget element
     * The private property _constrainRegion is cached until onMouseUp
     */
    getConstrainRegion: function () {
        var me = this;

        if (me.constrainTo) {
            if (me.constrainTo instanceof Ext.util.Region) {
                return me.constrainTo;
            }
            if (!me._constrainRegion) {
                me._constrainRegion = Ext.fly(me.constrainTo).getViewRegion();
            }
        } else {
            if (!me._constrainRegion) {
                me._constrainRegion = me.getDragCt().getViewRegion();
            }
        }
        return me._constrainRegion;
    },

    getXY: function (constrain) {
        return constrain ? this.constrainModes[constrain](this, this.lastXY) : this.lastXY;
    },

    /**
     * Returns the X, Y offset of the current mouse position from the mousedown point.
     *
     * This method may optionally constrain the real offset values, and returns a point coerced in one
     * of two modes:
     *
     *  - `point`
     *    The current mouse position is coerced into the constrainRegion and the resulting position is returned.
     *  - `dragTarget`
     *    The new {@link Ext.util.Region Region} of the {@link #getDragTarget dragTarget} is calculated
     *    based upon the current mouse position, and then coerced into the constrainRegion. The returned
     *    mouse position is then adjusted by the same delta as was used to coerce the region.\
     *
     * @param {String} constrainMode (Optional) If omitted the true mouse position is returned. May be passed
     * as `point` or `dragTarget`. See above.
     * @returns {Number[]} The `X, Y` offset from the mousedown point, optionally constrained.
     */
    getOffset: function (constrain) {
        var xy = this.getXY(constrain),
            s = this.startXY;

        return [xy[0] - s[0], xy[1] - s[1]];
    },

    constrainModes: {
        // Constrain the passed point to within the constrain region
        point: function (me, xy) {
            var dr = me.dragRegion,
                constrainTo = me.getConstrainRegion();

            // No constraint
            if (!constrainTo) {
                return xy;
            }

            dr.x = dr.left = dr[0] = dr.right = xy[0];
            dr.y = dr.top = dr[1] = dr.bottom = xy[1];
            dr.constrainTo(constrainTo);

            return [dr.left, dr.top];
        },

        // Constrain the dragTarget to within the constrain region. Return the passed xy adjusted by the same delta.
        dragTarget: function (me, xy) {
            var s = me.startXY,
                dr = me.startRegion.copy(),
                constrainTo = me.getConstrainRegion(),
                adjust;

            // No constraint
            if (!constrainTo) {
                return xy;
            }

            // See where the passed XY would put the dragTarget if translated by the unconstrained offset.
            // If it overflows, we constrain the passed XY to bring the potential
            // region back within the boundary.
            dr.translateBy(xy[0] - s[0], xy[1] - s[1]);

            // Constrain the X coordinate by however much the dragTarget overflows
            if (dr.right > constrainTo.right) {
                xy[0] += adjust = (constrainTo.right - dr.right);    // overflowed the right
                dr.left += adjust;
            }
            if (dr.left < constrainTo.left) {
                xy[0] += (constrainTo.left - dr.left);      // overflowed the left
            }

            // Constrain the Y coordinate by however much the dragTarget overflows
            if (dr.bottom > constrainTo.bottom) {
                xy[1] += adjust = (constrainTo.bottom - dr.bottom);  // overflowed the bottom
                dr.top += adjust;
            }
            if (dr.top < constrainTo.top) {
                xy[1] += (constrainTo.top - dr.top);        // overflowed the top
            }
            return xy;
        }
    }
});