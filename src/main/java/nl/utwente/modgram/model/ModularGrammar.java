package nl.utwente.modgram.model;

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

    public Iterable<? extends Module> getModules() {
        return modules.values();
    }
}
