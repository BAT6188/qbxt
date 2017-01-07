/**
 * 人员管理
 */
Ext.define('Ushine.cluesInfo.CluePerson',{
	extend:'Ext.panel.Panel',
	id:'cluePersoninfomanage_info',
	bodyStyle: 'background-color: #ffffff; border: none; padding: 10px;',
	layout: {
		type: 'vbox',
		align : 'stretch',
		pack  : 'start'
	},
	constructor: function(clueId,fieldValue,dataSearch,configFieldValue) {
		var self = this;
		var btnItems;
		var date=new Date();
		//第一个月
		date.setMonth(0);
		//第一天
		date.setDate(1);
		var startTime=Ext.Date.format(date,'Y-m-d');
		var createNewbtn;
		var delBtn;
		var height=41;
		var panelHeight=120;
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
		    	   border: false,
		    	   id: 'createNewBtn',
		    	   btnText: '关联人员', 
		    	   baseCls: 't-btn-red',
		    	   handler: function() {
		    	   	   //弹出新增人员window
		    		   self.selectAssociatedPersonData(clueId);
		    	   }
		   });
			delBtn=Ext.create('Ushine.buttons.IconButton', {
			    	   id: 'delBtn',
			    	   btnText: '解除人员',
			    	   handler: function () {
			    		  var personStoreGrid=Ext.getCmp('cluePersonInfoGridPanel');					    	   	
					   	   if(personStoreGrid.getSelectionModel().hasSelection()){
					   	   		//允许多行
					   	   		var record=personStoreGrid.getSelectionModel().getSelection();
					   	   		var ids=[];
					   	   		for(var i=0;i<record.length;i++){
					   	   		ids.push(record[i].get('id'));
					   	   		}
					   	   		Ext.Msg.confirm("提示","确定要解除选中的线索人员吗?",function(btn){
					   	   			if (btn == 'yes') {
					   	   			   self.removeCluePesonByClueId(ids,clueId);
			    				   }
					   	   		})
					   	   }else{
					   	   		Ext.create('Ushine.utils.Msg').onInfo("请至少选择一行数据");
					   	   }
			    	   }
		       		});
		}
		this.items = [         // 标题栏
		   Ext.create('Ushine.base.TitleBar', {
		       cTitle: '线索人员',
		       btnItems:btnItems
		   }),{
			// 工具栏
			xtype : 'panel',
		    baseCls : 'tar-body',
			height:panelHeight,
			style:"margin-top:-10px;",
			layout:'fit',
			items:{
				//表单
				xtype:'form',
				border:true,
				//height:120,
				id:'labl',
				baseCls: 'form-body1',
				items:[{
		    	   layout: "column", //行1
					height: height,
					baseCls: 'panel-body',
					items:[createNewbtn ,
					       delBtn,
					       Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'downExcelBtn',
					    	   btnText: '导出人员',
					    	   //style:"margin-left:10px;",
					    	   handler: function () {
					    		  var personStoreGrid=Ext.getCmp('cluePersonInfoGridPanel');	
					    		  //传递时间参数
					    		  var date=Ext.Date.format(new Date(),'YmdHms');
							   	   if(personStoreGrid.getSelectionModel().hasSelection()){
							   	   		//允许多行
							   	   		var record=personStoreGrid.getSelectionModel().getSelection();
							   	   		var personStoreIds=[];
							   	   		for(var i=0;i<record.length;i++){
							   	   			personStoreIds.push(record[i].get('id'));
							   	   		}
							   	   		//导入到Excel中
							   	   		//导出并下载
							   	   		exportPersonStoreToExcel(self,personStoreIds,date);
							   	   }else{
							   	   		Ext.create('Ushine.utils.Msg').onInfo("请至少选择一行数据");
							   	   }
					    	   }
					       })
					      ]
					}, {
					    	layout: "column", //行1
					    	height: 25,
							baseCls: 'panel-body',
							style:'margin-top:-10px;',
							items:[{
							id:'field',
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
		            		    data : [
		            		    	//字段筛选
		            		    	{"text":"任意字段", "value":"anyField"},
		            		       /* {"text":"姓名", "value":"personName"},
		            		        {"text":"曾用名", "value":"nameUsedBefore"},
		            		        {"text":"英文名", "value":"englishName"},
		            		        {"text":"现住地址", "value":"presentAddress"},
		            		        {"text":"工作单位", "value":"workUnit"},
		            		        {"text":"户籍地址", "value":"registerAddress"},
		            		        {"text":"出生日期", "value":"bebornTime"}*/
		            		    ]
		            		}),
		            		value:'anyField',
							width: 260
						},{
							fieldLabel: '关&nbsp;键&nbsp;&nbsp;字',
							id		  :'fieldValue',
	                    	xtype     : 'textfield',
	                    	name      : 'filtrateKeyword',
	                    	emptyText : '请输入相应字段的关键字...',
							height: 24,
							labelAlign : 'right',
							labelWidth: 100,
							width: 300,
							listeners:{
								 specialkey:function(field,e){
		                           if(e.getKey() == e.ENTER){
		                        	   //查询
		   	                           self.findPersonByProperty(clueId);
		                           }  
		                        }
							}
						}]
				},{
					layout: "column", //行2
					height: 45,
					baseCls: 'form-body',
					items:[{
						labelAlign : 'right',
						fieldLabel: '开始时间:',
						id:'startTime',
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
						id:'endTime',
						maxValue: new Date(),
						value:new Date()
					},
					Ext.create('Ushine.buttons.Button', {
			        	text : '查询人员',
			        	style:"margin-left:20px;",
			        	width:100,
			        	labelWidth: 60,
			        	height:22,
			        	id : 'search-Button',
			        	baseCls: 't-btn-red',
			        	handler:function(){
			        		//查询
	                        self.findPersonByProperty(clueId);
		                }
			        }),Ext.create('Ushine.buttons.Button', {
			        	text : '条件重置',
			        	width:100,
			        	style:"margin-left:10px;",
			        	id : 'reset-Button',
			        	baseCls: 't-btn-yellow',
			        	height:22,
			        	handler:function(){
			        		Ext.getCmp("labl").getForm().reset();
			        	}
			        })]
				}]
				}
		},
		//人员gridpanel
		new Ushine.cluesInfo.CluePersonInfoGridPanel(clueId)
		];	
		this.callParent();		
	},
	
	//选择关联人员数据
	selectAssociatedPersonData:function(clueId){
		Ext.define('SelectExistingDataWin',{
			extend:'Ushine.win.Window',
			title : "选择已有数据",
			modal : true,
			id:'SelectExistingDataWin',
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
		        	text : '查询人员',
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
			new Ushine.cluesInfo.SelectAssociatedPersonDataGridPanel()]
			}];
				this.buttons=[
		      	  	Ext.create('Ushine.buttons.Button', {
		      	   		text: '确定',
		      	   		baseCls: 't-btn-red',
		      	   		handler: function () {
		      	   			var records = Ext.getCmp('SelectAssociatedPersonDataGridPanelId').getSelectionModel().getSelection();
		      	   			var ids = [];
		      	   			//提取已选择数据的id
		      	   			for(var i = 0; i < records.length; i++){  
		      	   				ids.push(records[i].get("id"));
		      	   			}
		      	   			//ajax操作关联人员
		      	   			var loadMask=new Ext.LoadMask(self, {msg:"正在关联人员..."});
		      	   			loadMask.show();
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
	    						    	store:'personStore'
	    						    },
	    						    success: function(response){
	    						    	loadMask.hide();
	    						    	 var text = response.responseText;
	    							     var obj=Ext.JSON.decode(text);
	    							     if(obj.status){
	    							    	 Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
	    							    	 Ext.getCmp('cluePersoninfomanage_info').findPersonByProperty(clueId);
	    							    	 Ext.getCmp('SelectExistingDataWin').close();
	    							     }
	    							     Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
	    						    },
	    				            failure: function(form, action) {
	    				            	Ext.create('Ushine.utils.Msg').onInfo('关联线索人员失败，请联系管理员');
	    				            	loadMask.hide();
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
				win.remove(Ext.getCmp('SelectAssociatedPersonDataGridPanelId'))
				win.add(new Ushine.cluesInfo.SelectAssociatedPersonDataGridPanel());
			}
		});
		Ext.create('SelectExistingDataWin').show();
	},
	//查询人员
	findPersonByProperty:function(clueId){
		this.remove('p_cluePersoninfo_grid');
		this.add(new Ushine.cluesInfo.CluePersonInfoGridPanel(clueId));
	},
	//解除线索人员关系
	removeCluePesonByClueId:function(ids,clueId){
		var self=this;
		var loadMask=new Ext.LoadMask(self, {msg:"正在解除人员..."});
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
		    	   Ext.getCmp('cluePersoninfomanage_info').findPersonByProperty(clueId);
		       }else{
		    	   Ext.Msg.alert('提示',"解除线索人员失败，请联系管理员！");
		       }
		    },
		    failure:function(){
		    	 Ext.Msg.alert('提示',"解除线索人员失败，请求后台服务失败！");
		    	 loadMask.hide();
		    }
		});
	}
});



