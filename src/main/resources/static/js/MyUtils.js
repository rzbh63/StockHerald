/**
 * @title 格式化日期（返回为字符串，失败返回空）
 * @param {String} dateStr	日期字符串 或 日期对象
 * @param {String} format	日期的格式(YYYY年,MM月,DD日,格式可以为YYYYMMDD,MM/DD/YYYY等。不传默认为YYYY-MM-DD)
 */
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

/**
 * @title 检查日期格式是否有效
 * @param {String} dateStr	日期字符串
 * @param {String} format	日期的格式(YYYY年,MM月,DD日,格式可以为YYYYMMDD,MM/DD/YYYY等。不传默认为YYYY-MM-DD)
 */
function f_isValidDate(dateStr,format)
{
	if (format == undefined || format == null)
	{
		format = "YYYY-MM-DD";
	}
	var yearPos = format.indexOf("YYYY");
	var monthPos = format.indexOf("MM");
	var dayPos = format.indexOf("DD");
	
	if(dateStr.length != format.length){
		return false;
	}
	
	var year = dateStr.substring(yearPos,yearPos + 4);
	var month = dateStr.substring(monthPos,monthPos + 2);
	var day = dateStr.substring(dayPos,dayPos + 2); 
	
	if(isNaN(year) || isNaN(month) || isNaN(day)){
		return false;
	}
	
	var dayNumOfMonth = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
	
	if(((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)){
		dayNumOfMonth[1]=29;
	}
	
	if(month < 1 || month > 12 || day >dayNumOfMonth[month -1] || day < 1 ){
		return false;
	}
	else{
		return true;
	}
}