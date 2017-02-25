/**
 * 搜索领导讲话的gridpanel
 */
Ext.define('Ushine.storesearch.LeadSpeakStoreGridPanel', {
	extend: 'Ext.grid.Panel',
	id:"storesearch_leadspeakgridpanel",
	//height:350,
	stripeRows:false,		   
	autoHeight : true,
	disableSelection: false,  
	columnLines:true,		 
    loadMask: true,         
	defaults: {sortable: false},
	columns:[],
	viewConfig:{
		emptyText: '没有数据',
    	stripeRows:true,//在表格中显示斑马线
    	enableTextSelection:true //可以复制单元格文字
	},
	dockedItems: [{
	    xtype: 'toolbar',
	    dock: 'bottom',
	    border:false,
	    //最右边
	    items: ['->',{
	    	xtype: 'panel',
	    	border:false,
	    	html:"<a href='javascript:void(0)' onclick=showMoreLeadSpeakStores()>"+"更多领导讲话>>><a/>"
	    }]
	}],
	constructor: function(config) {
		var self = this;
		var store = Ext.create('Ext.data.JsonStore',{
			//这里控制每页显示的数量
			pageSize:10,
			model:'LeaderTalkModel',
			remoteSort:true,
		});
		this.columns=[
			//设置列id
			{text:'ID',dataIndex:'id',flex: 1,menuDisabled:true,hidden:true},
		    {text: '类别',  dataIndex: 'infoType',sortable: false,flex: 1,menuDisabled:true},
		    {text: '标题',  dataIndex: 'title',sortable: false,flex: 1,menuDisabled:true},
		    {text: '建立时间',  dataIndex: 'time',sortable: false,flex: 1 ,menuDisabled:true,renderer:function(value){
		    	if(value.indexOf("T00")>0){
		    		value=value.replace("T00","");
			    	value=value.substring(0,value.length-6);
		    	}
		    	return value;
		    }},
		    {text: '会议名称',  dataIndex: 'meetingName',sortable: false,flex: 1,menuDisabled:true},
		    {text: '密级',  dataIndex: 'secretRank',sortable: false,flex: 1,menuDisabled:true},
		    //{text: '涉及领域',  dataIndex: 'involvedInTheField',sortable: false,flex: 1 ,menuDisabled:true},
		    {text: '数据创建时间',  dataIndex: 'createDate',width:160,menuDisabled:true,renderer:function(value){
		    	  return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }}
		];
		this.loadMask =true;
		this.store = store;
		this.callParent(); 
	},listeners: {
		'itemdblclick':function(obj,record,index, eOpts){
			//详细信息
			Ext.create('LeaderTalkDetailWin',{
				record:record
			}).show();
		}
	}
});

//显示更多领导讲话
function showMoreLeadSpeakStores(){
	//人员
	var fieldValue=Ext.getCmp('fieldValue').getValue();
	Ext.getCmp('content_frame').removeAll();
	Ext.getCmp('content_frame').add(new Ushine.dataSearch.LeadSpeakStoreSearch({
		fieldValue:fieldValue
	}));
}

