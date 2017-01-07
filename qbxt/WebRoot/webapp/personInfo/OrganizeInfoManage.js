/**
 * 组织管理
 */
Ext.define('Ushine.personInfo.OrganizeInfoManage',{
	extend:'Ext.panel.Panel',
	id:'organizeinfomanage-panel',
	bodyStyle: 'background-color: #ffffff; border: none; padding: 10px;',
	layout: {
		type: 'vbox',
		align : 'stretch',
		pack  : 'start'
	},
	title:'重点组织',
	constructor: function() {
		var self = this;
		var date=new Date();
		//第一个月
		date.setMonth(0);
		//第一天
		date.setDate(1);
		var startTime=Ext.Date.format(date,'Y-m-d');
		this.items = [{
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
					items:[
					       Ext.create('Ushine.buttons.IconButton', {
					    	   border: false,
					    	   id: 'createNewBtn',
					    	   btnText: '新增组织', 
					    	   baseCls: 't-btn-red',
					    	   handler: function() {
					    	   	   //弹出新增组织window
					    		   self.saveOrganizeInfo();
					    	   	   //
					    	   }
					       }),
					        Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'updateBtn',
					    	   btnText: '修改组织',
					    	   handler: function () {
					    		   var orgGrid = Ext.getCmp('organizeInfoGridPanel');
					    		   if(orgGrid.getSelectionModel().hasSelection()){
					    			   var res=orgGrid.getSelectionModel().getSelection();
					    			   if(res.length > 1){
					    				   Ext.create('Ushine.utils.Msg').onInfo("对不起，最多可选择一行记录。");
					    			   }else if(res.length == 1){
					    				   self.updateOrganizStore(res);
					    			   }	
					    		   }else{
					    			   Ext.create('Ushine.utils.Msg').onInfo("对不起，请选择至少一行记录。");
					    		   }
					    	   }
					       }),
					       Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'delBtn',
					    	   btnText: '删除组织',
					    	   handler: function () {
					    		   var orgGrid = Ext.getCmp('organizeInfoGridPanel');
					    		   if(orgGrid.getSelectionModel().hasSelection()){
					    			   var res=orgGrid.getSelectionModel().getSelection();
					    			   var ids=[];
					    			   for(var i=0;i<res.length;i++){
					    				   ids.push(res[i].get('id'));
					    			   }
					    			   Ext.Msg.confirm('提示','确定要删除选中的组织库信息吗?',function(btn) {
					    				   if (btn == 'yes') {
					    					   self.delOrganizeInfo(ids);
					    				   	   
					    				   }
					    			   });	
					    		   }else{
					    			   Ext.create('Ushine.utils.Msg').onInfo("对不起，请选择至少一行记录。");
					    		   }
					    	   }
					     }),
					     Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'zhuanClueBtn',
					    	   btnText: '转线索库',
					    	   handler: function () {
					    		   var orgGrid = Ext.getCmp('organizeInfoGridPanel');
					    		   if(orgGrid.getSelectionModel().hasSelection()){
					    			   var res=orgGrid.getSelectionModel().getSelection();
					    			   var orgIds=[];
					    			   for(var i=0;i<res.length;i++){
					    				   orgIds.push(res[i].get('id'));
					    			   }
					    			   //转线索库函数
					    			   self.organizTurnClueStore(orgIds);
					    		   }else{
					    			   Ext.create('Ushine.utils.Msg').onInfo("对不起，请选择至少一行记录。");
					    		   }
					    	   		
					    	   }
					    }),
					   /* Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'updateStatusBtn',
					    	   btnText: '启用组织',
					    	   handler: function () {
					    		   //警告信息
					    		   var rolePanel=Ext.getCmp('organizeInfoGridPanel');
					    		   var records = rolePanel.getSelectionModel().getSelection();
					    		   console.log(records);
					    		   if(records.length <= 0){
					    			   Ext.create('Ushine.utils.Msg').onInfo('请你选择一个组织信息');
					    		   }else{  //1:否      2:是
					    			   var orgIds = [];
					    			   for ( var i = 0; i < records.length; i++) {
					    				   //判断要修改的信息状态是否都是启用的状态，如果是提示无须启用
					    				   if(records[i].data.isEnable=="1"){
					    					   orgIds.push(records[i].data.id);
					    				   }
					    			   }
					    			   if(orgIds.length<=0){
					    				   Ext.create('Ushine.utils.Msg').onInfo('选中的组织都为启用,无须启用');
					    			   }else{
					    				   Ext.Ajax.request({
					    					   url: 'startOrganiz.do',
					    					   actionMethods: {
					    						   create : 'POST',
					    						   read   : 'POST', // by default GET
					    						   update : 'POST',
					    						   destroy: 'POST'
					    					   },
					    					   params: {
					    						   orgIds:orgIds
					    					   },
					    					   success: function(response){
					    						   var text = response.responseText;
					    						   var obj=Ext.JSON.decode(text);
					    						   Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
					    						   //启用成功后刷新数据
					    						   self.findOrganizeByProperty();
					    					   },
					    					   failure: function(form, action) {
					    						   Ext.create('Ushine.utils.Msg').onInfo('启用组织失败，请联系管理员');
					    					   }
					    				   });
					    			   }
					    		   }
					    	   }
					       }),
					       Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'updateCeaseBtn',
					    	   btnText: '禁用组织',
					    	   handler: function () {
					    		   //警告信息
					    		   var rolePanel=Ext.getCmp('organizeInfoGridPanel');
					    		   var records = rolePanel.getSelectionModel().getSelection();
					    		   if(records.length <= 0){
					    			   Ext.create('Ushine.utils.Msg').onInfo('请你选择一个组织信息');
					    		   }else{
					    			   var orgIds = [];
					    			   for ( var i = 0; i < records.length; i++) {
					    				   //判断要修改状态的组织状态是否是禁用，是禁用才修改
					    				   if(records[i].data.isEnable=="2"){
					    					   orgIds.push(records[i].data.id);
					    				   }
					    			   }
					    			   if(orgIds.length<=0){
					    				   Ext.create('Ushine.utils.Msg').onInfo('选中的组织都为禁用,无须禁用');
					    			   }else{
					    				   //试用ajax请求服务器
					    				   Ext.Ajax.request({
					    					   url: 'ceaseOrganiz.do',
					    					   actionMethods: {
					    						   create : 'POST',
					    						   read   : 'POST', // by default GET
					    						   update : 'POST',
					    						   destroy: 'POST'
					    					   },
					    					   //要修改组织的id的集合
					    					   params: {
					    						   orgIds:orgIds
					    					   },
					    					   success: function(response){
					    						   var text = response.responseText;
					    						   var obj=Ext.JSON.decode(text);
					    						   Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
					    						   //禁用成功后刷新数据
					    						   self.findOrganizeByProperty();
					    					   },
					    					   failure: function(form, action) {
					    						   Ext.create('Ushine.utils.Msg').onInfo('启用组织失败，请联系管理员');
					    					   }
					    				   });
					    			   }
					    		   }
					    	   }
					       })*/
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
	            		    	{"text":"全部字段", "value":"anyField"},
	            		        {"text":"组织名称", "value":"organizName"},
	            		        {"text":"组织负责人", "value":"orgHeadOfName"},
	            		        {"text":"网站地址", "value":"websiteURL"},
	            		        {"text":"活动范围", "value":"degreeOfLatitude"},
	            		        {"text":"成立时间", "value":"foundTime"}
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
		                           		//按下enter键
		                        	   self.findOrganizeByProperty();
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
			        	text : '查询组织',
			        	style:"margin-left:20px;",
			        	width:100,
			        	labelWidth: 60,
			        	//height:22,
			        	id : 'search-Button',
			        	baseCls: 't-btn-red',
			        	handler:function(){
			        		//查询
	                        self.findOrganizeByProperty();
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
		//组织gridpanel
		Ext.create('Ushine.personInfo.OrganizeInfoGridPanel')
		];	
		this.callParent();		
	},
	//转线索库
	organizTurnClueStore:function(orgIds){
		Ext.define('SelectClueDataWin',{
			extend:'Ushine.win.Window',
			title : "选择线索数据",
			modal : true,
			id:'SelectClueDataWin',
			layout : {
				type:'vbox',
				align:'stretch'
			},
			border : false,
			closable : true,
			draggable:true,
			resizable : false,
			plain : true,
			padding:10,
			height:550,
			buttonAlign:"center",
			width:600,
			constructor:function(){
				var self=this;
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
	            		        {'text':'任意字段',value:'anyField'}
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
		        	text : '查询',
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
				new Ushine.utils.SelectClueDataGridPanel()]
			}];
				this.buttons=[
					Ext.create('Ushine.buttons.Button', {
					 		text: '确定',
					 		baseCls: 't-btn-red',
					 		handler: function () {
					 			var records = Ext.getCmp('SelectclueDataGridPanelId').getSelectionModel().getSelection();
					 			var clueIds = [];
					 			//提取已选择数据的id
					 			for(var i = 0; i < records.length; i++){  
					 				clueIds.push(records[i].get("id"));
					 			}
					 			var self=Ext.getCmp('SelectclueDataGridPanelId');
					 			var loadMask=new Ext.LoadMask(self,{
									msg:'正在转线索库,请耐心等待……'
								});
								loadMask.show();
						 		Ext.Ajax.request({
								    url: 'basisTurnClueStore.do',
								    actionMethods: {
							            create : 'POST',
							            read   : 'POST', // by default GET
							            update : 'POST',
							            destroy: 'POST'
							        },
								    params: {
								    	dataId:orgIds,
								    	clueIds:clueIds,
								    	store:'organizStore'
								    },
								    //转线索库成功后
								    success: function(response){
								    	loadMask.hide();
								    	 var text = response.responseText;
									     var obj=Ext.JSON.decode(text);
									     if(obj.status){
									    	 Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
									     }
									     Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
									     Ext.getCmp('SelectClueDataWin').close();
								 		 //取消选择
								 		 Ext.getCmp('organizeInfoGridPanel').getStore().reload();
										 Ext.getCmp('organizeInfoGridPanel').getSelectionModel().clearSelections();
								    },
							        failure: function(form, action) {
							        	loadMask.hide();
							        	Ext.getCmp('SelectClueDataWin').close();
							        	Ext.create('Ushine.utils.Msg').onInfo('转线索库失败，请联系管理员');
							        }
							   });
					 		}
					 	})
				];
				this.callParent();
			},
			//查询数据
			findDataByProperty:function(){
				//通过这种方式重新加载数据
				var win=this.getComponent(0);
				win.remove(Ext.getCmp('SelectclueDataGridPanelId'))
				win.add(new Ushine.utils.SelectClueDataGridPanel());
				
			},
		});
		Ext.create('SelectClueDataWin').show();
	},
	//新增组织
	saveOrganizeInfo:function(){
		Ext.create('SaveOrganizeInfoWin',{
			//不属于线索组织
			isClue:'isNotClue',
			clueOrganizNum:-999
		}).show();
	},
	//查询组织
	findOrganizeByProperty:function(){
		this.remove('p_organizeinfo_grid');
		this.add(Ext.create('Ushine.personInfo.OrganizeInfoGridPanel'));
	},
	
	updateOrganizStore:function(res){
		//定义一个SaveOrganizeInfoWin的Window
		Ext.define('UpdateOrganizeInfoWin',{
			extend:'Ushine.win.Window',
			title : "修改组织",
			modal : true,
			id:'updateOrganizInfo_formpanel',
			layout : 'vbox',
			border : false,
			closable : true,
			draggable:true,
			resizable : false,
			plain : true,
			buttonAlign:"center",
			borer:false,
			height:370,
			width:600,
			constructor:function(){
				var self=this;
				this.items=[{
					layout:'vbox',
					border: false,
					xtype:'form',
			        id:'updateOrgForm',
			        items:[{
						//第一行
						layout:'hbox',
				        bodyPadding: 8,
				        border:false,
						buttonAlign:"center",
						items:[{
							fieldLabel:'组织名称',
							labelStyle:'color:red;',
							allowBlank:false,
							xtype : 'textfield',
							emptyText:'请输入组织名称',
							blankText:'此选项不能为空',
							width: 260,
							labelAlign : 'right',
							labelWidth : 70,
							//height:22,
							name : 'organizName',
							value:self.replaceString(res[0].data.organizName)
						},{
							fieldLabel:'成立时间',
							margin:'0 0 0 20',
							labelStyle:'color:red;',
							allowBlank:false,
							xtype : 'datefield',
							format:'Y-m-d',
							value:new Date(),
							maxValue:new Date(),
							labelAlign : 'right',
							emptyText:'请选择日期',
							blankText:'此选项不能为空',
							width: 260,
							labelWidth : 70,
							//height:22,
							name : 'foundTime',
							value:self.replaceString(res[0].data.foundTime)
						}]
					},{
						//第二行
						layout:'hbox',
				        bodyPadding: 8,
				        border: false,
						buttonAlign:"center",
						items:[{
							fieldLabel:'组织类别',
							labelStyle:'color:red;',
							allowBlank:false,
							xtype : 'combo',
							emptyText:'请选择组织类别',
							blankText:'此选项不能为空',
							width: 260,
							labelAlign : 'right',
							labelWidth : 70,
							valueField: 'value',
							id:'organizeType',
							editable : false,
							////height:22,
							//类别应该加载后台数据
							name : 'organizeType',
							 store : new Ext.data.JsonStore({
									proxy: new Ext.data.HttpProxy({
										url : "getInfoTypeByOrganizStore.do"
									}),
									fields:['text', 'value'],
								    autoLoad:true,
								    autoDestroy: true
								}),
							value:self.replaceString(res[0].data.infoTypeId)
						},{
							fieldLabel:'活动范围',
							margin:'0 0 0 20',
							labelStyle:'color:black;',
							allowBlank:true,
							xtype : 'textfield',
							labelAlign : 'right',
							emptyText:'请输入活动范围',
							blankText:'此选项不能为空',
							width: 260,
							labelWidth : 70,
							////height:22,
							name : 'degreeOfLatitude',
							listeners : {
								'focus' : function(thiz,the,eObj) {
									Ext.create('Ushine.utils.WinUtils').cityWin(thiz,the,eObj);
								}
							},
							value:self.replaceString(res[0].data.degreeOfLatitude)
						}]
					},{
						//第三行
						layout:'hbox',
				        bodyPadding: 8,
				        border: false,
						buttonAlign:"center",
						items:[{
							fieldLabel:'组织负责人',
							xtype : 'textfield',
							emptyText:'请输入组织负责人',
							blankText:'此选项不能为空',
							width: 260,
							labelAlign : 'right',
							labelWidth : 70,
							////height:22,
							name : 'orgHeadOfName',
							value:self.replaceString(res[0].data.orgHeadOfName)
						},{
							fieldLabel:'选择负责人',
							margin:'0 0 0 20',
							xtype : 'textfield',
							labelAlign : 'right',
							emptyText:'请选择负责人',
							blankText:'此选项不能为空',
							width: 260,
							labelWidth : 70,
							////height:22,
							name : 'personStore',
							listeners : {
								'focus' : function(thiz,the,eObj) {
									var personIds = Ext.getCmp('personStoreIds');
									self.selectPersonData(thiz,personIds);
								}
							}
						},{
							fieldLabel:'选择负责人',
							margin:'0 0 0 20',
							xtype : 'textfield',
							labelAlign : 'right',
							emptyText:'请选择负责人',
							blankText:'此选项不能为空',
							width: 260,
							labelWidth : 70,
							hidden:true,
							////height:22,
							id:'personStoreIds',
							name : 'personStoreIds'
						}]
					},{
						//第四行
						layout:'hbox',
				        bodyPadding: 8,
				        margin:0,
				        border: false,
						buttonAlign:"center",
						items:[{
							fieldLabel:'主要成员',
							xtype : 'textfield',
							emptyText:'请输入主要成员',
							blankText:'此选项不能为空',
							width: 260,
							labelAlign : 'right',
							labelWidth : 70,
							//height:22,
							name : 'organizPersonNames',
							value:self.replaceString(res[0].data.organizPersonNames)
						},{
							fieldLabel:'下属组织',
							xtype : 'textfield',
							emptyText:'请输入下属组织',
							blankText:'此选项不能为空',
							width: 260,
							margin:'0 0 0 20',
							labelAlign : 'right',
							labelWidth : 70,
							//height:22,
							name : 'organizBranchesNames',
							value:self.replaceString(res[0].data.organizBranchesNames)
						}]
					},{
						//第四行
						layout:'hbox',
				        bodyPadding: 8,
				        margin:0,
				        border: false,
						buttonAlign:"center",
						items:[{
							fieldLabel:'创办刊物',
							xtype : 'textfield',
							emptyText:'请输入创办刊物',
							blankText:'此选项不能为空',
							width: 260,
							labelAlign : 'right',
							labelWidth : 70,
							//height:22,
							name : 'organizPublicActionNames',
							value:self.replaceString(res[0].data.organizPublicActionNames)
						},{
							fieldLabel:'网站地址',
							xtype : 'textfield',
							emptyText:'请输入网站地址',
							blankText:'此选项不能为空',
							width: 260,
							margin:'0 0 0 20',
							labelAlign : 'right',
							labelWidth : 70,
							//height:22,
							name : 'websiteURL',
							value:self.replaceString(res[0].data.websiteURL)
						}]
					},{
						//第六行
				        layout:'hbox',
				        bodyPadding: 8,
				        border: false,
				        margin:0,
						buttonAlign:"center",
						items:[{
							/*fieldLabel:'基本情况',
							xtype : 'htmleditor',
							labelStyle:'color:black;',
							allowBlank:true,
							enableFont : false,
							width: 540,
							labelAlign : 'right',
							emptyText:'请输入组织基本情况',
							labelWidth :70,
							height:120,
							name : 'basicCondition',
							value:res[0].data.basicCondition*/
							xtype : 'displayfield',
							fieldLabel:'基本情况',
							width: 250,
							labelWidth :60,
							labelAlign : 'right',
							value:"<a href='javascript:void(0)' onclick=editOrganizBasicCondition()>"
								+"点击修改组织基本情况<a/>"
						},{
							//隐藏
							xtype : 'hidden',
							name : 'basicCondition',
							value:self.replaceString(res[0].data.basicCondition)
						}]
					  },{
						//活动情况
				        layout:'hbox',
				        bodyPadding: 8,
				        border: false,
				        margin:0,
						buttonAlign:"center",
						items:[{
							/*fieldLabel:'活动情况',
							xtype : 'htmleditor',
							labelStyle:'color:black;',
							enableFont : false,
							allowBlank:true,
							width: 540,
							labelAlign : 'right',
							emptyText:'请输入活动情况',
							labelWidth :70,
							height:120,
							name : 'activityCondition',
							value:res[0].data.activityCondition*/
							xtype : 'displayfield',
							fieldLabel:'活动情况',
							width: 250,
							labelWidth :60,
							labelAlign : 'right',
							value:"<a href='javascript:void(0)' onclick=editOrganizActivityCondition()>"
								+"点击修改组织活动情况<a/>"
						},{
							//隐藏
							xtype : 'hidden',
							name : 'activityCondition',
							value:self.replaceString(res[0].data.activityCondition)
						}]
					  }]
				}]
				
				  this.buttons=[
				  	Ext.create('Ushine.buttons.Button', {
				   		text: '确定',
				   		baseCls: 't-btn-red',
				   		handler: function () {
				   			var form  = Ext.getCmp('updateOrgForm');
				   			//console.log(form.getForm());
				   			//提交给后台处理
				   			if(form.getForm().isValid()){
				   				var loadMask=new Ext.LoadMask(form,{
									msg:'正在修改组织,请耐心等待……'
								});
								loadMask.show();
				   				//取到类别的id
				   				var type = Ext.getCmp('organizeType').displayTplData[0].value;
				   				form.getForm().submit({
				   					url:'updateOrganizStoreById.do',
				   					method:'POST',
				   					params:{
				   						typeId:type,
				   						id:res[0].data.id
				   						},
				   					submitEmptyText:false,
				   					success:function(form,action){
				   						if(action.result.success){
				   							var obj=Ext.JSON.decode(action.response.responseText);
				   							Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				   							Ext.getCmp('updateOrganizInfo_formpanel').close();
				   							Ext.getCmp('organizeInfoGridPanel').getStore().reload();
				   							////取消选择
											Ext.getCmp('organizeInfoGridPanel').getSelectionModel().clearSelections();
				   							
				   						}else{
				   							Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				   						}
				   						loadMask.hide();
				   					},
				   					failure:function(response){
				   						loadMask.hide();
				   						var obj=Ext.JSON.decode(response.responseText);
				   						Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				   						Ext.getCmp('saveOrganizInfo_formpanel').close();
				   					}
				   				});
				   			}
				   		}
				   	}),Ext.create('Ushine.buttons.Button', {
				   		text: '重置',
				   		margin:'0 0 0 35',
				   		baseCls: 't-btn-yellow',
				   		handler: function () {
				   			self.getForm().reset();
				   		}
				   })
				  ];
				this.callParent();
			},
			//字符串替换
			replaceString:function(string){
				var value=string;
				//去除所有的html标签
				/*if(string.indexOf("<font color='orange'/>")>-1&&string.indexOf("</font>")>-1){
					value=string.replace(/<[^>]+>/g,"");
				}*/
				value=string.replace(/<[^>]+>/g,"");
				return value;
			},
			//选择人员数据
			selectPersonData:function(thiz,personIds){
				Ext.define('SelectExistingDataWin',{
					extend:'Ushine.win.Window',
					title : "选择人员数据",
					modal : true,
					id:'SelectExistingDataWin',
					layout : {
						type:'vbox',
						align:'stretch'
					},
					border : false,
					closable : true,
					draggable:true,
					resizable : false,
					plain : true,
					padding:10,
					height:550,
					buttonAlign:"center",
					width:600,
					constructor:function(){
						var self=this;
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
			            		        {'text':'任意字段',value:'anyField'}
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
				        	text : '查询',
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
						new Ushine.personInfo.SelectPersonDataGridPanel()]
					}];
						this.buttons=[
						      	  	Ext.create('Ushine.buttons.Button', {
						      	   		text: '确定',
						      	   		baseCls: 't-btn-red',
						      	   		handler: function () {
						      	   			var records = Ext.getCmp('SelectPersonDataGridPanelId').getSelectionModel().getSelection();
						      	   			var ids = "";
						      	   			var names = [];
						      	   			//提取已选择数据的id
						      	   			for(var i = 0; i < records.length; i++){  
							    			   ids += records[i].get("id")+",";
						      	   			}
						      	   			//提取已选择数据的名称
						      	   		   for(var i = 0; i < records.length; i++){
						      	   			 var value=records[i].get("personName").replace(/<[^>]+>/g,"");
						      	   			   names += value+",";
						      	   			}
						      	   		   if(ids.length>=1){
						      	   			 ids = ids.substring(0,ids.length-1);
						      	   		   }
						      	   		   if(names.length>=1){
						      	   			names = names.substring(0,names.length-1);
						      	   		   }
						      	   		   thiz.setValue(names);
						      	   		   personIds.setValue(ids);
						      	   		   Ext.getCmp('SelectExistingDataWin').close();
						      	   		}
						      	  	
						      	   	})
						];
						this.callParent();
					},
					//查询数据
					findDataByProperty:function(){
						//通过这种方式重新加载数据
						var win=this.getComponent(0);
						win.remove(Ext.getCmp('SelectPersonDataGridPanelId'))
						win.add(new Ushine.personInfo.SelectPersonDataGridPanel());
						
					},
				});
				Ext.create('SelectExistingDataWin').show();
			}
		});
		Ext.create('UpdateOrganizeInfoWin').show();
	},
	//删除组织
	delOrganizeInfo:function(ids){
		 var self=this;
		 var loadMask=new Ext.LoadMask(self,{
			msg:'正在删除组织,请耐心等待……'
		 });
		 loadMask.show();
		 Ext.Ajax.request({
		    url: 'delOrganizStoreById.do',
		    actionMethods: {
	            create : 'POST',
	            read   : 'POST', // by default GET
	            update : 'POST',
	            destroy: 'POST'
	        },
		    params: {
		    	ids:ids
		    },
		    success: function(response){
		    	 loadMask.hide();
		    	 var text = response.responseText;
			     var obj=Ext.JSON.decode(text);
			     if(obj.status){
			    	 Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
			    	 Ext.getCmp('organizeinfomanage-panel').findOrganizeByProperty();
			     }
			     Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
		    },
	        failure: function(form, action) {
	        	loadMask.hide();
	        	Ext.create('Ushine.utils.Msg').onInfo('删除失败，请联系管理员');
	        }
		});
	}
});

