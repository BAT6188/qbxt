/**
 * 线索库PDF设置panel
 */
Ext.define('Ushine.pDFStyleSetUp.CluePDFStyleManage',{
	extend:'Ext.panel.Panel',
	id:'clueStorePDFStylefomanage-info',
	title:'线索库PDF样式设置',
	bodyStyle: 'background-color: #ffffff; border: none; padding: 10px;',
	layout: {
		type: 'vbox',
		align : 'stretch',
		pack  : 'start'
	},
	constructor: function(url) {
		var self = this;
		this.items = [{
			// 工具栏
			xtype : 'panel',
		    baseCls : 'tar-body',
			height:44,
			style:"margin-top:-10px;",
			layout:'fit',
			items:{
				xtype:'form',
				border:true,
				height:44,
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
					    	   btnText: '设置样式', 
					    	   baseCls: 't-btn-red',
					    	   handler: function() {
					    		   Ext.Ajax.request({
			    					   url: 'getClueStorePDFStyle.do',
			    					   success: function(response){
			    						   var text = response.responseText;
			    						   var obj=Ext.JSON.decode(text);
			    						   if(obj.status!=0){
			    							   self.setUpPDFStyle(obj,url); 
			    						   }else{
			    							   Ext.create('Ushine.utils.Msg').onInfo(obj.msg); 
			    						   }
			    						   
			    					   },
			    					   failure: function(form, action) {
			    						   Ext.create('Ushine.utils.Msg').onInfo('获取当前样式失败，请联系管理员');
			    					   }
			    				   });
					    		   
					    	   }
					       }),
					        Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'updateBtn',
					    	   btnText: '重置样式',
					    	   handler: function () {
					    		   Ext.Ajax.request({
			    					   url: 'restClueStorePDFStyle.do',
			    					   success: function(response){
			    						   var text = response.responseText;
			    						   var obj=Ext.JSON.decode(text);
			    						   Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
			    						   //刷新页面
				   							Ext.getCmp('content_frame').removeAll();
											Ext.getCmp('content_frame').add(new Ushine.pDFStyleSetUp.PDFStyleSetUp('Ushine.pDFStyleSetUp.CluePDFStyleManage',url));
			    					   },
			    					   failure: function(form, action) {
			    						   Ext.create('Ushine.utils.Msg').onInfo('获取当前样式失败，请联系管理员');
			    					   }
			    				   });
					    	   }
					       }),
					       Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'uploadFont',
					    	   btnText: '上传字体',
					    	   handler: function () {
					    		  self.uploadFont();
					    	   }
					       }),
					       Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'dowPDFTemple',
					    	   btnText: '下载模板',
					    	   handler: function () {
					    		 //两次编码
					    		location.href = "downloadClueStorePDFTemplateFile.do";
					    	   }
					       })
					    ]}]
				}
		},{
				xtype:'panel',
				margin:'10 5 10 5',
				flex:1,
				id:'clueStoreTemplateImage',
				baseCls : 'case-panel-body',
				html:"<span id='photospan'></span>",
				autoScroll:true,
				layout:'vbox',
				height:'100%',
				listeners:{
					afterrender:function(){
						//console.log(config.record.get('id'));
						//var view=self.getPersonPhoto(config.record.get('id'));
						//Ext.getCmp('personphotoimage').add(view);
						//Ext.get('photospan').dom.innerHTML="双击放大图片";
					}
				},
				//加载图片
				items:[
					self.getClueStorePDFTemplate()
				]
				
		}
		];	
		this.callParent();		
	},
	//上传字体函数
	uploadFont:function(){
		var from = Ext.create('Ext.FormPanel',{
			layout : {
				type : 'vbox'
			},
			margin : '0 0 0 0',
			
			baseCls : 'form-body',
			buttonAlign : "center",
			height:470,
			border : false,
			items:[{
				region : 'center',  //案件记录
				height : 60,
				width:300,
				margin : '0 0 0 5',
				baseCls : 'case-panel-body',
				style:'border:0px solid red;',
				items : [{
					flex : 1,
					border : false,
					bodyStyle : "background: transparent;font-family: '新宋体', Arial;font-size: 12px;color: #333;border-color: red",
					padding:"0 5 0 5",
					layout : {
						type : 'hbox',
						align : 'stretch'
					},
					items : [{
						fieldLabel:'字体文件:',
						allowBlank:false,
						xtype : 'filefield',
						buttonText:'选择字体',
						emptyText:'请选择字体文件',
						blankText:'此选项不能为空',
						width: 260,
						labelAlign : 'right',
						labelWidth : 75,
						height:22,
						name : 'fontFile',
						id:'fontFile'
					}]},{
						flex : 1,
						border : false,
						bodyStyle : "background: transparent;font-family: '新宋体', Arial;font-size: 12px;color: #333;border-color: red",
						padding:"10 5 10 5",
						layout : {
							type : 'hbox',
							align : 'stretch'
					},
					items : [{
						fieldLabel:'字体名称:',
						allowBlank:false,
						xtype : 'textfield',
						emptyText:'请输入类别名称',
						blankText:'此选项不能为空',
						width: 260,
						labelAlign : 'right',
						labelWidth : 75,
						height:22,
						name : 'fontFileDesName',
						id:'fontFileDesName'
					}]}]
				}],
				buttons : [
			   		Ext.create('Ushine.buttons.Button', {
				   		text: '上传',
				   		baseCls: 't-btn-red',
				   		handler: function () {
				   			if(from.getForm().isValid()){
				   				var file = Ext.getCmp('fontFile').rawValue;
				   				var fontFileDesName = Ext.getCmp('fontFileDesName').lastValue;
				   				var fileSuffix = file.substring(file.length-3,file.length);
				   				if(fileSuffix == 'ttf' || fileSuffix == 'TTF'){
				   					from.getForm().submit({
				   						url:'uploadFontFile.do?fontFileDesName='+fontFileDesName,
										method:'POST',
										waitMsg:'字体上传中',
										timeout:1000*60*60,
										success:function(form,action){
											var obj=Ext.JSON.decode(action.response.responseText);
											console.log(obj);
				   							Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				   							win.close();
										},
										// 提交失败的回调函数
										failure : function() {
											Ext.create('Ushine.utils.Msg').onInfo("服务器出现错误请稍后再试!");
										}
									})
								
				   				
				   			}else{
				   				Ext.create('Ushine.utils.Msg').onInfo("字体文件不正确,后缀为TTF");
			   					return;
				   			}
				   			}
				   		}
				   	}),
				   Ext.create('Ushine.buttons.Button', {
				   		text: '重置',
				   		baseCls: 't-btn-yellow',
				   		handler: function () {
				   			from.getForm().reset();
				   		}
				   	}),
				   	
			   ]
		});
		var win = Ext.create('Ushine.win.Window', {
			title : "上传字体", // 标题
			width : 300, // 宽度
			height: 160,
			autoHeight : true, // 宽度
			collapsible : true, // 是否可以伸缩
			plain : true, // 强制背景色
			layout:'fit',
		//	iconCls : "houfei-addicon", // 图标样式
			modal : true, // 当前窗体为模态窗体
			bodyStyle: 'background-color: #ffffff; border: none;',
			items:from
		});
		win.show();
		
	},
	getClueStorePDFTemplate:function(){
		var self=this;
		Ext.define('CluePhotoModel',{
			extend:'Ext.data.Model',
			fields:['src']
		});
		Ext.create('Ext.data.Store',{
			model:'CluePhotoModel',
			id:'clueStorePDFTemplate',
			proxy:{
				type:'ajax',
				url:'getClueStorePDFTemplate.do',
				reader:{
					type:"json"
				}
			},
			//需要设置自动加载
			autoLoad:true
		});
		//'<span style="margin-left:0px;">双击放大图片</span><br/>',
		var imageTpl = new Ext.XTemplate(
		    '<tpl for=".">',
		         '<img style="width:700px;border:1px solid #67B2E2;height:1060px;margin:3px 3px;" src="{src}"  />',
		    '</tpl>'
		);
		
		var com=Ext.create('Ext.view.View', {
		    store: Ext.data.StoreManager.lookup('clueStorePDFTemplate'),
		    tpl: imageTpl,
		    //必须的
		    itemSelector: 'img',
			listeners: {
				//双击放大图片
				itemdblclick:function(thiz, record, item, index, e,eOpts){
					console.log(record.get('src'));
					var win=Ext.create('ZoomInPersonPhotoWin',{
						path:record.get('src')
					});
					win.show();
				}
			}
		});
		
		return com;
	},
	//设置pdf样式函数
	setUpPDFStyle:function(obj,url){
		Ext.define('setUpPDFStyleInfoWin',{
			extend:'Ushine.win.Window',
			title : "线索库PDF样式设置", // 标题
			width : 766, // 宽度
			height: 670,
			id:'cluePdfSet',
			autoHeight : true, // 宽度
			collapsible : true, // 是否可以伸缩
			plain : true,
			closable : true,
			draggable:true,
			resizable : false,
			plain : true, // 强制背景色
			layout:'vbox',
			buttonAlign:"center",
		//	iconCls : "houfei-addicon", // 图标样式
			modal : false, // 当前窗体为模态窗体
			border : false,
			bodyStyle: 'background-color: #ffffff; border: none;padding:5px;',
			constructor:function(){
				var self=this;
				var o=Ext.JSON.decode(obj.msg);
				this.items=[{
					id:'clueSetUpPDFInfoForm',
					xtype:'form',
					border:false,
					layout:'vbox',
					items:[{
					layout:'vbox',
					border: false,
					bodyPadding:'0 8 0 8',
					items:[Ext.create('Ushine.base.TitleBar1', {
		       		cTitle: '基础设置',
		       		btnItems: [
		       		     // 隐藏表单操作
		   					Ext.create('Ushine.buttons.MiniButton', {
		   						cls:'hideBtn',
		   						handler: function () {
		   							var from=Ext.getCmp('baseSetUpId1');
		   							var from2=Ext.getCmp('baseSetUpId2');
		   							var cluePdfSet = Ext.getCmp('cluePdfSet');
		   							if(from.hidden){
		   								this.removeCls("hideBtn");
		   								this.addCls("displayBtn");
		   								from.show();
		   								from2.show();
		   								Ext.getCmp('cluePdfSet').setWidth(cluePdfSet.width);
		   								Ext.getCmp('cluePdfSet').setHeight(cluePdfSet.height+63);
		   							}else{
		   								this.removeCls("displayBtn");
		   								this.addCls("hideBtn");
		   								from.hide();
		   								from2.hide();
		   								Ext.getCmp('cluePdfSet').setWidth(cluePdfSet.width);
		   								Ext.getCmp('cluePdfSet').setHeight(cluePdfSet.height-63);
		   							}
		   						}
		   					})
		       		        ]
		       	}),{
						//第二行   人员类别，性别,出生日期
						layout:'hbox',
					    bodyPadding:5,
					    border: false,
					    id:'baseSetUpId1',
					    buttonAlign:"center",
						items:[{
							fieldLabel:'标题间距',
							allowBlank:false,
							xtype : 'combo',
							emptyText:'标题间距',
							blankText:'此选项不能为空',
							width: 180,
							labelAlign : 'right',
							labelWidth : 76,
							//height:22,
							editable : false,
							store:Ext.create('Ext.data.Store',{
								fields:['value','text'],
								data:self.spacing()
							}),
							name : 'titleSpacing',
							value:o.titleSpacing
							},{
								fieldLabel:'左边边距',
								allowBlank:false,
								xtype : 'combo',
								emptyText:'左边边距',
								blankText:'此选项不能为空',
								width: 180,
								labelAlign : 'right',
								labelWidth : 76,
								//height:22,
								editable : false,
								store:Ext.create('Ext.data.Store',{
									fields:['value','text'],
									data:self.margin()
								}),
									name : 'leftSpacing',
									value:o.leftSpacing
								},{
									fieldLabel:'照片宽度',
									allowBlank:false,
									xtype : 'combo',
									emptyText:'照片宽度',
									blankText:'此选项不能为空',
									width: 180,
									labelAlign : 'right',
									labelWidth : 76,
									//height:22,
									editable : false,
									//类别应该加载后台数据
									store:Ext.create('Ext.data.Store',{
										fields:['value','text'],
										data:self.photoWidthAndHeight()
									}),
										name : 'photoWidth',
										value:o.photoWidth
									}]
						},{
							//第二行   人员类别，性别,出生日期
							layout:'hbox',
						    bodyPadding:5,
						    border: false,
						    id:'baseSetUpId2',
						    buttonAlign:"center",
							items:[{
								fieldLabel:'字段间距',
								allowBlank:false,
								xtype : 'combo',
								emptyText:'字段间距',
								blankText:'此选项不能为空',
								width: 180,
								labelAlign : 'right',
								labelWidth : 76,
								//height:22,
								editable : false,
								store:Ext.create('Ext.data.Store',{
									fields:['value','text'],
									data:self.spacing()
								}),
									name : 'fieldSpacing',
									value:o.fieldSpacing
								},{
									fieldLabel:'右边边距',
									allowBlank:false,
									xtype : 'combo',
									emptyText:'右边边距',
									blankText:'此选项不能为空',
									width: 180,
									labelAlign : 'right',
									labelWidth : 76,
									//height:22,
									editable : false,
									//类别应该加载后台数据
									store:Ext.create('Ext.data.Store',{
										fields:['value','text'],
										data:self.margin()
									}),
										name : 'rightSpacing',
										value:o.rightSpacing
									},{
										fieldLabel:'照片高度',
										allowBlank:false,
										xtype : 'combo',
										emptyText:'照片高度',
										blankText:'此选项不能为空',
										width: 180,
										labelAlign : 'right',
										labelWidth : 76,
										//height:22,
										editable : false,
										store:Ext.create('Ext.data.Store',{
											fields:['value','text'],
											data:self.photoWidthAndHeight()
										}),
											name : 'photoHeight',
											value:o.photoHeight
										}]
							}]
			},{
				layout:'vbox',
		        border: false,
				buttonAlign:"center",
				bodyPadding:'0 8 0 8',
				items:[Ext.create('Ushine.base.TitleBar1', {
		       		cTitle: '标题字体设置',
		       		btnItems: [// 隐藏表单操作
			   					Ext.create('Ushine.buttons.MiniButton', {
			   						cls:'hideBtn',
			   						handler: function () {
			   							var from=Ext.getCmp('titleFontSetUp');
			   							var cluePdfSet = Ext.getCmp('cluePdfSet');
			   							if(from.hidden){
			   								this.removeCls("hideBtn");
			   								this.addCls("displayBtn");
			   								from.show();
			   								Ext.getCmp('cluePdfSet').setWidth(cluePdfSet.width);
			   								Ext.getCmp('cluePdfSet').setHeight(cluePdfSet.height+33);
			   							}else{
			   								this.removeCls("displayBtn");
			   								this.addCls("hideBtn");
			   								from.hide();
			   								Ext.getCmp('cluePdfSet').setWidth(cluePdfSet.width);
			   								Ext.getCmp('cluePdfSet').setHeight(cluePdfSet.height-33);
			   							}
			   						}
			   					})
		       		        ]
		       	}),{
					//第二行   人员类别，性别,出生日期
					layout:'hbox',
				    bodyPadding:5,
				    border: false,
				    id:'titleFontSetUp',
				    buttonAlign:"center",
					items:[{
						fieldLabel:'字体模板',
						allowBlank:false,
						xtype : 'combo',
						emptyText:'字体模板',
						blankText:'此选项不能为空',
						width: 180,
						labelAlign : 'right',
						labelWidth : 76,
						valueField: 'value',
						//height:22,
						editable : false,
						//类别应该加载后台数据
						store : new Ext.data.JsonStore({
							proxy: new Ext.data.HttpProxy({
								url : "fontSelete.do"
							}),
							fields:['text', 'value'],
							   autoLoad:true,
							   autoDestroy: true
							}),
							name : 'titleFontTemplate',
							value:o.titleFontTemplate
						},{
							fieldLabel:'字体大小',
							allowBlank:false,
							xtype : 'combo',
							emptyText:'字体大小',
							blankText:'此选项不能为空',
							width: 180,
							labelAlign : 'right',
							labelWidth : 76,
							//height:22,
							editable : false,
							store:Ext.create('Ext.data.Store',{
								fields:['value','text'],
								data:self.fontSize()
							}),
								name : 'titleFontSize',
								value:o.titleFontSize
							},{
								fieldLabel:'是否加粗',
								allowBlank:false,
								xtype : 'combo',
								emptyText:'是否加粗',
								blankText:'此选项不能为空',
								width: 180,
								labelAlign : 'right',
								labelWidth : 76,
								//height:22,
								valueField: 'value',
								editable : false,
								store:Ext.create('Ext.data.Store',{
									fields:['value','text'],
									data:self.fontIsBoldIsCenter()
								}),
									name : 'titleFontIsBold',
									value:o.titleFontIsBold
								},{
									fieldLabel:'字体颜色',
									allowBlank:false,
									xtype : 'combo',
									emptyText:'字体颜色',
									blankText:'此选项不能为空',
									width: 180,
									labelAlign : 'right',
									labelWidth : 76,
									//height:22,
									valueField: 'value',
									editable : false,
									store:Ext.create('Ext.data.Store',{
										fields:['value','text'],
										data:self.fontColor()
									}),
										name : 'titleFontColor',
										value:o.titleFontColor
									}]
					}]
			},{
				layout:'vbox',
		        border: false,
				buttonAlign:"center",
				bodyPadding:'0 8 0 8',
				items:[Ext.create('Ushine.base.TitleBar1', {
		       		cTitle: '字段标题字体设置',
		       		btnItems: [// 隐藏表单操作
			   					Ext.create('Ushine.buttons.MiniButton', {
			   						cls:'hideBtn',
			   						handler: function () {
			   							var from=Ext.getCmp('fieldTtileFontSetUp');
			   							var cluePdfSet = Ext.getCmp('cluePdfSet');
			   							if(from.hidden){
			   								this.removeCls("hideBtn");
			   								this.addCls("displayBtn");
			   								from.show();
			   								Ext.getCmp('cluePdfSet').setWidth(cluePdfSet.width);
			   								Ext.getCmp('cluePdfSet').setHeight(cluePdfSet.height+33);
			   							}else{
			   								this.removeCls("displayBtn");
			   								this.addCls("hideBtn");
			   								from.hide();
			   								Ext.getCmp('cluePdfSet').setWidth(cluePdfSet.width);
			   								Ext.getCmp('cluePdfSet').setHeight(cluePdfSet.height-33);
			   							}
			   						}
			   					})
		       		        ]
		       	}),{
					//第二行   人员类别，性别,出生日期
					layout:'hbox',
				    bodyPadding:5,
				    border: false,
				    id:'fieldTtileFontSetUp',
				    buttonAlign:"center",
					items:[{
						fieldLabel:'字体模板',
						allowBlank:false,
						xtype : 'combo',
						valueField: 'value',
						emptyText:'字体模板',
						blankText:'此选项不能为空',
						valueField: 'value',
						width: 180,
						labelAlign : 'right',
						labelWidth : 76,
						//height:22,
						editable : false,
						//类别应该加载后台数据
						store : new Ext.data.JsonStore({
							proxy: new Ext.data.HttpProxy({
								url : "fontSelete.do"
							}),
							fields:['text', 'value'],
							   autoLoad:true,
							   autoDestroy: true
							}),
							name : 'fieldFontTemplate',
							value:o.fieldFontTemplate
						},{
							fieldLabel:'字体大小',
							allowBlank:false,
							xtype : 'combo',
							emptyText:'字体大小',
							blankText:'此选项不能为空',
							width: 180,
							labelAlign : 'right',
							labelWidth : 76,
							//height:22,
							editable : false,
							store:Ext.create('Ext.data.Store',{
								fields:['value','text'],
								data:self.fontSize()
							}),
								name : 'fieldFontSize',
								value:o.fieldFontSize
							},{
								fieldLabel:'是否加粗',
								allowBlank:false,
								xtype : 'combo',
								emptyText:'是否加粗',
								blankText:'此选项不能为空',
								width: 180,
								labelAlign : 'right',
								labelWidth : 76,
								//height:22,
								valueField: 'value',
								editable : false,
								store:Ext.create('Ext.data.Store',{
									fields:['value','text'],
									data:self.fontIsBoldIsCenter()
								}),
									name : 'fieldFontIsBold',
									value:o.fieldFontIsBold
								},{
									fieldLabel:'字体颜色',
									allowBlank:false,
									xtype : 'combo',
									emptyText:'字体颜色',
									blankText:'此选项不能为空',
									width: 180,
									labelAlign : 'right',
									labelWidth : 76,
									//height:22,
									valueField: 'value',
									editable : false,
									store:Ext.create('Ext.data.Store',{
										fields:['value','text'],
										data:self.fontColor()
									}),
										name : 'fieldFontColor',
										value:o.fieldFontColor
									}]
					}]
			},{
				layout:'vbox',
		        border: false,
				buttonAlign:"center",
				bodyPadding:'0 8 0 8',
				items:[Ext.create('Ushine.base.TitleBar1', {
		       		cTitle: '字段值字体设置',
		       		btnItems: [// 隐藏表单操作
			   					Ext.create('Ushine.buttons.MiniButton', {
			   						cls:'hideBtn',
			   						handler: function () {
			   							var from=Ext.getCmp('fieldValeFontSetUp');
			   							var cluePdfSet = Ext.getCmp('cluePdfSet');
			   							if(from.hidden){
			   								this.removeCls("hideBtn");
			   								this.addCls("displayBtn");
			   								from.show();
			   								Ext.getCmp('cluePdfSet').setWidth(cluePdfSet.width);
			   								Ext.getCmp('cluePdfSet').setHeight(cluePdfSet.height+33);
			   							}else{
			   								this.removeCls("displayBtn");
			   								this.addCls("hideBtn");
			   								from.hide();
			   								Ext.getCmp('cluePdfSet').setWidth(cluePdfSet.width);
			   								Ext.getCmp('cluePdfSet').setHeight(cluePdfSet.height-33);
			   							}
			   						}
			   					})
		       		        ]
		       	}),{
					//第二行   人员类别，性别,出生日期
					layout:'hbox',
				    bodyPadding:5,
				    border: false,
				    id:'fieldValeFontSetUp',
				    buttonAlign:"center",
					items:[{
						fieldLabel:'字体模板',
						allowBlank:false,
						xtype : 'combo',
						emptyText:'字体模板',
						blankText:'此选项不能为空',
						width: 180,
						valueField: 'value',
						labelAlign : 'right',
						labelWidth : 76,
						//height:22,
						editable : false,
						//类别应该加载后台数据
						store : new Ext.data.JsonStore({
							proxy: new Ext.data.HttpProxy({
								url : "fontSelete.do"
							}),
							fields:['text', 'value'],
							   autoLoad:true,
							   autoDestroy: true
							}),
							name : 'fieldValueFontTemplate',
							value:o.fieldValueFontTemplate
						},{
							fieldLabel:'字体大小',
							allowBlank:false,
							xtype : 'combo',
							emptyText:'字体大小',
							blankText:'此选项不能为空',
							width: 180,
							labelAlign : 'right',
							labelWidth : 76,
							//height:22,
							editable : false,
							store:Ext.create('Ext.data.Store',{
								fields:['value','text'],
								data:self.fontSize()
							}),
								name : 'fieldValueFontSize',
								value:o.fieldValueFontSize
							},{
								fieldLabel:'是否加粗',
								allowBlank:false,
								xtype : 'combo',
								emptyText:'是否加粗',
								blankText:'此选项不能为空',
								width: 180,
								labelAlign : 'right',
								labelWidth : 76,
								//height:22,
								valueField: 'value',
								editable : false,
								store:Ext.create('Ext.data.Store',{
									fields:['value','text'],
									data:self.fontIsBoldIsCenter()
								}),
									name : 'fieldValueFontIsBold',
									value:o.fieldValueFontIsBold
								},{
									fieldLabel:'字体颜色',
									allowBlank:false,
									xtype : 'combo',
									emptyText:'字体颜色',
									blankText:'此选项不能为空',
									width: 180,
									labelAlign : 'right',
									labelWidth : 76,
									//height:22,
									valueField: 'value',
									editable : false,
									store:Ext.create('Ext.data.Store',{
										fields:['value','text'],
										data:self.fontColor()
									}),
										name : 'fieldValueFontColor',
										value:o.fieldValueFontColor
									}]
					}]
			},,{
				layout:'vbox',
		        border: false,
				buttonAlign:"center",
				bodyPadding:'0 8 0 8',
				items:[Ext.create('Ushine.base.TitleBar1', {
		       		cTitle: '表格标题字体设置及单元格宽高设置',
		       		btnItems: [// 隐藏表单操作
			   					Ext.create('Ushine.buttons.MiniButton', {
			   						cls:'hideBtn',
			   						handler: function () {
			   							var from=Ext.getCmp('tableTtileFontSetUpAndCellWidthHeightSetUp1');
			   							var from2=Ext.getCmp('tableTtileFontSetUpAndCellWidthHeightSetUp2');
			   							var cluePdfSet = Ext.getCmp('cluePdfSet');
			   							if(from.hidden){
			   								this.removeCls("hideBtn");
			   								this.addCls("displayBtn");
			   								from.show();
			   								from2.show();
			   								Ext.getCmp('cluePdfSet').setWidth(cluePdfSet.width);
			   								Ext.getCmp('cluePdfSet').setHeight(cluePdfSet.height+63);
			   							}else{
			   								this.removeCls("displayBtn");
			   								this.addCls("hideBtn");
			   								from.hide();
			   								from2.hide();
			   								Ext.getCmp('cluePdfSet').setWidth(cluePdfSet.width);
			   								Ext.getCmp('cluePdfSet').setHeight(cluePdfSet.height-63);
			   							}
			   						}
			   					})
		       		        ]
		       	}),{
					//第二行   人员类别，性别,出生日期
					layout:'hbox',
				    bodyPadding:5,
				    id:'tableTtileFontSetUpAndCellWidthHeightSetUp1',
				    border: false,
				    buttonAlign:"center",
					items:[{
						fieldLabel:'字体模板',
						allowBlank:false,
						xtype : 'combo',
						emptyText:'字体模板',
						blankText:'此选项不能为空',
						width: 180,
						labelAlign : 'right',
						
						labelWidth : 76,
						valueField: 'value',
						//height:22,
						editable : false,
						//类别应该加载后台数据
						store : new Ext.data.JsonStore({
							proxy: new Ext.data.HttpProxy({
								url : "fontSelete.do"
							}),
							fields:['text', 'value'],
							   autoLoad:true,
							   autoDestroy: true
							}),
							name : 'tableTitleFontTemplate',
							value:o.tableTitleFontTemplate
						},{
							fieldLabel:'字体大小',
							allowBlank:false,
							xtype : 'combo',
							emptyText:'字体大小',
							blankText:'此选项不能为空',
							width: 180,
							labelAlign : 'right',
							labelWidth : 76,
							//height:22,
							valueField: 'value',
							editable : false,
							store:Ext.create('Ext.data.Store',{
								fields:['value','text'],
								data:self.fontSize()
							}),
								name : 'tableTitleFontSize',
								value:o.tableTitleFontSize
							},{
								fieldLabel:'是否居中',
								allowBlank:false,
								xtype : 'combo',
								emptyText:'是否居中',
								blankText:'此选项不能为空',
								width: 180,
								labelAlign : 'right',
								labelWidth : 76,
								//height:22,
								valueField: 'value',
								editable : false,
								store:Ext.create('Ext.data.Store',{
									fields:['value','text'],
									data:self.fontIsBoldIsCenter()
								}),
									name : 'tableTitleFontIsCenter',
									value:o.tableTitleFontIsCenter
								},{
									fieldLabel:'是否加粗',
									allowBlank:false,
									xtype : 'combo',
									emptyText:'是否加粗',
									blankText:'此选项不能为空',
									width: 180,
									labelAlign : 'right',
									labelWidth : 76,
									//height:22,
									
									valueField: 'value',
									editable : false,
									store:Ext.create('Ext.data.Store',{
										fields:['value','text'],
										data:self.fontIsBoldIsCenter()
									}),
										name : 'tableTitleFontIsBold',
										value:o.tableTitleFontIsBold
									}]
					},{
						//第二行   人员类别，性别,出生日期
						layout:'hbox',
					    bodyPadding:5,
					    border: false,
					    id:'tableTtileFontSetUpAndCellWidthHeightSetUp2',
					    buttonAlign:"center",
						items:[{
							fieldLabel:'字体颜色',
							allowBlank:false,
							xtype : 'combo',
							emptyText:'字体颜色',
							blankText:'此选项不能为空',
							width: 180,
							labelAlign : 'right',
							labelWidth : 76,
							//height:22,
							editable : false,
							valueField: 'value',
							store:Ext.create('Ext.data.Store',{
								fields:['value','text'],
								data:self.fontColor()
							}),
								name : 'tableTitleFontColor',
								value:o.tableTitleFontColor
							},{
								fieldLabel:'单元格宽度',
								allowBlank:false,
								xtype : 'combo',
								emptyText:'单元格宽度',
								blankText:'此选项不能为空',
								width: 180,
								labelAlign : 'right',
								labelWidth : 76,
								//height:22,
								valueField: 'value',
								editable : false,
								store:Ext.create('Ext.data.Store',{
									fields:['value','text'],
									data:self.cellWidth()
								}),
									name : 'tableCellWidth',
									value:o.tableCellWidth
								},{
									fieldLabel:'单元格高度',
									allowBlank:false,
									xtype : 'combo',
									emptyText:'单元格高度',
									blankText:'此选项不能为空',
									width: 180,
									labelAlign : 'right',
									labelWidth : 76,
									//height:22,
									valueField: 'value',
									editable : false,
									store:Ext.create('Ext.data.Store',{
										fields:['value','text'],
										data:self.cellHeight()
									}),
										name : 'tableCellHeight',
										value:o.tableCellHeight
									}]
						}]
			},,{
				layout:'vbox',
		        border: false,
				buttonAlign:"center",
				bodyPadding:'0 8 0 8',
				items:[Ext.create('Ushine.base.TitleBar1', {
		       		cTitle: '表格列标题字体设置',
		       		btnItems: [// 隐藏表单操作
			   					Ext.create('Ushine.buttons.MiniButton', {
			   						cls:'hideBtn',
			   						handler: function () {
			   							var from=Ext.getCmp('tableCoulmnTtileFontSetUp1');
			   							var from2=Ext.getCmp('tableCoulmnTtileFontSetUp2');
			   							var cluePdfSet = Ext.getCmp('cluePdfSet');
			   							if(from.hidden){
			   								this.removeCls("hideBtn");
			   								this.addCls("displayBtn");
			   								from.show();
			   								from2.show();
			   								Ext.getCmp('cluePdfSet').setWidth(cluePdfSet.width);
			   								Ext.getCmp('cluePdfSet').setHeight(cluePdfSet.height+63);
			   							}else{
			   								this.removeCls("displayBtn");
			   								this.addCls("hideBtn");
			   								from.hide();
			   								from2.hide();
			   								Ext.getCmp('cluePdfSet').setWidth(cluePdfSet.width);
			   								Ext.getCmp('cluePdfSet').setHeight(cluePdfSet.height-63);
			   							}
			   						}
			   					})
		       		        ]
		       	}),{
					//第二行   人员类别，性别,出生日期
					layout:'hbox',
				    bodyPadding:5,
				    border: false,
				    id:'tableCoulmnTtileFontSetUp1',
				    buttonAlign:"center",
					items:[{
						fieldLabel:'字体模板',
						allowBlank:false,
						xtype : 'combo',
						emptyText:'字体模板',
						blankText:'此选项不能为空',
						width: 180,
						labelAlign : 'right',
						labelWidth : 76,
						valueField: 'value',
						//height:22,
						editable : false,
						//类别应该加载后台数据
						store : new Ext.data.JsonStore({
							proxy: new Ext.data.HttpProxy({
								url : "fontSelete.do"
							}),
							fields:['text', 'value'],
							   autoLoad:true,
							   autoDestroy: true
							}),
							name : 'tableColumnTitleFontTemplate',
							value:o.tableColumnTitleFontTemplate
						},{
							fieldLabel:'字体大小',
							allowBlank:false,
							xtype : 'combo',
							emptyText:'字体大小',
							blankText:'此选项不能为空',
							width: 180,
							labelAlign : 'right',
							labelWidth : 76,
							//height:22,
							editable : false,
							store:Ext.create('Ext.data.Store',{
								fields:['value','text'],
								data:self.fontSize()
							}),
								name : 'tableColumnTitleFontSize',
								value:o.tableColumnTitleFontSize
							},{
								fieldLabel:'是否居中',
								allowBlank:false,
								xtype : 'combo',
								emptyText:'是否居中',
								blankText:'此选项不能为空',
								width: 180,
								labelAlign : 'right',
								labelWidth : 76,
								//height:22,
								editable : false,
								valueField: 'value',
								editable : false,
								store:Ext.create('Ext.data.Store',{
									fields:['value','text'],
									data:self.fontIsBoldIsCenter()
								}),
									name : 'tableColumnTitleFontIsCenter',
									value:o.tableColumnTitleFontIsCenter
								}]
					},{
						//第二行   人员类别，性别,出生日期
						layout:'hbox',
					    bodyPadding:5,
					    border: false,
					    id:'tableCoulmnTtileFontSetUp2',
					    buttonAlign:"center",
						items:[{
							fieldLabel:'字体颜色',
							allowBlank:false,
							xtype : 'combo',
							emptyText:'字体颜色',
							blankText:'此选项不能为空',
							width: 180,
							labelAlign : 'right',
							labelWidth : 76,
							//height:22,
							valueField: 'value',
							editable : false,
							store:Ext.create('Ext.data.Store',{
								fields:['value','text'],
								data:self.fontColor()
							}),
								name : 'tableColumnTitleFontColor',
								value:o.tableColumnTitleFontColor
							},{
								fieldLabel:'是否加粗',
								allowBlank:false,
								xtype : 'combo',
								emptyText:'是否加粗',
								blankText:'此选项不能为空',
								width: 180,
								labelAlign : 'right',
								labelWidth : 76,
								//height:22,
								valueField: 'value',
								editable : false,
								store:Ext.create('Ext.data.Store',{
									fields:['value','text'],
									data:self.fontIsBoldIsCenter()
								}),
									name : 'tableColumnTitleFontIsBold',
									value:o.tableColumnTitleFontIsBold
								}]
						}]
			},{
				layout:'vbox',
		        border: false,
				buttonAlign:"center",
				bodyPadding:'0 8 0 8',
				items:[Ext.create('Ushine.base.TitleBar1', {
		       		cTitle: '表格值字体设置',
		       		btnItems: [// 隐藏表单操作
			   					Ext.create('Ushine.buttons.MiniButton', {
			   						cls:'hideBtn',
			   						handler: function () {
			   							var from=Ext.getCmp('tableValueFontSetUp1');
			   							var from2=Ext.getCmp('tableValueFontSetUp2');
			   							var cluePdfSet = Ext.getCmp('cluePdfSet');
			   							if(from.hidden){
			   								this.removeCls("hideBtn");
			   								this.addCls("displayBtn");
			   								from.show();
			   								from2.show();
			   								Ext.getCmp('cluePdfSet').setWidth(cluePdfSet.width);
			   								Ext.getCmp('cluePdfSet').setHeight(cluePdfSet.height+63);
			   							}else{
			   								this.removeCls("displayBtn");
			   								this.addCls("hideBtn");
			   								from.hide();
			   								from2.hide();
			   								Ext.getCmp('cluePdfSet').setWidth(cluePdfSet.width);
			   								Ext.getCmp('cluePdfSet').setHeight(cluePdfSet.height-63);
			   							}
			   						}
			   					})
		       		        ]
		       	}),{
					//第二行   人员类别，性别,出生日期
					layout:'hbox',
				    bodyPadding:5,
				    border: false,
				    id:'tableValueFontSetUp1',
				    buttonAlign:"center",
					items:[{
						fieldLabel:'字体模板',
						allowBlank:false,
						xtype : 'combo',
						emptyText:'字体模板',
						blankText:'此选项不能为空',
						width: 180,
						labelAlign : 'right',
						labelWidth : 76,
						valueField: 'value',
						//height:22,
						editable : false,
						//类别应该加载后台数据
						store : new Ext.data.JsonStore({
							proxy: new Ext.data.HttpProxy({
								url : "fontSelete.do"
							}),
							fields:['text', 'value'],
							   autoLoad:true,
							   autoDestroy: true
							}),
							name : 'tableValueFontTemplate',
							value:o.tableValueFontTemplate
						},{
							fieldLabel:'字体大小',
							allowBlank:false,
							xtype : 'combo',
							emptyText:'字体大小',
							blankText:'此选项不能为空',
							width: 180,
							labelAlign : 'right',
							labelWidth : 76,
							//height:22,
							editable : false,
							store:Ext.create('Ext.data.Store',{
								fields:['value','text'],
								data:self.fontSize()
							}),
								name : 'tableValueFontSize',
								value:o.tableValueFontSize
							},{
								fieldLabel:'是否居中',
								allowBlank:false,
								xtype : 'combo',
								emptyText:'是否居中',
								blankText:'此选项不能为空',
								width: 180,
								labelAlign : 'right',
								labelWidth : 76,
								//height:22,
								valueField: 'value',
								editable : false,
								store:Ext.create('Ext.data.Store',{
									fields:['value','text'],
									data:self.fontIsBoldIsCenter()
								}),
									name : 'tableValueFontIsCenter',
									value:o.tableValueFontIsCenter
								}]
					},{
						//第二行   人员类别，性别,出生日期
						layout:'hbox',
					    bodyPadding:5,
					    border: false,
					    id:'tableValueFontSetUp2',
					    buttonAlign:"center",
						items:[{
							fieldLabel:'字体颜色',
							allowBlank:false,
							xtype : 'combo',
							emptyText:'字体颜色',
							blankText:'此选项不能为空',
							width: 180,
							labelAlign : 'right',
							labelWidth : 76,
							//height:22,
							valueField: 'value',
							editable : false,
							store:Ext.create('Ext.data.Store',{
								fields:['value','text'],
								data:self.fontColor()
							}),
								name : 'tableValueFontColor',
								value:o.tableValueFontColor
							},{
								fieldLabel:'是否加粗',
								allowBlank:false,
								xtype : 'combo',
								emptyText:'是否加粗',
								blankText:'此选项不能为空',
								width: 180,
								labelAlign : 'right',
								labelWidth : 76,
								//height:22,
								valueField: 'value',
								editable : false,
								store:Ext.create('Ext.data.Store',{
									fields:['value','text'],
									data:self.fontIsBoldIsCenter()
								}),
									name : 'tableValueFontIsBold',
									value:o.tableValueFontIsBold
								}]
						}]
		
				}]
				}];
				this.buttons=[
				  	  		Ext.create('Ushine.buttons.Button', {
				  	   			text: '保存',
				  	   			baseCls: 't-btn-red',
				  	   			//id:'addOrUpdatePersonInfo'
				  	   			handler:function(){
				  	   			var from = Ext.getCmp('clueSetUpPDFInfoForm');
				  	   			if(from.getForm().isValid()){
					   					from.getForm().submit({
					   						url:'setUpCluePDFStyle.do',
											method:'POST',
											waitMsg:'字体设置中',
											success:function(form,action){
												var obj=Ext.JSON.decode(action.response.responseText);
					   							Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
					   							//刷新页面
					   							Ext.getCmp('content_frame').removeAll();
												Ext.getCmp('content_frame').add(new Ushine.pDFStyleSetUp.PDFStyleSetUp('Ushine.pDFStyleSetUp.CluePDFStyleManage',url));
					   							Ext.getCmp('cluePdfSet').close();
											},
											// 提交失败的回调函数
											failure : function() {
												Ext.create('Ushine.utils.Msg').onInfo("服务器出现错误请稍后再试!");
											}
										})
								
					   			}
				  	   			}
				  	   		}),Ext.create('Ushine.buttons.Button', {
				  	   			text: '重置',
				  	   			margin:'0 0 0 35',
				  	   			baseCls: 't-btn-yellow',
				  	   			id:'resetPersonInfo777'
				  	   		})
				  	 	];
			this.callParent();
			},
			//边距函数
			margin:function(){
				var data = [];
				for(var i = 1;i <= 150; i++){
					var json = {};
					json['value'] = i;
					json['text'] = i;
					data.push(json);
					i = i+4;
				}
				return data;
			},
			//间距函数
			spacing:function(){
				var data = [];
				for(var i = 1;i <= 60; i++){
					var json = {};
					json['value'] = i;
					json['text'] = i;
					data.push(json);
				}
				return data;
			},
			//照片宽度与照片高度
			photoWidthAndHeight:function(){
				var data = [];
				for(var i = 100;i <= 300; i++){
					var json = {};
					json['value'] = i;
					json['text'] = i;
					data.push(json);
					i = i+4;
				}
				return data;
			},
			//字体大小
			fontSize:function(){
				var data = [];
				for(var i = 10;i <= 28; i++){
					var json = {};
					json['value'] = i;
					json['text'] = i;
					data.push(json);
				}
				return data;
				
			},
			//是否加粗
			fontIsBoldIsCenter:function(){
				var data = [
				            {'value':'true','text':'是'},
				            {'value':'false','text':'否'}
				            ];
				return data;
			},
			//字体颜色
			fontColor:function(){
				var data = [
				            {'value':'BLACK','text':'黑色'},
				            {'value':'BLUE','text':'蓝色'},
				            {'value':'CYAN','text':'青色'},
				            {'value':'DARK_GRAY','text':'深灰'},
				            {'value':'GRAY','text':'灰色'},
				            {'value':'GREEN','text':'绿色'},
				            {'value':'LIGHT_GRAY','text':'浅灰色'},
				            {'value':'MAGENTA','text':'品红色'},
				            {'value':'ORANGE','text':'橙色'},
				            {'value':'PINK','text':'粉红色'},
				            {'value':'RED','text':'红色'},
				            {'value':'WHITE','text':'白色'},
				            {'value':'YELLOW','text':'黄色'}
				            ];
				return data;
			},
			//单元格宽
			cellWidth:function(){
				var data = [];
				for(var i = 12;i <= 40; i++){
					var json = {};
					json['value'] = i;
					json['text'] = i;
					data.push(json);
				}
				return data;
			},
			//单元格高
			cellHeight:function(){
				var data = [];
				for(var i = 1;i <= 20; i++){
					var json = {};
					json['value'] = i;
					json['text'] = i;
					data.push(json);
				}
				return data;
			}
			
		});
		
		Ext.create('setUpPDFStyleInfoWin').show();
	}
	
});


