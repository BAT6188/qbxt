/**
 * 人员GridPanel
 */
Ext.define('Ushine.cluesInfo.CluePersonInfoGridPanel', {
	extend: 'Ext.grid.Panel',
	id:"cluePersonInfoGridPanel",
	flex: 1,
	itemId:'p_cluePersoninfo_grid',
	height:200,
	stripeRows:false,		    //True来实现隔行换颜色
	autoHeight : true,
	disableSelection: false,   //是否禁止行选择，默认false
	columnLines:true,		   //添加列的框线样式
    loadMask: true,           //是否在加载数据时显示遮罩效果，默认为false
	//selType: 'checkboxmodel', // 复选框
	defaults: {sortable: false},
	columns:[],
	viewConfig:{
		emptyText: '没有数据',
    	stripeRows:true,//在表格中显示斑马线
    	enableTextSelection:true //可以复制单元格文字
	},
	constructor: function(clueId) {
		var self = this;
		var store = Ext.create('Ext.data.JsonStore',{
			//这里控制每页显示的数量
			pageSize:50,
			model:'cluePersonInfoModel',
			remoteSort:true,
			proxy:{
				type:'ajax',
				url:'findCluePersonStore.do?clueId='+clueId,
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
		    {text: '人员类别',  dataIndex: 'infoType',sortable: false,flex: 1,menuDisabled:true},
		    {text: '所属线索',  dataIndex: 'clueName',sortable: false,flex: 1,menuDisabled:true},
		    {text: '是否入库',  dataIndex: 'isToStorage',sortable: false,menuDisabled:true,width:80,renderer:function(value){
	          	  if(value=="1"){
	          		   return "已入库";
	          	   }else{
	          		   return "未入库";
	          	   }
		    }},
		    {text: '姓名',  dataIndex: 'personName',sortable: false,flex: 1,menuDisabled:true},
		    {text: '曾用名',  dataIndex: 'nameUsedBefore',sortable: false,flex: 1,menuDisabled:true},
		    {text: '英文名',  dataIndex: 'englishName',sortable: false,flex: 1 ,menuDisabled:true},
		    {text: '性别',  dataIndex: 'sex',sortable: false,flex: 1,menuDisabled:true},
		    {text: '出生日期',  xtype: 'datecolumn',   format:'Y-m-d', dataIndex: 'bebornTime',sortable: false,flex: 1,menuDisabled:true},
		    {text: '现住地址',  dataIndex: 'presentAddress',sortable: false,flex: 1,menuDisabled:true},
		    {text: '工作单位',  dataIndex: 'workUnit',sortable: false,flex: 1,menuDisabled:true},
		    {text: '户籍地址',  dataIndex: 'registerAddress',sortable: false,flex: 1,menuDisabled:true},
		    {text: '数据创建时间',  dataIndex: 'createDate',width:160,menuDisabled:true,renderer:function(value){
	         	   return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }},
		    {text: '履历', hidden:true, dataIndex: 'antecedents',sortable: false,flex: 1,menuDisabled:true},
		    {text: '活动情况', hidden:true, dataIndex: 'activityCondition',sortable: false,flex: 1,menuDisabled:true},
		    {text: '操作',sortable: true,dataIndex: 'isToStorage',align:'center',width:100,xtype:'actioncolumn',
		    	items:[{
		            icon: 'images/user-red.png',
		            tooltip: '数据入库',
		            style:'margin-left:20px;',
		            handler: function(grid, rowIndex, colIndex){
		            	var data = self.store.getAt(rowIndex).data;   //选中一行的数据
		            	//console.log(data);
		            	if(data.isToStorage=='2'){
		            		//入库实际也是修改操作
		            		self.dataStoragePersonStore(data);
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
				//console.log(record.get('infoType'));
				Ext.create('PersonInfoDetailWin',{
					record:record
				}).show();
			}
		};
		this.loadMask =true;
		//创建面板底部的工具条
		this.bbar = new Ext.PagingToolbar({//一个要和Ext.data.Store参与绑定并且自动提供翻页控制的工具栏
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
	//入库操作
	dataStoragePersonStore:function(res){
		//定义一个SaveOrganizeInfoWin的Window
		Ext.define('DataStoragePersonInfoWin',{
			extend:'Ushine.win.Window',
			title : "数据入库",
			modal : true,
			id:'dataStoragePersonInfo_formpanel',
			layout : 'fit',
			bodyPadding: 10,
			border : false,
			closable : true,
			draggable:true,
			resizable : false,
			plain : true,
			buttonAlign:"center",
			borer:false,
			height:780,
			width:860,
			constructor:function(){
				var self=this;
				var sjnum=parseInt(Math.random()*1000);
				this.items=[{
				         xtype:'form',
					     id:'dataStoragePersonForm',
					     border:false,
					     items:[{
								//基本信息
								xtype:'fieldset',
								border:true,
								layout:'vbox',
								title:'基本信息',
								items:[{
									//第一行姓名，曾用名，英文名
									 layout:'hbox',
							         bodyPadding: 8,
							         border: false,
									 buttonAlign:"center",
									 items:[{
									 	xtype:'hiddenfield',
									 	//隐藏域
									 	name:'id'
									 },{
									 	fieldLabel:'姓名',
										labelStyle:'color:red;',
										allowBlank:false,
										xtype : 'textfield',
										emptyText:'请输入姓名',
										blankText:'此选项不能为空',
										width: 250,
										labelAlign : 'right',
										labelWidth : 60,
										height:22,
										name : 'personName',
										id : 'personName',
										value:res.personName
									 },{
									 	fieldLabel:'曾用名',
										margin:'0 0 0 20',
										//允许为空
										labelStyle:'color:black;',
										//allowBlank:false,
										xtype : 'textfield',
										labelAlign : 'right',
										emptyText:'请输入曾用名',
										width: 250,
										labelWidth : 60,
										height:22,
										id : 'nameUsedBefore',
										name : 'nameUsedBefore'
									 },{
									 	fieldLabel:'英文名',
										//允许为空
										margin:'0 0 0 20',
										labelStyle:'color:black;',
										//allowBlank:false,
										xtype : 'textfield',
										labelAlign : 'right',
										emptyText:'请输入英文名',
										width: 250,
										labelWidth : 60,
										height:22,
										id : 'englishName',
										name : 'englishName'
									 }]
								},{
									//第二行   人员类别，性别,出生日期
									layout:'hbox',
							        bodyPadding: 8,
							        border: false,
									buttonAlign:"center",
									items:[{
										fieldLabel:'人员类别',
										labelStyle:'color:red;',
										allowBlank:false,
										xtype : 'combo',
										emptyText:'请选择人员类别',
										blankText:'此选项不能为空',
										width: 250,
										labelAlign : 'right',
										labelWidth : 60,
										//height:22,
										editable : false,
										//类别应该加载后台数据
										store : new Ext.data.JsonStore({
											proxy: new Ext.data.HttpProxy({
												url : "getInfoTypeByPersonStore.do"
											}),
											fields:['text', 'value'],
										    autoLoad:true,
										    autoDestroy: true
										}),
										name : 'infoType',
										id:'infoType'
									},{
										fieldLabel:'性别',
										margin:'0 0 0 20',
										labelStyle:'color:red;',
										allowBlank:false,
										xtype : 'combo',
										labelAlign : 'right',
										emptyText:'男/女',
										blankText:'此选项不能为空',
										width: 250,
										labelWidth : 60,
										//height:22,
										editable : false,
										store:Ext.create('Ext.data.Store',{
											fields:['text','value'],
											data:[
												{text:'男',value:'男'},
												{text:'女',value:'女'}
											]
										}),
										name : 'sex',
										id:'sex',
									},{
										fieldLabel:'出生日期',
										format: 'Y-m-d', 
										//value:'1980-01-01',
										maxValue:new Date(),
										value:new Date(),
										margin:'0 0 0 20',
										labelStyle:'color:red;',
										allowBlank:false,
										xtype : 'datefield',
										labelAlign : 'right',
										emptyText:'请选择出生日期',
										blankText:'此选项不能为空',
										width: 250,
										labelWidth : 60,
										//height:22,
										name : 'bebornTime',
										id : 'bebornTime'
									}]
								},{
									//第三行
									layout:'hbox',
							        bodyPadding: 8,
							        border: false,
									buttonAlign:"center",
									items:[{
										fieldLabel:'现住地址:',
										//margin:'0 0 0 20',
										labelStyle:'color:black;',
										//可以为空
										//allowBlank:false,
										xtype : 'textfield',
										labelAlign : 'right',
										emptyText:'请输入现住地址',
										width: 250,
										labelWidth : 60,
										height:22,
										name : 'presentAddress',
										id : 'presentAddress'
									},{
										fieldLabel:'户籍地址',
										labelStyle:'color:red;',
										allowBlank:false,
										xtype : 'textfield',
										labelAlign : 'right',
										emptyText:'请输入户籍地址',
										blankText:'此选项不能为空',
										width: 250,
										labelWidth : 60,
										height:22,
										margin:'0 0 0 20',
										name : 'registerAddress',
										id : 'registerAddress',
										listeners : {
											'focus' : function(thiz,the,eObj) {
												Ext.create('Ushine.utils.WinUtils').asingleCityWin(thiz,the,eObj);
											}
										}
									},{
										fieldLabel:'工作单位',
										margin:'0 0 0 20',
										labelStyle:'color:black;',
										//allowBlank:false,
										xtype : 'textfield',
										labelAlign : 'right',
										emptyText:'请输入工作单位',
										//blankText:'此选项不能为空',
										width: 250,
										labelWidth : 60,
										height:22,
										name : 'workUnit',
										id : 'workUnit'
									}]
								},{
									//履历和基本情况
									layout:'hbox',
							        bodyPadding: 8,
							        border: false,
									buttonAlign:"center",
									height:150,
									items:[{
										xtype:'htmleditor',
										border:false,
										emptyText :'请输入人员履历',
										name : 'antecedents',
										id : 'antecedents',
										fieldLabel: '履历',
										labelWidth : 60,
										labelAlign : 'right',
										height:'100%',
										enableFont:false,
										margin:'0 10 0 0',
										width:390
									},{
										xtype:'htmleditor',
										border:false,
										emptyText :'请输入人员活动情况',
										name : 'activityCondition',
										id : 'activityCondition',
										fieldLabel: '活动情况',
										labelWidth : 60,
										labelAlign : 'right',
										height:'100%',
										enableFont:false,
										width:390
									}]
								}]
							},{
								//证件及网络账号
								xtype:'fieldset',
								border:true,
								layout:'vbox',
								title:'证件及网络账号',
								items:[{
									//第四行
									//证件和网络号
									xtype:'panel',
									border:false,
									layout:'vbox',
									bodyPadding: 5,
									height:160,
									width:805,
									items:[{
										xtype:'panel',
										border:false,
										margin:'0 0 0 3',
										buttonAlign:'left',
										layout:'hbox',
										width:'100%',
										items:[
											Ext.create('Ushine.buttons.Button',{
												text: '添加证件',
												baseCls: 't-btn-red',
												height:22,
												width:80,
												handler:function(){
													//添加证件的window
													Ext.create('CertificatesTypeWin').show();
												}
											}),
											Ext.create('Ushine.buttons.Button',{
												text: '添加网络身份',
												baseCls: 't-btn-red',
												height:22,
												width:80,
												margin:'0 0 0 320',
												handler:function(){
													//添加网络证件的window
													Ext.create('NetworkAccountTypeWin').show();
												}
											})
										],
										height:30
									},{
										xtype:'panel',
										border:false,
										height:110,
										width:'100%',
										layout:'hbox',
										margin:'0 0 0 8',
										items:[{
											xtype:'gridpanel',
											id:'certificatestypegrid',
											//证件的grid
											columns:[
												{text:'证件类型',dataIndex:"certificatesType",flex:2},
												{text:'证件号码',dataIndex:"certificatesTypeNumber",flex:2},
												{text:'操作',xtype:'actioncolumn',flex:1,icon: 'images/cancel.png',handler:function(grid,row,col){
														//删除
														var rec = grid.getStore().getAt(row);
													  	grid.getStore().remove(rec);
													}
												}
											],
											flex:1,
											//证件store
											store: Ext.data.StoreManager.lookup('certificatesTypeStore'),
											height:'100%'
										},{
											xtype:'gridpanel',
											id:'networkaccounttypegrid',
											//网络证件的grid
											margin:'0 0 0 20',
											columns:[
												{text:'网络账号类型',dataIndex:"networkAccountType",flex:2},
												{text:'网络账号',dataIndex:"networkAccountTypeNumber",flex:2},
												{text:'操作',xtype:'actioncolumn',flex:1,icon: 'images/cancel.png',handler:function(grid,row,col){
														//删除
														var rec = grid.getStore().getAt(row);
													  	grid.getStore().remove(rec);
													}
												}
											],
											flex:1,
											height:'100%',
											//网络证件store
											store: Ext.data.StoreManager.lookup('networkAccountTypeStore'),
											height:'100%'
										}]
									}]
								}]
							},{
								xtype:'fieldset',
								border:true,
								layout:'vbox',
								title:'照片及附件',
								items:[{
									//上传照片和附件
									xtype:'panel',
									border:false,
									layout:'vbox',
									bodyPadding: '5 5 0 5',
									height:150,
									//margin:'0 0 0 20',
									width:805,
									items:[{
										xtype:'panel',
										border:false,
										margin:'0 0 0 3',
										buttonAlign:'left',
										layout:'hbox',
										width:'100%',
										items:[{
											xtype:'filefield',
											buttonOnly:true,
											//buttonText:'上传照片',
											buttonConfig:{
												text:'上传照片',
												//baseCls:"t-btn-red",
												style:{
													backgroundColor:'#ff443a',
													verticalAlign:'middle'
												},
												width:80,
												height:22
											},
											listeners:{
												afterrender:function(cmp){
													  cmp.fileInputEl.set({
								        				  accept:'image/*'
								        			  });
												},
												change:function(){
													var arr=this.value.split(".");
													//后面扩展名
													var str=arr[arr.length-1];
													var flag=false;
													var types=["jpg","JPG","png","gif"];
													Ext.Array.each(types,function(type){
														if(type==str){
															flag=true;
														}
													});
													if(flag){
														//上传照片
														var form  = Ext.getCmp('dataStoragePersonForm');
														if(form.isValid()){
															form.submit({
																url:'personStorePhototUpload.do?number='+sjnum,
																method:'POST',
																waitMsg:'照片上传中',
																//1分钟
																timeout:1000*60,
																success:function(form,action){
																	var personStorePhotoImage=Ext.getCmp('personStorePhotoImage');
																	//console.log(Ext.get('nophotospan'));
																	Ext.get('nophotospan').dom.innerHTML="双击可删除";
																	personStorePhotoImage.add(self.update('tempPersonStorePhotoUploadImages'+sjnum
																		+"/"+action.result.msg))
																},
																// 提交失败的回调函数
																failure : function() {
																	Ext.create('Ushine.utils.Msg').onInfo("服务器出现错误请稍后再试!");
																}
															})
														}else{
															Ext.create('Ushine.utils.Msg').onInfo("请填写完整人员信息");
														}
													}
												}
											}
										},{
											xtype:'filefield',
											margin:'0 0 0 315',
											buttonOnly:true,
											//buttonText:'上传附件',
											buttonConfig:{
												text:'上传附件',
												//baseCls:"t-btn-red",
												style:{
													backgroundColor:'#ff443a',
													verticalAlign:'middle'
												},
												width:80,
												height:22
											},
											listeners:{
												 afterrender:function(cmp){
												 	  //设置文件类型
								        			  cmp.fileInputEl.set({
								        				  accept:'file/*'
								        			  });
						        		  		},
												change:function(){
													//往后台上传附件
													var form  = Ext.getCmp('dataStoragePersonForm');
													if(form.isValid()){
														form.submit({
															  url:'personStoreFileUpload.do?number='+sjnum,
									    					  method:'POST',
									    					  waitMsg:'文件上传中',
									    					  timeout:1000*60*2,
									    					  success:function(form, action) {
									    		    			   var personStoreTemp = Ext.getCmp('personStoreTempFileId');
									    		    			   personStoreTemp.removeAll();
									    		    			   personStoreTemp.add(new Ushine.utils.PersonStoreFileGridPanel(sjnum));
									    					  },
									    					  // 提交失败的回调函数
									    					  failure : function() {
									    						  Ext.create('Ushine.utils.Msg').onInfo("服务器出现错误请稍后再试!");
									    					  }
														});
													}else{
														Ext.create('Ushine.utils.Msg').onInfo("请填写完整人员信息");
													}
												}
											}
										}],
										height:30
									},{
										xtype:'panel',
										border:false,
										height:100,
										width:'100%',
										layout:'hbox',
										margin:'0 0 0 8',
										id:'personStoreTempFilesId',
										items:[{
											//存放照片的panel
											flex:1,
											height:'100%',
											id:'personStorePhotoImage',
											baseCls : 'case-panel-body',
											margin : '0 0 0 0',
											html:"<span id='nophotospan'>没有照片</span>",
											autoScroll:true,
											layout:'hbox'
										},{
											//存放附件的gridpanel
											margin:'0 0 0 20',
											flex:1,
											height:'100%',
											layout:'fit',
											border:false,
											id:'personStoreTempFileId',
											items:Ext.create('Ushine.utils.PersonStoreFileGridPanel')
										}]
									}]
								}]
							}]
				}];
				
				  this.buttons=[
				  	Ext.create('Ushine.buttons.Button', {
				   		text: '确定',
				   		baseCls: 't-btn-red',
				   		handler: function () {
				   			var form  = Ext.getCmp('dataStoragePersonForm');
				   		//提交给后台处理
				   			if(form.getForm().isValid()){
				   		//添加人员
			   				////证件集合certificatestypegrid
			   				var store1=Ext.getCmp('certificatestypegrid').getStore();
			   				var arr1=new Array();
		   					store1.each(function(record){
		   						arr1.push(record.data);
		   					});
		   					//将数据转成json字符串
		   					var certificatestype=Ext.JSON.encode(arr1);
			   				//网络账号集合networkaccounttypegrid
		   					var store2=Ext.getCmp('networkaccounttypegrid').getStore();
		   					var arr2=new Array();
		   					store2.each(function(record){
		   						arr2.push(record.data);
		   					});
		   					var networkaccounttype=Ext.JSON.encode(arr2);
		   					var type = Ext.getCmp('infoType').displayTplData[0].value;
			   				//把url做参数传
		   					var config={
		   						number:sjnum,
		   						certificatestype:certificatestype,
		   						networkaccounttype:networkaccounttype,
		   						typeId:type,
		   						id:res.id
		   					};
		   				
				   				self.savePersonStore(config);
				   			}else{
								Ext.create('Ushine.utils.Msg').onInfo("请填写完整人员信息");
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
			savePersonStore:function(config){
				var form  = Ext.getCmp('dataStoragePersonForm');
				if(form.isValid()){
					var self = this;
					//超时1分钟
					Ext.Ajax.timeout=1000*60;
					//添加遮罩	   				
					var loadMask=new Ext.LoadMask(self,{
						msg:'正在入库人员,请耐心等待……'
					});
					loadMask.show();
					Ext.Ajax.request({
						url:"dataStoragePersonStoreById.do",
						method:'post',
						params:{
							number:config.number,
							personName:Ext.getCmp('personName').getValue(),
							nameUsedBefore:Ext.getCmp('nameUsedBefore').getValue(),
							sex:Ext.getCmp('sex').getValue(),
							englishName:Ext.getCmp('englishName').getValue(),
							infoType:Ext.getCmp('infoType').getValue(),
							bebornTime:Ext.getCmp('bebornTime').getValue(),
							workUnit:Ext.getCmp('workUnit').getValue(),
							presentAddress:Ext.getCmp('presentAddress').getValue(),
							registerAddress:Ext.getCmp('registerAddress').getValue(),
							antecedents:Ext.getCmp('antecedents').getValue(),
							activityCondition:Ext.getCmp('activityCondition').getValue(),
							//证件集合certificatestypegrid
							certificatestype:config.certificatestype,
							//网络账号集合networkaccounttypegrid
							networkaccounttype:config.networkaccounttype,
							typeId:config.typeId,
							id:config.id
						},
						success:function(response){
							var obj=Ext.JSON.decode(response.responseText);
							if(obj.success){
	   							Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
	   							Ext.getCmp('dataStoragePersonInfo_formpanel').close();
	   							Ext.getCmp('cluePersonInfoGridPanel').getStore().reload();
	   							////取消选择
								Ext.getCmp('cluePersonInfoGridPanel').getSelectionModel().clearSelections();
	   							
	   						}else{
	   							Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
	   						}
						},
						failure:function(){
							var obj=Ext.JSON.decode(response.responseText);
	   						Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
	   						Ext.getCmp('dataStoragePersonInfo_formpanel').close();
						}
					})
				}else{
					Ext.create('Ushine.utils.Msg').onInfo("请填写完整人员信息");
				}
			},//显示上传图片
			update:function(path){
				/*var com=Ext.create('Ext.Component',{
					width:100,
					height:100,
					//padding:'2 0 0 10',
					padding:2,
					autoEl:{
						tag:'img',//指定为image标签
						src:path  //资源路径
					}
				});
				return com;*/
				
				Ext.define('PersonPhotoModel',{
					extend:'Ext.data.Model',
					fields:['src','caption']
				});
				Ext.create('Ext.data.Store',{
					model:'PersonPhotoModel',
					id:'imagesStore',
					data:[{
						src:path,caption:''//双击可删除
					}]
				})
				
				var imageTpl = new Ext.XTemplate(
				    '<tpl for=".">',
				        '<div class="mession">',
				          '<span style="margin-left:0px;">{caption}</span><br/>',
				          '<div><img style="width:100px;height:100px;margin:5px;" src="{src}"  /><div>',
				        '</div>',
				    '</tpl>'
				);
				
				var com=Ext.create('Ext.view.View', {
				    store: Ext.data.StoreManager.lookup('imagesStore'),
				    tpl: imageTpl,
				    itemSelector: 'div.mession',
				    //overItemCls: 'mession_over',
					//selectedClass:'mession_select',
				    //width:120,
				    //height:120
					listeners: {
						itemdblclick:function(thiz, record, item, index, e,eOpts){
							//console.log(record.data);
							//从后台删除临时文件
							Ext.Ajax.request({
								url:'deletePersonStorePhoto.do',
								params:{
									src:record.get('src')
								},
								method:'post',
								success:function(response){
									var obj=Ext.JSON.decode(response.responseText);
									//console.log(obj.status==1);
									if(obj.status==1){
										//成功页面删除该照片
										thiz.getStore().remove(record);								
									}
								}
							})
						}
					}
				});
				return com;
			}
		});
		Ext.create('DataStoragePersonInfoWin').show();
	}
});

//数据模型
Ext.define('cluePersonInfoModel', {
	extend: 'Ext.data.Model',
    fields: [
             {name: 'id', type:'string', mapping: 'id'},
             {name: 'infoType', type:'string', mapping: 'infoType'},
             {name: 'personName', type:'string', mapping: 'personName'},
             {name: 'nameUsedBefore', type:'string', mapping: 'nameUsedBefore'},
             {name: 'englishName', type:'string', mapping: 'englishName'},
             {name: 'sex', type:'string', mapping: 'sex'},
             {name: 'bebornTime', type:'string', mapping: 'bebornTime'},
             {name: 'presentAddress', type:'string', mapping: 'presentAddress'},
             {name: 'workUnit', type:'string', mapping: 'workUnit'},
             {name: 'registerAddress', type:'string', mapping: 'registerAddress'},
             {name: 'antecedents', type:'string', mapping: 'antecedents'},
             {name: 'activityCondition', type:'string', mapping: 'activityCondition'},
             {name: 'clueName', type:'string', mapping: 'clueName'},
             {name: 'createDate', type:'date', mapping: 'createDate'},
             {name: 'isToStorage', type:'string', mapping: 'isToStorage'},
           //把类型设置成json的
             {name: 'certificates', type:'json', mapping: 'certificates'},
             {name: 'networkaccount', type:'json', mapping: 'networkaccount'},
           //照片和附件
             {name: 'appendix', type:'string', mapping: 'appendix'},
             ],
    idProperty:'id'
});
