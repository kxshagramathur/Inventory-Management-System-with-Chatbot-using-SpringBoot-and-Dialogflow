//package com.trial.proj.dialogflow;
//
//import com.google.cloud.dialogflow.v2.WebhookRequest;
//import com.google.cloud.dialogflow.v2.WebhookResponse;
//import com.google.protobuf.Struct;
//import com.google.protobuf.Value;
//import com.trial.proj.model.InventoryItem;
//import com.trial.proj.service.InventoryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.http.MediaType;
//
//import java.util.Map;
//
//import com.google.protobuf.util.JsonFormat;
//
//@RestController
//@RequestMapping("/dialogflow")
//public class DialogflowController {
//
//    @Autowired
//    private InventoryService inventoryService;
//
//    @PostMapping(value = "/webhook", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public WebhookResponse handleDialogflowWebhook(@RequestBody String jsonString) throws Exception {
//        WebhookRequest.Builder requestBuilder = WebhookRequest.newBuilder();
//        JsonFormat.parser().merge(jsonString, requestBuilder);
//
//        WebhookRequest request = requestBuilder.build();
//        String intentName = request.getQueryResult().getIntent().getDisplayName();
//
//        WebhookResponse.Builder responseBuilder = WebhookResponse.newBuilder();
//
//        if ("phone_recommendation intent".equals(intentName)) { // Ensure intent name matches
//            Struct parameters = request.getQueryResult().getParameters();
//            Map<String, Value> fields = parameters.getFieldsMap();
//            String category = fields.containsKey("category") ? fields.get("category").getStringValue() : "phone"; // Default to "phone" if missing
//            InventoryItem item = inventoryService.getRandomItemByCategory(category);
//            if (item != null) {
//                String fulfillmentText = String.format("I recommend the %s: %s. It costs $%.2f.",
//                        item.getCategory(), item.getProd_name(), item.getPrice());
//                responseBuilder.setFulfillmentText(fulfillmentText);
//            } else {
//                responseBuilder.setFulfillmentText("Sorry, I couldn't find any products in that category.");
//            }
//        }
//
//        return responseBuilder.build();
//    }
//}
