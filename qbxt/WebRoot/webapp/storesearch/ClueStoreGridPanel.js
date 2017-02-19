/**
 * 搜索线索gridpanel
 */
Ext.define('Ushine.storesearch.ClueStoreGridPanel', {
	extend: 'Ext.grid.Panel',
	id:"storesearch_cluegridpanel",
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
	    	html:"<a href='javascript:void(0)' onclick=showMoreClueStores()>"+"更多线索>>><a/>"
	    }]
	}],
	constructor: function(config) {
		var self = this;
		var store = Ext.create('Ext.data.JsonStore',{
			//这里控制每页显示的数量
			pageSize:10,
			model:'CluesStoreModel',
			remoteSort:true,
		});
		this.columns=[
		    {text:'id',dataIndex:'id',flex: 1,menuDisabled:true,hidden:true},
		    {text: '线索名称',  dataIndex: 'clueName',sortable: false,flex: 2,menuDisabled:true},
		    {text: '线索来源',  dataIndex: 'clueSource',sortable: false,flex: 2,menuDisabled:true},
		    {text: '发现时间',  dataIndex: 'findTime',sortable: false,flex: 2,menuDisabled:true},
	        {text: '数据创建时间',  dataIndex: 'createDate',width:160,menuDisabled:true,renderer:function(value){
	         	   return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }},
		    {text: '线索内容',  hidden:true,dataIndex: 'clueContent',sortable: false,flex: 1,menuDisabled:true},
		    {text: '工作进度及进展情况',  hidden:true,dataIndex: 'arrangeAndEvolveCondition',sortable: false,flex: 1,menuDisabled:true},
		    {text: '操作',sortable: true,dataIndex: 'icon',align:'center',width:100,xtype:'actioncolumn',
		    	items:[{
		            icon: 'images/user-red.png',
		            tooltip: '人员对象',
		            style:'margin-left:20px;',
		            handler: function(grid, rowIndex, colIndex){
		            	var data = self.store.getAt(rowIndex).data;   //选中一行的数据
		            	var fieldValue=Ext.getCmp('fieldValue').getValue();
		            	//console.log(fieldValue)
		            	Ext.getCmp('content_frame').removeAll();
		            	//var fieldValue=Ext.getCmp('fieldValue1').getValue();
		            	//代表一键搜索
		            	var dataSearch='yes';
		            	var configFieldValue=config.fieldValue;
		            	//console.log(configFieldValue);
			        	Ext.getCmp('content_frame').add(new Ushine.cluesInfo.CluePerson(data.id,fieldValue,
			        			dataSearch,configFieldValue));
		            }
	             }/*,{
			        icon: 'images/briefcase.png',
			        iconCls:'personStore',
			        tooltip: '组织对象',
			        handler: function(grid, rowIndex, colIndex){
			        	var data = self.store.getAt(rowIndex).data//选中一行的数据
			        	var fieldValue=Ext.getCmp('fieldValue').getValue();
			        	Ext.getCmp('content_frame').removeAll();
			        	var dataSearch='yes';
		            	var configFieldValue=config.fieldValue;
			        	Ext.getCmp('content_frame').add(new Ushine.cluesInfo.ClueOrganiz(data.id,fieldValue,
			        			dataSearch,configFieldValue));
			         }
		         },{
				       icon: 'images/inbox-document-text.png',
				       iconCls:'personStore',
				       tooltip: '媒体刊物对象',
				       handler: function(grid, rowIndex, colIndex){
				    	   var data = self.store.getAt(rowIndex).data;   //选中一行的数据
				    	   var fieldValue=Ext.getCmp('fieldValue').getValue();
				    	   Ext.getCmp('content_frame').removeAll();
				    	   var dataSearch='yes';
			            	var configFieldValue=config.fieldValue;
			    	       Ext.getCmp('content_frame').add(new Ushine.cluesInfo.ClueMediaNetworkBook(data.id,
			    	    		   fieldValue,dataSearch,configFieldValue));
				      }
			     }*/],
	             menuDisabled:true,
	             renderer:function(value){
	            	 var self = this;
	            	 //分割成数组
	            	 var icon = value.split(",");
	            		 var c =[];
	            		 //显示线索涉及对象人员图标
	            		 c = icon[0].split(":");
	            		 if(c[1]=='true'){
	            			self.items[0].icon ="images/user-red.png";
	            		 }else{
	            			 self.items[0].icon ="images/user-red1.png";
	            		 }
	            		 
	            		 /*//显示线索涉及对象组织图标
	            		 c = icon[1].split(":");
	            		 if(c[1]=='true'){
	            			self.items[1].icon ="images/briefcase.png";
	            		 }else{
	            			 self.items[1].icon ="images/briefcase1.png";
	            		 }
	            		 //显示线索涉及对象媒体网站刊物图标
	            		 c = icon[2].split(":");
	            		 if(c[1]=='true'){
	            			self.items[2].icon ="images/inbox-document-text.png";
	            		 }else{
	            			 self.items[2].icon ="images/inbox-document-text1.png";
	            		 }*/
		        }
		    },
		    {text: '下载',sortable: false,align:'center',flex:0.5,xtype:'actioncolumn',
		    	items:[{
		            icon: 'images/document_small_download.png',
		            tooltip: '保存Word',
		            handler: function(grid, rowIndex, colIndex){
		            	var data = self.store.getAt(rowIndex).data;   //选中一行的数据
			            var clueId  = data.id; 
			            try{
		            		location.href = "downloadCluePDFFile.do?clueId="+clueId;
		            	}catch(e){
		                    Ext.Msg.alert('提示', "服务器连接失败，请联系管理员");
		            	}	
		            }
	             }],
	             menuDisabled:true,
		    }
		];
		this.loadMask =true;
		this.store = store;
		this.callParent(); 
	},listeners: {
		'itemdblclick':function(obj,record,index, eOpts){
			var data=record.data;
			//详细信息
			Ext.create('ServiceClueDetailWin',{
				record:record
			}).show();
		}
	}
});

//显示更多线索
function showMoreClueStores(){
	var fieldValue=Ext.getCmp('fieldValue').getValue();
	Ext.getCmp('content_frame').removeAll();
	Ext.getCmp('content_frame').add(new Ushine.dataSearch.ClueStoreSearch({
		fieldValue:fieldValue
	}));
}

