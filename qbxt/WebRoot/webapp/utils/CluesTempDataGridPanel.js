/**
 * 档案管理数据面板
 * @author 王百林
 */
Ext.define('Ushine.utils.CluesTempDataGridPanel', {
	extend: 'Ext.grid.Panel',
	//enableColumnResize:是否允许改变列宽，默认为true
	id:"CluesTempDataGridPanelId",
	itemId:'CluesTempDataId',
	border:true,
	height:200,
	hideHeaders:false,  //隐藏列头部
	stripeRows:true,		    //True来实现隔行换颜色
	autoHeight : true,
	rowLines:true,   //是否显示行分隔线
	disableSelection: false,   //是否禁止行选择，默认false
	columnLines:true,		   //添加列的框线样式
    loadMask: true,           //是否在加载数据时显示遮罩效果，默认为false
	defaults: {sortable: false},
	
	viewConfig:{
		emptyText: '还没有数据',
    	stripeRows:true,//在表格中显示斑马线
    	enableTextSelection:true //可以复制单元格文字
	},
	constructor: function(sjnum) {
		var self = this;
		this.store=Ext.create('Ext.data.JsonStore',{
			pageSize:50,
			fields:[{name : 'id',type : 'string',mapping : 'id'},
			        {name : 'name',type : 'string',mapping : 'name'},
			        {name : 'type',type : 'string',mapping : 'type'},
			        {name : 'state',type : 'string',mapping : 'state'},
			        {name : 'dataId',type : 'string',mapping : 'dataId'},
			        {name : 'action',type : 'string',mapping : 'action'}
			],
			proxy:{
				type:'ajax',
				url:'findTempClueData.do?number='+sjnum,
				method:'POST',
				simpleSortMode:true,
				reader:{
	                type: 'json',
	                root: 'datas'
				}
			},
			autoLoad:true,
			//autoSync:true
		});
		this.columns=[
		    {text:'id',dataIndex:'id',menuDisabled:true,hidden:true},
		    {text:'名称',dataIndex:'name',menuDisabled:true,flex: 1},
		    {text:'对象类别',dataIndex:'type',menuDisabled:true,flex: 1,renderer:function(value){
		    	if(value == 'personStore'){
		    		return "人员库信息";
		    	}else if(value == 'organizStore'){
		    		return "组织库信息";
		    	}else{
		    		return "媒体库信息";
		    	}
		    }},
		    {text: '状态',  dataIndex: 'state',flex: 1,menuDisabled:true,renderer:function(value){  
		    	if(value==1){
	         		   return "已入库";
	         	   }else{
	         		   return "未入库";
	         	   }
		    	
	        }},
		    {text:'数据id',dataIndex:'dataId',menuDisabled:true,hidden:true},
		    {text:'标识',dataIndex:'action',menuDisabled:true,hidden:true},
		    {text: '操作',sortable: false,flex: 1,xtype:'actioncolumn',
		    	items:[{
			            icon: 'images/database--plus.png',
			            tooltip: '入库',
			            handler: function(grid, rowIndex, colIndex){
			            	//获取点击行的数据
			            	var data = self.store.getAt(rowIndex).data;
			            	//console.log(data);
			            	//判断当前数据是否已入库，并提示及操作
			            	if(data.state=='1'){
			            		Ext.create('Ushine.utils.Msg').onInfo("该数据已入库，不能再次入库");
			            	}else{
			            		var type=data.type;
			            		//根据类别来调用相关的函数新增相关的数据
						        if(type == 'personStore'){
						        	//新增人员
						        	var win=Ext.create('SaveOrUpdatePersonInfoWin',{
										title:'添加人员',
										//是线索人员
										isClue:'isClue',
										cluePersonNum:sjnum,
										//还没有关联人员
										state:'0',
										items:[Ext.create('SavePersonInfoForm')]
									});
									win.show();
									//设置form表单的值
									var field=win.getComponent(0).getForm().findField('personName');
									field.setValue(data.name);
									Ext.getCmp('certificatestypegrid').getStore().removeAll();
									Ext.getCmp('networkaccounttypegrid').getStore().removeAll();
									
						        }else if(type == 'organizStore'){
						        	//新增组织信息
						        	var win=Ext.create('SaveOrganizeInfoWin',{
						        		//属于线索组织
						        		isClue:'isClue',
						        		clueOrganizNum:sjnum,
						        		//还没有关联组织
										state:'0'
						        	});
						        	win.show();
									//设置名称
						        	var form=win.getComponent(0).getForm();
						        	form.findField('organizName').setValue(data.name);
						        }else if(type == 'websiteJournalStore'){
						        	  //新增媒体刊物信息
						        	  var win=Ext.create('SaveMediaNetworkBookWin',{
						        		 isClue:'isClue',
						        		 clueNum:sjnum,
						        		 //还没有关联媒体
										state:'0'
						        	 });
						        	 //设置名称
						        	var form=win.getComponent(0).getForm();
						        	form.findField('name').setValue(data.name);
						        	 win.show();
						        }
			            		//Ext.create('Ushine.utils.Msg').onInfo("可以入库");
			            	}
			            }
		             },"_",{
		            icon: 'images/cancel.png',
		            tooltip: '删除',
		            handler: function(grid, rowIndex, colIndex){
		            	//获取点击行的数据
		            	var data = self.store.getAt(rowIndex).data;
		            	Ext.Ajax.request({
		            	    url: 'dateTempClueDataById.do',
		            	    params: {
		            	        id: data.id,
		            	    },
		            	    success: function(response){
		            	    	var text = response.responseText;
			    			       var obj=Ext.JSON.decode(text);
			    			       if(obj.success){
			    			    	   self.getStore().load();
			    			       }else{
			    			    	   Ext.create('Ushine.utils.Msg').onInfo("请求服务器失败,请联系管理员！");
			    			       }
		            	    },
		            	    failure:function(response){
		   						var obj=Ext.JSON.decode(response.responseText);
		   						Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
		   					}
		            	});
		            }
	             }],menuDisabled:true
		    }];
		this.callParent(); 
	}
});