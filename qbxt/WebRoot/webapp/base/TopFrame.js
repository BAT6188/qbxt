/**
 * 头部信息
 * @author Franklin
 */
Ext.define('Ushine.base.TopFrame', {
	extend: 'Ext.panel.Panel',
	id: 'top_frame',
	region: 'north',
	height: 35,
	border: false,
	layout: {
		type: 'hbox',
		pack: 'start',
		align: 'stretch'
	},
	constructor: function(obj) {
		var me=this;
		this.items = [{
			// 左侧区域
			xtype: 'panel',
			flex: 1,
			border: false,
			bodyCls: 'top-body',
			bodyStyle: 'padding-left: 10px;',
			items: [
				// 警徽
				Ext.create('Ushine.buttons.OperButton', {
					id: 'logo'
				}),{
					xtype: 'label',
					height: 35,
					style: 'line-height: 30px; font-size: 16px; vertical-align: middle; margin:0px;font-family: 黑体;  font-weight:bold; color:#f5f5f5',
					text: "公安技侦情报管理系统"
				}
			]
		},{
			// 右边区域
			xtype: 'panel',
			flex: 1,
			border: false,
			bodyCls: 'top-body',
			bodyStyle: 'text-align: right; padding-right: 10px;',
			items: [{
			      xtype:'label',
			      style: 'line-height: 35px; font-size: 12px;padding: 5px 0px;color:#ffffff',
			      html:'<B>'+obj.name+'</B> 欢迎您,登录本系统！上次登录时间:'+obj.date
			      },
				// 个人操作
				Ext.create('Ushine.buttons.OperButton', {
					id: 'selfSetBtn',
					tooltip:'个人设置',
					handler:function(){
						me.setting();
					}
				}),
				// 个人操作
				Ext.create('Ushine.buttons.OperButton', {
					id: 'infoTypeManage',
					tooltip:'基础库类型管理',
					handler:function(){
						me.infTypeManage();
					}
				}),
				// PDF样式设置
				/*Ext.create('Ushine.buttons.OperButton', {
					id: 'PDFStyleSetUp',
					tooltip:'PDF样式设置',
					handler:function(){
						var m_class = "m-btn m-btn-hover x-item-selected"; // 未选中背景
						var a_class = "m-btn m-btn-hover x-item-selected m-btn-activity"; // 选中背景
						var c_class = "m-btn x-item-selected m-btn-activity";//这也是选中的背景
						//菜单栏数据
						var menu = Ext.getCmp('menu_frame').store.data.items;
						//菜单栏数据所对应的背景
						var menuItemss = Ext.query('.m-btn');
						for(var i =0;i<menuItemss.length;i++){
							if(a_class==menuItemss[i].getAttribute('class') || c_class==menuItemss[i].getAttribute('class')){
								me.PDFStyleSetUp(menu[i].data.url);
								return;
							}
						}
						
					}
				}),*/
				// 退出操作
				Ext.create('Ushine.buttons.OperButton', {
					id: 'logoutBtn',
					tooltip:'退出',
					handler:function(){
			    		Ext.create('Ushine.utils.Msg').onQuest("您确定要退出系统吗?",function(btn) {
							if (btn == 'yes') {
								Ext.Ajax.request({
									url:'logout.do',
								    success: function(response,request){
								    	//console.log(response);
								    	location.href='login.jsp';
								    },
						            failure: function(form, action) {
						            	Ext.create('Ushine.utils.Msg').onWarn("服务器连接失败，请联系管理员！");
						            }
								});
							}
						});
					}
				})
			]	
		}];
		
		this.callParent(); 
	},
	setting:function(){
		var win = Ext.create('Ushine.win.Window', {
			title : "个人设置", // 标题
			width : 500, // 宽度
			height: 300,
			autoHeight : true, // 宽度
			collapsible : true, // 是否可以伸缩
			plain : true, // 强制背景色
		//	iconCls : "houfei-addicon", // 图标样式
			modal : true, // 当前窗体为模态窗体
			bodyStyle: 'background-color: #ffffff; border: none;padding:10px;',
			items:{
            	xtype:'tabpanel',
            	items:[{
            		title:'修改密码',
            		items:Ext.create('Ushine.base.PasPanel')
    			}]
		 
			}
        });
		win.show();
    },
    infTypeManage:function(){
		var win = Ext.create('Ushine.win.Window', {
			title : "基础库类型管理", // 标题
			width : 700, // 宽度
			height: 600,
			autoHeight : true, // 宽度
			collapsible : true, // 是否可以伸缩
			plain : true, // 强制背景色
			layout:'fit',
		//	iconCls : "houfei-addicon", // 图标样式
			modal : true, // 当前窗体为模态窗体
			bodyStyle: 'background-color: #ffffff; border: none;padding:10px;',
			items:new Ushine.infoTypeManage.InfoTypeManage
		});
		win.show();
    },
    /**/
});

Ext.define('Ushine.base.PasPanel', {
	extend: 'Ext.panel.Panel',
	//bodyStyle: 'background-color: #f5f5f5;',
	border:false,
	constructor: function(){
		var self=this;
		this.items=new Ext.FormPanel({
			bodyStyle: 'padding: 10px',
			border:false,
			buttonAlign:"center",
			//defaults: {labelWidth: 60,width: 280,height:22},
			items:{ 
				title:'基本信息',
			    margins:'5 5 0 5',
			    xtype:'fieldset',
			    defaults: {width: 300},
				items:[{
	            	name: 'oldPass',
	        		xtype: 'textfield',
	        		fieldLabel: '旧密码',
	        		allowNegative: false,
	        		inputType:"password",
	        		allowBlank: false
	        	},{
	        		name: 'pass',
	        		xtype: 'textfield',
	        		fieldLabel: '新设密码',
	        		inputType:"password",
	        		allowBlank: false,
	        	},{
	        		name: 'pass1',
	        		xtype: 'textfield',
	        		fieldLabel: '确认密码',
	        		inputType:"password",
	        		allowBlank: false,
	        	}]
			},
			buttons:[{
				text: '保存信息',
				xtype:'u_btn',
				//margins:'0 0 0 80',
				baseCls: 't-btn-red',
		        handler: function(){
		        	//self.ownerCt.getComponent(0).getComponent(0).getForm()
		        	//console.log(self.getComponent(0).getForm());
		        	var form = self.getComponent(0).getForm();
		        	var btn=this;
		        	if (form.isValid()) {
		        		btn.disable();
		                form.submit({
		                	url:'setPassword.do',
		                    success: function(form, action) {
		                    		self.ownerCt.ownerCt.ownerCt.close();
		                    		Ext.create('Ushine.utils.Msg').onInfo(action.result.msg);
		                    },
		                    failure: function(form, action) {
		                    	switch (action.failureType) {
        			            case Ext.form.Action.CLIENT_INVALID:
        			            	Ext.create('Ushine.utils.Msg').onWarn('操作失败，请重试');
        			                break;
        			            case Ext.form.Action.CONNECT_FAILURE:
        			            	Ext.create('Ushine.utils.Msg').onWarn('连接服务器失败，请重试！');
        			                break;
        			            case Ext.form.Action.SERVER_INVALID:
        			            	Ext.create('Ushine.utils.Msg').onWarn(action.result.msg);
            			        }
		                    	btn.enable();
		                    }
		                });
		            }
		        }
			},{
				xtype:'u_btn',
				text: '重置',
				baseCls: 't-btn-yellow',
		        handler: function(){
		        	self.getComponent(0).getForm().reset();
		        }
			}]
		});
		this.callParent();
	}
});
