/*
  Highcharts JS v7.1.0 (2019-04-01)

 Indicator series type for Highstock

 (c) 2010-2019 Wojciech Chmiel

 License: www.highcharts.com/license
*/
(function(b){"object"===typeof module&&module.exports?(b["default"]=b,module.exports=b):"function"===typeof define&&define.amd?define("highcharts/indicators/supertrend",["highcharts","highcharts/modules/stock"],function(l){b(l);b.Highcharts=l;return b}):b("undefined"!==typeof Highcharts?Highcharts:void 0)})(function(b){function l(b,A,l,v){b.hasOwnProperty(A)||(b[A]=v.apply(null,l))}b=b?b._modules:{};l(b,"indicators/supertrend.src.js",[b["parts/Globals.js"]],function(b){function l(e,d,b){return{index:d,
close:e.yData[d][b],x:e.xData[d]}}var C=b.seriesTypes.atr,v=b.seriesTypes.sma,D=b.isArray,z=b.merge,B=b.correctFloat;b.seriesType("supertrend","sma",{params:{multiplier:3,period:10},risingTrendColor:"#06B535",fallingTrendColor:"#F21313",changeTrendLine:{styles:{lineWidth:1,lineColor:"#333333",dashStyle:"LongDash"}}},{nameBase:"Supertrend",nameComponents:["multiplier","period"],requiredIndicators:["atr"],init:function(){var e;v.prototype.init.apply(this,arguments);e=this.options;e.cropThreshold=this.linkedParent.options.cropThreshold-
(e.params.period-1)},drawGraph:function(){for(var e=this,d=e.options,p=e.linkedParent,y=p?p.points:[],u=e.points,E=e.graph,q=u.length,r=y.length-q,r=0<r?r:0,x={options:{gapSize:d.gapSize}},m={top:[],bottom:[],intersect:[]},w={top:{styles:{lineWidth:d.lineWidth,lineColor:d.fallingTrendColor,dashStyle:d.dashStyle}},bottom:{styles:{lineWidth:d.lineWidth,lineColor:d.risingTrendColor,dashStyle:d.dashStyle}},intersect:d.changeTrendLine},f,n,c,g,h,t,k,a;q--;)f=u[q],n=u[q-1],c=y[q-1+r],g=y[q-2+r],h=y[q+r],
t=y[q+r+1],k=f.options.color,a={x:f.x,plotX:f.plotX,plotY:f.plotY,isNull:!1},!g&&c&&p.yData[c.index-1]&&(g=l(p,c.index-1,3)),!t&&h&&p.yData[h.index+1]&&(t=l(p,h.index+1,3)),!c&&g&&p.yData[g.index+1]?c=l(p,g.index+1,3):!c&&h&&p.yData[h.index-1]&&(c=l(p,h.index-1,3)),f&&c&&h&&g&&f.x!==c.x&&(f.x===h.x?(g=c,c=h):f.x===g.x?(c=g,g={close:p.yData[c.index-1][3],x:p.xData[c.index-1]}):t&&f.x===t.x&&(c=t,g=h)),n&&g&&c?(h={x:n.x,plotX:n.plotX,plotY:n.plotY,isNull:!1},f.y>=c.close&&n.y>=g.close?(f.color=k||d.fallingTrendColor,
m.top.push(a)):f.y<c.close&&n.y<g.close?(f.color=k||d.risingTrendColor,m.bottom.push(a)):(m.intersect.push(a),m.intersect.push(h),m.intersect.push(z(h,{isNull:!0})),f.y>=c.close&&n.y<g.close?(f.color=k||d.fallingTrendColor,n.color=k||d.risingTrendColor,m.top.push(a),m.top.push(z(h,{isNull:!0}))):f.y<c.close&&n.y>=g.close&&(f.color=k||d.risingTrendColor,n.color=k||d.fallingTrendColor,m.bottom.push(a),m.bottom.push(z(h,{isNull:!0}))))):c&&(f.y>=c.close?(f.color=k||d.fallingTrendColor,m.top.push(a)):
(f.color=k||d.risingTrendColor,m.bottom.push(a)));b.objectEach(m,function(c,a){e.points=c;e.options=z(w[a].styles,x);e.graph=e["graph"+a+"Line"];v.prototype.drawGraph.call(e);e["graph"+a+"Line"]=e.graph});e.points=u;e.options=d;e.graph=E},getValues:function(e,b){var d=b.period;b=b.multiplier;var l=e.xData,u=e.yData,v=[],q=[],r=[],x=0===d?0:d-1,m,w=[],f=[],n,c,g,h,t,k,a;if(l.length<=d||!D(u[0])||4!==u[0].length||0>d)return!1;e=C.prototype.getValues.call(this,e,{period:d}).yData;for(a=0;a<e.length;a++){k=
u[x+a];t=u[x+a-1]||[];c=w[a-1];g=f[a-1];h=r[a-1];0===a&&(c=g=h=0);d=B((k[1]+k[2])/2+b*e[a]);m=B((k[1]+k[2])/2-b*e[a]);w[a]=d<c||t[3]>c?d:c;f[a]=m>g||t[3]<g?m:g;if(h===c&&k[3]<w[a]||h===g&&k[3]<f[a])n=w[a];else if(h===c&&k[3]>w[a]||h===g&&k[3]>f[a])n=f[a];v.push([l[x+a],n]);q.push(l[x+a]);r.push(n)}return{values:v,xData:q,yData:r}}})});l(b,"masters/indicators/supertrend.src.js",[],function(){})});
//# sourceMappingURL=supertrend.js.map
