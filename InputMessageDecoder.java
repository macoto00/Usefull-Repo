package cz.skodaauto.ekkv.shiftmanagement.usecases;

import cz.skodaauto.ekkv.common.JsonConverter;
import cz.skodaauto.ekkv.common.model.EmptySpecification;
import lombok.NonNull;

public class InputMessageDecoder {

    /**
     * Automatically decodes payload:
     * - If target is EmptySpecification and payload is blank, returns new instance.
     * - If payload starts with '{', it's decoded directly.
     * - Otherwise, it's decoded twice (String, then actual target).
     */
    public <T> T decode(@NonNull String payload, @NonNull Class<T> targetClass) {
        var trimmed = payload.trim();

        if (EmptySpecification.class.equals(targetClass) && trimmed.isEmpty()) {
            return targetClass.cast(new EmptySpecification());
        }

        if (trimmed.startsWith("{")) {
            return JsonConverter.fromJson(trimmed, targetClass)
                    .orElseThrow(() -> new IllegalStateException("Decoding to " + targetClass.getSimpleName() + " failed"));
        } else {
            var decodedJson = JsonConverter.fromJson(trimmed, String.class)
                    .orElseThrow(() -> new IllegalStateException("First decoding (to String) failed for " + targetClass.getSimpleName()));
            return JsonConverter.fromJson(decodedJson, targetClass)
                    .orElseThrow(() -> new IllegalStateException("Second decoding (to " + targetClass.getSimpleName() + ") failed"));
        }
    }
}
