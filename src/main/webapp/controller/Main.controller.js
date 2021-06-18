sap.ui.define(["sap/ui/core/mvc/Controller",
               "jquery.sap.global"],
     function(Controller, jQuery){
        return Controller.extend("ecbank.controller.Main",{
            onLoadData: function(){
                var str = "{'userId' : 'yoonyoon', 'age': 20}"; 
                jQuery.ajax("/vendor",{

                    type: 'POST',
                    dataType:'json',
                    data: JSON.stringify(str),
                    contentType : 'application/json;charset=UTF-8',
                    beforeSend : function(xmlHttpRequest) {
                        xmlHttpRequest.setRequestHeader("ajax", "true");
                    },
                    success:function(data){
                        console.log(data);
                    },
                    error:function(err){

                    }
                });
            }
    });
})