Ext.define('Ushine.pDFStyleSetUp.PDFStyleSetUp', {
	extend: 'Ext.panel.Panel',
	layout: 'border',
	border:false,
	bodyStyle: 'background-color: #ffffff; border: none; padding: 10px;',
	constructor: function(className,url) {
		var self=this;
		this.items=[{
			region : 'north',
			border:false,
			items:
			Ext.create('Ushine.base.TitleBar', {
				cTitle: 'PDF样式设置',
				btnItems: [
/*					Ext.create('Ushine.buttons.MiniButton', {
						id: 'createBtn',
						handler: function () {
						//self.getComponent(1).enable();
						}
					}), */
					// 返回任务操作
					Ext.create('Ushine.buttons.MiniButton', {
						id: 'returnBtn',
						handler: function () {
								Ext.getCmp('content_frame').removeAll();
								Ext.getCmp('content_frame').add(Ext.create(url));
						}
					})
				]
		}),
			bodyStyle: 'background-color: #ffffff;'
		},{
			id:"PDFSetUpWestId",
			width:200,
			border:true,
			title:'PDF基础信息菜单',
			region : 'west',
			margins:'5 5 0 0',
			xtype:'treepanel',
			store: new Ext.data.TreeStore({
				root: {
			        expanded: true,
					children:[
					   {"id":"4028b881524485a101524485ab5e001f","text":"人员库PDF样式","url":"","className":"Ushine.pDFStyleSetUp.PersonPDFStyleManage","iconCls":"","leaf":true,"expanded":false},
					   {"id":"4028b881524485a101524485ab5e001g","text":"组织库PDF样式","url":"","className":"Ushine.pDFStyleSetUp.OrganizPDFStyleManage","iconCls":"","leaf":true,"expanded":false},
					   {"id":"4028b881524485a101524485ab5e001y","text":"媒体刊物库PDF样式","url":"","className":"Ushine.pDFStyleSetUp.WebsiteJounalPDFStyleManage","iconCls":"","leaf":true,"expanded":false},
					   {"id":"4028b881524485a101524485ab5e001h","text":"线索库PDF样式","url":"","className":"Ushine.pDFStyleSetUp.CluePDFStyleManage","iconCls":"","leaf":true,"expanded":false}
					   
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
		        	var PDFSetUpWest =Ext.getCmp('PDFSetUpWestId').store.tree.root.data.children;
		        	if(className=='Ushine.pDFStyleSetUp.PersonPDFStyleManage'){
		        		mainPanel.add(new Ushine.pDFStyleSetUp.PersonPDFStyleManage(url));
		        	}else if(className=='Ushine.pDFStyleSetUp.OrganizPDFStyleManage'){
		        		mainPanel.add(new Ushine.pDFStyleSetUp.OrganizPDFStyleManage(url));
		        	}else if(className=='Ushine.pDFStyleSetUp.CluePDFStyleManage'){
		        		mainPanel.add(new Ushine.pDFStyleSetUp.CluePDFStyleManage(url));
		        	}else{
		        		mainPanel.add(new Ushine.pDFStyleSetUp.WebsiteJounalPDFStyleManage(url));
		        	}
		        	
		        },
		        render:function(thiz,records){
		        	if(className=='Ushine.pDFStyleSetUp.PersonPDFStyleManage'){
		        		this.getSelectionModel().select(this.getStore().tree.root.childNodes[0]); 
		        	}else if(className=='Ushine.pDFStyleSetUp.OrganizPDFStyleManage'){
		        		this.getSelectionModel().select(this.getStore().tree.root.childNodes[1]); 
		        	}else if(className=='Ushine.pDFStyleSetUp.WebsiteJounalPDFStyleManage'){
		        		this.getSelectionModel().select(this.getStore().tree.root.childNodes[2]); 
		        	}else if(className=='Ushine.pDFStyleSetUp.CluePDFStyleManage'){
		        		this.getSelectionModel().select(this.getStore().tree.root.childNodes[3]); 
		        	}else{
		        		this.getSelectionModel().select(this.getStore().tree.root.childNodes[0]); 
		        	}
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