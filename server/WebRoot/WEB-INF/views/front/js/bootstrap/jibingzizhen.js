function add() {
	location.href = "/qyw/front/jibingzizhenWeb_edit";
}
function openbodycat(id) {
	location.href = "/qyw/front/jibingzizhenWeb_open?bodyCat.id="+id;
}
function addp() {
	location.href = "/qyw/front/jibingzizhenWeb_openPerformance";
}
function edit(id) {
	location.href = "/qyw/front/jibingzizhenWeb_edit?coolDiseaseDetial.id="
			+ id;
}
function editp(id, name) {
	if (id == null) {
		$("#performanceid").val("");
		$("#performancecatname").val("");
	} else {
		$("#performanceid").val(id);
		$("#performancecatname").val(name);
	}
}
function savep() {
	var name = $("#performancecatname").val()
	if (name == null || name == "") {
		alert("关键字名称不能为空");
		return;
	}
	var myOption = {
		url : "/qyw/front/jibingzizhenWeb_savePerEdit",
		type : "post",
		success : function(data) {
			var checkdata = data.checkdata;
			if (checkdata == -1) {
				alert("关键字名称已经存在");
				return;
			}
			alert("保存成功");
			location.href = "/qyw/front/jibingzizhenWeb_openPerformance";
		}
	};
	$("#formper").ajaxSubmit(myOption);
}
// 绑定药品搜索事件
$(document).ready(function() {
	$('#yaopinsearch').bind('keypress', function(event) {
		if (event.keyCode == "13") {
			search("yaopinsearch", "allyaopinlist");
			return false;
		}
	});
	$('#performancesearch').bind('keypress', function(event) {
		if (event.keyCode == "13") {
			search("performancesearch", "allperformancecatlist");
			return false;
		}
	});
});
function search(searchid, listid) {
	var searchvalue = $('#' + searchid).val();
	var showall = 0;
	if (searchvalue == null || searchvalue == "") {
		showall = 1;
	}
	var optionObjs = $("#" + listid + " option");
	$.each(optionObjs, function(i, item) {
		if (showall == 1) {
			$(item).removeClass("nonedisplay");
		} else {
			var value = $(item).text();
			if (value.indexOf(searchvalue) > -1) {
				$(item).removeClass("nonedisplay");
			} else {
				$(item).addClass("nonedisplay");
			}
		}
	});
}
function addOption(alllist, selectlist) {
	var checkText = $("#" + alllist).find("option:selected").text(); // 获取Select选择的Text
	var checkValue = $("#" + alllist).val(); // 获取Select选择的Value
	var find = 0;
	var optionObjs = $("#" + selectlist + " option");
	$.each(optionObjs, function(i, item) {
		var value = $(item).val();
		if (value == checkValue) {
			find = 1;
		}
	});
	if (find == 0) {
		var option = '<option value="' + checkValue + '">' + checkText
				+ '</option>';
		$("#" + selectlist).append(option);
	}
}
function deleteOption(id) {
	var checkValue = $("#" + id).val(); // 获取Select选择的Value
	$("#" + id + " option[value='" + checkValue + "']").remove(); // 删除Select中索引值为checkIndex的Option

}
function saveDisease() {
	var name = $("#diseasename").val();
	if (name == null || name == "") {
		alert("疾病名称不能为空");
		return;
	}
	// 所属人群
	var personCats = "";
	$(".personcheckbox").each(function() {

		if ($(this).prop("checked")) {
			personCats += $(this).val();
			personCats += '|';
		}
	})
	if (personCats == "") {
		alert("请选择疾病所属人群");
		return;
	}
	$("#diseasepersons").val(personCats);

	var performance = "";
	var optionObjs = $("#selectperformancelist option");
	$.each(optionObjs, function(i, item) {
		var value = $(item).val();
		performance += value;
		performance += '|';
	});
	if (performance == "") {
		alert("请添加疾病关键字");
		return;
	}
	$("#diseaseperformance").val(performance);

	var diseaseposition = $("#diseaseposition").val();
	if (diseaseposition == null || diseaseposition == "") {
		alert("疾病详细所属部位不能为空");
		return;
	}
	var diseaseperformanceSummary = $("#diseaseperformanceSummary").val();
	if (diseaseperformanceSummary == null || diseaseperformanceSummary == "") {
		alert("疾病体征不能为空");
		return;
	}
	var diseasedescribeDetail = $("#diseasedescribeDetail").val();
	if (diseasedescribeDetail == null || diseasedescribeDetail == "") {
		alert("疾病概述不能为空");
		return;
	}
	var diseaseperformanceDetail = $("#diseaseperformanceDetail").val();
	if (diseaseperformanceDetail == null || diseaseperformanceDetail == "") {
		alert("疾病预防不能为空");
		return;
	}
	// 疾病相关药物
	var yaopinlist = "|";
	var optionObjs = $("#selectyaopinlist option");
	$.each(optionObjs, function(i, item) {
		var value = $(item).val();
		yaopinlist += value;
		yaopinlist += '|';
	});
	$("#diseaseyaopinList").val(yaopinlist);
	var myOption = {
		url : "/qyw/front/jibingzizhenWeb_save",
		type : "post",
		success : function(data) {
			alert("保存成功");
			var id = data.bodyCat.id;
			location.href = "/qyw/front/jibingzizhenWeb_open?bodyCat.id=" + id;
		}
	};
	$("#form").ajaxSubmit(myOption);
}