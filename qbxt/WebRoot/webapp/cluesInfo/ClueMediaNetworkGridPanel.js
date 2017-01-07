/**
 * 媒体网站刊物gridpanel
 */
Ext.define('Ushine.cluesInfo.ClueMediaNetworkGridPanel',{
	extend:'Ext.grid.Panel',
	id:'clueMedianetworkbookgrid',
	itemId:'d_cluemedianetworkbook_grid',
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
	constructor:function(clueId){
		var self=this;
		var store=Ext.create('Ext.data.JsonStore',{
			pageSize:50,
			model:'MediaNetworkBookModel',
			remoteStore:true,
			//请求后台服务
			proxy:{
				type:'ajax',
				url:'findClueWebsiteJournal.do?clueId='+clueId,
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
			//设置列id
			{text:'ID',dataIndex:'id',flex: 1,menuDisabled:true,hidden:true},
			{text: '所属线索',  dataIndex: 'clueName',sortable: false,flex: 1,menuDisabled:true},
			{text: '是否入库',  dataIndex: 'isToStorage',sortable: false,menuDisabled:true,width:80,renderer:function(value){
	          	  if(value=="1"){
	          		   return "已入库";
	          	   }else{
	          		   return "未入库";
	          	   }
		    }},
			{text: '类别',  dataIndex: 'infoType',sortable: false,flex: 1,menuDisabled:true},
		    {text: '刊物名称',  dataIndex: 'name',sortable: false,flex: 1,menuDisabled:true},
		    {text: '域名',  dataIndex: 'websiteURL',sortable: false,flex: 1 ,menuDisabled:true},
		    {text: '服务器所在地',  dataIndex: 'serverAddress',sortable: false,flex: 1,menuDisabled:true},
		    {text: '创办地',  dataIndex: 'establishAddress',sortable: false,flex: 1,menuDisabled:true},
		    {text: '主要发行地',  dataIndex: 'mainWholesaleAddress',sortable: false,flex: 1,menuDisabled:true},
		    {text: '创办人',  dataIndex: 'establishPerson',sortable: false,flex: 1,menuDisabled:true},
		    {text: '创办时间',  dataIndex: 'establishTime',sortable: false,flex: 1,menuDisabled:true},
		    {text: '数据创建时间',  dataIndex: 'createDate',width:160,menuDisabled:true,renderer:function(value){
	         	   return Ext.Date.format(value, 'Y-m-d H:i:s');
	        }},
		    {text: '基本情况',  dataIndex: 'basicCondition',hidden:true,sortable: false,flex: 1,menuDisabled:true},
	        {text: '操作',sortable: true,dataIndex: 'isToStorage',align:'center',width:100,xtype:'actioncolumn',
		    	items:[{
		            icon: 'images/user-red.png',
		            tooltip: '数据入库',
		            style:'margin-left:20px;',
		            handler: function(grid, rowIndex, colIndex){
		            	var data = self.store.getAt(rowIndex).data;   //选中一行的数据
		            	if(data.isToStorage=='2'){
		            		self.dataStorageWebsiteJournalStore(data);
		            	}
		            }
	             }],
	             menuDisabled:true,
	             renderer:function(value){
	            	 var self = this;
            		 //显示线索涉及对象人员图标
            		 if(value=='2'){
            			self.items[0].icon ="images/block--plus.png";
            		 }else{
            			 self.items[0].icon ="images/block--plus1.png";
            		 }
		        }
		    }
		];
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
				//console.log(record);
				Ext.create('MediaNetworkDetailWindow',{
					record:record
				}).show();
			}
		};
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
		this.callParent(); 
	},
	//数据入库操作
	dataStorageWebsiteJournalStore:function(res){
		//定义一个SaveOrganizeInfoWin的Window
		Ext.define('DataStorageWebsiteJournalInfoWin',{
			extend:'Ushine.win.Window',
			title : "数据入库",
			modal : true,
			id:'dataStorageWebsiteJournal_formpanel',
			layout : 'fit',
			border : false,
			closable : true,
			draggable:true,
			resizable : false,
			plain : true,
			bodyPadding: 5,
			buttonAlign:"center",
			borer:false,
			height: 480,
		    width: 575,
			constructor:function(){
				var self=this;
				this.items=[{
				         xtype:'form',
					     id:'dataStorageWebsiteJounalForm',
					     border:false,
					     items:[{
					            layout: 'hbox',
					            bodyPadding: 5,
					            margin: 0,
					            border: false,
					            buttonAlign: "center",
					            items: [{
					                fieldLabel: '名称',
					                labelStyle: 'color:red;',
					                allowBlank: false,
					                xtype: 'textfield',
					                emptyText: '请输入媒体/网站/刊物名称',
					                blankText: '此选项不能为空',
					                width: 250,
					                labelAlign: 'right',
					                labelWidth: 60,
					                height: 22,
					                name: 'name',
					                value:res.name
					            }, {
					                fieldLabel: '创办人',
					                labelStyle: 'color:red;',
					                allowBlank: false,
					                xtype: 'textfield',
					                labelAlign: 'right',
					                emptyText: '请输入创办人',
					                blankText: '此选项不能为空',
					                width: 285,
					                labelWidth: 95,
					                height: 22,
					                name: 'establishPerson'
					            }]
					        }, {
					            layout: 'hbox',
					            height: 35,
					            bodyPadding: 5,
					            margin: 0,
					            border: false,
					            buttonAlign: "center",
					            items: [{
					                fieldLabel: '类别',
					                labelStyle: 'color:red;',
					                allowBlank: false,
					                xtype: 'combo',
					                emptyText: '请选择类别',
					                blankText: '此选项不能为空',
					                width: 250,
					                labelAlign: 'right',
					                labelWidth: 60,
					                // height:22,
					                name: 'infoType',
					                id:'infoType',
					                allowNegative: false,
					                editable: false,
					                hiddenName: 'colnum',
					                valueField: 'value',
					                store: Ext.create('Ext.data.Store', {
					                    fields: ['text', 'value'],
					                    proxy: {
					                        url: 'getwebsitejournalstoretype.do',
					                        type: 'ajax',
					                        reader: {
					                            type: 'json'
					                        }
					                    }
					                })
					            }, {
					                fieldLabel: '创办时间',
					                labelStyle: 'color:red;',
					                width: 285,
					                labelAlign: 'right',
					                emptyText: '请输入创办时间',
					                labelWidth: 95,
					                name: 'establishTime',
					                format: 'Y-m-d',
					                xtype: 'datefield',
					                maxValue: new Date(),
					                height: 22,
					                value: new Date()
					            }]
					        }, {
					            layout: 'hbox',
					            bodyPadding: 5,
					            // margin:'15 0 0 0',
					            border: false,
					            buttonAlign: "center",
					            items: [{
					                fieldLabel: '创办地',
					                xtype: 'textfield',
					                labelStyle: 'color:red;',
					                width: 250,
					                labelAlign: 'right',
					                emptyText: '请输入创办地',
					                labelWidth: 60,
					                name: 'establishAddress'
					            }, { 
					            	fieldLabel: '主要发行地',
					                labelStyle: 'color:black;',
					                //allowBlank: false,
					                xtype: 'textfield',
					                labelAlign: 'right',
					                emptyText: '请输入主要发行地',
					                blankText: '此选项不能为空',
					                width: 285,
					                labelWidth: 95,
					                height: 22,
					                name: 'mainWholesaleAddress'
					            }]
					        }, {
					            layout: 'hbox',
					            bodyPadding: 5,
					            // margin:'15 0 0 0',
					            border: false,
					            buttonAlign: "center",
					            items: [{
					                fieldLabel: '域名',
					                xtype: 'textfield',
					                labelStyle: 'color:black;',
					                width: 250,
					                labelAlign: 'right',
					                emptyText: '请输入域名',
					                labelWidth: 60,
					                name: 'websiteURL'
					            }, {
					                fieldLabel: '服务器所在地',
					                xtype: 'textfield',
					                labelStyle: 'color:black;',
					                width: 285,
					                labelAlign: 'right',
					                emptyText: '请输入服务器所在地',
					                labelWidth: 95,
					                name: 'serverAddress'
					            }]
					        }, {
					            layout: 'hbox',
					            bodyPadding: 5,
					            // margin:'15 0 0 0',
					            border: false,
					            buttonAlign: "center",
					            items: [{
					                fieldLabel: '基本情况',
					                xtype: 'htmleditor',
					                labelStyle: 'color:black;',
					                width: 534,
					                labelAlign: 'right',
					                emptyText: '请输入基本情况',
					                labelWidth: 60,
					                height: 220,
					                enableFont:false,
					                name: 'basicCondition'
					            }]
					        }]
				}
				     ]
				
				  this.buttons=[
				  	Ext.create('Ushine.buttons.Button', {
				   		text: '确定',
				   		baseCls: 't-btn-red',
				   		handler: function () {
				   			var form  = Ext.getCmp('dataStorageWebsiteJounalForm');
				   			//console.log(form.getForm());
				   			//提交给后台处理
				   			if(form.getForm().isValid()){
				   				//取到类别的id
				   				var type = Ext.getCmp('infoType').displayTplData[0].value;
				   				form.getForm().submit({
				   					url:'dataStorageWebsiteJounalById.do',
				   					method:'POST',
				   					params:{
				   						typeId:type,
				   						id:res.id
				   						},
				   					submitEmptyText:false,
				   					success:function(form,action){
				   						if(action.result.success){
				   							var obj=Ext.JSON.decode(action.response.responseText);
				   							Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				   							Ext.getCmp('dataStorageWebsiteJournal_formpanel').close();
				   							Ext.getCmp('clueMedianetworkbookgrid').getStore().reload();
				   							////取消选择
											Ext.getCmp('clueMedianetworkbookgrid').getSelectionModel().clearSelections();
				   							
				   						}else{
				   							Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				   						}
				   					},
				   					failure:function(response){
				   						var obj=Ext.JSON.decode(response.responseText);
				   						Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
				   						Ext.getCmp('dataStorageWebsiteJournal_formpanel').close();
				   					}
				   				});
				   			}
				   		}
				   	}),Ext.create('Ushine.buttons.Button', {
				   		text: '重置',
				   		margin:'0 0 0 35',
				   		baseCls: 't-btn-yellow',
				   		handler: function () {
				   			self.getForm().reset();
				   		}
				   })
				  ];
				this.callParent();
			}
		});
		Ext.create('DataStorageWebsiteJournalInfoWin').show();
	}
})

//媒体网站文档模型
Ext.define('MediaNetworkBookModel',{
	extend:'Ext.data.Model',
	fields:['id','infoType','name','clueName','domain','websiteURL','serverAddress'
		,'establishAddress','mainWholesaleAddress','establishPerson','establishTime','isToStorage','basicCondition',{name: 'createDate', type:'date', mapping: 'createDate'}]
})
