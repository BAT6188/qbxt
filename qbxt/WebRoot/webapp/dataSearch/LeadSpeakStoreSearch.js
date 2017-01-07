/**
 * 一键搜索命中领导讲话的文档
 */
Ext.define('Ushine.dataSearch.LeadSpeakStoreSearch',{
	extend:'Ext.panel.Panel',
	id:'dataserach_leadspeakstoresearch',
	bodyStyle: 'background-color: #ffffff; border: none; padding: 10px;',
	layout: {
		type: 'vbox',
		align : 'stretch',
		pack  : 'start'
	},
	constructor: function(config) {
		var self = this;
		//console.log(config.fieldValue);
		this.items = [         
		   // 标题栏
		   Ext.create('Ushine.base.TitleBar', {
		       cTitle: "符合条件为<font color='orange'>"+config.fieldValue+"</font>的领导讲话文档",
		       btnItems: [
						// 返回任务操作
						Ext.create('Ushine.buttons.MiniButton', {
							id: 'returnBtn',
							handler: function () {
								Ext.getCmp('content_frame').removeAll();
								var dataSearch=Ext.create('Ushine.dataSearch.DataSearchPenel');
								Ext.getCmp('content_frame').add(dataSearch);
								//获得子组件
								dataSearch.down('textfield').setValue(config.fieldValue);
								Ext.getCmp('dataSearchBtn').handler();
							}
						})
					]
		   }),{
			// 工具栏
			xtype : 'panel',
		    baseCls : 'tar-body',
			height:80,
			style:"margin-top:5px;",
			layout:'fit',
			items:{
				//表单
				xtype:'form',
				border:true,
				//height:120,
				id:'labl',
				baseCls: 'form-body1',
				items:[{
					    	layout: "column", //行1
					    	height: 25,
							baseCls: 'panel-body',
							style:'margin-top:-10px;',
							items:[{
							id:'field',
							fieldLabel: '字段筛选',
							labelAlign : 'right',
							labelWidth: 60,
		            		xtype:'combo',
		            		allowNegative: false,
		            		allowBlank: false,
		            		editable: false,
		            		hiddenName: 'colnum',
		            		name:'colnum',
		            		emptyText: '请选择字段',
		            		valueField: 'value',
		            		store:Ext.create('Ext.data.Store', {
		            		    fields: ['text', 'value'],
		            		    data : [
		            		    	//字段筛选
		            		    	{"text":"任意字段", "value":"anyField"},
		            		        /*{"text":"姓名", "value":"personName"},
		            		        {"text":"曾用名", "value":"nameUsedBefore"},
		            		        {"text":"英文名", "value":"englishName"},
		            		        {"text":"现住地址", "value":"presentAddress"},
		            		        {"text":"工作单位", "value":"workUnit"},
		            		        {"text":"户籍地址", "value":"registerAddress"},
		            		        {"text":"出生日期", "value":"bebornTime"}*/
		            		    ]
		            		}),
		            		value:'anyField',
							width: 260
						},{
							fieldLabel: '关&nbsp;键&nbsp;&nbsp;字',
							id		  :'fieldValue',
	                    	xtype     : 'textfield',
	                    	name      : 'filtrateKeyword',
	                    	emptyText : '请输入相应字段的关键字...',
							height: 24,
							labelAlign : 'right',
							labelWidth: 100,
							width: 300,
							listeners:{
								 specialkey:function(field,e){
		                           if(e.getKey() == e.ENTER){
		                           		//按下enter键
			   			        		self.remove('d_leadspeakstoresearch_gridpanel');
			   			        		self.add(Ext.create('Ushine.dataSearch.LeadSpeakStoreSearchGridPanel',{
			   			        			fieldValue:config.fieldValue,
			   			        			nowFieldValue:Ext.getCmp('fieldValue').getValue()
			   			        		}));
		                           }  
		                        }
							}
							//value:
						}]
				},{
					layout: "column", //行2
					height: 45,
					baseCls: 'form-body',
					items:[{
						labelAlign : 'right',
						fieldLabel: '开始时间:',
						id:'startTime',
						labelWidth: 60,
						
						format: 'Y-m-d', 
						//editable:false,
						xtype: 'datefield',
						maxValue: new Date(),
						height: 22,
						width: 260,
						value:getYearFirstDay()
					},{
						labelAlign : 'right',
						fieldLabel: '结束时间:',
						labelWidth: 100,
						format: 'Y-m-d', 
						xtype: 'datefield',
						//editable:false,
						height: 22,
						width: 300,
						id:'endTime',
						maxValue: new Date(),
						value:new Date()
					},
					Ext.create('Ushine.buttons.Button', {
			        	text : '查询文档',
			        	style:"margin-left:20px;",
			        	width:100,
			        	labelWidth: 60,
			        	height:22,
			        	id : 'search-Button',
			        	baseCls: 't-btn-red',
			        	handler:function(){
			        		//查询
			        		self.remove('d_leadspeakstoresearch_gridpanel');
   			        		self.add(Ext.create('Ushine.dataSearch.LeadSpeakStoreSearchGridPanel',{
   			        			fieldValue:config.fieldValue,
   			        			nowFieldValue:Ext.getCmp('fieldValue').getValue()
   			        		}));
		                }
			        }),Ext.create('Ushine.buttons.Button', {
			        	text : '条件重置',
			        	width:100,
			        	style:"margin-left:10px;",
			        	id : 'reset-Button',
			        	baseCls: 't-btn-yellow',
			        	height:22,
			        	handler:function(){
			        		Ext.getCmp("labl").getForm().reset();
			        	}
			        })]
				}]
			}
		},
			//领导讲话文档gridpanel
			new Ushine.dataSearch.LeadSpeakStoreSearchGridPanel({
				fieldValue:config.fieldValue,
				nowFieldValue:''
			})
		];	
		this.callParent();		
	}
});

