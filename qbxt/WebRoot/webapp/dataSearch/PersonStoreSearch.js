/**
 * 一键搜索命中人员
 */
Ext.define('Ushine.dataSearch.PersonStoreSearch',{
	extend:'Ext.panel.Panel',
	id:'dataserach_personstoreserach',
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
		       cTitle: "符合条件为<font color='orange'>"+config.fieldValue+"</font>的人员",
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
							margin:'0 15 0 0', 
							listeners:{
								 specialkey:function(field,e){
		                           if(e.getKey() == e.ENTER){
		                           		//按下enter键
		   			        			self.remove('p_personstoresearch_gridpanel');
		   			        			self.add(Ext.create('Ushine.dataSearch.PersonStoreSearchGridPanel',{
		   			        				fieldValue:config.fieldValue,
		   			        				nowFieldValue:Ext.getCmp('fieldValue').getValue()
		   			        			}));
		                           }  
		                        }
							}
						},
						Ext.create('Ushine.buttons.IconButton', {
								id: 'downExcelBtn',
					    	   btnText: '导出人员',
					    	   handler: function () {
					    		  var personStoreGrid=Ext.getCmp('personstoresearchgridpanel');	
					    		  //传递时间参数
					    		  var date=Ext.Date.format(new Date(),'YmdHms');
							   	   if(personStoreGrid.getSelectionModel().hasSelection()){
							   	   		//允许多行
							   	   		var record=personStoreGrid.getSelectionModel().getSelection();
							   	   		var personStoreIds=[];
							   	   		for(var i=0;i<record.length;i++){
							   	   			personStoreIds.push(record[i].get('id'));
							   	   		}
							   	   		//导入到Excel中
							   	   		//导出并下载
							   	   		exportPersonStoreToExcel(self,personStoreIds,date);
							   	   }else{
							   	   		Ext.create('Ushine.utils.Msg').onInfo("请至少选择一行数据");
							   	   }
					    	   }
					       })
						]
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
			        	text : '查询人员',
			        	style:"margin-left:20px;",
			        	width:90,
			        	labelWidth: 60,
			        	height:22,
			        	id : 'search-Button',
			        	baseCls: 't-btn-red',
			        	handler:function(){
			        		//查询
			        		self.remove('p_personstoresearch_gridpanel');
			        		self.add(Ext.create('Ushine.dataSearch.PersonStoreSearchGridPanel',{
			        			fieldValue:config.fieldValue,
			        			nowFieldValue:Ext.getCmp('fieldValue').getValue()
			        		}));
		                }
			        }),Ext.create('Ushine.buttons.Button', {
			        	text : '条件重置',
			        	width:90,
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
		//人员gridpanel
			new Ushine.dataSearch.PersonStoreSearchGridPanel({
				fieldValue:config.fieldValue,
				nowFieldValue:''
			})
		];	
		this.callParent();		
	}
});



















