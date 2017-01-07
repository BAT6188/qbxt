/**
 * 媒体网站刊物库 author：donghao
 */
Ext.define('Ushine.docInfo.MediaNetworkBook', {
    extend: 'Ext.panel.Panel',
    id: 'medianetworkbook-panel',
    region: 'center',
    title: '媒体网站刊物',
    bodyStyle: 'background-color: #ffffff; border: none; padding: 10px;',
    layout: {
        type: 'vbox',
        align: 'stretch',
        pack: 'start'
    },
    constructor: function () {
        var self = this;
        var date=new Date();
		//第一个月
		date.setMonth(0);
		//第一天
		date.setDate(1);
		var startTime=Ext.Date.format(date,'Y-m-d');
        this.items = [{
            // 工具栏
            xtype: 'panel',
            baseCls: 'tar-body',
            height: 120,
            style: "margin-top:-10px;",
            layout: 'fit',
            items: {
                // 表单
                xtype: 'form',
                border: true,
                height: 120,
                id: 'labl',
                baseCls: 'form-body1',
                items: [{
                    layout: "column", // 行1
                    height: 41,
                    baseCls: 'panel-body',
                    items: [Ext.create('Ushine.buttons.IconButton', {
                        border: false,
                        id: 'createNewBtn',
                        btnText: '新增刊物',
                        baseCls: 't-btn-red',
                        handler: function () {
                            // 弹出新增业务文档
                            self.saveMediaNetworkBook();
                        }
                    }), Ext.create('Ushine.buttons.IconButton', {
                            id: 'updateBtn',
                            btnText: '修改刊物',
                            // width:120,
                            handler: function () {
                                // 修改
                                var grid = self.getComponent(1);
                                if (grid.getSelectionModel().hasSelection()) {
                                    // 只能选一行
                                    var record = grid.getSelectionModel()
                                        .getSelection();
                                    if (record.length > 1) {
                                        Ext.create('Ushine.utils.Msg')
                                            .onInfo("只能选择一行数据");
                                    } else {
                                        self.updateMediaNetWorkBook(record);
                                    }
                                } else {
                                    Ext.create('Ushine.utils.Msg')
                                        .onInfo("请至少选择一行数据");
                                }
                            }
                        }), Ext.create('Ushine.buttons.IconButton', {
                            id: 'delBtn',
                            btnText: '删除刊物',
                            // width:120,
                            handler: function () {
                                // 删除刊物
                                var grid = self.getComponent(1);
                                if (grid.getSelectionModel().hasSelection()) {
                                    // 允许多行
                                    var record = grid.getSelectionModel()
                                        .getSelection();
                                    var ids = [];
                                    for (var i = 0; i < record.length; i++) {
                                        ids.push(record[i].get('id'));
                                    }
                                    Ext.Msg.confirm("提示", "确定删除吗?",
                                        function (btn) {
                                            if (btn == 'yes') {
                                                self.delMediaNetWorkBook(ids);
                                            }
                                    })
                                } else {
                                    Ext.create('Ushine.utils.Msg') .onInfo("请至少选择一行数据");
                                }
                            }
                        }), Ext.create('Ushine.buttons.IconButton', {
					    	   id: 'zhuanClueBtn',
					    	   btnText: '转线索库',
					    	   handler: function () {
					    		   var grid = self.getComponent(1);
					    		   if(grid.getSelectionModel().hasSelection()){
					    			   var res=grid.getSelectionModel().getSelection();
					    			   var orgIds=[];
					    			   for(var i=0;i<res.length;i++){
					    				   orgIds.push(res[i].get('id'));
					    			   }
					    			   //媒体网站转线索库函数
					    			   self.mediaTurnClueStore(orgIds);
					    		   }else{
					    			   Ext.create('Ushine.utils.Msg').onInfo("对不起，请选择至少一行记录。");
					    		   }
					    	   		
					    	   }
					    }), /*Ext.create('Ushine.buttons.IconButton',{
					       		id:'updateStatusBtn',
					       		btnText:'启用刊物',
					       		handler:function(){
					       			//启用
					       			self.startOrCeaseStore('启用');
					       		}
					    }), Ext.create('Ushine.buttons.IconButton',{
					       		id:'updateCeaseBtn',
					       		btnText:'禁用刊物',
					       		handler:function(){
					       			//刊物
					       			self.startOrCeaseStore('禁用');
					       		}
					    })*/]
                }, {
                    layout: "column", // 行1
                    height: 25,
                    baseCls: 'panel-body',
                    style: 'margin-top:-10px;',
                    items: [{
                        id: 'field',
                        fieldLabel: '字段筛选',
                        labelAlign: 'right',
                        labelWidth: 60,
                        xtype: 'combo',
                        allowNegative: false,
                        allowBlank: false,
                        editable: false,
                        hiddenName: 'colnum',
                        emptyText: '请选择字段',
                        valueField: 'value',
                        store: Ext.create('Ext.data.Store', {
                            fields: ['text', 'value'],
                            data: [
                                // 字段筛选
                                {"text":"任意字段", "value":"anyField"},
                                {
                                    "text": "刊物类别",
                                    "value": "infoType"
                                }, {
                                    "text": "刊物名称",
                                    "value": "name"
                                }, {
                                    "text": "域名",
                                    "value": "websiteURL"
                                }, {
                                    "text": "服务器所在地",
                                    "value": "serverAddress"
                                }, {
                                    "text": "创办地",
                                    "value": "establishAddress"
                                }, {
                                    "text": "主要发行地",
                                    "value": "mainWholesaleAddress"
                                }, {
                                    "text": "创办人",
                                    "value": "establishPerson"
                                }, {
                                    "text": "创办时间",
                                    "value": "establishTime"
                                }, {
                                    "text": "基本情况",
                                    "value": "basicCondition"
                                }]
                        }),
                        value: 'anyField',
                        width: 260
                    }, {
                        fieldLabel: '关&nbsp;键&nbsp;&nbsp;字',
                        id: 'fieldValue',
                        xtype: 'textfield',
                        name: 'filtrateKeyword',
                        emptyText: '请输入相应字段的关键字...',
                        height: 24,
                        labelAlign: 'right',
                        labelWidth: 100,
                        width: 300,
                        listeners:{
							 specialkey:function(field,e){
		                          if(e.getKey() == e.ENTER){
		                           	//按下enter键
		                        	self.findMediaNetworkBook();
		                          }  
		                      }
						}
                    }]
                }, {
                    layout: "column", // 行2
                    height: 45,
                    baseCls: 'form-body',
                    items: [{
                        labelAlign: 'right',
                        fieldLabel: '开始时间:',
                        id: 'startTime',
                        labelWidth: 60,

                        format: 'Y-m-d',
                        // editable:false,
                        xtype: 'datefield',
                        maxValue: new Date(),
                        height: 22,
                        width: 260,
                        value: startTime
                    }, {
                        labelAlign: 'right',
                        fieldLabel: '结束时间:',
                        labelWidth: 100,
                        format: 'Y-m-d',
                        xtype: 'datefield',
                        // editable:false,
                        height: 22,
                        width: 300,
                        id: 'endTime',
                        maxValue: new Date(),
                        value: new Date()
                    }, Ext.create('Ushine.buttons.Button', {
                        text: '查询文档',
                        style: "margin-left:20px;",
                        width: 100,
                        labelWidth: 60,
                        height: 22,
                        id: 'search-Button',
                        baseCls: 't-btn-red',
                        handler: function () {
                            // 查询媒体网站刊物
                            self.findMediaNetworkBook();
                        }
                    }), Ext.create('Ushine.buttons.Button', {
                        text: '条件重置',
                        width: 100,
                        style: "margin-left:10px;",
                        id: 'reset-Button',
                        baseCls: 't-btn-yellow',
                        height: 22,
                        handler: function () {
                            Ext.getCmp("labl").getForm()
                                .reset();
                        }
                    })]
                }]
            }
        },
            // gridpanel
            Ext.create('Ushine.docInfo.MediaNetworkGridPanel')];
        this.callParent();
    },
    startOrCeaseStore:function(type){
    	var self=this;
    	var websiteStoreGrid=self.getComponent(1);					    	   	
   	    if(websiteStoreGrid.getSelectionModel().hasSelection()){
   	   		//允许多行
   	   		var record=websiteStoreGrid.getSelectionModel().getSelection();
   	   		var ids=[];
   	   		for(var i=0;i<record.length;i++){
   	   			if(type=='启用'){
   	   				if(record[i].get('isEnable')=='1'){
   	   					//添加已经禁用的id
	   	   				ids.push(record[i].get('id'));
   	   				}
   	   			}
   	   			if(type=='禁用'){
   	   				if(record[i].get('isEnable')=='2'){
   	   					//添加已经启用的id
	   	   				ids.push(record[i].get('id'));
   	   				}
   	   			}
   	   		}
   	   		//console.log(ids);
   	   		//后台
   	   		if(ids.length>0){
   	   			Ext.Ajax.request({
	   	   			url:'startOrCeaseWebsiteJournalStore.do',
	   	   			params:{
	   	   				ids:ids,
	   	   				type:type
	   	   			},
	   	   			method:'post',
	   	   			success:function(response){
	   	   				var obj=Ext.JSON.decode(response.responseText);
						Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
						//刷新前台数据
						Ext.getCmp('medianetworkbookgrid').getStore().reload();
						////取消选择
						Ext.getCmp('medianetworkbookgrid').getSelectionModel().clearSelections();
	   	   			},
	   	   			failure:function(){
	   	   				Ext.create('Ushine.utils.Msg').onInfo("请求后台失败!!!");
	   	   			}
   	   			})
   	   			
   	   		}else{
   	   			Ext.create('Ushine.utils.Msg').onInfo("选中的媒体刊物已经启用");
   	   		}
   	    }else{
   	   		Ext.create('Ushine.utils.Msg').onInfo("请至少选择一行数据");
   	    }
    },
	//转线索库
	mediaTurnClueStore:function(orgIds){
		//定义一个SaveCluesWin的Window
		Ext.define('SelectClueDataWin',{
			extend:'Ushine.win.Window',
			title : "选择线索数据",
			modal : true,
			id:'SelectClueDataWin',
			layout : {
				type:'vbox',
				align:'stretch'
			},
			border : false,
			closable : true,
			draggable:true,
			resizable : false,
			plain : true,
			padding:10,
			height:550,
			buttonAlign:"center",
			width:600,
			constructor:function(){
				var self=this;
				this.items=[{
			    	xtype:'panel',
					region:'center',
					border:false,
					items:[{
				    	layout: "column", //行1
				    	height: 25,
						baseCls: 'panel-body',
						items:[{
						id:'field1',
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
	            		    data :[
	            		        {'text':'任意字段',value:'anyField'}
	            		    ]
	            		}),
	            		value:'anyField',
						width: 260
					},{
						fieldLabel: '关&nbsp;键&nbsp;&nbsp;字',
						id		  :'fieldValue1',
                    	xtype     : 'textfield',
                    	name      : 'filtrateKeyword',
                    	emptyText : '请输入相应字段的关键字...',
						height: 24,
						labelAlign : 'right',
						labelWidth: 100,
						width: 300,
						listeners:{
							//按enter
							specialkey:function(field,e){
								if(e.getKey()==e.ENTER){
									self.findDataByProperty();
								}
							}
						}
					}]
			},{
				layout: "column", //行2
				height: 25,
				border:false,
				margin:'10 0 0 0',
				items:[{
					labelAlign : 'right',
					fieldLabel: '开始时间:',
					id:'startTime1',
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
					id:'endTime1',
					maxValue: new Date(),
					value:new Date()
				}]
			},{
				layout: "column", //行2
				height: 38,
				border:true,
				baseCls: 'form-body',
				items:[
				Ext.create('Ushine.buttons.Button', {
		        	text : '查询',
		        	style:"margin-left:10px;",
		        	width:100,
		        	labelWidth: 60,
		        	height:22,
		        	id : 'search-Button1',
		        	baseCls: 't-btn-red',
		        	handler:function(){
		        		//查询
                        self.findDataByProperty();
	                }
		        }),Ext.create('Ushine.buttons.Button', {
		        	text : '条件重置',
		        	width:100,
		        	style:"margin-left:10px;",
		        	id : 'reset-Button1',
		        	baseCls: 't-btn-yellow',
		        	height:22,
		        	handler:function(){
		        		Ext.getCmp("labl").getForm().reset();
		        	}
		        })]
			},
				new Ushine.utils.SelectClueDataGridPanel()]
			}];
				this.buttons=[
					Ext.create('Ushine.buttons.Button', {
					 		text: '确定',
					 		baseCls: 't-btn-red',
					 		handler: function () {
					 			var records = Ext.getCmp('SelectclueDataGridPanelId').getSelectionModel().getSelection();
					 			var clueIds = [];
					 			//提取已选择数据的id
					 			for(var i = 0; i < records.length; i++){  
					 				clueIds.push(records[i].get("id"));
					 			}
					 			var self=Ext.getCmp('SelectclueDataGridPanelId');
					 			var loadMask=new Ext.LoadMask(self,{
									msg:'正在转线索库,请耐心等待……'
								});
								loadMask.show();
						 		Ext.Ajax.request({
								    url: 'basisTurnClueStore.do',
								    actionMethods: {
							            create : 'POST',
							            read   : 'POST', // by default GET
							            update : 'POST',
							            destroy: 'POST'
							        },
								    params: {
								    	dataId:orgIds,
								    	clueIds:clueIds,
								    	store:'websiteJournalStore'
								    },
								    //转线索库成功后
								    success: function(response){
								    	loadMask.hide();
								    	 var text = response.responseText;
									     var obj=Ext.JSON.decode(text);
									     if(obj.status){
									    	 Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
									     }
									     Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
									     //取消选择
									     Ext.getCmp('medianetworkbookgrid').getStore().reload();
										 Ext.getCmp('medianetworkbookgrid').getSelectionModel().clearSelections();
								 		 Ext.getCmp('SelectClueDataWin').close();
								    },
							        failure: function(form, action) {
							        	loadMask.hide();
								 		 Ext.getCmp('SelectClueDataWin').close();
							        	Ext.create('Ushine.utils.Msg').onInfo('转线索库失败，请联系管理员');
							        }
								});
					 		}
					 	})
				];
				this.callParent();
			},
			//查询数据
			findDataByProperty:function(){
				//通过这种方式重新加载数据
				var win=this.getComponent(0);
				win.remove(Ext.getCmp('SelectclueDataGridPanelId'))
				win.add(new Ushine.utils.SelectClueDataGridPanel());
			}
		});
		Ext.create('SelectClueDataWin').show();
	},
    // 新增媒体网站刊物
    saveMediaNetworkBook: function () {
        Ext.create('SaveMediaNetworkBookWin',{
        	isClue:'isNotClue',
        	clueNum:-999
        }).show();
    },
    // 查询
    findMediaNetworkBook: function () {
        this.remove('d-medianetworkbook-grid');
        this.add(Ext.create('Ushine.docInfo.MediaNetworkGridPanel'));
    },
  //字符串替换
	replaceString:function(string){
		var value=string;
		//去除所有的html标签
		/*if(string.indexOf("<font color='orange'/>")>-1&&string.indexOf("</font>")>-1){
			value=string.replace(/<[^>]+>/g,"");
		}*/
		value=string.replace(/<[^>]+>/g,"");
		return value;
	},
    // 更新重置
    resetValue: function (form, record) {
    	var self=this;
        form.findField('id').setValue(record[0].get('id'));
        form.findField('name').setValue(self.replaceString(record[0].get('name')));
        form.findField('websiteURL').setValue(self.replaceString(record[0].get('websiteURL')));
        form.findField('serverAddress').setValue(self.replaceString(record[0].get('serverAddress')));
        form.findField('establishAddress').setValue(self.replaceString(record[0].get('establishAddress')));
        form.findField('mainWholesaleAddress').setValue(self.replaceString(record[0].get('mainWholesaleAddress')));
        form.findField('establishPerson').setValue(self.replaceString(record[0].get('establishPerson')));
        form.findField('establishTime').setValue(self.replaceString(record[0].get('establishTime')));
        form.findField('basicCondition').setValue(self.replaceString(record[0].get('basicCondition')));
        form.findField('infoType').setValue(self.replaceString(record[0].get('infoType')));
    },
    // 修改
    updateMediaNetWorkBook: function (record) {
        var win = Ext.create('UpdateMediaNetworkBookWin').show();
        var form = win.getComponent(0).getForm();
        // 赋值
        var self = this;
        self.resetValue(form, record);
        Ext.getCmp('resetMediaNetworkBook').addListener('click', function () {
            self.resetValue(form, record);
        })
    },
    // 删除
    delMediaNetWorkBook: function (ids) {
    	var self=this;
    	var loadMask=new Ext.LoadMask(self,{
			msg:'正在删除媒体刊物,请耐心等待……'
		});
		loadMask.show();
        Ext.Ajax.request({
            url: 'delWebsiteJournalStore.do',
            params: {
                ids: ids
            },
            success: function (response) {
            	loadMask.hide();
                var obj = Ext.JSON.decode(response.responseText);
                Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
                // 刷新数据
                Ext.getCmp("medianetworkbookgrid").getStore().reload();
            },
            failure: function () {
            	loadMask.hide();
                Ext.create('Ushine.utils.Msg').onInfo("提示,请求后台服务失败");
            }
        })
    }

});

