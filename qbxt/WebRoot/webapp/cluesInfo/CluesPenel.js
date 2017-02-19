/**
 * 线索管理
 */
 Ext.define('Ushine.cluesInfo.CluesPenel', {
	extend: 'Ext.panel.Panel',
	id: 'cluesperson-panel',
	region: 'center',
	bodyStyle: 'background-color: #ffffff; border: none; padding: 10px;',
	layout: {
		type: 'vbox',
		align : 'stretch',
		pack  : 'start'
	},
	constructor: function(config) {
		var self = this;
		var fieldValue='';
		var date=new Date();
		//第一个月
		date.setMonth(0);
		//第一天
		date.setDate(1);
		var startTime=Ext.Date.format(date,'Y-m-d');
		if(config!=undefined){
			fieldValue=config.fieldValue
		}
		this.items = [
		    // 标题栏
			Ext.create('Ushine.base.TitleBar', {
				cTitle: '线索管理'
			}),{
			// 工具栏
			xtype : 'panel',
		    baseCls : 'tar-body',
			height:120,
			style:"margin-top:-10px;",
			layout:'fit',
			// 工具栏 -- 左侧
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
					    	   border: false,
					    	   id: 'createNewBtn',
					    	   btnText: '新增线索', 
					    	   baseCls: 't-btn-red',
					    	   handler: function() {
					    		   self.saveClues();
					    	   }
					       }),
					        Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'updateBtn',
					    	   btnText: '修改线索',
					    	   handler: function () {
					    		   var cluesGrid = Ext.getCmp('cluesinfogridpanel');
					    		   if(cluesGrid.getSelectionModel().hasSelection()){
					    			   var res=cluesGrid.getSelectionModel().getSelection();
					    			   if(res.length > 1){
					    				   Ext.create('Ushine.utils.Msg').onInfo("对不起，最多可选择一行记录。");
					    			   }else if(res.length == 1){
					    				   self.updateClue(res);
					    			   }	
					    		   }else{
					    			   Ext.create('Ushine.utils.Msg').onInfo("对不起，请选择至少一行记录。");
					    		   }
					    	   }
					       }),
					       Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'delBtn',
					    	   btnText: '删除线索',
					    	   handler: function () {
					    		   var cluesGrid = Ext.getCmp('cluesinfogridpanel');
					    		   if(cluesGrid.getSelectionModel().hasSelection()){
					    			   var res=cluesGrid.getSelectionModel().getSelection();
					    			   var ids=[];
					    			   for(var i=0;i<res.length;i++){
					    				   ids.push(res[i].data.id);
					    			   }
					    			   Ext.Msg.confirm('提示','确定要删除选中的线索吗?',function(btn) {
					    				   if (btn == 'yes') {
					    				   	   var loadMask=new Ext.LoadMask(cluesGrid,{
													msg:'正在删除线索,请耐心等待……'
												});
												loadMask.show();
					    					   Ext.Ajax.request({
					    						    url: 'delClueStoreByIds.do',
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
					    							    	 self.findClueByProperty();
					    							     }
					    							     Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
					    						    },
					    				            failure: function(form, action) {
					    				            	loadMask.hide();
					    				            	Ext.create('Ushine.utils.Msg').onInfo('删除线索失败，请联系管理员');
					    				            }
					    						});
					    				   }
					    			   });	
					    		   }else{
					    			   Ext.create('Ushine.utils.Msg').onInfo("对不起，请选择至少一行记录。");
					    		   }
					    	   }
					       }),
					      
					      /* Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'updateStatusBtn',
					    	   btnText: '启用线索',
					    	   handler: function () {
					    		   //警告信息
					    		   var rolePanel=Ext.getCmp('cluesinfogridpanel');
					    		   var records = rolePanel.getSelectionModel().getSelection();
					    		   console.log(records);
					    		   if(records.length <= 0){
					    			   Ext.create('Ushine.utils.Msg').onInfo('请你选择一个线索信息');
					    		   }else{  //1:否      2:是
					    			   var clueIds = [];
					    			   for ( var i = 0; i < records.length; i++) {
					    				   //判断要修改的信息状态是否都是启用的状态，如果是提示无须启用
					    				   if(records[i].data.isEnable=="1"){
					    					   clueIds.push(records[i].data.id);
					    				   }
					    			   }
					    			   if(clueIds.length<=0){
					    				   Ext.create('Ushine.utils.Msg').onInfo('选中的线索都为启用,无须启用');
					    			   }else{
					    				   Ext.Ajax.request({
					    					   url: 'startClue.do',
					    					   actionMethods: {
					    						   create : 'POST',
					    						   read   : 'POST', // by default GET
					    						   update : 'POST',
					    						   destroy: 'POST'
					    					   },
					    					   params: {
					    						   clueIds:clueIds
					    					   },
					    					   success: function(response){
					    						   var text = response.responseText;
					    						   var obj=Ext.JSON.decode(text);
					    						   Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
					    						   //启用成功后刷新数据
					    						   self.findClueByProperty();
					    					   },
					    					   failure: function(form, action) {
					    						   Ext.create('Ushine.utils.Msg').onInfo('启用线索失败，请联系管理员');
					    					   }
					    				   });
					    			   }
					    		   }
					    	   }
					       }),
					       Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'updateCeaseBtn',
					    	   btnText: '禁用线索',
					    	   handler: function () {
					    		   //警告信息
					    		   var rolePanel=Ext.getCmp('cluesinfogridpanel');
					    		   var records = rolePanel.getSelectionModel().getSelection();
					    		   if(records.length <= 0){
					    			   Ext.create('Ushine.utils.Msg').onInfo('请你选择一个线索信息');
					    		   }else{
					    			   var clueIds = [];
					    			   for ( var i = 0; i < records.length; i++) {
					    				   //判断要修改状态的线索状态是否是禁用，是禁用才修改
					    				   if(records[i].data.isEnable=="2"){
					    					   clueIds.push(records[i].data.id);
					    				   }
					    			   }
					    			   if(clueIds.length<=0){
					    				   Ext.create('Ushine.utils.Msg').onInfo('选中的线索都为禁用,无须禁用');
					    			   }else{
					    				   //试用ajax请求服务器
					    				   Ext.Ajax.request({
					    					   url: 'ceaseClue.do',
					    					   actionMethods: {
					    						   create : 'POST',
					    						   read   : 'POST', // by default GET
					    						   update : 'POST',
					    						   destroy: 'POST'
					    					   },
					    					   //要修改组织的id的集合
					    					   params: {
					    						   clueIds:clueIds
					    					   },
					    					   success: function(response){
					    						   var text = response.responseText;
					    						   var obj=Ext.JSON.decode(text);
					    						   Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
					    						   //禁用成功后刷新数据
					    						   self.findClueByProperty();
					    					   },
					    					   failure: function(form, action) {
					    						   Ext.create('Ushine.utils.Msg').onInfo('禁用失败，请联系管理员');
					    					   }
					    				   });
					    			   }
					    		   }
					    	   }
					       })*/
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
									    {"text":"任意字段", "value":"anyField"},
									    {"text":"线索名称", "value":"clueName"},
									    {"text":"线索来源", "value":"clueSource"},
									    {"text":"发现时间", "value":"findTime"}
									]
							}),
							value:'anyField',
							width: 260,
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
	                            	self.findClueByProperty();
	                            }  
	                         }
						},
						value:fieldValue
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
						editable:false,
						height: 22,
						width: 300,
						id:'endTime',
						maxValue: new Date(),
						value:new Date()
					},
					Ext.create('Ushine.buttons.Button', {
			        	text : '查询线索',
			        	style:"margin-left:20px;",
			        	width:100,
			        	labelWidth: 60,
			        	height:22,
			        	id : 'search-Button',
			        	baseCls: 't-btn-red',
			        	handler:function(){
	                        self.findClueByProperty();
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
		//线索gridpanel
		Ext.create('Ushine.cluesInfo.CluesGridPanel')];	
		this.callParent();		
	},
	//保存线索
	saveClues:function(){
		Ext.create('SaveCluesWin').show();
	},
	
	//修改线索
	updateClue:function(res){
		//定义一个SaveOrganizeInfoWin的Window
		Ext.define('UpdateClueInfoWin',{
			extend:'Ushine.win.Window',
			title : "修改线索",
			modal : true,
			id:'updateClueInfo_formpanel',
			layout : 'vbox',
			border : false,
			closable : true,
			draggable:true,
			resizable : false,
			plain : true,
			buttonAlign:"center",
			borer:false,
			height:250,
			width:600,
			constructor:function(){
				var self=this;//字符串替换
				this.replaceString=function(string){
					var value=string;
					//去除所有的html标签
					/*if(string.indexOf("<font color='orange'/>")>-1&&string.indexOf("</font>")>-1){
						value=string.replace(/<[^>]+>/g,"");
					}*/
					value=string.replace(/<[^>]+>/g,"");
					return value;
				},
				this.items=[{
					layout:'vbox',
					border: false,
					xtype:'form',
					margin:'10 0 0 0',
			        id:'updateClueForm',
			        items:[{
						//第一行
						layout:'hbox',
				        bodyPadding: 8,
				        border:false,
						buttonAlign:"center",
						items:[{
							fieldLabel:'线索名称',
							labelStyle:'color:red;',
							allowBlank:false,
							xtype : 'textfield',
							emptyText:'请输入组织名称',
							blankText:'此选项不能为空',
							width: 260,
							labelAlign : 'right',
							labelWidth : 70,
							//height:22,
							name : 'clueName',
							value:self.replaceString(res[0].data.clueName)
						},{
							fieldLabel:'线索来源',
							labelStyle:'color:red;',
							allowBlank:false,
							xtype : 'textfield',
							emptyText:'请输入线索来源',
							blankText:'此选项不能为空',
							width: 260,
							labelAlign : 'right',
							labelWidth : 70,
							//height:22,
							name : 'clueSource',
							value:self.replaceString(res[0].data.clueSource)
						}]
					},{
						//第一行
						layout:'hbox',
				        bodyPadding: 8,
				        border:false,
						buttonAlign:"center",
						items:[{
							fieldLabel:'发现时间',
							labelStyle:'color:red;',
							allowBlank:false,
							xtype : 'datefield',
							format:'Y-m-d',
							maxValue:new Date(),
							labelAlign : 'right',
							emptyText:'请选择日期',
							blankText:'此选项不能为空',
							width: 260,
							labelWidth : 70,
							//height:22,
							name : 'findTime',
							value:self.replaceString(res[0].data.findTime)
						}]
					},{
						//第六行
				        layout:'hbox',
				        bodyPadding: 8,
				        border: false,
				        margin:0,
						buttonAlign:"center",
						items:[{
							fieldLabel:'线索内容',
							xtype : 'displayfield',
							value:"<a href='javascript:void(0)' onclick=editClueContent()>" +
				  			"点击修改线索内容<a/>",
							labelStyle:'color:black;',
							enableFont:false,
							allowBlank:true,
							width: 520,
							labelAlign : 'right',
							emptyText:'请输入线索内容',
							labelWidth :70,
							//height:120,
							//name : 'clueContent',
							//value:res[0].data.clueContent
						},{
							xtype:'hiddenfield',
							name:'clueContent',
							value:self.replaceString(res[0].data.clueContent)
						}]
					  },{
						//活动情况
				        layout:'hbox',
				        bodyPadding: 8,
				        border: false,
				        margin:0,
						buttonAlign:"center",
						items:[{
							fieldLabel:'部署进展',
							xtype : 'displayfield',
							enableFont:false,
							labelStyle:'color:black;',
							allowBlank:true,
							width: 520,
							labelAlign : 'right',
							emptyText:'请输入部署进展',
							labelWidth :70,
							//height:120,
							value:"<a href='javascript:void(0)' onclick=editArrangeAndEvolveCondition()>" +
				  			"点击修改线索进展情况<a/>"
							//name : 'arrangeAndEvolveCondition',
							//value:res[0].data.arrangeAndEvolveCondition
						},{
							xtype:'hiddenfield',
							name:'arrangeAndEvolveCondition',
							value:self.replaceString(res[0].data.arrangeAndEvolveCondition)
						}]
					  }]
				}]
				
				  this.buttons=[
				  	Ext.create('Ushine.buttons.Button', {
				   		text: '确定',
				   		baseCls: 't-btn-red',
				   		handler: function () {
				   			var form  = Ext.getCmp('updateClueForm');
				   			//console.log(form.getForm());
				   			var loadMask=new Ext.LoadMask(form,{
								msg:'正在修改线索,请耐心等待……'
							});
							loadMask.show();
				   			//提交给后台处理
				   			if(form.getForm().isValid()){
				   				form.getForm().submit({
				   					url:'updateClueStore.do',
				   					method:'POST',
				   					params:{
				   						id:res[0].data.id
				   					},
				   					submitEmptyText:false,
				   					success:function(form,action){
				   						loadMask.hide();
				   						if(action.result.success){
				   							var obj=Ext.JSON.decode(action.response.responseText);
				   							Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				   							Ext.getCmp('updateClueInfo_formpanel').close();
				   							Ext.getCmp('cluesinfogridpanel').getStore().reload();
				   							//取消选择
				   							Ext.getCmp('cluesinfogridpanel').getSelectionModel().clearSelections();
				   						}else{
				   							Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				   						}
				   					},
				   					failure:function(response){
				   						loadMask.hide();
				   						var obj=Ext.JSON.decode(response.responseText);
				   						Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				   						Ext.getCmp('updateClueInfo_formpanel').close();
				   					}
				   				});
				   			}
				   		}
				   	}),Ext.create('Ushine.buttons.Button', {
				   		text: '重置',
				   		margin:'0 0 0 35',
				   		baseCls: 't-btn-yellow',
				   		handler: function () {
				   			self.getComponent(0).getForm().reset();
				   		}
				   })
				  ];
				this.callParent();
			}
		});
		Ext.create('UpdateClueInfoWin').show();
	},
	//查询人员
	findClueByProperty:function(){
		this.remove('c_cluesinfo_grid');
		this.add(Ext.create('Ushine.cluesInfo.CluesGridPanel'));
	}
});

