/**
 * 组织下属组织gridPanelgridpanel
 */
Ext.define('Ushine.personInfo.OrganizSubordinatesOrganizInfoGridPanel',{
	extend:'Ext.grid.Panel',
	id:'organizSubordinatesGrid',
	itemId:'oranizSubordinatesOrganiz_grid',
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
			model:'OrganizSubordinatesModel',
			remoteStore:true,
			//请求后台服务
			proxy:{
				type:'ajax',
				url:'findOrganizSubordinatesByOrgId.do?organizId='+organizId,
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
			{text:'ID',dataIndex:'id',flex: 1,menuDisabled:true,hidden:true},
		    {text: '组织名称',  dataIndex: 'organizName',sortable: false,flex: 1,menuDisabled:true},
		    {text: '组织负责人',  dataIndex: 'orgHeadOfName',sortable: false,flex: 1,menuDisabled:true},
		    {text: '组织类别',  dataIndex: 'infoType',sortable: false,flex: 1,menuDisabled:true},
		    {text: '网站地址',  dataIndex: 'websiteURL',sortable: false,flex: 1 ,menuDisabled:true},
		    {text: '创办刊物',  dataIndex: 'organizPublicActionNames',sortable: false,flex: 1,menuDisabled:true},
		    {text: '分支机构',  dataIndex: 'organizBranchesNames',sortable: false,flex: 1,menuDisabled:true},
		    {text: '主要成员',  dataIndex: 'organizPersonNames',sortable: false,flex: 1,menuDisabled:true},
		    {text: '成立时间',  dataIndex: 'foundTime',width:160,menuDisabled:true,renderer:function(value){
	         	   return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }},
		    {text: '活动范围',  dataIndex: 'degreeOfLatitude',sortable: false,flex: 1,menuDisabled:true},
		    {text: '数据创建时间',  dataIndex: 'createDate',width:160,menuDisabled:true,renderer:function(value){
	         	   return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }},
		    {text: '基本情况', hidden:true, dataIndex: 'basicCondition',sortable: false,flex: 1,menuDisabled:true},
		    {text: '活动情况', hidden:true, dataIndex: 'activityCondition',sortable: false,flex: 1,menuDisabled:true}
		];
		this.listeners={
				//双击事件
				itemdblclick:function(thiz, record, item, index, e, eOpts){
					//详细信息
					Ext.create('ServiceOrganizDetailWin',{
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
});
//数据模型
Ext.define('OrganizSubordinatesModel', {
	extend: 'Ext.data.Model',
    fields: [
             {name: 'id', type:'string', mapping: 'id'},
             {name: 'organizName', type:'string', mapping: 'organizName'},
             {name: 'orgHeadOfName', type:'string', mapping: 'orgHeadOfName'},
             {name: 'infoType', type:'string', mapping: 'infoType'},
             {name: 'clueName', type:'string', mapping: 'clueName'},
             {name: 'websiteURL', type:'string', mapping: 'websiteURL'},
             {name: 'organizPublicActionNames', type:'String', mapping: 'organizPublicActionNames'},
             {name: 'organizBranchesNames', type:'string', mapping: 'organizBranchesNames'},
             {name: 'organizPersonNames', type:'string', mapping: 'organizPersonNames'},
             {name: 'foundTime', type:'date', mapping: 'foundTime'},
             {name: 'degreeOfLatitude', type:'string', mapping: 'degreeOfLatitude'},
             {name: 'basicCondition', type:'string', mapping: 'basicCondition'},
             {name: 'activityCondition', type:'string', mapping: 'activityCondition'},
             {name: 'createDate', type:'date', mapping: 'createDate'}
             ],
    idProperty:'id'
});

