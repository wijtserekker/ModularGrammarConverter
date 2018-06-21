package nl.utwente.modgram.model;

import java.util.Collection;
import java.util.HashMap;

public class ModularGrammar {

    private HashMap<String, Module> modules = new HashMap<>();

    public ModularGrammar() {

    }

    public void addModule(Module module) {
        modules.put(module.getName(), module);
    }

    public Module getModule(String name) {
        return modules.get(name);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Module module : modules.values()) {
            result.append(module.toString()).append("\n");
        }
        return result.toString();
    }

    public Collection<Module> getModules() {
        return modules.values();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ModularGrammar) {
            ModularGrammar modularGrammar = (ModularGrammar) o;
            return this.modules.values().containsAll(modularGrammar.getModules())
                    && this.modules.values().size() == modularGrammar.getModules().size();
        }
        return false;
    }
}
