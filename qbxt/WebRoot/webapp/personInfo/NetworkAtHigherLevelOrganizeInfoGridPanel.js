/**
 *媒体上级组织gridpanel
 */
Ext.define('Ushine.personInfo.NetworkAtHigherLevelOrganizeInfoGridPanel',{
extend: 'Ext.grid.Panel',
	id:"organizeInfoGridPanel",
	flex: 1,
	itemId:'p_organizeinfo_grid',
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
	constructor: function(networkId) {
		var self = this;
		var store = Ext.create('Ext.data.JsonStore',{
			pageSize:40,
			model:'organizeInfoModel',
			remoteSort:true,
			proxy:{
				type:'ajax',
				url:'findNetworkAtHigherLevelByPersonStoreId.do?networkId='+networkId,
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
					var startTime = Ext.getCmp("startTime").getValue();  //开始时间
	                var endTime = Ext.getCmp("endTime").getValue();  //结束时间
	                startTime = Ext.Date.format(startTime, 'Y-m-d H:i:s');
	                endTime = Ext.Date.format(endTime, 'Y-m-d H:i:s');
	                var field = Ext.getCmp('field').getValue();
	                var fieldValue = Ext.getCmp('fieldValue').getValue();
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
			{text:'ID1',dataIndex:'subId',flex: 1,menuDisabled:true,hidden:true},
			{text: '是否启用',  dataIndex: 'isEnable',sortable: false,flex: 1,menuDisabled:true,renderer:function(value){
				if(value == '1'){
					return "否";
				}else{
					return "是";
				}
			}},
		    {text: '组织名称',  dataIndex: 'organizName',sortable: false,flex: 1,menuDisabled:true},
		    {text: '组织负责人',  dataIndex: 'orgHeadOfName',sortable: false,flex: 1,menuDisabled:true},
		    {text: '组织类别',  dataIndex: 'infoType',sortable: false,flex: 1,menuDisabled:true},
		    {text: '组织类别',  dataIndex: 'infoTypeId',hidden:true,sortable: false,flex: 1,menuDisabled:true},
		    
		    {text: '网站地址',  dataIndex: 'websiteURL',sortable: false,flex: 1 ,menuDisabled:true},
		    {text: '创办刊物',  dataIndex: 'organizPublicActionNames',sortable: false,flex: 1,menuDisabled:true},
		    {text: '分支机构',  dataIndex: 'organizBranchesNames',sortable: false,flex: 1,menuDisabled:true},
		    {text: '主要成员',  dataIndex: 'organizPersonNames',sortable: false,flex: 1,menuDisabled:true},
		    {text: '成立时间',  dataIndex: 'foundTime',width:160,menuDisabled:true,renderer:function(value){
	         	   return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }},
		    {text: '活动范围',  dataIndex: 'degreeOfLatitude',sortable: false,flex: 1,menuDisabled:true},
		    {text: '基本情况',  dataIndex: 'basicCondition',sortable: false,flex: 1,hidden:true,menuDisabled:true},
		    {text: '活动情况',  dataIndex: 'activityCondition',sortable: false,flex: 1,hidden:true,menuDisabled:true},
		    {text: '数据创建时间',  dataIndex: 'createDate',width:160,menuDisabled:true,renderer:function(value){
	         	   return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }},
		    {text: '下载',sortable: true,align:'center',width:130,xtype:'actioncolumn',
		    	items:[{
		            icon: 'images/document_small_download.png',
		            tooltip: '保存Word',
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
			//详细信息
			Ext.create('ServiceOrganizDetailWin',{
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
	    	text:'下属组织',
	    	cls:'context-btn',
	    	handler:function(){
	    		Ext.getCmp('content_frame').removeAll();
	        	Ext.getCmp('content_frame').add(new Ushine.personInfo.OrganizSubordinatesOrganiz(record.data.id));
	    	}
	    },{
	    	text:'下属人员',
	    	cls:'context-btn',
	    	handler:function(){
	    		Ext.getCmp('content_frame').removeAll();
	        	Ext.getCmp('content_frame').add(new Ushine.personInfo.OrganizSubordinatesPerson(record.data.id));
	    	
	    	}
	    },{
	    	text:'下属媒体刊物',
	    	cls:'context-btn',
	    	handler:function(){
	    		Ext.getCmp('content_frame').removeAll();
	        	Ext.getCmp('content_frame').add(new Ushine.personInfo.OrganizSubordinatesNetworkBook(record.data.id));
	    	
	    	}
	    });
	    this.menu.showAt(e.getPoint());
	}
});

//数据模型
Ext.define('organizeInfoModel', {
	extend: 'Ext.data.Model',
    fields: [
             {name: 'id', type:'string', mapping: 'id'},
             {name: 'subId', type:'string', mapping: 'subId'},
             {name: 'isEnable', type:'string', mapping: 'isEnable'},
             {name: 'organizName', type:'string', mapping: 'organizName'},
             {name: 'orgHeadOfName', type:'string', mapping: 'orgHeadOfName'},
             {name: 'infoType', type:'string', mapping: 'infoType'},
             {name: 'websiteURL', type:'string', mapping: 'websiteURL'},
             {name: 'organizPublicActionNames', type:'String', mapping: 'organizPublicActionNames'},
             {name: 'organizBranchesNames', type:'string', mapping: 'organizBranchesNames'},
             {name: 'organizPersonNames', type:'string', mapping: 'organizPersonNames'},
             {name: 'foundTime', type:'date', mapping: 'foundTime'},
             {name: 'degreeOfLatitude', type:'string', mapping: 'degreeOfLatitude'},
             {name: 'basicCondition', type:'string', mapping: 'basicCondition'},
             {name: 'infoTypeId', type:'string', mapping: 'infoTypeId'},
             {name: 'activityCondition', type:'string', mapping: 'activityCondition'},
             {name: 'createDate', type:'date', mapping: 'createDate'}
             ],
    idProperty:'id'
});