// 定义一个SaveMediaNetworkBookForm表单
// 把原来写在内部提到外部来
Ext.define('SaveMediaNetworkBookForm', {
    extend: 'Ext.form.Panel',
   	//id: 'savemedianetworkbook-formpanel',
    layout: 'vbox',
    bodyPadding: 5,
    margin: 0,
    border: false,
    baseCls: 'form-body',
    buttonAlign: "center",
    constructor: function () {
        var self = this;
        this.items = [{
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
                name: 'name'
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
                labelStyle: 'color:black;',
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
        }];
        this.buttons = [Ext.create('Ushine.buttons.Button', {
            text: '确定',
            baseCls: 't-btn-red',
            handler: function () {
                // 提交给后台处理
                // 新增
                var saveForm = self.getForm();
                var win=self.up('window');
                var isClue=win.isClue;
                var state=win.state;
                var clueNum=win.clueNum;
                //console.log(isClue);
                //console.log(clueNum);
                if (saveForm.isValid()) {
                	//先判断是否存在
	   				Ext.Ajax.request({
	   					//先判断是否有了
	   					url:"hasWebsiteStoreByName.do",
	   					method:'post',
	   					params:{
	   						name:saveForm.findField('name').getValue()
	   					},
	   					success:function(response){
	   						var obj=Ext.JSON.decode(response.responseText);
	   						//console.log(obj);
	   						if(obj.msg=='exist'){
	   							Ext.create('Ushine.utils.Msg').onQuest("媒体刊物已经存在，是否仍添加",function(btn){
	   								//console.log(btn);
	   								if(btn=='yes'){
	   									//仍添加
	   									self.saveWebsiteStore(self,saveForm,isClue,clueNum,state);
	   								}
	   							});
	   						}else{
	   							//不存在
	   							self.saveWebsiteStore(self,saveForm,isClue,clueNum,state);
	   						}
	   					},
	   					failure:function(){
	   						Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败");
	   					}
	   				})
                } else {
                    Ext.create('Ushine.utils.Msg').onInfo("请填写完整信息");
                }
            }
        }), Ext.create('Ushine.buttons.Button', {
            text: '重置',
            margin: '0 0 0 35',
            baseCls: 't-btn-yellow',
            handler: function () {
                self.getForm().reset();
            }
        })];
        this.callParent();
    },
    //保存
    saveWebsiteStore:function(self,saveForm,isClue,clueNum,state){
    	// 添加遮罩
        var loadMask = new Ext.LoadMask(self, {
            msg: '正在添加媒体网站刊物,请耐心等待……'
        });
        loadMask.show();
        saveForm.submit({
            url: "saveWebsiteJournalStore.do",
            method: 'post',
            timeout: 60000,
            //不提交空白
            submitEmptyText:false,
            params:{
            	isClue:isClue,
            	clueNum:clueNum,
            	state:state
            },
            success: function (form, action) {
                var obj = Ext.JSON.decode(action.response.responseText);
                Ext.create('Ushine.utils.Msg').onInfo(obj.msg);
                // 关闭
                self.findParentByType('window').close();
                if(Ext.getCmp("medianetworkbookgrid")){
                	// 刷新数据
                	Ext.getCmp("medianetworkbookgrid").getStore().reload();
                }
                 var CluesTempDataGrid = Ext.getCmp('CluesTempDataGridId');
                 if(CluesTempDataGrid){
                 	 //新增线索文档后刷新数据
			    	 CluesTempDataGrid.removeAll();
			    	 CluesTempDataGrid.add(new Ushine.utils.CluesTempDataGridPanel(clueNum));
                 }
            },
            failure: function (form, action) {
                Ext.create('Ushine.utils.Msg').onInfo("请求后台服务失败");
            }
        });
    }
});

