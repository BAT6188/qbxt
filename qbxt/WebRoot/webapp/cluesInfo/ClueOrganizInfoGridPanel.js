/**
 * 线索组织gridPanelgridpanel
 */
Ext.define('Ushine.cluesInfo.ClueOrganizInfoGridPanel',{
	extend:'Ext.grid.Panel',
	id:'clueOrganizGrid',
	itemId:'d_clueOraniz_grid',
	height:300,
	flex:1,
	stripeRows:false,//true为隔行换颜色
	autoHeight:true,
	disableSelection:false,//是否禁止行选择
	columnLines:true, //列的框线样式
	loadMask:true,
	//selType:'checkboxmodel',//复选框
	viewConfig:{
		emptyText:'没有数据',
		stripeRows:true,
		enableTextSelection:true
	},
	constructor:function(clueId){
		var self=this;
		var store=Ext.create('Ext.data.JsonStore',{
			pageSize:50,
			model:'clueOrganizModel',
			remoteStore:true,
			//请求后台服务
			proxy:{
				type:'ajax',
				url:'findClueOrganiz.do?clueId='+clueId,
				simpleSortMode:true,
				reader:{
	                type: 'json',
	                root: 'datas',
					totalProperty:'paging.totalRecord'
				}
			},
			listeners: {
				//页面加载事件
				'beforeload':function(thiz, options) {
					var startTime = Ext.getCmp("startTime").value;  //开始时间
	                var endTime = Ext.getCmp("endTime").value;  //结束时间
	                startTime = Ext.Date.format(startTime, 'Y-m-d H:i:s');
	                endTime = Ext.Date.format(endTime, 'Y-m-d H:i:s');
	                var field = Ext.getCmp('field').getValue();
	                var fieldValue = Ext.getCmp('fieldValue').getValue();
					//设置查询参数
					if(!options.params) options.params = {};
					options.params.field=field;
					options.params.fieldValue=fieldValue;
					options.params.startTime=startTime;
					options.params.endTime=endTime;
				}
			},
			autoLoad:true
		});
		
		this.columns=[
			{text:'ID',dataIndex:'id',flex: 1,menuDisabled:true,hidden:true},
			{text: '所属线索',  dataIndex: 'clueName',sortable: false,flex: 1,menuDisabled:true},
			{text: '是否入库',  dataIndex: 'isToStorage',sortable: false,menuDisabled:true,width:80,renderer:function(value){
	          	  if(value=="1"){
	          		   return "已入库";
	          	   }else{
	          		   return "未入库";
	          	   }
		    }},
			{text: '组织名称',  dataIndex: 'organizName',sortable: false,flex: 1,menuDisabled:true},
		    {text: '组织负责人',  dataIndex: 'orgHeadOfName',sortable: false,flex: 1,menuDisabled:true},
		    {text: '组织类别',  dataIndex: 'infoType',sortable: false,flex: 1,menuDisabled:true},
		    {text: '网站地址',  dataIndex: 'websiteURL',sortable: false,flex: 1 ,menuDisabled:true},
		    {text: '创办刊物',  dataIndex: 'organizPublicActionNames',sortable: false,flex: 1,menuDisabled:true},
		    {text: '分支机构',  dataIndex: 'organizBranchesNames',sortable: false,flex: 1,menuDisabled:true},
		    {text: '主要成员',  dataIndex: 'organizPersonNames',sortable: false,flex: 1,menuDisabled:true},
		    {text: '成立时间',  dataIndex: 'foundTime',width:160,menuDisabled:true,renderer:function(value){
	         	   return Ext.Date.format(value, 'Y-m-d');
	        }},
	        {text: '数据创建时间',  dataIndex: 'createDate',width:160,menuDisabled:true,renderer:function(value){
	         	   return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }},
		    {text: '活动范围',  dataIndex: 'degreeOfLatitude',sortable: false,flex: 1,menuDisabled:true},
		    {text: '基本情况',  hidden:true,dataIndex: 'basicCondition',sortable: false,flex: 1,menuDisabled:true},
		    {text: '活动情况',  hidden:true,dataIndex: 'activityCondition',sortable: false,flex: 1,menuDisabled:true},
		    {text: '操作',sortable: true,dataIndex: 'isToStorage',align:'center',width:100,xtype:'actioncolumn',
		    	items:[{
		            icon: 'images/user-red.png',
		            tooltip: '数据入库',
		            style:'margin-left:20px;',
		            handler: function(grid, rowIndex, colIndex){
		            	var data = self.store.getAt(rowIndex).data;   //选中一行的数据
		            	if(data.isToStorage=='2'){
		            		//入库实际也是修改操作
		            		self.dataStorageOrganizStore(data);
		            	}
		           }
	             }],
	             menuDisabled:true,
	             renderer:function(value){
	            	 var self = this;
            		 //显示线索涉及对象人员图标
            		 if(value=='2'){
            			self.items[0].icon ="images/block--plus.png";
            		 }else{
            			 self.items[0].icon ="images/block--plus1.png";
            		 }
		        }
	    }];
	     this.selModel=new Ext.selection.CheckboxModel({
			//点击复选框才会选中,防止其他行被取消
			checkOnly:true
		});
		this.listeners={
			//单击选中
			itemclick:function(thiz, record, item, index, e, eOpts){
				var model=self.getSelectionModel();
				if(model.isSelected(record)){
					model.deselect(record);
				}else{
					var records=model.getSelection();
					records.push(record);
					model.select(records);
					records=[];
				}
			},
			//双击事件
			itemdblclick:function(thiz, record, item, index, e, eOpts){
				//详细信息
				Ext.create('ServiceOrganizDetailWin',{
					record:record
				}).show();
			}
		};
		this.loadMask =true;
		//创建面板底部的工具条
		this.bbar = new Ext.PagingToolbar({
			store : store,  		//数据源
			displayInfo: true,   		//是否显示信息(true表示显示信息)
			firstText: '首页', 	 		//第一页文本 显示第一页按钮的快捷提示文本
			lastText: '末页', 	 		//最后一页的文本 显示最后一页按钮快捷提示文本
			prevText: '上一页',   		//上一个导航按钮工具条
			nextText: '下一页',  			//下一个导航按钮工具条
			refreshText: '刷新',			//刷新的文本 显示刷新按钮的快捷提示文本
			beforePageText: '当前第', 	//输入项前的文字 输入项前的文本显示。
			afterPageText: '/{0}页', 	//输入项后的文字 可定制的默认输入项后的文字
			displayMsg: '本页显示第{0}条到第{1}条, 共有{2}条记录', //显示消息 显示分页状态的消息
			emptyMsg: '没有查找记录'  		//空消息 没有找到记录时，显示该消息
		});
		this.store = store;
		this.callParent(); 
	},
	dataStorageOrganizStore:function(res){
		//定义一个SaveOrganizeInfoWin的Window
		Ext.define('DataStorageOrganizeInfoWin',{
			extend:'Ushine.win.Window',
			title : "数据入库",
			modal : true,
			id:'dataStorageOrganizInfo_formpanel',
			layout : 'vbox',
			border : false,
			closable : true,
			draggable:true,
			resizable : false,
			plain : true,
			buttonAlign:"center",
			borer:false,
			height:660,
			width:590,
			constructor:function(){
				var self=this;
				this.items=[{
				         xtype:'form',
					     id:'dataStorageOrganizForm',
					     items:[{
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
									name : 'organizName',
									value:res.organizName
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
									labelStyle:'color:red;',
									allowBlank:false,
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
							  }]
				}
				     ]
				
				  this.buttons=[
				  	Ext.create('Ushine.buttons.Button', {
				   		text: '确定',
				   		baseCls: 't-btn-red',
				   		handler: function () {
				   			var form  = Ext.getCmp('dataStorageOrganizForm');
				   			//console.log(form.getForm());
				   			//提交给后台处理
				   			if(form.getForm().isValid()){
				   				//取到类别的id
				   				var type = Ext.getCmp('organizeType').displayTplData[0].value;
				   				form.getForm().submit({
				   					url:'dataStorageOrganizStoreById.do',
				   					method:'POST',
				   					params:{
				   						typeId:type,
				   						id:res.id
				   						},
				   					submitEmptyText:false,
				   					success:function(form,action){
				   						if(action.result.success){
				   							var obj=Ext.JSON.decode(action.response.responseText);
				   							Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				   							Ext.getCmp('dataStorageOrganizInfo_formpanel').close();
				   							Ext.getCmp('clueOrganizGrid').getStore().reload();
				   							////取消选择
											Ext.getCmp('clueOrganizGrid').getSelectionModel().clearSelections();
				   							
				   						}else{
				   							Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				   						}
				   					},
				   					failure:function(response){
				   						var obj=Ext.JSON.decode(response.responseText);
				   						Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				   						Ext.getCmp('dataStorageOrganizInfo_formpanel').close();
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
			//选择人员数据
			selectPersonData:function(thiz,personIds){
				//定义一个SaveCluesWin的Window
				Ext.define('SelectPersonDataWin',{
					extend:'Ushine.win.Window',
					title : "选择人员数据",
					modal : true,
					id:'SelectPersonDataWin',
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
								id:'selectPersonDataId',
								items:new Ushine.personInfo.SelectPersonDataGridPanel()
						    }
						];
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
						      	   			   names += records[i].get("personName")+",";
						      	   			}
						      	   		   if(ids.length>=1){
						      	   			 ids = ids.substring(0,ids.length-1);
						      	   		   }
						      	   		   if(names.length>=1){
						      	   			names = names.substring(0,names.length-1);
						      	   		   }
						      	   		   thiz.setValue(names);
						      	   		   personIds.setValue(ids);
						      	   		   Ext.getCmp('SelectPersonDataWin').close();
						      	   		}
						      	  	
						      	   	})
						      	  ];
						this.callParent();
					}
				});
				Ext.create('SelectPersonDataWin').show();
			},//选择下属组织
			selectOrganizStoreData:function(thiz,personIds){
				//定义一个SaveCluesWin的Window
				Ext.define('SelectOrganizStoreDataWin',{
					extend:'Ushine.win.Window',
					title : "选择下属组织数据",
					modal : true,
					id:'SelectOrganizStoreDataWin',
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
								id:'selectOrganizStoreDataId',
								items:new Ushine.personInfo.SelectOrganizStoreDataGridPanel()
						    }
						];
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
						      	   			   names += records[i].get("organizName")+",";
						      	   			}
						      	   		   if(ids.length>=1){
						      	   			 ids = ids.substring(0,ids.length-1);
						      	   		   }
						      	   		   if(names.length>=1){
						      	   			names = names.substring(0,names.length-1);
						      	   		   }
						      	   		   thiz.setValue(names);
						      	   		   personIds.setValue(ids);
						      	   		   Ext.getCmp('SelectOrganizStoreDataWin').close();
						      	   		}
						      	  	
						      	   	})
						      	  ];
						this.callParent();
					}
				});
				Ext.create('SelectOrganizStoreDataWin').show();
			},//选择刊物数据
			selectWebsitejournalData:function(thiz,personIds){
				//定义一个SaveCluesWin的Window
				Ext.define('SelectWebsoteJournalDataWin',{
					extend:'Ushine.win.Window',
					title : "选择媒体刊物数据",
					modal : true,
					id:'SelectWebsoteJournalDataWin',
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
								id:'selectWebsiteJournalDataId',
								items:new Ushine.personInfo.SelectWebsiteJournalDataGridPanel()
						    }
						];
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
						      	   			   names += records[i].get("name")+",";
						      	   			}
						      	   		   if(ids.length>=1){
						      	   			 ids = ids.substring(0,ids.length-1);
						      	   		   }
						      	   		   if(names.length>=1){
						      	   			names = names.substring(0,names.length-1);
						      	   		   }
						      	   		   thiz.setValue(names);
						      	   		   personIds.setValue(ids);
						      	   		   Ext.getCmp('SelectWebsoteJournalDataWin').close();
						      	   		}
						      	  	
						      	   	})
						      	  ];
						this.callParent();
					}
				});
				Ext.create('SelectWebsoteJournalDataWin').show();
			}
		});
		Ext.create('DataStorageOrganizeInfoWin').show();
	}
});
//数据模型
Ext.define('clueOrganizModel', {
	extend: 'Ext.data.Model',
    fields: [
             {name: 'id', type:'string', mapping: 'id'},
             {name: 'organizName', type:'string', mapping: 'organizName'},
             {name: 'orgHeadOfName', type:'string', mapping: 'orgHeadOfName'},
             {name: 'infoType', type:'string', mapping: 'infoType'},
             {name: 'clueName', type:'string', mapping: 'clueName'},
             {name: 'websiteURL', type:'string', mapping: 'websiteURL'},
             {name: 'organizPublicActionNames', type:'String', mapping: 'organizPublicActionNames'},
             {name: 'organizBranchesNames', type:'string', mapping: 'organizBranchesNames'},
             {name: 'organizPersonNames', type:'string', mapping: 'organizPersonNames'},
             {name: 'foundTime', type:'date', mapping: 'foundTime'},
             {name: 'degreeOfLatitude', type:'string', mapping: 'degreeOfLatitude'},
             {name: 'basicCondition', type:'string', mapping: 'basicCondition'},
             {name: 'activityCondition', type:'string', mapping: 'activityCondition'},
             {name: 'createDate', type:'date', mapping: 'createDate'},
             {name: 'isToStorage', type:'string', mapping: 'isToStorage'}
             ],
    idProperty:'id'
});
