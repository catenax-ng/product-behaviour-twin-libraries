package net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CLSBody {
    private CLSCounts counts;
    private List<CLSClass> classes;
}
