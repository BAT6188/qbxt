Ext.define('Ushine.infoTypeManage.InfoTypeManage', {
	extend: 'Ext.panel.Panel',
	layout: 'border',
	border:false,
	bodyStyle: 'background-color: #ffffff; border: none;',
	constructor: function(id) {
		var self=this;
		this.items=[{
			id:"infoTypeMangerTreeId",
			width:200,
			border:true,
			title:'类别菜单',
			region : 'west',
			
			xtype:'treepanel',
			store: new Ext.data.TreeStore({
				fields:['text', 'id','type'],
		        proxy: {
		            type: 'ajax',
		            url:'getInfoTypeTree.do',
		        },
			    autoDestroy: true,
			    autoLoad:true
		    }),
//			store: new Ext.data.TreeStore({
//				root: {
//			        expanded: true,
//					children:[
//					   {"id":"4028b881524485a101524485ab5d0054","text":"业务文档库类别","url":"","className":"Ushine.infoTypeManage.InfoTypeSet","iconCls":"","leaf":true,"expanded":false},
//					 ]
//				}
//		    }),
		    listeners: {
				select: function(view, record, item, index, e) {
					var className = record.raw.className;
		        	var mainPanel = self.getComponent(1);
		        	mainPanel.removeAll(true);
		        	mainPanel.add(new Ushine.infoTypeManage.InfoTypeSet(record.raw.id,record.raw.text));
		        },
		        afterrender:function(thiz,records){
		        	console.log(this.getStore().tree.root.childNodes[0]);
					this.getSelectionModel().select(this.getStore().tree.root.childNodes[0]); 
				}
		    },
		    lines: true,
			autoScroll: true,
			rootVisible: false,
			
		},{
			id:'main_panel1',
			border:true,
			layout: 'fit',
			margins:'0 0 0 10',
			region : 'center',
			bodyStyle:'border:solid 1px #cbcbcb;',
		}];
		this.callParent(); 
	}
	
});