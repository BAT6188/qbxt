Ext.define('Ushine.dataSearch.DataSearchPenelGridPanel',{
	extend:'Ext.grid.Panel',
	id:'dataSearchPenelGridPanelgridpanel',
	itemId:'c_dataSearch_grid',
	flex:1,
	height:200,
	stripeRows:false,		    //True来实现隔行换颜色
	autoHeight : true,
	disableSelection: false,   //是否禁止行选择，默认false
	columnLines:true,		   //添加列的框线样式
    loadMask: true,           //是否在加载数据时显示遮罩效果，默认为false
	selType: 'checkboxmodel', // 复选框
	defaults: {sortable: false},
	columns:[],
	viewConfig:{
		emptyText: '没有数据',
    	stripeRows:true,//在表格中显示斑马线
    	enableTextSelection:true //可以复制单元格文字
	},
	//关键字
	fieldValue:'',
	constructor: function(config) {
		var self = this;
		this.config=config;
		var store = Ext.create('Ext.data.JsonStore',{
			pageSize:40,
			model:'LuceneSearchModel',
			remoteSort:true,
			listeners: {
				//页面加载事件
				'beforeload':function(thiz, options) {
				}
			},
			autoLoad:false
			
		});
		this.columns=[
		    //{text:'id',dataIndex:'id',flex: 1,menuDisabled:true,hidden:true},
		    {text: '涉及信息库',  dataIndex: 'dataType',align:'center',sortable: false,flex: 1,menuDisabled:true},
		    {text: '数据库总数',  dataIndex: 'totalCount',align:'center',flex: 1,menuDisabled:true},
	        {text: '符合要求数',  dataIndex: 'dataCount',align:'center',flex: 1,menuDisabled:true},
		    {text: '查看详情',align:'center',width:200,xtype:'actioncolumn',items:[{
	            icon: 'images/treeI.png',
	            tooltip: '点击查看',
	            style:'margin-left:20px;',
	            handler: function(grid, rowIndex, colIndex){
	            		var data = self.store.getAt(rowIndex).data;
	            		if(data.dataCount>0){
	            			Ext.getCmp('content_frame').removeAll();
	            			//console.log(self.fieldValue);
				        	if(data.dataType=='重点人员库'){
				        		//人员的
				        		Ext.getCmp('content_frame').add(new Ushine.dataSearch.PersonStoreSearch({
					        		fieldValue:self.fieldValue
					        	}));
				        	}
				        	
				        	if(data.dataType=='外来文档库'){
				        		//外来文档库
				        		Ext.getCmp('content_frame').add(new Ushine.dataSearch.OutsideDocStoreSearch({
					        		fieldValue:self.fieldValue
					        	}));
				        	}
				        	if(data.dataType=='业务文档库'){
				        		//业务文档库
				        		Ext.getCmp('content_frame').add(new Ushine.dataSearch.VocationalDocStoreSearch({
					        		fieldValue:self.fieldValue
					        	}));
				        	}
				        	if(data.dataType=='领导讲话库'){
				        		//业务文档库
				        		Ext.getCmp('content_frame').add(new Ushine.dataSearch.LeadSpeakStoreSearch({
					        		fieldValue:self.fieldValue
					        	}));
				        	}
				        	if(data.dataType=='线索库'){
				        		//线索库
				        		Ext.getCmp('content_frame').add(new Ushine.dataSearch.ClueStoreSearch({
					        		fieldValue:self.fieldValue
					        	}));
				        	}
	            		}else{
	            			Ext.create('Ushine.utils.Msg').onInfo("符合要求数为0");
	            		}
	            	}
             	}]
	        }
		];
		this.loadMask =false;
		this.store = store;
		this.callParent(); 
	}
});
//数据模型
Ext.define('LuceneSearchModel', {
	extend: 'Ext.data.Model',
    fields: [
         {name: 'dataType', type:'string', mapping: 'dataType'},
         {name: 'totalCount', type:'string', mapping: 'totalCount'},
         {name: 'dataCount', type:'string', mapping: 'dataCount'},         
       //{name: 'clueSource', type:'string', mapping: 'clueSource'},
    ],
    idProperty:'id'
});
