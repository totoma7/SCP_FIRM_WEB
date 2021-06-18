
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//기능 일반 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

// CONSOLE 오류방지

var gCSRF_PARAM_NAME = "__CSRF_TOKEN__";

var _dynamicResizeTarget		= []; //gridName, gridDivId
var _dynamicResizeTargetWidth	= []; //gridName, gridDivId

var console = window.console || {
    log: function() {
    }
};

var resizeGrid = function() {
	//if (""!=_dynamicResizeTarget) {
	if (_dynamicResizeTarget != null && _dynamicResizeTarget.length > 0 ) {
		$.each(_dynamicResizeTarget, function(index, _gInfo){
			var _new_height = getGridHeight(_gInfo.gridDivId, _gInfo.gridPaging);

			//console.log( "_new_height = " + _new_height);
			//console.log( "_gInfo.gridDivId = " + _gInfo.gridDivId);
			document.getElementById(_gInfo.gridDivId).style.height = _new_height + "px";
			//eval(_gInfo.name+".resetSize();");
			eval(_gInfo.gridDivId+".resetSize();");

            $( ".main-content" ).find( ".rgrid-area" ).css( "height", "750" );
		});
	}
}

var getGridHeight = function(objId, isPaging) {

	var obj = $("#" + objId).closest(".rgrid-area");

	var retVal = obj.height();
	if( isPaging == true ) {
		retVal = retVal - 30;
	}else if(retVal >= 750){
        retVal = 680;
    }

	return retVal;

};

var getGridWidth = function(objId) {

	var obj = $("#" + objId).closest(".rgrid-area");
	var retVal = obj.width();

	return retVal;
};


var dynamicGridWidthChange = function () {
    if ("" !== _dynamicResizeTargetWidth) {
        $.each(_dynamicResizeTargetWidth, function(index, _gInfo){
//            var _new_height = getGridHeight(_gInfo.gridDivId);
//            var _new_width = getGridWidth(_gInfo.gridDivId);
//            console.log("_new_width : " + _new_width);

            //document.getElementById(_gInfo.gridDivId).style.height = _new_height + "px";
//            console.log("[1111]" + _gInfo.gridName);
        	eval(_gInfo.gridDivId+".resetSize();");
        });
    }

    try {
        if ("function" === typeof resizeEventHandler) resizeEventHandler();
    } catch (e) {
        //console.log("window.resize:" + e);
    }
};

/**
 * browser detect
 */
var browser = (function() {
    var s = navigator.userAgent.toLowerCase();
    var match = /(webkit)[ \/](\w.]+)/.exec(s) ||
        /(opera)(?:.*version)?[ \/](\w.]+)/.exec(s) ||
        /(msie) ([\w.]+)/.exec(s) ||
        /(edge)[ \/]([\w.]+)/.exec(s) ||
        /(chrome)[\/]([\w.]+)/.exec(s) ||
        !/compatible/.test(s) &&
        /(mozilla)(?:.*? rv:([\w.]+))?/.exec(s) ||
        [];
    return { name: match[1] || '', version: match[2] || '0' };
}());

var fnHide = function ($obj) {
    if (-1 < $.inArray(browser.name, ['msie', 'edge', 'mozilla'])) {
        $obj.attr('disabled', true);
    } else {
        $obj.hide();
    }
};

var fnShow = function ($obj) {
    if (-1 < $.inArray(browser.name, ['msie', 'edge', 'mozilla'])) {
        $obj.attr('disabled', false);
    } else {
        $obj.show();
    }
};

var showMask = function () {
    $('#mask').remove();
    $('body').append("<div id='mask' style='position:absolute;left:0;top:0;z-index:9000;background-color:#000;display:none;'></div>");
    //mask { position:absolute;left:0;top:0;z-index:9000;background-color:#000;display:none;}
    //화면의 높이와 너비를 구한다.
    var maskHeight = $(document).height();
    var maskWidth = $(window).width();

    //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
    $('#mask').css({'width':maskWidth,'height':maskHeight});

    //애니메이션 효과
    //$('#mask').fadeIn(1000);
    $('#mask').fadeTo("slow",0.8);
};

var hideMask = function () {
    $('#mask').remove();
};

var showProgress = function(base) {
    base = base||'body';
    $('#progress').remove();

    var maskHeight;
    var maskWidth;
    var maskCentorPosTop;
    var maskCentorPosLeft;

    if ('body'===base) {
        maskHeight = $(document).height();
    	//maskHeight = $(window).height();
        maskWidth = $(window).width();
    } else {
        maskHeight = $(base).height();
        maskWidth = $(base).width();
    }

    maskCentorPosTop	= Math.floor(maskHeight / 2) - 70;
    maskCentorPosLeft	= Math.floor(maskWidth / 2) - 80;

    var _progressHtml = "<br/><br/><div id='floatingBarsG' class='loadingBody'>"
        + "<div class='blockG' id='rotateG_01'></div>"
        + "<div class='blockG' id='rotateG_02'></div>"
        + "<div class='blockG' id='rotateG_03'></div>"
        + "<div class='blockG' id='rotateG_04'></div>"
        + "<div class='blockG' id='rotateG_05'></div>"
        + "<div class='blockG' id='rotateG_06'></div>"
        + "<div class='blockG' id='rotateG_07'></div>"
        + "<div class='blockG' id='rotateG_08'></div>"
        + "</div>"
        + "</div>";

    if ('body'===base) {
        $(base).append("<div id='progress' style='position:absolute;left:0;top:0;z-index:9900;background-color:#F2F2F2;display:none;vertical-align:middle;'>" + _progressHtml);
    } else {
        $(base).parent().css("position"	,"relative");
        $(base).parent().css("height"	,maskHeight);

        $(base).css("position"	,"relative");
        $(base).css("z-index"	,"1");

        $(base).parent().append("<div id='progress' style='position:relative;left:0;top:-"+maskHeight+";z-index:9900;background-color:#F2F2F2;display:none;'>" + _progressHtml);
    }

    //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
    if ('body'===base) {
        $('#progress').css({'width':maskWidth,'height':maskHeight});
        $('#floatingBarsG').css({'top':maskCentorPosTop,'left':maskCentorPosLeft});
    } else {
        $('#progress').css({'width':maskWidth,'height':maskHeight});
        $('#floatingBarsG').css({'top':maskCentorPosTop,'left':maskCentorPosLeft});
    }

    //애니메이션 효과
    //$('#mask').fadeIn(1000);
    $('#progress').fadeTo("slow",0.8);

};

var hideProgress = function(base) {
    $('#progress').remove();
};


/*
 * replaceAll
 */
String.prototype.replaceAll = function(oldStr, newStr) {
    if(!isNullValue(this)) {
        return this.split(oldStr).join(newStr);
    } else {
        return this;
    }
};

/*
 * 문자열의 길이가 n이 될때까지 왼쪽에 문자열 c를 채움
 * ex) alert('123'.lpad(5, '0')); -> 00123
 */
String.prototype.lPad = function (n,c) {
    var i; var a = this.split(''); for (i = 0; i < n - this.length; i++) {a.unshift (c);}; return a.join('');
};

/*
 * 문자열의 길이가 n이 될때까지 오른쪽에 문자열 c를 채움
 */
String.prototype.rPad = function (n,c) {
    var i; var a = this.split(''); for (i = 0; i < n - this.length; i++) {a.push (c);}; return a.join('');
};

/**
 * null 체크
 *
 * @param event
 */
var isNullValue = function(inputValue) {
    if (inputValue == null || inputValue.length == 0 || inputValue == "") {
        return true;
    } else {
        return false;
    }
};

/**
 * 입력받은 값에서 양쪽 공백 지워주기
 *
 * @param event
 */
var trimValue = function(inputValue) {
	if( isEmpty(inputValue) ) {
		return "";
	}

    var sLeftTrimed = inputValue.replace(/^\s+/, "");
    var sBothTrimed = sLeftTrimed.replace(/\s+$/, "");
    return (sBothTrimed);
};


/**
 * 폼데이터를 객체화 (jqGrid의 postData 옵션에 사용)
 */
jQuery.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

/**
 * contextPath 구하기 (http://localhost:8080/test/request.do - "/test")
 */
var getContextPath = function() {
    var path = "";

    path = sessionStorage.getItem("_CONTEXT_PATH_");
    //console.log("path=" + path);
    return path;
};


/**
 * String바이트 수 구하기
 *
 * @param szValue
 * @returns {Number}
 */
var calculateBytes = function(szValue, maxSize) {
    var tcount = 0;
    var tmpStr = new String(szValue);
    var temp = tmpStr.length;

    var onechar;

    if ("undefined" === maxSize) {
        for (var k = 0; k < temp; k++) {
            onechar = tmpStr.charAt(k);
            if (escape(onechar).length > 4) {
                tcount += 3;
            } else {
                tcount += 1;
            }
        }
        return tcount;
    } else {
        var rtnVal = new Array();

        rtnVal[0] = 0;
        rtnVal[1] = "";
        for (var k=0; k<temp; k++ ){
            onechar = tmpStr.charAt(k);

            if (escape(onechar).length > 4){
                tcount += 3;
            }else{
                tcount += 1;
            }

            if (tcount<=maxSize) {
                rtnVal[1] += onechar;
            }
        }

        rtnVal[0]= tcount;
        return rtnVal;
    }
};


/**
 * 날짜 차이 계산 함수
 *
 * @param 필드아이디1
 *            stndDateId : 기준 날짜(YYYY-MM-DD)
 * @param 필드아이디2
 *            targetDateId : 대상 날짜(YYYY-MM-DD)
 * @param 결과반환필드
 *            resultId
 * @returns
 */
var getDateDiff = function(stndDateId, targetDateId, resultId) {
    var date1 = $("#" + stndDateId).val();
    var date2 = $("#" + targetDateId).val();
    if (date1 === "" || date2 === "") { return; }
    var arrDate1 = date1.split("-");
    var getDate1 = new Date(parseInt(arrDate1[0]), parseInt(arrDate1[1]) - 1, parseInt(arrDate1[2]));
    var arrDate2 = date2.split("-");
    var getDate2 = new Date(parseInt(arrDate2[0]), parseInt(arrDate2[1]) - 1, parseInt(arrDate2[2]));
    var getDiffTime = getDate1.getTime() - getDate2.getTime();
    $("#" + resultId).val(Math.floor(getDiffTime / (1000 * 60 * 60 * 24)));
};

/**
 * 날짜 차이 계산 함수
 *
 * @param 날짜1
 *            srcDateStr : 기준 날짜(YYYY-MM-DD)
 * @param 날짜2
 *            targetDateStr : 대상 날짜(YYYY-MM-DD)
 * @param 결과반환
 * @returns
 */
var getStrDateDiff = function(srcDateStr, targetDateStr) {
    if (srcDateStr === "" || targetDateStr === "") { return; }
    var srcDate = new Date(srcDateStr.substring(0,4), srcDateStr.substring(4,6), srcDateStr.substring(6,8), srcDateStr.substring(8,10), srcDateStr.substring(10,12), srcDateStr.substring(12,14));
    var targetDate = new Date(targetDateStr.substring(0,4), targetDateStr.substring(4,6), targetDateStr.substring(6,8), targetDateStr.substring(8,10), targetDateStr.substring(10,12), targetDateStr.substring(12,14));
    var getDiffTime = srcDate.getTime() - targetDate.getTime();
    return Math.floor(getDiffTime / (60 * 60 * 24));
};




var fnGetDomain = function() {
    var dns, arrDns, str;
    dns = document.location.href; // <-- 현재 URL 얻어온다
    arrDns = dns.split("//"); // <-- // 구분자로 짤라와서
    str = arrDns[0] + "//" + arrDns[1].substring(0, arrDns[1].indexOf("/")); // <--
    // 뒤에부터
    // 다음 /
    // 까지
    // 가져온다
    return str;
};


//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//기능 Ajax 통신 ////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * ajax post통신
 */
var ajaxJsonCall = function(url, param, successCallback, errorCallback, isProgress) {
    var contentType;
    var data;
    var dataType;

    //console.log( "param = "+ JSON.stringify(param));

    //fnAddDefaultValue(param);

    if (typeof param == "string") {
        contentType = "application/json;charset=UTF-8";
        data = param;
        dataType = "json"
    } else if (typeof param == "object") {
        contentType = "application/json;charset=UTF-8";
        data = JSON.stringify(param);
        dataType = "json"
    } else {
        alert("ajaxJsonCall Type 오류");
    }



    if (isEmpty(isProgress)) {
        ajaxShowProgress();
    } else {
        if (isProgress) ajaxShowProgress();
    }

    $.ajax({
        type : 'POST'
        ,url : url
        ,contentType : contentType
        ,data : data
        ,dataType : dataType
        ,beforeSend : function(xmlHttpRequest) {
            xmlHttpRequest.setRequestHeader("ajax", "true");
            xmlHttpRequest.setRequestHeader(gCSRF_PARAM_NAME, fnGetCsrfToken() );
        }
        ,success : function(data) {
            //console.log(">>>>>>>>>", JSON.stringify(data));
            try {
                if (data) {
                    if (data["status"] === "SUCC") {
                        if (!isEmpty(data["message"])) alert(data["message"]);
                        if (typeof successCallback !== 'undefined') {
                            successCallback(data);
                        }
                    } else if (data["status"] === "FAIL") {
                    	if (!isEmpty(data["message"])) {
                    		if( "_ACCESS_DENIED_" == data["message"] ) {
                    			location.href = getContextPath() + "/common/accessDenied.jsp";
                    			return;
                    		} else if ( "_NO_AUTH_" == data["message"] ) {
                    			location.href = getContextPath() + "/common/noAuthError.jsp";
                    			return;
                    		} else if ( "_DUP_USER_" == data["message"] ) {
                    			location.href = getContextPath() + "/common/dupAccessDenied.jsp";
                    			return;
                    		}
                    	}
                        if (!isEmpty(data["errMsg"])) alert(data["errMsg"]);
                        if (typeof errorCallback !== 'undefined') {
                            errorCallback(data);
                        }
                    } else {
                        if (typeof successCallback !== 'undefined') {
                            successCallback(data);
                        }
                    }
                }
            } catch (e) {
                //console.log(JSON.stringify( e));
                alert(e.message);
            }
        }
        ,error : function(xhr, status, error) {
             //console.log("xhr=" + JSON.stringify( xhr));
        	 //console.log("status=" + JSON.stringify( status));
             //console.log("error=" +  JSON.stringify(error));
            if (401 === xhr.status) {
                //alert('사용자 세션이 만료 되었거나, 로그인 하지 않았습니다.');
                location.href = getContextPath() + "/common/accessDenied.jsp";
            } else {
                alert("서버 응답 오류:" + error);
            }
        }
    });
};

