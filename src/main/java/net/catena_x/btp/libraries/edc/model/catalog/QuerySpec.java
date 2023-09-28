package net.catena_x.btp.libraries.edc.model.catalog;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuerySpec {
    private int offset = 0;
    private int limit = 1000;
    private FilterExpression filterExpression = null;
}
