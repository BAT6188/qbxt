/**
 * 信息类型管理数据面板
 * @author 王百林
 */
Ext.define('Ushine.infoTypeManage.InfoTypeSetGridPanel', {
	extend: 'Ext.grid.Panel',
	//enableColumnResize:是否允许改变列宽，默认为true
	id:"infoTypeSetGridPanelId",
	flex: 1,
	height:200,
	hideHeaders:false,  //隐藏列头部
	stripeRows:false,		    //True来实现隔行换颜色
	autoHeight : true,
	rowLines:true,   //是否显示行分隔线
	disableSelection: false,   //是否禁止行选择，默认false
	columnLines:true,		   //添加列的框线样式
    loadMask: true,           //是否在加载数据时显示遮罩效果，默认为false
	selType: 'checkboxmodel', // 复选框
	defaults: {sortable: false},
	columns:[],
	viewConfig:{
		emptyText: '没有数据',
    	stripeRows:true,//在表格中显示斑马线
    	enableTextSelection:true //可以复制单元格文字
	},
	constructor: function(tableTypeName) {
	var self = this;
	var store = Ext.create('Ext.data.JsonStore',{
		model:'InfoTypeModel',
		remoteSort:true,
		proxy:{
			type:'ajax',
			url:'findInfoTypeByTypeName.do?tableTypeName='+tableTypeName,
			simpleSortMode:true,
			reader:{
                type: 'json',
                root: 'datas'
			}
		},
		autoLoad:true
	});
		
	this.columns=[
	    {text: 'id',  dataIndex: 'id',width:80,flex: 1,menuDisabled:true,hidden:true},
	    {text: '类别名称',  dataIndex: 'typeName',width:80,flex: 1,menuDisabled:true},
	    {text: '创建时间',  dataIndex: 'saveTime',width:160,menuDisabled:true,renderer:function(value){
      	   return Ext.Date.format(value, 'Y-m-d H:i');
	    }}];
		this.loadMask =true;
		
		this.store = store;
		this.callParent(); 
	},
	listeners : { 
		itemdblclick : function(obj,record,index, eOpts) {  //双击事件
			var self = this;
			self.logWithRecord(record.data);
		} 
	},
	logWithRecord:function(data){
		var self = this;
		var imageData = new Array();
		if(data.hwImagePath!=""){
			Ext.each(data.hwImagePath.split(","), function(item){
				var temp = [];
				temp.push(item);
				imageData.push(temp);
			});
		}
		var win = Ext.create('Ushine.win.Window',{
			title : '档案信息',
			border : false,
			closable : true,
			draggable:true,
			resizable : false,
			modal:true,
			width : 596,
			height : 530,
			plain : true,
		    layout: {
		        type: 'vbox',
		        padding:'0 10 0 10',
		        align: 'stretch'
		    },
		    border:false,
		    items:[{
		    	title:'基本信息',
		    	xtype:'fieldset',
		    	height:330,
		    	layout:{
		    		type:'vbox',
		    		align:'stretch'
		    	},
		    	items:[{
		    		flex: 1,
			    	border:false,
			    	bodyStyle: "background: transparent;font-family: '新宋体', Arial;font-size: 12px;line-height:24px;color: #333;",
				    layout: {
				        type: 'hbox',
				        align: 'stretch'
				    },
				    items:[
				        {
				        	flex: 1,
				        	xtype:'label',
				        	text:'案件编号：'+data.caseNo
				        },{
				        	flex: 1,
				        	xtype:'label',
				        	text:'笔迹所有人姓名：'+data.hwName
				        }]
		    	},{
		    		flex: 1,
			    	layout:'hbox',
			    	border:false,
			    	bodyStyle: "background: transparent;font-family: '新宋体', Arial;font-size: 12px;color: #333;",
			    	items:[{
			    		flex: 1,
			    		xtype:'label',
			    		text:'笔迹人身份证：'+data.hwIdCard
			    	},{
			    		flex: 1,
			    		xtype:'label',
			    		text:'笔迹所有人手机：'+data.hwMobile
			    	}]
		    	},{
		    		flex: 1,
			    	layout:'hbox',
			    	border:false,
			    	bodyStyle: "background: transparent;font-family: '新宋体', Arial;font-size: 12px;color: #333;",
			    	items:[{
			    		flex: 1,
			    		xtype:'label',
			        	text:'笔迹来源：'+data.hwSource
			    	},{
			    		flex: 1,
			    		xtype:'label',
			        	text:'图片格式：'+data.imageFormate
			    	}]
		    	},{
		    		flex: 1,
			    	layout:'hbox',
			    	border:false,
			    	bodyStyle: "background: transparent;font-family: '新宋体', Arial;font-size: 12px;color: #333;",
			    	items:[{
			    		flex: 1,
			    		xtype:'label',
			        	text:'笔迹特征：'+data.hwType
			    	},{
			    		flex: 1,
			    		xtype:'label',
			        	text:'采集人：'+data.collectName
			    	}]
		    	},{
		    		flex: 1,
			    	layout:'hbox',
			    	border:false,
			    	bodyStyle: "background: transparent;font-family: '新宋体', Arial;font-size: 12px;color: #333;",
			    	items:[{
			    		flex: 1,
			    		xtype:'label',
			        	text:'采集单位编码：'+data.collectUnit
			    	},{
			    		flex: 1,
			    		xtype:'label',
			        	text:'增量数据操作类型：'+data.action
			    	}
			    	]
		    	},{
		    		flex: 1,
			    	layout:'hbox',
			    	border:false,
			    	bodyStyle: "background: transparent;font-family: '新宋体', Arial;font-size: 12px;color: #333;",
			    	items:[{
			    		flex: 1,
			    		xtype:'label',
			        	text:'首次采集时间：'+Ext.Date.format(data.collectFirstTime, 'Y-m-d H:i:s')
			    	},{
			    		flex: 1,
			    		xtype:'label',
			        	text:'最近更新时间：'+Ext.Date.format(data.collectUpdateTime, 'Y-m-d H:i:s')
			    	}]
		    	},{
		    		flex: 1,
			    	layout:'hbox',
			    	border:false,
			    	bodyStyle: "background: transparent;font-family: '新宋体', Arial;font-size: 12px;color: #333;",
			    	items:[{
			    		flex: 1,
			    		xtype:'label',
			    		text:'书写日期：'+Ext.Date.format(data.hwDate, 'Y-m-d H:i:s')
			    	},{
			    		flex: 1,
			    		xtype:'label',
			    		text:'采集日期：'+Ext.Date.format(data.collectDate, 'Y-m-d H:i:s')
			    	}]
		    	},{
		    		flex: 1,
			    	layout:'hbox',
			    	border:false,
			    	bodyStyle: "background: transparent;font-family: '新宋体', Arial;font-size: 12px;color: #333;",
			    	items:[{
			    		flex: 1,
			    		xtype:'label',
			    		text:'备注：'
			    	}]
		    	},{
			    	xtype: 'fieldset',
					flex: 4,
				    layout: {
				        type: 'vbox',
				        align: 'stretch'
				    },
				    items:[{
				    	flex:4,
				    	layout:'hbox',
				    	border:false,
				    	margins: '8 0 0 0',
				    	bodyStyle: "background: transparent;font-family: '新宋体', Arial;font-size: 12px;color: #333;",
				    	items:[{
				    		flex: 1,
				    		xtype:'label',
				    		border:false,
				    		text:data.remark
				    	}]
				    }]
			    }
		    	]
		    },{
		    	title:'档案图片',
		    	xtype:'fieldset',
		    	height:140,
		    	layout:'fit',
		    	items:{
		    		xtype: 'dataview',
		    		singleSelect: true,
		    		height:155,
		    		autoScroll:true,
		    		margins:'0 0 5 0',
		    		style:"border:1px solid #A9A9A9",
		    		//overClass:'x-view-selected',
		    		tpl:new Ext.XTemplate(
		    				'<tpl for=".">',
		    				'<div class="img_item">',
		    			        '<div style="border:solid 1px ccc"><img width="120px" height="100px" src="{path}"/></div>',
		    		        '</div>',
		    		    '</tpl>'),
		    	    itemSelector:'div.img_item',
		    	    store:Ext.create('Ext.data.ArrayStore', {
		    	    	data:imageData,
		    	    	fields:['path']
		    	    }),
		    	    listeners: {
		    	    	itemdblclick: function( thiz, record, eOpts ){
		    	    		self.showImg(record.data.path);
		    	    	}
		    	    }}
		    	}]
			});
			win.show();
		},
		showImg:function(src){
			//console.log(src);
			var win = Ext.create('Ushine.win.Window',{
				modal:true,
				width : 596,
				height : 530,
				layout:'fit',
				items:{
					xtype:'image',
					src:src
				}
			})
			win.show();
		}
});

// 数据模型
Ext.define('InfoTypeModel', {
	extend: 'Ext.data.Model',
    fields: [
{name: 'typeName', type:'string', mapping: 'typeName'},
{name: 'saveTime', type:'date', mapping: 'saveTime'},
],
    idProperty:'id'
});