var ajaxJsonCallSync = function(url, param, successCallback, errorCallback, isProgress) {
    var contentType;
    var data;
    var dataType;

    //fnAddDefaultValue(param);

    if (typeof param == "string") {
        contentType = "application/json;charset=UTF-8";
        data = param;
        dataType = "json"
    } else if (typeof param == "object") {
        contentType = "application/json;charset=UTF-8";
        data = JSON.stringify(param);
        dataType = "json"
    } else {
        alert("ajaxJsonCall Type 오류");
    }

    // console.log( "data = "+ data);

    if (isEmpty(isProgress)) {
        ajaxShowProgress();
    } else {
        if (isProgress) ajaxShowProgress();
    }

    $.ajax({
        type : 'POST'
        ,url : url
        ,contentType : contentType
        ,data : data
        ,dataType : dataType
        ,async : false
        ,beforeSend : function(xmlHttpRequest) {
            xmlHttpRequest.setRequestHeader("ajax", "true");
            xmlHttpRequest.setRequestHeader(gCSRF_PARAM_NAME, fnGetCsrfToken() );
        }
        ,success : function(data) {
           // console.log(">>>>>>>>>", JSON.stringify(data));
            try {
                if (data) {
                    if (data["status"] === "SUCC") {
                        if (!isEmpty(data["message"])) alert(data["message"]);
                        if (typeof successCallback !== 'undefined') {
                            successCallback(data);
                        }
                    } else if (data["status"] === "FAIL") {
                    	if (!isEmpty(data["message"])) {
                    		if( "_ACCESS_DENIED_" == data["message"] ) {
                    			location.href = getContextPath() + "/common/accessDenied.jsp";
                    			return;
                    		} else if ( "_NO_AUTH_" == data["message"] ) {
                    			location.href = getContextPath() + "/common/noAuthError.jsp";
                    			return;

                    		} else if ( "_DUP_USER_" == data["message"] ) {
                    			location.href = getContextPath() + "/common/dupAccessDenied.jsp";
                    			return;
                    		}
                    	}
                        if (!isEmpty(data["errMsg"])) alert(data["errMsg"]);
                        if (typeof errorCallback !== 'undefined') {
                            errorCallback(data);
                        }
                    } else {
                        if (typeof successCallback !== 'undefined') {
                            successCallback(data);
                        }
                    }
                }
            } catch (e) {
                //console.log(JSON.stringify( e));
                alert(e.message);
            }
        }
        ,error : function(xhr, status, error) {
            // console.log("xhr=" + JSON.stringify( xhr));
            // console.log("status=" + JSON.stringify( status));
            // console.log("error=" +  JSON.stringify(error));
            if (401 === xhr.status) {
                //alert('사용자 세션이 만료 되었거나, 로그인 하지 않았습니다.');
                location.href = getContextPath() + "/common/accessDenied.jsp";
            } else {
                alert("서버 응답 오류:" + error);
            }
        }
    });
};


/*
 * 첨부파일  ajax 통신
 * */
var ajaxFileJsonCall = function(url, params, successCallback, errorCallback, isProgress) {

	var formParam = new FormData();

	for(var i=0; i < $("#"+params.fileInputId)[0].files.length; i++){
		formParam.append("_uploadFile", $("#"+params.fileInputId)[0].files[i]);
	}

	formParam.append("fileInputId", params.fileInputId);

	$.each(params, function(key, value){
		if(isNotEmpty(key) && isNotEmpty(value) ){
			formParam.append(key, value);
		}
	});

    if (isEmpty(isProgress)) {
        ajaxShowProgress();
    } else {
        if (isProgress) ajaxShowProgress();
    }

    $.ajax({
        type : 'POST'
        ,url : url
        ,contentType : false
        ,processData : false
        ,data : formParam
        ,dataType : "json"
        ,beforeSend : function(xmlHttpRequest) {
            xmlHttpRequest.setRequestHeader("ajax", "true");
            xmlHttpRequest.setRequestHeader(gCSRF_PARAM_NAME, fnGetCsrfToken() );
        }
        ,success : function(data) {
            try {
                if (data) {
                    if (data["status"] === "SUCC") {
                        if (!isEmpty(data["message"])) alert(data["message"]);
                        if (typeof successCallback !== 'undefined') {
                            successCallback(data);
                        }
                    } else if (data["status"] === "FAIL") {
                    	if (!isEmpty(data["message"])) {
                    		if( "_ACCESS_DENIED_" == data["message"] ) {
                    			location.href = getContextPath() + "/common/accessDenied.jsp";
                    			return;
                    		} else if ( "_NO_AUTH_" == data["message"] ) {
                    			location.href = getContextPath() + "/common/noAuthError.jsp";
                    			return;

                    		} else if ( "_DUP_USER_" == data["message"] ) {
                    			location.href = getContextPath() + "/common/dupAccessDenied.jsp";
                    			return;
                    		}
                    	}
                        if (!isEmpty(data["errMsg"])) alert(data["errMsg"]);
                        if (typeof errorCallback !== 'undefined') {
                            errorCallback(data);
                        }
                    } else {
                        if (typeof successCallback !== 'undefined') {
                            successCallback(data);
                        }
                    }
                }
            } catch (e) {
                //console.log(JSON.stringify( e));
                alert(e.message);
            }
        }
        ,error : function(xhr, status, error) {
            if (401 === xhr.status) {
                //alert('사용자 세션이 만료 되었거나, 로그인 하지 않았습니다.');
            	location.href = getContextPath() + "/common/accessDenied.jsp";
            } else {
                alert("서버 응답 오류:" + error);
            }
        }
    });
};

/*
 * XML파일  ajax 통신
 * */
var ajaxXmlFileJsonCall = function(url, params, successCallback, errorCallback, isProgress) {
	var formParam = new FormData();

	for(var i=0; i < $("#"+params.fileInputId)[0].files.length; i++){
		formParam.append("_xmlFile", $("#"+params.fileInputId)[0].files[i]);

		var fileNm = $("#"+params.fileInputId)[0].files[i].name;
		var ext = fileNm.substring(fileNm.lastIndexOf(".") + 1, fileNm.length);
		if(params.zipYn == "N"){
			if(!(ext == "xml" || ext == "XML" || ext == "dti" || ext == "DTI")){
				alert("xml 또는 dti 확장자가 아닌 파일입니다.")
				return;
			}
		} else{
			if(!(ext == "xml" || ext == "XML" || ext == "dti" || ext == "DTI" || ext == "zip" || ext == "ZIP")){
				alert("zip, xml, dti 확장자가 아닌 파일입니다.")
				return;
			}
		}
	}

	$.each(params, function(key, value){
		if(isNotEmpty(key) && isNotEmpty(value) ){
			formParam.append(key, value);
		}
	});

    if (isEmpty(isProgress)) {
        ajaxShowProgress();
    } else {
        if (isProgress) ajaxShowProgress();
    }

    $.ajax({
        type : 'POST'
        ,url : url
        ,contentType : false
        ,processData : false
        ,data : formParam
        ,dataType : "json"
        ,beforeSend : function(xmlHttpRequest) {
            xmlHttpRequest.setRequestHeader("ajax", "true");
            xmlHttpRequest.setRequestHeader(gCSRF_PARAM_NAME, fnGetCsrfToken() );
        }
        ,success : function(data) {
            try {
                if (data) {
                    if (data["status"] === "SUCC") {
                    	if(params.zipYn == "N"){
	                        if (!isEmpty(data["message"])) alert(data["message"]);
                    	}
                        if (typeof successCallback !== 'undefined') {
                            successCallback(data);
                        }
                    } else if (data["status"] === "FAIL") {
                        if (!isEmpty(data["errMsg"])) alert(data["errMsg"]);
                        if (typeof errorCallback !== 'undefined') {
                            errorCallback(data);
                        }
                    } else {
                        if (typeof successCallback !== 'undefined') {
                            successCallback(data);
                        }
                    }
                }
            } catch (e) {
                //console.log(JSON.stringify( e));
                alert(e.message);
            }
        }
        ,error : function(xhr, status, error) {
            if (401 === xhr.status) {
                //alert('사용자 세션이 만료 되었거나, 로그인 하지 않았습니다.');
                location.href = getContextPath() + "/common/accessDenied.jsp";
            } else {
                alert("서버 응답 오류:" + error);
            }
        }
    });
};

/*
 * Excel파일  ajax 통신
 * */
var ajaxExcelFileJsonCall = function(url, params, successCallback, errorCallback, isProgress) {
	var formParam = new FormData();

	for(var i=0; i < $("#"+params.fileInputId)[0].files.length; i++){
		formParam.append("_excelFile", $("#"+params.fileInputId)[0].files[i]);

		var fileNm = $("#"+params.fileInputId)[0].files[i].name;
		var ext = fileNm.substring(fileNm.lastIndexOf(".") + 1, fileNm.length);

		if(!(ext == "xls" || ext == "XLS" || ext == "xlsx" || ext == "XLSX")){
			alert("xls 또는 xlsx 확장자가 아닌 파일입니다.")
			return;
		}
	}

	$.each(params, function(key, value){
		if(isNotEmpty(key) && isNotEmpty(value) ){
			formParam.append(key, value);
		}
	});

    if (isEmpty(isProgress)) {
        ajaxShowProgress();
    } else {
        if (isProgress) ajaxShowProgress();
    }

    $.ajax({
        type : 'POST'
        ,url : url
        ,contentType : false
        ,processData : false
        ,data : formParam
        ,dataType : "json"
        ,beforeSend : function(xmlHttpRequest) {
            xmlHttpRequest.setRequestHeader("ajax", "true");
            xmlHttpRequest.setRequestHeader(gCSRF_PARAM_NAME, fnGetCsrfToken() );
        }
        ,success : function(data) {
            try {
                if (data) {
                    if (data["status"] === "SUCC") {
                    	if(params.zipYn == "N"){
	                        if (!isEmpty(data["message"])) alert(data["message"]);
                    	}
                        if (typeof successCallback !== 'undefined') {
                            successCallback(data);
                        }
                    } else if (data["status"] === "FAIL") {
                        if (!isEmpty(data["errMsg"])) alert(data["errMsg"]);
                        if (typeof errorCallback !== 'undefined') {
                            errorCallback(data);
                        }
                    } else {
                        if (typeof successCallback !== 'undefined') {
                            successCallback(data);
                        }
                    }
                }
            } catch (e) {
                //console.log(JSON.stringify( e));
                alert(e.message);
            }
        }
        ,error : function(xhr, status, error) {
            if (401 === xhr.status) {
                //alert('사용자 세션이 만료 되었거나, 로그인 하지 않았습니다.');
                location.href = getContextPath() + "/common/accessDenied.jsp";
            } else {
                alert("서버 응답 오류:" + error);
            }
        }
    });
};

/*
 * 첨부파일 확장자 체크 함수
 */
function isValidFileExt(ext){

    fileExt = ext.toLowerCase();

    var acceptFileTypes = _gFileAttchAllowType.replaceAll("*.", "");

	if (acceptFileTypes.indexOf(fileExt)<0) {
		alert("허용되지 않은 확장자 입니다.\n\n업로드 가능 확장자 : " + acceptFileTypes );
		return false;
	} else {
		return true;
	}
}


var ajaxHtmlCall = function(url, param, successCallback, errorCallback, isProgress) {
    var contentType;
    var data;
    var dataType;

    if (typeof param == "string") {
        contentType = "application/json;charset=UTF-8";
        data = param;
        dataType = "html"
    } else if (typeof param == "object") {
        contentType = "application/json;charset=UTF-8";
        data = JSON.stringify(param);
        dataType = "html"
    } else {
        alert("ajaxHtmlCall Type 오류");
    }

    //console.log( "data = "+ data);

    if (isEmpty(isProgress)) {
        ajaxShowProgress();
    } else {
        if (isProgress) ajaxShowProgress();
    }

    $.ajax({
        type : 'POST'
        ,url : url
        ,contentType : contentType
        ,data : data
        ,dataType : dataType
        ,beforeSend : function(xmlHttpRequest) {
            xmlHttpRequest.setRequestHeader("ajax", "true");
        }
        ,success : function(data) {
            //console.log(">>>>>>>>>", JSON.stringify(data));
            try {
                if (data) {
                    if (data["status"] === "SUCC") {
                        if (!isEmpty(data["message"])) alert(data["message"]);
                        if (typeof successCallback !== 'undefined') {
                            successCallback(data);
                        }
                    } else if (data["status"] === "FAIL") {
                        if (!isEmpty(data["errMsg"])) alert(data["errMsg"]);
                        if (typeof errorCallback !== 'undefined') {
                            errorCallback(data);
                        }
                    } else {
                        if (typeof successCallback !== 'undefined') {
                            successCallback(data);
                        }
                    }
                }
            } catch (e) {
                //console.log(JSON.stringify( e));
                alert(e.message);
            }
        }
        ,error : function(xhr, status, error) {
            //console.log("xhr=" + JSON.stringify( xhr));
            //console.log("status=" + JSON.stringify( status));
            //console.log("error=" +  JSON.stringify(error));
            if (401 === xhr.status) {
                alert('사용자 세션이 만료 되었거나, 로그인 하지 않았습니다.');
                //location.href = getContextPath() + "URL정의 필요.";
            } else {
                alert("서버 응답 오류:" + error);
            }
        }
    });
};


var errorCallback = function() {
    alert("서버와의 연결이 해지되었습니다.");
};

/**
 * loading bar function
 */
