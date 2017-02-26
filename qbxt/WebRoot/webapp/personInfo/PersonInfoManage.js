/**
 * 人员管理
 */
Ext.define('Ushine.personInfo.PersonInfoManage',{
	extend:'Ext.panel.Panel',
	id:'personinfomanage-info',
	title:'重点人员',
	bodyStyle: 'background-color: #ffffff; border: none; padding: 10px;',
	layout: {
		type: 'vbox',
		align : 'stretch',
		pack  : 'start'
	},
	constructor: function() {
		var self = this;
		var date=new Date();
		//第一个月
		date.setMonth(0);
		//第一天
		date.setDate(1);
		var startTime=Ext.Date.format(date,'Y-m-d');
		//var startTime=Ext.Date.format(date.getFullYear()+'-01-01','Y-m-d');
		//startTime=(date.getFullYear()+'-01-01');
		this.items = [{
			// 工具栏
			xtype : 'panel',
		    baseCls : 'tar-body',
			height:120,
			style:"margin-top:-10px;",
			layout:'fit',
			items:{
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
							/*Ext.create('Ushine.buttons.IconButton', {
								   border: false,
								   id: 'importNewBtn',
								   //width:80,
								   btnText: '导入人员', 
								   baseCls: 't-btn-red',
								   handler: function() {
								   	   //导入Excel
									   self.importPersonInfo();
								   }
							}),*/
					       Ext.create('Ushine.buttons.IconButton', {
					    	   border: false,
					    	   id: 'createNewBtn',
					    	   btnText: '新增人员', 
					    	   baseCls: 't-btn-red',
					    	   handler: function() {
					    	   	   //弹出新增人员window
					    		   self.savePersonInfo();
					    	   }
					       }),
					        Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'updateBtn',
					    	   btnText: '修改人员',
					    	   handler: function () {
					    	   	   //获得第二个子组件
							   	   var personStoreGrid=self.getComponent(1);					    	   	
							   	   if(personStoreGrid.getSelectionModel().hasSelection()){
							   	   		var record=personStoreGrid.getSelectionModel().getSelection();
							   	   		if(record.length>1){
							   	   			Ext.create('Ushine.utils.Msg').onInfo("请选择一行数据");
							   	   		}else{
							   	   			//修改数据
							   	   			Ext.Ajax.request({
							   	   				url:'getPersonStoreDetailById.do?id='+record[0].data.id,
							   	   				method:'get',
							   	   				success:function(response){
							   	   					var obj=response.responseText;
							   	   					var temp=Ext.JSON.decode(obj);
							   	   					//console.log(temp.datas[0]);
							   	   					self.modifyPerson(temp.datas[0]);
							   	   				}
							   	   			})
							   	   		}
							   	   }else{
							   	   		Ext.create('Ushine.utils.Msg').onInfo("请至少选择一行数据");
							   	   }
					    	   }
					    	  
					       }),
					       Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'delBtn',
					    	   btnText: '删除人员',
					    	   handler: function () {
					    		  var personStoreGrid=self.getComponent(1);					    	   	
							   	   if(personStoreGrid.getSelectionModel().hasSelection()){
							   	   		//允许多行
							   	   		var record=personStoreGrid.getSelectionModel().getSelection();
							   	   		var personStoreIds=[];
							   	   		for(var i=0;i<record.length;i++){
							   	   			personStoreIds.push(record[i].get('id'));
							   	   		}
							   	   		Ext.Msg.confirm("提示","确定删除选中的人员吗?",function(btn){
							   	   			if (btn == 'yes') {
							   	   			   self.delPesonInfo(personStoreIds);
					    				   }
							   	   		})
							   	   }else{
							   	   		Ext.create('Ushine.utils.Msg').onInfo("请至少选择一行数据");
							   	   }
					    	   }
					       }),
					       Ext.create('Ushine.buttons.IconButton',{
					       		 id: 'zhuanClueBtn',
					       		btnText:'转线索库',
					       		handler:function(){
					       			//转到线索库
					       			var personStoreGrid=self.getComponent(1);		
					       			if(personStoreGrid.getSelectionModel().hasSelection()){
							   	   		//允许多行
							   	   		var record=personStoreGrid.getSelectionModel().getSelection();
							   	   		
							   	   		var personStoreIds=[];
							   	   		for(var i=0;i<record.length;i++){
							   	   			personStoreIds.push(record[i].get('id'));
							   	   		}
							   	   		self.personTurnClueStore(personStoreIds);
							   	   }else{
							   	   		Ext.create('Ushine.utils.Msg').onInfo("请至少选择一行数据");
							   	   }
					       		}
					       }),
						   Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'downExcelBtn',
					    	   btnText: '导出人员',
					    	   handler: function () {
					    		  var personStoreGrid=self.getComponent(1);
					    		  //传递时间参数
					    		  var date=Ext.Date.format(new Date(),'YmdHms');
							   	   if(personStoreGrid.getSelectionModel().hasSelection()){
							   	   		//允许多行
							   	   		var record=personStoreGrid.getSelectionModel().getSelection();
							   	   		var personStoreIds=[];
							   	   		for(var i=0;i<record.length;i++){
							   	   			personStoreIds.push(record[i].get('id'));
							   	   		}
								   	   	//导出并下载
							   	   		exportPersonStoreToExcel(self,personStoreIds,date);
							   	   }else{
							   	   		Ext.create('Ushine.utils.Msg').onInfo("请至少选择一行数据");
							   	   }
					    	   }
					       }),
					      
					       
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
		            		        {"text":"任意字段", "value":"anyField"},
		            		        {"text":"姓名", "value":"personName"},
		            		        {"text":"人员类别", "value":"infoType"},
		            		        {"text":"曾用名", "value":"nameUsedBefore"},
		            		        {"text":"英文名", "value":"englishName"},
		            		        {"text":"现住地址", "value":"presentAddress"},
		            		        {"text":"工作单位", "value":"workUnit"},
		            		        {"text":"户籍地址", "value":"registerAddress"},
		            		        {"text":"出生日期", "value":"bebornTime"}
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
		                           		self.findPersonByProperty();
		                           }  
		                        },
		                        //清空时
		                        change:function(thiz){
		                        	//去掉左右空格
		                        	var value=thiz.getValue().replace(/(^\s*)|(\s*$)/g,'');
		                        	if(value.length==0){
		                        		//self.findPersonByProperty();
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
	                        self.findPersonByProperty();
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
			Ext.create('Ushine.personInfo.PersonInfoGridPanel')
		];	
		this.callParent();		
	},
	//转线索库
	personTurnClueStore:function(orgIds){
		//定义一个SaveCluesWin的Window
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
								    	store:'personStore'
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
									     //取消选择
									     Ext.getCmp('personInfoGridPanel').getStore().reload();
										 Ext.getCmp('personInfoGridPanel').getSelectionModel().clearSelections();
										 Ext.getCmp('SelectClueDataWin').close();
								    },
							        failure: function(form, action) {
							        	loadMask.hide();
							        	Ext.create('Ushine.utils.Msg').onInfo('转线索库失败，请联系管理员');
							 		    Ext.getCmp('SelectClueDataWin').close();
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
	//导入人员Excel
	//自动加载后台列
	importPersonInfo:function(){
		//Ext.create('ImportPersonInfoWin').show();
		var columns=[];
		var fields=new Array();
		//加载标准表头
		Ext.Ajax.request({
			url:'getPersonStoreExcelHeader.do?time='+Math.random(),
			method:'post',
			success:function(response){
				var number=new Ext.grid.column.RowNumberer({
	   				text: '序号',
	   				flex: 0.5
				});
	   			columns.push(number);
					//用decode
					var string=Ext.JSON.decode(response.responseText);
					for(var i=0;i<string.length;i++){
						var dataIndex=string[i].dataIndex;
						fields.push(dataIndex);
						var property=new Ext.grid.column.Column({
							text: string[i].text,
							sortable: true,
							dataIndex: string[i].dataIndex,
							align:'center',
							sortable: false,
							width:100,
							menuDisabled:true
						});
						columns.push(property);
					};
					var del=new Ext.grid.column.Action({
	   				text: '操作',
	   				sortable: true,
	   				dataIndex: 'icon',
	   				align:'center',
	   				width:50,
	   				items:[{
	   				    	//删除 "_",
	   				    	icon: 'images/cancel.png',
	   				        tooltip: '删除',
	   				        handler: function(grid, rowIndex, colIndex){
	   				        	//选中一行的数据
	   			            	//var data = grid.getStore().getAt(rowIndex).data;   
	   			            	grid.getStore().removeAt(rowIndex);
	   			            }
	   			   		}
	   			    ]
	   			});
	   			columns.push(del);
	   			//加载表格
	   			Ext.create('ImportPersonInfoWin',{
	   				columns:columns,
	   				fields:fields,
	   			}).show();
			},
			failure:function(){
				Ext.create('Ushine.utils.Msg').onInfo("获取表头失败");
			}
		});
		//定义map对象
		var map={
			'columns':columns,
			'fields':fields
		};
		console.log(map);
	},
	//新增人员
	savePersonInfo:function(){
		var win=Ext.create('SaveOrUpdatePersonInfoWin',{
			title:'添加人员',
			isClue:'isNotClue',
			items:[Ext.create('SavePersonInfoForm')]
		});
		win.show();
		Ext.getCmp('certificatestypegrid').getStore().removeAll();
		Ext.getCmp('networkaccounttypegrid').getStore().removeAll();
		/*Ext.getCmp('resetPersonInfo').addListener('click',function(){
			//重置
			win.getComponent(0).getForm().reset();
			
			Ext.getCmp('certificatestypegrid').getStore().removeAll();
			Ext.getCmp('networkaccounttypegrid').getStore().removeAll();
		});*/
	},
	//查询人员
	findPersonByProperty:function(){
		this.remove('p_personinfo_grid');
		this.add(Ext.create('Ushine.personInfo.PersonInfoGridPanel'));
	},
	//字符串替换
	replaceString:function(string){
		if(string){
			//去除所有的html标签
			return string.replace(/<[^>]+>/g,"");
		}
	},
	//重置
	resetPersonInfo:function(form,record){
		var self=this;
		form.findField('personName').setValue(self.replaceString(record.personName));
		form.findField('englishName').setValue(self.replaceString(record.englishName));
		form.findField('nameUsedBefore').setValue(self.replaceString(record.nameUsedBefore));
		form.findField('sex').setValue(self.replaceString(record.sex));
		if(self.replaceString(record.bebornTime)){
			form.findField('bebornTime').setValue(self.replaceString(record.bebornTime).substring(0,10));
		}
		form.findField('presentAddress').setValue(self.replaceString(record.presentAddress));
		form.findField('workUnit').setValue(self.replaceString(record.workUnit));
		form.findField('registerAddress').setValue(self.replaceString(record.registerAddress));
		form.findField('antecedents').setValue(self.replaceString(record.antecedents));
		form.findField('activityCondition').setValue(self.replaceString(record.activityCondition));
		form.findField('infoType').setValue(self.replaceString(record.infoType));
		form.findField('id').setValue(record.id);
	},
	//修改人员
	modifyPerson:function(record){
		var win=Ext.create('SaveOrUpdatePersonInfoWin',{
			title:'修改人员信息',
			items:[Ext.create('UpdatePersonInfoForm',{
				record:record
			})]
		});
		//console.log(record);
		win.show();
		Ext.getCmp('certificatestypegrid').getStore().removeAll();
		Ext.getCmp('networkaccounttypegrid').getStore().removeAll();
		//再把数据加进来
		var certificates=record.certificates;
		//.length>0
		if(certificates){
			Ext.getCmp('certificatestypegrid').getStore().add(certificates);
		}
		var networkaccount=record.networkaccount;
		//length>0
		if(networkaccount){
			Ext.getCmp('networkaccounttypegrid').getStore().add(networkaccount);
		}
		//获得第一个组件
		var self=this;
		var form=win.getComponent(0).getForm();
		self.resetPersonInfo(form,record);
	},
	//删除人员
	delPesonInfo:function(personStoreIds){
		var self=this;
		var mask=new Ext.LoadMask(self,{msg:'正在删除人员……'});
		mask.show();
		Ext.Ajax.request({
		    url: 'delPersonStoreById.do',
		    method:'POST',
		    params: {
		    	personStoreIds: personStoreIds
		    },
		    success: function(response, opts){
		       var text = response.responseText;
		       var obj = Ext.JSON.decode(text);
		       if(obj.success){
		    	   mask.hide();
		    	   self.getComponent(1).getStore().reload();
		    	   Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
		       }else{
		    	   Ext.Msg.alert('提示',"删除人员失败，请联系管理员！");
		       }
		    },
		    failure:function(){
		    	 mask.hide();
		    	 Ext.Msg.alert('提示',"删除人员失败，请求后台服务失败！");
		    }
		});
	}
});

