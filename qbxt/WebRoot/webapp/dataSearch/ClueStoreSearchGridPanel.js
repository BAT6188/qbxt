Ext.define('Ushine.dataSearch.ClueStoreSearchGridPanel',{
	extend:'Ext.grid.Panel',
	id:'cluestoresearchgridpanel',
	itemId:'c_cluestoresearch_gridpanel',
	flex:1,
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
		var store = Ext.create('Ext.data.JsonStore',{
			pageSize:40,
			model:'CluesStoreModel',
			remoteSort:true,
			proxy:{
				type:'ajax',
				url:'findClueStore.do',
				simpleSortMode:true,
				reader:{
	                type: 'json',
	                root: 'datas',
					totalProperty:'paging.totalRecord'
				},
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
		    {text:'id',dataIndex:'id',flex: 1,menuDisabled:true,hidden:true},
		    {text: '线索名称',  dataIndex: 'clueName',sortable: true,flex: 2,menuDisabled:true},
		    {text: '线索来源',  dataIndex: 'clueSource',sortable: true,flex: 2,menuDisabled:true},
		    {text: '发现时间',  dataIndex: 'findTime',sortable: true,flex: 2,menuDisabled:true},
		   /* {text: '发现时间',  dataIndex: 'findTime',width:160,menuDisabled:true,renderer:function(value){
	         	   return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }},*/
	        {text: '数据创建时间',  sortable: true,dataIndex: 'createDate',flex: 2,menuDisabled:true,renderer:function(value){
	         	   return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }},
		    {text: '线索内容',  hidden:true,dataIndex: 'clueContent',sortable: false,flex: 1,menuDisabled:true},
		    {text: '工作进度及进展情况',  hidden:true,dataIndex: 'arrangeAndEvolveCondition',sortable: false,flex: 1,menuDisabled:true},
		    {text: '操作',sortable: false,dataIndex: 'icon',align:'center',flex: 1,xtype:'actioncolumn',
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
	             }],
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
	            		
		        }
		    },
		    {text: '下载',sortable: true,align:'center',flex: 1,xtype:'actioncolumn',
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
		this.callParent(); 
	},listeners: {
		'itemdblclick':function(obj,record,index, eOpts){

			var data=record.data;
			//详细信息
			Ext.create('ServiceClueDetailWin',{
				record:record
			}).show();
			//var data=record.data;
			//Ext.create('Ushine.utils.WinUtils').orderWin(data);
		},
		'cellcontextmenu':function( thiz, td, cellIndex, record, tr, rowIndex, e, eOpts){
			this.rightClick( thiz, td, cellIndex, record, tr, rowIndex, e, eOpts);
		}
	},rightClick:function( thiz, td, cellIndex, record, tr, rowIndex, e, eOpts){
		var self=this;
		if(this.menu){
			this.menu.hide(true);
			this.menu.removeAll(true);
		}else{
			this.menu = new Ext.menu.Menu();
		}
		this.menu.addCls('context-menu');
	    this.menu.add({
	    	text:'线索人员',
	    	cls:'context-btn',
	    	handler:function(){
	    		Ext.getCmp('content_frame').removeAll();
	        	Ext.getCmp('content_frame').add(new Ushine.cluesInfo.CluePerson(record.data.id));
	    	}
	    },{
	    	text:'线索组织',
	    	cls:'context-btn',
	    	handler:function(){
	    		Ext.getCmp('content_frame').removeAll();
	        	Ext.getCmp('content_frame').add(new Ushine.cluesInfo.ClueOrganiz(record.data.id));
	    	}
	    },{
	    	text:'线索媒体网站',
	    	cls:'context-btn',
	    	handler:function(){
	    		Ext.getCmp('content_frame').removeAll();
	        	Ext.getCmp('content_frame').add(new Ushine.cluesInfo.ClueMediaNetworkBook(record.data.id));
	    	}
	    });
	    this.menu.showAt(e.getPoint());
	}
});