function ajaxShowProgress() {
    $(document).ajaxStart(function() {
        showProgress();
    }).ajaxStop(function() {
        hideProgress();
    });


}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//기능 Controll 제어 ////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * input시 글자수 제한
 * inputObjId : 입력박스 아이디
 * msgObjId : 글자수 표현할 메시지 박스 아이디
 * maxSize : 제한 글자수
 */
var setTextLimit = function (inputObjId, maxSize) {
    var hanLen 		= maxSize/3;
    var _msgObjId 	= "_"+inputObjId+"_MSG";
    hanLen = parseInt(hanLen);

    //console.log($("#"+inputObjId).attr('type'));

    if ("text" === $("#"+inputObjId).attr('type')) {
        $("#"+inputObjId).after("<div class='t_right d_none'><span id='"+_msgObjId+"'></span></div>");
    } else {
        $("#"+inputObjId).after("<div class='t_right'><span id='"+_msgObjId+"'></span></div>");
    }

    if (_msgObjId!=null && _msgObjId!="") {
        var rtnVal = calculateBytes($("#"+inputObjId).val(), maxSize);

        $("#"+_msgObjId).empty();
        $("#"+_msgObjId).append(rtnVal[0]+"/"+maxSize);
    }

    $("#"+inputObjId).on("change keyup", function() {
        var rtnVal = calculateBytes($("#"+inputObjId).val(), maxSize);
        var currSize = rtnVal[0];
        if (currSize > maxSize) {
            alert("입력 글자수가 초과되었습니다.\n영문은 " + maxSize +"자, 한글은 " + hanLen + "자 만큼 입력가능합니다.");
            $("#"+inputObjId).val(rtnVal[1]);
            currSize = maxSize;
        }
        if (_msgObjId!=null && _msgObjId!="") {
            $("#"+_msgObjId).empty();
            $("#"+_msgObjId).append(currSize+"/"+maxSize);
        }

    });
};

/**
 * 숫자만 입력받게 하고 콤마를 붙였다 뗐다함. 사용법 : setCommaFormat('id')
 *
 * @param numObjId
 */
