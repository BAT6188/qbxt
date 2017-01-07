/**
 * 媒体网站刊物gridpanel
 */
Ext.define('Ushine.docInfo.MediaNetworkGridPanel',{
	extend:'Ext.grid.Panel',
	id:'medianetworkbookgrid',
	itemId:'d-medianetworkbook-grid',
	height:300,
	flex:1,
	stripeRows:false,//true为隔行换颜色
	autoHeight:true,
	disableSelection:false,//是否禁止行选择
	columnLines:true, //列的框线样式
	loadMask:true,
	//selType:'checkboxmodel',//复选框
	viewConfig:{
		emptyText:'没有数据',
		stripeRows:true,
		enableTextSelection:true
	},
	constructor:function(){
		var self=this;
		var store=Ext.create('Ext.data.JsonStore',{
			pageSize:40,
			model:'WebsiteJournalModel',
			remoteSort:true,
			//请求后台服务
			proxy:{
				type:'ajax',
				url:'findWebsiteJournalStore.do',
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
					//设置查询参数
					if(!options.params) options.params = {};
					options.params.field=Ext.getCmp('field').getValue();
					options.params.fieldValue=Ext.getCmp('fieldValue').getValue();
					options.params.startTime=Ext.getCmp('startTime').getValue();
					options.params.endTime=Ext.getCmp('endTime').getValue();
				}
			},
			autoLoad:true
		});
		
		this.columns=[
			//设置列id
			{text:'ID',dataIndex:'id',flex: 1,menuDisabled:true,hidden:true},
		    /*{text: '是否启用',  dataIndex: 'isEnable',sortable: false,flex: 1,menuDisabled:true,renderer:function(value){
				if(value == '1'){
					return "否";
				}else{
					return "是";
				}
			}},*/
		    {text: '类别',  dataIndex: 'infoType',sortable: true,flex: 1,menuDisabled:true},
		    {text: '名称',  dataIndex: 'name',sortable: true,flex: 1,menuDisabled:true},
		    {text: '域名',  dataIndex: 'websiteURL',sortable: true,flex: 1 ,menuDisabled:true},
		    {text: '服务器所在地',  dataIndex: 'serverAddress',sortable: true,flex: 1,menuDisabled:true},
		    {text: '创办地',  dataIndex: 'establishAddress',sortable: true,flex: 1,menuDisabled:true},
		    {text: '主要发行地',  dataIndex: 'mainWholesaleAddress',sortable: true,flex: 1,menuDisabled:true},
		    {text: '创办人',  dataIndex: 'establishPerson',sortable: true,flex: 1,menuDisabled:true},
		    {text: '创办时间',  dataIndex: 'establishTime',sortable: true,flex: 1,menuDisabled:true},
		    {text: '数据创建时间', sortable: true, dataIndex: 'createDate',width:160,menuDisabled:true,renderer:function(value){
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
		//创建面板底部的工具条
		this.bbar = new Ext.PagingToolbar({
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
		this.selModel=new Ext.selection.CheckboxModel({
			//点击复选框才会选中,防止其他行被取消
			checkOnly:true
		});
		this.listeners={
			//单击选中
			itemclick:function(thiz, record, item, index, e, eOpts){
				var model=self.getSelectionModel();
				if(model.isSelected(record)){
					model.deselect(record);
				}else{
					var records=model.getSelection();
					records.push(record);
					model.select(records);
					records=[];
				}
			},
			//双击事件
			itemdblclick:function(thiz, record, item, index, e, eOpts){
				//详细信息
				Ext.create('MediaNetworkDetailWindow',{
					record:record
				}).show();
			}
		};

		this.callParent(); 
	},
		listeners: {
		'itemdblclick':function(obj,record,index, eOpts){
			//详细信息
			console.log(record);
			Ext.create('MediaNetworkDetailWin',{
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
	    	text:'媒体上级组织',
	    	cls:'context-btn',
	    	handler:function(){
	    		//record.data.id
	    		Ext.getCmp('content_frame').removeAll();
	        	Ext.getCmp('content_frame').add(new Ushine.personInfo.NetworkAtHigherLevelOrganizeInfoManage(record.data.id));
	    	}
	    });
	    this.menu.showAt(e.getPoint());
	}
})

//媒体网站文档模型
Ext.define('WebsiteJournalModel',{
	extend:'Ext.data.Model',
	fields:[
		 {name: 'id', type:'string', mapping: 'id'},
         {name: 'isEnable', type:'string', mapping: 'isEnable'},
         {name: 'infoType', type:'string', mapping: 'infoType'},
         {name: 'name', type:'string', mapping: 'name'},
         {name: 'domain', type:'string', mapping: 'domain'},
         {name: 'websiteURL', type:'string', mapping: 'websiteURL'},
         {name: 'serverAddress', type:'String', mapping: 'serverAddress'},
         {name: 'establishAddress', type:'string', mapping: 'establishAddress'},
         {name: 'establishPerson', type:'string', mapping: 'establishPerson'},
         {name: 'basicCondition', type:'string', mapping: 'basicCondition'},
         {name: 'establishTime', type:'string', mapping: 'establishTime'},
         {name: 'mainWholesaleAddress', type:'string', mapping: 'mainWholesaleAddress'},
         {name: 'createDate', type:'date', mapping: 'createDate'}
	]
})

//显示媒体网站详细信息
Ext.define('MediaNetworkDetailWindow',{
	extend:'Ushine.win.Window',
	title:'媒体网站详细信息',
	modal : true,
	//gridpanel出现问题解决方式
	layout:{
		type : 'vbox',
		align:'stretch'
	},
	id:'mediaNetworkDetailWindow',
	border : false,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:300,
	width:600,
	bodyPadding:20,
	autoScroll:true,
	constructor:function(config){
		this.config=config;
		this.items=[{
			//基本信息
			xtype:'fieldset',
			title:'基本信息',
			layout:'vbox',
			autoScroll:true,
			//height:100,
			flex:2,
			items:[{
				//第一行
				layout:'hbox',
				margin:'5 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'名称',
					value:config.record.get('name'),
					width: 250,
					labelAlign : 'right',
					labelWidth :90
				},{
					xtype:'displayfield',
					fieldLabel:'创办人',
					value:config.record.get('establishPerson'),
					width: 250,
					labelAlign : 'right',
					labelWidth :90
				}]
			},{
				//第二行
				layout:'hbox',
				margin:'5 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'类别',
					value:config.record.get('infoType'),
					width: 250,
					labelAlign : 'right',
					labelWidth :90
				},{
					xtype:'displayfield',
					fieldLabel:'主要发行地',
					value:config.record.get('mainWholesaleAddress'),
					width: 250,
					labelAlign : 'right',
					labelWidth :90
				}]
			},{
				//第三行
				layout:'hbox',
				margin:'5 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'域名',
					value:config.record.get('websiteURL'),
					width: 250,
					labelAlign : 'right',
					labelWidth :90,
					autoScroll:true
				},{
					xtype:'displayfield',
					fieldLabel:'服务器所在地',
					value:config.record.get('serverAddress'),
					width: 250,
					labelAlign : 'right',
					labelWidth :90
				}]
			},{
				//第四行
				layout:'hbox',
				margin:'5 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'创办地',
					value:config.record.get('establishAddress'),
					width: 250,
					labelAlign : 'right',
					labelWidth :90
				},{
					xtype:'displayfield',
					fieldLabel:'创办时间',
					value:config.record.get('establishTime'),
					width: 250,
					labelAlign : 'right',
					labelWidth :90
				}]
			},{
				//第五行
				layout:'hbox',
				margin:'5 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'基本情况',
					//value:config.record.get('time'),
					width: 250,
					labelAlign : 'right',
					labelWidth : 90,
					value:"<a href='javascript:void(0)' onclick=showMediaNetworkDetail()>" +
				  			"点击查看基本情况<a/>"
				}]
			}]
		}]
		this.callParent();
	}
});
//显示基本情况
function showMediaNetworkDetail(){
	var window=Ext.getCmp('mediaNetworkDetailWindow');
	var config=window.config.record.data;
	Ext.create('ShowMediaNetworkDocumentBasicCondition',{
		name:config.name,
		basicCondition:config.basicCondition
	}).show();
};

