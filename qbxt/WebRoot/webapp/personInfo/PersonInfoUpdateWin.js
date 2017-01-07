/**
 * 修改人员信息的window
 * 证件、网络账号可以在gridpanel中修改
 */
//定义一个PersonInfoUpdateWin的Window
Ext.define('Ushine.personInfo.PersonInfoUpdateWin',{
	extend:'Ushine.win.Window',
	title : "修改人员信息",
	modal : true,
	layout : 'fit',
	border : false,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:620,
	width:840,
	constructor:function(){
		var self=this;
		
		this.items=[
			Ext.create('PersonInfoUpdateForm')
		];
		this.callParent();
	}
});

//定义一个PersonInfoUpdateForm修改人员信息表单
Ext.define('PersonInfoUpdateForm',{
	extend:'Ext.form.Panel',
	id:'saveperoninfo-formpanel',
	layout:'vbox',
    bodyPadding: 8,
    margin:0,
    border: false,
	baseCls: 'form-body',
	buttonAlign:"center",
	constructor:function(){
		var sjnum = parseInt(Math.random()*1000);//产生一个三位数的随机数  用于上传照片附件临时文件夹名称
		var self=this;
		this.items=[{   //第一行姓名，曾用名，英文名
			layout:'hbox',
	        bodyPadding: 8,
	        border: false,
			buttonAlign:"center",
			items:[{
				//一个隐藏域
				  xtype: 'hiddenfield',
        		  name: 'hidden_field_id'
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
				//blankText:'此选项不能为空',
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
				//blankText:'此选项不能为空',
				width: 250,
				labelWidth : 60,
				height:22,
				name : 'englishName'
			}]
		},{//第二行   人员类别，性别,出生日期
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
				    autoLoad:true
				    //autoDestroy: true
				}),
				name : 'infoType'
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
						{text:'男',value:'male'},
						{text:'女',value:'female'}
					]
				}),
				name : 'sex'
			},{
				fieldLabel:'出生日期',
				format: 'Y-m-d', 
				value:'2001-01-01',
				maxValue:new Date(),
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
				name : 'bebornTime'
			}]
			},{   
			//第三行   工作单位
				layout:'hbox',
		        bodyPadding: 8,
		        border: false,
				buttonAlign:"center",
				items:[{
					fieldLabel:'工作单位',
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
			},{
				fieldLabel:'现住地址:',
				margin:'0 0 0 20',
				labelStyle:'color:black;',
				//可以为空
				//allowBlank:false,
				xtype : 'textfield',
				labelAlign : 'right',
				emptyText:'请输入工作单位',
				//blankText:'此选项不能为空',
				width: 250,
				labelWidth : 60,
				height:22,
				name : 'presentAddress'
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
				margin:'0 0 0 20',
				name : 'registerAddress',
				listeners : {
					'focus' : function(thiz,the,eObj) {
						Ext.create('Ushine.utils.WinUtils').asingleCityWin(thiz,the,eObj);
					}
				}
			}]
		},{
			//履历
	        layout:'hbox',
	        bodyPadding: 8,
	        border: false,
	        margin:"0 0 0 0",
			buttonAlign:"center",
			items:[{
				fieldLabel:'履历',
				xtype : 'textarea',
				labelStyle:'color:red;',
				allowBlank:true,
				width: 788,
				labelAlign : 'right',
				emptyText:'请输入人员履历',
				labelWidth :60,
				height:60,
				name : 'antecedents'
			}]
	  	},{
			//活动情况
	        layout:'hbox',
	        bodyPadding: 8,
	        border: false,
	        margin:"0 0 0 0",
			buttonAlign:"center",
			items:[{
				fieldLabel:'活动情况',
				xtype : 'textarea',
				labelStyle:'color:red;',
				allowBlank:true,
				width: 788,
				labelAlign : 'right',
				emptyText:'请输入活动情况',
				labelWidth :60,
				height:60,
				name : 'activityCondition'
			}]
		  },{
				height:120,
				layout:'border',
				xtype:'fieldset',
				padding:'5 10 10 10',
				margin:'0 0 0 14',
				border:false,
				width:790,
				items:[{
		    	   height:25,
		    	   xtype:'form',
		    	   region:'north',
				   border : false,
		    	   baseCls : 'panel-body',
		    	   items:[{
		    		  xtype:'filefield',
		        	  name:'photo',
		        	  width:50,
		        	  margin:'0 0 0 -5',
		        	  msgTarget:'side',
		        	  //allowBlank:false,
		        	  buttonOnly:true,
		        	  buttonText:'上传附件',
		        	  listeners:{
		        		  afterrender:function(cmp){
		        			  cmp.fileInputEl.set({
		        				  accept:'file/*'
		        			  });
		        		  },
		        		  change:function(){
		  	        		  this.up("form").getForm().submit(
		    				  {
		    					  url:'personStoreFileUpload.do?number='+sjnum,
		    					  mothed:'POST',
		    					  waitMsg:'文件上传中',
		    					  success : function(form, action) {
		    		    			   var personStoreTemp = Ext.getCmp('personStoreTempFileId');
		    		    			   personStoreTemp.removeAll();
		    		    			   personStoreTemp.add(new Ushine.utils.PersonStoreFileGridPanel(sjnum));
		    					  },
		    					  // 提交失败的回调函数
		    					  failure : function() {
		    						  Ext.create('Ushine.utils.Msg').onInfo("服务器出现错误请稍后再试!");
		    					  }
		    				  });
		        		  }
		        	  }
		    	  	}]
				},{
						layout:'fit',
						xtype:'panel',
						region:'center',
						border:false,
						id:'personStoreTempFileId',
						items:Ext.create('Ushine.utils.PersonStoreFileGridPanel')
				}]
			},{
				height:120,
				layout:'border',
				xtype:'fieldset',
				padding:'5 10 10 10',
				margin:'0 0 0 14',
				border:false,
				width:794,
				items:[{
		    	   height:25,
		    	   xtype:'form',
		    	   region:'north',
				   border : false,
		    	   baseCls : 'panel-body',
		    	   items:[{
						xtype:'filefield',
						name:'photo',
						msgTarget:'side',
						width:80,
						margin:'0 0 0 -5',
						//allowBlank:false,
						buttonOnly:true,
						buttonText:'上传照片',
						listeners:{
							afterrender:function(cmp){
								cmp.fileInputEl.set({
									accept:'image/*'
								});
							}
							,'change':function(){
							var arr = this.value.split(".");
							var str = arr[arr.length-1];
							var types =["jpg","JPG","png"];
							var flag = false;
							Ext.Array.each(types,function(type){
								if(type==str){
									flag = true;
									return false;
								}
							})
							if(flag){
								this.up("form").getForm().submit({
									url:'personStorePhototUpload.do?number='+sjnum,
									mothed:'POST',
									waitMsg:'文件上传中',
									success : function(form, action) {
											var personStorePhotoImage = Ext.getCmp("personStorePhotoImage");
											personStorePhotoImage.add(self.update("tempPersonStorePhotoUploadImages"+sjnum+"/"+action.result.msg));
									},
									// 提交失败的回调函数
									failure : function() {
										Ext.create('Ushine.utils.Msg').onInfo("服务器出现错误请稍后再试!");
									}
								});
							}else{
								Ext.create('Ushine.utils.Msg').onInfo("图片格式不对!(仅支持jpg图片格式)");
							}
						}}
					}]
				},{
					layout:'fit',
					xtype:'panel',
					region:'center',
					border:false,
					id:'personStoreTempFilesId',
					width:784,
					items:[{
							height : 120,
							id:'personStorePhotoImage',
							baseCls : 'case-panel-body',
							margin : '0 0 0 0',
							width:784,
							html:'<p>没有照片</p>'
					}]
				}]
		}];
	  this.buttons=[
	  	Ext.create('Ushine.buttons.Button', {
	   		text: '修改',
	   		baseCls: 't-btn-red',
	   		handler: function () {
	   			var updateForm=self.getForm();
	   			//提交给后台处理
	   			//后台方法应该设置成post
	   		
	   			if(updateForm.isValid()){
					//添加遮罩	   				
	   				var loadMask=new Ext.LoadMask(self,{
	   					msg:'正在修改人员信息,请耐心等待……'
	   				});
	   				loadMask.show();
	   				updateForm.submit({
	   					url:'updatePersonStore.do?number='+sjnum,
	   					method:'post',
	   					success:function(){
	   						
	   					},
	   					failure:function(response){
	   						var obj=Ext.JSON.decode(response.responseText);
   							Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
	   					}
	   				});
	   				//设置超时时间,默认是30000毫秒;
	   				//如果在内部设置timeout：600000可能不会生效
	   				/*Ext.Ajax.timeout=600000;
	   				Ext.Ajax.request({
	   				//url:'savePersonStore.do?number='+sjnum,
	   				
	   				method:'post',
	   				
	   				success:function(response){
   						var obj=Ext.JSON.decode(response.responseText);
   						Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
   						//关掉父window
   						self.findParentByType('window').close();
   					},
   					failure:function(response){
   						var obj=Ext.JSON.decode(response.responseText);
   						Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
   						self.findParentByType('window').close();
   					}
	   				});*/
	   			}else{
	   				Ext.create('Ushine.utils.Msg').onInfo("请填写完整的人员信息");
	   			}
	   		}
	   	}),Ext.create('Ushine.buttons.Button', {
	   		text: '重置',
	   		margin:'0 0 0 35',
	   		baseCls: 't-btn-yellow',
	   		handler:function(){
	   			self.getForm().reset();
	   		}
	   })
	  ];
	  this.callParent();
	},
	update:function(path){
		var com = Ext.create('Ext.Component',{
					width: 77, //图片宽度  
		    		height: 77, //图片高度 
		    		padding:'2 0 0 10',
		    		autoEl: {  
						tag: 'img',    //指定为img标签  
						src:path
		    		}
				}
		);
		return com;
	}
});