var setCommaFormat = function(numObjId, scale) {

    // $("input[id=" + numObjId + "]").attr("style","text-align:right"); // 다른
    // Style까지 모두 무시되어 아래 CSS로 변경
    $("input[id=" + numObjId + "]").css("text-align", "right");
    $("input[id=" + numObjId + "]").keyup(function(event) {

        if( isNotEmpty(scale)) {
            var val = $(this).val().match(/[^0-9.-]/g);
            if (val != null) {
                $(this).val($(this).val().replace(/[^0-9.-]/g, ''));
            }
        } else {
            var val = $(this).val().match(/[^0-9-]/g);
            if (val != null) {
                $(this).val($(this).val().replace(/[^0-9-]/g, ''));
            }
        }
    });
    $("input[id=" + numObjId + "]").focus(function(event) {
        $(this).val($(this).val().replace(/,/g, ""));
    });
    $("input[id=" + numObjId + "]").blur(function() {
        //console.log( numObjId + " blur");
        if ($(this).val() != null) {
            //console.log(  "value = " + $(this).val());
            if( isNotEmpty(scale)) {
                var arrNum = $(this).val().split('.');
                if (arrNum.length >= 2) {
                    var scaleValue = arrNum[1].substring(0, scale);
                    $(this).val(arrNum[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "." + scaleValue);
                } else {
                    $(this).val(arrNum[0].replace(/\B(?=(\d{3})+(?!\d))/g, ","));
                }
            } else {
                var arrNum = $(this).val().split('.');
                if (arrNum.length >= 2)
                    $(this).val(arrNum[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",") );
                else
                    $(this).val(arrNum[0].replace(/\B(?=(\d{3})+(?!\d))/g, ","));
            }
        }
    });
};



/**
 * 00 ~ 23시까지만 입력하게 함,  사용법 : setHourFormat('id')
 *
 * @param numObjId
 */
var setHourFormat = function(numObjId) {

    $("input[id=" + numObjId + "]").css("text-align", "center");
    $("input[id=" + numObjId + "]").keyup(function(event) {
        var val = $(this).val().match(/[^0-9]/g);
        if (val != null) {
            $(this).val($(this).val().replace(/[^0-9]/g, ''));
        }
    });

    $("input[id=" + numObjId + "]").blur(function() {
        //console.log( numObjId + " blur");
        if ($(this).val() != null) {
            var value = Number($(this).val());
            //console.log(  "value = " + value);

            if( value < 0 || value > 23) {
                alert( "00 ~ 23 시 까지만 가능합니다.");
                $(this).val("23");
            } else if( value < 10 ) {
                $(this).val("0" + value);
            }

        }
    });
};


/**
 * 00 ~ 59분 까지만 입력하게 함,  사용법 : setMinuteFormat('id')
 *
 * @param numObjId
 */
var setMinuteFormat = function(numObjId) {

    $("input[id=" + numObjId + "]").css("text-align", "center");
    $("input[id=" + numObjId + "]").keyup(function(event) {
        var val = $(this).val().match(/[^0-9]/g);
        if (val != null) {
            $(this).val($(this).val().replace(/[^0-9]/g, ''));
        }
    });

    $("input[id=" + numObjId + "]").blur(function() {
        if ($(this).val() != null) {
            var value = Number($(this).val());
            if( value < 0 ||  value > 59) {
                alert( "00 ~ 59 분 까지만 가능합니다.");
                $(this).val("59");
            } else if( value < 10 ) {
                $(this).val("0" + value);
            }
        }
    });
};

/**
 * 엔터키 입력 시 특정 함수 호출
 *
 * @param width -
 *            창크기(width)
 * @param height -
 *            창크기(height)
 */
var checkEnter = function(functionName) {
    if (event.keyCode === 13) {
        eval(functionName);
    }
};


/**
 * HTML Document에서 입력 Tag들의 값을 Object에 담아 반환
 * Input, Select, TextArea tag?값을 읽어서 param에 담음
 * @param isIncludeDisabled
 *            disabled된 입력 Tag 값을 가져올지 구분, 디폴트는 가져옴
 * @returns params Objects
 */
var fnGetParams = function(isIncludeDisabled) {
    isIncludeDisabled = isIncludeDisabled || true;
    var params = {};
    $(':input').each(function() {
        if (-1 < this.id.indexOf('jqg')) {
            //나중에 그리드 INPUT 받아서 처리시에 추가
        } else if (this.id !== '' && typeof $(this).val() == "string" && -1 < $(this).val().indexOf("***1")) {
            // realgrid bypass
        } else {
            if (this.id !== '') {
                if (!isIncludeDisabled) {
                    if ('disabled' !== $(this).attr('disabled')) {
                        params[this.id] = $(this).val();
                    }
                } else {
                    if (this.type==="checkbox") {
                        if (this.checked) params[this.id] = $(this).val();
                        else params[this.id] = "";
                    } else if (this.type==="radio") {
                        if (this.checked) params[this.id] = $(this).val();
                    } else {
                        params[this.id] = $(this).val();
                    }
                }
            }
        }
    });

    //console.log( 'fnGetParams params = ' + JSON.stringify(params));
    return params;
};


var fnGetMakeParams = function() {
    var params = {};
    $('#_MENU_FORM_ :input').each(function() {
        if (this.id.indexOf('jqg')>-1) {
            //나중에 그리드 INPUT 받아서 처리시에 추가
        } else {
            params[this.id] = $(this).val();
        }
    });
    return params;
};

/**
 * HTML Document에서 입력 Tag들의 값을 Object에 담아 반환
 * Input, Select, TextArea tag?값을 읽어서 param에 담음
 * @param isIncludeDisabled
 *            disabled된 입력 Tag 값을 가져올지 구분, 디폴트는 가져옴
 * @returns params Objects
 */
var fnGetFormParams = function(formId, isIncludeDisabled) {
    isIncludeDisabled = isIncludeDisabled || true;
    var params = {};
    $('#'+ formId + ' :input').each(function() {
        if (this.id != '') {
            if (this.id.indexOf('jqg')>-1) {
                //나중에 그리드 INPUT 받아서 처리시에 추가
            } else {
                if (!isIncludeDisabled) {
                    if ('disabled' != $(this).attr('disabled')) {
                        params[this.id] = $(this).val();
                    }
                } else {
                    if (this.type=="checkbox") {
                        if (this.checked) params[this.id] = $(this).val();
                        else params[this.id] = "";
                    } else if (this.type=="radio") {
                        if( this.checked) params[this.name] = $(this).val();
                    } else {
                        params[this.id] = $(this).val();
                    }
                }
            }
        }
    });
    $.extend(params, fnGetMakeParams());
    return params;
};





/**
 * 입력 화면을 상세 조회 화면으로 사용할때
 * input, select, textarea에 대해서 공통으로 처리
 */
var SetReadOnly = function (btnYn) {
    'use strict';
    $(':input').attr('readonly', true);
    $(':input').addClass('readonly');

    var btnShowYn = false;

    if (btnYn === undefined) {
        btnShowYn = false;
    } else {
        if (btnYn) {
            btnShowYn = true;
        } else {
            btnShowYn = false;
        }
    }
    $('.gBtn, button').hide();
    $('.tBtn, button').hide();
    $('.ico').hide();
    $('.searchicon').hide();
    if (!btnShowYn) {
        $('.btnL, button').hide();
    }

    /*  single file upload readonly */
    $('input[id$="_uploadFile"]').hide();
    $('.file-upload').hide();
    $('input[id$="_setReadOnlyFlag"]').val('Y');
    /*  single file upload readonly */

    $(".datepicker").each(function() {
        $(this).hide();
        $(this).after($(this).val());
    });

    $(".datetimepicker").each(function() {
        $(this).hide();
        $(this).after($(this).val());
    });

    $('input:radio').each(function() {
        $(this).hide();
        if (!$(this).is(':checked')) {try{$(this)[0].nextSibling.nodeValue = "";}catch(e){}}
    });
    $('input:checkbox').each(function() {
        $(this).hide();
        if (!$(this).is(':checked')) {try{$(this)[0].nextSibling.nodeValue = "";}catch(e){}}
    });
    $('select').each(function() {
        //if (!$(this).is(':selected')) fnHide($(this));
        $(this).hide();
        try{
            if (""!==$(this)[0].options[$(this)[0].selectedIndex].value) {
                $(this).after($(this)[0].options[$(this)[0].selectedIndex].text);
            }}catch(e){}
    });
    $('textarea').each(function() {
        $(this).css('border', 'solid 1px #c6c6c6');
    });

};


/**
 * form 의
 * input, select, textarea에 대해서 공통으로 처리
 */
var SetFormReadOnly = function (formId, btnYn) {
    'use strict';
    $('#' + formId + ' :input').attr('readonly', true);
    $('#' + formId + ' :input').addClass('readOnly');

    var btnShowYn = false;

    if (!btnYn) {
        btnShowYn = false;
    } else {
        if (btnYn) {
            btnShowYn = true;
        } else {
            btnShowYn = false;
        }
    }

    $('#' + formId + ' .gBtn').hide();
    $('#' + formId + ' .tBtn').hide();
    $('#' + formId + ' :button').hide();
    $('#' + formId + ' .ico').hide();

    if (!btnShowYn) {
        $('#' + formId + ' .btnL').hide();
    }

    /*  single file upload readonly */
    $('#' + formId + ' input[id$="_uploadFile"]').hide();
    $('#' + formId + ' .file-upload').hide();
    $('#' + formId + ' input[id$="_setReadOnlyFlag"]').val('Y');
    /*  single file upload readonly */

    $('#' + formId + ' .datepicker').each(function() {
        $(this).hide();
        $(this).after($(this).val());
    });

    $('#' + formId + ' input:radio').each(function() {
        $(this).hide();
        if (!$(this).is(':checked')) $(this)[0].nextSibling.nodeValue = "";
    });
    $('#' + formId + ' input:checkbox').each(function() {
        $(this).hide();
        if (!$(this).is(':checked')) $(this)[0].nextSibling.nodeValue = "";
    });
    $('#' + formId + ' select').each(function() {
        //if (!$(this).is(':selected')) fnHide($(this));
        $(this).hide();
        if (""!=$(this)[0].options[$(this)[0].selectedIndex].value) {
            $(this).after($(this)[0].options[$(this)[0].selectedIndex].text);
        }
    });
    $('#' + formId + ' textarea').each(function() {
        $(this).css('border', 'solid 1px #c6c6c6');
    });

};



/**
 * 특정 id의
 * input, select, textarea에 대해서 공통으로 처리
 */
var SetReadOnlyById = function (args) {
    'use strict';
    for (var i = 0, nSize = args.length; i < nSize; i++) {
        var obj = $('#' + args[i]);
        /*
        console.log( "args[i] = " + args[i]);
        console.log( "obj attr type = " + obj.attr("type"));
        console.log( "obj hasClass = " + obj.hasClass('datepicker') );
        console.log( "obj is select= " + obj.is("select") );
        console.log( "obj is textarea= " + obj.is("textarea") );
        console.log( "obj.next().text()=" + obj.next().is("button") );
        */

        obj.attr('readonly', true);
        obj.addClass('readOnly');

        if (obj.is("input")) {
            var objType = obj.attr("type");
            if (objType === "text") {
                if (obj.hasClass('datepicker')) {
                    if (obj.next().is("button")) {
                        if(obj.next().hasClass('ui-datepicker-trigger')) {
                            obj.next().hide();
                        }
                    }
                }
            } else if (objType === "radio") {
            } else if (objType === "checkbox") {
            } else {
            }
        } else if (obj.is("select")) {
            obj.each(function() {
                obj.hide();
                if (""!==obj[0].options[obj[0].selectedIndex].value) {
                    obj.after(obj[0].options[obj[0].selectedIndex].text);
                }
            });
        } else if( obj.is("textarea")) {
            obj.css('border', 'solid 1px #c6c6c6');
        } else if( obj.is("button")) {
            obj.hide();
        } else {
            if (obj.hasClass('tBtn')
                || obj.hasClass('gBtn')
                || obj.hasClass('ico')) {
                obj.hide();
            }
        }
    }
};


var SetDisabledById = function (args, isFlag) {
    'use strict';
    for (var i = 0, nSize = args.length; i < nSize; i++) {
        var obj = $('#' + args[i]);
        /*
        console.log( "args[i] = " + args[i]);
        console.log( "obj attr type = " + obj.attr("type"));
        console.log( "obj hasClass = " + obj.hasClass('datepicker') );
        console.log( "obj is select= " + obj.is("select") );
        console.log( "obj is textarea= " + obj.is("textarea") );
        console.log( "obj.next().text()=" + obj.next().is("button") );
        */

        if (isFlag) {
            obj.attr('disabled', true);
            if (obj.is("input")) {
                var objType = obj.attr("type");
                if (objType === "text") {
                    if (obj.hasClass('datepicker') ) {
                        if (obj.next().is("button")) {
                            if (obj.next().hasClass('ui-datepicker-trigger')) {
                                obj.next().hide();
                            }
                        }
                    }
                } else if( objType === "radio") {
                    $('input:radio[name="' + args[i] + '"]').each(function() {
                        $(this).attr('disabled', true);
                    });
                } else if( objType === "checkbox") {
                } else {
                }
            } else if( obj.is("button")) {
                obj.hide();
            } else {
                if (obj.hasClass('tBtn')
                    || obj.hasClass('gBtn')
                    || obj.hasClass('ico')) {
                    obj.hide();
                }
            }
        } else {
            obj.attr('disabled', false);
            if (obj.is("input")) {
                var objType = obj.attr("type");
                if (objType === "text") {

                    if (obj.hasClass('datepicker')) {
                        if (obj.next().is("button")) {
                            if (obj.next().hasClass('ui-datepicker-trigger')) {
                                obj.next().show();
                            }
                        }
                    }
                }
            } else if (obj.is("button")) {
                obj.show();
            } else {
                if (obj.hasClass('tBtn')
                    || obj.hasClass('gBtn')
                    || obj.hasClass('ico')) {
                    obj.show();
                }
            }
        }
    }
};


//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//기능 화면제어 ////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////


/**
 * 버튼의 상태를 처리한다.
 *
 * @param btnId -
 *            button 객체 아이디
 * @param btnStatus -
 *            button 상태
 */
var setButtonStatus = function(btnId, btnStatus) {
    var arrData = btnId.split("^");
    for (var i = 0; i < arrData.length; i++) {
        if (btnStatus === 'N') {
            $("#" + arrData[i]).hide();
        } else {
            $("#" + arrData[i]).show();
        }
    }
};

/**
 * 팝업 윈도우 가운데로 띄울 위치 구하기
 *
 * @param width -
 *            창크기(width)
 * @param height -
 *            창크기(height)
 */
var centerWindow = function(width, height) {
    //var outx = screen.width;
    //var outy = screen.height;
    var outx = screen.availWidth;
    var outy = screen.availHeight;
    var x = (outx - width) / 2;
    var y = (outy - height) / 2;
    dim = new Array(2);
    dim[0] = x;
    dim[1] = y;
    dim[2] = width;
    dim[3] = height;
    return dim;
};

/**
 * target URL로 POST request 한다. 사용법 : gotoUrlMethodPost("<c:url
 * value='TARGET_URL지정'/>", params , "_self");
 *
 * @param url
 * @param params
 * @param target
 */
var gotoUrlMethodPost = function(url, params, target) {
    var f = document.createElement('form');
    var obj, value;
    for (var key in params) {
        value = params[key];
        obj = document.createElement('input');
        obj.setAttribute('type', 'hidden');
        obj.setAttribute('name', key);
        obj.setAttribute('value', value);
        f.appendChild(obj);
    }

    if (target) f.setAttribute('target', target);

    f.setAttribute('method', 'post');
    f.setAttribute('action', url);
    document.body.appendChild(f);
    f.submit();
};


/**
 * CSRF Token을 가져온다
 */
var fnGetCsrfToken = function() {
	return $("#_MENU_FORM_").find("input[name=" + gCSRF_PARAM_NAME + "]").val();
};


/**
 * CSRF Token Param을 가져온다
 */
var fnGetCsrfTokenParam = function() {
	var param = {};
	param[gCSRF_PARAM_NAME] = fnGetCsrfToken();
	return param;
};


/**
 * Post 방식으로 submit
 *
 * @param url
 *            submit할 주소
 * @param params
 *            Plain Object
 * @param target
 *            url이 적용되는 target 이름(현재 페이지는 '_self'
 */
var fnPostGoto = function(url, params, target) {
	//fnAddDefaultValue(params);
	$.extend(params, fnGetCsrfTokenParam());
	//console.log( "fnPostGoto params = " + JSON.stringify( params));

    var f = document.createElement('form');
    var obj, value;
    for (var key in params) {
        value = params[key];
        obj = document.createElement('input');
        obj.setAttribute('type', 'hidden');
        obj.setAttribute('name', key);
        obj.setAttribute('value', value);
        f.appendChild(obj);
    }
    if (target) f.setAttribute('target', target);
    f.setAttribute('method', 'post');
    //f.setAttribute('action', url);
    var path = url;
    f.setAttribute('action', path);
    document.body.appendChild(f);
    f.submit();
};


/**
 * Post 방식으로 Popup
 *
 * @param url
 *            submit할 주소
 * @param params
 *            Plain Object
 * @param target
 *            url이 적용되는 target 이름(현재 페이지는 '_self')
 * @param width
 *            팝업창 넓이
 * @param width
 *            팝업창 높이
 */
var fnPostPopup = function(url, params, target, width, height, scrollbar, resizable) {
    if ("_self"==target) {
        fnPostGoto(url, params, target);
    } else {
        scrollbar = scrollbar || "yes";
        resizable = resizable || "yes";

    	var popWidth = width;
    	var popHeight = height;
    	var availHeight = screen.availHeight -70;

    	if( popHeight > availHeight) {
    		popHeight = availHeight;
    	}


        var pos = centerWindow(popWidth, popHeight);

        var popup = window.open("", target, "width=" + popWidth + ",height=" + popHeight + ",left=" + pos[0] + ",top=" + pos[1] + ",status=no,scrollbars=" + scrollbar + ", resizable=" + resizable );

        fnPostGoto(url, params, target);
        if( isNotEmpty(popup) ){
        	popup.focus();
        }

        return popup;
    }
};

/**
 * Post 방식으로 Popup 다중 계단식으로 띄웁
 *
 * @param url
 *            submit할 주소
 * @param params
 *            Plain Object
 * @param target
 *            url이 적용되는 target 이름(현재 페이지는 '_self')
 * @param width
 *            팝업창 넓이
 * @param width
 *            팝업창 높이
 * @param offset
 *            팝업창 높이
 */
var fnPostPopupOffset = function(url, params, target, width, height, offset, scrollbar, resizable) {
    //console.log("offset",offset);
    if ("_self"==target) {
        fnPostGoto(url, params, target);
    } else {
        scrollbar = scrollbar || "yes";
        resizable = resizable || "yes";
        var pos = centerWindow(width, height);
        var xpos =  Number(pos[0]) + Number(offset);
        var ypos =  Number(pos[1]) + Number(offset);
        var popup = window.open("", target, "width=" + width + ",height=" + height + ",left=" + xpos + ",top=" + ypos + ",status=no, scrollbars=" + scrollbar + ", resizable=" + resizable );
        fnPostGoto(url, params, target);
        popup.focus();
        return popup;
    }
};

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//문자,숫자열 Format 관련 기능////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 콤마제거
 *
 * @param data
 */
var removeComma = function(data) {
    return data.replace(/,/g, "");
};

/**
 * 콤마 추가
 *
 * @param data
 */
var addComma = function(data) {
    var rtnVal = "";
    if (isNotEmpty(data)) {
        var arrNum = data.toString().split('.');

        if (arrNum.length >= 2) {
            rtnVal = arrNum[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "." + arrNum[1];
        } else {
            rtnVal = arrNum[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        }
    }
    return rtnVal;
};


/**
 * 예금주 식별번호 형식 지정
 *
 * @param cellvalue -
 *            Cell 값
 * @param options -
 *            Cell Option 정보
 * @param rowObject -
 *            Row Obejct
 */
var accteeNoFormat = function(cellvalue) {

    if (cellvalue.length === 7) { return cellvalue.substring(0, 6) + "-" + cellvalue.substring(6, 7); }
    return cellvalue;

};

/**
 * 사업자등록번호 형식 지정
 *
 * @param cellvalue -
 *            Cell 값
 * @param options -
 *            Cell Option 정보
 * @param rowObject -
 *            Row Obejct
 */
var bunsinessNoFormat = function(cellvalue, options, rowObject) {
    if (cellvalue.length === 10) { return cellvalue.substring(0, 3) + "-" + cellvalue.substring(3, 5) + "-" + cellvalue.substring(5); }
    return cellvalue;

};

/**
 * @Description : 사업자등록번호 체크
 *
 * @return : true, false
 */
var checkBizID = function(bizID) {
    // bizID는 숫자만 10자리로 해서 문자열로 넘긴다.
    var checkID = new Array(1, 3, 7, 1, 3, 7, 1, 3, 5, 1);
    var tmpBizID, i, chkSum=0, c2, remander;
    bizID = bizID.replace(/-/gi,'');
    for (i=0; i<=7; i++) chkSum += checkID[i] * bizID.charAt(i);
    c2 = "0" + (checkID[8] * bizID.charAt(8));
    c2 = c2.substring(c2.length - 2, c2.length);
    chkSum += Math.floor(c2.charAt(0)) + Math.floor(c2.charAt(1));
    remander = (10 - (chkSum % 10)) % 10 ;
    if (Math.floor(bizID.charAt(9)) === remander) return true ; // OK!
    return false;
};

/**
 * 법인사업자등록번호 형식 지정
 *
 * @param cellvalue -
 *            Cell 값
 * @param options -
 *            Cell Option 정보
 * @param rowObject -
 *            Row Obejct
 */
var corpBusinessNoFormat = function(cellvalue, options, rowObject) {
    if (cellvalue.length === 13) {return cellvalue.substring(0, 6) + "-" + cellvalue.substring(6, 13);}
    return cellvalue;

};


// 법인사업자 체크
var checkCorpBusiness = function(businessNo) {
    //console.log("businessNo" ,businessNo);
    // 법인번호 오류검증 공식
    // 법인번호에서 마지막 자리를 제외한
    // 앞에 모든 자리수를 1과 2를 순차적으로 곱한다.
    // 예) 1234567890123
    //     ************
    //     121212121212
    //     각각 곱한 수를 모든 더하고 10으로 나눈 나머지 수를 구한다.
    //     (각각더한수 % 10)
    //     나눈 나머지 수와 법인번호 마지막 번호가 일치하면 검증일치
    businessNo = businessNo.replace(/-/gi,'');
    if (businessNo.length !== 13){
        return false;
    }

    var arr_regno  = businessNo.split("");
    var arr_wt   = new Array(1,2,1,2,1,2,1,2,1,2,1,2);
    var iSum_regno  = 0;
    var iCheck_digit = 0;

    for (i = 0; i < 12; i++){
        iSum_regno +=  eval(arr_regno[i]) * eval(arr_wt[i]);
    }

    iCheck_digit = 10 - (iSum_regno % 10);
    iCheck_digit = iCheck_digit % 10;
    if (iCheck_digit != arr_regno[12]) {
        return false;
    }
    return true;
};

var checkBubinNo = function (bubinNo){
    var as_Biz_no = String(bubinNo);
    var isNum = true;
    var I_TEMP_SUM = 0 ;
    var I_TEMP = 0;
    var S_TEMP;
    var I_CHK_DIGIT = 0;

    if (bubinNo.length !== 13) {
        return false;
    }

    for (index01 = 1; index01 < 13; index01++) {
        var i = index01 % 2;
        var j = 0;

        if(i === 1){
            j = 1;
        }
        else if( i === 0){
            j = 2;
        }

        I_TEMP_SUM = I_TEMP_SUM + parseInt(as_Biz_no.substring(index01-1, index01),10) * j;
    }

    I_CHK_DIGIT= I_TEMP_SUM%10 ;

    if (I_CHK_DIGIT !== 0 ) {
        I_CHK_DIGIT = 10 - I_CHK_DIGIT;
    }
    if (as_Biz_no.substring(12,13) !== String(I_CHK_DIGIT)) {
        return false;
    }
    return true;
};




/**
 * 날짜 포멧 변경
 *
 * @param date
 */
var formatDate = function(txtDate) {
    // 공백인 경우는 정상으로 처리
    if (txtDate != "") {
        if (!isDate(txtDate)) {
            alert("날짜 형식이 맞지 않습니다.");
            return txtDate;
        }
        return txtDate.substring(0, 4) + "-" + txtDate.substring(4, 6) + "-" + txtDate.substring(6, 8);
    } else {
        return txtDate;
    }
};


/**
 * 날짜 포멧 변경
 *
 * @param date
 */
var removeDashFromDateText = function(txtDate) {
    // 공백인 경우는 정상으로 처리
    if (isEmpty( txtDate) ) {
        return "";
    } else {
        return txtDate.replace(/-/g, "");
    }
};


/**
 * 날짜 포멧 변경
 *
 * @param date
 */
var unFormatDate = function(strFormatDate) {
    // 공백인 경우는 정상으로 처리
    if (strFormatDate != "") {
        if (!isDate(strFormatDate)) {
            alert("날짜 형식이 맞지 않습니다.");
            return txtDate;
        }

        return strFormatDate.replace(/-/g, "");
    } else {
        return strFormatDate;
    }
};

/**
 * Date를 String으로 변경
 *
 * @param date
 * @param type [DATE:YYYYMMDD | TIME:HH24MISS | DATETIME:YYYYMMDDHH24MISS | default(DATETIME):YYYYMMDDHH24MISS]
 */
var fn_convertDateToString = function(date, type) {

    var strDt = null;

    if(type == 'DATE') {
        strDt = fn_paddingZero(date.getFullYear(), 4) + fn_paddingZero(date.getMonth() + 1, 2) + fn_paddingZero(date.getDate(), 2)
    } else if(type == 'TIME') {
        strDt = fn_paddingZero(date.getHours(), 2) + fn_paddingZero(date.getMinutes(), 2) + fn_paddingZero(date.getSeconds(), 2);
    } else {
        strDt = fn_paddingZero(date.getFullYear(), 4) + fn_paddingZero(date.getMonth() + 1, 2) + fn_paddingZero(date.getDate(), 2) + fn_paddingZero(date.getHours(), 2) + fn_paddingZero(date.getMinutes(), 2) + fn_paddingZero(date.getSeconds(), 2);
    }

    return strDt;
}

/**
 * 현재시간을 YYYYMMDDHH24MISS형태로 가져옴
 *
 * @returns
 */
var fn_getCurrentTime = function() {
    var toDay = new Date();

    var formatDate = fn_paddingZero(toDay.getFullYear(), 4) + fn_paddingZero(toDay.getMonth() + 1, 2) + fn_paddingZero(toDay.getDate(), 2) + fn_paddingZero(toDay.getHours(), 2) + fn_paddingZero(toDay.getMinutes(), 2) + fn_paddingZero(toDay.getSeconds(), 2);
    return formatDate;
};

/**
 * 현재시간을 YYYYMMDD형태로 가져옴
 *
 * @returns
 */
var fn_getCurrentDate = function() {
    var toDay = new Date();

    var formatDate = fn_paddingZero(toDay.getFullYear(), 4) + fn_paddingZero(toDay.getMonth() + 1, 2) + fn_paddingZero(toDay.getDate(), 2);
    return formatDate;
};

/**
 * 0값으로 자리수 메꾸기
 *
 * @param val
 * @param digits
 * @returns {String}
 */
var fn_paddingZero = function(val, digits) {
    var zeroVal = '';
    val = val.toString();

    if (val.length < digits) {
        for (var i = 0; i < digits - val.length; i++)
            zeroVal += '0';
    }
    return zeroVal + val;
};

/**
 * 숫자를 한글로 변환
 *
 * @param num
 * @returns hanStr
 */
var MoneyToHan = function(num) {
    arrayNum = new Array("", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구");
    arrayUnit = new Array("", "십", "백", "천", "만", "십만", "백만", "천만", "억", "십억", "백억", "천억", "조", "십조", "백조");
    arrayStr = new Array()
    len = num.length;
    hanStr = "";
    for (i = 0; i < len; i++) {
        arrayStr[i] = num.substr(i, 1)
    }
    code = len;
    for (i = 0; i < len; i++) {
        code--;
        tmpUnit = "";
        if (arrayNum[arrayStr[i]] != "") {
            tmpUnit = arrayUnit[code];
            if (code > 4) {
                if ((Math.floor(code / 4) == Math.floor((code - 1) / 4) && arrayNum[arrayStr[i + 1]] != "") || (Math.floor(code / 4) == Math.floor((code - 2) / 4) && arrayNum[arrayStr[i + 2]] != "")) {
                    tmpUnit = arrayUnit[code].substr(0, 1);
                }
            }
        }
        hanStr += arrayNum[arrayStr[i]] + tmpUnit;
    }
    return hanStr;
};

/**
 * 전화번호, 폰번호 입력 포멧 맞춤
 *
 * @param num ,type
 * @returns formatNum
 */
var PhoneFomatter = function (num,type){
    var formatNum = '';

    if(num.length==11){
        if(type==0){
            formatNum = num.replace(/(\d{3})(\d{4})(\d{4})/, '$1-****-$3');
        }else{
            formatNum = num.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
        }
    }else if(num.length==8){
        formatNum = num.replace(/(\d{4})(\d{4})/, '$1-$2');
    }else{
        if(num.indexOf('02')==0){
            if(type==0){
                formatNum = num.replace(/(\d{2})(\d{4})(\d{4})/, '$1-****-$3');
            }else{
                formatNum = num.replace(/(\d{2})(\d{4})(\d{4})/, '$1-$2-$3');
            }
        }else{
            if(type==0){
                formatNum = num.replace(/(\d{3})(\d{3})(\d{4})/, '$1-***-$3');
            }else{
                formatNum = num.replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3');
            }
        }
    }
    return formatNum;
};

/**
 * 이메일 형식 체크
 *
 * @param num ,type
 * @returns formatNum
 */
var EemailFormatCheck = function(strValue)
{
    var regExp = /[0-9a-zA-Z][_0-9a-zA-Z-]*@[_0-9a-zA-Z-]+(\.[_0-9a-zA-Z-]+){1,2}$/;

    //입력을 안했으면
    if(strValue.lenght == 0){
        alert("이메일을 입력하셔야 합니다.");
        return false;
    }

    //이메일 형식에 맞지않으면
    if (!strValue.match(regExp)){
        alert("이메일 형식에 맞지 않습니다.");
        return false;
    }
    return true;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Validation 체크 ////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*
* 숫자여부
*/
function isNumCheck(num)
{
    var objPattern =  /^[-]?\d?\d*\.?\d*$/;
    var str = num;
    if(str == "" || str == null) {
        return true;
    } else {
        if (!objPattern.test(str)) {
            return false;
        } else {
            return true;
        }
    }
}

/**
 * 비밀번호 유효성 검증
 *
 * @param uidObjId -
 *            사용자 아이디 Object ID
 * @param upwObjId -
 *            입력된 비밀번호 Object ID
 */
var checkPasswordValidate = function(uidObjId, upwObjId) {

    var uid = $("#" + uidObjId).val();
    var upw = $("#" + upwObjId).val();

    if (!/^[a-zA-Z0-9]{8,20}$/.test(upw)) {
        alert('비밀번호는 숫자와 영문자 조합으로 8자리 이상을 사용해야 합니다.');
        return false;
    }

    var chk_num = upw.search(/[0-9]/g);
    var chk_eng = upw.search(/[a-z]/ig);

    if (chk_num < 0 || chk_eng < 0) {
        alert('비밀번호는 숫자와 영문자를 혼용하여야 합니다.');
        return false;
    }

    if (/(\w)\1\1\1/.test(upw)) {
        alert('비밀번호에 같은 문자를 4번 이상 사용하실 수 없습니다.');
        return false;
    }

    if (upw.search(uid) > -1) {
        alert('ID가 포함된 비밀번호는 사용하실 수 없습니다.');
        return false;
    }

    return true;

};

/**
 * 값이 다른지 비교
 *
 * @param objId -
 *            Object Id
 * @param compareData -
 *            비교할 값
 */
var isNotEqual = function(objId, compareData) {
    if ($("#" + objId).val() == compareData) { return false; }
    return true;
};

/**
 * 값이 동일한지 비교
 *
 * @param objId -
 *            Object Id
 * @param compareData -
 *            비교할 값
 */
var isEqual = function(objId, compareData) {
    if ($("#" + objId).val() == compareData) { return true; }
    return false;
};

/**
 * 빈값체크 후 메시지 처리 및 포커스
 *
 * @param objId -
 *            Object Id
 * @param fieldText -
 *            필드명
 */
var isEmpty = function(fieldText) {
    if (null==fieldText || "undefined" ==  fieldText || fieldText.length == 0)	return true;
    else return false;
};

var isNotEmpty = function(fieldText) {
    if( isEmpty(fieldText) ) {
        return false;
    } else {
        return true;
    }
};

var isBlank = function(fieldText) {
    if (null==fieldText || "undefined" == fieldText || trimValue(fieldText).length == 0)	return true;
    else return false;
};

var isNotBlank = function(fieldText) {
    if( isBlank(fieldText) ) {
        return false;
    } else {
        return true;
    }
};


/**
 * 빈값체크 후 메시지 처리 및 포커스
 *
 * @param objId -
 *            Object Id
 * @param msgText -
 *            빈값일 경우 출력할 메세지
 */
var isEmptyMsg = function(objId, msgText) {

    if ($("#" + objId).val() == "") {
        alert(msgText);
        $("#" + objId).focus();
        return true;
    }
    return false;

};

/**
 * 값이 다른지 비교
 *
 * @param formId -
 *            Form Id
 * @param objId -
 *            Object Id
 * @param compareData -
 *            비교할 값
 */
var isNotEqualToArr = function(formId, objId, compareData, idx) {
    if ($("#" + formId + " #" + objId).eq(idx).val() == compareData) { return false; }
    return true;
};

/**
 * 배열객체의 특정 순번의 필드 값이 동일한지 비교
 *
 * @param formId -
 *            Form Id
 * @param objId -
 *            Object Id
 * @param compareData -
 *            비교할 값
 */
var isEqualToArr = function(formId, objId, compareData, idx) {
    if ($("#" + formId + " #" + objId).eq(idx).val() == compareData) { return true; }
    return false;
};

/**
 * 빈값체크 후 메시지 처리 및 포커스
 *
 * @param formId -
 *            Form Id
 * @param objId -
 *            Object Id
 * @param fieldText -
 *            필드명
 */
var isEmptyToArr = function(formId, objId, fieldText, idx) {

    if ($("#" + formId + " #" + objId).eq(idx).val() == "") {
        if (typeof fieldText !== 'undefined') {
            alert(fieldText + "을(를) 입력하세요.");
            $("#" + formId + " #" + objId).eq(idx).focus();
        }
        return true;
    }
    return false;
};

/**
 * 빈값체크 후 메시지 처리 및 포커스
 *
 * @param formId -
 *            Form Id
 * @param objId -
 *            Object Id
 * @param msgText -
 *            빈값일 경우 출력할 메세지
 */
var isEmptyMsgToArr = function(formId, objId, msgText, idx) {

    if ($("#" + formId + " #" + objId).eq(idx).val() == "") {
        alert(msgText);
        $("#" + formId + " #" + objId).eq(idx).focus();
        return true;
    }
    return false;

};

/**
 * 라디오 빈값체크 후 메시지 처리 및 포커스
 *
 * @param email -
 *            이메일주소
 */
var isEmptyRadio = function(objId, fieldText) {
    if ($('input[name=' + objId + ']:radio:checked').length == 0) {
        alert(fieldText );
        $("#" + objId).focus();
        return true;
    } else {
        return false;
    }
};

/**
 * 날짜가 맞는 날인지 확인
 *
 * @param date
 */
var isDate = function(txtDate) {
    var currVal = txtDate;

    if (currVal === '') return false;

    if (currVal.length === 10) {
        currVal = currVal.replace(/-/g, "");
    }

    if (currVal.length < 8) return false;

    var dtArray = currVal.match(/^[0-9]{4}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])/);

    if (dtArray == null) return false;

    // Checks for mm/dd/yyyy format. yyyymmdd
    dtYear = dtArray[3];
    dtMonth = dtArray[5];
    dtDay = dtArray[7];

    if (dtMonth < 1 || dtMonth > 12)
        return false;
    else if (dtDay < 1 || dtDay > 31)
        return false;
    else if ((dtMonth == 4 || dtMonth == 6 || dtMonth == 9 || dtMonth == 11) && dtDay == 31)
        return false;
    else if (dtMonth == 2) {
        var isleap = (dtYear % 4 == 0 && (dtYear % 100 != 0 || dtYear % 400 == 0));
        if (dtDay > 29 || (dtDay == 29 && !isleap)) return false;
    }
    return true;
};

/**
 * From / To 날짜 비교 '-' 가 들어있는 날짜여야 함
 *
 * @param date
 */
var betweenDate = function(fromDate, toDate) {
    if (!isDate(fromDate)) {
        alert("시작 날짜가 형식이 맞지 않습니다.");
        return false;
    }

    if (!isDate(toDate)) {
        alert("종료 날짜가 형식이 맞지 않습니다.");
        return false;
    }

    var fromDay = getDateObj(fromDate);
    var toDay = getDateObj(toDate);

    if ((toDay.getTime() - fromDay.getTime()) < 0)
        return false;
    else {

    }
    return true;
};


/**
 * From / To 시간 비교,  '-' 가 들어있는 날짜여야 함
 *
 * 날짜: _DD, 시간: _HH, 분: _MM 이 붙어 있는 id 임
 * @param date
 */
var betweenDt = function(fromDtId, toDtId) {
    var fromDate = $("#" + fromDtId + "_DD").val();
    var toDate = $("#" + toDtId + "_DD").val();

    if (!isDate(fromDate) ) {
        alert("시작 날짜가 형식이 맞지 않습니다.");
        return false;
    }

    if (!isDate(toDate) ) {
        alert("종료 날짜가 형식이 맞지 않습니다.");
        return false;
    }
    var fromArray = fromDate.split("-");
    var toArray = toDate.split("-");

    fromDate = new Date(fromArray[0], Number(fromArray[1])-1, fromArray[2], $("#" + fromDtId + "_HH").val(), $("#" + fromDtId + "_MM").val());
    toDate = new Date(toArray[0], Number(toArray[1])-1, toArray[2], $("#" + toDtId + "_HH").val(), $("#" + toDtId + "_MM").val());

    if((toDate.getTime() - fromDate.getTime()) < 0){
        return false;
    }
    return true;
};

/**
 * From / To 시간 비교,  '-' 가 들어있는 날짜여야 함
 *
 * 날짜: _DD, 시간: _HH, 분: _MM 이 붙어 있는 id 임
 * @param date
 */
var betweenDtStrObj = function(fromDtStr, toDtId) {
    var fromDate = fromDtStr;
    var toDate = $("#" + toDtId + "_DD").val();

    if (!isDate(fromDate) ) {
        alert("시작 날짜가 형식이 맞지 않습니다.");
        return false;
    }

    if (!isDate(toDate) ) {
        alert("종료 날짜가 형식이 맞지 않습니다.");
        return false;
    }
    var fromArray = fromDate.split("-");
    var toArray = toDate.split("-");

    fromDate = getDateObj(fromDate);
    toDate = new Date(toArray[0], Number(toArray[1])-1, toArray[2], $("#" + toDtId + "_HH").val(), $("#" + toDtId + "_MM").val());

    if((toDate.getTime() - fromDate.getTime()) < 0){
        return false;
    }
    return true;
};


/**
 * 숫자여부 true, false
 *
 * @param date
 */
function is_integer(varNum) {
    var reg = /^[-|+]?\d+$/;
    return reg.test(varNum);
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//기능 파일 //////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//var oneFileDownload = function(url) {
//    $('#downloadFrame').remove();
//    $('body').append('<iframe id="downloadFrame" style="display:none"></iframe>');
//    $('#downloadFrame').get(0).contentWindow.location.href = url;
//};
/**
 * 파일업로드 공통팝업
 * options 항목 target, url, callback, dataFormat, width, height, readOnly
 */
var openFileUplaod = function(options) {
    options = options || {};
    if (options["KEY_ID"] === 'undefined') {
        alert('필수값 keyId가 없습니다.');
        return;
    }
    var defaults = {TARGET:'FileUpload', url:getContextPath() + '/upload.do', CALLBACK:'fnCallBackFileUpload', DATA_FORMAT:'raw', width:800, height:500, READ_ONLY:false};
    for(var prop in defaults) {
        options[prop] = typeof options[prop] !== 'undefined' ? options[prop] : defaults[prop];
    }

    $.extend(options,fnGetMakeParams());

    options.FILE_MNG_KEY = $("#" + options.KEY_ID).val();
    fnPostPopup(options.url, options, options.TARGET, 600, 300);

};

var openAllFileDownload = function(options) {
    options = options || {};
    if (options["KEY_ID"] === 'undefined') {
        alert('필수값 KEY_ID가 없습니다.');
        return;
    }
    var defaults = {target:'downloadFrame', url:getContextPath() + '/download.do', callback:'fnCallBackFileUpload', dataFormat:'raw', width:800, height:500, readOnly:false};
    for(var prop in defaults) {
        options[prop] = typeof options[prop] !== 'undefined' ? options[prop] : defaults[prop];
    }
    options.fileMngKey = $("#" + options.KEY_ID).val();
    if (options.fileMngKey === '') {
        alert('다운로드 파일이 없습니다');
        return;
    }
    $('#downloadFrame').remove();
    $('body').append('<iframe id="downloadFrame" style="display:none"></iframe>');
    $('#downloadFrame').get(0).contentWindow.location.href = options.url + "?FILE_MNG_KEY=" + options.fileMngKey;
};

var oneFileDownload = function(filePath, fileNm) {
    if (filePath === undefined || filePath=="") {
        alert("파일 경로가 없습니다.");
        return false;
    }
    var url = getContextPath() + "/download.do";
    url += "?FILE_PATH=" + filePath;
    if (isNotBlank(fileNm)) {
        url += "&FILE_NM=" + fileNm;
    }

    $('#downloadFrame').remove();
    $('body').append('<iframe id="downloadFrame" style="display:none"></iframe>');
    $('#downloadFrame').get(0).contentWindow.location.href = url;
};

var displayFileUpload = function(options) {
    options = options || {};
    if (options["KEY_ID"] === undefined) {
        alert('필수 파라미터 keyId가 없습니다.');
        return;
    }
    var defaults = {url:getContextPath() + '/upload/file.do', CALLBACK:'fnCallBackFileUpload', DATA_FORMAT:'raw'};
    for(var prop in defaults) {
        options[prop] = typeof options[prop] !== 'undefined' ? options[prop] : defaults[prop];
    }
    options.FILE_MNG_KEY = $("#" + options.KEY_ID).val();

    var fnCallBack = window[options.CALLBACK];

    $.ajax({
        type:'GET',
        url:options.url,
        data:{FILE_MNG_KEY:options.FILE_MNG_KEY},
        dataType : 'json',
        success : function(result) {
            /*console.log(JSON.stringify(result));*/
            try {
                if (result && result.files) {
                    if (fnCallBack) {
                        var rdata;
                        if ("table" === options.DATA_FORMAT) {
                            var strTable	= "";
                            strTable	+= "<table class='tableStyle t_center topLineNo leftLine'>	";
                            strTable	+= "<colgroup>                                            	";
                            strTable	+= "	<col style='width:8%;'>                   			";
                            strTable	+= "	<col style='width:*;'>                   	 		";
                            strTable	+= "	<col style='width:15%;'>                  			";
                            strTable	+= "	<col style='width:10%;'>                  			";
                            strTable	+= "</colgroup>                                           	";
                            strTable	+= "<thead>                                               	";
                            strTable	+= "	<tr>                                              	";
                            strTable	+= "		<th>No</th>                                   	";
                            strTable	+= "		<th>파일명</th>                               	";
                            strTable	+= "		<th>등록일자</th>                             	";
                            strTable	+= "		<th>파일크기</th>                             	";
                            strTable	+= "	</tr>                                             	";
                            strTable	+= "</thead>                                              	";
                            strTable	+= "<tbody>                                               	";

                            if (0 === result.files.length) {
                                strTable	+= "<tr><td colspan='4' class='t_center' style='height:80px;'>파일이 없습니다.</td></tr>";

                            } else {
                                for(var i in result.files) {
                                    strTable	+= "	<tr>											";
                                    strTable	+= "		<td>"+(parseInt(i)+1)+"</td>				";

                                    var _downLinkUrl = "";
                                    if ("msie"==browser.name)
                                        _downLinkUrl = "<a href='javascript:oneFileDownload(\""+result.files[i].FILE_MNG_KEY+"\",\""+result.files[i].FILE_SEQ+"\")' title='"+result.files[i].name+"' style='color:blue;'>"+result.files[i].name+"</a>";
                                    else
                                        _downLinkUrl = "<a href='"+getContextPath() + "/" + result.files[i].url + "' title='"+result.files[i].name+"' style='color:blue;' download>"+result.files[i].name+"</a>";

                                    strTable	+= "		<td class='t_left'>"+_downLinkUrl+"</td>											";
                                    strTable	+= "		<td class='t_center'>"+formatDate(result.files[i].inputDt.substring(0, 8))+"</td>	";
                                    strTable	+= "		<td class='t_right'>"+formatFileSize(parseInt(result.files[i].size))+"</td>			";
                                    strTable	+= "	</tr>											";
                                }
                            }

                            strTable	+= "</tbody>												";
                            strTable	+= "</table>												";

                            rdata = strTable;
                        } else if ("raw" === options.DATA_FORMAT) {
                            rdata = result.files;
                        } else if("bTable" === options.DATA_FORMAT){
                            var table = "";
                            for(var i in result.files) {
                                table  += "<a href='javascript:oneFileDownload(\"" + getContextPath() + "/" + result.files[i].url + "\");' class='downLink'>"+result.files[i].name+"</a>";
                            }
                            rdata=table;
                        }
                        if (0 === result.files.length) {
                            $("#" + options.KEY_ID).val("");
                        }
                        fnCallBack(rdata);
                    }
                }
            } catch (e) {
                alert('첨부파일 조회 중 오류가 발생하였습니다.');
                //console.log(e.message);
            }
        },
        error : errorCallback
    });
};

/**
 * 파일 사이즈 표시
 */
var formatFileSize = function(bytes) {
    if (typeof bytes !== 'number') {
        return '';
    }
    if (bytes >= 1000000000) {
        return (bytes / 1000000000).toFixed(2) + ' GB';
    }
    if (bytes >= 1000000) {
        return (bytes / 1000000).toFixed(2) + ' MB';
    }
    return (bytes / 1000).toFixed(2) + ' KB';
};


/**
 * 문자열("YYYYMMDD" or "YYYYMMDDHH24MISS") 을 받아
 * javascript  Data 객체로 return 한다.
 */
var getDateObj = function( strTime) {

    if( isEmpty(strTime) ) {
        alert("날짜 형식이 맞지 않습니다.");
        return;
    }

    var strDt = strTime.replace(/-/g, "");
    if( strDt.length != 8 && strDt.length != 14 ) {
        alert("날짜 형식이 맞지 않습니다.");
        return;
    }

    if(strDt.length == 8 ) {
        var nYear = parseInt(strDt.substr(0,4));
        var nMonth = parseInt(strDt.substr(4,2));
        var nDay = parseInt(strDt.substr(6,2));

        return new Date(nYear, nMonth - 1, nDay);
    } else if(strDt.length == 14 ) {
        var nYear = parseInt(strDt.substr(0,4));
        var nMonth = parseInt(strDt.substr(4,2));
        var nDay = parseInt(strDt.substr(6,2));
        var nHour = parseInt(strDt.substr(8,2));
        var nMinute = parseInt(strDt.substr(10,2));
        var nSecond = parseInt(strDt.substr(12,2));

        //console.log( nYear + "-" + nMonth + "-" + nDay + "-" + nHour + "-" + nMinute + "-" + nSecond);
        return new Date(nYear, nMonth - 1, nDay, nHour, nMinute, nSecond);
    }
}


/**
 * Date 객체를 문자열 날짜("YYYYMMDDHH24MISS")로 return 한다.
 * nSize 값이 있을 경우 nSize 만큼 substr하여 return 한다.
 *
 */
var getDateStr = function( dateObj, nSize) {
    //console.log( "objTime = " + objTime);
    var nYear = dateObj.getFullYear();
    var nMonth = dateObj.getMonth() + 1;
    var nDate = dateObj.getDate();
    var nHour = dateObj.getHours();
    var nMinute = dateObj.getMinutes();
    var nSecond = dateObj.getSeconds();

    var strMonth = fn_paddingZero(nMonth, 2);
    var strDate = fn_paddingZero(nDate, 2);
    var strHour = fn_paddingZero(nHour, 2);
    var strMinute = fn_paddingZero(nMinute, 2);
    var strSecond = fn_paddingZero(nSecond, 2);


    var strDay = "" + nYear + strMonth + strDate;
    //console.log( strDate + "-" + strHour + "-" + strMinute);

    var retVal = strDay + strHour + strMinute + strSecond;

    if (isNotEmpty(nSize)) {
        retVal = retVal.substr(0,nSize);
    }
    return retVal;
}

/**
 * PC 시간과 DB 시간의 날짜 차이 계산 함수
 * dbTime : 화면 load 시 또는 ajax로 가져온 DB 시간 : YYYYMMDDHH24MISS
 */
var getDbTimeBalance = function(dbTime){
    var pcTime = new Date();
    var strCurrTime = dbTime;
    //console.log( "strCurrTime = " + strCurrTime);
    var serverTime = getDateObj(strCurrTime);
    //console.log( "pcTime = " + pcTime);
    //console.log( "serverTime = " + serverTime);
    var nTimeBalance = (serverTime - pcTime);
    return nTimeBalance;

};


/**
 * 현재 시간 구하기
 *
 * getDbTimeBalance 으로 구한 balance를 이용하여 현재시간 구하기
 *
 * return : Date 객체
 */
var getCalCurrentDbTime = function(nTimeBalance) {
    var nowPcTime = new Date();
    //console.log( "nowPcTime.getTime() = " + nowPcTime.getTime());

    var nTime = (nowPcTime.getTime() + nTimeBalance);
    //console.log( "nTime = " + nTime);
    return new Date(nTime);
};


/**
 * 현재 시간 구하기
 *
 * getDbTimeBalance 으로 구한 balance를 이용하여 현재시간 구하기
 *
 * return : Date 문자열
 */
var getCalCurrentDbTimeStr = function(nTimeBalance) {
    return getDateStr(getCalCurrentDbTime(nTimeBalance));
};

/**
 * 목표시간까지의 남은 시간을 구한다.
 *
 *
 */
var getRemainTime = function(strEndDt, nbalance) {
    var endTime = getDateObj(strEndDt).getTime();
    var now = getCalCurrentDbTime(nbalance).getTime();
    var remainTimes = (endTime - now) /1000 ;
    var remainText = "";

    // 남은 일수
    var day = Math.floor(remainTimes / (3600 * 24));
    var mod = remainTimes % (24 * 3600);
    //  남은시간
    var hour = Math.floor(mod / 3600);
    mod = mod % 3600;
    //    남은분
    var min = Math.floor(mod / 60);
    //    남은초
    var sec = Math.round(mod % 60);

    remainText = ( day  > 0) ? ("" + day + "일 ") : "";
    remainText = ( hour > 0) ? remainText + hour + "시간 " : (remainText.length > 0) ? remainText + hour + "시간 " : remainText;
    remainText = ( min  > 0) ? remainText + min + "분 " : (remainText.length > 0) ? remainText + min + "분 " : remainText;
    remainText = remainText + sec + "초"

    if (( sec <= 0 && remainText === "0초" ) || ( remainText === "" )) {
        //    이제그만...
        remainText = "종료";
    }
    return remainText;

};


var removeAllOption = function(objId) {
    $("#" + objId + " option").each(function() {
        $(this).remove();
    });
};


// String message 을 argument 를 치환한 문자열을 리턴한다.
var springMsgReplace = function(srcMsg, args) {
    var retMsg = srcMsg;
    if (isEmpty(args)) {
        return retMsg;
    } else {
        var argType = typeof(args);
        if (argType == "object") {
            for (var i = 0, nSize = args.length; i < nSize; i++) {
                retMsg = retMsg.replace("{" + i + "}", args[i]);
            }
        } else {
            retMsg = retMsg.replace( "{0}", args );
        }
        return retMsg;
    }
};



/**
 * Keypress시 input에 입력 key 값이 숫자만 입력 할 수 있도록 하는 함수
 * @param event
 */
function keyNumeric() {
    if ((event.keyCode >= 48 && event.keyCode <= 57) /*0 ~ 9*/
        || (event.keyCode === 8) /*Backspace key*/
        || (event.keyCode === 9) /*Tab key*/
        || (event.keyCode === 13) /*Enter key*/
        || (event.keyCode === 46) /*Delete key*/
        || (event.keyCode >= 37 && event.keyCode <= 40) /*arrow key*/){
        event.returnValue = true;
    } else {
        event.returnValue = false;
    }
}


/**
 *  Keypress시 input에 입력 key 값이 숫자와 '-' 만 입력 할 수 있도록 하는 함수
 * @param event
 */
var keyNumericDash = function() {
    //console.log("event.keyCode",event.keyCode);
    if ((event.keyCode >= 48 && event.keyCode <= 57)  ||
        (event.keyCode === 13) ||
        (event.keyCode === 45)){
        event.returnValue = true;
    } else {
// 		alert("숫자와 '-'만 입력 가능합니다.");
        event.returnValue = false;
    }
};

/**
 *  Keypress시 input에 입력 key 값이 숫자와 '-', '.' 만 입력 할 수 있도록 하는 함수
 * @param event
 */
var keyNumericDashPoint = function() {
    if((event.keyCode >= 48 && event.keyCode <= 57)  ||
        (event.keyCode === 13) ||
        (event.keyCode === 45) ||
        (event.keyCode === 46) )
    {
        event.returnValue = true;
    } else {
        alert("숫자와 '-', '.'만 입력 가능합니다.");
        event.returnValue = false;
    }
};


/**
 *  소수점 자리수 리턴
 * @param event
 */
var getPrecision = function(val) {
    var split = val.split(".");
    if (split[1] != null) {
        return split[1].length;
    } else {
        return 0;
    }
};


/**
 * 날짜 일 연산(날짜 문자열, 추가할 일수)
 */
var getAddDate = function(dateStr, addDay) {
	if( isNotEmpty(dateStr)) {
	    var dateObj = getDateObj(dateStr);
	    dateObj.setDate(dateObj.getDate() + addDay);
	    var textDate = getDateStr(dateObj, 8);
	    return formatDate(textDate);
	} else {
		return dateStr;
	}
};

/**
 * 날짜 월 더하기
 */
var getAddMonth = function(dateStr, addMonth) {
	if( isNotEmpty(dateStr)) {
	    var dateObj = getDateObj(dateStr);
	    dateObj.setMonth(dateObj.getMonth() + addMonth);
	    var textDate = getDateStr(dateObj, 8);
	    return formatDate(textDate);
	} else {
		return dateStr;
	}
};


/*
 * 첨부파일명만 리턴하는 함수
 */
function getAttachFileName(full_path, target, validYn) {

	if(window.FileReader){ // modern browser
		var file_nm = full_path[0].files[0].name;
	}
	else { // old IE
		var file_nm = full_path.val().split('/').pop().split('\\').pop(); // 파일명만 추출
	}

    // 확장자
    var ext = file_nm.substring(file_nm.lastIndexOf(".")+1, file_nm.length);

    if(isEmpty(validYn)){
	    if (isValidFileExt(ext)) {
	        $("#"+target).val(file_nm);
	    } else {
	    	$("#"+target).val();
	        alert("첨부할 수 없는 확장자를 가진 파일입니다.");
	    }
    }else{
    	$("#"+target).val(file_nm);
    }
}

$(function() {
    $(".yearselect").each(function(index, element) {
        var now = new Date();
        var nowYear = now.getFullYear();
        var e = $(element);
        var baseYear = 2015;
        try {
            baseYear = Number(e.data("baseyear"));
            if(isNaN(baseYear)) {
                baseYear = 2015;
            }
        } catch (e) {
            baseYear = 2015;
        }

        var selected = null;
        try {
            selected = Number(e.data("selected"));
        } catch (e) {
            selected = null;
        }

        for(var i = nowYear ; i >= baseYear ; i--) {
            var option = $("<option>");
            option.text(i);
            if(i === selected) {
                option.prop("selected", true);
            }
            e.append(option);
        }
    });
});

var cfnInitLogParam = function(global, options) {
    $.extend(options, global);
    var oparams = {scrNm1:'', scrNm2:'', scrNm3:'', scrNm4:'', btnId:'', btnNm:'', actNm:''};
    oparams.scrNm1 = options.scrNm1 || '';
    oparams.scrNm2 = options.scrNm2 || '';
    oparams.scrNm3 = options.scrNm3 || '';
    oparams.scrNm4 = options.scrNm4 || '';
    oparams.btnId = options.btnId || '';
    oparams.btnNm = options.btnNm || '';
    oparams.actNm = options.actNm || '';
    return oparams;
};
var cfnGlobalParam = function(g_params, scrName) {
    if (opener && opener.g_params) {
        $.extend(g_params, opener.g_params);
        if (!g_params.scrNm1) g_params.scrNm1 = scrName;
        else {
            if (!g_params.scrNm2) g_params.scrNm2 = scrName;
            else {
                if (!g_params.scrNm3) g_params.scrNm3 = scrName;
                else {
                    if (!g_params.scrNm4) g_params.scrNm4 = scrName;
                }
            }
        }
    } else {
        g_params.scrNm1 = scrName;
    }
    $.each(g_params, function(id, value) {
        //console.log("id =", id, ", value =", value);
    });
};
var cfnCheckParam = function(param) {
    if (param.srchEndDate1 && isNotEmpty(param.srchEndDate1)) param.srchEndDate1 = param.srchEndDate1 + " 23:59:59.999";
    if (param.srchEndDate2 && isNotEmpty(param.srchEndDate2)) param.srchEndDate2 = param.srchEndDate2 + " 23:59:59.999";
    if (param.srchEndDate2 && isNotEmpty(param.srchEndDate2)) param.srchEndDate2 = param.srchEndDate2 + " 23:59:59.999";
    return param;
};

var cfnChangeMode = function(options) {
    'use strict';
    var mode = options.mode || 'display'; // display, modify
    var excludes = options.excludes || []; // display.. 제외 대상
    var includes = options.includes || []; // modify.. readonly 처리 대상

    //console.log('excludes', excludes);
    if (mode === 'display') {
        $(':input').each(function() {// input
            var type = $(this).attr('type');
            var tag = $(this).prop('tagName');
            var index = jQuery.inArray($(this).attr('id'), excludes);
            if (0 > index) {
                if (!(type && tag === "INPUT" && type.toUpperCase() === 'HIDDEN')) {
                    // 공통 처리
                    $(this).attr('readonly', true);
                    $(this).removeClass('req_input');
                    $(this).addClass('inputView');
                    // datepicker 클래스가 있는지 체크하여 제거한다.
                    if (type) {
                        if (type.toUpperCase() === 'RADIO') {// 라디오버튼 일때
                            //console.log("RADIO");
                            $(this).hide();
                            if (!$(this).is(':checked')) {try{$(this)[0].nextSibling.nodeValue = "";}catch(e){}}
                        } else if (type.toUpperCase() === 'CHECKBOX') {
                            //console.log("CHECKBOX");
                            $(this).hide();
                            if (!$(this).is(':checked')) {try{$(this)[0].nextSibling.nodeValue = "";}catch(e){}}
                        }
                    } else {
                        if (tag === "SELECT") {
                            //console.log("SELECT");
                            $(this).hide();
                            try {
                                if ("" !== $(this)[0].options[$(this)[0].selectedIndex].value) {
                                    $(this).after("&nbsp;&nbsp;" + $(this)[0].options[$(this)[0].selectedIndex].text);
                                }
                            } catch (e) {
                            }
                        } else if (tag === "TEXTAREA") {
                            //console.log("TEXTAREA");
                            //$(this).css('border', 'solid 1px #c6c6c6');
                        }
                    }
                }
            }
            //console.log('id =', $(this).attr('id'), ', index =', index, ', type =', $(this).attr('type'), ', tag =', $(this).prop('tagName'));
        });

        // file upload
        $('input[id$="_uploadFile"]').hide();
        $('.file-upload').hide();
        $('input[id$="_setReadOnlyFlag"]').val('Y');

        // datepicker
        $(".datepicker").each(function() {
            $(this).hide();
            $(this).after($(this).val());
        });
        $(".ui-datepicker-trigger").hide();


        // datetimepicker
        $(".datetimepicker").each(function() {
            $(this).hide();
            $(this).after($(this).val());
        });
        $(".xdsoft_datetimepicker").hide();

        // inline popup button
        $(".ibtn").each(function () {
            $(this).hide();
        });

        // auth-save button
        $(".auth").each(function () {
            var index = jQuery.inArray($(this).attr('id'), excludes);
            if (0 > index) {
                $(this).hide();
            }
        });
    } else {
        $(':input').each(function() {// input
            var type = $(this).attr('type');
            var tag = $(this).prop('tagName');
            var index = jQuery.inArray($(this).attr('id'), excludes);
            if (0 > index) {
                if (!(type && tag === "INPUT" && type.toUpperCase() === 'HIDDEN')) {
                    // 공통 처리
                    $(this).attr('disabled', false);
                    $(this).attr('readonly', false);
                    $(this).removeClass('inputView');
                    if (type) {
                        if (type.toUpperCase() === 'RADIO') {// 라디오버튼 일때
                            //console.log("RADIO");
                        } else if (type.toUpperCase() === 'CHECKBOX') {
                            //console.log("CHECKBOX");
                        }
                    } else {
                        if (tag === "SELECT") {
                            //console.log("SELECT");
                        } else if (tag === "TEXTAREA") {
                            //console.log("TEXTAREA");
                            //$(this).css('border', 'solid 1px #c6c6c6');
                        }
                    }
                }
            } else { //excludes
                $(this).attr('disabled', false);
                $(this).attr('readonly', true);
                $(this).addClass('readonly');
            }
            //console.log('id =', $(this).attr('id'), ', index =', index, ', type =', $(this).attr('type'), ', tag =', $(this).prop('tagName'));
        });
    }
};
//트리 텍스트 검색
var cfnSearchTree = function(treeView, scrName) {
	var ret = treeView.getDataProvider().searchData({fields:["deptNm"], value:scrName, wrap:true});
	if (ret) {
	    var rowId = ret.dataRow;
	    var parents = treeView.getDataProvider().getAncestors(rowId);
	    if (parents) {
	        for (var i = parents.length - 1; i >= 0 ; i--) {
	            treeView.expand(treeView.getItemIndex(parents[i]));
	        }
	        treeView.setCurrent({itemIndex:treeView.getItemIndex(rowId), fieldIndex:ret.fieldIndex});
	    }
	}
};




/**
 * 콤보 Option 모두 제거
 */
var removeAllComboOption = function(objId) {
	$("#" + objId + " option").each(function() {
		$(this).remove();
	});
};


/**
 * 처번째 Option 제외하고 나머지 제거
 */
var removeAllComboOptionExcludeFirstCd = function(objId) {
	$("#" + objId + " option").each(function(idx) {
		if( idx != 0 ) {
			$(this).remove();
		}
	});
};


/**
 * 콤보 option 생성
 */
var fnMakeComboOption = function(objId, codeList, keyCol, valueCol, firstCode, firstValue, selectedValue) {

   if (isNotEmpty(codeList)) {

	   removeAllComboOption(objId);

	   if( firstCode != null || firstCode == "undefined") {

		   $("#" + objId).append("<option value='" + firstCode + "' selected='selected' >" + firstValue + "</option>");
       }

       for ( var i = 0; i < codeList.length; i++) {
            var codeObj = codeList[i];

            var strKey = codeObj[keyCol];
            var strVal = codeObj[valueCol];
            /*
            if( isNotEmpty(keyCol)) {
            	strKey = codeObj[keyCol];
            }

            if( isNotEmpty(valueCol)) {
            	strVal = codeObj[valueCol];
            }
            */
            if (strKey == selectedValue) {
            	$("#" + objId).append("<option value='" + strKey + "' selected='selected'>" + strVal + "</option>");
            } else {
            	$("#" + objId).append("<option value='" + strKey + "'>" + strVal + "</option>");

            }
        }
    }
};



//grid dropdown 코드 설정
function fnSetGridDropdownValue(grid, colName, codeList, keyCol, valueCol, firstCode, firstValue) {


	//console.log( "fnSetGridDropdownValue");
	var localValues = new Array();
	var localLabels = new Array();

	if (isNotEmpty(codeList)) {

	   if( firstCode != null || firstCode == "undefined") {

		   localValues.push(firstCode);
		   localLabels.push(firstValue);
       }

       for ( var i = 0; i < codeList.length; i++) {
            var codeObj = codeList[i];

            //console.log( "fnSetGridDropdownValue codeObj =" + JSON.stringify( codeObj));
            var strKey = codeObj[keyCol];
            var strVal = codeObj[valueCol];

            localValues.push(strKey);
 		   	localLabels.push(strVal);
        }
    }

	var column = grid.columnByName(colName);
	column.values = localValues;
	column.labels = localLabels;
	grid.setColumn(column);
}



// 숫자로 변환
function fnToNumber(value) {
	var retVal = Number( value);

	if( isNaN( retVal) ) {
		return 0;
	}

	return retVal;
}


// 절사
function fnFloor( value, precision ) {

	var retVal = value;

	if( isNotEmpty( precision) ) {
		if( precision > 0 ) {
			retVal = Math.floor( fnToNumber(value) * Math.pow( 10, precision)) / Math.pow(10, precision);
		} else if ( precision == 0 ) {
			retVal = Math.floor( fnToNumber(value));
		} else {
			retVal = Math.floor( fnToNumber(value) / Math.pow( 10, Math.abs(precision))) * Math.pow(10, Math.abs(precision));
		}
	} else {
		retVal = Math.floor( fnToNumber(value));
	}
	return retVal;
}

// 법인세 여부
function isCorpTax(fgTax) {
	var retVal = false;

	if( fgTax == '14' || fgTax == '15') {  // 14:영세(세금계산서), 15:영세(수출)
		retVal = true;
	}

	return retVal;
}



// 원화 환산 금액 계산
function fnCalculExchangeAmt(cdExch, rtRate, exAmt) {

	var retVal = fnToNumber(exAmt) * fnToNumber(rtRate);

	if( isNotEmpty(cdExch) ) {
		if( cdExch == '002' || cdExch == 'JPY' || cdExch == 'IDR' || cdExch == 'VND' ) {
			retVal = retVal / 100;
		}
	}

	return retVal;
}

// 원화 -> 외화
function fnCalculExchangeKRWAmt(cdExch, rtRate, amt) {

	var retVal = fnToNumber(amt) / fnToNumber(rtRate);

	if( isNotEmpty(cdExch) ) {
		if( cdExch == '002' || cdExch == 'JPY' || cdExch == 'IDR' || cdExch == 'VND' ) {
			retVal = retVal * 100;
		}
	}

	return retVal;
}

function pop_open_modal(obj,wid,hei){

	var dHeight = $(document).height();
	var dWidth = $(document).width();
	var sHeight = $(document).scrollTop();
	var objWidth = wid;

	$('<div class="popModal" style="position:fixed; top:40px;z-index:200000;"><p class="dragHandler" style="width:91%; height:38px; position:absolute; cursor:move"></p><iframe id="test" src="'+obj+'" width="'+wid+'" height="'+hei+'" frameborder="0"></iframe></div>').appendTo('body');
	$('body').append('<div class="popBg pngFix" style="background:url(/static/images/common/bg_modal.png) repeat left top;position:absolute;left:0;top:0;z-index:2000;"></div>');
	$('.popBg').css({ width: dWidth, height: dHeight });

	var wHeight = $(window).height();
	var posX = $('.popBg').width();
	var posY = $('.popBg').height();
	var posTop = sHeight + ((wHeight - hei) / 2); //(sHeight + wHeight / 2) - posY / 2;
	if(posTop < 50){posTop = 50};

	$('.popModal').show()
//	$('.popModal').css({top: '-1600px',zIndex:'5000',width:wid,height:hei});
	$('.popModal').css("left", (posX - wid) / 2)

	// 팝업장 드래그 엔 드롭
	$('.popModal').draggable({
		handle: "p.dragHandler",
		containment: "body",
		scroll: false
	});

	// 팝업창 리사이징
	/*$("#test").load(function(){
		setTimeout(function(){
			var heiSize = $('#test').contents().find('#popWrap').height();
			heiSize = heiSize + 2;
			$("#test, .popModal").css("height",+heiSize);
			//console.log(heiSize);
		},300);
	});*/


} //모달팝업 오픈

function pop_close_modal(){
    $("body .popBg:last").css('background', 'none').remove();
    $('.popModal:last').remove();
} //모달팝업 닫기



//ORACLE NVL 기능
function nvl(str, defaultStr) {
	if(typeof str == "undefined" || str == null || str == "") {
		return defaultStr;
	} else {
		return str;
	}
}



//PRINT OPRION
function printOption() {
	var Dwidth  = parseInt(document.body.scrollWidth);
	var Dheight = parseInt(document.body.scrollHeight);

	var divEl = document.createElement("div");	//document안의 "div"의 객체,변수,메소드를
	divEl.style.position = "absolute";			//divEI에게 넘겨준다. 여기서 "div"는 popup창 body전체
	divEl.style.left = "50px";
	divEl.style.top = "150px";
	divEl.style.width = "100%";
	divEl.style.height = "100%";
	document.body.appendChild(divEl);			//appendChild : body에다가 divEI를 적용시키겠다

	window.resizeBy(Dwidth-divEl.offsetWidth, Dheight-divEl.offsetHeight);
	document.body.removeChild(divEl);
}

//숫자와 '-' 만 입력 허용
function onlyNumberInput( Ev ) {
	if(window.event) {// IE코드
		var code = window.event.keyCode;
	} else {// 타브라우저
		var code = Ev.which;
	}


	if ((code > 34 && code < 41) || (code > 47 && code < 58) || (code > 95 && code < 106) || code == 8 || code == 9 || code == 13 || code == 46 || code == 189) {
		window.event.returnValue = true;
		return;
	}

	if(window.event) {
		window.event.returnValue = false;
	} else {
		Ev.preventDefault();
	}
}


//숫자만 입력
function onlyNumberInput2( Ev ) {
	if(window.event) {// IE코드
		var code = window.event.keyCode;
	} else {// 타브라우저
		var code = Ev.which;
	}

	if ((code > 34 && code < 41) || (code > 47 && code < 58) || (code > 95 && code < 106) || code == 8 || code == 9 || code == 13 || code == 46) {
		window.event.returnValue = true;
		return;
  }

  if(window.event) {
  	window.event.returnValue = false;
  } else {
  	Ev.preventDefault();
  }
}

function getLength2Bytes(chkstr) {
    var j, lng = 0;

    for (j=0; j<chkstr.length; j++){
        if (chkstr.charCodeAt(j) > 255)
        {
            ++lng;
        }

		++lng;
    }

    return lng;
}

/**
 * 날짜 유효성 검사
 * @param d
 * @returns {Boolean}
 */
function isValidDate(d) {
	if(!isDateFormat(d)) {
		return false;
	}

	var month_day = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

	var year = Number(d.substr(0, 4));
	var month = Number(d.substr(4, 2));
	var day = Number(d.substr(6));

	// 날짜가 0이면 false
	if(day == 0) {
		return false;
	}

	var isValid = false;

	// 윤년일때
	if(isLeaf(year)) {
		if(month == 2) {
			if(day <= month_day[month-1] + 1) {
				isValid = true;
			}
		} else {
			if(day <= month_day[month-1]) {
				isValid = true;
			}
		}
	} else {
		if(day <= month_day[month-1]) {
			isValid = true;
		}
	}
	return isValid;
}

/**
 * 날짜포맷에 맞는지 검사
 */
function isDateFormat(d) {
	if (d.length != 8) {
		return false;
	}
	var df = /^(19[7-9][0-9]|9999|20\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$/;

	return d.match(df);
}

/**
 * 윤년여부 검사
 */
function isLeaf(year) {
	var leaf = false;

	if(year % 4 == 0) {
		leaf = true;

		if(year % 100 == 0) {
			leaf = false;
		}

		if(year % 400 == 0) {
			leaf = true;
		}
	}
	return leaf;
}

/**
 * 숫자형으로 캐스팅
 */
var toNumber = function(intObj) {
	if (isEmpty(intObj) == true) {
		return 0;
	} else {
		intObj = removeComma(intObj);
		if (isNaN(intObj) == true) {
			return 0;
		} else {
			return Number(intObj);
		}
	}
}

function initView() {

	if($(".card-body").length > 0 || $(".popWrap").length > 0 ) contentsResize();

	$(window).on("resize", function() {
		contentsResize();
	});
}


function contentsResize(contentId) {
	//console.log( "contentsResize contentId =" + contentId);
	if (_dynamicResizeTarget != null && _dynamicResizeTarget.length > 0 ) {

		if($(".card-body").find(".contentBlock").length > 0 ) {
			try{
				customResize();
			} catch( e) {
			}
		} else {
		   	var $contents = null;

		   	var header_height = 0;
		   	var footer_height = 0;
		   	var extend_size = 0;

            var extend_height = 0;

		   	if( contentId != undefined && contentId != null ) {
		   		$contents = $("#" + contentsId);
		   	} else {

		   		if($(".card-body").length > 0 ) {
		   			$contents = $(".content");
		   			header_height = $(".page-header").height();
		   			extend_height = 120;

		   		}
		   		/*else if($(".card-body").length > 0) {
		   			$contents = $(".popCont");
		   			header_height = $(".popHead").height();
		   			extend_height = 30;
		   		}*/

		   	}

		   	//console.log( "header_height = " + header_height);
		   	//console.log( "extend_height = " + extend_height);

		   	var $gridArea = $(".rgrid-area");
		   	if( $gridArea.length > 0 ) {

				var body_height = $(window).height();


                //console.log("body_height : " + body_height);

				var gridAreaHeight = body_height - header_height - footer_height - (($gridArea.offset() !== undefined ) ? $gridArea.offset().top : 0) -30;
				//console.log( "gridAreaHeight = " + gridAreaHeight);

				gridAreaHeight += extend_height;

				//console.log( "gridAreaHeight = " + gridAreaHeight);
				$gridArea.css("height", gridAreaHeight);
				resizeGrid();
		   	}
		}
	}
}


function initViewHorizental(upRate) {
	if($(".content").length > 0 || $(".popCont").length > 0 ) contentsResizeHorizental(upRate);

	$(window).on("resize", function() {
		contentsResizeHorizental(upRate);

	});
}


function contentsResizeHorizental(upRate, contentId) {
	//console.log( "contentsResize contentId =" + contentId);
	if (_dynamicResizeTarget != null && _dynamicResizeTarget.length > 0 ) {
	   	var $contents = null;

	   	var header_height = 0;
	   	var footer_height = 0;
	   	var extend_size = 0;
	   	if( contentId != undefined && contentId != null ) {
	   		$contents = $("#" + contentsId);
	   	} else {

	   		if($(".content").length > 0 ) {
	   			$contents = $(".content");
	   			header_height = $(".header-wrap").height();
	   			extend_height = 49;
	   		}
	   	}

	   	var isResizeGrid = false;
	  	if($contents.find(".contentHorizentalBlock").length > 0 ) {
			var body_height = $(window).height();

			var footer_height = 0
			if( $(".footer-wrap").length > 0 ) {
				footer_height = $(".footer-wrap").height();
			}

			var $contentBlock = $contents.find(".contentHorizentalBlock").eq(0);
			var total_height = body_height - header_height - footer_height - (($contentBlock.offset() !== undefined ) ? $contentBlock.offset().top : 0);
			total_height += extend_height;

			var height1 = Math.ceil( total_height * ( upRate /100.0) );
			var height2 = total_height - height1;

			//console.log( "contentHorizentalBlock height1 = " + height1);
			//console.log( "contentHorizentalBlock height2 = " + height2);
			var blockHeight = [height1, height2];

			$contents.find(".contentHorizentalBlock").each(function(idx) {
				var obj = $(this);
				var gridHeight = blockHeight[idx];

				var titleHeight = 0;
				if( obj.find(".sub_tit").length > 0 ) {
					titleHeight = 43;
				}
				//console.log( "contentHorizentalBlock titleHeight = " + titleHeight);
				gridHeight = gridHeight - titleHeight;
				//console.log( "contentHorizentalBlock gridHeight = " + gridHeight);
				if( obj.find(".rgrid-area").length > 0 ) {
					obj.find(".rgrid-area").css("height", gridHeight);
				}
			});

			isResizeGrid = true;
			//resizeGrid();
	  	}

	  	if($contents.find(".contentBlock").length > 0 ) {

			var body_height = $(window).height();

			var footer_height = 0
			if( $(".footer-wrap").length > 0 ) {
				footer_height = $(".footer-wrap").height();
			}

			var $contentBlock = $contents.find(".contentBlock").eq(0);
			var total_height = body_height - header_height - footer_height - (($contentBlock.offset() !== undefined ) ? $contentBlock.offset().top : 0);
			total_height += extend_height;

			$contents.find(".contentBlock").each(function(idx) {
				var obj = $(this);
				var gridHeight = total_height;

				var titleHeight = 0;
				if( obj.find(".sub_tit").length > 0 ) {
					titleHeight = obj.find(".sub_tit").outerHeight() + 7;

				}
				//console.log( "contentlBlock titleHeight = " + titleHeight);
				gridHeight = gridHeight - titleHeight;
				//console.log( "contentlBlock gridHeight = " + gridHeight);
				if( obj.find(".rgrid-area").length > 0 ) {
					obj.find(".rgrid-area").css("height", gridHeight);
				}
			});

			isResizeGrid = true;
	  	}

	  	if( isResizeGrid == true) {
	  		resizeGrid();
	  	}
	}
}

/**
 * 날짜 길이 맞추기
 */
var formatLen = function(str) {
	return str = (""+str).length<2 ? "0"+str : str;
}

/**
 * 오늘 날짜
 */
var getToDay = function(delimiter) {
	return getDateFormatData("today", 0, delimiter);
};

/**
 * 날짜 포맷 맞추기
 */
var getDateFormatData = function(flag, cnt, delimiter) {
	if (delimiter == undefined) delimiter = "-";

	var dateObj = new Date();

	var y1 = dateObj.getFullYear();
	var m1 = dateObj.getMonth();
	var d1 = dateObj.getDate();

	if (flag == "y") {
		dateObj.setYear(y1 + cnt);

	} else if (flag == "m") {
		dateObj.setMonth(m1 + cnt);

	} else if (flag = "d") {
		dateObj.setDate(d1 + cnt);
	}
	y1 = dateObj.getFullYear();
	m1 = dateObj.getMonth() + 1;
	d1 = dateObj.getDate();

	return y1 + delimiter + this.formatLen(m1) + delimiter + this.formatLen(d1);
};

/**
 * 기존 조회 조건을 가지고 List 로 보내기 위한 셋팅
 */
var fnSetSearchParams = function(reqParam){
	$('#DIV_SRCH_PARAMS').remove();
    $('body').append("<div id='DIV_SRCH_PARAMS' style='display:none;'></div>");
	var strTr = "";
	$.each(reqParam, function(reqNm, reqValue) {
		if( reqNm.indexOf("SRCH_") > -1 && reqValue != "") {
			strTr  += "<input type='hidden' id="+reqNm+" value="+reqValue+" />";
		}
		//현재 Page 정보 Setting
		if( reqNm == ("page") && reqValue != "") {
			strTr  += "<input type='hidden' id="+reqNm+" value="+reqValue+" />";
		}

		//현재 Page 정보 Setting
		if( reqNm == ("pageSize") && reqValue != "") {
			strTr  += "<input type='hidden' id="+reqNm+" value="+reqValue+" />";
		}
    });

	if(strTr == ""){
		$('#DIV_SRCH_PARAMS').remove();
	}else{
		$("#DIV_SRCH_PARAMS").empty().append(strTr);
	}
};


var isNumberKey = function(evt) {
    var charCode = (evt.which) ? evt.which : event.keyCode;

    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57))
        return false;

    // Textbox value
    var _value = event.srcElement.value;

    // 소수점(.)이 두번 이상 나오지 못하게
    var _pattern0 = /^\d*[.]\d*$/; // 현재 value값에 소수점(.) 이 있으면 . 입력불가
    if (_pattern0.test(_value)) {
        if (charCode == 46) {
            return false;
        }
    }

    // 소수점 둘째자리까지만 입력가능
    var _pattern2 = /^\d*[.]\d{2}$/; // 현재 value값이 소수점 둘째짜리 숫자이면 더이상 입력 불가
    if (_pattern2.test(_value)) {
        alert("소수점 둘째자리까지만 입력가능합니다.");

        return false;
    }

    return true;
}