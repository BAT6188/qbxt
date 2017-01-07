/**
 * 线索组织 author：donghao
 */
Ext.define('Ushine.cluesInfo.ClueOrganiz', {
	extend : 'Ext.panel.Panel',
	id : 'clueOrganiz_panel',
	region : 'center',
	bodyStyle : 'background-color: #ffffff; border: none; padding: 10px;',
	layout : {
		type : 'vbox',
		align : 'stretch',
		pack : 'start'
	},
	constructor : function(clueId,fieldValue,dataSearch,configFieldValue) {
		var self = this;
		var date=new Date();
		//第一个月
		date.setMonth(0);
		//第一天
		date.setDate(1);
		var startTime=Ext.Date.format(date,'Y-m-d');
		var btnItems;
		var createNewbtn;
		var delBtn;
		var height;
		var panelHeight;
		if(dataSearch=='yes'&&configFieldValue!=undefined){
			//代表一键搜索
			btnItems=Ext.create('Ushine.buttons.MiniButton', {
				id: 'returnBtn',
				handler: function () {
					Ext.getCmp('content_frame').removeAll();
					Ext.getCmp('content_frame').add(Ext.create('Ushine.dataSearch.ClueStoreSearch',{
						fieldValue:configFieldValue
					}));
					
				}
			});
			height=10;
			panelHeight=90;
		}else{
			btnItems=Ext.create('Ushine.buttons.MiniButton', {
				id: 'returnBtn',
				handler: function () {
					Ext.getCmp('content_frame').removeAll();
					Ext.getCmp('content_frame').add(Ext.create('Ushine.cluesInfo.CluesPenel',{
						fieldValue:fieldValue
					}));
				}
			});
			createNewbtn= Ext.create('Ushine.buttons.IconButton', {
				border : false,
				id : 'createNewBtn',
				btnText : '关联组织',
				baseCls : 't-btn-red',
				handler : function() {
					//弹出新增人员window
	    		   self.selectAssociatedOrganizData(clueId);
				}
			});
			delBtn=Ext.create('Ushine.buttons.IconButton', {
				id : 'delBtn',
				btnText : '解除组织',
				// width:120,
				handler : function() {
					var personStoreGrid=Ext.getCmp('clueOrganizGrid');					    	   	
				   	   if(personStoreGrid.getSelectionModel().hasSelection()){
				   	   		//允许多行
				   	   		var record=personStoreGrid.getSelectionModel().getSelection();
				   	   		var ids=[];
				   	   		for(var i=0;i<record.length;i++){
				   	   		ids.push(record[i].get('id'));
				   	   		}
				   	   		Ext.Msg.confirm("提示","确定要解除选中的线索组织吗?",function(btn){
				   	   			if (btn == 'yes') {
				   	   			   self.removeClueOrganizByClueId(ids,clueId);
		    				   }
				   	   		})
				   	   }else{
				   	   		Ext.create('Ushine.utils.Msg').onInfo("请至少选择一行数据");
				   	   }
				}
			});
       		height=41;
       		panelHeight=120;
		}
		this.items = [Ext.create('Ushine.base.TitleBar', {
		       cTitle: '线索组织',
		       btnItems: btnItems
		   }),{
			// 工具栏
			xtype : 'panel',
			baseCls : 'tar-body',
			height : panelHeight,
			style : "margin-top:-10px;",
			layout : 'fit',
			items : {
				// 表单
				xtype : 'form',
				border : true,
				//height : 120,
				id : 'labl',
				baseCls : 'form-body1',
				items : [{
					layout : "column", // 行1
					height : height,
					baseCls : 'panel-body',
					items : [createNewbtn,delBtn]
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
											   {"text":"任意字段", "value":"anyField"}
											        /*{
												"text" : "组织名称",
												"value" : "organizName"
											}, {
												"text" : "网站地址",
												"value" : "websiteURL"
											},{
												"text" : "组织负责人",
												"value" : "orgHeadOfName"
											}, {
												"text" : "活动范围",
												"value" : "degreeOfLatitude"
											}, {
												"text" : "成立时间",
												"value" : "foundTime"
											}*/]
										}),
								value : 'anyField',
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
								width : 300,
								listeners:{
									 specialkey:function(field,e){
			                           if(e.getKey() == e.ENTER){
			                        	   //查询
			                        	   self.findclueOrganiz(clueId);
			                           }  
			                        }
								}
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
								value : startTime
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
										text : '查询组织',
										style : "margin-left:20px;",
										width : 100,
										labelWidth : 60,
										height : 22,
										id : 'search-Button',
										baseCls : 't-btn-red',
										handler : function() {
											// 查询线索组织
											self.findclueOrganiz(clueId);
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
		new Ushine.cluesInfo.ClueOrganizInfoGridPanel(clueId)];
		this.callParent();
	},
	// 查询
	findclueOrganiz : function(clueId) {
		this.remove('d_clueOraniz_grid');
		this.add(new Ushine.cluesInfo.ClueOrganizInfoGridPanel(clueId));
	},
	//选择关联组织数据
	selectAssociatedOrganizData:function(clueId){
		Ext.define('SelectAssociatedOrganizDataWin',{
			extend:'Ushine.win.Window',
			title : "选择组织数据",
			modal : true,
			id:'SelectAssociatedOrganizDataWin',
			layout : 'fit',
			border : false,
			closable : true,
			draggable:true,
			resizable : false,
			plain : true,
			padding:10,
			height:550,
			buttonAlign:"right",
			width:600,
			constructor:function(){
				var self=this;
				var date=new Date();
				//第一个月
				date.setMonth(0);
				//第一天
				date.setDate(1);
				var startTime=Ext.Date.format(date,'Y-m-d');
				this.items=[{
			    	xtype:'panel',
					region:'center',
					border:false,
					items:[{
				    	layout: "column", //行1
				    	height: 25,
						baseCls: 'panel-body',
						items:[{
						id:'field1',
						fieldLabel: '字段筛选',
						labelAlign : 'right',
						labelWidth: 60,
	            		xtype:'combo',
	            		allowNegative: false,
	            		allowBlank: false,
	            		editable: false,
	            		hiddenName: 'colnum',
	            		name:'colnum',
	            		emptyText: '请选择字段',
	            		valueField: 'value',
	            		store:Ext.create('Ext.data.Store', {
	            		    fields: ['text', 'value'],
	            		    data :[
	            		       {'text':'任意字段','value':'anyField'}
	            		    ]
	            		}),
	            		value:'anyField',
						width: 260
					},{
						fieldLabel: '关&nbsp;键&nbsp;&nbsp;字',
						id		  :'fieldValue1',
                    	xtype     : 'textfield',
                    	name      : 'filtrateKeyword',
                    	emptyText : '请输入相应字段的关键字...',
						height: 24,
						labelAlign : 'right',
						labelWidth: 100,
						width: 300,
						listeners:{
							//按enter
							specialkey:function(field,e){
								if(e.getKey()==e.ENTER){
									self.findDataByProperty();
								}
							}
						}
					}]
			},{
				layout: "column", //行2
				height: 25,
				border:false,
				margin:'10 0 0 0',
				items:[{
					labelAlign : 'right',
					fieldLabel: '开始时间:',
					id:'startTime1',
					labelWidth: 60,
					
					format: 'Y-m-d', 
					//editable:false,
					xtype: 'datefield',
					maxValue: new Date(),
					height: 22,
					width: 260,
					value:startTime
				},{
					labelAlign : 'right',
					fieldLabel: '结束时间:',
					labelWidth: 100,
					format: 'Y-m-d', 
					xtype: 'datefield',
					//editable:false,
					height: 22,
					width: 300,
					id:'endTime1',
					maxValue: new Date(),
					value:new Date()
				}]
			},{
				layout: "column", //行2
				height: 38,
				border:true,
				baseCls: 'form-body',
				items:[
				Ext.create('Ushine.buttons.Button', {
		        	text : '查询组织',
		        	style:"margin-left:10px;",
		        	width:100,
		        	labelWidth: 60,
		        	height:22,
		        	id : 'search-Button1',
		        	baseCls: 't-btn-red',
		        	handler:function(){
		        		//查询
                        self.findDataByProperty();
	                }
		        }),Ext.create('Ushine.buttons.Button', {
		        	text : '条件重置',
		        	width:100,
		        	style:"margin-left:10px;",
		        	id : 'reset-Button1',
		        	baseCls: 't-btn-yellow',
		        	height:22,
		        	handler:function(){
		        		Ext.getCmp("labl").getForm().reset();
		        	}
		        })]
			},
				new Ushine.cluesInfo.SelectAssociatedOrganizStoreDataGridPanel()]
				}];
				this.buttons=[
					      	  	Ext.create('Ushine.buttons.Button', {
					      	   		text: '确定',
					      	   		baseCls: 't-btn-red',
					      	   		handler: function () {
					      	   			var records = Ext.getCmp('SelectAssociatedOrganizStoreDataGridPanelId')
					      	   							.getSelectionModel().getSelection();
					      	   			var ids = [];
					      	   			//提取已选择数据的id
					      	   			for(var i = 0; i < records.length; i++){  
					      	   			ids.push(records[i].get("id"));
					      	   			}
						      	   		var loadMask=new Ext.LoadMask(self, {msg:"正在关联组织..."});
					      	   			loadMask.show();
						      	   		 //ajax操作关联
						      	   		 Ext.Ajax.request({
				    						    url: 'associatedClueStoreByClueId.do',
				    						    actionMethods: {
				    				                create : 'POST',
				    				                read   : 'POST', // by default GET
				    				                update : 'POST',
				    				                destroy: 'POST'
				    				            },
				    						    params: {
				    						    	ids:ids,
				    						    	clueId:clueId,
				    						    	store:'organizStore'
				    						    },
				    						    success: function(response){
				    						    	loadMask.hide();
				    						    	 var text = response.responseText;
				    							     var obj=Ext.JSON.decode(text);
				    							     if(obj.status){
				    							    	 Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				    							    	 Ext.getCmp('clueOrganiz_panel').findclueOrganiz(clueId);
				    							    	 Ext.getCmp('SelectAssociatedOrganizDataWin').close();
				    							     }
				    							     Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				    						    },
				    				            failure: function(form, action) {
				    				            	loadMask.hide();
				    				            	Ext.create('Ushine.utils.Msg').onInfo('关联线索组织失败，请联系管理员');
				    				            }
				    						});
					      	   			
					      	   		}
					      	  	
					      	   	})
					      	  ];
				this.callParent();
			},
			//查询数据
			findDataByProperty:function(type){
				var win=this.getComponent(0);
				win.remove(Ext.getCmp('SelectAssociatedOrganizStoreDataGridPanelId'))
				win.add(new Ushine.cluesInfo.SelectAssociatedOrganizStoreDataGridPanel());
			},
		});
		Ext.create('SelectAssociatedOrganizDataWin').show();
	},
	//解除线索组织关系
	removeClueOrganizByClueId:function(ids,clueId){
		var self=this;
		var loadMask=new Ext.LoadMask(self, {msg:"正在解除组织..."});
 		loadMask.show();
		Ext.Ajax.request({
		    url: 'removeClueByClueId.do',
		    method:'POST',
		    params: {
		    	ids: ids,
		    	clueId:clueId
		    },
		    success: function(response, opts){
		    	loadMask.hide();
		       var text = response.responseText;
		       var obj = Ext.JSON.decode(text);
		       if(obj.success){
		    	   Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
		    	   Ext.getCmp('clueOrganiz_panel').findclueOrganiz(clueId);
		       }else{
		    	   Ext.Msg.alert('提示',"解除线索组织失败，请联系管理员！");
		       }
		    },
		    failure:function(){
		    	loadMask.hide();
		    	Ext.Msg.alert('提示',"解除线索组织失败，请求后台服务失败！");
		    }
		});
	}

});

