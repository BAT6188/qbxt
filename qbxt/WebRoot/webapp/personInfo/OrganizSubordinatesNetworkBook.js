/**
 * 组织下属媒体刊物 author：donghao
 */
Ext.define('Ushine.personInfo.OrganizSubordinatesNetworkBook', {
	extend : 'Ext.panel.Panel',
	id : 'organizSubordinatesNetworkBooknetworkbook_panel',
	region : 'center',
	bodyStyle : 'background-color: #ffffff; border: none; padding: 10px;',
	layout : {
		type : 'vbox',
		align : 'stretch',
		pack : 'start'
	},
	constructor : function(organizId) {
		var self = this;
		this.items = [Ext.create('Ushine.base.TitleBar', {
		       cTitle: '组织下属媒体刊物',
		       btnItems: [
					// 返回任务操作
					Ext.create('Ushine.buttons.MiniButton', {
						id: 'returnBtn',
						handler: function () {
							Ext.getCmp('content_frame').removeAll();
							Ext.getCmp('content_frame').add(Ext.create('Ushine.personInfo.PersonInfos'));
						}
					})
			]
		   }),{
			// 工具栏
			xtype : 'panel',
			baseCls : 'tar-body',
			height : 120,
			style : "margin-top:-10px;",
			layout : 'fit',
			items : {
				// 表单
				xtype : 'form',
				border : true,
				height : 120,
				id : 'labl',
				baseCls : 'form-body1',
				items : [{
					layout : "column", // 行1
					height : 41,
					baseCls : 'panel-body',
					items : [Ext.create('Ushine.buttons.IconButton', {
										border : false,
										id : 'createNewBtn',
										btnText : '关联刊物',
										baseCls : 't-btn-red',
										handler : function() {
											// 弹出选择刊物数据窗体
											self.selectAssociatedWebsiteJournalData(organizId);
										}
									}),
									Ext.create('Ushine.buttons.IconButton', {
								id : 'delBtn',
								btnText : '解除刊物',
								// width:120,
								handler : function() {
									 var personStoreGrid=Ext.getCmp('organizSuborinatesNetworkbookgrid');					    	   	
								   	   if(personStoreGrid.getSelectionModel().hasSelection()){
								   	   		//允许多行
								   	   		var record=personStoreGrid.getSelectionModel().getSelection();
								   	   		var ids=[];
								   	   		for(var i=0;i<record.length;i++){
								   	   		ids.push(record[i].get('id'));
								   	   		}
								   	   		Ext.Msg.confirm("提示","确定要解除选中的组织下属媒体刊物吗?",function(btn){
								   	   			if (btn == 'yes') {
								   	   			   self.removeOrgSuborinatesWebsiteJournal(ids,organizId);
						    				   }
								   	   		})
								   	   }else{
								   	   		Ext.create('Ushine.utils.Msg').onInfo("请至少选择一行数据");
								   	   }
								}
							})]
				}, {
					layout : "column", // 行1
					height : 25,
					baseCls : 'panel-body',
					style : 'margin-top:-10px;',
					items : [{
								id : 'field',
								fieldLabel : '字段筛选',
								labelAlign : 'right',
								labelWidth : 60,
								xtype : 'combo',
								allowNegative : false,
								allowBlank : false,
								editable : false,
								hiddenName : 'colnum',
								emptyText : '请选择字段',
								valueField : 'value',
								store : Ext.create('Ext.data.Store', {
											fields : ['text', 'value'],
											data : [
											        {
											        	"text": "刊物名称",
											        	"value": "name"
											        }, {
											        	"text": "域名",
											        	"value": "websiteURL"
											        }, {
											        	"text": "服务器所在地",
											        	"value": "serverAddress"
											        }, {
											        	"text": "创办地",
											        	"value": "establishAddress"
											        }, {
											        	"text": "主要发行地",
											        	"value": "mainWholesaleAddress"
											        }, {
											        	"text": "创办人",
											        	"value": "establishPerson"
											        }, {
											        	"text": "创办时间",
											        	"value": "establishTime"
											        }]
										}),
								value : 'name',
								width : 260
							}, {
								fieldLabel : '关&nbsp;键&nbsp;&nbsp;字',
								id : 'fieldValue',
								xtype : 'textfield',
								name : 'filtrateKeyword',
								emptyText : '请输入相应字段的关键字...',
								height : 24,
								labelAlign : 'right',
								labelWidth : 100,
								width : 300
							}]
				}, {
					layout : "column", // 行2
					height : 45,
					baseCls : 'form-body',
					items : [{
								labelAlign : 'right',
								fieldLabel : '开始时间:',
								id : 'startTime',
								labelWidth : 60,

								format : 'Y-m-d',
								// editable:false,
								xtype : 'datefield',
								maxValue : new Date(),
								height : 22,
								width : 260,
								value : getYearFirstDay()
							}, {
								labelAlign : 'right',
								fieldLabel : '结束时间:',
								labelWidth : 100,
								format : 'Y-m-d',
								xtype : 'datefield',
								// editable:false,
								height : 22,
								width : 300,
								id : 'endTime',
								maxValue : new Date(),
								value : new Date()
							}, Ext.create('Ushine.buttons.Button', {
										text : '查询文档',
										style : "margin-left:20px;",
										width : 100,
										labelWidth : 60,
										height : 22,
										id : 'search-Button',
										baseCls : 't-btn-red',
										handler : function() {
											// 查询组织下属媒体网站刊物
											self.findOrganizSuborinatesNetworkBook(organizId);
										}
									}), Ext.create('Ushine.buttons.Button', {
										text : '条件重置',
										width : 100,
										style : "margin-left:10px;",
										id : 'reset-Button',
										baseCls : 't-btn-yellow',
										height : 22,
										handler : function() {
											Ext.getCmp("labl").getForm()
													.reset();
										}
									})]
				}]
			}
		},
		new Ushine.personInfo.OrganizSubordinatesNetworkGridPanel(organizId)];
		this.callParent();
	},
	//选择关联人员数据
	selectAssociatedWebsiteJournalData:function(organizId){
		//定义一个SaveCluesWin的Window
		Ext.define('SelectAssociatedWebsiteJournalDataWin',{
			extend:'Ushine.win.Window',
			title : "选择媒体刊物数据",
			modal : true,
			id:'SelectAssociatedWebsiteJournalDataWin',
			layout : 'fit',
			border : false,
			closable : true,
			draggable:true,
			resizable : false,
			plain : true,
			height:500,
			buttonAlign:"center",
			width:570,
			constructor:function(){
				var self=this;
				this.items=[
					{
						layout:'fit',
				    	xtype:'panel',
						region:'center',
						border:false,
						id:'selectAssociatedWebsiteJournalDataId',
						items:new Ushine.cluesInfo.SelectAssociatedWebsiteJournalDataGridPanel()
				    }
				];
				this.buttons=[
				      	  	Ext.create('Ushine.buttons.Button', {
				      	   		text: '确定',
				      	   		baseCls: 't-btn-red',
				      	   		handler: function () {
				      	   			var records = Ext.getCmp('SelectAssociatedWebsiteJournalDataGridPanelId').getSelectionModel().getSelection();
				      	   			var ids = [];
				      	   			//提取已选择数据的id
				      	   			for(var i = 0; i < records.length; i++){  
				      	   			ids.push(records[i].get("id"));
				      	   			}
				      	   		 //ajax操作关联刊物
				      	   		 Ext.Ajax.request({
		    						    url: 'associatedOrgSuborinatesBook.do',
		    						    actionMethods: {
		    				                create : 'POST',
		    				                read   : 'POST', // by default GET
		    				                update : 'POST',
		    				                destroy: 'POST'
		    				            },
		    						    params: {
		    						    	ids:ids,
		    						    	organizId:organizId
		    						    },
		    						    success: function(response){
		    						    	 var text = response.responseText;
		    							     var obj=Ext.JSON.decode(text);
		    							     if(obj.status){
		    							    	 Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
		    							    	 Ext.getCmp('organizSubordinatesNetworkBooknetworkbook_panel').findOrganizSuborinatesNetworkBook(organizId);
		    							    	 Ext.getCmp('SelectAssociatedWebsiteJournalDataWin').close();
		    							     }
		    							     Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
		    						    },
		    				            failure: function(form, action) {
		    				            	Ext.create('Ushine.utils.Msg').onInfo('关联线索媒体刊物失败，请联系管理员');
		    				            }
		    						});
				      	   			
				      	   		}
				      	  	
				      	   	})
				      	  ];
				this.callParent();
			}
		});
		Ext.create('SelectAssociatedWebsiteJournalDataWin').show();
	},
	// 查询
	findOrganizSuborinatesNetworkBook : function(organizId) {
		this.remove('d_organizSuborinatesNetworkbook_grid');
		this.add(new Ushine.personInfo.OrganizSubordinatesNetworkGridPanel(organizId));
	},
	//解除组织下属媒体刊物
	removeOrgSuborinatesWebsiteJournal:function(ids,organizId){
		var self=this;
		Ext.Ajax.request({
		    url: 'removeOrganizSuborinatesBook.do',
		    method:'POST',
		    params: {
		    	ids: ids,
		    	organizId:organizId
		    },
		    success: function(response, opts){
		       var text = response.responseText;
		       var obj = Ext.JSON.decode(text);
		       if(obj.success){
		    	   Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
		    	   Ext.getCmp('organizSubordinatesNetworkBooknetworkbook_panel').findOrganizSuborinatesNetworkBook(organizId);
		       }else{
		    	   Ext.Msg.alert('提示',"解除线索媒体刊物失败，请联系管理员！");
		       }
		    },
		    failure:function(){
		    	 Ext.Msg.alert('提示',"解除线索媒体刊物失败，请求后台服务失败！");
		    }
		});
	}

});

