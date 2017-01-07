Ext.define('Ext.form.KindEditor', {
    extend: 'Ext.form.field.TextArea',
    alias: 'widget.kindeditorfield',
    allowBlank: true,
    initComponent: function() {
        this.callParent();
    },
    constructor: function(config){
    	config.layout = 'fit';
		this.config = config;
		this.callParent([config]);
	},
    afterRender : function() {
    	var me = this;
        this.callParent(arguments);
        var id = this.inputEl.dom.id;
//      this.tpl.overwrite(this.bodyEl.dom, {id:id});
    	me.editor = KindEditor.create("#"+id, {
    		basePath : 'kindeditor/',
    		themesPath : 'kindeditor/themes/',
    		pluginsPath : 'kindeditor/plugins/',
    		langPath : 'kindeditor/lang/',
	        cssPath : 'kindeditor/plugins/code/prettify.css',
	        uploadJson : 'announcementUploadFile.do',//上传文件的服务器端程序
			fileManagerJson : 'kindeditor/previewImage.do',//浏览远程图服务器端程序
			newlineTag : 'br',//回车换行标签  默认值 'p'
			pasteType : 2,//0:禁止粘贴 1:纯文本粘贴 2:HTML粘贴
			resizeType : 0,//2:可以改变宽度和高度 1:只能改变高度 0:不能拖动
			filePostName : '',//上传文件form名称
			width: me.width + 'px',
			height: me.height + 'px',
			minWidth: 525,
			minHeight: 190,
			items: [
			    'undo', 'redo', '|', 'selectall', 'preview', 'cut', 'copy', 'paste', 'plainpaste', '|', 'justifyleft', 'justifycenter',
			    'justifyright', 'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript', 'superscript',
			    'quickformat', '|', '', 'fullscreen', 'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'blod',
			    'italic', 'underline', 'strikethrough', 'removeformat', '|', 'image', 'multiimage', 'table', 'hr', 'emoticons', 'link',
			    'unlink', 'lineheight', 'pagebreak', 'insertfile'
			],
			afterChange : function() {
				var self = this;
				//var result = KindEditor.escape(self.html());//将特殊字符转换成HTML entities
				if(self.html() == '<br />'){
					self.html('');
				}
				me.setValue(self.html());
				//self.sync();
			}
    	});
    },
});