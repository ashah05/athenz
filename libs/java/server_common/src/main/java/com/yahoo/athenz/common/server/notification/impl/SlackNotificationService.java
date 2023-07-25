import com.slack.api.Slack;
import com.slack.api.methods.MethodCompletionException;
import com.slack.api.methods.MethodResult;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.api.model.block.element.ButtonElement;
import com.slack.api.model.block.element.RichTextElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SlackNotificationService implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackNotificationService.class);

    private final String slackToken;
    private final String channel;

    public SlackNotificationService(String slackToken, String channel) {
        this.slackToken = slackToken;
        this.channel = channel;
    }

    @Override
    public boolean notify(Notification notification) {
        if (notification == null) {
            return false;
        }

        NotificationMessage notificationAsMessage = notification.getNotificationAsEmail();

        if (notificationAsMessage == null) {
            return false;
        }

        final String subject = notificationAsMessage.getSubject();
        final String body = notificationAsMessage.getBody();

        if (sendMessage(text, actionText)) {
            LOGGER.info("Successfully sent Slack notification. Text={}", text);
            return true;
        } else {
            LOGGER.error("Failed sending Slack notification. Text={}", text);
            return false;
        }
    }

    boolean sendMessage(String text, String actionText) {
        Slack slack = Slack.getInstance();

        List<LayoutBlock> blocks = new ArrayList<>();
        blocks.add(
                SectionBlock.builder()
                        .text(MarkdownTextObject.builder().text(text).build())
                        .build()
        );

        if (actionText != null) {
            blocks.add(
                    SectionBlock.builder()
                            .accessory(
                                    ButtonElement.builder()
                                            .text(PlainTextObject.builder().text(actionText).build())
                                            .build()
                            )
                            .build()
            );
        }

        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .token(slackToken)
                .channel(channel)
                .blocks(blocks)
                .build();

        try {
            MethodResult<ChatPostMessageResponse, SlackError> result = slack.methods().chatPostMessage(request);
            if (result.isSuccessful()) {
                return true;
            } else {
                LOGGER.error("Failed sending Slack message. Error: {}", result.getError());
            }
        } catch (IOException | MethodCompletionException e) {
            LOGGER.error("Failed sending Slack message. Error: {}", e.getMessage());
        }

        return false;
    }
}
