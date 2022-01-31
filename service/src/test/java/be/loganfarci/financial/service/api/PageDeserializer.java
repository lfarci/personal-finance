package be.loganfarci.financial.service.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.data.domain.PageImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PageDeserializer<T> extends StdDeserializer<PageImpl<T>> {

    public PageDeserializer() {
        this(null);
    }

    public PageDeserializer(Class<?> vc) {
        super(vc);
    }

    protected T deserialize(JsonNode node) {
        return null;
    }

    @Override
    final public PageImpl<T> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        ArrayNode content = (ArrayNode) root.get("content");
        List<T> children = new ArrayList<>();
        content.forEach(node -> children.add(deserialize(node)));
        return new PageImpl<T>(children);
    }

}
