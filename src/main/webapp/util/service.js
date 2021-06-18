sap.ui.define(
    ["jquery.sap.global"],
    function(jQuery){
        return {
            callService: function(sUrl, sMethod, sData){
                return new Promise(function(resolve, reject){
                    switch(sMethod.toUpperCase()){
                        case "GET":
                            jQuery.ajax(sUrl,{
                                type: 'GET',
                                dataType:'json',
                                data: JSON.stringify(sData),
                                contentType : 'application/json;charset=UTF-8',
                                success:function(data){
                                    console.log(data);
                                    resolve(data);
                                },
                                error:function(err){
                                    reject(err);
                                }
                            });
                            break;
                        case "POST":
                            jQuery.ajax(sUrl,{
                                type: 'POST',
                                dataType:'json',
                                data: JSON.stringify(sData),
                                contentType : 'application/json;charset=UTF-8',
                                success:function(data){
                                    console.log(data);
                                },
                                error:function(err){

                                }
                            });
                            break;
                    }
                });
            }
        }
    }
);