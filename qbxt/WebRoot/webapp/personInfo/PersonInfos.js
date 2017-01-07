Ext.define('Ushine.personInfo.PersonInfos', {
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
				cTitle: '重点信息',
			}),
			bodyStyle: 'background-color: #ffffff;'
		},{
			id:"west",
			width:200,
			border:true,
			title:'重点信息列表',
			region : 'west',
			margins:'5 5 0 0',
			xtype:'treepanel',
			store: new Ext.data.TreeStore({
				root: {
			        expanded: true,
					children:[
					   {"id":"4028b881524485a101524485ab5e001f","text":"重点人员","url":"","className":"Ushine.personInfo.PersonInfoManage","iconCls":"","leaf":true,"expanded":false},
					   {"id":"4028b881524485a101524485ab5e001g","text":"重点组织","url":"","className":"Ushine.personInfo.OrganizeInfoManage","iconCls":"","leaf":true,"expanded":false},
					   {"id":"26818283525ce64b01525ce654e8008b","text":"媒体网站刊物","url":"","className":"Ushine.docInfo.MediaNetworkBook","iconCls":"","leaf":true,"expanded":false},
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