//定义一个SaveOrganizeInfoForm保存组织信息表单
Ext.define('SaveOrganizeInfoForm',{
	extend:'Ext.form.Panel',
	
	layout:'vbox',
    bodyPadding: 8,
    margin:0,
    border: false,
	baseCls: 'form-body',
	buttonAlign:"center",
	constructor:function(){
		var self=this;
		this.items=[{
		//第一行
		layout:'hbox',
        bodyPadding: 8,
        border: false,
		buttonAlign:"center",
		items:[{
			fieldLabel:'组织名称',
			labelStyle:'color:red;',
			allowBlank:false,
			xtype : 'textfield',
			emptyText:'请输入组织名称',
			blankText:'此选项不能为空',
			width: 260,
			labelAlign : 'right',
			labelWidth : 70,
			//height:22,
			name : 'organizName'
		},{
			fieldLabel:'成立时间',
			margin:'0 0 0 20',
			labelStyle:'color:red;',
			allowBlank:false,
			xtype : 'datefield',
			format:'Y-m-d',
			value:new Date(),
			maxValue:new Date(),
			labelAlign : 'right',
			emptyText:'请选择日期',
			blankText:'此选项不能为空',
			width: 260,
			labelWidth : 70,
			//height:22,
			name : 'foundTime'
		}]
	},{
		//第二行
		layout:'hbox',
        bodyPadding: 8,
        border: false,
		buttonAlign:"center",
		items:[{
			fieldLabel:'组织类别',
			labelStyle:'color:red;',
			allowBlank:false,
			xtype : 'combo',
			emptyText:'请选择组织类别',
			blankText:'此选项不能为空',
			width: 260,
			labelAlign : 'right',
			labelWidth : 70,
			id:'organizeType',
			////height:22,
			//类别应该加载后台数据
			editable : false,
			name : 'organizeType',
			 store : new Ext.data.JsonStore({
					proxy: new Ext.data.HttpProxy({
						url : "getInfoTypeByOrganizStore.do"
					}),
					fields:['text', 'value'],
				    autoLoad:true,
				    autoDestroy: true
				})
		},{
			fieldLabel:'活动范围',
			margin:'0 0 0 20',
			labelStyle:'color:black;',
			allowBlank:true,
			xtype : 'textfield',
			labelAlign : 'right',
			emptyText:'请输入活动范围',
			blankText:'此选项不能为空',
			width: 260,
			labelWidth : 70,
			////height:22,
			name : 'degreeOfLatitude',
			listeners : {
				'focus' : function(thiz,the,eObj) {
					Ext.create('Ushine.utils.WinUtils').cityWin(thiz,the,eObj);
				}
			}
		}]
	},{
		//第三行
		layout:'hbox',
        bodyPadding: 8,
        border: false,
		buttonAlign:"center",
		items:[{
			fieldLabel:'组织负责人',
			xtype : 'textfield',
			emptyText:'请输入组织负责人',
			blankText:'此选项不能为空',
			width: 260,
			labelAlign : 'right',
			labelWidth : 70,
			////height:22,
			name : 'orgHeadOfName'
		},{
			fieldLabel:'选择负责人',
			margin:'0 0 0 20',
			xtype : 'textfield',
			labelAlign : 'right',
			emptyText:'请选择负责人',
			blankText:'此选项不能为空',
			width: 260,
			labelWidth : 70,
			////height:22,
			name : 'personStore',
			listeners : {
				'focus' : function(thiz,the,eObj) {
					var personIds = Ext.getCmp('personStoreIds');
					self.selectPersonData(thiz,personIds);
				}
			}
		},{
			fieldLabel:'选择负责人',
			margin:'0 0 0 20',
			xtype : 'textfield',
			labelAlign : 'right',
			emptyText:'请选择负责人',
			blankText:'此选项不能为空',
			width: 260,
			labelWidth : 70,
			hidden:true,
			////height:22,
			id:'personStoreIds',
			name : 'personStoreIds'
		}]
	},{
		//第四行
		layout:'hbox',
        bodyPadding: 8,
        margin:0,
        border: false,
		buttonAlign:"center",
		items:[{
			fieldLabel:'主要成员',
			xtype : 'textfield',
			emptyText:'请输入主要成员',
			blankText:'此选项不能为空',
			width: 260,
			labelAlign : 'right',
			labelWidth : 70,
			//height:22,
			name : 'organizPersonNames'
		},{
			fieldLabel:'选择成员',
			margin:'0 0 0 20',
			xtype : 'textfield',
			labelAlign : 'right',
			emptyText:'请选择成员',
			blankText:'此选项不能为空',
			width: 260,
			labelWidth : 70,
			//height:22,
			name : 'organizPersons',
			listeners : {
				'focus' : function(thiz,the,eObj) {
					var personIds = Ext.getCmp('organizPersonIds');
					self.selectPersonData(thiz,personIds);
				}
			}
		},{
			fieldLabel:'选择成员',
			margin:'0 0 0 20',
			xtype : 'textfield',
			labelAlign : 'right',
			emptyText:'请选择成员',
			blankText:'此选项不能为空',
			width: 260,
			labelWidth : 70,
			//height:22,
			hidden:true,
			id:'organizPersonIds',
			name : 'organizPersonIds'
		}]
	},{
		//第四行
		layout:'hbox',
        bodyPadding: 8,
        margin:0,
        border: false,
		buttonAlign:"center",
		items:[{
			fieldLabel:'创办刊物',
			xtype : 'textfield',
			emptyText:'请输入创办刊物',
			blankText:'此选项不能为空',
			width: 260,
			labelAlign : 'right',
			labelWidth : 70,
			//height:22,
			name : 'organizPublicActionNames'
		},{
			fieldLabel:'选择刊物',
			margin:'0 0 0 20',
			xtype : 'textfield',
			labelAlign : 'right',
			emptyText:'请选择刊物',
			blankText:'此选项不能为空',
			width: 260,
			labelWidth : 70,
			name : 'organizPublicActions',
			listeners : {
				'focus' : function(thiz,the,eObj) {
					var personIds = Ext.getCmp('organizPublicActionIds');
					self.selectWebsitejournalData(thiz,personIds);
				}
			}
		},{
			fieldLabel:'选择刊物',
			margin:'0 0 0 20',
			xtype : 'textfield',
			labelAlign : 'right',
			emptyText:'请选择刊物',
			blankText:'此选项不能为空',
			width: 260,
			labelWidth : 70,
			id:'organizPublicActionIds',
			name : 'organizPublicActionIds'
		}]
	},{
		//第四行
		layout:'hbox',
        bodyPadding: 8,
        margin:0,
        border: false,
		buttonAlign:"center",
		items:[{
			fieldLabel:'下属组织',
			xtype : 'textfield',
			emptyText:'请输入下属组织',
			blankText:'此选项不能为空',
			width: 260,
			labelAlign : 'right',
			labelWidth : 70,
			//height:22,
			name : 'organizBranchesNames'
		},{
			fieldLabel:'选择组织',
			margin:'0 0 0 20',
			xtype : 'textfield',
			labelAlign : 'right',
			emptyText:'请选择组织',
			blankText:'此选项不能为空',
			width: 260,
			labelWidth : 70,
			name : 'organizBranches',
			listeners : {
				'focus' : function(thiz,the,eObj) {
					var personIds = Ext.getCmp('organizBrancheIds');
					self.selectOrganizStoreData(thiz,personIds);
				}
			}
		},{
			fieldLabel:'选择组织',
			margin:'0 0 0 20',
			xtype : 'textfield',
			labelAlign : 'right',
			emptyText:'请选择组织',
			blankText:'此选项不能为空',
			width: 260,
			labelWidth : 70,
			id:'organizBrancheIds',
			name : 'organizBrancheIds'
		}]
	},{
		//第四行
		layout:'hbox',
        bodyPadding: 8,
        margin:0,
        border: false,
		buttonAlign:"center",
		items:[{
			fieldLabel:'网站地址',
			xtype : 'textfield',
			emptyText:'请输入网站地址',
			blankText:'此选项不能为空',
			width: 260,
			labelAlign : 'right',
			labelWidth : 70,
			//height:22,
			name : 'websiteURL'
		}]
	},{
		//第六行
        layout:'hbox',
        bodyPadding: 8,
        border: false,
        margin:0,
		buttonAlign:"center",
		items:[{
			fieldLabel:'基本情况',
			xtype : 'htmleditor',
			labelStyle:'color:black;',
			allowBlank:true,
			width: 540,
			labelAlign : 'right',
			emptyText:'请输入组织基本情况',
			labelWidth :70,
			enableFont : false,
			height:120,
			name : 'basicCondition'
		}]
	  },{
		//活动情况
        layout:'hbox',
        bodyPadding: 8,
        border: false,
        margin:0,
		buttonAlign:"center",
		items:[{
			fieldLabel:'活动情况',
			xtype : 'htmleditor',
			labelStyle:'color:black;',
			allowBlank:true,
			enableFont : false,
			width: 540,
			labelAlign : 'right',
			emptyText:'请输入活动情况',
			labelWidth :70,
			height:120,
			name : 'activityCondition'
		}]
	  }];
	  this.buttons=[
	  	Ext.create('Ushine.buttons.Button', {
	   		text: '确定',
	   		baseCls: 't-btn-red',
	   		handler: function () {
	   			//提交给后台处理
	   			var win=self.up('window');
	   			var isClue=win.isClue;
	   			var state=win.state;
	   			var clueOrganizNum=win.clueOrganizNum;
	   			//console.log(clueOrganizNum);
	   			if(self.getForm().isValid()){
	   			//先判断是否存在
	   				Ext.Ajax.timeout=60000;
	   				Ext.Ajax.request({
	   					//先判断是否有了
	   					url:"hasStoreByOrganizName.do",
	   					method:'post',
	   					params:{
	   						organizName:self.getForm().findField('organizName').getValue()
	   					},
	   					success:function(response){
	   						var obj=Ext.JSON.decode(response.responseText);
	   						//console.log(obj);
	   						if(obj.msg=='exist'){
	   							Ext.create('Ushine.utils.Msg').onQuest("组织已经存在，是否仍添加",function(btn){
	   								//console.log(btn);
	   								if(btn=='yes'){
	   									//仍添加
	   									self.saveOrganizStore(self,isClue,clueOrganizNum,state);
	   								}
	   							});
	   						}else{
	   							//不存在
	   							self.saveOrganizStore(self,isClue,clueOrganizNum,state);
	   						}
	   					},
	   					failure:function(){
	   						Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败");
	   					}
	   				})
	   			}
	   		}
	   	}),Ext.create('Ushine.buttons.Button', {
	   		text: '重置',
	   		margin:'0 0 0 35',
	   		baseCls: 't-btn-yellow',
	   		handler: function () {
	   			self.getForm().reset();
	   		}
	   })
	  ];
	  this.callParent();
	},
	//添加组织
	saveOrganizStore:function(self,isClue,clueOrganizNum,state){
		var loadMask=new Ext.LoadMask(self,{
			msg:'正在新增组织,请耐心等待……'
		});
		loadMask.show();
		//取到类别的id
		var type = Ext.getCmp('organizeType').displayTplData[0].value;
		self.getForm().submit({
			url:'saveOrganizStore.do',
			method:'POST',
			params:{
				typeId:type,
				isClue:isClue,
				clueOrganizNum:clueOrganizNum,
				state:state
			},
			submitEmptyText:false,
			success:function(form,action){
				loadMask.hide();
				if(action.result.success){
					var obj=Ext.JSON.decode(action.response.responseText);
					Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
					//关闭
					Ext.getCmp('saveOrganizInfo_formpanel').close();
					if(Ext.getCmp('organizeInfoGridPanel')){
					//刷新数据   								
						Ext.getCmp('organizeInfoGridPanel').getStore().reload();
					}
					var CluesTempDataGrid = Ext.getCmp('CluesTempDataGridId');
                 if(CluesTempDataGrid){
                 	 //新增线索组织后刷新数据
			    	 CluesTempDataGrid.removeAll();
			    	 CluesTempDataGrid.add(new Ushine.utils.CluesTempDataGridPanel(clueOrganizNum));
                 }
				}else{
					Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				}
			},
			failure:function(response){
				loadMask.hide();
				var obj=Ext.JSON.decode(response.responseText);
				Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				Ext.getCmp('saveOrganizInfo_formpanel').close();
			}
		});
	},
	//选择人员数据
	selectPersonData:function(thiz,personIds){
		Ext.define('SelectExistingDataWin',{
			extend:'Ushine.win.Window',
			title : "选择人员数据",
			modal : true,
			id:'SelectExistingDataWin',
			layout : {
				type:'vbox',
				align:'stretch'
			},
			border : false,
			closable : true,
			draggable:true,
			resizable : false,
			plain : true,
			padding:10,
			height:550,
			buttonAlign:"center",
			width:600,
			constructor:function(){
				var self=this;
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
	            		        {'text':'任意字段',value:'anyField'}
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
		        	text : '查询',
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
				new Ushine.personInfo.SelectPersonDataGridPanel()]
			}];
				this.buttons=[
				      	  	Ext.create('Ushine.buttons.Button', {
				      	   		text: '确定',
				      	   		baseCls: 't-btn-red',
				      	   		handler: function () {
				      	   			var records = Ext.getCmp('SelectPersonDataGridPanelId').getSelectionModel().getSelection();
				      	   			var ids = "";
				      	   			var names = [];
				      	   			//提取已选择数据的id
				      	   			for(var i = 0; i < records.length; i++){  
					    			   ids += records[i].get("id")+",";
				      	   			}
				      	   			//提取已选择数据的名称
				      	   		   for(var i = 0; i < records.length; i++){
				      	   			 var value=records[i].get("personName").replace(/<[^>]+>/g,"");
				      	   			   names += value+",";
				      	   			}
				      	   		   if(ids.length>=1){
				      	   			 ids = ids.substring(0,ids.length-1);
				      	   		   }
				      	   		   if(names.length>=1){
				      	   			names = names.substring(0,names.length-1);
				      	   		   }
				      	   		   thiz.setValue(names);
				      	   		   personIds.setValue(ids);
				      	   		   Ext.getCmp('SelectExistingDataWin').close();
				      	   		}
				      	  	
				      	   	})
				];
				this.callParent();
			},
			//查询数据
			findDataByProperty:function(){
				//通过这种方式重新加载数据
				var win=this.getComponent(0);
				win.remove(Ext.getCmp('SelectPersonDataGridPanelId'))
				win.add(new Ushine.personInfo.SelectPersonDataGridPanel());
				
			},
		});
		Ext.create('SelectExistingDataWin').show();
	},
	//选择刊物数据
	selectWebsitejournalData:function(thiz,personIds){
		Ext.define('SelectExistingDataWin',{
			extend:'Ushine.win.Window',
			title : "选择媒体刊物",
			modal : true,
			id:'SelectExistingDataWin',
			layout : {
				type:'vbox',
				align:'stretch'
			},
			border : false,
			closable : true,
			draggable:true,
			resizable : false,
			plain : true,
			padding:10,
			height:550,
			buttonAlign:"center",
			width:600,
			constructor:function(){
				var self=this;
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
	            		        {'text':'任意字段',value:'anyField'}
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
		        	text : '查询',
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
				new Ushine.personInfo.SelectWebsiteJournalDataGridPanel()]
			}];
				this.buttons=[
				      	  	Ext.create('Ushine.buttons.Button', {
				      	   		text: '确定',
				      	   		baseCls: 't-btn-red',
				      	   		handler: function () {
				      	   			var records = Ext.getCmp('SelectWebsiteJournalDataGridPanelId').getSelectionModel().getSelection();
				      	   			var ids = "";
				      	   			var names = [];
				      	   			//提取已选择数据的id
				      	   			for(var i = 0; i < records.length; i++){  
					    			   ids += records[i].get("id")+",";
				      	   			}
				      	   			//提取已选择数据的名称
				      	   		   for(var i = 0; i < records.length; i++){
				      	   			 var value=records[i].get("name").replace(/<[^>]+>/g,"");
				      	   			   names += value+",";
				      	   			}
				      	   		   if(ids.length>=1){
				      	   			 ids = ids.substring(0,ids.length-1);
				      	   		   }
				      	   		   if(names.length>=1){
				      	   			names = names.substring(0,names.length-1);
				      	   		   }
				      	   		   thiz.setValue(names);
				      	   		   personIds.setValue(ids);
				      	   		   Ext.getCmp('SelectExistingDataWin').close();
				      	   		}
				      	  	
				      	   	})
				];
				this.callParent();
			},
			//查询数据
			findDataByProperty:function(){
				//通过这种方式重新加载数据
				var win=this.getComponent(0);
				win.remove(Ext.getCmp('SelectWebsiteJournalDataGridPanelId'))
				win.add(new Ushine.personInfo.SelectWebsiteJournalDataGridPanel());
			},
		});
		Ext.create('SelectExistingDataWin').show();
	},
	//选择下属组织
	selectOrganizStoreData:function(thiz,personIds){
		Ext.define('SelectExistingDataWin',{
			extend:'Ushine.win.Window',
			title : "选择下属组织",
			modal : true,
			id:'SelectExistingDataWin',
			layout : {
				type:'vbox',
				align:'stretch'
			},
			border : false,
			closable : true,
			draggable:true,
			resizable : false,
			plain : true,
			padding:10,
			height:550,
			buttonAlign:"center",
			width:600,
			constructor:function(){
				var self=this;
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
	            		        {'text':'任意字段',value:'anyField'}
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
		        	text : '查询',
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
				new Ushine.personInfo.SelectOrganizStoreDataGridPanel()]
			}];
				this.buttons=[
				      	  	Ext.create('Ushine.buttons.Button', {
				      	   		text: '确定',
				      	   		baseCls: 't-btn-red',
				      	   		handler: function () {
				      	   			var records = Ext.getCmp('SelectOrganizStoreDataGridPanelId').getSelectionModel().getSelection();
				      	   			var ids = "";
				      	   			var names = [];
				      	   			//提取已选择数据的id
				      	   			for(var i = 0; i < records.length; i++){  
					    			   ids += records[i].get("id")+",";
				      	   			}
				      	   			//提取已选择数据的名称
				      	   		   for(var i = 0; i < records.length; i++){
				      	   			 var value=records[i].get("organizName").replace(/<[^>]+>/g,"");
				      	   			   names += value+",";
				      	   			}
				      	   		   if(ids.length>=1){
				      	   			 ids = ids.substring(0,ids.length-1);
				      	   		   }
				      	   		   if(names.length>=1){
				      	   			names = names.substring(0,names.length-1);
				      	   		   }
				      	   		   thiz.setValue(names);
				      	   		   personIds.setValue(ids);
				      	   		   Ext.getCmp('SelectExistingDataWin').close();
				      	   		}
				      	  	
				      	   	})
				];
				this.callParent();
			},
			//查询数据
			findDataByProperty:function(){
				//通过这种方式重新加载数据
				var win=this.getComponent(0);
				win.remove(Ext.getCmp('SelectOrganizStoreDataGridPanelId'))
				win.add(new Ushine.personInfo.SelectOrganizStoreDataGridPanel());
			},
		});
		Ext.create('SelectExistingDataWin').show();
	}
});

