lt.insoft.training.myComponents.CustomModal = zk.$extends(zk.Widget, {
    _title: '',
    _type: '',
    _show: '',

    setShow: function(show) {
        this._show = show;
        if(show){
            var uuid = this.uuid;
            $(this.$n()).modal('show');
        }else if (!show && this.desktop){
            $(this.$n()).modal('hide');
        }
    },

    setType: function(type) {
        if (this._type != type) {
            this._type = type;
        }
    },

    setTitle: function(title) {
        if (this._title != title) {
            this._title = title;
            if (this.desktop){
                this.$n("title").innerHTML = zUtl.encodeXML(title);
            }
        }
    },

    redraw: function (out) {
        output = out;
        var uuid = this.uuid;
        out.push('<div aria-labelledby="enlargeImageModal" ',
        'class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false" id="', uuid, '">');
        out.push('<div class="modal-dialog');
        if (this._type === 'picture') {
            out.push(" modal-lg");
        }
        out.push('">',
            '<div class="modal-content">',
                '<div class="modal-header">',
                    '<button type="button" id="', uuid, '-close" class="close">&#10006;</button>',
                    '<h4 class="');
        if(this._type === 'picture'){
            out.push('gallery-title"');
        }else {
            out.push('title"');
        }
        out.push('id="', uuid, '-title">', this._title, '</h4>',
                '</div>');
                    for (var w = this.firstChild; w; w = w.nextSibling){
                        w.redraw(out);
                    }
        out.push('</div>',
                '</div>',
            '</div>'
        );
    },

    _doClose: function(evt) {
        this.fire("onClick",{}, {toServer:true});
    },

    bind_: function(evt) {
        this.$supers('bind_', arguments);
        this.domListen_(this.$n('close'), 'onClick', '_doClose');
    },

    unbind_: function(evt) {
        this.domUnlisten_(this.$n('close'), 'onClick', '_doClose');
        this.$supers('unbind_', arguments);
    }

})