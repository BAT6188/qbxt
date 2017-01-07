/**
 * 人员GridPanel
 */
Ext.define('Ushine.personInfo.PersonInfoGridPanel', {
	extend: 'Ext.grid.Panel',
	id:"personInfoGridPanel",
	flex: 1,
	itemId:'p_personinfo_grid',
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
	constructor: function(config) {
		var self = this;
		//console.log(config)
		var store = Ext.create('Ext.data.JsonStore',{
			//这里控制每页显示的数量
			pageSize:50,
			model:'PersonStoreModel',
			//storeId:'personinfostore',
			remoteSort:true,
			proxy:{
				type:'ajax',
				url:'findPersonStoreByConditions.do',
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
					//设置查询参数
					if(!options.params) options.params = {};
					options.params.field=Ext.getCmp('field').getValue();
					
					options.params.fieldValue=Ext.getCmp('fieldValue').getValue();
					
					options.params.startTime=Ext.getCmp('startTime').getValue();
					options.params.endTime=Ext.getCmp('endTime').getValue();
				}
			},
			autoLoad:true
		});
		this.columns=[
		    {text:'ID',dataIndex:'id',flex: 1,menuDisabled:true,hidden:true},
		    //{text: '是否启用',  dataIndex: 'isEnable',sortable: false,flex: 1,menuDisabled:true},
		    {text: '人员类别',  dataIndex: 'infoType',sortable: true,flex: 1,menuDisabled:true},
		    {text: '姓名',  dataIndex: 'personName',sortable: true,flex: 1,menuDisabled:true},
		    {text: '曾用名',  dataIndex: 'nameUsedBefore',sortable: true,flex: 1,menuDisabled:true},
		    {text: '英文名',  dataIndex: 'englishName',sortable: true,flex: 1 ,menuDisabled:true},
		    {text: '性别',  dataIndex: 'sex',sortable: true,flex: 0.5,menuDisabled:true},
		    //{text: '出生日期',  xtype: 'datecolumn',   format:'Y-m-d', dataIndex: 'bebornTime',sortable: false,flex: 1,menuDisabled:true},
		    {text: '出生日期',  dataIndex: 'bebornTime',sortable: true,flex: 1,menuDisabled:true,renderer:function(value){
		    	if(value.indexOf("T00")>0){
		    		value=value.replace("T00","");
			    	value=value.substring(0,value.length-6);
		    	}
		    	return value;
		    }},
		    {text: '现住地址',  dataIndex: 'presentAddress',sortable: true,flex: 1,menuDisabled:true},
		    {text: '工作单位',  dataIndex: 'workUnit',sortable: true,flex: 1,menuDisabled:true},
		    {text: '户籍地址',  dataIndex: 'registerAddress',sortable: true,flex: 1,menuDisabled:true},
		    {text: '数据创建时间',  sortable: true,dataIndex: 'createDate',flex: 1,menuDisabled:true,renderer:function(value){
	         	   return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }},
		    //{text: '履历',  dataIndex: 'antecedents',sortable: false,flex: 1,menuDisabled:true},
		    //{text: '活动情况',  dataIndex: 'activityCondition',sortable: false,flex: 1,menuDisabled:true},
		    {text: '下载',sortable: false,align:'center',flex:0.5,xtype:'actioncolumn',
		    	items:[{
		            icon: 'images/document_small_download.png',
		            tooltip: '保存为Word',
		            handler: function(grid, rowIndex, colIndex){
		            	var data = self.store.getAt(rowIndex).data;   //选中一行的数据
			            var personId  = data.id; 
			            try{
		            		location.href = "downloadPersonPDFFile.do?personId="+personId;
		            	}catch(e){
		                    Ext.Msg.alert('提示', "服务器连接失败，请联系管理员");
		            	}	
		            }
	             }],menuDisabled:true
		    }
		];
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
		this.selModel=new Ext.selection.CheckboxModel({
			//点击复选框才会选中,防止其他行被取消
			checkOnly:true
		});
		this.listeners={
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
			/*cellcontextmenu:function(thiz, td, cellIndex, record, tr, rowIndex, e, eOpts){
				var model=self.getSelectionModel();
				if(model.isSelected(record)){
					model.deselect(record);
				}
			},*/
			itemdblclick:function(obj,record,index, eOpts){
				//详细信息
				//console.log(record.get('infoType'));
				Ext.create('PersonInfoDetailWin',{
					record:record
				}).show();
			}
		};
		this.callParent();
	},
	listeners: {
		/*'cellcontextmenu':function( thiz, td, cellIndex, record, tr, rowIndex, e, eOpts){
			this.rightClick( thiz, td, cellIndex, record, tr, rowIndex, e, eOpts);
		}*/
	},rightClick:function( thiz, td, cellIndex, record, tr, rowIndex, e, eOpts){
		var self=this;
		if(this.menu){
			this.menu.hide(true);
			this.menu.removeAll(true);
		}else{
			this.menu = new Ext.menu.Menu();
		}
		this.menu.addCls('context-menu');
	    this.menu.add({
	    	text:'人员上级组织',
	    	cls:'context-btn',
	    	handler:function(){
	    		//record.data.id
	    		Ext.getCmp('content_frame').removeAll();
	        	Ext.getCmp('content_frame').add(new Ushine.personInfo.PersonAtHigherLevelOrganizeInfoManage(record.data.id));
	    	}
	    });
	    this.menu.showAt(e.getPoint());
	}
});

