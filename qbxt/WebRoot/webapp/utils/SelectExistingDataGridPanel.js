/**
 * 档案管理数据面板
 * @author 王百林
 */
Ext.define('Ushine.utils.SelectExistingDataGridPanel', {
	extend: 'Ext.grid.Panel',
	//enableColumnResize:是否允许改变列宽，默认为true
	id:"SelectExistingDataGridPanelId",
	itemId:'selectExistingDataId',
	border:true,
	height:340,
	hideHeaders:false,  //隐藏列头部
	stripeRows:true,		    //True来实现隔行换颜色
	autoHeight : true,
	rowLines:true,   //是否显示行分隔线
	disableSelection: false,   //是否禁止行选择，默认false
	columnLines:true,		   //添加列的框线样式
    loadMask: true,           //是否在加载数据时显示遮罩效果，默认为false
    //selType: 'checkboxmodel', // 复选框
	defaults: {sortable: false},
	
	viewConfig:{
		emptyText: '没有数据',
    	stripeRows:true,//在表格中显示斑马线
    	enableTextSelection:true //可以复制单元格文字
	},
	constructor: function(type) {
		var self = this;
		var url='';
		//字段类型
		var name=''
		if(type=='personStore'){
			url="findPersonStoreByConditions.do";
			name='personName'
		}
		if(type=='organizStore'){
			url="findOrganizStoreByConditions.do";
			name='organizName'
		}
		if(type=='websiteJournalStore'){
			name='name'
			url="findWebsiteJournalStore.do";
		}
		this.store=Ext.create('Ext.data.JsonStore',{
			pageSize:50,
			remoteSort:true,
			fields:[{name : 'id',type : 'string',mapping : 'id'},
			        {name : name,type : 'string',mapping : name},
			        {name : 'infoType',type : 'string',mapping : 'infoType'}
			],
			proxy:{
				type:'ajax',
				url:url,
				method:'POST',
				simpleSortMode:true,
				reader:{
	                type: 'json',
	                root: 'datas',
	                //这个属性用来控制分页
	                totalProperty:'paging.totalRecord'
				}
			},
			listeners: {
				//页面加载事件
				'beforeload':function(thiz, options) {
					//设置查询参数
					if(!options.params) options.params = {};
					options.params.field=Ext.getCmp('field1').getValue();
					options.params.fieldValue=Ext.getCmp('fieldValue1').getValue();
					options.params.startTime=Ext.getCmp('startTime1').getValue();
					options.params.endTime=Ext.getCmp('endTime1').getValue();
				}
			},
			autoLoad:true
		});
		this.columns=[
		    {text:'id',dataIndex:'id',menuDisabled:true,width:400,hidden:true},
		    {text:'名称',dataIndex:name,menuDisabled:true,flex: 1},
		    {text:'类别',dataIndex:'infoType',menuDisabled:true,flex: 1}
		   ];
		//创建面板底部的工具条
		this.bbar = new Ext.PagingToolbar({//一个要和Ext.data.Store参与绑定并且自动提供翻页控制的工具栏
			//id:'selectexistingdata_pagingtoolbar',
			store : self.store,  		//数据源
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
			}
		};
		this.callParent(); 
	}
});
