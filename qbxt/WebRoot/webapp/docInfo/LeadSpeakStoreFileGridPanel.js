/**
 * 显示领导讲话文档附件
 */
Ext.define('Ushine.docInfo.LeadSpeakStoreFileGridPanel', {
	extend: 'Ext.grid.Panel',
	//enableColumnResize:是否允许改变列宽，默认为true
	id:"LeadSpeakStoreFileGridPanel",
	//itemId:'personStoreFileId',
	border:true,
	hideHeaders:true,  //隐藏列头部
	stripeRows:false,		    //True来实现隔行换颜色
	autoHeight : true,
	rowLines:false,   //是否显示行分隔线
	disableSelection: false,   //是否禁止行选择，默认false
	columnLines:false,		   //添加列的框线样式
    loadMask: true,           //是否在加载数据时显示遮罩效果，默认为false
	defaults: {sortable: false},
	
	viewConfig:{
		//emptyText: '还没有附件',
    	stripeRows:true,//在表格中显示斑马线
    	enableTextSelection:true //可以复制单元格文字
	},
	constructor: function(sjnum,record) {
		var self = this;
		//详细信息记录
		this.record=record;
		//console.log(record);
		var attaches='';
		if(this.record){
			attaches=encodeURI(encodeURI(this.record[0].data.attaches));
			//console.log(attaches);
		}
		this.store=Ext.create('Ext.data.JsonStore',{
			pageSize:50,
			fields:[{name : 'filePath',type : 'string',mapping : 'filePath'},
			        {name : 'fileName',type : 'string',mapping : 'fileName'},
			        {name : 'fileTempName',type : 'string',mapping : 'fileTempName'}
			],
			
			proxy:{
				type:'ajax',
				url:'getLeadSpeakStoreTempFile.do?number='+sjnum+'&appendix='+attaches,
				method:'get',
				simpleSortMode:true,
				reader:{
	                type: 'json',
	                root: 'datas'
				}
			},
			autoLoad:true
		});
		this.columns=[
		    {text:'文件路径',dataIndex:'filePath',flex: 1,menuDisabled:true,hidden:true},
		    {text: '文件名称 ',  dataIndex: 'fileName',sortable: false,flex: 20,menuDisabled:true},
		    {text: '操作',sortable: false,width:130,flex: 3,xtype:'actioncolumn',
		    	items:[{
		            icon: 'images/cancel.png',
		            tooltip: '删除',
		            handler: function(grid, rowIndex, colIndex){
		            	//获取点击行的数据
		            	var data = self.store.getAt(rowIndex).data;
		            	console.log(data);
		            	var index=data.filePath.indexOf("tempLeadSpeakStoreFileUpload_");
		            	if(index>-1){
		            		//代表临时上传的
		            		//后台删掉
		            		var fileName=data.filePath.substring("tempLeadSpeakStoreFileUpload_".length,data.filePath.length);
		            		console.log(fileName);
		            		Ext.Ajax.request({
			            	    url: 'deleteLeadSpeakStoreFile.do',
			            	    params: {
			            	        fileName: fileName,
			            	        number:sjnum
			            	    },
			            	    success: function(response){
			            	    	var text = response.responseText;
				    			       var obj=Ext.JSON.decode(text);
				    			       if(obj.success){
				    			    	   //self.getStore().load();
				    			       		self.getStore().removeAt(rowIndex);
				    			       }else{
				    			    	   Ext.create('Ushine.utils.Msg').onInfo("请求服务器失败,请联系管理员！");
				    			       }
			            	    },
			            	    failure:function(response){
			   						var obj=Ext.JSON.decode(response.responseText);
			   						Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
			   						win.close();
			   					}
		            		});
		            	}else{
		            		//原来就有的,删除这行数据就行
		            		self.getStore().removeAt(rowIndex);
		            	}
		            	
		            }
	             }],menuDisabled:true
		    }];
		this.callParent(); 
	}
});