//定义一个SaveOrUpdatePersonInfoWin的Window
//修改或更新
Ext.define('SaveOrUpdatePersonInfoWin',{
	extend:'Ushine.win.Window',
	id:'saveorupdatepersoninfowin',
	modal : true,
	layout : 'fit',
	border : false,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:780,
	width:860,
	constructor:function(config){
		this.title=config.title;
		//是否新增线索人员
		this.isClue=config.isClue;
		//线索人员的随机号config.cluePersonNum
		//默认不是线索人员
		this.cluePersonNum=config.cluePersonNum;
		//state为0没关联人员
		this.state=config.state;
		var self=this;
		//this.items=[Ext.create('SavePersonInfoForm')]
		this.items=config.items;
		this.callParent();
	}
	
});
//新增人员的form
Ext.define('SavePersonInfoForm',{
	extend:'Ext.form.Panel',
	id:'savepersoninfoform',
	layout:'vbox',
    bodyPadding: '0 8 8 3',
    margin:0,
    border: false,
	baseCls: 'form-body',
	buttonAlign:"center",
	constructor:function(){
		var sjnum=parseInt(Math.random()*1000);
		var self=this;
		this.items=[{
			layout:'vbox',
	        bodyPadding: 8,
	        border: false,
			buttonAlign:"center",
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
						name : 'personName'
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
						name : 'infoType'
					},{
						fieldLabel:'性别',
						margin:'0 0 0 20',
						labelStyle:'color:red;',
						allowBlank:true,
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
						name : 'sex'
					},{
						fieldLabel:'出生日期',
						format: 'Y-m-d', 
						//value:'1980-01-01',
						maxValue:new Date(),
						value:new Date(),
						margin:'0 0 0 20',
						labelStyle:'color:red;',
						allowBlank:true,
						xtype : 'datefield',
						labelAlign : 'right',
						emptyText:'请选择出生日期',
						blankText:'此选项不能为空',
						width: 250,
						labelWidth : 60,
						//height:22,
						name : 'bebornTime'
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
						name : 'presentAddress'
					},{
						fieldLabel:'户籍地址',
						labelStyle:'color:black;',
						allowBlank:true,
						xtype : 'textfield',
						labelAlign : 'right',
						emptyText:'请输入户籍地址',
						blankText:'此选项不能为空',
						width: 250,
						labelWidth : 60,
						height:22,
						margin:'0 0 0 20',
						name : 'registerAddress',
						listeners : {
							/*'focus' : function(thiz,the,eObj) {
								Ext.create('Ushine.utils.WinUtils').asingleCityWin(thiz,the,eObj);
							}*/
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
						name : 'workUnit'
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
										var form=self.getForm();
										if(form.isValid()){
											form.submit({
												url:'personStorePhototUpload.do?number='+sjnum,
												method:'POST',
												waitMsg:'照片上传中',
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
									var form=self.getForm();
									if(form.isValid()){
										form.submit({
											  url:'personStoreFileUpload.do?number='+sjnum,
					    					  method:'POST',
					    					  waitMsg:'文件上传中',
					    					  timeout:1000*60,
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
	   			//id:'addOrUpdatePersonInfo'
	   			handler:function(){
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
	   				//把url做参数传
   					var config={
   						number:sjnum,
   						certificatestype:certificatestype,
   						networkaccounttype:networkaccounttype
   					};
	   				self.savePersonStore(config);
	   			}
	   		}),Ext.create('Ushine.buttons.Button', {
	   			text: '重置',
	   			margin:'0 0 0 35',
	   			baseCls: 't-btn-yellow',
	   			id:'resetPersonInfo',
	   			handler:function(){
	   				var self=this;
	   				//关闭window
	   				//console.log(self.up('window').getComponent(0));
	   				self.up('window').close();
	   				var win=Ext.create('SaveOrUpdatePersonInfoWin',{
						title:'添加人员',
						isClue:'isNotClue',
						items:[Ext.create('SavePersonInfoForm')]
					});
					win.show();
					Ext.getCmp('certificatestypegrid').getStore().removeAll();
					Ext.getCmp('networkaccounttypegrid').getStore().removeAll();
	   			}
	   		})
	 	];
		this.callParent();
	},
	savePersonStore:function(config){
		//添加人员
		var form=this.getForm();
		var self=this;
		var win=this.up('window');
		//是否新增线索人员
		var isClue=win.isClue;
		//人员是否没关联线索
		var state=win.state;
		if(form.isValid()){
			Ext.Ajax.timeout=1000*60;
			//添加遮罩	   				
			var loadMask=new Ext.LoadMask(self,{
				msg:'正在添加人员,请耐心等待……'
			});
			loadMask.show();
			Ext.Ajax.request({
				url:"findPersonStoreByPersonName.do",
				method:'post',
				params:{
					personName:form.findField('personName').getValue()
				},
				success:function(response){
					var obj=Ext.JSON.decode(response.responseText);
					if(obj.msg=='exist'){
						//已经存在
						Ext.create('Ushine.utils.Msg').onQuest("人员已存在，是否仍添加",function(btn){
							if(btn=='yes'){
								self.savePersonStoreReally(self,config,form,win,loadMask);
							}
						});
					}else if(obj.msg=='not_exist'){
						self.savePersonStoreReally(self,config,form,win,loadMask);
					}
				}
			});
		}else{
			Ext.create('Ushine.utils.Msg').onInfo("请填写完整人员信息");
		}
	},
	//真正添加
	savePersonStoreReally:function(self,config,form,win,loadMask){
		Ext.Ajax.request({
			url:"savePersonStore.do",
			method:'post',
			params:{
				number:config.number,
				personName:form.findField('personName').getValue(),
				nameUsedBefore:form.findField('nameUsedBefore').getValue(),
				sex:form.findField('sex').getValue(),
				englishName:form.findField('englishName').getValue(),
				infoType:form.findField('infoType').getValue(),
				bebornTime:form.findField('bebornTime').getValue(),
				workUnit:form.findField('workUnit').getValue(),
				presentAddress:form.findField('presentAddress').getValue(),
				registerAddress:form.findField('registerAddress').getValue(),
				antecedents:form.findField('antecedents').getValue(),
				activityCondition:form.findField('activityCondition').getValue(),
				//证件集合certificatestypegrid
				certificatestype:config.certificatestype,
				//网络账号集合networkaccounttypegrid
				networkaccounttype:config.networkaccounttype,
				//是否为新增线索人员
				isClue:win.isClue,
				//如果为0代表没关联
				state:win.state,
				cluePersonNum:win.cluePersonNum
			},
			success:function(response){
				var obj=Ext.JSON.decode(response.responseText);
				//console.log(obj);
				Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				//关闭window
				self.findParentByType('window').close();
				if(Ext.getCmp('personInfoGridPanel')){
					//刷新前台数据
					Ext.getCmp('personInfoGridPanel').getStore().reload();
				}
				var CluesTempDataGrid = Ext.getCmp('CluesTempDataGridId');
				if(CluesTempDataGrid){
					//新增线索文档后刷新数据
					CluesTempDataGrid.removeAll();
					CluesTempDataGrid.add(new Ushine.utils.CluesTempDataGridPanel(win.cluePersonNum));
				};
				loadMask.hide();
			},
			failure:function(){
				loadMask.hide();
				Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败!");
			}
		})
	},
	//显示上传图片
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
//修改人员的form
Ext.define('UpdatePersonInfoForm',{
	extend:'Ext.form.Panel',
	id:'updatepersoninfoform',
	layout:'vbox',
    bodyPadding: '0 8 8 3',
    margin:0,
    border: false,
	baseCls: 'form-body',
	buttonAlign:"center",
	constructor:function(config){
		var sjnum=parseInt(Math.random()*1000);
		var self=this;
		//人员信息
		this.record=config.record;
		this.items=[{
			layout:'vbox',
	        bodyPadding: 8,
	        border: false,
			buttonAlign:"center",
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
						name : 'personName'
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
						valueField:'text'
					},{
						fieldLabel:'性别',
						margin:'0 0 0 20',
						labelStyle:'color:red;',
						allowBlank:true,
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
						name : 'sex'
					},{
						fieldLabel:'出生日期',
						format: 'Y-m-d', 
						value:'1980-01-01',
						maxValue:new Date(),
						margin:'0 0 0 20',
						labelStyle:'color:red;',
						allowBlank:true,
						xtype : 'datefield',
						labelAlign : 'right',
						emptyText:'请选择出生日期',
						blankText:'此选项不能为空',
						width: 250,
						labelWidth : 60,
						//height:22,
						name : 'bebornTime'
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
						name : 'presentAddress'
					},{
						fieldLabel:'户籍地址',
						labelStyle:'color:black;',
						allowBlank:true,
						xtype : 'textfield',
						labelAlign : 'right',
						emptyText:'请输入户籍地址',
						blankText:'此选项不能为空',
						width: 250,
						labelWidth : 60,
						height:22,
						margin:'0 0 0 20',
						name : 'registerAddress',
						listeners : {
							/*'focus' : function(thiz,the,eObj) {
								Ext.create('Ushine.utils.WinUtils').asingleCityWin(thiz,the,eObj);
							}*/
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
						name : 'workUnit'
					}]
				},{
					//履历和基本情况
					layout:'hbox',
			        bodyPadding: 8,
			        border: false,
					buttonAlign:"center",
					//height:150,
					items:[{
						/*xtype:'htmleditor',
						emptyText :'请输入人员履历',
						name : 'antecedents',
						fieldLabel: '履历',
						labelWidth : 60,
						labelAlign : 'right',
						height:'100%',
						margin:'0 10 0 0',
						enableFont:false,
						width:390*/
						xtype : 'displayfield',
						fieldLabel:'履历',
						width: 250,
						labelWidth :60,
						labelAlign : 'right',
						value:"<a href='javascript:void(0)' onclick=editPersonAntecedents()>"
							+"点击修改人员履历<a/>"
					},{
						//隐藏
						xtype : 'hidden',
						name : 'antecedents'
					},{
						/*xtype:'htmleditor',
						emptyText :'请输入人员履历',
						name : 'activityCondition',
						fieldLabel: '活动情况',
						labelWidth : 60,
						enableFont:false,
						labelAlign : 'right',
						height:'100%',
						width:390*/
						xtype : 'displayfield',
						fieldLabel:'活动情况',
						labelWidth :60,
						margin:'0 0 0 20',
						labelAlign : 'right',
						width: 250,
						value:"<a href='javascript:void(0)' onclick=editPersonActivityCondition()>"
							+"点击修改人员活动情况<a/>"
					},{
						//隐藏
						xtype : 'hidden',
						name : 'activityCondition'
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
					height:210,
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
						height:160,
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
					height:220,
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
										var form=self.getForm();
										if(form.isValid()){
											form.submit({
												url:'personStorePhototUpload.do?number='+sjnum,
												method:'POST',
												waitMsg:'照片上传中',
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
									var form=self.getForm();
									if(form.isValid()){
										form.submit({
											  url:'personStoreFileUpload.do?number='+sjnum,
					    					  method:'POST',
					    					  waitMsg:'文件上传中',
					    					  timeout:1000*60,
					    					  success:function(form, action) {
					    		    			   var personStoreTemp = Ext.getCmp('personStoreTempFileId');
					    		    			   personStoreTemp.removeAll();
					    		    			   personStoreTemp.add(new Ushine.utils.PersonStoreFileGridPanel(sjnum,self.record));
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
						height:170,
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
							layout:'hbox',
							listeners:{
								//显示原来的照片
								'afterrender':function(){
									var personStorePhotoImage=Ext.getCmp('personStorePhotoImage');
									//console.log(config.record[0].data.photo);
									/*Ext.get('nophotospan').dom.innerHTML="双击可删除";
									var arr=config.record[0].data.photo.split(",");
									if(config.record[0].data.photo.length>1){
										//原来有照片
										Ext.Array.each(arr,function(value){
											personStorePhotoImage.add(self.update(value));
										});
									}*/
								}
							}
						},{
							//存放附件的gridpanel
							margin:'0 0 0 20',
							flex:1,
							height:'100%',
							layout:'fit',
							border:false,
							id:'personStoreTempFileId',
							//items:Ext.create('Ushine.utils.PersonStoreFileGridPanel')
							items:[new Ushine.utils.PersonStoreFileGridPanel(sjnum,self.record)]
						}]
					}]
				}]
			}]
		}];
		this.buttons=[
	  		Ext.create('Ushine.buttons.Button', {
	   			text: '确定',
	   			baseCls: 't-btn-red',
	   			//id:'addOrUpdatePersonInfo'
	   			handler:function(){
	   				//修改人员信息
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
	   				//附件列表数据
   					var store3=Ext.getCmp('personStoreFileGridPanelId').getStore();
   					var arr3=new Array();
   					store3.each(function(record){
   						arr3.push(record.data.filePath);
   					});
   					var appendix=Ext.JSON.encode(arr3);
   					//var appendix=encodeURI(encodeURI(arr3));
	   				//把url做参数传
   					var config={
   						number:sjnum,
   						certificatestype:certificatestype,
   						networkaccounttype:networkaccounttype,
   						appendix:arr3
   					};
	   				self.updatePersonStore(config);
	   			}
	   		}),Ext.create('Ushine.buttons.Button', {
	   			text: '重置',
	   			margin:'0 0 0 35',
	   			baseCls: 't-btn-yellow',
	   			id:'resetPersonInfo',
	   			handler:function(){
	   				var self=this;
	   				var win=self.up('window');
	   				win.close();
	   				var btn=Ext.getCmp('updateBtn');
	   				//触发事件 
	   				//重新加载
	   				btn.handler();
	   			}
	   		})
	 	];
		this.callParent();
	},
	updatePersonStore:function(config){
		//修改人员
		var form=this.getForm();
		var self=this;
		if(form.isValid()){
			Ext.Ajax.timeout=1000*60;
			//添加遮罩	   				
			var loadMask=new Ext.LoadMask(self,{
				msg:'正在修改人员,请耐心等待……'
			});
			loadMask.show();
			var arr4=new Array();
			//照片路径列表
			var views=Ext.getCmp('personStorePhotoImage').query('dataview');
			Ext.Array.each(views,function(view){
				var store=view.getStore();
				store.each(function(record){
					arr4.push(record.data.src);
					//console.log(record.data.src);
				})
			});
			Ext.Ajax.request({
				url:"updatePersonStoreById.do",
				method:'post',
				params:{
					id:form.findField('id').getValue(),
					number:config.number,
					personName:form.findField('personName').getValue(),
					nameUsedBefore:form.findField('nameUsedBefore').getValue(),
					sex:form.findField('sex').getValue(),
					englishName:form.findField('englishName').getValue(),
					infoType:form.findField('infoType').getValue(),
					bebornTime:form.findField('bebornTime').getValue(),
					workUnit:form.findField('workUnit').getValue(),
					presentAddress:form.findField('presentAddress').getValue(),
					registerAddress:form.findField('registerAddress').getValue(),
					antecedents:form.findField('antecedents').getValue(),
					activityCondition:form.findField('activityCondition').getValue(),
					//证件集合certificatestypegrid
					certificatestype:config.certificatestype,
					//网络账号集合networkaccounttypegrid
					networkaccounttype:config.networkaccounttype,
					//附件列表
					appendix:config.appendix,
					//照片
					photo:arr4
				},
				success:function(response){
					var obj=Ext.JSON.decode(response.responseText);
					Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
					//关闭window
					self.findParentByType('window').close();
					//刷新前台数据
					if(Ext.getCmp('personInfoGridPanel')){
						Ext.getCmp('personInfoGridPanel').getStore().reload();
					};
					//取消选择
					Ext.getCmp('personInfoGridPanel').getSelectionModel().clearSelections();
				},
				failure:function(){
					loadMask.disable();
					Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败!");
				}
			})
		}else{
			
			Ext.create('Ushine.utils.Msg').onInfo("请填写完整人员信息");
		}
	},
	//显示上传图片
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
		//console.log(path);
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
		          '<div><img style="width:130px;height:130px;margin:5px;" src="{src}"  /><div>',
		        '</div>',
		    '</tpl>'
		);
		
		var com=Ext.create('Ext.view.View', {
		    store: Ext.data.StoreManager.lookup('imagesStore'),
		    tpl: imageTpl,
		    //id:'personphotoview',
		    itemSelector: 'div.mession',
		    //overItemCls: 'mession_over',
			//selectedClass:'mession_select',
		    //width:120,
		    //height:120
			listeners: {
				itemdblclick:function(thiz, record, item, index, e,eOpts){
					var photo=record.get('src');
					//console.log(photo);
					//console.log(photo.indexOf("tempPersonStorePhotoUploadImages"))
					if(photo.indexOf('tempPersonStorePhotoUploadImages')>-1){
						//这是临时上传的
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
					}else{
						//只需要删除这个节点就行					
						thiz.getStore().remove(record);	
					}
				}
			}
		});
		return com;
	}
});

//证件model
Ext.define('CertificatesTypeModel',{
	extend:'Ext.data.Model',
	fields:['certificatesType','certificatesTypeNumber']
})
//证件store
Ext.create('Ext.data.Store',{
	model:'CertificatesTypeModel',
	storeId:'certificatesTypeStore',
	data:[]
})
//网络账号model
Ext.define('NetworkAccountTypeModel',{
	extend:'Ext.data.Model',
	fields:['networkAccountType','networkAccountTypeNumber']
})

//网络账号store
Ext.create('Ext.data.Store',{
	model:'NetworkAccountTypeModel',
	storeId:'networkAccountTypeStore',
	data:[]
})

//添加证件win
Ext.define('CertificatesTypeWin',{
	extend:'Ushine.win.Window',
	modal : true,
	layout : 'fit',
	border : false,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:200,
	width:300,
	title:'添加证件',
	buttonAlign:'center',
	constructor:function(){
		var self=this;
		this.items=[{
			xtype:'form',
			layout:'vbox',
			bodyPadding: '8',
			border:false,
			buttonAlign:'center',
			items:[{
				xtype:'combo',
				width:250,
				labelWidth:80,
				margin:'15 0 0 0',
				fieldLabel: '证件类型',
				labelAlign:'right',
				emptyText :'请选择证件类型',
				name:'certificatesType',
				allowBlank:false,
				editable:false,
				//类别应该加载后台数据
				store : new Ext.data.JsonStore({
					proxy: new Ext.data.HttpProxy({
						url : "getInfoTypeByCertificatesStore.do"
					}),
					fields:['text', 'value'],
				    autoLoad:true,
				    autoDestroy: true
				})
			},{
				xtype:'textfield',
				width:250,
				allowBlank:false,
				labelWidth:80,
				margin:'25 0 0 0',
				fieldLabel: '证件号码',
				labelAlign:'right',
				emptyText :'请输入证件号码',
				name:'certificatesTypeNumber'
			}]
		}];
		this.buttons=[{
			text:'确定',
			baseCls: 't-btn-red',
			height:22,
			align:'center',
			handler:function(){
				//添加证件
				var grid=Ext.getCmp('certificatestypegrid');
				var form=self.getComponent(0).getForm();
				if(form.isValid()){
					var store=grid.getStore();
					store.add(
						{certificatesType:form.findField('certificatesType').getValue(),
						certificatesTypeNumber:form.findField('certificatesTypeNumber').getValue()}
					);
					self.close();
				}else{
						Ext.create('Ushine.utils.Msg').onInfo('填写完整证件信息');
				}
			}
		}];
		this.callParent();
	}
})

//添加网络账号win
Ext.define('NetworkAccountTypeWin',{
	extend:'Ushine.win.Window',
	modal : true,
	layout : 'fit',
	border : false,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:200,
	width:300,
	title:'添加网络账号',
	buttonAlign:'center',
	constructor:function(){
		var self=this;
		this.items=[{
			xtype:'form',
			layout:'vbox',
			bodyPadding: '8',
			border:false,
			buttonAlign:'center',
			items:[{
				xtype:'combo',
				width:250,
				labelWidth:80,
				margin:'15 0 0 0',
				fieldLabel: '网络类型',
				labelAlign:'right',
				emptyText :'请选择网络证件类型',
				name:'networkAccountType',
				allowBlank:false,
				editable:false,
				//类别加载后台数据
				store : new Ext.data.JsonStore({
					proxy: new Ext.data.HttpProxy({
						url : "getInfoTypeByNetworkAccountStore.do"
					}),
					fields:['text', 'value'],
				    autoLoad:true,
				    autoDestroy: true
				 })
			},{
				xtype:'textfield',
				width:250,
				allowBlank:false,
				labelWidth:80,
				margin:'25 0 0 0',
				fieldLabel: '网络号码',
				labelAlign:'right',
				emptyText :'请输入证件号码',
				name:'networkAccountTypeNumber'
			}]
		}];
		this.buttons=[{
			text:'确定',
			baseCls: 't-btn-red',
			height:22,
			align:'center',
			handler:function(){
				//添加网络证件
				var grid=Ext.getCmp('networkaccounttypegrid');
				var form=self.getComponent(0).getForm();
				if(form.isValid()){
					var store=grid.getStore();
					store.add(
						{networkAccountType:form.findField('networkAccountType').getValue(),
						networkAccountTypeNumber:form.findField('networkAccountTypeNumber').getValue()}
					);
					self.close();
				}else{
						Ext.create('Ushine.utils.Msg').onInfo('填写完整网络账号信息');
				}
			}
		}];
		this.callParent();
	}
});
//修改履历
function editPersonAntecedents(){
	//updatepersoninfoform
	//var win=Ext.getCmp('updatepersoninfoform');
	var form=Ext.getCmp('updatepersoninfoform').getForm();
	//得到表单
	var field=form.findField('antecedents');
	var value=form.findField('antecedents').getValue();
	Ext.define('EditPersonAntecedentsWin',{
		height:800,
		width:1000,
		extend:'Ext.window.Window',
		layout : 'fit',
		border : false,
		buttonAlign:"center",
		title:'修改人员履历信息',
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
	Ext.create('EditPersonAntecedentsWin').show();
};
//修改活动情况
function editPersonActivityCondition(){
	var form=Ext.getCmp('updatepersoninfoform').getForm();
	//得到表单
	var field=form.findField('activityCondition');
	var value=form.findField('activityCondition').getValue();
	Ext.define('EditPersonActivityConditionWin',{
		height:800,
		width:1000,
		extend:'Ext.window.Window',
		layout : 'fit',
		border : false,
		buttonAlign:"center",
		title:'修改人员活动情况',
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
	Ext.create('EditPersonActivityConditionWin').show();
}
//导出人员信息到Excel
function exportPersonStoreToExcel(thiz,personStoreIds,date){
	var myMask = new Ext.LoadMask(thiz, {msg:"正在导出人员..."});
	myMask.show();
	//导入到Excel中
	//2分钟超时
	Ext.Ajax.timeout=1000*60*2;
	Ext.Ajax.request({
		url:'personToExcel.do',
		method:'post',
		params:{
			personStoreIds:personStoreIds,
			date:date
		},
		success:function(response){
			myMask.hide();
			var obj=Ext.JSON.decode(response.responseText);
			Ext.create('Ushine.utils.Msg').onInfo(obj.msg,function(btn,text){
				if(btn=='ok'){
					//下载
					window.open("downLoadPersonExcel.do?date="+date);
				}
			});
		},
		failure:function(){
			Ext.create('Ushine.utils.Msg').onInfo("请求后台失败!!!");
			myMask.hide();
		}
	})
};
//定义导入人员的ImportPersonInfoWin
Ext.define('ImportPersonInfoWin',{
	extend:'Ushine.win.Window',
	title : "导入人员Excel",
	modal : true,
	layout:{
		type : 'vbox',
		align:'stretch'
	},
	autoScroll:true,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:600,
	width:800,
	margin:'10 0 0 0',
	bodyPadding: 5,
	constructor:function(config){
		var self=this;
		var sjnum=parseInt(Math.random()*1000);
		this.items=[{
			xtype:'fieldset',
			margin:'0 10 0 10',
			title:'基本信息',
			items:[{
				//第一大行
				layout:{
					type : 'vbox',
					align:'stretch'
				},
				id:'identifypersoninfoform',
				xtype:'form',
				border:false,
				items:[{
					//第一行
					layout:{
						type : 'hbox',
						//align:'stretch'
					},
					height:40,
					border:false,
					bodyPadding: 5,
					items:[{
						xtype:'filefield',
						margin:'0 5 0 0',
						buttonOnly:true,
						buttonConfig:{
							text:'上传Excel',
							margin:'0 0 0 20',
							style:{
								backgroundColor:'#ff443a',
								verticalAlign:'middle'
							},
							width:100,
							height:22
						},
						listeners:{
							 afterrender:function(cmp){
							 	  //设置文件类型
			        			  cmp.fileInputEl.set({
			        				  //accept:'application/vnd.ms-excel',
			        				  accept:'file/*',
			        				  //选择多个文件
			        				  //multiple:'multiple'
			        			  });
					  		},
							change:function(){
								var form=Ext.getCmp('identifypersoninfoform').getForm();
								form.submit({
									  url:'identifyPersonInfoExcel.do?number='+sjnum,
									  method:'POST',
									  waitMsg:'识别人员信息中',
									  timeout:1000*60*5,
									  success:function(form, action) {
										  var result=Ext.JSON.decode(action.response.responseText);
										  if(result.status>0){
											  result=Ext.JSON.decode(result.msg);
											  //console.log(result.startRowNumber);
											  form.findField('realRowCount').setValue(result.realRowCount+'条');
											  form.findField('startRowNumber').setValue(result.startRowNumber);
											  var store=Ext.getCmp('importpersoninfogridpanel').getStore();
											  store.removeAll();
											  store.add((result.array));
											  Ext.create('Ushine.utils.Msg').onInfo("识别完成");
										  }else{
											  Ext.create('Ushine.utils.Msg').onInfo(result.msg);
										  }
									  },
									  // 提交失败的回调函数
									  failure : function(form, action) {
										  var result=Ext.JSON.decode(action.response.responseText);
										  Ext.create('Ushine.utils.Msg').onInfo(result.msg);
									  }
								});
							}
						}
					},Ext.create('Ushine.buttons.Button',{
						text: '入库',
						height:22,
						width:100,
	   					baseCls: 't-btn-yellow',
	   					handler:function(){
	   						//超时
	   						Ext.Ajax.timeout=1000*60*60;
	   						var store=Ext.getCmp('importpersoninfogridpanel').getStore();
	   						var array=new Array();
	   						//获得数据
	   						store.each(function(record){
	   							array.push(record.data);
	   						});
	   						var form=Ext.getCmp('identifypersoninfoform').getForm();
	   						var startRowNumber= form.findField('startRowNumber').getValue();
	   						if(array.length>0){
	   							var loadMask=new Ext.LoadMask(self,{
			   						msg:'正在添加人员,请耐心等待……'
			   					});
			   					loadMask.show();
			   					Ext.Ajax.request({
			   						url:'savePersonStore.do',
			   						params:{
			   							multiple:'multiple',
			   							datas:Ext.JSON.encode(array),
			   							startRowNumber:startRowNumber
			   						},
			   						success:function(response){
			   							loadMask.hide();
		   								var obj=Ext.JSON.decode(response.responseText);
		   								var msg=Ext.JSON.decode(obj.msg);
				   						Ext.create('Ushine.utils.Msg').onInfo(msg.msg);
				   						//未保存的信息
				   						form.findField('failure').setValue(msg.failure);
				   						//保存成功
				   						form.findField('saveCount').setValue(msg.saveCount);
				   						form.findField('savePersonName').setValue(msg.savePersonName);
				   						//关闭窗体
				   						//self.close();
				   						if(msg.saveCount>0){
					   						//刷新数据
											Ext.getCmp("personInfoGridPanel").getStore().reload();
				   						}
				   						store.removeAll();
			   						},
			   						failure:function(){
			   							loadMask.hide();
				   						Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败！！！");
			   						}
			   					})
	   						}else{
	   							Ext.create('Ushine.utils.Msg').onInfo("请先添加人员数据");
	   						}
	   					}
					}),{
						xtype:'displayfield',
						labelAlign : 'right',
						width: 150,
						labelWidth : 60,
						fieldLabel:'数量总量',
						value:'0条',
						name:'realRowCount',
						margin:'0 0 0 20'
					},{
				        xtype: 'hiddenfield',
				        name: 'startRowNumber'
				    },{
				        xtype: 'hiddenfield',
				        name: 'failure'
				    },{
				        xtype: 'hiddenfield',
				        name: 'savePersonName'
				    },{
				        xtype: 'hiddenfield',
				        name: 'saveCount',
				        value:'0'
				    },{
						xtype:'displayfield',
						labelAlign : 'right',
						width: 150,
						labelWidth : 60,
						fieldLabel:'成功入库',
						value:"<a href='javascript:void(0)' onclick=showSavedPersonStore()>点击查看详情<a/>"
					},{
						xtype:'displayfield',
						labelAlign : 'right',
						width: 150,
						labelWidth : 60,
						//点击查看详情
						fieldLabel:'未入库',
						//name:'identifyCount',
						value:"<a href='javascript:void(0)' onclick=showNotSavedPersonStore()>点击查看详情<a/>",
					}]
				}]
			}]
		},{
			//xtype:'fieldset',
			border:false,
			//margin:'10 10 10 10',
			margin:10,
			//title:'Excel内容',
			layout:'fit',
			flex:3,
			items:[
				//读取人员信息
				Ext.create('ImportPersonInfoGridpanel',{
					fields:config.fields,
					columns:config.columns
				})
			]
		}]
		this.callParent();
	}
});
/**
 * 设置导入gridpanel的列
 */
//定义导入人员的gridpanel
Ext.define('ImportPersonInfoGridpanel',{
	extend:'Ext.grid.Panel',
	id:'importpersoninfogridpanel',
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
	autoScroll:true,
	//设置列
	//columns:setImportPersonInfoGridpanelColumns(self).columns,
	constructor:function(config){
		var self=this;
		
		Ext.define('ImportPersonInfoModel',{
			extend: 'Ext.data.Model',
			/*fields:['infoType','personName','nameUsedBefore','englishName',
			        'sex','bebornTime','presentAddress','workUnit','registerAddress',
			        'email','mobilephone','telephone','idcard','passport','hktrafficpermit',
			        'qq','weixin']*/
			fields:config.fields
		});
		var store=Ext.create('Ext.data.JsonStore',{
			model:'ImportPersonInfoModel'
		});
		this.columns=config.columns;
		/*this.columns=[
			{xtype: 'rownumberer',flex: 0.5},
		    {text: '人员类别',  dataIndex: 'infoType',sortable: false,width:100,menuDisabled:true},
		    {text: '姓名',  dataIndex: 'personName',sortable: false,width:100,menuDisabled:true},
		    {text: '曾用名',  dataIndex: 'nameUsedBefore',sortable: false,width:100,menuDisabled:true},
		    {text: '英文名',  dataIndex: 'englishName',sortable: false,width:100 ,menuDisabled:true},
		    {text: '性别',  dataIndex: 'sex',sortable: false,flex: 0.5,menuDisabled:true},
		    {text: '出生日期',  dataIndex: 'bebornTime',sortable: false,width:100,menuDisabled:true},
		    {text: '现住地址',  dataIndex: 'presentAddress',sortable: false,width:100,menuDisabled:true},
		    {text: '工作单位',  dataIndex: 'workUnit',sortable: false,width:100,menuDisabled:true},
		    {text: '户籍地址',  dataIndex: 'registerAddress',sortable: false,width:100,menuDisabled:true},
		    //账号
		    {text: '电子邮箱',  dataIndex: 'email',sortable: false,width:100,menuDisabled:true},
		    {text: '手机号码',  dataIndex: 'mobilephone',sortable: false,width:100,menuDisabled:true},
		    {text: '固定电话',  dataIndex: 'telephone',sortable: false,width:100,menuDisabled:true},
		    {text: '身份证',  dataIndex: 'idcard',sortable: false,width:100,menuDisabled:true},
		    {text: '护照',  dataIndex: 'passport',sortable: false,width:100,menuDisabled:true},
		    {text: '港澳通行证',  dataIndex: 'hktrafficpermit',sortable: false,width:100,menuDisabled:true},
		    {text: 'QQ号码',  dataIndex: 'qq',sortable: false,width:100,menuDisabled:true},
		    {text: '微信号',  dataIndex: 'weixin',sortable: false,width:100,menuDisabled:true},
		    //{text: 'csdn账号',  dataIndex: 'csdn',sortable: false,width:100,menuDisabled:true},
		    {text: '操作',sortable: true,dataIndex: 'icon',align:'center',width:50,xtype:'actioncolumn',items:[{
			    	//删除 "_",
			    	icon: 'images/cancel.png',
			        tooltip: '删除',
			        handler: function(grid, rowIndex, colIndex){
			        	//选中一行的数据
		            	var data = self.store.getAt(rowIndex).data;   
			            //Ext.getCmp('content_frame').removeAll();
		            	self.store.removeAt(rowIndex);
		            }
		   		}
		    ]}
		];*/
		this.loadMask =true;
		this.store = store;
		
		this.callParent(); 
	}
});


/**
 * 显示保存成功的人员名称
 */
function showSavedPersonStore(){
	var form=Ext.getCmp('identifypersoninfoform').getForm();
	//未保存的信息 setValue(msg.failure);
	var result=form.findField('savePersonName').getValue();
	var count=form.findField('saveCount').getValue();
	//显示详细履历和活动情况的window
	Ext.define('showSavedPersonStoreWin',{
		height:500,
		width:500,
		title:'入库人员信息，成功'+count+'条',
		autoScroll:true,
		buttonAlign:"center",
		layout:'fit',
		border:false,
		//maximizable:true,
		extend:'Ushine.win.Window',
		constructor:function(config){
			var self=this;
			//格式化日期
			var date=Ext.util.Format.date(new Date(), 'YmdHis');
			this.items=[{
				xtype:'panel',
				autoScroll:true,
				border:false,
				html:result
			}];
			this.buttons=[
				Ext.create('Ushine.buttons.Button', {
					text: '保存',
					baseCls: 't-btn-red',
					handler:function(){
						//保存为txt
						Ext.Ajax.request({
							url:'savePersonStoreDetail.do',
							method:'post',
							params:{
								result:result,
								date:date
							},
							success:function(){
								location.href = "downloadPersonStoreDetail.do?date="+date;
								//window.open("downloadSaveVocationalStoreDetail.do?date="+date);
							},
							failure:function(){
								//console.log('保存失败')
							}
						})
					}
				}),
				Ext.create('Ushine.buttons.Button', {
	      			text: '关闭',
	   				baseCls: 't-btn-yellow',
	   				handler:function(){
   						//关闭
   						self.close();
	   				}
				})
		   	];
			this.callParent();
		}
	});
	Ext.create('showSavedPersonStoreWin').show();
}

/**
 * 显示没有被保存的人员的原因说明
 */
function showNotSavedPersonStore(){
	var form=Ext.getCmp('identifypersoninfoform').getForm();
	//未保存的信息 setValue(msg.failure);
	var result=form.findField('failure').getValue();
	//显示详细履历和活动情况的window
	Ext.define('showNotSavedPersonStoreWin',{
		height:500,
		width:500,
		title:'未入库人员详情',
		autoScroll:true,
		buttonAlign:"center",
		layout:'fit',
		border:false,
		//maximizable:true,
		extend:'Ushine.win.Window',
		constructor:function(config){
			var self=this;
			//格式化日期
			var date=Ext.util.Format.date(new Date(), 'YmdHis');
			this.items=[{
				xtype:'panel',
				autoScroll:true,
				border:false,
				html:result
			}];
			this.buttons=[
				Ext.create('Ushine.buttons.Button', {
					text: '保存',
					baseCls: 't-btn-red',
					handler:function(){
						//保存为txt
						Ext.Ajax.request({
							url:'savePersonStoreDetail.do',
							method:'post',
							params:{
								result:result,
								date:date
							},
							success:function(){
								location.href = "downloadPersonStoreDetail.do?date="+date;
								//window.open("downloadSaveVocationalStoreDetail.do?date="+date);
							},
							failure:function(){
								//console.log('保存失败')
							}
						})
					}
				}),
				Ext.create('Ushine.buttons.Button', {
	      			text: '关闭',
	   				baseCls: 't-btn-yellow',
	   				handler:function(){
   						//关闭
   						self.close();
	   				}
				})
		   	];
			this.callParent();
		}
	});
	Ext.create('showNotSavedPersonStoreWin').show();
}


















