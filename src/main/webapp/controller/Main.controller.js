sap.ui.define(
    ["sap/ui/core/mvc/Controller",
    "jquery.sap.global",
    "ecbank/util/service",
    "sap/m/MessageBox"],

    function(Controller, jQuery, service, MessageBox){
        return Controller.extend("ecbank.controller.Main",{
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


            console.log("userLoad1 : " + JSON.stringify(userLoad));
            console.log("userLoad2 : " + userLoad);

            service.callService("/login.do", "POST", userLoad).then(function(){

                MessageBox.confirm("Login Succes");

            }).catch(function(oError){
                MessageBox.error("Login Fail");
            });
        }
    });
});