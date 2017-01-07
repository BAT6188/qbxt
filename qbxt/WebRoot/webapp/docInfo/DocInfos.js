/**
 * 文档信息
 * author：donghao
 */
Ext.define('Ushine.docInfo.DocInfos', {
	extend: 'Ext.panel.Panel',
	layout: 'border',
	border:false,
	bodyStyle: 'background-color: #ffffff; border: none; padding: 10px;',
	constructor: function(id) {
		var self=this;
		this.items=[{
			region : 'north',
			border:false,
			items:
			Ext.create('Ushine.base.TitleBar', {
				cTitle: '文档信息'
			}),
			bodyStyle: 'background-color: #ffffff;'
		},{
			id:"west",
			width:200,
			border:true,
			title:'文档信息列表',
			region : 'west',
			margins:'5 5 0 0',
			xtype:'treepanel',
			store: new Ext.data.TreeStore({
				root: {
			        expanded: true,
					children:[
					   {"id":"26818283525ce64b01525ce654e8008g","text":"业务文档库","url":"","className":"Ushine.docInfo.ServiceDocument","iconCls":"","leaf":true,"expanded":false},
					   {"id":"26818283525ce64b01525ce654e8008c","text":"外来文档库","url":"","className":"Ushine.docInfo.ForeignDocument","iconCls":"","leaf":true,"expanded":false},
					   {"id":"26818283525ce64b01525ce654e8008d","text":"领导讲话库","url":"","className":"Ushine.docInfo.LeaderTalk","iconCls":"","leaf":true,"expanded":false}
					]
				}
		    }),
		    lines: true,
			autoScroll: true,
			rootVisible: false,
			listeners: {
				select: function(view, record, item, index, e) {
					var className = record.raw.className;
		        	var mainPanel = self.getComponent(2);
		        	mainPanel.removeAll(true);
		        	mainPanel.add(Ext.create(className));
		        },
		        render:function(thiz,records){
					this.getSelectionModel().select(this.getStore().tree.root.childNodes[0]); 
				}
		    }
		},{
			id:'main_panel',
			border:true,
			layout: 'fit',
			margins:'5 0 0 5',
			region : 'center',
			bodyStyle:'border:solid 1px #cbcbcb;',
		}];
		this.callParent(); 
	}
	
});