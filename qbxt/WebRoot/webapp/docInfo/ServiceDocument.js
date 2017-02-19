/**
 * 业务文档
 * author：donghao
 */

Ext.define('Ushine.docInfo.ServiceDocument', {
	extend: 'Ext.panel.Panel',
	id: 'servicedocument-panel',
	region: 'center',
	title:'业务文档库管理',
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
							/*Ext.create('Ushine.buttons.IconButton', {
								   border: false,
								   hidden:true,
								   id: 'importSericeDoc',
								   //width:80,
								   btnText: '上传多个文档', 
								   baseCls: 't-btn-red',
								   handler: function() {
								   	   //弹出导入文档
									   self.importMultiServiceDoc();
								   }
							}),*/
							
					 		/*Ext.create('Ushine.buttons.IconButton', {
					    	   border: false,
					    	   id: 'importNewBtn',
					    	   //width:80,
					    	   btnText: '导入文档', 
					    	   baseCls: 't-btn-red',
					    	   handler: function() {
					    	   	   //弹出导入业务文档
					    	   	   self.importServiceDoc();
					    	   }
					       }),
					       Ext.create('Ushine.buttons.IconButton', {
					    	   border: false,
					    	   id: 'uploadNewBtn',
					    	   //width:80,
					    	   btnText: '上传文档', 
					    	   baseCls: 't-btn-red',
					    	   handler: function() {
					    	   	   self.uploadServiceDoc();
					    	   }
					       }),*/
					       Ext.create('Ushine.buttons.IconButton', {
					    	   border: false,
					    	   id: 'createNewBtn',
					    	   //width:80,
					    	   btnText: '新增文档', 
					    	   baseCls: 't-btn-red',
					    	   handler: function() {
					    	   	   //弹出新增业务文档
					    		   self.saveServiceDoc();
					    	   }
					       }),
					        Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'updateBtn',
					    	   btnText: '修改文档',
					    	   //width:80,
					    	   handler: function () {
					    		   //修改
				    	  		   var servicDocGrid=self.getComponent(1);					    	   	
							   	   if(servicDocGrid.getSelectionModel().hasSelection()){
						   	   			//只能选一行
							   	   		var record=servicDocGrid.getSelectionModel().getSelection();
							   	   		if(record.length>1){
						   	   				Ext.create('Ushine.utils.Msg').onInfo("只能选择一行数据");
							   	   		}else{
						   	   				self.updateServiceDoc(record);
							   	   		}
							   	   }else{
							   	   		Ext.create('Ushine.utils.Msg').onInfo("请至少选择一行数据");
							   	   }
				    	   	  }
					       }),
					       Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'delBtn',
					    	   btnText: '删除文档',
					    	   //width:80,
					    	   handler: function () {
					    		 	  var servicDocGrid=self.getComponent(1);					    	   	
								   	   if(servicDocGrid.getSelectionModel().hasSelection()){
								   	   		//允许多行
								   	   		var record=servicDocGrid.getSelectionModel().getSelection();
								   	   		var serviceDocStoreIds=[];
								   	   		for(var i=0;i<record.length;i++){
								   	   			serviceDocStoreIds.push(record[i].get('id'));
								   	   		}
								   	   		Ext.Msg.confirm("提示","确定删除选中的文档吗?",function(btn){
								   	   			if (btn == 'yes') {
								   	   			   self.delServiceDoc(self,serviceDocStoreIds);
						    				   }
								   	   		})
								   	   }else{
								   	   		Ext.create('Ushine.utils.Msg').onInfo("请至少选择一行数据");
								   	   }
						    	   }
					       })]
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
	            		        {"text":"文档类别", "value":"infoType"},
	            		        {"text":"文档名称", "value":"docName"},
	            		        {"text":"期刊号", "value":"docNumber"},
	            		        {"text":"建立时间", "value":"time"},
	            		        {"text":"原文", "value":"theOriginal"},
	            		        {"text":"涉及领域", "value":"involvedInTheField"}
	            		    ]
	            		}),
	            		//默认全字段
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
		                        	self.findDocByProperty();
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
			        	text : '查询文档',
			        	style:"margin-left:20px;",
			        	width:100,
			        	labelWidth: 60,
			        	height:22,
			        	id : 'search-Button',
			        	baseCls: 't-btn-red',
			        	handler:function(){
			        		//查询文档
			        		self.findDocByProperty();
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
			//业务文档gridpanel
			Ext.create('Ushine.docInfo.ServiceDocGridPanel')
		];	
			this.callParent();		
	},
	//导入文档
	//文件夹方式
	importServiceDoc:function(){
		Ext.create('ImportServiceDocumentWin').show();
	},
	//单文件上传方式
	uploadServiceDoc:function(){
		Ext.create('UploadServiceDocumentWin').show();
	},
	//上传多个
	importMultiServiceDoc:function(){
		//ImportMultiServiceDocumentWin
		//Ext.create('ImportMultiServiceDocumentWin').show();
		Ext.define('ImportMultiServiceDocumentWin',{
			height:400,
			width:500,
			extend:'Ext.window.Window',
			layout : 'fit',
			border : false,
			buttonAlign:"center",
			title:'上传多个文档',
			constructor:function(){
				var self=this;
				this.items=[{
					xtype:'form',
					enableFont:false,
					items:[{
						xtype:'panel',
						html:"<div id='uploadForm'><input id='file' type='file' multiple='multiple'/>" +
								"<button id='upload' type='button' onclick='muiltiFileUpload()'>upload</button></div>"
					}/*{
						xtype:'filefield',
						name:'muiltifileupload',
						buttonOnly:false,
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
			        				  accept:'file/*',
			        				  multiple:'multiple'
			        			  });
					  		},
							change:function(){
								//往后台上传附件
								var form=self.down('form').getForm();
								//文件列表
								//console.log(document.getElementById('filefield-1074-button-fileInputEl').files);
								var files=document.getElementById('filefield-1074-button-fileInputEl').files;
								var map=[];
								$(files).each(function(index,element){
									//map.push(element.lastModified+"-"+element.name);
									map.push({lastModified:element.lastModified,name:element.name});
									element.name=element.lastModified+element.name;
								});
								//console.log(Ext.JSON.encode(map));
								//把object对象转成json字符串,后台解析
								var string=Ext.JSON.encode(map);
								if(form.isValid()){
									form.submit({
										  url:'uploadmuiltifiles.do?map='+string+'&&number='+parseInt(Math.random()*1000),
				    					  method:'POST',
				    					  waitMsg:'文件上传中',
				    					  timeout:1000*60,
				    					  success:function(form, action) {
				    		    			  console.log('success');
				    					  },
				    					  // 提交失败的回调函数
				    					  failure : function() {
				    						  console.log('failure');
				    						  //Ext.create('Ushine.utils.Msg').onInfo("服务器出现错误请稍后再试!");
				    					  }
									});
								}else{
									Ext.create('Ushine.utils.Msg').onInfo("请填写完整信息");
								}
							}
						}
					}*/]
				}];
				this.buttons=[
					Ext.create('Ushine.buttons.Button', {
		      			text: '确定',
		      	   		baseCls: 't-btn-red',
		      	   		handler:function(){
		      	   			//修改
		      	   			htmlField.setValue(self.getComponent(0).getValue());
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
		Ext.create('ImportMultiServiceDocumentWin').show();
	},
	//新增业务文档
	saveServiceDoc:function(){
			Ext.create('SaveServiceDocumentWin').show();
	},
	//重置
	resetValue:function(form,record){
		var self=this;
		form.findField('id').setValue(record[0].get('id'));
		form.findField('docNumber').setValue(self.replaceString(record[0].get('docNumber')));
		form.findField('docName').setValue(self.replaceString(record[0].get('docName')));
		form.findField('time').setValue(self.replaceString(record[0].get('time')).substring(0,10));
		form.findField('infoType').setValue(self.replaceString(record[0].get('infoType')));
		form.findField('involvedInTheField').setValue(self.replaceString(record[0].get('involvedInTheField')));
		//保留格式
		form.findField('theOriginal').setValue(record[0].get('theOriginal'));
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
	//修改业务文档
	updateServiceDoc:function(record){
		var self=this;
		var win=Ext.create('UpdateServiceDocumentWin',{
			record:record
		});
		win.show();
		//console.log(record[0]);
		var form=win.getComponent(0).getForm();
		//重置
		self.resetValue(form,record);
		Ext.getCmp('resetForeignDocument').addListener('click',function(){
			self.resetValue(form,record);
		})
	},
	//查询业务文档
	findDocByProperty:function(){
		this.remove('d_servicedoc_grid');
		this.add(Ext.create('Ushine.docInfo.ServiceDocGridPanel'));
	},
	//删除业务文档
	delServiceDoc:function(self,serviceDocStoreIds){
		//请求超时时间
		Ext.Ajax.timeout=60000;
		var loadMask=new Ext.LoadMask(self,{
			msg:'正在删除业务文档,请耐心等待……'
		});
		loadMask.show();
		Ext.Ajax.request({
			url:'delVocationalWorkStore.do',
			params:{
				serviceDocStoreIds:serviceDocStoreIds
			},
			success:function(response){
				var obj=Ext.JSON.decode(response.responseText);
				//刷新数据
				Ext.getCmp("servicedocgridpanel").getStore().reload();
				loadMask.hide();
				Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
   			},
			failure:function(){
				loadMask.hide();
				Ext.create('Ushine.utils.Msg').onInfo("提示,请求后台服务失败");
			}
		})
	}
});

//定义一个SaveServiceDocumentForm表单
//把原来写在内部提到外部来
Ext.define('SaveServiceDocumentForm',{
	extend:'Ext.form.Panel',
	//id:'saveservicedocument-formpanel',
	layout:'vbox',
    bodyPadding: 5,
    margin:0,
    border: false,
	baseCls: 'form-body',
	buttonAlign:"center",
	constructor:function(){
		var self=this;
		var sjnum=parseInt(Math.random()*1000);
		this.items=[{
		layout:'hbox',
        bodyPadding: 5,
        border: false,
		buttonAlign:"center",
		items:[{
			fieldLabel:'文档名称',
			labelStyle:'color:red;',
			allowBlank:false,
			xtype : 'textfield',
			emptyText:'请输入文档名称',
			blankText:'此选项不能为空',
			width: 250,
			labelAlign : 'right',
			labelWidth : 60,
			//height:22,
			name : 'docName'
		},{
			fieldLabel:'期刊号',
			labelStyle:'color:red;',
			xtype : 'textfield',
			labelAlign : 'right',
			emptyText:'请输入期刊号',
			blankText:'此选项不能为空',
			width: 250,
			labelWidth : 60,
			//height:22,
			name : 'docNumber'
		}]
	},{
		layout:'hbox',
        bodyPadding: 5,
        margin:0,
        border: false,
		buttonAlign:"center",
		items:[{
			fieldLabel:'文档类别',
			labelStyle:'color:red;',
			allowBlank:false,
			xtype:'combo',
			emptyText:'请选择类别',
			blankText:'此选项不能为空',
			width: 250,
			labelAlign : 'right',
			labelWidth : 60,
			name : 'infoType',
			allowNegative: false,
    		editable: false,
    		hiddenName: 'colnum',
    		valueField: 'value',
			//读取类别表中的业务文档类别    		
    		store:Ext.create('Ext.data.Store',{
    			fields: ['text', 'value'],
    			autoLoad:true,
    			proxy:{
    				type:'ajax',
    				url:'getvocationalworkstoretype.do',
    				reader:{
    					type:'json'
    				}
    			}
    		})
		},{
			fieldLabel:'时间',
			labelStyle:'color:red;',
			allowBlank:false,
			labelAlign : 'right',
			emptyText:'请输入时间',
			blankText:'此选项不能为空',
			width: 250,
			labelWidth : 60,
			name : 'time',
			format: 'Y-m-d', 
			xtype: 'datefield',
			maxValue: new Date(),
			//height: 22,
			value: new Date()
		}]
	},{
		layout:'hbox',
        bodyPadding: 5,
        margin:0,
        border: false,
		buttonAlign:"center",
		items:[{
			fieldLabel:'涉及领域',
			labelStyle:'color:red;',
			allowBlank:false,
			xtype:'combo',
			emptyText:'请选择涉及领域',
			blankText:'此选项不能为空',
			width: 250,
			labelAlign : 'right',
			labelWidth : 60,
			name : 'involvedInTheField',
			allowNegative: false,
    		editable: false,
    		hiddenName: 'colnum',
    		valueField: 'value',
			//读取类别表中的业务文档类别    		
    		store:Ext.create('Ext.data.Store',{
    			fields: ['text', 'value'],
    			autoLoad:true,
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
        border: false,
        margin:0,
		buttonAlign:"center",
		items:[{
			fieldLabel:'原文',
			xtype : 'htmleditor',
			labelStyle:'color:black;',
			width: 504,
			labelAlign : 'right',
			emptyText:'请输入原文',
			labelWidth :60,
			height:210,
			name : 'theOriginal',
			enableFont:false
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
							  url:'serviceDocStoreFileUpload.do?number='+sjnum,
	    					  method:'POST',
	    					  waitMsg:'文件上传中',
	    					  timeout:1000*60,
	    					  success:function(form, action) {
	    		    			   var sericeDocStoreTemp = Ext.getCmp('seriveDocStoreTempFileId');
	    		    			   //LeadSpeakStoreFileGridPanel
	    		    			   sericeDocStoreTemp.remove(Ext.getCmp('ServiceDocStoreFileGridPanel'));
	    		    			   sericeDocStoreTemp.add(new Ushine.docInfo.ServiceDocStoreFileGridPanel(sjnum));
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
			height:130,
			width:'100%',
			layout: {
		        type: 'hbox',
		        align: 'stretch'
		    },
			margin:'0 0 0 8',
			//id:'personStoreTempFilesId',
			items:[{
				id:'seriveDocStoreTempFileId',
				//存放附件的gridpanel
				margin:'5 15 0 60',
				flex:1,
				height:'100%',
				layout:'fit',
				border:false,
				items:Ext.create('Ushine.docInfo.ServiceDocStoreFileGridPanel')
			}]
		}];
	  this.buttons=[
	  	Ext.create('Ushine.buttons.Button', {
	   		text: '确定',
	   		baseCls: 't-btn-red',
	   		handler: function () {
	   			//提交给后台处理
	   			var saveForm=self.getForm();
	   			if(saveForm.isValid()){
	   				Ext.Ajax.timeout=60000;
	   				Ext.Ajax.request({
	   					//先判断是否有了
	   					url:"hasVocationalStoreByDocName.do",
	   					method:'post',
	   					params:{
	   						docName:saveForm.findField("docName").getValue(),
	   					},
	   					success:function(response){
	   						var obj=Ext.JSON.decode(response.responseText);
	   						//console.log(obj);
	   						if(obj.msg=='exist'){
	   							Ext.create('Ushine.utils.Msg').onQuest("文档已经存在，是否仍添加",function(btn){
	   								//console.log(btn);
	   								if(btn=='yes'){
	   									//仍添加
	   									self.saveVocationalStore(sjnum,saveForm,self);
	   								}
	   							});
	   						}else{
	   							//不存在
	   							self.saveVocationalStore(sjnum,saveForm,self);
	   						}
	   					},
	   					failure:function(){
	   						Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败");
	   					}
	   				})
	   				/*saveForm.submit({
		   				submitEmptyText:false,
		   			});*/
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
	saveVocationalStore:function(sjnum,saveForm,self){
		//添加遮罩	
		var loadMask=new Ext.LoadMask(self,{
		msg:'正在添加业务文档,请耐心等待……'
		});
		loadMask.show();
		//设置超时时间,默认是30000毫秒;
		//设置timeout：1分钟
		Ext.Ajax.request({
			url:"saveVocationalWorkStore.do",
			method:'post',
			params:{
				number:sjnum,
				docName:saveForm.findField("docName").getValue(),
				docNumber:saveForm.findField("docNumber").getValue(),
				infoType:saveForm.findField("infoType").getValue(),
				time:saveForm.findField("time").getValue(),
				theOriginal:saveForm.findField("theOriginal").getValue(),
				involvedInTheField:saveForm.findField("involvedInTheField").getValue(),
			},
			success:function(response){
				loadMask.hide();
				var obj=Ext.JSON.decode(response.responseText);
				Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				//关闭
				self.findParentByType('window').close();
				//刷新数据
				Ext.getCmp("servicedocgridpanel").getStore().reload();
			},
			failure:function(response){
				loadMask.hide();
				Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败");
			}
		})
	}
});
//定义修改的UpdateServiceDocumentForm
Ext.define('UpdateServiceDocumentForm',{
	extend:'Ext.form.Panel',
	layout:'vbox',
    bodyPadding: 5,
    margin:'10 0 0 0',
    border: false,
	baseCls: 'form-body',
	buttonAlign:"center",
	constructor:function(config){
		var self=this;
		var sjnum=parseInt(Math.random()*1000);
		//console.log(config.record);
		this.items=[{
		layout:'hbox',
        bodyPadding: 5,
        border: false,
		buttonAlign:"center",
		items:[{
			//隐藏域
			 xtype: 'hiddenfield',
       		 name: 'id'
		},{
			fieldLabel:'文档名称',
			labelStyle:'color:red;',
			allowBlank:false,
			xtype : 'textfield',
			emptyText:'请输入文档名称',
			blankText:'此选项不能为空',
			width: 250,
			labelAlign : 'right',
			labelWidth : 60,
			//height:22,
			name : 'docName'
		},{
			fieldLabel:'期刊号',
			labelStyle:'color:red;',
			xtype : 'textfield',
			labelAlign : 'right',
			emptyText:'请输入期刊号',
			blankText:'此选项不能为空',
			width: 250,
			labelWidth : 60,
			//height:22,
			name : 'docNumber'
		}]
	},{
		layout:'hbox',
        bodyPadding: 5,
        margin:'10 0 0 0',
        border: false,
		buttonAlign:"center",
		items:[{
			fieldLabel:'类别',
			labelStyle:'color:red;',
			allowBlank:false,
			xtype:'combo',
			emptyText:'请选择类别',
			blankText:'此选项不能为空',
			width: 250,
			labelAlign : 'right',
			labelWidth : 60,
			name : 'infoType',
			allowNegative: false,
    		editable: false,
    		valueField: 'text',
    		displayField:'text',
			//读取类别表中的业务文档类别    		
    		store:Ext.create('Ext.data.Store',{
    			fields: ['text', 'value'],
    			autoLoad:true,
    			proxy:{
    				type:'ajax',
    				url:'getvocationalworkstoretype.do',
    				reader:{
    					type:'json'
    				}
    			}
    		})
		},{
			fieldLabel:'时间',
			labelStyle:'color:red;',
			allowBlank:false,
			labelAlign : 'right',
			emptyText:'请输入时间',
			blankText:'此选项不能为空',
			width: 250,
			labelWidth : 60,
			name : 'time',
			format: 'Y-m-d', 
			xtype: 'datefield',
			maxValue: new Date()
			//height: 22,
			//value:'2005-01-01'
		}]
	},{
		layout:'hbox',
        bodyPadding: 5,
        margin:0,
        border: false,
		buttonAlign:"center",
		items:[{
			fieldLabel:'涉及领域',
			labelStyle:'color:red;',
			allowBlank:false,
			xtype:'combo',
			emptyText:'请选择涉及领域',
			blankText:'此选项不能为空',
			width: 250,
			labelAlign : 'right',
			labelWidth : 60,
			name : 'involvedInTheField',
			allowNegative: false,
    		editable: false,
    		hiddenName: 'colnum',
    		valueField: 'text',
			//读取类别表中的业务文档类别    		
    		store:Ext.create('Ext.data.Store',{
    			fields: ['text', 'value'],
    			autoLoad:true,
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
        border: false,
        margin:'10 0 0 0',
		buttonAlign:"center",
		items:[{
			xtype : 'displayfield',
			fieldLabel:'原文',
			labelWidth :60,
			labelAlign : 'right',
			value:"<a href='javascript:void(0)' onclick=editTheOriginal()>" +"点击修改该文档原文<a/>"
			/*fieldLabel:'原文',
			xtype : 'htmleditor',
			labelStyle:'color:black;',
			width: 504,
			labelAlign : 'right',
			emptyText:'请输入原文',
			labelWidth :60,
			//height:210,
			name : 'theOriginal',
			enableFont:false*/
		},{
			//隐藏域
			//业务文档的原文
			xtype:'hiddenfield',
			name : 'theOriginal'
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
							  url:'serviceDocStoreFileUpload.do?number='+sjnum,
	    					  method:'POST',
	    					  waitMsg:'文件上传中',
	    					  timeout:1000*60,
	    					  success:function(form, action) {
	    		    			   var sericeDocStoreTemp = Ext.getCmp('seriveDocStoreTempFileId');
	    		    			   //LeadSpeakStoreFileGridPanel
	    		    			   sericeDocStoreTemp.remove(Ext.getCmp('ServiceDocStoreFileGridPanel'));
	    		    			   sericeDocStoreTemp.add(new Ushine.docInfo.ServiceDocStoreFileGridPanel(sjnum,config.record));
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
			height:130,
			width:'100%',
			layout: {
		        type: 'hbox',
		        align: 'stretch'
		    },
			margin:'0 0 0 8',
			//id:'personStoreTempFilesId',
			items:[{
				id:'seriveDocStoreTempFileId',
				//存放附件的gridpanel
				margin:'5 15 0 60',
				flex:1,
				height:'100%',
				layout:'fit',
				border:false,
				/*items:Ext.create('Ushine.docInfo.ServiceDocStoreFileGridPanel',{
					sjnum:sjnum,
					record:config.record
				})*/
				//items:Ext.create('Ushine.docInfo.ServiceDocStoreFileGridPanel')
				items:new Ushine.docInfo.ServiceDocStoreFileGridPanel(sjnum,config.record)
			}]
		}];
	  this.buttons=[
	  	Ext.create('Ushine.buttons.Button', {
	   		text: '确定',
	   		baseCls: 't-btn-red',
	   		handler: function () {
	   			//提交给后台处理
	   			var updateForm=self.getForm();
	   			if(updateForm.isValid()){
		   				//添加遮罩	   				
		   				var loadMask=new Ext.LoadMask(self,{
		   					msg:'正在修改业务文档,请耐心等待……'
		   				});
	   					loadMask.show();
	   					//设置超时时间,默认是30000毫秒;
	   					//设置timeout：1分钟
	   					//获得文件名列表
	   					var fileStore=Ext.getCmp('ServiceDocStoreFileGridPanel').getStore();
		   				var arr=new Array();
		   				fileStore.each(function(record){
	   						arr.push(record.data.filePath);
	   					});
	   					//修改
	   					Ext.Ajax.timeout=1000*60;
	   					Ext.Ajax.request({
	   						url:"updateVocationalWorkStore.do",
			   				method:'post',
			   				params:{
			   					id:updateForm.findField('id').getValue(),
			   					number:sjnum,
			   					attaches:arr,//附件
			   					docName:updateForm.findField("docName").getValue(),
			   					docNumber:updateForm.findField("docNumber").getValue(),
			   					infoType:updateForm.findField("infoType").getValue(),
			   					time:updateForm.findField("time").getValue(),
			   					theOriginal:updateForm.findField("theOriginal").getValue(),
			   					involvedInTheField:updateForm.findField("involvedInTheField").getValue(),
			   				},
			   				success:function(response){
			   					var obj=Ext.JSON.decode(response.responseText);
								Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
								//关闭
								self.findParentByType('window').close();
								//刷新数据
								Ext.getCmp("servicedocgridpanel").getStore().reload();
								//清除选择的数据
								Ext.getCmp("servicedocgridpanel").getSelectionModel().clearSelections();
			   				},
			   				failure:function(response){
			   					Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败");
			   				}
	   					})
	   					/*updateForm.submit({
			   				submitEmptyText:false,
	   					});*/
	   			}else{
	   				Ext.create('Ushine.utils.Msg').onInfo("请填写完整信息");
	   			}
	   		}
	   	}),Ext.create('Ushine.buttons.Button', {
	   		text: '重置',
	   		margin:'0 0 0 35',
	   		baseCls: 't-btn-yellow',
	   		id:'resetForeignDocument'
	   	  })
	  ];
	  this.callParent();
	}
});
//定义上传单个文件的UploadServiceDocumnetWin
Ext.define('UploadServiceDocumentWin',{
	extend:'Ushine.win.Window',
	title : "上传业务文档",
	modal : true,
	layout:{
		type : 'vbox',
		align:'stretch'
	},
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:550,
	width:900,
	margin:'10 0 0 0',
	bodyPadding: 5,
	constructor:function(){
		var self=this;
		var number=parseInt(Math.random()*1000);
		this.items=[{
			xtype:'form',
			layout:'hbox',
			height:55,
			border:false,
			id:'identifyservicedocform',
			items:[{
					xtype: 'hiddenfield',
					name:'saveInfo',
					value:'0'
				},{
					xtype: 'hiddenfield',
					name:'saveDetail'
				},{
				  	xtype: 'hiddenfield',
					name:'unSaveDetail'
				},{
				xtype:'combo',
				margin:'15 0 0 0',
				allowBlank:false,
				labelAlign : 'right',
				emptyText:'请选择文件类别',
				blankText:'此选项不能为空',
				width: 300,
				labelWidth : 90,
				fieldLabel:'文档类别',
				valueField: 'value',
				editable: false,
	    		displayField:'text',
				//读取类别表中的业务文档类别
	    		//value是指id,text是指文本
	    		store:Ext.create('Ext.data.Store',{
	    			fields: ['text', 'value'],
	    			autoLoad:true,
	    			proxy:{
	    				type:'ajax',
	    				url:'getvocationalworkstoretype.do',
	    				reader:{
	    					type:'json'
	    				}
	    			}
	    		}),
	    		name:'docType'
			},{
				xtype:'filefield',
				margin:'15 0 0 10',
				buttonOnly:true,
				id:'uploadServiceDocFile',
				//formData.append('file', $('#file')[0].files[0]);
				buttonConfig:{
					text:'上传',
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
	        				  accept:'file/*'
	        			  });
			  		},
					change:function(){
						//往后台上传附件
						var form=self.getComponent(0).getForm();
						var docTypeId=form.findField('docType').getValue();
						//获得文件最后修改时间
						var lastModified=$("#uploadServiceDocFile-button-fileInputEl")[0].files[0].lastModified;
						if(form.isValid()){
							form.submit({
								  url:'uploadFileAndIdentify.do?number='+number+'&lastModified='+lastModified+'&docTypeId='+docTypeId,
		    					  method:'POST',
		    					  waitMsg:'文件上传中',
		    					  timeout:1000*60,
		    					  success:function(form, action) {
		    						  //添加数据到grid中
		    						  var store=Ext.getCmp('importservicedocgridpanel').getStore();
									  //store.removeAll();
									  store.add(Ext.JSON.decode(action.result.msg));
		    					  },
		    					  // 提交失败的回调函数
		    					  failure : function(form, action) {
		    						  Ext.create('Ushine.utils.Msg').onInfo(action.result.msg);
		    					  }
							});
						}else{
							Ext.create('Ushine.utils.Msg').onInfo("请选择文档类别");
						}
					}
				}
			},Ext.create('Ushine.buttons.Button',{
				text: '导入',
				height:22,
				margin:'15 0 0 10',
				baseCls: 't-btn-red',
				handler:function(){
					//超时10分钟
					Ext.Ajax.timeout=1000*60*10;
					var store=Ext.getCmp('importservicedocgridpanel').getStore();
					var arr2=new Array();
					//获得数据
					store.each(function(record){
						arr2.push(record.data);
					});
					if(arr2.length>0){
						var loadMask=new Ext.LoadMask(self,{
							msg:'正在添加业务文档,请耐心等待……'
						});
						loadMask.show();
						//添加到数据库
						Ext.Ajax.request({
							//url:'saveMultiVocationalWorkStore.do',
							url:'saveVocationalWorkStore.do',
							params:{
								//意味新增多个
								uploadAndSaveMultiDoc:'yes',
								datas:Ext.JSON.encode(arr2),
							},
							success:function(response){
								loadMask.hide();
								var obj=Ext.JSON.decode(response.responseText);
								var msg=Ext.JSON.decode(obj.msg);
							 	msg=Ext.JSON.decode(msg.msg);
							 	var form=self.getComponent(0).getForm();
								//入库信息
								form.findField('saveInfo').setValue(msg.saveInfo);
								form.findField('unSaveDetail').setValue(msg.unSaveDetail);
								form.findField('saveDetail').setValue(msg.saveDetail);
								//关闭窗体
								//self.close();
								Ext.create('Ushine.utils.Msg').onInfo("新增成功");
								//刷新数据
								Ext.getCmp("servicedocgridpanel").getStore().reload();
								store.removeAll();
							},
							failure:function(){
								Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败");
								loadMask.hide();
							}
						})
					}else{
						Ext.create('Ushine.utils.Msg').onInfo("请先添加文档");
					}
				}
			}),{
				xtype:'displayfield',
				labelAlign : 'right',
				//width: 250,
				labelWidth : 60,
				margin:'15 0 0 10',
				fieldLabel:'入库信息',
				//value:'无',
				value:"<a href='javascript:void(0)' onclick=showSaveDetail()>点击查看详情<a/>"
				//name:'saveInfo'
			},{
				xtype:'displayfield',
				labelAlign : 'right',
				//width: 250,
				labelWidth : 70,
				fieldLabel:'未入库详情',
				margin:'15 0 0 10',
				value:"<a href='javascript:void(0)' onclick=showUnSaveDetail()>点击查看详情<a/>"
			}]
		},
		Ext.create('ImportServiceDocGridpanel')]
		this.callParent();
	}
});
//定义导入业务文档的ImportServiceDocumentWin
Ext.define('ImportServiceDocumentWin',{
	extend:'Ushine.win.Window',
	title : "导入业务文档",
	modal : true,
	layout:{
		type : 'vbox',
		align:'stretch'
	},
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:550,
	width:850,
	margin:'10 0 0 0',
	bodyPadding: 5,
	constructor:function(){
		var self=this;
		this.items=[/*{
			xtype:'panel',
			height:50,
			margin:'0 10 0 10',
			//
			html:"<form method='post' enctype='multipart/form-data' " +
					"id='muiltifileuploadform'><input type='file' id='muiltifileupload' name='muiltifileupload' multiple='multiple'><br/>" +
					"<button onclick='muiltiFileUpload()'>多文件上传</button></form>",  
		},*/{
			xtype:'fieldset',
			margin:'0 10 0 10',
			title:'基本信息',
			items:[{
				//第一大行
				layout:{
					type : 'vbox',
					align:'stretch'
				},
				id:'identifyservicedocform',
				xtype:'form',
				border:false,
				items:[{
					xtype:'hidden',
					name:'uploadNumber',
					//生成随机数
					//value:parseInt(Math.random()*1000)
				},{
					//第一行
					layout:'hbox',
					height:40,
					border:false,
					bodyPadding: 5,
					items:[{
						xtype:'textfield',
						//xtype:'displayfield',
						allowBlank:false,
						labelAlign : 'right',
						emptyText:'请输入共享文件夹全路径',
						blankText:'此选项不能为空',
						width: 500,
						labelWidth : 120,
						fieldLabel:'共享文件夹地址',
						name:'ip',
						value:''
					},/*{
						xtype:'combo',
						allowBlank:false,
						labelAlign : 'right',
						emptyText:'请选择文件夹',
						blankText:'此选项不能为空',
						width: 250,
						labelWidth : 90,
						valueField: 'folderName',
						editable: false,
			    		displayField:'folderName',
			    		listConfig:{
			    			maxHeight:150
			    		},
						//读取文件夹名称
			    		store:Ext.create('Ext.data.Store',{
			    			fields: ['folderName']
			    		}),
			    		listeners:{
			    			focus:function(thiz){
			    				var field=thiz.up('form').getForm().findField('ip');
			    				var ip=field.getValue();
			    				var store=thiz.getStore();
			    				store.removeAll();
			    				var proxy=new Ext.data.proxy.Ajax({
			    					url:'getrootfoldername.do?ip='+ip,
			    					reader:{
			    						type:'json'
			    					}
			    				});
			    				store.setProxy(proxy);
								store.reload();
								thiz.clearValue();
			    			}
			    		},
						fieldLabel:'文件夹名称',
						name:'folderName'
					}*/,{
						xtype:'combo',
						margin:'0 15 0 0',
						allowBlank:false,
						labelAlign : 'right',
						emptyText:'请选择文件类别',
						blankText:'此选项不能为空',
						width: 250,
						labelWidth : 90,
						fieldLabel:'文档类别',
						valueField: 'value',
						editable: false,
			    		displayField:'text',
						//读取类别表中的业务文档类别
			    		//value是指id,text是指文本
			    		store:Ext.create('Ext.data.Store',{
			    			fields: ['text', 'value'],
			    			autoLoad:true,
			    			proxy:{
			    				type:'ajax',
			    				url:'getvocationalworkstoretype.do',
			    				reader:{
			    					type:'json'
			    				}
			    			}
			    		}),
			    		name:'docType'
					}]
				},{
					//第二行
					layout:{
						type : 'hbox',
						align:'stretch'
					},
					border:false,
					bodyPadding: 5,
					items:[{
						xtype:'displayfield',
						labelAlign : 'right',
						width: 250,
						labelWidth : 90,
						fieldLabel:'识别word',
						value:'无',
						name:'identifyInfo'
					},{
					  	xtype: 'hiddenfield',
						name:'unIdentifyDetail'
					},{
						xtype:'displayfield',
						labelAlign : 'right',
						width: 250,
						labelWidth : 90,
						fieldLabel:'未识别详情',
						margin:'0 30 0 0',
						value:"<a href='javascript:void(0)' onclick=showUnIdentifyDetail()>点击查看详情<a/>"
					},Ext.create('Ushine.buttons.Button',{
							text: '识别',
							height:22,
							baseCls: 't-btn-red',
							handler:function(){
								var form=Ext.getCmp('identifyservicedocform').getForm();
								if(form.isValid()){
									var loadMask=new Ext.LoadMask(self,{
										msg:'正在识别文档,请耐心等待……'
									});
									loadMask.show();
									//设置uploadNumber的值
									form.findField('uploadNumber').setValue(parseInt(Math.random()*1000));
									var panel=Ext.getCmp('importservicedocgridpanel');
									panel.getStore().removeAll();
									form.submit({
										url:"identifyVocationalWorkStore.do",
										method:'POST',
										//设置超时时间,默认是30000毫秒;
										timeout:1000*60*10,
										submitEmptyText:false,
										//waitMsg:'正在识别文档,请耐心等待……',
										success:function(form,action){
											loadMask.hide();
											 var result=Ext.JSON.decode(action.response.responseText);
											 //console.log(result);
											 if(result.status>0){
												 result=Ext.JSON.decode(result.msg);
												 form.findField('identifyInfo').setValue(result.identifyInfo);
												 form.findField('unIdentifyDetail').setValue(result.unIdentifyDetail);
												 form.findField('saveInfo').setValue('无');
												 form.findField('unSaveDetail').setValue('无');
												 //加载数据
												 var store=Ext.getCmp('importservicedocgridpanel').getStore();
												 store.removeAll();
												 store.add(result.datas);
												 Ext.create('Ushine.utils.Msg').onInfo("识别完成");
										  	 }
										},
										failure:function(form,action){
											loadMask.hide();
											Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败");
										}
									});
								}else{
									Ext.create('Ushine.utils.Msg').onInfo("请填写完整信息");
								}
							}
					})]
				},{
					//第三行
					layout:{
						type : 'hbox',
						align:'stretch'
					},
					border:false,
					bodyPadding: 5,
					items:[{
						xtype:'displayfield',
						labelAlign : 'right',
						width: 250,
						labelWidth : 90,
						fieldLabel:'入库信息',
						//value:'无',
						value:"<a href='javascript:void(0)' onclick=showSaveDetail()>点击查看详情<a/>"
						//name:'saveInfo'
					},{
						xtype: 'hiddenfield',
						name:'saveInfo',
						value:'0'
					},{
						xtype: 'hiddenfield',
						name:'saveDetail'
					},{
					  	xtype: 'hiddenfield',
						name:'unSaveDetail'
					},{
						xtype:'displayfield',
						labelAlign : 'right',
						width: 250,
						labelWidth : 90,
						fieldLabel:'未入库详情',
						margin:'0 30 0 0',
						value:"<a href='javascript:void(0)' onclick=showUnSaveDetail()>点击查看详情<a/>"
					},Ext.create('Ushine.buttons.Button',{
						text: '入库',
						height:22,
						baseCls: 't-btn-yellow',
						handler:function(){
							//超时10分钟
							Ext.Ajax.timeout=1000*60*10;
							var store=Ext.getCmp('importservicedocgridpanel').getStore();
							var arr2=new Array();
							//获得数据
							store.each(function(record){
								arr2.push(record.data);
							});
							//临时文件夹的名称
							var form=Ext.getCmp('identifyservicedocform').getForm();
							var uploadNumber=form.findField('uploadNumber').getValue();
							var infoType=form.findField('docType').getValue();
							if(arr2.length>0){
								var loadMask=new Ext.LoadMask(self,{
									msg:'正在添加业务文档,请耐心等待……'
								});
								loadMask.show();
								//添加到数据库
								Ext.Ajax.request({
									//url:'saveMultiVocationalWorkStore.do',
									url:'saveVocationalWorkStore.do',
									params:{
										//意味新增多个
										saveMultiDoc:'yes',
										datas:Ext.JSON.encode(arr2),
										uploadNumber:uploadNumber,
										//类型
										infoType:infoType
									},
									success:function(response){
										loadMask.hide();
										var obj=Ext.JSON.decode(response.responseText);
										var msg=Ext.JSON.decode(obj.msg);
									 	msg=Ext.JSON.decode(msg.msg);
										//console.log(msg);
										//入库信息
										form.findField('saveInfo').setValue(msg.saveInfo);
										form.findField('unSaveDetail').setValue(msg.unSaveDetail);
										form.findField('saveDetail').setValue(msg.saveDetail);
										//Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
										//关闭窗体
										//self.close();
										Ext.create('Ushine.utils.Msg').onInfo("新增成功");
										//刷新数据
										Ext.getCmp("servicedocgridpanel").getStore().reload();
										//清空grid里面的数据
										store.removeAll();
									},
									failure:function(){
										Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败");
										loadMask.hide();
									}
								})
							}else{
								Ext.create('Ushine.utils.Msg').onInfo("请先添加文档");
							}
						}
					})]
				}]
			}]
		},{
			//xtype:'fieldset',
			border:false,
			//margin:'10 10 10 10',
			margin:10,
			//title:'文档信息',
			layout:'fit',
			flex:3,
			items:[
				//识别出符合要求的文档信息
				Ext.create('ImportServiceDocGridpanel')
			]
		}]
		this.callParent();
	}
});
//未能够识别的详情
function showUnIdentifyDetail(){
	var form=Ext.getCmp('identifyservicedocform').getForm();
	var result=form.findField('unIdentifyDetail').getValue();
	//显示详细履历和活动情况的window
	Ext.define('showUnIdentifyDetailWin',{
		height:500,
		width:500,
		title:'未识别的文档信息',
		autoScroll:true,
		buttonAlign:"center",
		layout:'fit',
		border:false,
		//maximizable:true,
		extend:'Ushine.win.Window',
		constructor:function(config){
			//格式化日期
			var date=Ext.util.Format.date(new Date(), 'YmdHis');
			//console.log(date);
			var self=this;
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
	   					//window.open("downloadSaveVocationalStoreDetail.do?result="+result);
	   					Ext.Ajax.request({
	   						url:'saveVocationalStoreDetail.do',
	   						method:'post',
	   						params:{
	   							result:result,
	   							date:date
	   						},
	   						success:function(){
	   							location.href = "downloadSaveVocationalStoreDetail.do?date="+date;
	   							//window.open("downloadSaveVocationalStoreDetail.do?date="+date);
	   						},
	   						failure:function(){
	   							//console.log('保存失败')
	   						}
	   					})
	   				}
				}),Ext.create('Ushine.buttons.Button', {
	      			text: '关闭',
	   				baseCls: 't-btn-yellow',
	   				handler:function(){
   						//关闭
   						self.close();
	   				}
			})];
			this.callParent();
		}
	});
	Ext.create('showUnIdentifyDetailWin').show();
};
//保存的文档详情
function showSaveDetail(){
	var form=Ext.getCmp('identifyservicedocform').getForm();
	var result=form.findField('saveDetail').getValue();
	var count=form.findField('saveInfo').getValue();
	//显示详细履历和活动情况的window
	Ext.define('showSaveDetailWin',{
		height:500,
		width:500,
		title:'入库文档信息，'+count,
		autoScroll:true,
		buttonAlign:"center",
		layout:'fit',
		border:false,
		//maximizable:true,
		extend:'Ushine.win.Window',
		constructor:function(config){
			//格式化日期
			var date=Ext.util.Format.date(new Date(), 'YmdHis');
			//console.log(date);
			var self=this;
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
	   					//window.open("downloadSaveVocationalStoreDetail.do?result="+result);
	   					Ext.Ajax.request({
	   						url:'saveVocationalStoreDetail.do',
	   						method:'post',
	   						params:{
	   							result:result,
	   							date:date
	   						},
	   						success:function(){
	   							location.href = "downloadSaveVocationalStoreDetail.do?date="+date;
	   							//window.open("downloadSaveVocationalStoreDetail.do?date="+date);
	   						},
	   						failure:function(){
	   							//console.log('保存失败')
	   						}
	   					})
	   				}
				}),Ext.create('Ushine.buttons.Button', {
	      			text: '关闭',
	   				baseCls: 't-btn-yellow',
	   				handler:function(){
   						//关闭
   						self.close();
	   				}
			})];
			this.callParent();
		}
	});
	Ext.create('showSaveDetailWin').show();
}
//未能够保存的文档详情
function showUnSaveDetail(){
	var form=Ext.getCmp('identifyservicedocform').getForm();
	var result=form.findField('unSaveDetail').getValue();
	//显示详细履历和活动情况的window
	Ext.define('showUnSaveDetailWin',{
		height:500,
		width:500,
		title:'未保存的文档信息',
		autoScroll:true,
		buttonAlign:"center",
		layout:'fit',
		border:false,
		//maximizable:true,
		extend:'Ushine.win.Window',
		constructor:function(config){
			//格式化日期
			var date=Ext.util.Format.date(new Date(), 'YmdHis');
			//console.log(date);
			var self=this;
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
	   					//window.open("downloadSaveVocationalStoreDetail.do?result="+result);
	   					Ext.Ajax.request({
	   						url:'saveVocationalStoreDetail.do',
	   						method:'post',
	   						params:{
	   							result:result,
	   							date:date
	   						},
	   						success:function(){
	   							location.href = "downloadSaveVocationalStoreDetail.do?date="+date;
	   							//window.open("downloadSaveVocationalStoreDetail.do?date="+date);
	   						},
	   						failure:function(){
	   							//console.log('保存失败')
	   						}
	   					})
	   				}
				}),Ext.create('Ushine.buttons.Button', {
	      			text: '关闭',
	   				baseCls: 't-btn-yellow',
	   				handler:function(){
   						//关闭
   						self.close();
	   				}
			})];
			this.callParent();
		}
	});
	Ext.create('showUnSaveDetailWin').show();
}

//定义导入文档的gridpanel
Ext.define('ImportServiceDocGridpanel',{
	extend:'Ext.grid.Panel',
	id:'importservicedocgridpanel',
	height:300,
	flex:1,
	stripeRows:false,//true为隔行换颜色
	autoHeight:true,
	disableSelection:false,//是否禁止行选择
	columnLines:true, //列的框线样式
	loadMask:true,
	autoScroll:true,
	//selType:'checkboxmodel',//复选框
	viewConfig:{
		emptyText:'没有数据',
		stripeRows:true,
		enableTextSelection:true
	},
	constructor:function(){
		var self=this;
		var store=Ext.create('Ext.data.JsonStore',{
			model:'ServiceDocModel',
			remoteStore:true,
		});
		
		this.columns=[
			//设置列id
		    {text: '文件名称',  dataIndex: 'fileName',sortable: false,flex: 3,menuDisabled:true},
		    {text: '标题',  dataIndex: 'docName',sortable: false,flex: 3,menuDisabled:true},
		    {text: '期刊号',  dataIndex: 'docNumber',sortable: false,flex: 1 ,menuDisabled:true},
		    {text: '建立时间',  dataIndex: 'time',sortable: false,flex: 1,menuDisabled:true},
		    //{text: '类别',  dataIndex: 'infoType',sortable: false,flex: 2,menuDisabled:true},
		    //{text: '全路径',  dataIndex: 'filePath',sortable: false,flex: 1,menuDisabled:true},
		    {text: '操作',sortable: false,menuDisabled:true,dataIndex: 'icon',align:'center',flex: 0.5,xtype:'actioncolumn',items:[{
			    	//删除
			    	icon: 'images/cancel.png',
			        tooltip: '删除',
			        handler: function(grid, rowIndex, colIndex){
			        	//选中一行的数据
		            	var data = self.store.getAt(rowIndex).data;
		            	console.log(data.filePath);
			            Ext.Ajax.request({
			            	url:'deleteTempServiceDoc.do',
			            	params:{
			            		filePath:data.filePath,
			            	},
			            	success:function(response){
			            		var msg=Ext.decode(response.responseText).status;
			            		if(msg>0){
			            			//删除该行数据
			            			self.store.removeAt(rowIndex);
			            		}
			            	}
			            })
		            }
		   		}
		    ]}
		    //{text: '原文',  dataIndex: 'theOriginal',sortable: false,flex: 1,menuDisabled:true},
		];
		this.loadMask =true;
		//创建面板底部的工具条
		
		/*
		 * {
			    	//查看原文
			    	icon: 'images/page_white_text.png',
			        tooltip: '查看原文',
			         handler: function(grid, rowIndex, colIndex){
		            	var data = self.store.getAt(rowIndex).data;   //选中一行的数据
		            	//读word文档内容
		            	var loadMask=new Ext.LoadMask(self,{
							msg:'读取文档内容中,请耐心等待……'
						});
						loadMask.show();
		            	Ext.Ajax.request({
		            		url:'readWord.do',
		            		method:'POST',
		            		params:{
		            			filePath:data.filePath
		            		},
		            		success:function(response){
		            			//console.log(response);
		            			loadMask.hide();
		            			var msg=Ext.decode(response.responseText).msg;
		            			//var msg=response.responseText;
		            			var win=Ext.create('ShowServiceDocumentContentWin',{
		            				content:msg
		            			});
		            			win.show();
		            			//win.getComponent(0).setValue(msg);
		            		},
		            		failure:function(){
		            			loadMask.hide();
		            			Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败");
		            		}
		            	})
		            }
		    },"_",
		 * this.bbar = new Ext.PagingToolbar({
			//一个要和Ext.data.Store参与绑定并且自动提供翻页控制的工具栏
			store : store,  		//数据源
			displayInfo: true,   		//是否显示信息(true表示显示信息)
			firstText: '首页', 	 		//第一页文本 显示第一页按钮的快捷提示文本
			lastText: '末页', 	 		//最后一页的文本 显示最后一页按钮快捷提示文本
			prevText: '上一页',   		//上一个导航按钮工具条
			nextText: '下一页',  			//下一个导航按钮工具条
			refreshText: '刷新',			//刷新的文本 显示刷新按钮的快捷提示文本
			beforePageText: '当前第', 	//输入项前的文字 输入项前的文本显示。
			afterPageText: '/{0}页', 	//输入项后的文字 可定制的默认输入项后的文字
			displayMsg: '本页显示第{0}条到第{1}条, 共有{2}条文档', //显示消息 显示分页状态的消息
			emptyMsg: '没有文档记录'  		//空消息 没有找到记录时，显示该消息
		});*/
		this.store = store;
		this.listeners={
			/*//双击事件
			itemdblclick:function(thiz, record, item, index, e, eOpts){
				//详细信息
				//console.log(record);
				Ext.create('ServiceDocDetailWin',{
					record:record
				}).show();
			}*/
		};
		this.callParent(); 
	}
})
//定义一个word内容查看的window
Ext.define('ShowServiceDocumentContentWin',{
	extend:'Ushine.win.Window',
	title : "文档具体内容",
	modal : true,
	layout : 'fit',
	border : false,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:600,
	width:700,
	buttonAlign:"center",
	maximizable:true,
	constructor:function(config){
		var self=this;
		this.items=[{
			xtype:'htmleditor',
			value:config.content,
			enableFont:false,
			html:"<div id='ServiceDocumentContent'></div>"
		}];
		this.buttons=[Ext.create('Ushine.buttons.Button',{
			text: '打印',
	   		baseCls: 't-btn-red',
	   		handler:function(){
	   			//赋值
	   			var value=self.getComponent(0).getValue();
	   			Ext.get('ServiceDocumentContent').dom.innerHTML=value;
	   			$('#ServiceDocumentContent').printArea();
	   		}
		}),Ext.create('Ushine.buttons.Button', {
  			text: '取消',
  			margin:'0 0 0 35',
			baseCls: 't-btn-yellow',
			handler:function(){
				//关闭
				self.close();
			}
	   	})];
		this.callParent();
	}
});

//定义一个SaveServiceDocumentWin
Ext.define('SaveServiceDocumentWin',{
	extend:'Ushine.win.Window',
	title : "添加业务文档",
	modal : true,
	layout : 'fit',
	border : false,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:600,
	width:545,
	constructor:function(){
		var self=this;
		this.items=[
			Ext.create('SaveServiceDocumentForm')
		];
		this.callParent();
	}
});
//定义一个修改UpdateServiceDocumentWin
Ext.define('UpdateServiceDocumentWin',{
	extend:'Ushine.win.Window',
	title : "修改业务文档",
	modal : true,
	layout : 'fit',
	border : false,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:430,
	width:540,
	id:'updateservicedocument-win',
	constructor:function(config){
		var self=this;
		//console.log(config);
		this.items=[
			Ext.create('UpdateServiceDocumentForm',{
				record:config.record
			})
		];
		this.callParent();
	}
});
//修改原文的window
function editTheOriginal(){
	var win=Ext.getCmp('updateservicedocument-win');
	var form=win.getComponent(0).getForm();
	//得到原文表单
	var id=form.findField('id').getValue();
	var field=form.findField('theOriginal');
	var value="";
	if(form.findField('theOriginal').getValue().length>0){
		value=form.findField('theOriginal').getValue();
		showEditTheOriginalWin(field,value);
	}else{
		Ext.Ajax.request({
			url:'getTheOriginal.do?id='+id,
			method:'get',
			success:function(response){
				var result=Ext.JSON.decode(response.responseText);
				value=result.msg;
				showEditTheOriginalWin(field,value);
			}
		});
	}
};

function showEditTheOriginalWin(htmlField,value){
	Ext.define('EditTheOriginalWin',{
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
	      	   			htmlField.setValue(self.getComponent(0).getValue());
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
	Ext.create('EditTheOriginalWin').show();
}


//多文件上传
function muiltiFileUpload(){
	var formData = new FormData();
	formData.append('file', $('#file')[0].files[0]);
	
	$.ajax({
	    url: 'uploadmuiltifiles.do',
	    type: 'POST',
	    cache: false,
	    data: formData,
	    processData: false,
	    contentType: false
	}).done(function(res) {
		
	}).fail(function(res) {
		
	});
	
	 /*var formData=new FormData();
	 var file = document.getElementById('muiltifileupload').files;
	 var form=document.getElementById('muiltifileuploadform');
	 //action='uploadmuiltifiles.do'
	 var data=[];
	 for(i=0;i<file.length;i++){
		 //获得最后修改时间
		 data.push(file[i].name);
		 formData.append("name",file[i].name);
		 formData.append("lastModified",file[i].lastModified);
    }*/
	//$("#muiltifileuploadform").attr("action", "uploadmuiltifiles.do?data='"+data+"'");
	
	//form.submit();
	 //异步上传文件
	  //var oOutput = document.getElementById("output");
	 /* var oData = new FormData(document.forms.namedItem("muiltifileuploadform"));

	  oData.append("CustomField", "This is some extra data");

	  var oReq = new XMLHttpRequest();
	  oReq.open("POST", "uploadmuiltifiles.do", true);
	  oReq.onload = function(oEvent) {
	    if (oReq.status == 200) {
	      alert('success');
	    } else {
	    	 alert('failures');
	    }
	  };*/
	 //oReq.send(oData);
	/* var fd = new FormData(document.getElementById("muiltifileuploadform"));
	 fd.append("CustomField", "This is some extra data");
	 $.ajax({
	   url: "uploadmuiltifiles.do",
	   type: "POST",
	   data: fd,
	   processData: false,  // 告诉jQuery不要去处理发送的数据
	   contentType: false   // 告诉jQuery不要去设置Content-Type请求头
	 });*/

	/* $.ajax({
		    url: 'uploadmuiltifiles.do',
		    type: 'POST',
		    //必要时key/value的形式
		    //data: {data:data},
		    data: new FormData($('#muiltifileuploadform')[0]),
		    cache: false,
		    contentType: false,
		    //contentType: 'multipart/form-data',
		    processData: false,
		   
		}).done(function(res) {
			alert('ok');
		}).fail(function(res) {
			
		});*/
}
