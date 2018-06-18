package nl.utwente.modgram.model;

import java.util.HashMap;

public class ModularGrammar {

    private HashMap<String, Module> modules = new HashMap<>();

    public ModularGrammar() {

    }

    public void addModule(Module module) {
        modules.put(module.getName(), module);
    }

}
