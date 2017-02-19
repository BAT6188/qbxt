/**
 * 外来文档库
 * author：donghao
 */
Ext.define('Ushine.docInfo.ForeignDocument', {
	extend: 'Ext.panel.Panel',
	id: 'foreigndocument-panel',
	region: 'center',
	title:'外来文档库管理',
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
					      /* Ext.create('Ushine.buttons.IconButton', {
					    	   border: false,
					    	   id: 'importNewBtn',
					    	   //width:100,
					    	   btnText: '导入文档', 
					    	   baseCls: 't-btn-red',
					    	   handler: function() {
					    	   	   //弹出导入外来文档
					    		   self.importOutsideDoc();
					    	   }
					       }),
					       Ext.create('Ushine.buttons.IconButton', {
					    	   border: false,
					    	   id: 'uploadNewBtn',
					    	   //width:80,
					    	   btnText: '上传文档', 
					    	   baseCls: 't-btn-red',
					    	   handler: function() {
					    	   	   self.uploadForeignDoc();
					    	   }
					       }),*/
					       Ext.create('Ushine.buttons.IconButton', {
					    	   border: false,
					    	   id: 'createNewBtn',
					    	   //width:120,
					    	   btnText: '新增文档', 
					    	   baseCls: 't-btn-red',
					    	   handler: function() {
					    	   	   //新增外来文档
					    	   	   self.saveForeignDoc();
					    	   }
					       }),
					        Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'updateBtn',
					    	   btnText: '修改文档',
					    	   //width:120,
					    	   handler: function () {
					    		   var grid=self.getComponent(1);					    	   	
							   	   if(grid.getSelectionModel().hasSelection()){
							   	   		//只能一行
							   	   		var record=grid.getSelectionModel().getSelection();
							   	   		if(record.length>1){
							   	   			Ext.create('Ushine.utils.Msg').onInfo("只能选择一行数据");
							   	   		}else{
							   	   			self.updateForeignDoc(record);
							   	   		}
							   	   }else{
							   	   		Ext.create('Ushine.utils.Msg').onInfo("请至少选择一行数据");
							   	   }
					    	   }
					       }),
					       Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'delBtn',
					    	   btnText: '删除文档',
					    	   //width:120,
					    	   handler: function () {
					    		  //删除刊物
				    	   		 var grid=self.getComponent(1);					    	   	
							   	   if(grid.getSelectionModel().hasSelection()){
							   	   		//允许多行
							   	   		var record=grid.getSelectionModel().getSelection();
							   	   		var ids=[];
							   	   		for(var i=0;i<record.length;i++){
							   	   			ids.push(record[i].get('id'));
							   	   		}
							   	   		Ext.Msg.confirm("提示","确定删除吗?",function(btn){
							   	   			if (btn == 'yes') {
							   	   			   	self.delForeignDoc(ids);
					    				   }
							   	   		})
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
	            		    //fields:['id','infoType','secretRank','name',
	            		    //'sourceUnit','title','time','centent']
	            		    data : [
	            		    	//字段筛选
	            		    	{"text":"任意字段", "value":"anyField"},
	            		        {"text":"文档类别", "value":"infoType"},
	            		        {"text":"来源单位", "value":"sourceUnit"},
	            		        {"text":"文档名称", "value":"name"},
	            		        {"text":"标题", "value":"title"},
	            		        {"text":"建立时间", "value":"time"},
	            		        {"text":"密级", "value":"secretRank"},
	            		        {"text":"内容", "value":"centent"}
	            		    ]
	            		}),
						width: 260,
						value:'anyField'
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
		                           	 self.findForeignDoc();
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
			        		//查询外来文档
			        		self.findForeignDoc();
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
		//外来文档gridpanel
		Ext.create('Ushine.docInfo.ForeignDocGridPanel')
		];	
		this.callParent();		
	},
	//导入外来文档：
	importOutsideDoc:function(){
		Ext.create('ImportOutsideDocumentWin').show();
	},
	//上传单个文档
	uploadForeignDoc:function(){
		Ext.create('UploadForeignDocumentWin').show();
	},
	//新增外来文档
	saveForeignDoc:function(){
		Ext.create('SaveForeignDocumentWin').show();
	},
	resetValue:function(form,record){
		var self=this;
		form.findField("id").setValue(record[0].get('id'));
		form.findField("name").setValue(self.replaceString(record[0].get('name')));
		form.findField("docNumber").setValue(self.replaceString(record[0].get('docNumber')));
		form.findField("infoType").setValue(self.replaceString(record[0].get('infoType')));
		form.findField("time").setValue(self.replaceString(record[0].get('time')).substring(0,10));
		form.findField('involvedInTheField').setValue(self.replaceString(record[0].get('involvedInTheField')));
		//form.findField("title").setValue(self.replaceString(record[0].get('title')));
		//保留格式
		form.findField("centent").setValue(record[0].get('centent'));
		//form.findField("secretRank").setValue(self.replaceString(record[0].get('secretRank')));
	},
	//字符串替换
	replaceString:function(string){
		var value=string;
		//去除所有的html标签
		/*if(string.indexOf("<font color='orange'/>")>-1&&string.indexOf("</font>")){
			value=string.replace(/<[^>]+>/g,"");
		}*/
		value=string.replace(/<[^>]+>/g,"");
		return value;
	},
	//修改外来文档
	updateForeignDoc:function(record){
		//console.log(record);
		var self=this;
		var win=Ext.create('UpdateForeignDocumentWin',{
			record:record
		});
		win.show();
		var form=win.getComponent(0).getForm();
		self.resetValue(form,record);
		//重置
		Ext.getCmp('resetForeignDoc').addListener('click',function(){
			self.resetValue(form,record);
		});
	},
	//删除外来文档
	delForeignDoc:function(ids){
		var self=this;
		var loadMask=new Ext.LoadMask(self,{
			msg:'正在删除外来文档,请耐心等待……'
		});
		loadMask.show();
		//请求超时时间
		Ext.Ajax.request({
			url:'delOutsideDocStore.do',
			params:{
				ids:ids
			},
			success:function(response){
				loadMask.hide();
				var obj=Ext.JSON.decode(response.responseText);
				Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				//刷新数据
				Ext.getCmp("foreigndocgridpanel").getStore().reload();
   			},
			failure:function(){
				Ext.create('Ushine.utils.Msg').onInfo("提示,请求后台服务失败");
				loadMask.hide();
			}
		})
	},
	//查询
	findForeignDoc:function(){
		this.remove('d-foreigndoc-grid');
		this.add(Ext.create('Ushine.docInfo.ForeignDocGridPanel'));
	}
});