//定义一个SaveCluesForm保存线索人员表单
Ext.define('SaveCluesForm',{
	extend:'Ext.form.Panel',
	id:'clues-formpanel',
	layout:'vbox',
    bodyPadding: 8,
    margin:0,
    border: false,
	baseCls: 'form-body',
	buttonAlign:"center",
	constructor:function(){
		var sjnum = parseInt(Math.random()*1000000);//产生一个三位数的随机数
		var self=this;
		var date=new Date();
		//第一个月
		date.setMonth(0);
		//第一天
		date.setDate(1);
		var startTime=Ext.Date.format(date,'Y-m-d');
		this.items=[{
			title:'基本信息',
			//layout:'fit',
			height:345,
	    	xtype: 'fieldset',
		    layout: {
		        type: 'vbox',
		        align: 'stretch'
		    },
		    items:[{
		    	border:false,
		    	bodyStyle: "background: transparent;font-family: '新宋体', Arial;font-size: 12px;color: #333;",
		    	layout: {
		    		type: 'hbox',
		    		align: 'stretch'
		    	},
		    	items:[{
		    		//第一行
		    		fieldLabel:'线索名称',
		    		labelStyle:'color:red;',
		    		allowBlank:false,
		    		xtype : 'textfield',
		    		emptyText:'请输入线索名称',
		    		blankText:'此选项不能为空',
		    		width: 250,
		    		labelAlign : 'right',
		    		labelWidth : 70,
		    		height:22,
		    		name : 'clueName'
		    	},{
		    		fieldLabel:'线索来源',
		    		margin:'0 0 0 20',
		    		labelStyle:'color:red;',
		    		allowBlank:false,
		    		xtype : 'textfield',
		    		labelAlign : 'right',
		    		emptyText:'请输入线索来源',
		    		blankText:'此选项不能为空',
		    		width: 250,
		    		labelWidth : 70,
		    		height:22,
		    		name : 'clueSource'
		    	}]
		    },{
		    	border:false,
		    	margin:'10 0 0 0',
		    	bodyStyle: "background: transparent;font-family: '新宋体', Arial;font-size: 12px;color: #333;line-height:30px;",
		    	layout: {
		    		type: 'hbox',
		    		align: 'stretch'
		    	},
		    	items:[{
		    		fieldLabel:'发现时间',
		    		labelStyle:'color:red;',
		    		allowBlank:false,
		    		xtype : 'datefield',
		    		format:'Y-m-d',
		    		value:new Date(),
		    		maxValue:new Date(),
		    		labelAlign : 'right',
		    		emptyText:'请输入线索发现时间',
		    		blankText:'此选项不能为空',
		    		width: 250,
		    		labelWidth : 70,
		    		//height:22,
		    		name : 'findTime'
		    	},{
		    		fieldLabel:'发现时间',
		    		labelStyle:'color:red;',
		    		allowBlank:false,
		    		labelAlign : 'right',
		    		xtype:'textfield',
		    		width: 250,
		    		labelWidth : 70,
		    		name : 'number',
		    		hidden:true,
		    		value:sjnum
		    	}]
		    },{
		    	border:false,
		    	margin:'10 0 0 0',
		    	bodyStyle: "background: transparent;font-family: '新宋体', Arial;font-size: 12px;color: #333;line-height:30px;",
		    	layout: {
		    		type: 'hbox',
		    		align: 'stretch'
		    	},
		    	items:[{
					xtype : 'htmleditor',
					labelStyle:'color:black;',
					fieldLabel:'线索内容',
					allowBlank:true,
					width: 520,
					labelAlign : 'right',
					emptyText:'请输入线索内容',
					enableFont:false,
					labelWidth :70,
					height:120,
					name : 'clueContent'
				}]
		    },{
		    	border:false,
		    	margin:'10 0 0 0',
		    	bodyStyle: "background: transparent;font-family: '新宋体', Arial;font-size: 12px;color: #333;line-height:30px;",
		    	layout: {
		    		type: 'hbox',
		    		align: 'stretch'
		    	},
		    	items:[{
					fieldLabel:'部署进展',
					xtype : 'htmleditor',
					enableFont:false,
					labelStyle:'color:black;',
					allowBlank:true,
					width: 520,
					labelAlign : 'right',
					emptyText:'请输入工作部署及进展情况',
					labelWidth :70,
					height:120,
					name : 'arrangeAndEvolveCondition'
				}]
		    }
		    ]
		},{
			title:'涉及对象操作',
			height:320,
			width:544,
	    	xtype: 'fieldset',
		    layout: {
		        type: 'vbox',
		        align: 'stretch'
		    },
		    items:[{
		    	border:false,
		    	layout: {
		    		type: 'hbox',
		    		align: 'stretch'
		    	},
		    	items:[{//基础库类别
		    		border:false,
		    		bodyStyle: "background: transparent;font-family: '新宋体', Arial;font-size: 12px;color: #333;",
		    		layout: {
			    		type: 'vbox',
			    		align: 'stretch'
			    	},
				    items:[{
				    	border:false,
				    	margin:'10 0 10 0',
			    		bodyStyle: "background: transparent;font-family: '新宋体', Arial;font-size: 12px;color: #333;line-height:30px;",
			    		layout: {
				    		type: 'hbox',
				    		align: 'stretch'
				    	},
				    	items:[{
					    	fieldLabel:'基础库类别',
							labelAlign : 'right',
							labelWidth: 70,
		            		xtype:'combo',
		            		allowNegative: false,
		            		allowBlank: false,
		            		editable: false,
		            		width:210,
		            		emptyText: '请选择字段',
		            		valueField: 'value',
		            		name : 'tempClueDataType',
					    	id:'tempClueDataType',
							store:Ext.create('Ext.data.Store', {
		            		    fields: ['text', 'value'],
		            		    data : [
		            		        {"text":"人员库信息", "value":"personStore"},
		            		        //{"text":"组织库信息", "value":"organizStore"},
		            		        //{"text":"媒体网站库信息", "value":"websiteJournalStore"}
		            		    ]
		            		}),
		            		value:'personStore',
		            		listeners:{
		            			//当选项变化时清除CluesTempDataGridId里面的数据
		            			/*change:function(){
		            				 var CluesTempDataGrid = Ext.getCmp('CluesTempDataGridId');	            				 
		      				    	 CluesTempDataGrid.removeAll();
		      				    	 var panel=new Ushine.utils.CluesTempDataGridPanel();
		      				    	 panel.getStore().removeAll();
		      				    	 CluesTempDataGrid.add(panel);
		            			}*/
		            		}
						},{
					    	fieldLabel:'信息名称',
					    	xtype : 'textfield',
					    	emptyText:'请输入线索名称',
					    	blankText:'此选项不能为空',
					    	width: 210,
					    	labelAlign : 'right',
					    	labelWidth : 70,
					    	height:22,
					    	name : 'tempClueDataName',
					    	id:'tempClueDataName',
					    	listeners:{
								 specialkey:function(field,e){
		                            if(e.getKey() == e.ENTER){
		                            	//按下回车键
		                            	
		                            }  
		                         }
							}
					    }/*,Ext.create('Ushine.buttons.IconButton', {
					    	border: false,
						    id: 'addNewClue',
						    btnText: '添加信息', 
						    height:25,
						    baseCls: 't-btn-red',
						    handler:function(){
								//数据名称
						    	var tempClueDataName  = Ext.getCmp('tempClueDataName').value;
						    	if(tempClueDataName == '' || tempClueDataName == null){
						    		Ext.create('Ushine.utils.Msg').onInfo("请输入名称！");
						    	}else{
						    		//数据类型
							    	var tempClueDataType = Ext.getCmp('tempClueDataType').displayTplData[0].value;
							    	//console.log(tempClueDataType);
							    	self.saveTempClueDataName(sjnum,tempClueDataName,tempClueDataType);
							    	
						    	}				    
						    }
					    })*/]
				    },{
				    	border:false,
				    	margin:'0 0 10 0',
			    		bodyStyle: "background: transparent;font-family: '新宋体', Arial;font-size: 12px;color: #333;",
			    		layout: {
				    		type: 'hbox',
				    		align: 'stretch'
				    	},
				    	items:[Ext.create('Ushine.buttons.IconButton', {
						    border: false,
						    id: 'createNewClue',
						    btnText: '新增信息', 
						    height:25,
						    baseCls: 't-btn-red',
						    handler: function() {
						    	//数据名称
						    	var tempClueDataName  = Ext.getCmp('tempClueDataName').value;
						    	if(tempClueDataName == '' || tempClueDataName == null){
						    		Ext.create('Ushine.utils.Msg').onInfo("请输入名称！");
						    	}else{
						    		//数据类型
							    	var tempClueDataType = Ext.getCmp('tempClueDataType').displayTplData[0].value;
							    	//console.log(tempClueDataType);
							    	self.saveTempClueDataName(sjnum,tempClueDataName,tempClueDataType);
							    	
						    	}	
						    	/*//获得类别选择数据
						        var type = Ext.getCmp('tempClueDataType').displayTplData[0].value;
						        //根据类别来调用相关的函数新增相关的数据
						        if(type == 'personStore'){
						        	//新增人员
						        	var win=Ext.create('SaveOrUpdatePersonInfoWin',{
										title:'添加人员',
										//是线索人员
										isClue:'isClue',
										cluePersonNum:sjnum,
										items:[Ext.create('SavePersonInfoForm')]
									});
									win.show();
									Ext.getCmp('certificatestypegrid').getStore().removeAll();
									Ext.getCmp('networkaccounttypegrid').getStore().removeAll();
									Ext.getCmp('resetPersonInfo').addListener('click',function(){
										//重置
										win.getComponent(0).getForm().reset();
									});
						        }else if(type == 'organizStore'){
						        	//新增组织信息
						        	Ext.create('SaveOrganizeInfoWin',{
						        		//属于线索组织
						        		isClue:'isClue',
						        		clueOrganizNum:sjnum
						        	}).show();
						        	
						        }else if(type == 'websiteJournalStore'){
						        	  //新增媒体刊物信息
						        	  Ext.create('SaveMediaNetworkBookWin',{
						        		 isClue:'isClue',
						        		 clueNum:sjnum
						        	 }).show();
						        }*/
						 }
					}),Ext.create('Ushine.buttons.IconButton', {
				    	   border: false,
				    	   id: 'selectExistClue',
				    	   btnText: '选择已有数据',
				    	   baseCls: 't-btn-red',
				    	   handler: function() {
				    		   var tempClueDataType = Ext.getCmp('tempClueDataType').displayTplData[0].value;
				    		  self.selectExistingData(tempClueDataType,sjnum);
				    	   }
				       })]
				    }]
		    	}]
		    },{
		    	layout:'fit',
		    	xtype:'panel',
				region:'center',
				border:true,
				height:1,
				style:'background-color:#5CA7BA;',
				margin:'0 0 10 0'
		    },{
		    	layout:'fit',
		    	xtype:'panel',
				region:'center',
				border:false,
				id:'CluesTempDataGridId',
				items:new Ushine.utils.CluesTempDataGridPanel(sjnum)
		    }]
		}
	]
		    
	  this.buttons=[
	  	Ext.create('Ushine.buttons.Button', {
	   		text: '确定',
	   		baseCls: 't-btn-red',
	   		handler: function () {
	   			//提交给后台处理
	   			if(self.getForm().isValid()){
	   				//先判断是否存在
	   				Ext.Ajax.request({
	   					//先判断是否有了
	   					url:"hasStoreByClueName.do",
	   					method:'post',
	   					params:{
	   						clueName:self.getForm().findField('clueName').getValue()
	   					},
	   					success:function(response){
	   						var obj=Ext.JSON.decode(response.responseText);
	   						//console.log(obj);
	   						if(obj.msg=='exist'){
	   							Ext.create('Ushine.utils.Msg').onQuest("线索已经存在，是否仍添加",function(btn){
	   								//console.log(btn);
	   								if(btn=='yes'){
	   									//仍添加
	   									self.saveClueStore(self);
	   								}
	   							});
	   						}else{
	   							//不存在
	   							self.saveClueStore(self);
	   						}
	   					},
	   					failure:function(){
	   						Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败");
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
	//保存线索
	saveClueStore:function(self){
		self.getForm().submit({
			submitEmptyText:false,
			url:'saveClueInfo.do',
			method:'POST',
			waitMsg:'正在保存.....',
			success:function(form,action){
				if(action.result.success){
					var obj=Ext.JSON.decode(action.response.responseText);
					Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
					Ext.getCmp('saveCluesWin').close();
					var cluesperson_panel = Ext.getCmp('cluesperson-panel');
					cluesperson_panel.remove('c_cluesinfo_grid');
					cluesperson_panel.add(Ext.create('Ushine.cluesInfo.CluesGridPanel'));
				}else{
					Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				}
			},
			failure:function(response){
				var obj=Ext.JSON.decode(response.responseText);
				Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				Ext.getCmp('saveCluesWin').close();
			}
		});
	},
	//新增信息名称到临时库中
	saveTempClueDataName:function(sjnum,tempClueDataName,tempClueDataType){
		Ext.Ajax.request({
		    url: 'saveTempClueDataName.do',
		    actionMethods: {
                create : 'POST',
                read   : 'POST', // by default GET
                update : 'POST',
                destroy: 'POST'
            },
		    params: {
		    	tempClueDataName:tempClueDataName,
		    	tempClueDataType:tempClueDataType,
		    	sjnum:sjnum
		    },
		    success: function(response){
		    	 var text = response.responseText;
			     var obj=Ext.JSON.decode(text);
			     if(obj.status){
			    	 Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
			    	 var CluesTempDataGrid = Ext.getCmp('CluesTempDataGridId');
			    	 CluesTempDataGrid.removeAll();
			    	 CluesTempDataGrid.add(new Ushine.utils.CluesTempDataGridPanel(sjnum));
			     }
			     Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
		    },
            failure: function(form, action) {
            	Ext.create('Ushine.utils.Msg').onInfo('删除档案失败，请联系管理员');
            }
		});
	},
	//选择已有数据
	selectExistingData:function(type,sjnum){
		var data = [];
		var text='查询数据';
		var name='';
		//根据类型生产处查询字段
		if(type=='personStore'){
			text='查询人员';
			name='personName';
			data = [
    		    	//字段筛选
    		    	{"text":"任意字段", "value":"anyField"},
    		        {"text":"姓名", "value":"personName"},
    		        {"text":"曾用名", "value":"nameUsedBefore"},
    		        {"text":"英文名", "value":"englishName"},
    		        {"text":"现住地址", "value":"presentAddress"},
    		        {"text":"工作单位", "value":"workUnit"},
    		        {"text":"户籍地址", "value":"registerAddress"},
    		        {"text":"出生日期", "value":"bebornTime"}
    		    ];
		}else if(type=='organizStore'){
			name='organizName';
			data =[
		    	//字段筛选
		    	{"text":"任意字段", "value":"anyField"},
		        {"text":"组织名称", "value":"organizName"},
		        {"text":"组织负责人", "value":"orgHeadOfName"},
		        {"text":"网站地址", "value":"websiteURL"},
		        {"text":"活动范围", "value":"degreeOfLatitude"},
		        {"text":"成立时间", "value":"foundTime"}
		    ];
			text='查询组织';
		}else if(type=='websiteJournalStore'){
			name='name';
			data = [
             // 字段筛选
             {"text":"任意字段", "value":"anyField"},
             {"text": "刊物类别","value": "infoType" }, 
             {"text": "刊物名称","value": "name"},
             {"text": "域名","value": "websiteURL"},
             {"text": "服务器所在地","value": "serverAddress"},
             {"text": "创办地","value": "establishAddress"},
             {"text": "主要发行地","value": "mainWholesaleAddress"},
             {"text": "创办人","value": "establishPerson"},
             {"text": "创办时间","value": "establishTime"},
             {"text": "基本情况","value": "basicCondition"}
             ]
			text='查询刊物';
		}
		
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
	            		    data :data
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
									self.findDataByProperty(type);
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
		        	text : text,
		        	style:"margin-left:10px;",
		        	width:100,
		        	labelWidth: 60,
		        	height:22,
		        	id : 'search-Button1',
		        	baseCls: 't-btn-red',
		        	handler:function(){
		        		//查询
                        self.findDataByProperty(type);
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
				new Ushine.utils.SelectExistingDataGridPanel(type)]
				}];
				this.buttons=[
				      	  	Ext.create('Ushine.buttons.Button', {
				      	   		text: '确定',
				      	   		baseCls: 't-btn-red',
				      	   		handler: function () {
				      	   			var records = Ext.getCmp('SelectExistingDataGridPanelId').getSelectionModel().getSelection();
				      	   			var ids = [];
				      	   			var names = [];
				      	   			//提取已选择数据的id
				      	   			for(var i = 0; i < records.length; i++){  
					    			   ids.push(records[i].get("id"));
				      	   			}
				      	   			//提取已选择数据的名称
				      	   		   for(var i = 0; i < records.length; i++){  
				      	   			   //names.push(records[i].get("name"));
				      	   			   //替换掉html标签
				      	   			   var value=records[i].get(name).replace(/<[^>]+>/g,"");
				      	   			   names.push(value);
				      	   			}
				      	   		Ext.Ajax.request({
				      			    url: 'saveTempClueDataBySelect.do',
				      			    actionMethods: {
				      	                create : 'POST',
				      	                read   : 'POST', // by default GET
				      	                update : 'POST',
				      	                destroy: 'POST'
				      	            },
				      			    params: {
				      			    	ids:ids,
				      			    	names:names,
				      			    	type:type,
				      			    	number:sjnum
				      			    },
				      			    success: function(response){
				      			    	 var text = response.responseText;
				      				     var obj=Ext.JSON.decode(text);
				      				     if(obj.status){
				      				    	 Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				      				    	 var CluesTempDataGrid = Ext.getCmp('CluesTempDataGridId');
				      				    	 Ext.getCmp('SelectExistingDataWin').close();
				      				    	 CluesTempDataGrid.removeAll();
				      				    	 CluesTempDataGrid.add(new Ushine.utils.CluesTempDataGridPanel(sjnum));
				      				     }
				      				     Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				      			    },
				      	            failure: function(form, action) {
				      	            	Ext.create('Ushine.utils.Msg').onInfo('选择数据失败，请联系管理员');
				      	            }
				      			});
				      	   		}
				      	  	
				      	   	})
				      	  ];
				this.callParent();
			},//查询数据
			findDataByProperty:function(type){
				var win=this.getComponent(0);
				win.remove(Ext.getCmp('SelectExistingDataGridPanelId'))
				win.add(new Ushine.utils.SelectExistingDataGridPanel(type));
				//Ext.getCmp('selectexistingdata_pagingtoolbar').removeAll();
				//this.remove('selectExistingDataId');
				//this.add(Ext.create(new Ushine.utils.SelectExistingDataGridPanel(type)));
			},
		});
		Ext.create('SelectExistingDataWin').show();
	}
});
//修改线索内容的window
function editClueContent(){
	var win=Ext.getCmp('updateClueInfo_formpanel');
	var form=win.getComponent(0).getForm();
	//得到线索内容的field
	var field=form.findField('clueContent');
	var value=form.findField('clueContent').getValue();
	Ext.define('EditClueContentWin',{
		height:800,
		width:1000,
		extend:'Ext.window.Window',
		layout : 'fit',
		border : false,
		buttonAlign:"center",
		title:'修改线索内容',
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
	      	   			//修改账号	
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
	Ext.create('EditClueContentWin').show();
}
//修改线索进展情况的window
function editArrangeAndEvolveCondition(){
	var win=Ext.getCmp('updateClueInfo_formpanel');
	var form=win.getComponent(0).getForm();
	//得到进展情况的field
	var field=form.findField('arrangeAndEvolveCondition');
	var value=form.findField('arrangeAndEvolveCondition').getValue();
	Ext.define('EditArrangeAndEvolveConditionWin',{
		height:800,
		width:1000,
		extend:'Ext.window.Window',
		layout : 'fit',
		border : false,
		buttonAlign:"center",
		title:'修改线索部署及进展情况',
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
	      	   			//修改账号	
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
	Ext.create('EditArrangeAndEvolveConditionWin').show();
};
//定义一个SaveCluesWin的Window
Ext.define('SaveCluesWin',{
	extend:'Ushine.win.Window',
	title : "添加线索",
	modal : true,
	layout : 'fit',
	id:'saveCluesWin',
	border : false,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:790,
	width:570,
	constructor:function(){
		var self=this;
		
		this.items=[
			Ext.create('SaveCluesForm')
		];
		this.callParent();
	}
});