/**
 * 组织下属媒体刊物gridpanel
 */
Ext.define('Ushine.personInfo.OrganizSubordinatesNetworkGridPanel',{
	extend:'Ext.grid.Panel',
	id:'organizSuborinatesNetworkbookgrid',
	itemId:'d_organizSuborinatesNetworkbook_grid',
	height:300,
	flex:1,
	stripeRows:false,//true为隔行换颜色
	autoHeight:true,
	disableSelection:false,//是否禁止行选择
	columnLines:true, //列的框线样式
	loadMask:true,
	selType:'checkboxmodel',//复选框
	viewConfig:{
		emptyText:'没有数据',
		stripeRows:true,
		enableTextSelection:true
	},
	constructor:function(organizId){
		var self=this;
		var store=Ext.create('Ext.data.JsonStore',{
			pageSize:40,
			model:'OrganizSuborinatesNetworkBookModel',
			remoteStore:true,
			//请求后台服务
			proxy:{
				type:'ajax',
				url:'findOrganizNetworkBook.do?organizId='+organizId,
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
					var startTime = Ext.getCmp("startTime").value;  //开始时间
	                var endTime = Ext.getCmp("endTime").value;  //结束时间
	                startTime = Ext.Date.format(startTime, 'Y-m-d H:i:s');
	                endTime = Ext.Date.format(endTime, 'Y-m-d H:i:s');
	                var field = Ext.getCmp('field').getValue();
	                var fieldValue = Ext.getCmp('fieldValue').getValue();
					//设置查询参数
					if(!options.params) options.params = {};
					options.params.field=field;
					options.params.fieldValue=fieldValue;
					options.params.startTime=startTime;
					options.params.endTime=endTime;
				}
			},
			autoLoad:true
		});
		
		this.columns=[
			//设置列id
			{text:'ID',dataIndex:'id',flex: 1,menuDisabled:true,hidden:true},
		    {text: '类别',  dataIndex: 'infoType',sortable: false,flex: 1,menuDisabled:true},
		    {text: '刊物名称',  dataIndex: 'name',sortable: false,flex: 1,menuDisabled:true},
		    {text: '域名',  dataIndex: 'websiteURL',sortable: false,flex: 1 ,menuDisabled:true},
		    {text: '服务器所在地',  dataIndex: 'serverAddress',sortable: false,flex: 1,menuDisabled:true},
		    {text: '创办地',  dataIndex: 'establishAddress',sortable: false,flex: 1,menuDisabled:true},
		    {text: '主要发行地',  dataIndex: 'mainWholesaleAddress',sortable: false,flex: 1,menuDisabled:true},
		    {text: '创办人',  dataIndex: 'establishPerson',sortable: false,flex: 1,menuDisabled:true},
		    {text: '创办时间',  dataIndex: 'establishTime',sortable: false,flex: 1,menuDisabled:true},
		    {text: '数据创建时间',  dataIndex: 'createDate',width:160,menuDisabled:true,renderer:function(value){
	         	   return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }},
		    {text: '基本情况', hidden:true, dataIndex: 'basicCondition',sortable: false,flex: 1,menuDisabled:true}
		];
		this.listeners={
				//双击事件
				itemdblclick:function(thiz, record, item, index, e, eOpts){
					//详细信息
					//console.log(record);
					Ext.create('MediaNetworkDetailWindow',{
						record:record
					}).show();
				}
			};
		this.loadMask =true;
		//创建面板底部的工具条
		this.bbar = new Ext.PagingToolbar({
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
		this.callParent(); 
	}
})

//媒体网站文档模型
Ext.define('OrganizSuborinatesNetworkBookModel',{
	extend:'Ext.data.Model',
	fields:['id','infoType','name','clueName','domain','websiteURL','serverAddress'
		,'establishAddress','mainWholesaleAddress','establishPerson','establishTime','basicCondition',{name: 'createDate', type:'date', mapping: 'createDate'}]
})

//显示媒体网站详细信息
Ext.define('MediaNetworkDetailWindow',{
	extend:'Ushine.win.Window',
	title:'媒体网站详细信息',
	modal : true,
	//gridpanel出现问题解决方式
	layout:{
		type : 'vbox',
		align:'stretch'
	},
	id:'mediaNetworkDetailWindow',
	border : false,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:300,
	width:600,
	bodyPadding:20,
	autoScroll:true,
	constructor:function(config){
		this.config=config;
		this.items=[{
			//基本信息
			xtype:'fieldset',
			title:'基本信息',
			layout:'vbox',
			autoScroll:true,
			//height:100,
			flex:2,
			items:[{
				//第一行
				layout:'hbox',
				margin:'5 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'名称',
					value:config.record.get('name'),
					width: 250,
					labelAlign : 'right',
					labelWidth :90
				},{
					xtype:'displayfield',
					fieldLabel:'创办人',
					value:config.record.get('establishPerson'),
					width: 250,
					labelAlign : 'right',
					labelWidth :90
				}]
			},{
				//第二行
				layout:'hbox',
				margin:'5 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'类别',
					value:config.record.get('infoType'),
					width: 250,
					labelAlign : 'right',
					labelWidth :90
				},{
					xtype:'displayfield',
					fieldLabel:'主要发行地',
					value:config.record.get('mainWholesaleAddress'),
					width: 250,
					labelAlign : 'right',
					labelWidth :90
				}]
			},{
				//第三行
				layout:'hbox',
				margin:'5 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'域名',
					value:config.record.get('websiteURL'),
					width: 250,
					labelAlign : 'right',
					labelWidth :90,
					autoScroll:true
				},{
					xtype:'displayfield',
					fieldLabel:'服务器所在地',
					value:config.record.get('serverAddress'),
					width: 250,
					labelAlign : 'right',
					labelWidth :90
				}]
			},{
				//第四行
				layout:'hbox',
				margin:'5 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'创办地',
					value:config.record.get('establishAddress'),
					width: 250,
					labelAlign : 'right',
					labelWidth :90
				},{
					xtype:'displayfield',
					fieldLabel:'创办时间',
					value:config.record.get('establishTime'),
					width: 250,
					labelAlign : 'right',
					labelWidth :90
				}]
			},{
				//第五行
				layout:'hbox',
				margin:'5 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'基本情况',
					//value:config.record.get('time'),
					width: 250,
					labelAlign : 'right',
					labelWidth : 90,
					value:"<a href='javascript:void(0)' onclick=showMediaNetworkDetail()>" +
				  			"点击查看基本情况<a/>"
				}]
			}]
		}]
		this.callParent();
	}
});