// 定义一个SaveMediaNetworkBookWin
Ext.define('SaveMediaNetworkBookWin', {
    extend: 'Ushine.win.Window',
    title: "添加媒体网站刊物",
    modal: true,
    layout: 'fit',
    border: false,
    closable: true,
    draggable: true,
    resizable: false,
    plain: true,
    borer: false,
    height: 480,
    width: 575,
    constructor: function (config) {
        var self = this;
        //是否是线索文档
        this.isClue=config.isClue;
        //是线索的编号
        this.clueNum=config.clueNum;
        //关联媒体状态没关联为0
        this.state=config.state;
        this.items = [Ext.create('SaveMediaNetworkBookForm')];
        this.callParent();
    }
})

// 定义一个UpdateMediaNetworkBookForm表单
Ext.define('UpdateMediaNetworkBookForm', {
    extend: 'Ext.form.Panel',
    //id: 'updatemedianetworkbook-formpanel',
    layout: 'vbox',
    bodyPadding: 5,
    margin: 0,
    border: false,
    baseCls: 'form-body',
    buttonAlign: "center",
    constructor: function () {
        var self = this;
        this.items = [{
            layout: 'hbox',
            bodyPadding: 5,
            margin: 0,
            border: false,
            buttonAlign: "center",
            items: [{
                // 添加一个隐藏的域
                xtype: 'hiddenfield',
                name: 'id'
            }, {
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
                name: 'name'
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
                allowNegative: false,
                editable: false,
                // 传给controller是text
                displayField: 'text',
                valueField: 'text',
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
                height: 22
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
                labelStyle: 'color:black;',
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
                /*fieldLabel: '基本情况',
                xtype: 'htmleditor',
                labelStyle: 'color:black;',
                width: 534,
                labelAlign: 'right',
                emptyText: '请输入基本情况',
                labelWidth: 60,
                height: 220,
                enableFont:false,
                name: 'basicCondition'*/
            	xtype : 'displayfield',
				fieldLabel:'内容',
				labelWidth :60,
				labelAlign : 'right',
				value:"<a href='javascript:void(0)' onclick=editMediaNetworkBasicCondition()>"
					+"点击修改媒体基本情况<a/>"
            },{
            	//隐藏
				xtype : 'hidden',
				name : 'basicCondition'
            }]
        }];
        this.buttons = [Ext.create('Ushine.buttons.Button', {
            text: '确定',
            baseCls: 't-btn-red',
            handler: function () {
                // 提交给后台处理
                // 修改
                var updateForm = self.getForm();
                if (updateForm.isValid()) {
                   // 添加遮罩
                    var loadMask = new Ext.LoadMask(self, {
                        msg: '正在修改媒体网站刊物,请耐心等待……'
                    });
                    loadMask.show();
                    updateForm.submit({
                        url: "updateWebsiteJournalStore.do",
                        method: 'post',
                        timeout: 60000,
                        submitEmptyText:false,
                        success: function (form, action) {
                            var obj = Ext.JSON
                                .decode(action.response.responseText);
                            Ext.create('Ushine.utils.Msg')
                                .onInfo(obj.msg);
                            // 关闭
                            self.findParentByType('window').close();
                            // 刷新数据
                            Ext.getCmp("medianetworkbookgrid")
                                .getStore().reload();
                            // 清除选择
                            Ext.getCmp("medianetworkbookgrid")
                                .getSelectionModel()
                                .clearSelections();
                        },
                        failure: function (form, action) {
                            Ext.create('Ushine.utils.Msg')
                                .onInfo("请求后台服务失败");
                        }
                    });
                } else {
                    Ext.create('Ushine.utils.Msg').onInfo("请填写完整信息");
                }
            }
        }), Ext.create('Ushine.buttons.Button', {
            text: '重置',
            margin: '0 0 0 35',
            baseCls: 't-btn-yellow',
            id: 'resetMediaNetworkBook'
        })];
        this.callParent();
    }
});