//数据模型
Ext.define('PersonStoreModel', {
	extend: 'Ext.data.Model',
    fields: [
             {name: 'id', type:'string', mapping: 'id'},
             {name: 'isEnable', type:'string', mapping: 'isEnable',convert:function(value,record){
             	  //console.log(value);
				  if(value=="1")return "否";           	
				  if(value=="2")return "是";           	
             }},
             {name: 'infoType', type:'string', mapping: 'infoType'},
             {name: 'personName', type:'string', mapping: 'personName'},
             {name: 'nameUsedBefore', type:'string', mapping: 'nameUsedBefore'},
             {name: 'englishName', type:'string', mapping: 'englishName'},
             {name: 'sex', type:'string', mapping: 'sex'},
             /*{name: 'bebornTime', type:'date', mapping: 'bebornTime',renderer:function(value){
             	//格式化日期
             	 return Ext.Date.format(value, 'Y-m-d');
             }},*/
             {name: 'bebornTime', type:'string', mapping: 'bebornTime'},
             {name: 'presentAddress', type:'string', mapping: 'presentAddress'},
             {name: 'workUnit', type:'string', mapping: 'workUnit'},
             {name: 'registerAddress', type:'string', mapping: 'registerAddress'},
             {name: 'antecedents', type:'string', mapping: 'antecedents'},
             {name: 'activityCondition', type:'string', mapping: 'activityCondition'},
             //把类型设置成json的
             {name: 'certificates', type:'json', mapping: 'certificates'},
             {name: 'networkaccount', type:'json', mapping: 'networkaccount'},
             //照片和附件
             {name: 'appendix', type:'string', mapping: 'appendix'},
             {name: 'photo', type:'string', mapping: 'photo'},
             {name: 'createDate', type:'date', mapping: 'createDate'}
             ],
    idProperty:'id'
});
//详细信息window
Ext.define('PersonInfoDetailWin',{
	extend:'Ushine.win.Window',
	title:'人员详细信息',
	modal : true,
	//gridpanel出现问题解决方式
	layout:{
		type : 'vbox',
		align:'stretch'
	},
	id:'personinfodetailwin',
	border : false,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:650,
	width:850,
	bodyPadding:8,
	autoScroll:true,
	constructor:function(config){
		var self=this;
		this.record=config.record;
		this.items=[{
			layout:'vbox',
			xtype:'fieldset',
			margin:'5 0 0 5',
			bodyPadding:8,
			title:'基本信息',
			autoScroll:true,
			items:[{
				xtype:"panel",
				border:false,
				layout:'hbox',
				//第一行
				items:[{
					xtype: 'displayfield',
				  	fieldLabel: '姓名',
				  	labelAlign : 'right',
				  	labelWidth : 60,
				  	width:250,
				  	value:config.record.get('personName')
				},{
					xtype: 'displayfield',
				  	fieldLabel: '曾用名',
			  		labelAlign : 'right',
			  		width:250,
				  	labelWidth : 60,
				  	value:config.record.get('nameUsedBefore')
				},{
					xtype: 'displayfield',
					labelAlign : 'right',
					width:250,
				  	labelWidth : 60,
				  	fieldLabel: '英文名',
				  	value:config.record.get('englishName')
				}]
			},{
				//第二行  人员类别，性别,出生日期
				xtype:"panel",
				border:false,
				layout:'hbox',
				items:[{
					xtype: 'displayfield',
					labelAlign : 'right',
					width:250,
				  	labelWidth : 60,
				  	fieldLabel: '人员类别',
				  	value:config.record.get('infoType')
				},{
					xtype: 'displayfield',
					labelAlign : 'right',
					width:250,
				  	labelWidth : 60,
				  	fieldLabel: '性别',
				  	value:config.record.get('sex')
				},{
					xtype: 'displayfield',
					labelAlign : 'right',
					width:250,
				  	labelWidth : 60,
				  	fieldLabel: '出生日期',
				  	//value:Ext.Date.format(config.record.get('bebornTime'),'Y-m-d')
				  	value:config.record.get('bebornTime')
				}]
			},{
				//第三行
				xtype:"panel",
				border:false,
				layout:'hbox',
				items:[{
					xtype: 'displayfield',
					labelAlign : 'right',
					width:250,
				  	labelWidth : 60,
				  	fieldLabel: '现住地址',
				  	value:config.record.get('presentAddress')
				},{
					xtype: 'displayfield',
					labelAlign : 'right',
					width:250,
				  	labelWidth : 60,
				  	fieldLabel: '户籍地址',
				  	value:config.record.get('registerAddress')
				},{
					xtype: 'displayfield',
					labelAlign : 'right',
					width:250,
				  	labelWidth : 60,
				  	fieldLabel: '工作单位',
				  	
				  	value:config.record.get('workUnit')
				}]
			},{
				//第三行
				xtype:"panel",
				border:false,
				layout:'vbox',
				items:[{
					xtype: 'displayfield',
					labelAlign : 'right',
				  	labelWidth : 60,
				  	fieldLabel: '履历',
				  	//width:'100%',
				  	flex:1,
				  	//value:config.record.get('antecedents')
				  	value:"<a href='javascript:void(0)' onclick=showDetail('antecedents')>" +
				  			"点击查看人员履历详细信息<a/>"
				  	//html:"<div>点击查看人员履历详细信息</div>"
				},{
					xtype: 'displayfield',
					labelAlign : 'right',
				  	labelWidth : 60,
			  		//width:'100%',
				  	flex:1,
				  	fieldLabel: '活动情况',
				  	value:"<a href='javascript:void(0)' onclick=showDetail('activityCondition')>" +
				  			"点击查看人员活动情况详细信息<a/>"
				  	//value:config.record.get('activityCondition')
				}]
			}]
		},{
			//证件和网络账号
			xtype:'fieldset',
			margin:'5 0 0 5',
			bodyPadding:8,
			height:220,
			title:'证件和网络账号',
			layout:{
				type:"hbox",
				align:"stretch"
			},
			items:[{
				xtype:'gridpanel',
				columns:[
					{text:'证件类型',dataIndex:'certificatesType',flex:1},
					{text:'证件号码',dataIndex:'certificatesTypeNumber',flex:1}
				],
				store:{
					model:'CertificatesTypeModel',
					data:config.record.get('certificates')
				},
				autoScroll:true,
				margin:'10 5 10 5',
				flex:1
			},{
				xtype:'gridpanel',
				columns:[
					{text:'网络账号类型',dataIndex:'networkAccountType',flex:1},
					{text:'网络账号',dataIndex:'networkAccountTypeNumber',flex:1}
				],
				store:{
					model:'NetworkAccountTypeModel',
					data:config.record.get('networkaccount')
				},
				autoScroll:true,
				margin:'10 5 10 5',
				flex:1
			}]
		},{
			//照片和附件
			xtype:'fieldset',
			margin:'5 0 0 5',
			bodyPadding:8,
			height:200,
			title:'照片及附件',
			layout:{
				type:"hbox",
				align:"stretch"
			},
			items:[{
				xtype:'panel',
				margin:'10 5 10 5',
				flex:1,
				baseCls : 'case-panel-body',
				html:"<span id='photospan'></span>",
				autoScroll:true,
				layout:'hbox',
				height:'100%',
				
				listeners:{
					afterrender:function(){
						//console.log(config.record.get('id'));
						//var view=self.getPersonPhoto(config.record.get('id'));
						//Ext.getCmp('personphotoimage').add(view);
						Ext.get('photospan').dom.innerHTML="双击放大图片";
					}
				},
				//加载图片
				items:[
					self.getPersonPhoto(config.record.get('id'))
				]
			},{
				xtype:'panel',
				margin:'10 5 10 5',
				flex:1,
				border:false,
				height:150,
				layout:'fit',
				items:[
					Ext.create('DownloadAttachGridPanel',{
						id:config.record.get('id')
					})		
				]
			}]
		}]
		this.callParent();
	},
	getPersonPhoto:function(id){
		var self=this;
		Ext.define('PersonPhotoModel',{
			extend:'Ext.data.Model',
			fields:['src']
		});
		Ext.create('Ext.data.Store',{
			model:'PersonPhotoModel',
			id:'imagesStore',
			proxy:{
				type:'ajax',
				url:'getPersonPhotoRealPath.do?id='+id,
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
		         '<img style="width:120px;height:120px;margin:15px 3px;" src="{src}"  />',
		    '</tpl>'
		);
		
		var com=Ext.create('Ext.view.View', {
		    store: Ext.data.StoreManager.lookup('imagesStore'),
		    tpl: imageTpl,
		    //必须的
		    itemSelector: 'img',
			listeners: {
				//双击放大图片
				itemdblclick:function(thiz, record, item, index, e,eOpts){
					var win=Ext.create('ZoomInPersonPhotoWin',{
						path:record.get('src')
					});
					win.show();
				}
			}
		});
		
		return com;
	}
});
//下载附件gridpanel
Ext.define('DownloadAttachGridPanel',{
	extend:'Ext.grid.Panel',
	border:true,
	stripeRows:false,		    //True来实现隔行换颜色
	autoHeight : true,
	rowLines:false,   //是否显示行分隔线
	disableSelection: false,   //是否禁止行选择，默认false
	columnLines:false,		   //添加列的框线样式
    loadMask: true,           //是否在加载数据时显示遮罩效果，默认为false
	defaults: {sortable: false},
	viewConfig:{
    	stripeRows:true,//在表格中显示斑马线
    	enableTextSelection:true //可以复制单元格文字
	},
	constructor:function(config){
		var self=this;
		this.columns=[
			{text:'附件名称',dataIndex:'name',flex: 3,menuDisabled:true},
			{text: '操作',sortable: false,width:130,flex: 1,xtype:'actioncolumn',items:[{
				//下载
				icon: 'images/document_small_download.png',
		        tooltip: '下载',
		        handler: function(grid, rowIndex, colIndex){
	            	//获取点击行的数据
	            	var data = self.store.getAt(rowIndex).data;
	            	//console.log(encodeURI(data.src));
	            	//两次编码
	            	window.open("downloadPersonAttach.do?src="+encodeURI(encodeURI(data.src)))
		        }
			}]}
		],
		this.store={
			fields:['src','name'],
			proxy:{
				type:'ajax',
				url:'getPersonAttachRealPath.do?id='+config.id,
				reader:{
					type:'json'
				}
			},
			autoLoad:true
		}
		this.callParent();
	}
})
//图片放大win
Ext.define('ZoomInPersonPhotoWin',{
	width:500,
	height:500,
	title:'照片',
	extend:'Ushine.win.Window',
	modal : true,
	border : false,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	layout:'fit',
	constructor:function(config){
		this.items=[{
			layout:'fit',
			xtype:'component',
			autoEl:{
				//指定为image标签
				tag:'img',
				//资源路径
				src:config.path  
			}
		}];
		this.callParent();
	}
});

//显示详细履历和活动情况信息
function showDetail(type){
	Ext.create('AntecedentsAndActivityConditionWin',{
		type:Ext.getCmp('personinfodetailwin').record.get(type)
	}).show();
	//alert(Ext.getCmp('personinfodetailwin').record.get(type))
}
//显示详细履历和活动情况的window
Ext.define('AntecedentsAndActivityConditionWin',{
	height:650,
	width:850,
	title:'详细信息',
	autoScroll:true,
	buttonAlign:"center",
	layout:'fit',
	border:false,
	maximizable:true,
	extend:'Ushine.win.Window',
	constructor:function(config){
		/*this.html=config.type;
		this.callParent();*/
		var self=this;
		this.listeners={
		'afterrender':function(){
				Ext.get('AntecedentsAndActivityCondition').dom.innerHTML=config.type;
			}
		},
		this.items=[{
			xtype:'panel',
			autoScroll:true,
			border:false,
			html:"<div id='AntecedentsAndActivityCondition'></div>"
		}];
		this.buttons=[
			Ext.create('Ushine.buttons.Button',{
				text: '打印',
		   		baseCls: 't-btn-red',
		   		handler:function(){
		   			$('#AntecedentsAndActivityCondition').printArea();
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
})