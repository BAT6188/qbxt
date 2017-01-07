/**
 * 领导讲话库
 * author：donghao
 */
Ext.define('Ushine.docInfo.LeaderTalk', {
	extend: 'Ext.panel.Panel',
	id: 'leadertalk-panel',
	region: 'center',
	title:'领导讲话库管理',
	bodyStyle: 'background-color: #ffffff; border: none; padding: 10px;',
	layout: {
		type: 'vbox',
		align : 'stretch',
		pack  : 'start'
	},
	constructor: function() {
		var self = this;
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
					    	   //width:120,
					    	   btnText: '新增讲话', 
					    	   baseCls: 't-btn-red',
					    	   handler: function() {
					    	   	   //新增领导讲话
					    		   self.saveLeaderTalk();
					    	   }
					       }),
					        Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'updateBtn',
					    	   btnText: '修改讲话',
					    	   //width:120,
					    	   handler: function () {
					    	   		//修改
					    	   		var grid=self.getComponent(1);
					    	   		//
					    	   		if (grid.getSelectionModel().hasSelection()) {
										var record = grid.getSelectionModel().getSelection();
										if(record.length>1){
											Ext.create('Ushine.utils.Msg').onInfo("请最多选择一行数据");
										}else{
											self.updateLeaderTalk(record);
										}
									} else {
										Ext.create('Ushine.utils.Msg').onInfo("请至少选择一行数据");
									}
					    	   }
					       }),
					       Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'delBtn',
					    	   btnText: '删除讲话',
					    	   //width:120,
					    	   handler: function () {
					    		   //删除讲话
					    	   	   var grid=self.getComponent(1);
					    	   	   if (grid.getSelectionModel().hasSelection()) {
										// 允许多行
										var record = grid.getSelectionModel().getSelection();
										var ids = [];
										for (var i = 0; i < record.length; i++) {
											ids.push(record[i].get('id'));
										}
										Ext.Msg.confirm("提示", "确定删除吗?",
											function(btn) {
												if (btn == 'yes') {
													self.delLeaderTalk(ids);
												}
										})
									} else {
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
	            		        {"text":"类别", "value":"infoType"},
	            		        {"text":"标题", "value":"title"},
	            		        {"text":"建立时间", "value":"time"},
	            		        {"text":"会议名称", "value":"meetingName"},
	            		        {"text":"密级", "value":"secretRank"},
	            		        {"text":"内容", "value":"centent"}
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
		                           		self.findLeaderTalk();
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
			        	text : '查询文档',
			        	style:"margin-left:20px;",
			        	width:100,
			        	labelWidth: 60,
			        	height:22,
			        	id : 'search-Button',
			        	baseCls: 't-btn-red',
			        	handler:function(){
			        		//查询文档
			        		self.findLeaderTalk();
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
		//领导讲话gridpanel
		Ext.create('Ushine.docInfo.LeaderTalkGridPanel')
		];	
		this.callParent();		
	},
	
	//新增领导讲话
	saveLeaderTalk:function(){
		Ext.create('SaveLeaderTalkWin').show();
	},
	
	//重置值
	resetValue:function(form,record){
		var self=this;
		form.findField('id').setValue(record[0].get('id'));
		form.findField('meetingName').setValue(self.replaceString(record[0].get('meetingName')));
		form.findField('title').setValue(self.replaceString(record[0].get('title')));
		form.findField('time').setValue(self.replaceString(record[0].get('time')).substring(0,10));
		//form.findField('centent').setValue(self.replaceString(record[0].get('centent')));
		//保留格式
		form.findField('centent').setValue(record[0].get('centent'));
		form.findField('secretRank').setValue(self.replaceString(record[0].get('secretRank')));
		form.findField('infoType').setValue(self.replaceString(record[0].get('infoType')));
		form.findField('involvedInTheField').setValue(self.replaceString(record[0].get('involvedInTheField')));
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
	//修改
	updateLeaderTalk:function(record){
		//console.log(this);
		var win=Ext.create('UpdateLeaderTalkWin',{
			record:record
		});
		win.show();
		var form=win.getComponent(0).getForm();
		//赋值
		var self=this;
		self.resetValue(form,record);
		Ext.getCmp('resetLeaderTalk').addListener('click',function(){
			self.resetValue(form,record);
		})
	},
	//删除
	//id数组
	delLeaderTalk:function(ids){
		var self=this;
		var loadMask=new Ext.LoadMask(self,{
			msg:'正在删除领导讲话,请耐心等待……'
		});
		loadMask.show();
		Ext.Ajax.request({
			method:'post',
			params:{
				ids:ids
			},
			url:'delLeadSpeakStore.do',
			success:function(response){
				loadMask.hide();
				var obj = Ext.JSON.decode(response.responseText);
				Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				// 刷新数据
				self.getComponent(1).getStore().reload();
				//self.findLeaderTalk();
			},
			failure:function(){
				loadMask.hide();
				Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败");
			}
		})
	},
	//查询
	findLeaderTalk:function(){
		this.remove('d-leadertalk-grid');
		this.add(Ext.create('Ushine.docInfo.LeaderTalkGridPanel'));
	}
});