//定义一个SaveForeignDocumentForm表单
Ext.define('SaveForeignDocumentForm',{
	extend:'Ext.form.Panel',
	//id:'savemedianetworkbook-formpanel',
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
	            margin:0,
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
					height:22,
					name : 'name'
				},{
					layout:'hbox',
	                //height:35,
		            //bodyPadding: 5,
		            margin:0,
		            border: false,
					buttonAlign:"center",
					items:[{
						fieldLabel:'类别',
						labelStyle:'color:red;',
						allowBlank:false,
						xtype:'combo',
						emptyText:'请选择类别',
						blankText:'此选项不能为空',
						width: 285,
						labelAlign : 'right',
						labelWidth : 90,
						//height:22,
						name : 'infoType',
						allowNegative: false,
	            		editable: false,
	            		hiddenName: 'colnum',
	            		valueField: 'value',
	            		store:Ext.create('Ext.data.Store', {
	            		    fields: ['text', 'value'],
	            		 	proxy:{
	            		 		type:'ajax',
	            		 		url:'getoutsidedocstoretype.do',
	            		 		reader:{
	            		 			type:'json'
	            		 		}
	            		 	}
	            		})
					}]
				}]
			},{
				layout:'hbox',
	            bodyPadding: 5,
	            margin:0,
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
					height: 22,
					margin:0,
					value: new Date()
				},{
					fieldLabel:'期刊号',
					labelStyle:'color:red;',
					xtype : 'textfield',
					labelAlign : 'right',
					emptyText:'请输入期刊号',
					blankText:'此选项不能为空',
					width: 285,
					labelWidth : 90,
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
								  url:'foreignDocStoreFileUpload.do?number='+sjnum,
		    					  method:'POST',
		    					  waitMsg:'文件上传中',
		    					  timeout:1000*60,
		    					  success:function(form, action) {
		    		    			   var foreignDocStoreTemp = Ext.getCmp('ForeignDocStoreTempFileId');
		    		    			   //LeadSpeakStoreFileGridPanel
		    		    			   foreignDocStoreTemp.remove(Ext.getCmp('ForeignDocFileGridPanel'));
		    		    			   foreignDocStoreTemp.add(new Ushine.docInfo.ForeignDocFileGridPanel(sjnum));
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
					id:'ForeignDocStoreTempFileId',
					//存放附件的gridpanel
					margin:'5 15 0 60',
					flex:1,
					height:'100%',
					layout:'fit',
					border:false,
					items:Ext.create('Ushine.docInfo.ForeignDocFileGridPanel')
				}]
			}
		];
	  	this.buttons=[
	  		Ext.create('Ushine.buttons.Button', {
		   		text: '确定',
		   		baseCls: 't-btn-red',
		   		handler: function () {
		   			//提交给后台处理
		   			var saveForm=self.getForm();
		   			if(saveForm.isValid()){
		   				//首先判断
		   				Ext.Ajax.timeout=1000*60;
		   				Ext.Ajax.request({
		   					url:'hasStoreByName.do',
			   				method:'post',
			   				params:{
			   					name:saveForm.findField("name").getValue(),
			   				},
			   				success:function(response){
		   						var obj=Ext.JSON.decode(response.responseText);
		   						//console.log(obj);
		   						if(obj.msg=='exist'){
		   							Ext.create('Ushine.utils.Msg').onQuest("文档已经存在，是否仍添加",function(btn){
		   								//console.log(btn);
		   								if(btn=='yes'){
		   									//仍添加
		   									self.saveOutsideDocStore(self,sjnum,saveForm);
		   								}
		   							});
		   						}else{
		   							//不存在
		   							self.saveOutsideDocStore(self,sjnum,saveForm);
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
	saveOutsideDocStore:function(self,sjnum,saveForm){
		//添加遮罩	   				
		var loadMask=new Ext.LoadMask(self,{
		msg:'正在添加外来文档,请耐心等待……'
		});
		loadMask.show();
		//超时1分钟
		Ext.Ajax.timeout=1000*60;
		Ext.Ajax.request({
		url:'saveOutsideDocStore.do',
		method:'post',
		params:{
			number:sjnum,
			name:saveForm.findField("name").getValue(),
			docNumber:saveForm.findField("docNumber").getValue(),
			//sourceUnit:saveForm.findField("sourceUnit").getValue(),
			infoType:saveForm.findField("infoType").getValue(),
			time:saveForm.findField("time").getValue(),
			involvedInTheField:saveForm.findField("involvedInTheField").getValue(),
			//title:saveForm.findField("title").getValue(),
			centent:saveForm.findField("centent").getValue(),
			//secretRank:saveForm.findField("secretRank").getValue(),
		},
		success:function(response){
			var obj=Ext.JSON.decode(response.responseText);
			Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
			//关闭
			self.findParentByType('window').close();
			//刷新数据
			Ext.getCmp("foreigndocgridpanel").getStore().reload();
		},
		failure:function(){
			Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败");
		}
		})
		/*saveForm.submit({
		submitEmptyText:false,
		})*/
	}
});

//定义一个SaveForeignDocumentWin
Ext.define('SaveForeignDocumentWin',{
	extend:'Ushine.win.Window',
	title : "添加外来文档",
	modal : true,
	layout : 'fit',
	border : false,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:630,
	width:575,
	constructor:function(){
		var self=this;
		this.items=[
			Ext.create('SaveForeignDocumentForm')
		];
		this.callParent();
	}
});


//定义一个UpdateForeignDocumentForm表单
Ext.define('UpdateForeignDocumentForm',{
	extend:'Ext.form.Panel',
	layout:'vbox',
    bodyPadding: 5,
    
    margin:0,
    border: false,
	baseCls: 'form-body',
	buttonAlign:"center",
	constructor:function(config){
		var self=this;
		var sjnum=parseInt(Math.random()*1000);
		this.items=[{
				layout:'hbox',
	            bodyPadding: 5,
	            margin:0,
	            border: false,
				buttonAlign:"center",
				items:[{
					xtype:'hiddenfield',
					name:'id'
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
					height:22,
					name : 'name'
				},{
					fieldLabel:'类别',
					labelStyle:'color:red;',
					allowBlank:false,
					xtype:'combo',
					emptyText:'请选择类别',
					blankText:'此选项不能为空',
					width: 285,
					labelAlign : 'right',
					labelWidth : 90,
					//height:22,
					name : 'infoType',
					allowNegative: false,
            		editable: false,
            		valueField: 'text',
            		displayField:'text',
            		store:Ext.create('Ext.data.Store', {
            		    fields: ['text', 'value'],
            		 	proxy:{
            		 		type:'ajax',
            		 		url:'getoutsidedocstoretype.do',
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
					//height: 22
					//value:'2001-01-01'
				},{
					fieldLabel:'期刊号',
					labelStyle:'color:red;',
					xtype : 'textfield',
					labelAlign : 'right',
					emptyText:'请输入期刊号',
					blankText:'此选项不能为空',
					width: 285,
					labelWidth : 90,
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
	            //margin:'15 0 0 0',
	            border: false,
				buttonAlign:"center",
				items:[{
					xtype : 'displayfield',
					fieldLabel:'内容',
					labelWidth :60,
					labelAlign : 'right',
					value:"<a href='javascript:void(0)' onclick=editForeignDocumentContent()>"
						+"点击修改该文档内容<a/>"
					/*fieldLabel:'内容',
					xtype : 'htmleditor',
					enableFont:false,
					labelStyle:'color:black;',
					width: 534,
					labelAlign : 'right',
					emptyText:'请输入内容',
					labelWidth :60,
					height:220,
					name : 'centent'*/
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
								  url:'foreignDocStoreFileUpload.do?number='+sjnum,
		    					  method:'POST',
		    					  waitMsg:'文件上传中',
		    					  timeout:1000*60,
		    					  success:function(form, action) {
		    		    			   var foreignDocStoreTemp = Ext.getCmp('ForeignDocStoreTempFileId');
		    		    			   //LeadSpeakStoreFileGridPanel
		    		    			   foreignDocStoreTemp.remove(Ext.getCmp('ForeignDocFileGridPanel'));
		    		    			   foreignDocStoreTemp.add(new Ushine.docInfo.ForeignDocFileGridPanel(sjnum,config.record));
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
					id:'ForeignDocStoreTempFileId',
					//存放附件的gridpanel
					margin:'5 15 0 60',
					flex:1,
					height:'100%',
					layout:'fit',
					border:false,
					items:new Ushine.docInfo.ForeignDocFileGridPanel(sjnum,config.record)
				}]
			}
		];
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
		   					msg:'正在修改外来文档,请耐心等待……'
		   				});
	   					loadMask.show();
	   					var fileStore=Ext.getCmp('ForeignDocFileGridPanel').getStore();
		   				var arr=new Array();
		   				fileStore.each(function(record){
	   						arr.push(record.data.filePath);
	   					});
	   					//超时1分钟
	   					Ext.Ajax.timeout=1000*60;
	   					Ext.Ajax.request({
	   						url:'updateOutsideDocStore.do',
	   						method:'post',
	   						params:{
	   							//请求参数
	   							id:updateForm.findField('id').getValue(),
		   						number:sjnum,
		   						attaches:arr,
	   							name:updateForm.findField("name").getValue(),
	   							//sourceUnit:updateForm.findField("sourceUnit").getValue(),
	   							infoType:updateForm.findField("infoType").getValue(),
	   							docNumber:updateForm.findField("docNumber").getValue(),
	   							time:updateForm.findField("time").getValue(),
	   							involvedInTheField:updateForm.findField("involvedInTheField").getValue(),
	   							//title:updateForm.findField("title").getValue(),
	   							centent:updateForm.findField("centent").getValue(),
	   							//secretRank:updateForm.findField("secretRank").getValue(),
	   						},
	   						success:function(response){
		   						var obj=Ext.JSON.decode(response.responseText);
								Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
								//关闭
								self.findParentByType('window').close();
								//刷新数据
								Ext.getCmp("foreigndocgridpanel").getStore().reload();
								//清除选择
								Ext.getCmp("foreigndocgridpanel").getSelectionModel().clearSelections();
		   					},
		   					failure:function(response){
				   				Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败");
				   			}
	   					})
		   				/*updateForm.submit({
		   					submitEmptyText:false,
		   				})*/
		   			}else{
		   				Ext.create('Ushine.utils.Msg').onInfo("请填写完整信息");
		   			}
		   		}
		   	}),Ext.create('Ushine.buttons.Button', {
		   		text: '重置',
		   		margin:'0 0 0 35',
		   		baseCls: 't-btn-yellow',
		   		id:'resetForeignDoc'
		   })
	  	];
	  this.callParent();
	}
});

//定义一个修改MediaNetworkBookWin
Ext.define('UpdateForeignDocumentWin',{
	extend:'Ushine.win.Window',
	title : "修改外来文档",
	id:'updateforeigndocument-win',
	modal : true,
	layout : 'fit',
	border : false,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:465,
	width:580,
	constructor:function(config){
		var self=this;
		this.items=[
			Ext.create('UpdateForeignDocumentForm',{
				record:config.record
			})
		];
		this.callParent();
	}
});

//修改内容
function editForeignDocumentContent(){
	var win=Ext.getCmp('updateforeigndocument-win');
	var form=win.getComponent(0).getForm();
	//得到内容表单
	var field=form.findField('centent');
	var value=form.findField('centent').getValue();
	Ext.define('EditForeignDocumentContentWin',{
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
	Ext.create('EditForeignDocumentContentWin').show();
};

//定义导入外来文档的Window
Ext.define('ImportOutsideDocumentWin',{
	extend:'Ushine.win.Window',
	title : "导入外来文档",
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
	height:500,
	width:850,
	margin:'10 0 0 0',
	bodyPadding: 5,
	constructor:function(){
		var self=this;
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
					},{
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
			    				url:'getoutsidedocstoretype.do',
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
						xtype:'textfield',
						labelAlign : 'right',
						width: 500,
						labelWidth : 120,
						fieldLabel:'来源单位',
						allowBlank:false,
						name:'sourceUnit'
					},{
						xtype:'combo',
						margin:'0 15 0 0',
						allowBlank:false,
						labelAlign : 'right',
						emptyText:'请选择文件类别',
						blankText:'此选项不能为空',
						width: 250,
						labelWidth : 90,
						fieldLabel:'文档密级',
						valueField: 'value',
						editable: false,
			    		displayField:'text',
						//读取类别表中的业务文档类别
			    		//value是指id,text是指文本
			    		store:Ext.create('Ext.data.Store',{
			    			fields: ['text', 'value'],
			    			autoLoad:true,
			    			data:[
			    			      {text:'无',value:'无'},
			    			      {text:'秘密',value:'秘密'},
			    			      {text:'机密',value:'机密'},
			    			      {text:'绝密',value:'绝密'},
			    			]
			    		}),
			    		name:'secretRank'
					}]
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
								//后台任务执行
								var form=Ext.getCmp('identifyservicedocform').getForm();
								
								if(form.isValid()){
									var loadMask=new Ext.LoadMask(self,{
										msg:'正在识别文档,请耐心等待……'
									});
									loadMask.show();
									form.findField('uploadNumber').setValue(parseInt(Math.random()*1000));
									var panel=Ext.getCmp('importoutsidedocgridpanel');
									panel.getStore().removeAll();
									form.submit({
										url:"identifyOutsideDocStore.do",
										method:'POST',
										//设置超时时间,默认是30000毫秒;
										timeout:1000*60*10,
										submitEmptyText:false,
										//waitMsg:'正在识别文档,请耐心等待……',
										success:function(form,action){
											loadMask.hide();
											 var result=Ext.JSON.decode(action.response.responseText);
											 console.log(result);
											 if(result.status>0){
												 result=Ext.JSON.decode(result.msg);
												 form.findField('identifyInfo').setValue(result.identifyInfo);
												 form.findField('unIdentifyDetail').setValue(result.unIdentifyDetail);
												 form.findField('saveInfo').setValue('无');
												 form.findField('unSaveDetail').setValue('无');
												 //加载数据
												 var store=panel.getStore();
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
							var store=Ext.getCmp('importoutsidedocgridpanel').getStore();
							var arr2=new Array();
							//获得数据
							store.each(function(record){
								arr2.push(record.data);
							});
							//临时文件夹的名称
							var form=Ext.getCmp('identifyservicedocform').getForm();
							var uploadNumber=form.findField('uploadNumber').getValue();
							var infoType=form.findField('docType').getValue();
							//密级
							var secretRank=form.findField('secretRank').getValue();
							//来源单位
							var sourceUnit=form.findField('sourceUnit').getValue();
							if(arr2.length>0&&sourceUnit.length>0&&secretRank.length>0){
								var loadMask=new Ext.LoadMask(self,{
									msg:'正在添加外来文档,请耐心等待……'
								});
								loadMask.show();
								//添加到数据库
								Ext.Ajax.request({
									url:'saveOutsideDocStore.do',
									params:{
										//意味新增多个
										saveMultiDoc:'yes',
										datas:Ext.JSON.encode(arr2),
										uploadNumber:uploadNumber,
										//类型
										infoType:infoType,
										secretRank:secretRank,
										sourceUnit:sourceUnit
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
										Ext.getCmp("foreigndocgridpanel").getStore().reload();
										//清空grid中数据
										store.removeAll();
									},
									failure:function(){
										Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败");
										loadMask.hide();
									}
								})
							}else{
								Ext.create('Ushine.utils.Msg').onInfo("请先添加文档以及来源");
							}
						}
					})]
				}]
			}]
		},{
			border:false,
			margin:10,
			layout:'fit',
			flex:3,
			items:[
				//识别出符合要求的文档信息
				Ext.create('ImportOutsideDocGridpanel')
			]
		}]
		this.callParent();
	}
});

//定义导入文档的gridpanel
Ext.define('ImportOutsideDocGridpanel',{
	extend:'Ext.grid.Panel',
	id:'importoutsidedocgridpanel',
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
	constructor:function(){
		var self=this;
		var store=Ext.create('Ext.data.JsonStore',{
			//pageSize:50,
			model:'ForeignDocModel',
			remoteStore:true,
		});
		
		this.columns=[
			//设置列id
			{xtype: 'rownumberer',flex: 0.5,text: '序号'},
			//{text:'id',dataIndex:'id',flex: 1,menuDisabled:true,hidden:true},
		    {text: '文件名称',  dataIndex: 'fileName',sortable: false,flex: 3,menuDisabled:true},
		    {text: '文档名',  dataIndex: 'docName',sortable: false,flex: 3,menuDisabled:true},
		    //{text: '来源单位',  dataIndex: 'sourceUnit',sortable: false,flex: 3,menuDisabled:true},
		    //{text: '密级',  dataIndex: 'secretRank',sortable: false,flex: 3,menuDisabled:true},
		    //{text: '文档路径',  dataIndex: 'filePath',sortable: false,flex: 3,menuDisabled:true},
		    {text: '期刊号',  dataIndex: 'docNumber',sortable: false,flex: 1 ,menuDisabled:true},
		    {text: '建立时间',  dataIndex: 'time',sortable: false,flex: 1,menuDisabled:true},
		    {text: '操作',sortable: false,menuDisabled:true,dataIndex: 'icon',align:'center',flex: 0.5,xtype:'actioncolumn',items:[{
			    	//删除
			    	icon: 'images/cancel.png',
			        tooltip: '删除',
			        handler: function(grid, rowIndex, colIndex){
			        	//选中一行的数据
		            	var data = self.store.getAt(rowIndex).data;
		            	console.log(data.filePath);
			            Ext.Ajax.request({
			            	url:'deleteTempForeignDoc.do',
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
		];
		this.loadMask =true;
		this.store = store;
		this.callParent(); 
	}
});

//定义上传单个文件的UploadServiceDocumnetWin
Ext.define('UploadForeignDocumentWin',{
	extend:'Ushine.win.Window',
	title : "上传外来文档",
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
			layout:'vbox',
			height:100,
			border:false,
			id:'identifyservicedocform',
			items:[{
				layout:{
					type : 'hbox',
					align:'stretch'
				},
				border:false,
				bodyPadding: 5,
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
		    				url:'getoutsidedocstoretype.do',
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
									  url:'uploadForeinDocAndIdentify.do?number='+number+'&lastModified='+lastModified+'&docTypeId='+docTypeId,
			    					  method:'POST',
			    					  waitMsg:'文件上传中',
			    					  timeout:1000*60,
			    					  success:function(form, action) {
			    						  //添加数据到grid中
			    						  var store=Ext.getCmp('importoutsidedocgridpanel').getStore();
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
						var store=Ext.getCmp('importoutsidedocgridpanel').getStore();
						var arr2=new Array();
						//获得数据
						store.each(function(record){
							arr2.push(record.data);
						});
						if(arr2.length>0){
							var loadMask=new Ext.LoadMask(self,{
								msg:'正在添加外来文档,请耐心等待……'
							});
							loadMask.show();
							//添加到数据库
							Ext.Ajax.request({
								url:'saveOutsideDocStore.do',
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
									Ext.getCmp("foreigndocgridpanel").getStore().reload();
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
			},{
				layout:{
					type : 'hbox',
					align:'stretch'
				},
				border:false,
				margin: 10,
				items:[{
					xtype:'textfield',
					labelAlign : 'right',
					width: 500,
					labelWidth : 90,
					fieldLabel:'来源单位',
					allowBlank:false,
					name:'sourceUnit'
				},{
					xtype:'combo',
					margin:'0 15 0 0',
					allowBlank:false,
					labelAlign : 'right',
					emptyText:'请选择文件类别',
					blankText:'此选项不能为空',
					width: 250,
					labelWidth : 90,
					fieldLabel:'文档密级',
					valueField: 'value',
					editable: false,
		    		displayField:'text',
					//读取类别表中的业务文档类别
		    		//value是指id,text是指文本
		    		store:Ext.create('Ext.data.Store',{
		    			fields: ['text', 'value'],
		    			autoLoad:true,
		    			data:[
		    			      {text:'无',value:'无'},
		    			      {text:'秘密',value:'秘密'},
		    			      {text:'机密',value:'机密'},
		    			      {text:'绝密',value:'绝密'},
		    			]
		    		}),
		    		name:'secretRank'
				}]
			}],
			
		},
		Ext.create('ImportOutsideDocGridpanel')]
		this.callParent();
	}
});