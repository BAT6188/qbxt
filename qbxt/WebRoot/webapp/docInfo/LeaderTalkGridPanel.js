/**
 * 领导讲话gridpanel
 */
Ext.define('Ushine.docInfo.LeaderTalkGridPanel',{
	extend:'Ext.grid.Panel',
	id:'leadertalkgridpanel',
	itemId:'d-leadertalk-grid',
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
			pageSize:50,
			model:'LeaderTalkModel',
			//后台排序
			remoteSort :true,
			//请求后台服务
			proxy:{
				type:'ajax',
				url:'findLeadSpeakStore.do',
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
		this.selModel=new Ext.selection.CheckboxModel({
			//点击复选框才会选中,防止其他行被取消
			checkOnly:true
		});
		this.columns=[
			//设置列id
			{text:'ID',dataIndex:'id',flex: 1,menuDisabled:true,hidden:true},
			{text:'attaches',dataIndex:'id',flex: 1,menuDisabled:true,hidden:true},
		    {text: '类别',  dataIndex: 'infoType',sortable: true,flex: 1,menuDisabled:true},
		    {text: '标题',  dataIndex: 'title',sortable: true,flex: 1,menuDisabled:true},
		    {text: '会议名称',  dataIndex: 'meetingName',sortable: true,flex: 1,menuDisabled:true},
		    {text: '密级',  dataIndex: 'secretRank',sortable: true,flex: 1,menuDisabled:true},
		    //{text: '涉及领域',  dataIndex: 'involvedInTheField',sortable: true,flex: 1 ,menuDisabled:true},
		    {text: '建立时间',  dataIndex: 'time',sortable: true,flex: 1,menuDisabled:true,renderer:function(value){
		    	if(value.indexOf("T00")>0){
		    		value=value.replace("T00","");
			    	value=value.substring(0,value.length-6);
		    	}
		    	return value;
		    }},
		    {text: '数据创建时间',  dataIndex: 'createDate',width:160,sortable: true,menuDisabled:true,renderer:function(value){
		    	  return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }}
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
		this.listeners={
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
				//console.log(record);
				Ext.create('LeaderTalkDetailWin',{
					record:record
				}).show();
			}
		};
		this.callParent(); 
	}
})

//领导讲话模型
Ext.define('LeaderTalkModel',{
	extend:'Ext.data.Model',
	fields:['id','attaches','infoType','title','time','meetingName'
		,'secretRank','centent','involvedInTheField',{name: 'createDate', type:'date', mapping: 'createDate'}]
})
//领导讲话详细信息
Ext.define('LeaderTalkDetailWin',{
	extend:'Ushine.win.Window',
	title:'领导讲话详细信息',
	modal : true,
	//gridpanel出现问题解决方式
	layout:{
		type : 'vbox',
		align:'stretch'
	},
	id:'leadertalkdetailwin',
	border : false,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:410,
	width:600,
	bodyPadding:10,
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
			flex:1,
			items:[{
				//第一行
				layout:'hbox',
				margin:'10 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'会议名称',
					value:config.record.get('meetingName'),
					width: 250,
					labelAlign : 'right',
					labelWidth :90
				},{
					xtype:'displayfield',
					fieldLabel:'会议标题',
					value:config.record.get('title'),
					width: 250,
					labelAlign : 'right',
					labelWidth :90
				}]
			},{
				//第二行
				layout:'hbox',
				margin:'10 0 0 0',
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
					fieldLabel:'密级',
					value:config.record.get('secretRank'),
					width: 250,
					labelAlign : 'right',
					labelWidth :90
				}]
			},{
				//第三行
				layout:'hbox',
				margin:'10 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'建立时间',
					value:config.record.get('time').substring(0,10),
					width: 250,
					labelAlign : 'right',
					labelWidth :90
				},{
					xtype:'displayfield',
					fieldLabel:'涉及领域',
					value:config.record.get('involvedInTheField'),
					width: 250,
					labelAlign : 'right',
					labelWidth :90
				}]
			},{
				//第三行
				layout:'hbox',
				margin:'10 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'内容',
					//value:config.record.get('time'),
					width: 250,
					labelAlign : 'right',
					labelWidth : 60,
					value:"<a href='javascript:void(0)' onclick=showLeaderTalkContent()>" +
				  			"点击查看讲话内容<a/>"
				}]
			}]
		},{
			//查看附件
			xtype:'panel',
			margin:'5 0 0 0',
			//flex:1,
			border:false,
			height:150,
			layout:'fit',
			items:[
				Ext.create('DownloadLeadSpeakStoreAttachGridPanel',{
					id:config.record.get('id')
				})
			]
		}]
		this.callParent();
	}
});

//下载附件gridpanel
Ext.define('DownloadLeadSpeakStoreAttachGridPanel',{
	extend:'Ext.grid.Panel',
	border:true,
	stripeRows:false,		    //True来实现隔行换颜色
	autoHeight : true,
	height:100,
	rowLines:false,   //是否显示行分隔线
	disableSelection: false,   //是否禁止行选择，默认false
	columnLines:false,		   //添加列的框线样式
    loadMask: true,           //是否在加载数据时显示遮罩效果，默认为false
	defaults: {sortable: false},
	viewConfig:{
    	stripeRows:true,//在表格中显示斑马线
    	enableTextSelection:true //可以复制单元格文字
	},
	constructor:function(config){
		var self=this;
		this.columns=[
			{text:'附件名称',dataIndex:'name',flex: 3,menuDisabled:true},
			{text: '操作',sortable: false,menuDisabled:true,width:130,flex: 1,xtype:'actioncolumn',items:[{
				//下载
				icon: 'images/document_small_download.png',
		        tooltip: '下载',
		        handler: function(grid, rowIndex, colIndex){
	            	//获取点击行的数据
	            	var data = self.store.getAt(rowIndex).data;
	            	//console.log(encodeURI(data.src));
	            	//两次编码
	            	window.open("downloadLeadSpeakAttach.do?src="+encodeURI(encodeURI(data.src)))
		        }
			}]}
		],
		this.store={
			fields:['src','name'],
			proxy:{
				type:'ajax',
				url:'getLeadSpeakAttachRealPath.do?id='+config.id,
				reader:{
					type:'json'
				}
			},
			autoLoad:true
		}
		this.callParent();
	}
});

//领导讲话内容
function showLeaderTalkContent(){
	var window=Ext.getCmp('leadertalkdetailwin');
	var config=window.config.record.data;
	Ext.create('ShowLeaderTalkCententWin',{
		meetingName:config.meetingName,
		centent:config.centent
	}).show();
}

//内容
Ext.define('ShowLeaderTalkCententWin',{
	height:410,
	width:600,
	autoScroll:true,
	extend:'Ushine.win.Window',
	/*constructor:function(config){
		this.title=config.meetingName+'具体内容',
		this.html=config.centent;
		this.callParent();
	}*/
	buttonAlign:'center',
	border:false,
	layout:'fit',
	maximizable:true,
	constructor:function(config){
		var self=this;
		this.title=config.meetingName+'具体内容';
		this.listeners={
		'afterrender':function(){
				Ext.get('LeaderTalkCentent').dom.innerHTML=config.centent;
			}
		},
		this.items=[{
			xtype:'panel',
			autoScroll:true,
			border:false,
			html:"<div id='LeaderTalkCentent'></div>"
		}];
		this.buttons=[
			Ext.create('Ushine.buttons.Button',{
				text: '打印',
		   		baseCls: 't-btn-red',
		   		handler:function(){
		   			$('#LeaderTalkCentent').printArea();
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
		this.callParent();
	}
})






