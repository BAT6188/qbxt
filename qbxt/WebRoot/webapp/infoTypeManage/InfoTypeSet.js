/**
 *信息类别管理面板
 * @author 王百林
 */
Ext.define('Ushine.infoTypeManage.InfoTypeSet', {
	extend: 'Ext.panel.Panel',
	itemId:'infoTypeSetId',
	region: 'center',
	title:'类别信息',
	bodyStyle: 'background-color: #ffffff; border: none; padding: 10px;',
	layout: {
		type: 'vbox',
		align : 'stretch',
		pack  : 'start'
	}, 
	constructor: function(tableTypeName,tableTypeText) {
		var self = this;
		this.items = [{
			// 工具栏
			xtype : 'panel',
		    baseCls : 'tar-body',
			height:30,
			style:"margin-top:-10px;",
			layout:'fit',
			border:true,
			// 工具栏 -- 左侧
			items:{
				//表单
				xtype:'form',
				border:true,
				height:30,
				id:'labl2',
				baseCls: 'form-body1',
				
				items:[{
		    	   layout: "column", //行1
					height: 30,
					baseCls: 'panel-body',
					items:[
				       Ext.create('Ushine.buttons.IconButton', {
				    	   border: false,
				    	   id: 'infoTypeCreateNewBtn',
				    	   btnText: '新增类别', 
				    	   baseCls: 't-btn-red',   //让按钮为黄色
				    	   handler: function() {
				    		   //Ext.create('Ushine.utils.Msg').onInfo('暂不支持!');
				    		   //alert(tableTypeName);
				    		   self.saveInfoType(tableTypeName,tableTypeText);
				    	   }
				       }),
				       Ext.create('Ushine.buttons.IconButton', {
				    	   border: false,
				    	   id: 'infoTypeUpdateBtn',
				    	   btnText: '修改类别', 
				    	   baseCls: 't-btn-red',   //让按钮为黄色
				    	   handler: function() {
				    		   var rolePanel=self.getComponent(2);
				    		   var records = rolePanel.getSelectionModel().getSelection();
				    		   if(records.length <= 0){
				    			   Ext.create('Ushine.utils.Msg').onInfo('请你选择一个类别进行修改');
				    		   }else if(records.length >1){
				    			   Ext.create('Ushine.utils.Msg').onInfo('只能选择一个类别进行修改');
				    		   }else{
				    			   //修改档案
				    			   self.updateInfoType(tableTypeName,tableTypeText,records[0].data);
				    		   }
				    	   }
				       }),	
				       Ext.create('Ushine.buttons.IconButton', {
				    	   id: 'infoTypeDelBtn',
				    	   btnText: '删除类别',
				    	   handler: function () {
				    		   var rolePanel=self.getComponent(2);
				    		   var records = rolePanel.getSelectionModel().getSelection();
				    		   
				    		   var ids = [];  //用户选择的笔迹档案id集合
				    		   for(var i = 0; i < records.length; i++){  
				    			   ids.push(records[i].data.id);
				    		   }
				    		   Ext.Msg.confirm('提示','确定要删除选中的类别吗?',function(btn) {
				    			   if (btn == 'yes') {
				    				   Ext.Ajax.request({
				    	    			    url: 'delInfoType.do',
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
				    	    			    	 var text = response.responseText;
				    		    			     var obj=Ext.JSON.decode(text);
				    		    			     if(obj.status){
				    		    			    	 Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				    		    			    	 Ext.getCmp('infoTypeSetGridPanelId').getStore().reload();
				    		    			     }
				    		    			     Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				    		    			     //删除成功后刷新人员、组织、文档的数据
				    		    			     if(Ext.getCmp('personInfoGridPanel')){
				    		    			     	//人员列表
				    		    			     	Ext.getCmp('personInfoGridPanel').getStore().reload();
				    		    			     	Ext.getCmp('personInfoGridPanel').getSelectionModel().clearSelections();
				    		    			     }
				    		    			     if(Ext.getCmp('organizeInfoGridPanel')){
				    		    			     	//组织列表
				    		    			     	Ext.getCmp('organizeInfoGridPanel').getStore().reload();
				    		    			     	Ext.getCmp('organizeInfoGridPanel').getSelectionModel().clearSelections();
				    		    			     }
				    		    			     if(Ext.getCmp('medianetworkbookgrid')){
				    		    			     	//媒体网站列表
				    		    			     	Ext.getCmp('medianetworkbookgrid').getStore().reload();
				    		    			     	Ext.getCmp('medianetworkbookgrid').getSelectionModel().clearSelections();
				    		    			     }
				    		    			     //servicedocgridpanel
				    		    			     if(Ext.getCmp('servicedocgridpanel')){
				    		    			     	//业务文档
				    		    			     	Ext.getCmp('servicedocgridpanel').getStore().reload();
				    		    			     	Ext.getCmp('servicedocgridpanel').getSelectionModel().clearSelections();
				    		    			     }
				    		    			     //foreigndocgridpanel
				    		    			     if(Ext.getCmp('foreigndocgridpanel')){
				    		    			     	//外来文档
				    		    			     	Ext.getCmp('foreigndocgridpanel').getStore().reload();
				    		    			     	Ext.getCmp('foreigndocgridpanel').getSelectionModel().clearSelections();
				    		    			     }
				    		    			     if(Ext.getCmp('leadertalkgridpanel')){
				    		    			     	//领导讲话
				    		    			     	Ext.getCmp('leadertalkgridpanel').getStore().reload();
				    		    			     	Ext.getCmp('leadertalkgridpanel').getSelectionModel().clearSelections();
				    		    			     }
				    	    			    },
				    	                    failure: function(form, action) {
				    	                    	Ext.create('Ushine.utils.Msg').onInfo('删除类别失败，请联系管理员');
				    	                    }
				    	    			});
				    			   }
				    		   });
				    	   }
				       }),
				       Ext.create('Ushine.buttons.IconButton', {
				    	   border: false,
				    	   id: 'infoTypeDataCount',
				    	   btnText: '数据统计', 
				    	   baseCls: 't-btn-yellow',   //让按钮为黄色
				    	   handler: function() {
				    		   //数据统计窗体
				    		  self.dataCountWin(tableTypeName,tableTypeText);
				    	   }
				       })
					]
				}]
				}
		},
		Ext.create('Ext.panel.Panel',{
			border: false,
			height: 10,
			baseCls: 'conent_title_thin',
			layout: {
				type: 'hbox',
				pack: 'start',
				align: 'stretch'
			}
		}),new Ushine.infoTypeManage.InfoTypeSetGridPanel(tableTypeName)
		];	
		this.callParent();		
	},
	dataCountWin:function(tableTypeName,tableTypeText){
		var win = Ext.create('Ushine.win.Window', {
			title : tableTypeText, // 标题
			width : 700, // 宽度
			height: 600,
			autoHeight : true, // 宽度
			collapsible : true, // 是否可以伸缩
			plain : true, // 强制背景色
			layout:'fit',
		//	iconCls : "houfei-addicon", // 图标样式
			modal : true, // 当前窗体为模态窗体
			bodyStyle: 'background-color: #ffffff; border: none;padding:10px;',
			items:[{
				flex:5,
				border:true,
				width:'100%',
				height:'100%',
				items:[{
			    	 animate: true,
			            style: 'background:#fff',
			            shadow: false,
			            xtype:'chart',
			            width:650,
			            height:500,
			            store : new Ext.data.JsonStore({
							proxy: new Ext.data.HttpProxy({
								url : "getInfoTypeDataCount.do?tableTypeName="+tableTypeName
							}),
							fields:['name', 'data'],
						    autoLoad:true,
						    autoDestroy: true
						}),
			            axes: [{
			                type: 'Numeric',
			                position: 'bottom',
			                fields: ['data'],
			                label: {
			                   renderer: Ext.util.Format.numberRenderer('0,0')
			                },
			                title: '',
			                minimum: 0
			            }, {
			                type: 'Category',
			                position: 'left',
			                fields: ['name'],
			                title: ''
			            }],
			            series: [{
			                type: 'bar',
			                axis: 'bottom',
			                label: {
			                    display: 'insideEnd',
			                    field: 'data',
			                    renderer: Ext.util.Format.numberRenderer('0'),
			                    orientation: 'horizontal',
			                    color: '#333',
			                    'text-anchor': 'middle',
			                    contrast: true
			                },
			                xField: 'name',
			                yField: ['data'],
			                //color renderer
			                renderer: function(sprite, record, attr, index, store) {
			                    var fieldValue = Math.random() * 20 + 10;
			                    var value = (record.get('data') >> 0) % 5;
			                    var color = ['rgb(213, 70, 121)', 
			                                 'rgb(44, 153, 201)', 
			                                 'rgb(146, 6, 157)', 
			                                 'rgb(49, 149, 0)', 
			                                 'rgb(49, 149, 0)'][value];
			                    return Ext.apply(attr, {
			                        fill: color
			                    });
			                }
			            }]
			    }]
			}]
		});
		win.show();
    },
	saveInfoType:function(tableTypeName,tableTypeText){
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
						fieldLabel:'所属类别:',
						allowBlank:false,
						xtype : 'textfield',
						disabled:true,
						//readonly:true,
						emptyText:'  ',
						blankText:'  ',
						width: 260,
						labelAlign : 'right',
						labelWidth : 75,
						height:22,
						name : 'tableTypeName',
						value:tableTypeText
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
						fieldLabel:'类别名称:',
						allowBlank:false,
						xtype : 'textfield',
						emptyText:'请输入类别名称',
						blankText:'此选项不能为空',
						width: 260,
						labelAlign : 'right',
						labelWidth : 75,
						height:22,
						name : 'typeName'
					}]}]
				}],
				buttons : [
			   		Ext.create('Ushine.buttons.Button', {
				   		text: '新增',
				   		baseCls: 't-btn-red',
				   		handler: function () {
				   			if(from.getForm().isValid()){
				   				Ext.Ajax.timeout=1000*60;
				   				var loadMask= new Ext.LoadMask(from, {msg:"正在新增..."});
				   				loadMask.show();
				   				Ext.Ajax.request({
				   					url:'saveInfoType.do',
				   					method:'POST',
				   					params : {
				   						tableTypeName:tableTypeName,
				   						typeName:from.getForm().findField('typeName').getValue()
				   					},
				   					success:function(response){
				   						loadMask.hide();
				   						var obj=Ext.JSON.decode(response.responseText);
				   						//判断是否新增成功，如失败显示失败原因
				   						if(obj.success){
				   							//状态等于0，失败
				   							if(obj.status==0){
				   								Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				   								return;
				   							}
				   							Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				   							Ext.getCmp('infoTypeSetGridPanelId').getStore().reload();
				   							Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				   							win.close();
				   						}else{
				   							Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				   							win.close();
				   						}
				   					},
				   					failure:function(){
				   						loadMask.hide();
				   						Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败");
			   							win.close();
				   					}
				   				})
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
			title : "新增类别", // 标题
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
	updateInfoType:function(tableTypeName,tableTypeText,data){
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
						fieldLabel:'所属类别:',
						allowBlank:false,
						xtype : 'textfield',
						disabled:true,
						//readonly:true,
						emptyText:'  ',
						blankText:'  ',
						width: 260,
						labelAlign : 'right',
						labelWidth : 75,
						height:22,
						name : 'tableTypeName',
						value:tableTypeText
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
						fieldLabel:'类别名称:',
						allowBlank:false,
						xtype : 'textfield',
						emptyText:'请输入类别名称',
						blankText:'此选项不能为空',
						width: 260,
						labelAlign : 'right',
						labelWidth : 75,
						height:22,
						name : 'typeName',
						value:data.typeName
					}]}]
				}],
				buttons : [
			   		Ext.create('Ushine.buttons.Button', {
				   		text: '修改',
				   		baseCls: 't-btn-red',
				   		handler: function () {
				   			if(from.getForm().isValid()){
				   				Ext.Ajax.timeout=1000*60;
				   				var loadMask= new Ext.LoadMask(from, {msg:"正在修改..."});
				   				loadMask.show();
				   				Ext.Ajax.request({
				   					url:'updateInfoType.do',
				   					method:'POST',
				   					params : {
				   						infoTypeId:data.id,
				   						typeName:from.getForm().findField('typeName').getValue()
				   					},
				   					success:function(response){
				   						loadMask.hide();
				   						var obj=Ext.JSON.decode(response.responseText);
				   						//判断是否新增成功，如失败显示失败原因
				   						if(obj.success){
				   							//状态等于0，失败
				   							if(obj.status==0){
				   								Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				   								return;
				   							}
				   							Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				   							Ext.getCmp('infoTypeSetGridPanelId').getStore().reload();
				   							Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				   							win.close();
				   							//修改成功后刷新人员、组织、文档的数据
			    		    			     if(Ext.getCmp('personInfoGridPanel')){
			    		    			     	//人员列表
			    		    			     	Ext.getCmp('personInfoGridPanel').getStore().reload();
			    		    			     	Ext.getCmp('personInfoGridPanel').getSelectionModel().clearSelections();
			    		    			     }
			    		    			     if(Ext.getCmp('organizeInfoGridPanel')){
			    		    			     	//组织列表
			    		    			     	Ext.getCmp('organizeInfoGridPanel').getStore().reload();
			    		    			     	Ext.getCmp('organizeInfoGridPanel').getSelectionModel().clearSelections();
			    		    			     }
			    		    			     if(Ext.getCmp('medianetworkbookgrid')){
			    		    			     	//媒体网站列表
			    		    			     	Ext.getCmp('medianetworkbookgrid').getStore().reload();
			    		    			     	Ext.getCmp('medianetworkbookgrid').getSelectionModel().clearSelections();
			    		    			     }
			    		    			     //servicedocgridpanel
			    		    			     if(Ext.getCmp('servicedocgridpanel')){
			    		    			     	//业务文档
			    		    			     	Ext.getCmp('servicedocgridpanel').getStore().reload();
			    		    			     	Ext.getCmp('servicedocgridpanel').getSelectionModel().clearSelections();
			    		    			     }
			    		    			     //foreigndocgridpanel
			    		    			     if(Ext.getCmp('foreigndocgridpanel')){
			    		    			     	//外来文档
			    		    			     	Ext.getCmp('foreigndocgridpanel').getStore().reload();
			    		    			     	Ext.getCmp('foreigndocgridpanel').getSelectionModel().clearSelections();
			    		    			     }
			    		    			     if(Ext.getCmp('leadertalkgridpanel')){
			    		    			     	//领导讲话
			    		    			     	Ext.getCmp('leadertalkgridpanel').getStore().reload();
			    		    			     	Ext.getCmp('leadertalkgridpanel').getSelectionModel().clearSelections();
			    		    			     }
				   						}else{
				   							Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				   							win.close();
				   						}
				   					},
				   					failure:function(){
				   						loadMask.hide();
				   						Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败");
			   							win.close();
				   					}
				   				})
				   			}
				   		}
				   	}),
				   Ext.create('Ushine.buttons.Button', {
				   		text: '重置',
				   		baseCls: 't-btn-yellow',
				   		handler: function () {
				   			from.getForm().reset();
				   		}
				   	})
			   ]
		});
		var win = Ext.create('Ushine.win.Window', {
			title : "修改类别", // 标题
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
		
	}
});
