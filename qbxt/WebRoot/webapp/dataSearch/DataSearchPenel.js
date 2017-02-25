/**
 * 数据检索
 * 
 */
 Ext.define('Ushine.dataSearch.DataSearchPenel', {
	extend: 'Ext.panel.Panel',
	id: 'dataSearchPenel-panel',
	region: 'center',
	bodyStyle: 'background-color: #ffffff; border: none; padding: 10px;',
	layout: {
		type: 'vbox',
		align : 'stretch',
		pack  : 'start'
	},
	autoScroll:true,
	
	searchDataPostion:function(datas,storeName){
		var value;
		$(datas).each(function(index,data){
			if(data.storeName==storeName){
				//console.log(data.storeName);
				value=data;
			}
		});
		return value;
	},
	/*
	 * 显示符合关键字的数据
	 */
	showStoresCount:function(data,gridPanel,createGridPanel,parentPanel,htmlId,fieldValue){
		//总数
		var dataCount=data.dataCount;		
		//console.log(dataCount);
		//具体数据
		var datas=data.datas.datas;
		//console.log(fieldValue);
		//设置文本
		var panel=Ext.getCmp(gridPanel);
		//先移除panel再加载panel
		if(panel){
			Ext.getCmp(parentPanel).remove(panel);
		}
		var newPanel;
		if(datas.length>0){
			if(parentPanel=='cluestorespanel'){
				newPanel=Ext.create(createGridPanel,{
					fieldValue:fieldValue
				});
			}else{
				newPanel=Ext.create(createGridPanel);
			}
			newPanel.getStore().add(datas);
			//有才显示
			Ext.getCmp(parentPanel).setVisible(true);
			Ext.getCmp(parentPanel).add(newPanel);
		}else{
			//没有则隐藏
			Ext.getCmp(parentPanel).setVisible(false);
		}
		$(htmlId).html(data.dataType+"：<span style='color:red'>共"+dataCount+"条</span>");
	}, 
	constructor: function() {
		var self = this;
		var fieldValue;
		//加载10条数据
		var size=10;
		//查询
		this.searchData=function(content){
			var loadMask=new Ext.LoadMask(self,{
				msg:'正在进行搜索,请耐心等待……'
			});
			loadMask.show();
			//1分钟超时
			//Ext.Ajax.timeout=1000*60;
			Ext.Ajax.request({
				url:'searchForKeyWord.do',
				params:{
					fieldValue:content,
					size:size
				},
				method:'post',
				success:function(response){
					var data=Ext.JSON.decode(response.responseText);
					//console.log(data);
					var i=0;
					$(data).each(function(temp,value){
						//console.log(value);
						if(value.dataCount==0){
							i++;
						}
					});
					if(i==data.length){
						//全部隐藏
						var panels=['personstorespanel',
							'cluestorespanel','outsidedocstorespanel','servicedocstorespanel',
							'leadspeakstorespanel'];
						$(panels).each(function(index,panel){
							var temp=Ext.getCmp(panel);
							if(temp){
								temp.hide();
							}
						})
						Ext.create('Ushine.utils.Msg').onInfo("没有符合条件的数据");
					}else{
						//线程异步执行任务
						//人员的
						var person = new Ext.util.TaskRunner();
						person.start({
							run:function(){
								self.showStoresCount(self.searchDataPostion(data,'PersonStore'),'storesearch_persongridpanel',
										'Ushine.storesearch.PersonStoreGridPanel','personstorespanel','#personstorecount');
							},
							interval:0,
							repeat:1
						});
						//线索
						var clue = new Ext.util.TaskRunner();
						clue.start({
							run:function(){
								var fieldValue=Ext.getCmp('fieldValue').getValue();
								self.showStoresCount(self.searchDataPostion(data,'ClueStore'),'storesearch_cluegridpanel',
									'Ushine.storesearch.ClueStoreGridPanel','cluestorespanel','#cluestorecount',fieldValue);
							},
							interval:0,
							repeat:1
						});
						//外来文档
						var outside = new Ext.util.TaskRunner();
						outside.start({
							run:function(){
								self.showStoresCount(self.searchDataPostion(data,'OutsideDocStore'),'storesearch_outsidedocgridpanel',
										'Ushine.storesearch.OutsideDocStoreGridPanel','outsidedocstorespanel','#outsidedocstorecount');
							},
							interval:0,
							repeat:1
						});
						//业务文档
						var vocation= new Ext.util.TaskRunner();
						vocation.start({
							run:function(){
								self.showStoresCount(self.searchDataPostion(data,'VocationalWorkStore'),'storesearch_servicedocgridpanel',
										'Ushine.storesearch.ServiceDocStoreGridPanel','servicedocstorespanel','#servicedocstorecount');
							},
							interval:0,
							repeat:1
						});
						//领导讲话
						var lead=new Ext.util.TaskRunner();
						lead.start({
							run:function(){
								self.showStoresCount(self.searchDataPostion(data,'LeadSpeakStore'),'storesearch_leadspeakgridpanel',
										'Ushine.storesearch.LeadSpeakStoreGridPanel','leadspeakstorespanel','#leadspeakstorecount');
							},
							interval:0,
							repeat:1
						});
					}
					loadMask.hide();
				},
				failure:function(){
					loadMask.hide();
					Ext.create('Ushine.utils.Msg').onInfo("请求后台失败");
				}
			})
		}
		this.items = [
		    // 标题栏
			Ext.create('Ushine.base.TitleBar2', {
				cTitle: '数据搜索',
				height:50,
				btnItems:[{
					// 工具栏
					xtype : 'panel',
					baseCls: 'panel-body',
					//height:40,
					border:true,
					style:'border:0px solid red;text-align:center;',
					layout:{
						type:'hbox',
						align:'middle',
						pack:'center'
					},
					region:'center',
					items:[{
						id		  :'fieldValue',
		            	xtype     : 'textfield',
		            	name      : 'filtrateKeyword',
		            	emptyText : '请输入搜索关键字....',
						height: 30,
						//size:20,
						labelAlign : 'right',
						labelWidth: 100,
						width: 400,
						listeners:{
							 specialkey:function(field,e){
							   
		                       if(e.getKey() == e.ENTER){
		                    	   var content = Ext.getCmp('fieldValue').value;
		                    	   //用jquery进行trim
		                    	   content=$.trim(content);
		                    	   if(content){
			                    	   self.searchData(content);
			                    	   fieldValue=content;
		                    	   }else{
		                    	   	   Ext.create('Ushine.utils.Msg').onInfo('请输入有效的关键字');	
		                    	   }
		                       }  
		                    }
						}
					},Ext.create('Ushine.buttons.IconButton2', {
			    	   border: false,
			    	   id: 'dataSearchBtn',
			    	   //width:80,
			    	   btnText: '搜  索',
			    	   handler: function() {
			    		   var content = Ext.getCmp('fieldValue').value;
                    	   content=$.trim(content);
			    		   if(content){
	                    	   self.searchData(content);
	                    	   fieldValue=content;
                    	   }else{
                    	   	   Ext.create('Ushine.utils.Msg').onInfo('请输入有效的关键字');	
                    	   }
			    	   }
			       })]
				}]
			}),{
				//人员
				margin:'5 0 0 0',
				xtype:'panel',
				layout:{
					type:'vbox',
					align:'stretch'
				},
				border:false,
				id:'personstorespanel',
				items:[{
					xtype:'panel',
					border:false,
					margin:'0 0 5 0',
					//重点人员库：<span style='color:red'>0条</span>
					html:"<span id='personstorecount'></span>"
				}]
			},{
				//组织
				margin:'5 0 0 0',
				xtype:'panel',
				layout:{
					type:'vbox',
					align:'stretch'
				},
				border:false,
				id:'organizstorespanel',
				items:[{
					xtype:'panel',
					border:false,
					margin:'0 0 5 0',
					//重点组织库：<span style='color:red'>0条</span>
					html:"<span id='organizstorecount'></span>"
				}]
			},{
				//业务文档
				margin:'5 0 0 0',
				xtype:'panel',
				layout:{
					type:'vbox',
					align:'stretch'
				},
				border:false,
				id:'servicedocstorespanel',
				items:[{
					xtype:'panel',
					border:false,
					margin:'0 0 5 0',
					//业务文档库：<span style='color:red'>0条</span>
					html:"<span id='servicedocstorecount'></span>"
				}]
			},{
				//外来文档
				margin:'5 0 0 0',
				xtype:'panel',
				layout:{
					type:'vbox',
					align:'stretch'
				},
				border:false,
				id:'outsidedocstorespanel',
				items:[{
					xtype:'panel',
					border:false,
					margin:'0 0 5 0',
					//外来文档库：<span style='color:red'>0条</span>
					html:"<span id='outsidedocstorecount'></span>"
				}]
			},{
				//领导讲话
				margin:'5 0 0 0',
				xtype:'panel',
				layout:{
					type:'vbox',
					align:'stretch'
				},
				border:false,
				id:'leadspeakstorespanel',
				items:[{
					xtype:'panel',
					border:false,
					margin:'0 0 5 0',
					//领导讲话库：<span style='color:red'>0条</span>
					html:"<span id='leadspeakstorecount'></span>"
				}]
			},{
				//媒体刊物
				margin:'5 0 0 0',
				xtype:'panel',
				layout:{
					type:'vbox',
					align:'stretch'
				},
				border:false,
				id:'websitestorespanel',
				items:[{
					xtype:'panel',
					border:false,
					margin:'0 0 5 0',
					//媒体网站刊物库：<span style='color:red'>0条</span>
					html:"<span id='websitestorecount'></span>"
				}]
			},{
				//线索
				margin:'5 0 0 0',
				xtype:'panel',
				layout:{
					type:'vbox',
					align:'stretch'
				},
				border:false,
				id:'cluestorespanel',
				items:[{
					xtype:'panel',
					border:false,
					margin:'0 0 5 0',
					//线索库：<span style='color:red'>0条</span>
					html:"<span id='cluestorecount'></span>"
				}]
			}];	
		this.callParent();		
	}
});



