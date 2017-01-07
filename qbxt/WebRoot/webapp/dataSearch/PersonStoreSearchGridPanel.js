/**
 * 一键搜索人员的gridpanel
 */
Ext.define('Ushine.dataSearch.PersonStoreSearchGridPanel', {
	extend: 'Ext.grid.Panel',
	id:"personstoresearchgridpanel",
	flex: 1,
	itemId:'p_personstoresearch_gridpanel',
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
		    {text:'ID',dataIndex:'id',flex: 1,menuDisabled:true,hidden:true},
		    {text: '人员类别',  dataIndex: 'infoType',sortable: true,flex: 1,menuDisabled:true},
		    {text: '姓名',  dataIndex: 'personName',sortable: true,flex: 1,menuDisabled:true},
		    {text: '曾用名',  dataIndex: 'nameUsedBefore',sortable: true,flex: 1,menuDisabled:true},
		    {text: '英文名',  dataIndex: 'englishName',sortable: true,flex: 1 ,menuDisabled:true},
		    {text: '性别',  dataIndex: 'sex',sortable: true,flex: 1,menuDisabled:true},
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
		    {text: '数据创建时间',  dataIndex: 'createDate',flex:1,sortable: true,menuDisabled:true,renderer:function(value){
	         	   return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }},
		    //{text: '履历',  dataIndex: 'antecedents',sortable: false,flex: 1,menuDisabled:true},
		    //{text: '活动情况',  dataIndex: 'activityCondition',sortable: false,flex: 1,menuDisabled:true},
		    {text: '下载',sortable: false,align:'center',width:130,xtype:'actioncolumn',
		    	items:[{
		            icon: 'images/document_small_download.png',
		            tooltip: '保存Word',
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
			'itemdblclick':function(obj,record,index, eOpts){
				//console.log(record);
				//详细信息
				Ext.create('PersonInfoDetailWin',{
					record:record
				}).show();
			}
		};
		this.callParent();
	}
});