//定义一个SaveLeaderTalkForm表单
//新增
Ext.define('SaveLeaderTalkForm',{
	extend:'Ext.form.Panel',
	//id:'saveLeaderTalk-formpanel',
	layout:'vbox',
    bodyPadding: 5,
    margin:0,
    border: false,
	baseCls: 'form-body',
	buttonAlign:"center",
	constructor:function(){
		var self=this;
		//产生随机数
		var sjnum=parseInt(Math.random()*1000);
		this.items=[{
				layout:'hbox',
	            bodyPadding: 5,
	            margin:0,
	            border: false,
				buttonAlign:"center",
				items:[{
					fieldLabel:'会议名称',
					labelStyle:'color:red;',
					allowBlank:false,
					xtype : 'textfield',
					emptyText:'请输入会议名称',
					blankText:'此选项不能为空',
					width: 250,
					labelAlign : 'right',
					labelWidth : 60,
					//height:22,
					name : 'meetingName'
				},{
					fieldLabel:'会议标题',
					labelStyle:'color:red;',
					allowBlank:false,
					xtype : 'textfield',
					labelAlign : 'right',
					emptyText:'请输入会议标题',
					blankText:'此选项不能为空',
					width: 285,
					labelWidth : 95,
					//height:22,
					name : 'title'
				}]
			},{
				layout:'hbox',
                //height:35,
	            bodyPadding: 5,
	            margin:0,
	            border: false,
				buttonAlign:"center",
				items:[{
					fieldLabel:'密级',
					labelStyle:'color:red;',
					allowBlank:false,
					xtype:'combo',
					emptyText:'请选择类别',
					blankText:'此选项不能为空',
					width: 250,
					labelAlign : 'right',
					labelWidth : 60,
					//height:22,
					name : 'secretRank',
					allowNegative: false,
            		editable: false,
            		hiddenName: 'colnum',
            		//name:'colnum',
            		valueField: 'value',
            		store:Ext.create('Ext.data.Store', {
            		    fields: ['text', 'value'],
            		    data : [
            		        //包括秘密，机密，绝密
            		    	{"text":"无", "value":"无"},
            		        {"text":"秘密", "value":"秘密"},
            		        {"text":"机密", "value":"机密"},
            		        {"text":"绝密", "value":"绝密"}
            		    ]
            		})
				},{
					fieldLabel:'类别',
					labelStyle:'color:red;',
					allowBlank:false,
					xtype:'combo',
					emptyText:'请选择类别',
					blankText:'此选项不能为空',
					width: 285,
					labelAlign : 'right',
					labelWidth : 95,
					//height:22,
					name : 'infoType',
					allowNegative: false,
            		editable: false,
            		valueField: 'value',
            		store:Ext.create('Ext.data.Store', {
            			//请求后台领导讲话类别
            		    fields: ['text', 'value'],
            		   	proxy:{
            		   		type:'ajax',
            		   		url:'getleadspeakstoretype.do',
            		   		reader:{
            		   			type:'json'
            		   		}
            		   	}
            		})
				}]
			},{
				layout:'hbox',
	            bodyPadding: 5,
	            border: false,
				buttonAlign:"center",
				items:[{
					fieldLabel:'时间',
					labelStyle:'color:red;',
					width: 250,
					labelAlign : 'right',
					emptyText:'请输入时间',
					labelWidth :60,
					name : 'time',
					format: 'Y-m-d', 
					xtype: 'datefield',
					maxValue: new Date(),
					value:new Date()
				},{
					fieldLabel:'涉及领域',
					labelStyle:'color:red;',
					allowBlank:false,
					xtype:'combo',
					emptyText:'请选择涉及领域',
					blankText:'此选项不能为空',
					width: 285,
					labelAlign : 'right',
					labelWidth : 95,
					//height:22,
					name : 'involvedInTheField',
					allowNegative: false,
            		editable: false,
            		valueField: 'value',
            		store:Ext.create('Ext.data.Store', {
            			//请求后台领导讲话类别
            		    fields: ['text', 'value'],
            		   	proxy:{
            		   		type:'ajax',
            		   		url:'getInvolvedInTheFieldType.do',
            		   		reader:{
            		   			type:'json'
            		   		}
            		   	}
            		})
				}]
			},{
				layout:'hbox',
	            bodyPadding: 5,
	            //margin:'15 0 0 0',
	            border: false,
				buttonAlign:"center",
				items:[{
					fieldLabel:'内容',
					xtype : 'htmleditor',
					enableFont:false,
					labelStyle:'color:black;',
					width: 534,
					labelAlign : 'right',
					emptyText:'请输入内容',
					labelWidth :60,
					height:210,
					name : 'centent'
				}]
			},{
				xtype:'filefield',
				margin:'5 0 0 64',
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
								  url:'leadSpeakStoreFileUpload.do?number='+sjnum,
		    					  method:'POST',
		    					  waitMsg:'文件上传中',
		    					  timeout:1000*60,
		    					  success:function(form, action) {
		    		    			   var leadSpeakStoreTemp = Ext.getCmp('leadSpeakStoreTempFileId');
		    		    			   //LeadSpeakStoreFileGridPanel
		    		    			   leadSpeakStoreTemp.remove(Ext.getCmp('LeadSpeakStoreFileGridPanel'));
		    		    			   leadSpeakStoreTemp.add(new Ushine.docInfo.LeadSpeakStoreFileGridPanel(sjnum));
		    		    			   //console.log(Ext.getCmp('LeadSpeakStoreFileGridPanel'))
		    					  },
		    					  // 提交失败的回调函数
		    					  failure : function() {
		    						  Ext.create('Ushine.utils.Msg').onInfo("服务器出现错误请稍后再试!");
		    					  }
							});
						}else{
							Ext.create('Ushine.utils.Msg').onInfo("请填写完整信息");
						}
					}
				}
			},{
				xtype:'panel',
				border:false,
				height:150,
				width:'100%',
				layout: {
			        type: 'hbox',
			        align: 'stretch'
			    },
				margin:'0 0 0 8',
				//id:'personStoreTempFilesId',
				items:[{
					id:'leadSpeakStoreTempFileId',
					//存放附件的gridpanel
					margin:'5 15 0 60',
					flex:1,
					height:'100%',
					layout:'fit',
					border:false,
					items:Ext.create('Ushine.docInfo.LeadSpeakStoreFileGridPanel')
				}]
			}
		];
		this.buttons=[
		  		Ext.create('Ushine.buttons.Button', {
			   		text: '确定',
			   		baseCls: 't-btn-red',
			   		handler: function () {
			   			//提交给后台处理
			   			//新增
			   			var saveForm=self.getForm();
			   			if(saveForm.isValid()){
			   				//先判断是否存在
			   				Ext.Ajax.timeout=60000;
			   				Ext.Ajax.request({
			   					//先判断是否有了
			   					url:"hasStoreByMeetingName.do",
			   					method:'post',
			   					params:{
			   						meetingName:saveForm.findField('meetingName').getValue()
			   					},
			   					success:function(response){
			   						var obj=Ext.JSON.decode(response.responseText);
			   						//console.log(obj);
			   						if(obj.msg=='exist'){
			   							Ext.create('Ushine.utils.Msg').onQuest("文档已经存在，是否仍添加",function(btn){
			   								//console.log(btn);
			   								if(btn=='yes'){
			   									//仍添加
			   									self.saveLeadspeakStore(self,saveForm,sjnum);
			   								}
			   							});
			   						}else{
			   							//不存在
			   							self.saveLeadspeakStore(self,saveForm,sjnum);
			   						}
			   					},
			   					failure:function(){
			   						Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败");
			   					}
			   				})
			   			}else{
			   				Ext.create('Ushine.utils.Msg').onInfo("请填写完整信息");
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
	saveLeadspeakStore:function(self,saveForm,sjnum){
		//添加遮罩	   				
		var loadMask=new Ext.LoadMask(self,{
			msg:'正在添加领导讲话,请耐心等待……'
		});
		loadMask.show();
		Ext.Ajax.timeout=1000*60;
		Ext.Ajax.request({
			url:'saveLeadSpeakStore.do',
			method:'post',
			params:{
				number:sjnum,
				meetingName:saveForm.findField('meetingName').getValue(),
				title:saveForm.findField('title').getValue(),
				time:saveForm.findField('time').getValue(),
				centent:saveForm.findField('centent').getValue(),
				secretRank:saveForm.findField('secretRank').getValue(),
				infoType:saveForm.findField('infoType').getValue(),
				involvedInTheField:saveForm.findField('involvedInTheField').getValue(),
			},
			success:function(response){
				var obj=Ext.JSON.decode(response.responseText);
				Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				//关闭
				self.findParentByType('window').close();
				//刷新数据
				Ext.getCmp("leadertalkgridpanel").getStore().reload();
				//清除选择
				Ext.getCmp("leadertalkgridpanel").getSelectionModel().clearSelections();
			},
			failure:function(response){
				Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败");
				//console.log(response.responseText);
			}
		})
		/*saveForm.submit({
			submitEmptyText:false,
		});*/
	}
});

//定义一个领导讲话的SaveLeaderTalkWin
Ext.define('SaveLeaderTalkWin',{
	extend:'Ushine.win.Window',
	title : "添加领导讲话",
	modal : true,
	layout : 'fit',
	border : false,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:615,
	width:575,
	constructor:function(){
		var self=this;
		
		this.items=[
			Ext.create('SaveLeaderTalkForm')
		];
		this.callParent();
	}
});


//定义一个UpdateLeaderTalkForm表单
//修改领导讲话form
Ext.define('UpdateLeaderTalkForm',{
	extend:'Ext.form.Panel',
	layout:'vbox',
    bodyPadding: 5,
    margin:0,
    border: false,
	baseCls: 'form-body',
	buttonAlign:"center",
	constructor:function(config){
		var self=this;
		this.record=config.record;
		var sjnum=parseInt(Math.random()*1000);
		this.items=[{
				layout:'hbox',
	            bodyPadding: 5,
	            margin:0,
	            border: false,
				buttonAlign:"center",
				items:[{
					xtype:'hiddenfield',
					//隐藏域
					name:'id'
				},{
					fieldLabel:'会议名称',
					labelStyle:'color:red;',
					allowBlank:false,
					xtype : 'textfield',
					emptyText:'请输入会议名称',
					blankText:'此选项不能为空',
					width: 250,
					labelAlign : 'right',
					labelWidth : 60,
					name : 'meetingName'
				},{
					fieldLabel:'会议标题',
					labelStyle:'color:red;',
					allowBlank:false,
					xtype : 'textfield',
					labelAlign : 'right',
					emptyText:'请输入会议标题',
					blankText:'此选项不能为空',
					width: 285,
					labelWidth : 95,
					//height:22,
					name : 'title'
				}]
			},{
				layout:'hbox',
                //height:35,
	            bodyPadding: 5,
	            margin:0,
	            border: false,
				buttonAlign:"center",
				items:[{
					fieldLabel:'密级',
					labelStyle:'color:red;',
					allowBlank:false,
					xtype:'combo',
					emptyText:'请选择类别',
					blankText:'此选项不能为空',
					width: 250,
					labelAlign : 'right',
					labelWidth : 60,
					//height:22,
					name : 'secretRank',
					allowNegative: false,
            		editable: false,
            		valueField: 'value',
            		store:Ext.create('Ext.data.Store', {
            		    fields: ['text', 'value'],
            		    data : [
            		        //包括秘密，机密，绝密
            		        {"text":"无", "value":"无"},
            		        {"text":"秘密", "value":"秘密"},
            		        {"text":"机密", "value":"机密"},
            		        {"text":"绝密", "value":"绝密"}
            		    ]
            		})
				},{
					fieldLabel:'类别',
					labelStyle:'color:red;',
					allowBlank:false,
					xtype:'combo',
					emptyText:'请选择类别',
					blankText:'此选项不能为空',
					width: 285,
					labelAlign : 'right',
					labelWidth : 95,
					//height:22,
					name : 'infoType',
					allowNegative: false,
            		editable: false,
            		valueField: 'text',
            		store:Ext.create('Ext.data.Store', {
            			//请求后台领导讲话类别
            		    fields: ['text', 'value'],
            		   	proxy:{
            		   		type:'ajax',
            		   		url:'getleadspeakstoretype.do',
            		   		reader:{
            		   			type:'json'
            		   		}
            		   	}
            		})
				}]
			},{
				layout:'hbox',
	            bodyPadding: 5,
	            border: false,
				buttonAlign:"center",
				items:[{
					fieldLabel:'时间',
					labelStyle:'color:red;',
					width: 250,
					labelAlign : 'right',
					emptyText:'请输入时间',
					labelWidth :60,
					name : 'time',
					format: 'Y-m-d', 
					xtype: 'datefield',
					maxValue: new Date()
					
				},{
					fieldLabel:'涉及领域',
					labelStyle:'color:red;',
					allowBlank:false,
					xtype:'combo',
					emptyText:'请选择涉及领域',
					blankText:'此选项不能为空',
					width: 285,
					labelAlign : 'right',
					labelWidth : 95,
					//height:22,
					name : 'involvedInTheField',
					allowNegative: false,
            		editable: false,
            		valueField: 'text',
            		store:Ext.create('Ext.data.Store', {
            			//请求后台领导讲话类别
            		    fields: ['text', 'value'],
            		   	proxy:{
            		   		type:'ajax',
            		   		url:'getInvolvedInTheFieldType.do',
            		   		reader:{
            		   			type:'json'
            		   		}
            		   	}
            		})
				}]
			},{
				layout:'hbox',
	            bodyPadding: 5,
	            //margin:'15 0 0 0',
	            border: false,
				buttonAlign:"center",
				items:[{
					/*fieldLabel:'内容',
					xtype : 'htmleditor',
					enableFont:false,
					labelStyle:'color:black;',
					width: 534,
					labelAlign : 'right',
					emptyText:'请输入内容',
					labelWidth :60,
					height:210,
					name : 'centent'*/
					xtype : 'displayfield',
					fieldLabel:'内容',
					labelWidth :60,
					labelAlign : 'right',
					value:"<a href='javascript:void(0)' onclick=editLeaderTalkContent()>"
						+"点击修改领导讲话内容<a/>"
				},{
					//隐藏
					xtype : 'hidden',
					name : 'centent'
				}]
			},{
				xtype:'filefield',
				margin:'5 0 0 64',
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
								  url:'leadSpeakStoreFileUpload.do?number='+sjnum,
		    					  method:'POST',
		    					  waitMsg:'文件上传中',
		    					  timeout:1000*60,
		    					  success:function(form, action) {
		    		    			   var leadSpeakStoreTemp = Ext.getCmp('leadSpeakStoreTempFileId');
		    		    			   //LeadSpeakStoreFileGridPanel
		    		    			   leadSpeakStoreTemp.remove(Ext.getCmp('LeadSpeakStoreFileGridPanel'));
		    		    			   leadSpeakStoreTemp.add(new Ushine.docInfo.LeadSpeakStoreFileGridPanel(sjnum,self.record));
		    		    			   //console.log(Ext.getCmp('LeadSpeakStoreFileGridPanel'))
		    					  },
		    					  // 提交失败的回调函数
		    					  failure : function() {
		    						  Ext.create('Ushine.utils.Msg').onInfo("服务器出现错误请稍后再试!");
		    					  }
							});
						}else{
							Ext.create('Ushine.utils.Msg').onInfo("请填写完整信息");
						}
					}
				}
			},{
				xtype:'panel',
				border:false,
				height:150,
				width:'100%',
				layout: {
			        type: 'hbox',
			        align: 'stretch'
			    },
				margin:'0 0 0 8',
				//id:'personStoreTempFilesId',
				items:[{
					id:'leadSpeakStoreTempFileId',
					//存放附件的gridpanel
					margin:'5 15 0 60',
					flex:1,
					height:'100%',
					layout:'fit',
					border:false,
					//sjnum,self.record
					items:new Ushine.docInfo.LeadSpeakStoreFileGridPanel(sjnum,self.record)
				}]
			}
		];
		this.buttons=[
	  		Ext.create('Ushine.buttons.Button', {
		   		text: '确定',
		   		baseCls: 't-btn-red',
		   		handler: function () {
		   			//提交给后台处理
		   			//修改
		   			var updateForm=self.getForm();
		   			if(updateForm.isValid()){
		   				//添加遮罩	   				
		   				var loadMask=new Ext.LoadMask(self,{
		   					msg:'正在修改领导讲话,请耐心等待……'
		   				});
		   				loadMask.show();
		   				/*updateForm.submit({
		   					url:'updateLeadSpeakStore.do',
		   					method:'post',
		   					submitEmptyText:false,
		   					success:function(form,action){
		   						var obj=Ext.JSON.decode(action.response.responseText);
								Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
								//关闭
								self.findParentByType('window').close();
								//刷新数据
								Ext.getCmp("leadertalkgridpanel").getStore().reload();
								//清除选择
								Ext.getCmp("leadertalkgridpanel").getSelectionModel().clearSelections();
		   					},
		   					failure:function(form,actio){
		   						Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败");
		   					}
		   				});*/
		   				//获得附件里的文件名
		   				var fileStore=Ext.getCmp('LeadSpeakStoreFileGridPanel').getStore();
		   				var arr=new Array();
		   				fileStore.each(function(record){
	   						arr.push(record.data.filePath);
	   					});
		   				//不用进行json编码
	   					//var attaches=Ext.JSON.encode(arr);
		   				//修改
		   				Ext.Ajax.timeout=1000*60;
		   				Ext.Ajax.request({
		   					url:'updateLeadSpeakStore.do',
		   					method:'post',
		   					params:{
		   						id:updateForm.findField('id').getValue(),
		   						number:sjnum,
		   						attaches:arr,
		   						meetingName:updateForm.findField('meetingName').getValue(),
		   						title:updateForm.findField('title').getValue(),
		   						time:updateForm.findField('time').getValue(),
		   						centent:updateForm.findField('centent').getValue(),
		   						secretRank:updateForm.findField('secretRank').getValue(),
		   						infoType:updateForm.findField('infoType').getValue(),
		   						involvedInTheField:updateForm.findField('involvedInTheField').getValue(),
		   					},
		   					success:function(response){
		   						var obj=Ext.JSON.decode(response.responseText);
								Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
								//关闭
								self.findParentByType('window').close();
								//刷新数据
								Ext.getCmp("leadertalkgridpanel").getStore().reload();
								//清除选择
								Ext.getCmp("leadertalkgridpanel").getSelectionModel().clearSelections();
		   					},
		   					failure:function(resoponse){
		   						Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败");
		   					}
		   				})
		   			}else{
		   				Ext.create('Ushine.utils.Msg').onInfo("请填写完整信息");
		   			}
		   		}
		   	}),Ext.create('Ushine.buttons.Button', {
		   		text: '重置',
		   		margin:'0 0 0 35',
		   		baseCls: 't-btn-yellow',
		   		id:'resetLeaderTalk'
		   })
	  	];
	  	this.callParent();
	}
});

//定义一个领导讲话的UpdateLeaderTalkWin
Ext.define('UpdateLeaderTalkWin',{
	extend:'Ushine.win.Window',
	title : "修改领导讲话",
	modal : true,
	layout : 'fit',
	id:'updateleadertalk-win',
	border : false,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:430,
	width:580,
	constructor:function(config){
		var self=this;
		this.items=[
			Ext.create('UpdateLeaderTalkForm',{
				record:config.record
			})
		];
		this.callParent();
	}
});

//修改领导讲话内容
function editLeaderTalkContent(){
	var win=Ext.getCmp('updateleadertalk-win');
	var form=win.getComponent(0).getForm();
	//得到内容表单
	var field=form.findField('centent');
	var value=form.findField('centent').getValue();
	Ext.define('EditLeaderTalkContentWin',{
		height:800,
		width:1000,
		extend:'Ext.window.Window',
		layout : 'fit',
		border : false,
		buttonAlign:"center",
		title:'修改文档原文内容',
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
	Ext.create('EditLeaderTalkContentWin').show();
}