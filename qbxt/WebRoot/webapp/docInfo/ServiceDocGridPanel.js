/**
 * 业务文档gridpanel
 */
Ext.define('Ushine.docInfo.ServiceDocGridPanel',{
	extend:'Ext.grid.Panel',
	id:'servicedocgridpanel',
	height:300,
	itemId:'d_servicedoc_grid',
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
		var timeout=1000*60;
		var store=Ext.create('Ext.data.JsonStore',{
			pageSize:50,
			model:'ServiceDocModel',
			remoteSort:true,
			/*proxy:{
				type:'ajax',
				url:'findVocationalWorkStoreByConditions.do',
				simpleSortMode:true,
				reader:{
	                type: 'json',
	                root: 'datas',
					totalProperty:'paging.totalRecord'
				}
			},*/
		    //请求后台服务
			proxy:new Ext.data.proxy.Ajax({
				url:'findVocationalWorkStoreByConditions.do',
				simpleSortMode:true,
				//超时时间1分钟
				timeout:timeout,
				reader:{
	                type: 'json',
	                root: 'datas',
					totalProperty:'paging.totalRecord'
				}
			}),
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
			//设置列id
			{text:'id',dataIndex:'id',flex: 1,menuDisabled:true,hidden:true},
			{text:'attaches',dataIndex:'attaches',flex: 1,menuDisabled:true,hidden:true},
		    {text: '文档类别',  dataIndex: 'infoType',sortable: true,flex: 1,menuDisabled:true},
		    {text: '文档名称',  dataIndex: 'docName',sortable: true,flex: 2,menuDisabled:true},
		    {text: '涉及领域',  dataIndex: 'involvedInTheField',sortable: true,flex: 0.5,menuDisabled:true},
		    {text: '期刊号',  dataIndex: 'docNumber',sortable: true,flex: 0.5 ,menuDisabled:true},
		    {text: '建立时间',  dataIndex: 'time',sortable: true,flex: 1,menuDisabled:true,renderer:function(value){
		    	if(value.indexOf("T00")>0){
		    		value=value.replace("T00","");
			    	value=value.substring(0,value.length-6);
		    	}
		    	return value;
		    }},
		    {text: '数据创建时间',  dataIndex: 'createDate',sortable: true,width:160,menuDisabled:true,renderer:function(value){
	         	   return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }}
		    //{text: '原文',  dataIndex: 'theOriginal',sortable: false,flex: 1,menuDisabled:true},
		];
		this.loadMask =true;
		//创建面板底部的工具条
		this.bbar = new Ext.PagingToolbar({
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
			//双击事件
			itemdblclick:function(thiz, record, item, index, e, eOpts){
				//详细信息
				//console.log(record);
				Ext.create('ServiceDocDetailWin',{
					record:record
				}).show();
			}
		};
		this.callParent(); 
	}
})

//业务文档模型
Ext.define('ServiceDocModel',{
	extend:'Ext.data.Model',
	fields:['id','fileName','infoType','docName','docNumber','time','theOriginal',
	'involvedInTheField','attaches',
		{name: 'createDate', type:'date', mapping: 'createDate'},
		{name: 'filePath', type:'string', mapping: 'filePath'}],
	idProperty:'id'
})
//显示详细信息
Ext.define('ServiceDocDetailWin',{
	extend:'Ushine.win.Window',
	title:'业务文档详细信息',
	modal : true,
	//gridpanel出现问题解决方式
	layout:{
		type : 'vbox',
		align:'stretch'
	},
	id:'servicedocdetailwin',
	border : false,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:370,
	width:600,
	bodyPadding:10,
	autoScroll:true,
	constructor:function(config){
		this.config=config
		this.items=[{
			//基本信息
			xtype:'fieldset',
			title:'基本信息',
			layout:'vbox',
			autoScroll:true,
			//height:100,
			flex:3,
			items:[{
				//第一行
				layout:'hbox',
				margin:'10 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'文档名称',
					value:config.record.get('docName'),
					width: 250,
					labelAlign : 'right',
					labelWidth : 60
				},{
					xtype:'displayfield',
					fieldLabel:'期刊号',
					value:config.record.get('docNumber'),
					width: 250,
					labelAlign : 'right',
					labelWidth : 60
				}]
			},{
				//第二行
				layout:'hbox',
				margin:'10 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'类别',
					value:config.record.get('infoType'),
					width: 250,
					labelAlign : 'right',
					labelWidth : 60
				},{
					xtype:'displayfield',
					fieldLabel:'建立时间',
					value:config.record.get('time'),
					width: 250,
					labelAlign : 'right',
					labelWidth : 60
				}]
			},{
				//第三行
				layout:'hbox',
				margin:'10 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'原文',
					//value:config.record.get('time'),
					width: 250,
					labelAlign : 'right',
					labelWidth : 60,
					value:"<a href='javascript:void(0)' onclick=showServiceDocumentDetail()>" +
				  			"点击查看文档内容<a/>"
				},{
					xtype:'displayfield',
					fieldLabel:'涉及领域',
					value:config.record.get('involvedInTheField'),
					width: 250,
					labelAlign : 'right',
					labelWidth :60
				}]
			}]
		},{
			//查看附件
			xtype:'panel',
			margin:'5 0 0 0',
			flex:2,
			border:false,
			//height:120,
			layout:'fit',
			items:[
				Ext.create('DownloadSericeDocStoreAttachGridPanel',{
					id:config.record.get('id')
				})
			]
		}]
		this.callParent();
	}
});

