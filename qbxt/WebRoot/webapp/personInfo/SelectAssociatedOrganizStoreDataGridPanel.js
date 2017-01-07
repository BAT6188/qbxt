/**
 * 线索关联组织
 * @author 王百林
 */
Ext.define('Ushine.cluesInfo.SelectAssociatedOrganizStoreDataGridPanel', {
	extend: 'Ext.grid.Panel',
	//enableColumnResize:是否允许改变列宽，默认为true
	id:"SelectAssociatedOrganizStoreDataGridPanelId",
	itemId:'SelectAssociatedOrganizStoreDataId',
	border:true,
	height:200,
	hideHeaders:false,  //隐藏列头部
	stripeRows:true,		    //True来实现隔行换颜色
	autoHeight : true,
	rowLines:true,   //是否显示行分隔线
	disableSelection: false,   //是否禁止行选择，默认false
	columnLines:true,		   //添加列的框线样式
    loadMask: true,           //是否在加载数据时显示遮罩效果，默认为false
    selType: 'checkboxmodel', // 复选框
	defaults: {sortable: false},
	
	viewConfig:{
		emptyText: '没有数据',
    	stripeRows:true,//在表格中显示斑马线
    	enableTextSelection:true //可以复制单元格文字
	},
	constructor: function(type) {
		var self = this;
		this.store=Ext.create('Ext.data.JsonStore',{
			pageSize:50,
			fields:[{name : 'id',type : 'string',flex: 1,mapping : 'id'},
			        {name : 'organizName',type : 'string',flex: 1,mapping : 'organizName'},
			        {name : 'type',type : 'string',flex: 1,mapping : 'type'},
			        {name : 'websiteURL',type : 'string',flex: 1,mapping : 'websiteURL'},
			        {name : 'foundTime',type : 'string',flex: 1,mapping : 'foundTime'}
			],
			proxy:{
				type:'ajax',
				url:'selectOrganizData.do',
				method:'POST',
				simpleSortMode:true,
				reader:{
	                type: 'json',
	                root: 'datas'
				}
			},
			autoLoad:true
		});
		this.columns=[
		    {text:'id',dataIndex:'id',menuDisabled:true,hidden:true},
		    {text:'组织名称',dataIndex:'organizName',menuDisabled:true,width:140},
		    {text:'类别名称',dataIndex:'type',menuDisabled:true,width:140},
		    {text:'网站地址',dataIndex:'websiteURL',menuDisabled:true,width:140},
		    {text:'成立时间',dataIndex:'foundTime',menuDisabled:true,width:140}
		   ];
		//创建面板底部的工具条
		this.bbar = new Ext.PagingToolbar({//一个要和Ext.data.Store参与绑定并且自动提供翻页控制的工具栏
			store : this.store,  		//数据源
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
		this.callParent(); 
	}
});
