Ext.define('Ushine.cluesInfo.CluesGridPanel',{
	extend:'Ext.grid.Panel',
	id:'cluesinfogridpanel',
	itemId:'c_cluesinfo_grid',
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
	constructor: function() {
		var self = this;
		var store = Ext.create('Ext.data.JsonStore',{
			pageSize:50,
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
		    /*{text: '是否启用',  dataIndex: 'isEnable',id:'isEnable',sortable: false,flex: 1,menuDisabled:true,renderer:function(value){
		    	if(value == '1'){
					return "否";
				}else{
					return "是";
				}
			}},*/
		    {text: '线索名称',  dataIndex: 'clueName',sortable: true,flex: 2,menuDisabled:true},
		    {text: '线索来源',  dataIndex: 'clueSource',sortable: true,flex: 2,menuDisabled:true},
		    {text: '发现时间',  dataIndex: 'findTime',sortable: true,flex: 2,menuDisabled:true},
	        {text: '数据创建时间',  dataIndex: 'createDate',width:160,menuDisabled:true,renderer:function(value){
	         	   return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }},
		    {text: '线索内容',  hidden:true,dataIndex: 'clueContent',sortable: true,flex: 1,menuDisabled:true},
		    {text: '工作进度及进展情况',  hidden:true,dataIndex: 'arrangeAndEvolveCondition',sortable: false,flex: 1,menuDisabled:true},
		    {text: '操作',dataIndex: 'icon',align:'center',flex: 0.5,xtype:'actioncolumn',sortable: false,
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
			        	Ext.getCmp('content_frame').add(new Ushine.cluesInfo.CluePerson(data.id,fieldValue));
		            }
	             }/*,{
			        icon: 'images/briefcase.png',
			        iconCls:'personStore',
			        tooltip: '组织对象',
			        handler: function(grid, rowIndex, colIndex){
			        	var data = self.store.getAt(rowIndex).data//选中一行的数据
			        	var fieldValue=Ext.getCmp('fieldValue').getValue();
			        	Ext.getCmp('content_frame').removeAll();
			        	Ext.getCmp('content_frame').add(new Ushine.cluesInfo.ClueOrganiz(data.id,fieldValue));
			         }
		         },{
				       icon: 'images/inbox-document-text.png',
				       iconCls:'personStore',
				       tooltip: '媒体刊物对象',
				       handler: function(grid, rowIndex, colIndex){
				    	   var data = self.store.getAt(rowIndex).data;   //选中一行的数据
				    	   var fieldValue=Ext.getCmp('fieldValue').getValue();
				    	   Ext.getCmp('content_frame').removeAll();
			    	       Ext.getCmp('content_frame').add(new Ushine.cluesInfo.ClueMediaNetworkBook(data.id,fieldValue));
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
	            		 
	            		 //显示线索涉及对象组织图标
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
	            		 }
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
	             menuDisabled:true
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
//数据模型
Ext.define('CluesStoreModel', {
	extend: 'Ext.data.Model',
    fields: [
             {name: 'id', type:'string', mapping: 'id'},
             {name: 'clueName', type:'string', mapping: 'clueName'},
             {name: 'clueSource', type:'string', mapping: 'clueSource'},
             {name: 'findTime', type:'string', mapping: 'findTime'},
             {name: 'isEnable', type:'string', mapping: 'isEnable'},
             {name: 'clueContent', type:'string', mapping: 'clueContent'},
             {name: 'isInvolvedObj', type:'string', mapping: 'isInvolvedObj'},
             {name: 'icon', type:'string', mapping: 'icon'},
             {name: 'createDate', type:'date', mapping: 'createDate'},
             {name: 'arrangeAndEvolveCondition', type:'string', mapping: 'arrangeAndEvolveCondition'}
             ],
    idProperty:'id'
});


//显示详细信息
Ext.define('ServiceClueDetailWin',{
	extend:'Ushine.win.Window',
	title:'线索详情信息',
	modal : true,
	layout:{
		type : 'vbox',
		align:'stretch'
	},
	id:'serviceCluedetailwin',
	border : false,
	closable : true,
	draggable:true,
	resizable : false,
	plain : true,
	borer:false,
	height:250,
	width:600,
	bodyPadding:20,
	autoScroll:true,
	constructor:function(config){
		this.config=config
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
					fieldLabel:'线索名称',
					value:config.record.get('clueName'),
					width: 250,
					labelAlign : 'right',
					labelWidth : 60
				},{
					xtype:'displayfield',
					fieldLabel:'线索来源',
					value:config.record.get('clueSource'),
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
					fieldLabel:'发现时间',
					//value:Ext.Date.format(config.record.get('findTime'), 'Y-m-d H:i'),
					value:config.record.get('findTime'),
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
					fieldLabel:'线索内容',
					//value:config.record.get('time'),
					width: 250,
					labelAlign : 'right',
					labelWidth : 60,
					value:"<a href='javascript:void(0)' onclick=showServiceClueUmentDetail()>" +
				  			"点击查看线索内容<a/>"
				}]
			},{
				//第三行
				layout:'hbox',
				margin:'10 0 0 0',
				border:false,
				items:[{
					xtype:'displayfield',
					fieldLabel:'进展情况',
					//value:config.record.get('time'),
					width: 250,
					labelAlign : 'right',
					labelWidth : 60,
					value:"<a href='javascript:void(0)' onclick=showServiceClueUmentDetailjin()>" +
				  			"点击查看进展情况<a/>"
				}]
			}]
		}]
		this.callParent();
	}
});



//显示进展
function showServiceClueUmentDetailjin(){
	//query函数能够获得多个符合条件的
	var window=Ext.getCmp('serviceCluedetailwin');
	var config=window.config.record.data;
	Ext.create('ShowServiceClueUmentTheOriginaljin',{
		clueName:config.clueName,
		arrangeAndEvolveCondition:config.arrangeAndEvolveCondition
	}).show();
}


//线索进展
Ext.define('ShowServiceClueUmentTheOriginaljin',{
	height:250,
	width:600,
	autoScroll:true,
	extend:'Ushine.win.Window',
/*	constructor:function(config){
		this.title=config.clueName+'进展情况',
		this.html=config.arrangeAndEvolveCondition;
		this.callParent();
	}*/
	buttonAlign:'center',
	border:false,
	layout:'fit',
	maximizable:true,
	constructor:function(config){
		var self=this;
		this.title=config.clueName+'进展情况';
		this.listeners={
		'afterrender':function(){
				Ext.get('ServiceClueUmentTheOriginaljin').dom.innerHTML=config.arrangeAndEvolveCondition;
			}
		},
		this.items=[{
			xtype:'panel',
			autoScroll:true,
			border:false,
			html:"<div id='ServiceClueUmentTheOriginaljin'></div>"
		}];
		this.buttons=[
			Ext.create('Ushine.buttons.Button',{
				text: '打印',
		   		baseCls: 't-btn-red',
		   		handler:function(){
		   			$('#ServiceClueUmentTheOriginaljin').printArea();
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
});
//显示线索内容
function showServiceClueUmentDetail(){
	//query函数能够获得多个符合条件的
	var window=Ext.getCmp('serviceCluedetailwin');
	var config=window.config.record.data;
	Ext.create('ShowServiceClueUmentTheOriginal',{
		clueName:config.clueName,
		clueContent:config.clueContent
	}).show();
}


//线索内容
Ext.define('ShowServiceClueUmentTheOriginal',{
	height:250,
	width:600,
	autoScroll:true,
	extend:'Ushine.win.Window',
	/*constructor:function(config){
		this.title=config.clueName+'线索内容',
		this.html=config.clueContent;
		this.callParent();
	}*/
	buttonAlign:'center',
	border:false,
	layout:'fit',
	maximizable:true,
	constructor:function(config){
		var self=this;
		this.title=config.clueName+'线索内容';
		this.listeners={
		'afterrender':function(){
				Ext.get('ServiceClueUmentTheOriginal').dom.innerHTML=config.clueContent;
			}
		},
		this.items=[{
			xtype:'panel',
			autoScroll:true,
			border:false,
			html:"<div id='ServiceClueUmentTheOriginal'></div>"
		}];
		this.buttons=[
			Ext.create('Ushine.buttons.Button',{
				text: '打印',
		   		baseCls: 't-btn-red',
		   		handler:function(){
		   			$('#ServiceClueUmentTheOriginal').printArea();
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
});