package net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CLSBody {
    private CLSCounts counts;
    private List<CLSClass> classes;
}
