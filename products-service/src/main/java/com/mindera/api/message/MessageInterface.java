package com.mindera.api.message;

import com.mindera.api.enums.Queues;

public interface MessageInterface {

    boolean canHandle(Queues queue);

    void processMessage(PromotionMessage promotionMessage);

}
