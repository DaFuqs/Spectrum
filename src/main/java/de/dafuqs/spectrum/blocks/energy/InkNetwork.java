package de.dafuqs.spectrum.blocks.energy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class InkNetwork {
    protected static List<InkNetwork> networks = new ArrayList<>();

    protected HashSet<InkDuctBlockEntity> inkDucts = new HashSet<>();

    public boolean canConnect(InkDuctBlockEntity newDuct){
        for (InkDuctBlockEntity currentDuct: inkDucts){
            currentDuct.canSee(newDuct);
        }
        return false;
    }
}
