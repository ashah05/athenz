import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.DividerBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificationToSlackConverterCommon {

    public static List<LayoutBlock> generateBlocksFromJsonTemplate(Map<String, String> metaDetails,
                                                                   String jsonTemplate) {
        List<LayoutBlock> blocks = new ArrayList<>();

        // Parse the JSON template and retrieve required fields
        String domain = metaDetails.get("domain");
        String tableRows = metaDetails.get("tableRows");
        String athenzUiUrl = metaDetails.get("athenzUiUrl");

        // Parse the JSON template and generate Slack blocks
        JsonNode rootNode = JsonUtil.fromJson(jsonTemplate, JsonNode.class);
        if (rootNode != null && rootNode.has("blocks") && rootNode.get("blocks").isArray()) {
            ArrayNode blockArray = (ArrayNode) rootNode.get("blocks");
            for (JsonNode blockNode : blockArray) {
                LayoutBlock block = generateLayoutBlock(blockNode, domain, tableRows, athenzUiUrl);
                if (block != null) {
                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

    private static LayoutBlock generateLayoutBlock(JsonNode blockNode, String domain, String tableRows, String athenzUiUrl) {
        String type = JsonUtil.getAsString(blockNode, "type");

        if ("section".equals(type)) {
            return generateSectionBlock(blockNode, domain);
        } else if ("divider".equals(type)) {
            return new DividerBlock();
        }

        return null;
    }

    private static SectionBlock generateSectionBlock(JsonNode blockNode, String domain) {
        String text = JsonUtil.getAsString(blockNode, "text");
        if (text != null) {
            text = text.replace("{domain}", domain);
        }

        return SectionBlock.builder()
                .text(MarkdownTextObject.builder()
                        .text(text)
                        .build())
                .build();
    }

    // Your existing getSlackUserIds method
    public static Set<String> getSlackUserIds(Set<String> recipients) {
        // ...
    }
}
