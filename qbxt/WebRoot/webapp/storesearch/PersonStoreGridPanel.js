/**
 * 一键搜索人员的gridpanel
 */
Ext.define('Ushine.storesearch.PersonStoreGridPanel', {
	extend: 'Ext.grid.Panel',
	id:"storesearch_persongridpanel",
	//height:350,
	stripeRows:false,		    //True来实现隔行换颜色
	autoHeight : true,
	disableSelection: false,   //是否禁止行选择，默认false
	columnLines:true,		   //添加列的框线样式
    loadMask: true,           //是否在加载数据时显示遮罩效果，默认为false
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
	    layout:'hbox',
	    //最右边
	    items: ['->',{
	    	xtype: 'panel',
	    	border:false,
	    	html:"<a href='javascript:void(0)' onclick=exportThisPagePersonStores()>"+"导出本页人员<a/>&nbsp;&nbsp;" +
	    			"<a href='javascript:void(0)' onclick=showMorePersonStores()>"+"更多人员>>><a/>"
	    }]
	}],
	constructor: function(config) {
		var self = this;
		var store = Ext.create('Ext.data.JsonStore',{
			//这里控制每页显示的数量
			pageSize:10,
			model:'PersonStoreModel',
			//storeId:'personinfostore',
			remoteSort:true,
			//autoLoad:true
			//data:config.datas
		});
		/*var proxy=new Ext.data.proxy.Ajax({
			url:'searchForKeyWord.do?fieldValue='+1989+'&size=10',
			reader:{
				type:'json'
			}
		});
		store.setProxy(proxy);
		store.reload();*/
		this.columns=[
		    {text:'ID',dataIndex:'id',flex: 1,menuDisabled:true,hidden:true},
		    {text: '人员类别',  dataIndex: 'infoType',sortable: true,flex: 1,menuDisabled:true},
		    {text: '姓名',  dataIndex: 'personName',sortable: true,flex: 1,menuDisabled:true},
		    {text: '曾用名',  dataIndex: 'nameUsedBefore',sortable: true,flex: 1,menuDisabled:true},
		    {text: '英文名',  dataIndex: 'englishName',sortable: true,flex: 1 ,menuDisabled:true},
		    {text: '性别',  dataIndex: 'sex',sortable: true,flex: 1,menuDisabled:true},
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
		    {text: '数据创建时间',  dataIndex: 'createDate',sortable: true,flex: 1,menuDisabled:true,renderer:function(value){
	         	   return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }},
	        {text: '下载',sortable: false,align:'center',flex:0.5,xtype:'actioncolumn',
		    	items:[{
		            icon: 'images/document_small_download.png',
		            tooltip: '保存为Word',
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
		this.store = store;
		this.callParent(); 
	},listeners: {
		'itemdblclick':function(obj,record,index, eOpts){
			//详细信息
			Ext.create('PersonInfoDetailWin',{
				record:record
			}).show();
		}
	}
});

//显示更多人员的信息
function showMorePersonStores(){
	//人员
	var fieldValue=Ext.getCmp('fieldValue').getValue();
	Ext.getCmp('content_frame').removeAll();
	Ext.getCmp('content_frame').add(new Ushine.dataSearch.PersonStoreSearch({
		fieldValue:fieldValue
	}));
};

//导出当前页的人员
function exportThisPagePersonStores(){
	var store=Ext.getCmp('storesearch_persongridpanel').getStore();
	var personStoreIds=[];
	store.each(function(record){
		personStoreIds.push(record.get('id'));
	});
	var myMask = new Ext.LoadMask(Ext.getCmp('content_frame'), {msg:"正在导出人员..."});
	myMask.show();
	var date=Ext.Date.format(new Date(),'YmdHms');
	//导入到Excel中
	Ext.Ajax.request({
		url:'personToExcel.do',
		method:'post',
		params:{
			personStoreIds:personStoreIds,
			date:date
		},
		success:function(response){
			myMask.hide();
			var obj=Ext.JSON.decode(response.responseText);
			console.log(obj);
			if(obj.status>0){
				//下载
				window.open("downLoadPersonExcel.do?date="+date);
			}else{
				Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
			}
		},
		failure:function(){
			Ext.create('Ushine.utils.Msg').onInfo("请求后台失败!!!");
			myMask.hide();
		}
	})
}

