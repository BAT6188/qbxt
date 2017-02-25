/**
 * 一键搜索的业务文档gridpanel
 */
Ext.define('Ushine.dataSearch.VocationalDocStoreSearchGridPanel',{
	extend:'Ext.grid.Panel',
	id:'vocationaldocstoresearchgridpanel',
	height:300,
	itemId:'d_vocationaldocstoresearch_gridpanel',
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
	constructor:function(config){
		var self=this;
		//超时1分钟
		var timeout=1000*60;
		var store=Ext.create('Ext.data.JsonStore',{
			pageSize:50,
			model:'ServiceDocModel',
			remoteSort:true,
		    //请求后台服务
			proxy:new Ext.data.proxy.Ajax({
				url:'findVocationalWorkStoreByConditions.do',
				simpleSortMode:true,
				//超时时间尽量长
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
					var startTime = Ext.getCmp("startTime").value;  //开始时间
	                var endTime = Ext.getCmp("endTime").value;  //结束时间
	                startTime = Ext.Date.format(startTime, 'Y-m-d H:i:s');
	                endTime = Ext.Date.format(endTime, 'Y-m-d H:i:s');
	                var field = Ext.getCmp('field').getValue();
	                var fieldValue = Ext.getCmp('fieldValue').getValue();
	                if(config!=undefined){
	                	 fieldValue = config.fieldValue+"&&"+config.nowFieldValue;
	                }
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
			{text:'id',dataIndex:'id',flex: 1,menuDisabled:true,hidden:true},
		    {text: '文档类别',  dataIndex: 'infoType',sortable: true,flex: 1,menuDisabled:true},
		    {text: '文档名称',  dataIndex: 'docName',sortable: true,flex: 2,menuDisabled:true},
		    //{text: '涉及领域',  dataIndex: 'involvedInTheField',sortable: true,flex:0.5,menuDisabled:true},
		    {text: '期刊号',  dataIndex: 'docNumber',sortable: true,flex: 0.5 ,menuDisabled:true},
		    {text: '建立时间',  dataIndex: 'time',sortable: true,flex: 1,menuDisabled:true,renderer:function(value){
		    	if(value.indexOf("T00")>0){
		    		value=value.replace("T00","");
			    	value=value.substring(0,value.length-6);
		    	}
		    	return value;
		    }},
		    {text: '数据创建时间',sortable: true,dataIndex: 'createDate',flex:1,menuDisabled:true,renderer:function(value){
	         	   return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }}
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
		this.listeners={
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
