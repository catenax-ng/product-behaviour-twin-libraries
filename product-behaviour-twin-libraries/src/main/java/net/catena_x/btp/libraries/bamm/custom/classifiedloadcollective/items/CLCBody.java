package net.catena_x.btp.libraries.bamm.custom.classifiedloadcollective.items;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CLCBody {
    private CLCCounts counts;
    private List<CLCClass> classes;
}
