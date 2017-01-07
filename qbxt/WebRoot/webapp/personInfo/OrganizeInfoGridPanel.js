/**
 * 组织gridpanel
 */
Ext.define('Ushine.personInfo.OrganizeInfoGridPanel',{
extend: 'Ext.grid.Panel',
	id:"organizeInfoGridPanel",
	flex: 1,
	itemId:'p_organizeinfo_grid',
	height:200,
	stripeRows:false,		    //True来实现隔行换颜色
	autoScroll:true,
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
	constructor: function() {
		var self = this;
		var store = Ext.create('Ext.data.JsonStore',{
			pageSize:40,
			model:'organizeStoreModel',
			remoteSort:true,
			proxy:{
				type:'ajax',
				url:'findOrganizStoreByConditions.do',
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
					var startTime = Ext.getCmp("startTime").value;  //开始时间
	                var endTime = Ext.getCmp("endTime").value;  //结束时间
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
			/*{text: '是否启用',  dataIndex: 'isEnable',sortable: false,flex: 1,menuDisabled:true,renderer:function(value){
				if(value == '1'){
					return "否";
				}else{
					return "是";
				}
			}},*/
		    {text: '组织名称',  dataIndex: 'organizName',sortable: true,flex: 1,menuDisabled:true},
		    {text: '组织负责人',  dataIndex: 'orgHeadOfName',sortable: true,flex: 1,menuDisabled:true},
		    {text: '组织类别',  dataIndex: 'infoType',sortable: true,flex: 1,menuDisabled:true},
		    {text: '组织类别',  dataIndex: 'infoTypeId',hidden:true,sortable: true,flex: 1,menuDisabled:true},
		    
		    {text: '网站地址',  dataIndex: 'websiteURL',sortable: true,flex: 1 ,menuDisabled:true},
		    {text: '创办刊物',  dataIndex: 'organizPublicActionNames',sortable: true,flex: 1,menuDisabled:true},
		    {text: '分支机构',  dataIndex: 'organizBranchesNames',sortable: true,flex: 1,menuDisabled:true},
		    {text: '主要成员',  dataIndex: 'organizPersonNames',sortable: true,flex: 1,menuDisabled:true},
		   
		    {text: '成立时间',  dataIndex: 'foundTime',sortable: true,flex: 1,menuDisabled:true,renderer:function(value){
		    	if(value.indexOf("T00")>0){
		    		value=value.replace("T00","");
			    	value=value.substring(0,value.length-6);
		    	}
		    	return value;
		    }},
		    {text: '活动范围',  dataIndex: 'degreeOfLatitude',sortable: true,flex: 1,menuDisabled:true},
		    {text: '基本情况',  dataIndex: 'basicCondition',sortable: true,flex: 1,hidden:true,menuDisabled:true},
		    {text: '活动情况',  dataIndex: 'activityCondition',sortable: true,flex: 1,hidden:true,menuDisabled:true},
		    {text: '数据创建时间',  sortable: true,dataIndex: 'createDate',flex: 1,menuDisabled:true,renderer:function(value){
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
		this.selModel=new Ext.selection.CheckboxModel({
			//点击复选框才会选中,防止其他行被取消
			checkOnly:true
		});
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
			'itemdblclick':function(obj,record,index, eOpts){
			//详细信息
				Ext.create('ServiceOrganizDetailWin',{
					record:record
				}).show();
			},
			'cellcontextmenu':function( thiz, td, cellIndex, record, tr, rowIndex, e, eOpts){
				this.rightClick( thiz, td, cellIndex, record, tr, rowIndex, e, eOpts);
			}
		};
		this.callParent();
		
	},
	rightClick:function( thiz, td, cellIndex, record, tr, rowIndex, e, eOpts){
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
Ext.define('organizeStoreModel', {
	extend: 'Ext.data.Model',
    fields: [
        /* {name: 'id', type:'string', mapping: 'id'},
         {name: 'isEnable', type:'string', mapping: 'isEnable'},
         {name: 'organizName', type:'string', mapping: 'organizName'},
         {name: 'orgHeadOfName', type:'string', mapping: 'orgHeadOfName'},
         {name: 'infoType', type:'string', mapping: 'infoType'},
         {name: 'websiteURL', type:'string', mapping: 'websiteURL'},
         {name: 'organizPublicActionNames', type:'String', mapping: 'organizPublicActionNames'},
         {name: 'organizBranchesNames', type:'string', mapping: 'organizBranchesNames'},
         {name: 'organizPersonNames', type:'string', mapping: 'organizPersonNames'},
         {name: 'foundTime', type:'string', mapping: 'foundTime'},
         {name: 'degreeOfLatitude', type:'string', mapping: 'degreeOfLatitude'},
         {name: 'basicCondition', type:'string', mapping: 'basicCondition'},
         {name: 'infoTypeId', type:'string', mapping: 'infoTypeId'},
         {name: 'activityCondition', type:'string', mapping: 'activityCondition'},
         {name: 'createDate', type:'date', mapping: 'createDate'}*/
         'id','isEnable',"organizName","orgHeadOfName","infoType","organizPublicActionNames","websiteURL","organizBranchesNames",
         	"organizPersonNames","foundTime","degreeOfLatitude","basicCondition","infoTypeId", 'activityCondition', 
         	  {name: 'createDate', type:'date', mapping: 'createDate'}
         ],
    idProperty:'id'
});

//显示详细信息
Ext.define('ServiceOrganizDetailWin',{
	extend:'Ushine.win.Window',
	title:'组织库详情信息',
	modal : true,
	layout:{
		type : 'vbox',
		align:'stretch'
	},
	id:'serviceOrganizDetailwin',
	border : false,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:350,
	width:600,
	bodyPadding:20,
	autoScroll:true,
	constructor:function(config){
		this.config=config;
		console.log(config);
		this.items=[{
			//基本信息
			xtype:'fieldset',
			layout:'vbox',
			autoScroll:true,
			//height:100,
			flex:1,
			items:[{
				layout:'hbox',
				margin:'10 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'组织名称',
					value:config.record.get('organizName'),
					width: 250,
					labelAlign : 'right',
					labelWidth : 60
				},{
					xtype:'displayfield',
					fieldLabel:'负责人',
					value:config.record.get('orgHeadOfName'),
					width: 250,
					labelAlign : 'right',
					labelWidth : 60
				}]
			},{
				layout:'hbox',
				margin:'10 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'组织类别',
					value:config.record.get('infoType'),
					width: 250,
					labelAlign : 'right',
					labelWidth : 60
				},{
					xtype:'displayfield',
					fieldLabel:'网站地址',
					value:config.record.get('websiteURL'),
					width: 250,
					labelAlign : 'right',
					labelWidth : 60
				}]
			},{
				layout:'hbox',
				margin:'10 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'分支机构',
					value:config.record.get('organizBranchesNames'),
					width: 250,
					labelAlign : 'right',
					labelWidth : 60
				},{
					xtype:'displayfield',
					fieldLabel:'主要成员',
					value:config.record.get('organizPersonNames'),
					width: 250,
					labelAlign : 'right',
					labelWidth : 60
				}]
			},{
				//第二行
				layout:'hbox',
				margin:'10 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'成立时间',
					//value:Ext.Date.format(config.record.get('foundTime'), 'Y-m-d H:i:s'),
					value:config.record.get('foundTime'),
					width: 250,
					labelAlign : 'right',
					labelWidth : 60
				},{
					xtype:'displayfield',
					fieldLabel:'活动范围',
					value:config.record.get('degreeOfLatitude'),
					width: 250,
					labelAlign : 'right',
					labelWidth : 60
				}]
			},{
				//第三行
				layout:'hbox',
				margin:'10 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'基本情况',
					//value:config.record.get('time'),
					width: 250,
					labelAlign : 'right',
					labelWidth : 60,
					value:"<a href='javascript:void(0)' onclick=showServiceBasicConditionUmentDetail()>" +
				  			"点击查看基本情况<a/>"
				}]
			},{
				//第三行
				layout:'hbox',
				margin:'10 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'活动情况',
					//value:config.record.get('time'),
					width: 250,
					labelAlign : 'right',
					labelWidth : 60,
					value:"<a href='javascript:void(0)' onclick=showServiceActivityConditionUmentDetail()>" +
				  			"点击查看活动情况<a/>"
				}]
			}]
		}]
		this.callParent();
	}
});

