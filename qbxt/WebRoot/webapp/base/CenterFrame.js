/**
 * 内容区域
 * @author Franklin
 */
Ext.define('Ushine.base.CenterFrame', {
	extend: 'Ext.panel.Panel',
	id: 'center_frame',
	region: 'center',
	
	margins: '0 0 0 10',
	border: false,
	layout: 'border', // 边界布局
	
	constructor: function() {
		
		this.items = [{
				region: 'west',
				width: 70,
				border: false,
				baseCls: 'blue-body',
				
				items: [{
					xtype: 'panel',
					height: 80,
					baseCls: 'blue-body',
					html: '<div id="badge"></div>'
				}, Ext.create('Ushine.base.Menu')  
				]
			}, { 
				id: 'content_frame',
				xtype: 'panel',
				region: 'center',
				baseCls: 'panel-body',
				layout: 'fit',
				items:[
//				    Ext.create('Ushine.case.CaseTask')// 检索面板   
//					Ext.create('Ushine.case.Case')
				]
			}
		];
		
		this.callParent(); 
	}
	
});