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
    public static final Context DEFAULT_CONTEXT_ASSET =
            new Context(ContextElement.EDC_V0_0_1, null, null, null, null, null, null);

    public static final Context DEFAULT_CONTEXT_POLICY =
            new Context(ContextElement.EDC_V0_0_1, null, null, null, null, ContextElement.ODRL_2, null);

    public static final Context DEFAULT_CONTEXT_CONTRACT =
            new Context(ContextElement.EDC_V0_0_1, null, null, null, null, null, null);

    public static final Context DEFAULT_CONTEXT_CATALOG =
            new Context(ContextElement.EDC_V0_0_1, null, null, null, null, null, null);

    public static final Context DEFAULT_CONTEXT_CATALOG_RESULT =
            new Context(null, ContextElement.DCT_TERMS, ContextElement.TRACTUS_X, ContextElement.EDC_V0_0_1,
                    ContextElement.DCAT, ContextElement.ODRL_2, ContextElement.DSPACE_V0_8);

    public static final Context DEFAULT_CONTEXT_CONTRACT_NEGOTIATION =
            new Context(ContextElement.EDC_V0_0_1, null, null, null, null,
                    ContextElement.ODRL_2, null);

    public static final Context DEFAULT_CONTEXT_TRANSFER =
            new Context(ContextElement.EDC_V0_0_1, null, null, null, null,
                    ContextElement.ODRL_2, null);

    @JsonProperty("@vocab")
    private ContextElement vocab = null;

    private ContextElement dct = null;
    private ContextElement tx = null;
    private ContextElement edc = null;
    private ContextElement dcat = null;
    private ContextElement odrl = null;
    private ContextElement dspace = null;
}
