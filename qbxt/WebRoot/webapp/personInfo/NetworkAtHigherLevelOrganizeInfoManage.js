/**
 * 媒体上级组织
 */
Ext.define('Ushine.personInfo.NetworkAtHigherLevelOrganizeInfoManage',{
	extend:'Ext.panel.Panel',
	id:'organizeinfomanage-panel',
	bodyStyle: 'background-color: #ffffff; border: none; padding: 10px;',
	layout: {
		type: 'vbox',
		align : 'stretch',
		pack  : 'start'
	},
	constructor: function(networkId) {
		var self = this;
		this.items = [Ext.create('Ushine.base.TitleBar', {
		       cTitle: '媒体上级组织',
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
			height:120,
			style:"margin-top:-10px;",
			layout:'fit',
			
			items:{
				//表单
				xtype:'form',
				border:true,
				height:120,
				id:'labl',
				baseCls: 'form-body1',
				items:[{
		    	   layout: "column", //行1
					height: 41,
					baseCls: 'panel-body',
					items:[Ext.create('Ushine.buttons.IconButton', {
						border : false,
						id : 'createNewBtn',
						btnText : '关联组织',
						baseCls : 't-btn-red',
						handler : function() {
							//弹出选择组织窗体
				    		self.selectAssociatedOrganizData(networkId);
						}
					}),
					Ext.create('Ushine.buttons.IconButton', {
				id : 'delBtn',
				btnText : '解除组织',
				// width:120,
				handler : function() {
					var organizSubordinatesGrid=Ext.getCmp('organizeInfoGridPanel');					    	   	
				   	   if(organizSubordinatesGrid.getSelectionModel().hasSelection()){
				   	   		//允许多行
				   	   		var record=organizSubordinatesGrid.getSelectionModel().getSelection();
				   	   		var ids=[];
				   	   		for(var i=0;i<record.length;i++){
				   	   		ids.push(record[i].get('subId'));
				   	   		}
				   	   		Ext.Msg.confirm("提示","确定要解除选中的组织吗?",function(btn){
				   	   			if (btn == 'yes') {
				   	   			   self.removeOrgSubordinatesNetwork(ids,networkId);
		    				   }
				   	   		})
				   	   }else{
				   	   		Ext.create('Ushine.utils.Msg').onInfo("请至少选择一行数据");
				   	   }
				}
			})
					]}, {
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
	            		        {"text":"组织名称", "value":"organizName"},
	            		        {"text":"组织负责人", "value":"orgHeadOfName"},
	            		        {"text":"网站地址", "value":"websiteURL"},
	            		        {"text":"活动范围", "value":"degreeOfLatitude"},
	            		        {"text":"成立时间", "value":"foundTime"}
	            		    ]
	            		}),
	            		value:'organizName',
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
						width: 300
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
						value:getYearFirstDay()
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
			        	text : '查询组织',
			        	style:"margin-left:20px;",
			        	width:100,
			        	labelWidth: 60,
			        	//height:22,
			        	id : 'search-Button',
			        	baseCls: 't-btn-red',
			        	handler:function(){
			        		//查询
	                        self.findSubordinatesOrganiz(networkId);
		                }
			        }),Ext.create('Ushine.buttons.Button', {
			        	text : '条件重置',
			        	width:100,
			        	style:"margin-left:10px;",
			        	id : 'reset-Button',
			        	baseCls: 't-btn-yellow',
			        	//height:22,
			        	handler:function(){
			        		Ext.getCmp("labl").getForm().reset();
			        	}
			        })]
				}]
				}
		},
		//上级组织gridpanel
		new Ushine.personInfo.NetworkAtHigherLevelOrganizeInfoGridPanel(networkId)
		];	
		this.callParent();		
	},//选择关联人员数据
	selectAssociatedOrganizData:function(networkId){
		//定义一个SaveCluesWin的Window
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
						id:'selectAssociatedOrganizDataId',
						items:new Ushine.cluesInfo.SelectAssociatedOrganizStoreDataGridPanel()
				    }
				];
				this.buttons=[
				      	  	Ext.create('Ushine.buttons.Button', {
				      	   		text: '确定',
				      	   		baseCls: 't-btn-red',
				      	   		handler: function () {
				      	   			var records = Ext.getCmp('SelectAssociatedOrganizStoreDataGridPanelId').getSelectionModel().getSelection();
				      	   			var ids = [];
				      	   			//提取已选择数据的id
				      	   			for(var i = 0; i < records.length; i++){  
				      	   			ids.push(records[i].get("id"));
				      	   			}
				      	   		 //ajax操作关联人员
				      	   		 Ext.Ajax.request({
		    						    url: 'associatedNetworkSubOrganiz.do',
		    						    actionMethods: {
		    				                create : 'POST',
		    				                read   : 'POST', // by default GET
		    				                update : 'POST',
		    				                destroy: 'POST'
		    				            },
		    						    params: {
		    						    	ids:ids,
		    						    	networkId:networkId
		    						    },
		    						    success: function(response){
		    						    	 var text = response.responseText;
		    							     var obj=Ext.JSON.decode(text);
		    							     if(obj.status){
		    							    	 Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
		    							    	 Ext.getCmp('organizeinfomanage-panel').findSubordinatesOrganiz(networkId);
		    							    	 Ext.getCmp('SelectAssociatedOrganizDataWin').close();
		    							     }
		    							     Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
		    						    },
		    				            failure: function(form, action) {
		    				            	Ext.create('Ushine.utils.Msg').onInfo('关联线索组织失败，请联系管理员');
		    				            }
		    						});
				      	   			
				      	   		}
				      	  	
				      	   	})
				      	  ];
				this.callParent();
			}
		});
		Ext.create('SelectAssociatedOrganizDataWin').show();
	},// 查询
	findSubordinatesOrganiz : function(networkId) {
		this.remove('p_organizeinfo_grid');
		this.add(new Ushine.personInfo.NetworkAtHigherLevelOrganizeInfoGridPanel(networkId));
	},
	//解除媒体组织关系
	removeOrgSubordinatesNetwork:function(ids,networkId){
		var self=this;
		Ext.Ajax.request({
		    url: 'removeOrgSubordinatesNetwork.do',
		    method:'POST',
		    params: {
		    	ids: ids
		    },
		    success: function(response, opts){
		       var text = response.responseText;
		       var obj = Ext.JSON.decode(text);
		       if(obj.success){
		    	   Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
		    	   Ext.getCmp('organizeinfomanage-panel').findSubordinatesOrganiz(networkId);
		       }else{
		    	   Ext.Msg.alert('提示',"解除媒体上级组织失败，请联系管理员！");
		       }
		    },
		    failure:function(){
		    	 Ext.Msg.alert('提示',"解除媒体上级失败，请求后台服务失败！");
		    }
		});
	}
});

