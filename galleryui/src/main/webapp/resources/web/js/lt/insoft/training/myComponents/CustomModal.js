lt.insoft.training.myComponents.CustomModal = zk.$extends(zk.Widget, {

    _title: '',
    _type: '',

    getType: function() {
        return this._type;
    },

    setType: function(type) {
        if (this._type != type) {
            this._type = type;
            if (this.desktop){
                this.$n().innerHTML = zUtl.encodeXML(type);
            }
        }
    },

    getTitle: function() {
        return this._title;
    },

    setTitle: function(title) {
        if (this._title != title) {
            this._title = title;
            if (this.desktop){
                $('.modal-title').text(zUtl.encodeXML(title));
            }
        }
    },

    redraw: function (out) {
            var uuid = this.uuid;
        	out.push('<div aria-labelledby="enlargeImageModal" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false" id="', uuid, '">');
                        out.push('<div class="modal-dialog');
                        if (this.getType() === "picture") {
                            out.push(" modal-lg");
                        }
                        out.push('">',
                            '<div class="modal-content">',
                                '<div class="modal-header">',
                                    '<button type="button" class="close">&#10006;</button>',
                                    '<h4 class="modal-title">', this.getTitle(), '</h4>',
                                '</div>');
                                    for (var w = this.firstChild; w; w = w.nextSibling)
                                        w.redraw(out);
            out.push('</div>',
                        '</div>',
                    '</div>'
        	);
    },

    _doClose: function(evt) {
        var uuid = this.uuid;
        $('#' + uuid).modal('toggle');
    },

    _show: function(evt) {
        var uuid = this.uuid;
        $('#' + uuid).modal('show');
    },

    bind_: function(evt) {
        this.$supers('bind_', arguments);
        this.domListen_($("button.close"), "onClick", '_doClose');
        this.domListen_($(".picture"), "onClick", '_show');
    },

    unbind_: function(evt) {
        this.domUnlisten_($("button.close"), "onClick", 'doClose');
        this.domUnlisten_($(".picture"), "onClick", '_show');
        this.$supers('unbind_', arguments);
    }
})