//下载附件gridpanel
Ext.define('DownloadSericeDocStoreAttachGridPanel',{
	extend:'Ext.grid.Panel',
	border:true,
	stripeRows:false,		    //True来实现隔行换颜色
	autoHeight : true,
	//height:100,
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
			{text: '操作',sortable: false,menuDisabled:true,width:130,flex: 1,xtype:'actioncolumn',items:[{
				//下载
				icon: 'images/document_small_download.png',
		        tooltip: '下载',
		        handler: function(grid, rowIndex, colIndex){
	            	//获取点击行的数据
	            	var data = self.store.getAt(rowIndex).data;
	            	//console.log(encodeURI(data.src));
	            	//两次编码
	            	window.open("downloadServiceDocAttach.do?src="+encodeURI(encodeURI(data.src)))
		        }
			}]}
		],
		this.store={
			fields:['src','name'],
			proxy:{
				type:'ajax',
				url:'getSerciceDocAttachRealPath.do?id='+config.id,
				reader:{
					type:'json'
				}
			},
			autoLoad:true
		}
		this.callParent();
	}
});

//显示业务文档详情
function showServiceDocumentDetail(){
	//query函数能够获得多个符合条件的
	var window=Ext.getCmp('servicedocdetailwin');
	var config=window.config.record.data;
	var loadMask=new Ext.LoadMask(window,{
		msg:'正在获取原文内容……'
	});
	loadMask.show();
	Ext.Ajax.request({
		url:'getTheOriginal.do?id='+config.id,
		method:'get',
		success:function(response){
			loadMask.hide();
			var result=Ext.JSON.decode(response.responseText);
			
			Ext.create('ShowServiceDocumentTheOriginal',{
				docName:config.docName,
				theOriginal:result.msg
			}).show();
		},
		failure:function(){
			loadMask.hide();
			Ext.create('Ushine.utils.Msg').onInfo("获得原文内容失败");
		}
	})
}
//原文
Ext.define('ShowServiceDocumentTheOriginal',{
	height:370,
	width:600,
	autoScroll:true,
	extend:'Ushine.win.Window',
	/*constructor:function(config){
		this.title=config.docName+'原文内容',
		this.html=config.theOriginal;
		this.callParent();
	}*/
	buttonAlign:'center',
	border:false,
	layout:'fit',
	maximizable:true,
	constructor:function(config){
		/*this.html=config.type;
		this.callParent();*/
		var self=this;
		this.title=config.docName+'原文内容';
		this.listeners={
		'afterrender':function(){
				Ext.get('ServiceDocumentTheOriginal').dom.innerHTML=config.theOriginal;
			}
		},
		this.items=[{
			xtype:'panel',
			autoScroll:true,
			border:false,
			html:"<div id='ServiceDocumentTheOriginal'></div>"
		}];
		this.buttons=[
			Ext.create('Ushine.buttons.Button',{
				text: '打印',
		   		baseCls: 't-btn-red',
		   		handler:function(){
		   			$('#ServiceDocumentTheOriginal').printArea();
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

