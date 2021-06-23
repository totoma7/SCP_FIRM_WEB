sap.ui.define([
    "sap/ui/Device",
    "sap/ui/core/mvc/Controller",
    "sap/ui/model/json/JSONModel",
    "sap/ui/core/format/DateFormat",
    "sap/m/Popover",
    "sap/m/Button",
    "sap/m/library",
    "sap/ui/thirdparty/jquery",
    "ecbank/util/service"
], function (Device, Controller, JSONModel, Popover, Button, library, service) {
    "use strict";

    var ButtonType = library.ButtonType,
        PlacementType = library.PlacementType;

    return Controller.extend("ecbank.controller.Main", {

        onInit: function () {
            var oModel = new JSONModel(sap.ui.require.toUrl("ecbank/model/data.json"));
            this.getView().setModel(oModel);
            this._setToggleButtonTooltip(!Device.system.desktop);

            // Sample DB Search
            var oJSONModel = this.initSampleDataModel();
            this.getView().setModel(oJSONModel);
        },

        initSampleDataModel : function() {
            var oModel = new JSONModel();

            jQuery.ajax("/trnBookListSample.do",{
                type: 'POST',
                dataType:'json',
                contentType : 'application/json;charset=UTF-8',

                // 추후 param data 처리 어떻게 할지 js 확인 할것 common.js - fnGetParams 참조 하면 좋을 듯
                data: JSON.stringify({"startDate": "1", "endDate": "2"}),
                beforeSend : function(xhr) {
                    xhr.setRequestHeader("ajax", "true");
                    xhr.setRequestHeader("__CSRF_TOKEN__", "1111" );
                },

                success:function(data){

                    console.log("data : " + JSON.stringify(data.rows));

                    if (typeof data.rows != 'undefined') {
                        oModel.setData(data);
                    }
                },
                error: function() {
                    console.error("failed to load json");
                }
            });

            return oModel;
        },

        onItemSelect: function (oEvent) {
            var oItem = oEvent.getParameter("item");
            this.byId("pageContainer").to(this.getView().createId(oItem.getKey()));
        },

        handleUserNamePress: function (event) {
            var oPopover = new Popover({
                showHeader: false,
                placement: PlacementType.Bottom,
                content: [
                    new Button({
                        text: 'Feedback',
                        type: ButtonType.Transparent
                    }),
                    new Button({
                        text: 'Help',
                        type: ButtonType.Transparent
                    }),
                    new Button({
                        text: 'Logout',
                        type: ButtonType.Transparent
                    })
                ]
            }).addStyleClass('sapMOTAPopover sapTntToolHeaderPopover');

            oPopover.openBy(event.getSource());
        },

        onSideNavButtonPress: function () {
            var oToolPage = this.byId("toolPage");
            var bSideExpanded = oToolPage.getSideExpanded();

            this._setToggleButtonTooltip(bSideExpanded);

            oToolPage.setSideExpanded(!oToolPage.getSideExpanded());
        },

        _setToggleButtonTooltip: function (bLarge) {
            var oToggleButton = this.byId('sideNavigationToggleButton');
            if (bLarge) {
                oToggleButton.setTooltip('Large Size Navigation');
            } else {
                oToggleButton.setTooltip('Small Size Navigation');
            }
        }

    });
});