//定义一个SaveOrganizeInfoWin的Window
Ext.define('SaveOrganizeInfoWin',{
	extend:'Ushine.win.Window',
	title : "添加组织",
	modal : true,
	id:'saveOrganizInfo_formpanel',
	layout : 'fit',
	border : false,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:660,
	width:590,
	constructor:function(config){
		var self=this;
		//是否是线索组织
		this.isClue=config.isClue;
		//线索组织的临时编号
		this.clueOrganizNum=config.clueOrganizNum;
		//状态是否关联组织
		this.state=config.state;
		this.items=[
			Ext.create('SaveOrganizeInfoForm')
		];
		this.callParent();
	}
});
//修改组织基本情况
function editOrganizBasicCondition(){
	var form=Ext.getCmp('updateOrgForm').getForm();
	//得到表单
	var field=form.findField('basicCondition');
	var value=form.findField('basicCondition').getValue();
	Ext.define('EditOrganizBasicConditionWin',{
		height:800,
		width:1000,
		extend:'Ext.window.Window',
		layout : 'fit',
		border : false,
		buttonAlign:"center",
		title:'修改组织基本情况',
		constructor:function(){
			var self=this;
			this.items=[{
				xtype:'htmleditor',
				value:value,
				enableFont:false
			}];
			this.buttons=[
				Ext.create('Ushine.buttons.Button', {
	      			text: '确定',
	      	   		baseCls: 't-btn-red',
	      	   		handler:function(){
	      	   			//修改
	      	   			field.setValue(self.getComponent(0).getValue());
	      	   			//关闭
	      	   			self.close();
	      	   		}
	      	   	}),Ext.create('Ushine.buttons.Button', {
	      			text: '重置',
	      			margin:'0 0 0 35',
	   				baseCls: 't-btn-yellow',
	   				handler:function(){
   						//重置
   						self.getComponent(0).setValue(value);
	   				}
	   			})
			];
			this.callParent();
		}
	});
	Ext.create('EditOrganizBasicConditionWin').show();
}

