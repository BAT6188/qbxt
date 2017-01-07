/**
 * 一键搜索组织gridpanel
 */
Ext.define('Ushine.storesearch.OrganizStoreGridPanel',{
	extend: 'Ext.grid.Panel',
	id:"storesearch_organizgridpanel",
	//height:350,
	stripeRows:false,		   
	//autoScroll:true,
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
	    	html:"<a href='javascript:void(0)' onclick=showMoreOrganizStores()>"+"更多组织>>><a/>"
	    }]
	}],
	constructor: function(config) {
		var self = this;
		//console.log(config);
		var store = Ext.create('Ext.data.JsonStore',{
			pageSize:10,
			model:'organizeStoreModel',
		});
		this.columns=[
			{text:'ID',dataIndex:'id',flex: 1,menuDisabled:true,hidden:true},
		    {text: '组织名称',  dataIndex: 'organizName',sortable: false,flex: 1,menuDisabled:true},
		    {text: '组织负责人',  dataIndex: 'orgHeadOfName',sortable: false,flex: 1,menuDisabled:true},
		    {text: '组织类别',  dataIndex: 'infoType',sortable: false,flex: 1,menuDisabled:true},
		    {text: '组织类别',  dataIndex: 'infoTypeId',hidden:true,sortable: false,flex: 1,menuDisabled:true},
		    {text: '网站地址',  dataIndex: 'websiteURL',sortable: false,flex: 1 ,menuDisabled:true},
		    {text: '创办刊物',  dataIndex: 'organizPublicActionNames',sortable: false,flex: 1,menuDisabled:true},
		    {text: '分支机构',  dataIndex: 'organizBranchesNames',sortable: false,flex: 1,menuDisabled:true},
		    {text: '主要成员',  dataIndex: 'organizPersonNames',sortable: false,flex: 1,menuDisabled:true},
		    {text: '成立时间',  dataIndex: 'foundTime',sortable: false,flex: 1,menuDisabled:true,renderer:function(value){
		    	if(value.indexOf("T00")>0){
		    		value=value.replace("T00","");
			    	value=value.substring(0,value.length-6);
		    	}
		    	return value;
		    }},
		    {text: '活动范围',  dataIndex: 'degreeOfLatitude',sortable: false,flex: 1,menuDisabled:true},
		    {text: '基本情况',  dataIndex: 'basicCondition',sortable: false,flex: 1,hidden:true,menuDisabled:true},
		    {text: '活动情况',  dataIndex: 'activityCondition',sortable: false,flex: 1,hidden:true,menuDisabled:true},
		    {text: '数据创建时间',  dataIndex: 'createDate',width:160,menuDisabled:true,renderer:function(value){
	         	   return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }},
        	{text: '下载',sortable: false,align:'center',flex:0.5,xtype:'actioncolumn',
	    	items:[{
	            icon: 'images/document_small_download.png',
	            tooltip: '保存为Word',
	            handler: function(grid, rowIndex, colIndex){
	            	var data = self.store.getAt(rowIndex).data;   //选中一行的数据
		            var orgId  = data.id; 
		            try{
	            		location.href = "downloadOrganizPDFFile.do?orgId="+orgId;
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
	},
	listeners: {
		'itemdblclick':function(obj,record,index, eOpts){
			//详细信息
			Ext.create('ServiceOrganizDetailWin',{
				record:record
			}).show();
		}
	}
});

//显示更多组织
function showMoreOrganizStores(){
	var fieldValue=Ext.getCmp('fieldValue').getValue();
	Ext.getCmp('content_frame').removeAll();
	Ext.getCmp('content_frame').add(new Ushine.dataSearch.OrganizStoreSearch({
		fieldValue:fieldValue
	}));
}
