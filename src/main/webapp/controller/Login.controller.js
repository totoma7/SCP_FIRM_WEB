sap.ui.define(
    ["sap/ui/core/mvc/Controller",
    "jquery.sap.global",
    "ecbank/util/service",
    "sap/m/MessageBox",
    "ecbank/util/common"],

    function(Controller, jQuery, service, MessageBox, common){
        return Controller.extend("ecbank.controller.Login",{
            onInit: function(){
                var oModel = new sap.ui.model.json.JSONModel();

                // init id set
                oModel.setData({
                    "USER" :{
                        "ID":"",
                        "PASS":""
                    },
                });

                sap.ui.getCore().setModel(oModel);

            },
            onLogin: function(){

            var oModel = sap.ui.getCore().getModel();
            var userLoad = oModel.getProperty("/USER");

            service.callService("/login.do", "POST", userLoad).then(function(data){

                var retCode = data.fields.RET_CODE;
                var retMsg = data.fields.RET_MSG;

                    if( "000" == retCode) {

                        var f = document.createElement('form');

                        f.setAttribute('method', 'post');
                        f.setAttribute('action', '/main.do');
                        document.body.appendChild(f);
                        f.submit();

                    } else {
                        MessageBox.error(retMsg);
                    }

            }).catch(function(oError){
                MessageBox.error("Login Fail");
            });
        }
    });
});