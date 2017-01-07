/**
 * 一键搜索的业务文档gridpanel
 */
Ext.define('Ushine.storesearch.ServiceDocStoreGridPanel',{
	extend:'Ext.grid.Panel',
	id:'storesearch_servicedocgridpanel',
	stripeRows:false,//true为隔行换颜色
	autoHeight:true,
	disableSelection:false,//是否禁止行选择
	columnLines:true, //列的框线样式
	loadMask:true,
	viewConfig:{
		emptyText:'没有数据',
		stripeRows:true,
		enableTextSelection:true
	},
	dockedItems: [{
	    xtype: 'toolbar',
	    dock: 'bottom',
	    border:false,
	    //最右边
	    items: ['->',{
	    	xtype: 'panel',
	    	border:false,
	    	html:"<a href='javascript:void(0)' onclick=showMoreSercieDocStores()>"+"更多业务文档>>><a/>"
	    }]
	}],
	constructor:function(config){
		var self=this;
		var store=Ext.create('Ext.data.JsonStore',{
			pageSize:10,
			model:'ServiceDocModel',
			remoteStore:true,
		});
		
		this.columns=[
			//设置列id
			{text:'id',dataIndex:'id',flex: 1,menuDisabled:true,hidden:true},
		    {text: '文档类别',  dataIndex: 'infoType',sortable: false,flex: 1,menuDisabled:true},
		    {text: '文档名称',  dataIndex: 'docName',sortable: false,flex: 2,menuDisabled:true},
		    {text: '涉及领域',  dataIndex: 'involvedInTheField',sortable: false,flex: 0.5,menuDisabled:true},
		    {text: '期刊号',  dataIndex: 'docNumber',sortable: false,flex: 0.5 ,menuDisabled:true},
		    {text: '建立时间',  dataIndex: 'time',sortable: false,flex: 1,menuDisabled:true,renderer:function(value){
		    	if(value.indexOf("T00")>0){
		    		value=value.replace("T00","");
			    	value=value.substring(0,value.length-6);
		    	}
		    	return value;
		    }},
		    {text: '数据创建时间',  dataIndex: 'createDate',width:160,menuDisabled:true,renderer:function(value){
	         	   return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }}
		];
		this.loadMask =true;
		this.store = store;
		this.listeners={
			//双击事件
			itemdblclick:function(thiz, record, item, index, e, eOpts){
				//详细信息
				Ext.create('ServiceDocDetailWin',{
					record:record
				}).show();
			}
		};
		this.callParent(); 
	}
})


//显示更多业务文档
function showMoreSercieDocStores(){
	var fieldValue=Ext.getCmp('fieldValue').getValue();
	Ext.getCmp('content_frame').removeAll();
	Ext.getCmp('content_frame').add(new Ushine.dataSearch.VocationalDocStoreSearch({
		fieldValue:fieldValue
	}));
}