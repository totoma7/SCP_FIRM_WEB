<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>Sample Page</title>
</head>
<script id="sap-ui-bootstrap" src="https://sapui5.hana.ondemand.com/resources/sap-ui-core.js"
        data-sap-ui-libs="sap.m"
        data-sap-ui-theme="sap_fiori_3"
        data-sap-ui-resourceroots='{
            "ecbank" : "../"
        }'>
</script>

<script>
    /*
    var oBtn = new sap.m.Button({
        text: "Click Buttom",
        press: function(){
            alert('welcom to ui5');
        }
    });
    */


    var oMain = new sap.ui.view({
        viewName: "ecbank.view.Main",
        type: "XML"
    });

    oMain.placeAt("content");
</script>
<body class="sapUiBody sapUiSizeCozy">
    <div id="content"></div>
</body>
</html>