//活动情况
function showServiceActivityConditionUmentDetail(){
	//query函数能够获得多个符合条件的
	var window=Ext.getCmp('serviceOrganizDetailwin');
	var config=window.config.record.data;
	Ext.create('ShowServiceOrganizUmentTheOriginalActivity',{
		organizName:config.organizName,
		activityCondition:config.activityCondition
	}).show();
	//console.log(window.config.record.data.theOriginal);
}


//活动情况
Ext.define('ShowServiceOrganizUmentTheOriginalActivity',{
	height:350,
	width:600,
	autoScroll:true,
	extend:'Ushine.win.Window',
	maximizable:true,
	buttonAlign:"center",
	layout:'fit',
	border:false,
	
	constructor:function(config){
		var self=this;
		this.listeners={
		'afterrender':function(){
				Ext.get('OrganizUmentTheOriginalActivity').dom.innerHTML=config.activityCondition;
			}
		},
		this.title=config.organizName+'活动情况',
		this.items=[{
			xtype:'panel',
			autoScroll:true,
			border:false,
			html:"<div id='OrganizUmentTheOriginalActivity'></div>"
		}];
		this.buttons=[
			Ext.create('Ushine.buttons.Button',{
				text: '打印',
		   		baseCls: 't-btn-red',
		   		handler:function(){
		   			$('#OrganizUmentTheOriginalActivity').printArea();
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
});
//显示基本情况
function showServiceBasicConditionUmentDetail(){
	//query函数能够获得多个符合条件的
	var window=Ext.getCmp('serviceOrganizDetailwin');
	var config=window.config.record.data;
	Ext.create('ShowServiceBasicConditionUmentTheOriginal',{
		organizName:config.organizName,
		basicCondition:config.basicCondition
	}).show();
	//window.open("login.html","基本情况","width=1440,height=900")  
}

//显示基本情况
Ext.define('ShowServiceBasicConditionUmentTheOriginal',{
	height:350,
	width:600,
	maximizable:true,
	autoScroll:true,
	extend:'Ushine.win.Window',
	buttonAlign:"center",
	layout:'fit',
	border:false,
	constructor:function(config){
		/*this.title=config.organizName+'基本情况',
		this.html=config.basicCondition;
		this.callParent();*/
		var self=this;
		this.listeners={
		'afterrender':function(){
				Ext.get('BasicConditionUmentTheOriginal').dom.innerHTML=config.basicCondition;
			}
		},
		this.title=config.organizName+'基本情况',
		this.items=[{
			xtype:'panel',
			autoScroll:true,
			border:false,
			html:"<div id='BasicConditionUmentTheOriginal'></div>"
		}];
		this.buttons=[
			Ext.create('Ushine.buttons.Button',{
				text: '打印',
		   		baseCls: 't-btn-red',
		   		handler:function(){
		   			$('#BasicConditionUmentTheOriginal').printArea();
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
});