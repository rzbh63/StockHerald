function f_formatDate(date, format)
{
	format = format ? format : "YYYY-MM-DD";
	try{
		if (typeof(date) == "string"){
			if (/(\D+)/g.test(date)){
				date = new Date(date.replace(/\D+/g,"/"));
			} else {
				date = date.substr(0,4) + "/" + date.substr(4,2) + "/" + date.substr(6,2);
				date = new Date(date);
			}
			if (date == "Invalid Date"){
				return "";
			}
		}else if (typeof(date) == "object"){
			
		}else{
			return "";
		}
		
		var dateTag = {
			"M+": date.getMonth() + 1,
			"D+": date.getDate(),
			"h+": date.getHours(),
			"m+": date.getMinutes(),
			"s+": date.getSeconds(),
			"q+": Math.floor((date.getMonth() + 3) / 3),
			"S+": date.getMilliseconds()
		};
		if(/(y+)/i.test(format)) {
			format = format.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
		}
		for (var k in dateTag) {
			if (new RegExp("(" + k + ")").test(format)) {
				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? dateTag[k] : ("00" + dateTag[k]).substr(("" + dateTag[k]).length));
			}
		}
		return format;
	}catch(e){
		return "";
	}
}