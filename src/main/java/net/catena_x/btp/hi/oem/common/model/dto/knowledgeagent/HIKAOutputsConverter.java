package net.catena_x.btp.hi.oem.common.model.dto.knowledgeagent;

import net.catena_x.btp.hi.oem.common.model.dao.knowledgeagent.*;
import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Stream;

@Component
public final class HIKAOutputsConverter extends DAOConverter<HIKAOutputsDAO, HIKAOutputs> {
    protected HIKAOutputs toDTOSourceExists(@NotNull final HIKAOutputsDAO source) {
        final HIKAOutputs outputs = new HIKAOutputs();

        int count = getCount(source);
        outputs.setResults(new ArrayList<>(count));

        if(count > 0) {
            for (final HIKAOutputBindingDAO binding: source.getResults().getBindings()) {
                outputs.getResults().add(new HIKAOutput(getVan(binding), getValues(binding)));
            }
        }

        return outputs;
    }

    protected HIKAOutputsDAO toDAOSourceExists(@NotNull final HIKAOutputs source) {
        final HIKAOutputsDAO outputs = new HIKAOutputsDAO();

        outputs.setHead(new HIKAHeaderDAO());
        outputs.getHead().setVars(new ArrayList<>());
        outputs.getHead().getVars().add("van");
        outputs.getHead().getVars().add("aggregate");
        outputs.getHead().getVars().add("healthType");
        outputs.getHead().getVars().add("health");

        int count = getCount(source);

        outputs.setResults(new HIKAResultsDAO<>());
        outputs.getResults().setBindings(new ArrayList<>(count));

        if(count > 0) {
            for (final HIKAOutput sourceOutput: source.getResults()) {
                addBinding(outputs, sourceOutput.getVan(), "Differential Gear",
                        "GearSet", sourceOutput.getHealthIndicatorValues());
            }
        }

        return outputs;
    }

    private void addBinding(@NotNull final HIKAOutputsDAO outputs, @NotNull final String van,
                            @NotNull final String aggregate, @NotNull final String healthType,
                            @NotNull final double[] health) {
        outputs.getResults().getBindings().add(new HIKAOutputBindingDAO(
                new HIKAVariableDAO("literal", van, null),
                new HIKAVariableDAO("literal", aggregate, null),
                null,
                new HIKAVariableDAO("literal", healthType, null),
                null, null, null,
                new HIKAVariableDAO("literal", convertToJson(health), null)));
    }

    private int getCount(@NotNull final HIKAOutputsDAO source) {
        if(source.getResults() == null) {
            return 0;
        }

        if(source.getResults().getBindings() == null) {
            return 0;
        }

        return source.getResults().getBindings().size();
    }

    private int getCount(@NotNull final HIKAOutputs source) {
        if(source.getResults() == null) {
            return 0;
        }

        return source.getResults().size();
    }

    private String getVan(@NotNull final HIKAOutputBindingDAO binding) {
        if(binding.getVan() == null) {
            return null;
        }

        return binding.getVan().getValue();
    }

    private double[] getValues(@NotNull final HIKAOutputBindingDAO binding) {
        if(binding.getHealth() == null) {
            return null;
        }

        if(binding.getHealth().getValue() == null) {
            return null;
        }

        return convertFromJson(binding.getHealth().getValue());
    }

    private double[] convertFromJson(@NotNull final String values) {
        return Stream.of(values.replace("[", "").replace("]", "")
                .replace(" ", "").split(",")).mapToDouble (Double::parseDouble).toArray();
    }

    private String convertToJson(@NotNull final double[] values) {
        if(values == null) {
            return null;
        }

        if(values.length != 2) {
            return "ERROR in value length, must be 2!";
        }

        return String.format(Locale.US, "[%f, %f]", values[0], values[1]);
    }
}