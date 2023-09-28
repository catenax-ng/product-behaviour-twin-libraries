package net.catena_x.btp.libraries.edc.model.general;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Context {
    public static final Context DEFAULT_CONTEXT_ASSET = new Context(Vocab.EDC_V0_0_1, null);
    public static final Context DEFAULT_CONTEXT_POLICY = new Context(Vocab.EDC_V0_0_1, Odrl.ODRL_2);
    public static final Context DEFAULT_CONTEXT_CONTRACT = new Context(Vocab.EDC_V0_0_1, null);
    public static final Context DEFAULT_CONTEXT_CATALOG = new Context(Vocab.EDC_V0_0_1, null);

    @JsonProperty("@vocab")
    private Vocab vocab = null;

    private Odrl odrl = null;
}