//修改组织活动情况
function editOrganizActivityCondition(){
	var form=Ext.getCmp('updateOrgForm').getForm();
	//得到表单
	var field=form.findField('activityCondition');
	var value=form.findField('activityCondition').getValue();
	console.log(field);
	Ext.define('EditOrganizActivityConditionWin',{
		height:800,
		width:1000,
		extend:'Ext.window.Window',
		layout : 'fit',
		border : false,
		buttonAlign:"center",
		title:'修改组织活动情况',
		constructor:function(){
			var self=this;
			this.items=[{
				xtype:'htmleditor',
				value:value,
				enableFont:false
			}];
			this.buttons=[
				Ext.create('Ushine.buttons.Button', {
	      			text: '确定',
	      	   		baseCls: 't-btn-red',
	      	   		handler:function(){
	      	   			//修改
	      	   			field.setValue(self.getComponent(0).getValue());
	      	   			//关闭
	      	   			self.close();
	      	   		}
	      	   	}),Ext.create('Ushine.buttons.Button', {
	      			text: '重置',
	      			margin:'0 0 0 35',
	   				baseCls: 't-btn-yellow',
	   				handler:function(){
   						//重置
   						self.getComponent(0).setValue(value);
	   				}
	   			})
			];
			this.callParent();
		}
	});
	Ext.create('EditOrganizActivityConditionWin').show();
}