//定义一个UpdateMediaNetworkBookWin
Ext.define('UpdateMediaNetworkBookWin', {
    extend: 'Ushine.win.Window',
    title: "修改媒体网站刊物",
    modal: true,
    layout: 'fit',
    border: false,
    closable: true,
    draggable: true,
    resizable: false,
    id:'updatemedianetworkbook-win',
    plain: true,
    borer: false,
    height:280,
	width:600,
    constructor: function () {
        var self = this;
        this.items = [Ext.create('UpdateMediaNetworkBookForm')];
        this.callParent();
    }
});

//修改基本情况
function editMediaNetworkBasicCondition(){
	var win=Ext.getCmp('updatemedianetworkbook-win');
	var form=win.getComponent(0).getForm();
	//得到表单
	var field=form.findField('basicCondition');
	var value=form.findField('basicCondition').getValue();
	Ext.define('EditLeaderTalkContentWin',{
		height:800,
		width:1000,
		extend:'Ext.window.Window',
		layout : 'fit',
		border : false,
		maximizable:true,
		buttonAlign:"center",
		title:'修改文档基本情况',
		constructor:function(){
			var self=this;
			this.items=[{
				xtype:'htmleditor',
				value:value,
				enableFont:false
			}];
			this.buttons=[
				Ext.create('Ushine.buttons.Button', {
	      			text: '确定',
	      	   		baseCls: 't-btn-red',
	      	   		handler:function(){
	      	   			//修改
	      	   			field.setValue(self.getComponent(0).getValue());
	      	   			//关闭
	      	   			self.close();
	      	   		}
	      	   	}),Ext.create('Ushine.buttons.Button', {
	      			text: '重置',
	      			margin:'0 0 0 35',
	   				baseCls: 't-btn-yellow',
	   				handler:function(){
   						//重置
   						self.getComponent(0).setValue(value);
	   				}
	   			})
			];
			this.callParent();
		}
	});
	Ext.create('EditLeaderTalkContentWin').show();
}