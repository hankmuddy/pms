Ext.define("Pms.core.Print", {
    statics: {
        print: function(htmlElement, printAutomatically) {
            var win = window.open('', 'Print Panel');

            win.document.open();
            win.document.write(htmlElement.outerHTML);
            win.document.close();

            console.log(win);

            if(printAutomatically) {
                win.print();
            }

            if(this.closeAutomaticallyAfterPrint) {
                if(Ext.isIE) {
                    window.close();
                } else {
                    win.close();
                }
            }
        }

    }
});

Pms.Print = Pms.core.Print;