/**
 * 搜索媒体网站刊物的gridpanel
 */
Ext.define('Ushine.storesearch.WebsiteJournalStoreGridPanel', {
	extend: 'Ext.grid.Panel',
	id:"storesearch_websitegridpanel",
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
	    	html:"<a href='javascript:void(0)' onclick=showMoreWebsiteStores()>"+"更多媒体网站刊物>>><a/>"
	    }]
	}],
	constructor: function(config) {
		var self = this;
		var store = Ext.create('Ext.data.JsonStore',{
			//这里控制每页显示的数量
			pageSize:10,
			model:'WebsiteJournalModel',
			remoteSort:true,
		});
		this.columns=[
			//设置列id
			{text:'ID',dataIndex:'id',flex: 1,menuDisabled:true,hidden:true},
		    {text: '类别',  dataIndex: 'infoType',sortable: false,flex: 1,menuDisabled:true},
		    {text: '名称',  dataIndex: 'name',sortable: false,flex: 1,menuDisabled:true},
		    {text: '域名',  dataIndex: 'websiteURL',sortable: false,flex: 1 ,menuDisabled:true},
		    {text: '服务器所在地',  dataIndex: 'serverAddress',sortable: false,flex: 1,menuDisabled:true},
		    {text: '创办地',  dataIndex: 'establishAddress',sortable: false,flex: 1,menuDisabled:true},
		    {text: '主要发行地',  dataIndex: 'mainWholesaleAddress',sortable: false,flex: 1,menuDisabled:true},
		    {text: '创办人',  dataIndex: 'establishPerson',sortable: false,flex: 1,menuDisabled:true},
		    {text: '创办时间',  dataIndex: 'establishTime',sortable: false,flex: 1,menuDisabled:true,renderer:function(value){
		    	if(value.indexOf("T00")>0){
		    		value=value.replace("T00","");
			    	value=value.substring(0,value.length-6);
		    	}
		    	return value;
		    }},
		    {text: '数据创建时间',  dataIndex: 'createDate',flex:1,menuDisabled:true,renderer:function(value){
	         	   return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }},
		    {text: '下载',sortable: false,align:'center',flex:0.5,xtype:'actioncolumn',
		    	items:[{
		            icon: 'images/document_small_download.png',
		            tooltip: '保存Word',
		            handler: function(grid, rowIndex, colIndex){
		            	var data = self.store.getAt(rowIndex).data;   //选中一行的数据
			            var networkId  = data.id; 
			            try{
		            		location.href = "downloadMediaNetworkPDFFile.do?networkId="+networkId;
		            	}catch(e){
		                    Ext.Msg.alert('提示', "服务器连接失败，请联系管理员");
		            	}	
		            }
	             }],menuDisabled:true
		    }
		];
		this.loadMask =true;
		this.store = store;
		this.callParent(); 
	},listeners: {
		'itemdblclick':function(obj,record,index, eOpts){
			//详细信息
			Ext.create('MediaNetworkDetailWindow',{
				record:record
			}).show();
		}
	}
});

//显示更多媒体刊物
function showMoreWebsiteStores(){
	var fieldValue=Ext.getCmp('fieldValue').getValue();
	Ext.getCmp('content_frame').removeAll();
	Ext.getCmp('content_frame').add(new Ushine.dataSearch.WebsiteJournalStoreSearch({
		fieldValue:fieldValue
	}));
}

