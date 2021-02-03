package panda.host.model.models;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

public class ClientProcess {
    String id;
    boolean alive;
    Timestamp dateHasRegistered;
    Timestamp dateHasUnregistered;

    public ClientProcess(String id) {
        this.id = id;
        this.dateHasRegistered = Timestamp.from(Instant.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientProcess process = (ClientProcess) o;
        return id.equals(process.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getId() {
        return id;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill(){
        // Matter of fact, I wan
        alive = false;
        dateHasUnregistered = Timestamp.from(Instant.now());
    }
}