//基本情况
Ext.define('ShowMediaNetworkDocumentBasicCondition',{
	height:350,
	width:600,
	autoScroll:true,
	maximizable:true,
	extend:'Ushine.win.Window',
	buttonAlign:'center',
	border:false,
	layout:'fit',
	constructor:function(config){
		/*this.title=config.name+'基本情况',
		this.html=config.basicCondition;
		this.callParent();*/
		/*this.title=config.organizName+'基本情况',
		this.html=config.basicCondition;
		this.callParent();*/
		var self=this;
		this.listeners={
		'afterrender':function(){
				Ext.get('MediaNetworkDocumentBasicCondition').dom.innerHTML=config.basicCondition;
			}
		},
		this.title=config.name+'基本情况',
		this.items=[{
			xtype:'panel',
			autoScroll:true,
			border:false,
			html:"<div id='MediaNetworkDocumentBasicCondition'></div>"
		}];
		this.buttons=[
			Ext.create('Ushine.buttons.Button',{
				text: '打印',
		   		baseCls: 't-btn-red',
		   		handler:function(){
		   			$('#MediaNetworkDocumentBasicCondition').printArea();
		   		}
		}),Ext.create('Ushine.buttons.Button', {
      			text: '取消',
      			margin:'0 0 0 35',
   				baseCls: 't-btn-yellow',
   				handler:function(){
					//关闭
					self.close();
   				}
	   	})];
		
		//this.html=config.activityCondition;
		//config.activityCondition
		this.callParent();
	}
})